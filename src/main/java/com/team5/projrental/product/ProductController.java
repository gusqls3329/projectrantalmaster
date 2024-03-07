package com.team5.projrental.product;

import com.team5.projrental.common.Const;
import com.team5.projrental.common.exception.ErrorMessage;
import com.team5.projrental.common.model.ResVo;
import com.team5.projrental.product.model.*;
import com.team5.projrental.product.model.review.ReviewResultVo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

import static com.team5.projrental.common.exception.ErrorMessage.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/prod")
public class ProductController {

    //    private final RefProductService productService;
    private final ProductService productService;

    @Operation(summary = "해당 카테고리의 전체 제품 목록",
            description = "<strong>해당 카테고리의 전체 제품 목록</strong><br>" +
                          "[ [v] : 필수값 ]<br>" +
                          "search: 검색어 조회 (특정 단어가 제목에 포함된 제품목록 조회<br><br>" +
                          "[v] mc: 조회할 메인 카테고리 숫자<br>" +
                          "sc: 조회할 서브 카테고리 숫자<br>" +
                          "<br><br>" +
                          "성공시:<br>" +
                          "[<br>" +
                          "  count : (조건에 맞는 전체 제품 갯수)<br>" +
                          "<br><br>" +
                          "실패시:<br>" +
                          "message: 에러 발생 사유<br>errorCode: 에러 코드")
    @Validated
    @GetMapping("/count")
    public ResVo getProdCount(@RequestParam(required = false)
                              @Length(min = 1, message = ILLEGAL_RANGE_EX_MESSAGE)
                              String search,
                              @RequestParam(value = "mc", required = false)
                              @Min(value = 1, message = ILLEGAL_RANGE_EX_MESSAGE)
                              Integer imainCategory,
                              @RequestParam(name = "sc", required = false)
                              @Min(value = 1, message = ILLEGAL_RANGE_EX_MESSAGE)
                              Integer isubCategory,
                              @RequestParam(required = false)
                              String addr) {
        return productService.getProdCount(search, imainCategory, isubCategory, addr);
    }

    @Operation(summary = "특정 제품의 리스트 의 개수 조회 ",
            description = "<strong></strong><br>" +
                          "[ [v] : 필수값 ]<br>" +
                          "[ [v] page: 페이징<br> " +
                          "iuser : 유저pk<br><br>" +
                          "<br><br>" +
                          "성공시:<br>" +
                          "[<br>" +
                          "  count : (개수)<br>" +
                          "<br><br>" +
                          "실패시:<br>" +
                          "message: 에러 발생 사유<br>errorCode: 에러 코드")
    @Validated
    @GetMapping("/list/count")
    public ResVo getUserProductCount(@RequestParam(required = false)
                                     @Min(value = 1, message = ILLEGAL_RANGE_EX_MESSAGE)
                                     Integer iuser) {
        return productService.getUserProductCount(iuser);
    }


    @Operation(summary = "전체 작성받은 리뷰수",
            description = "<strong>전체 작성리뷰수/strong><br>" +
                          "[ [v] : 필수값 ]<br>" +
                          "[v]iproduct: 상품 pk<br> " +
                          "성공시: <br>" +
                          "result: count(전체리뷰수)<br><br>" +
                          "실패시:<br>" +
                          "message: 에러 발생 사유<br>errorCode: 에러 코드")
    @Validated
    @GetMapping("/review/count")
    public ResVo getReviewCount(@RequestParam(required = false)
                                Long iproduct) {
        return productService.getReviewCount(iproduct);
    }


    @Operation(summary = "메인페이지용 카테고리별 상품 (8개씩 조회)",
            description = "<strong>메인페이지용 카테고리별 상품 (8개씩 조회)</strong><br>" +
                          "[ [v] : 필수값 ]<br>" +
                          "[v] mc: 조회할 메인 카테고리 PK<br>" +
                          "[v] sc: 조회할 서브 카테고리 PK<br>" +
                          "    ㄴ> 쿼리 파라미터로 제공 & 구분자 ',' 또는 &로 구분하여 여러개 전송 가능" +
                          "    ㄴ>/api/prod/main?c=1,2,3,4 또는 /api/prod/main?c=1&c=2&c=3&c=4" +
                          "<br><br>" +
                          "성공시: <br>" +
                          "result: 1<br><br>" +
                          "실패시:<br>" +
                          "message: 에러 발생 사유<br>errorCode: 에러 코드")
    @Validated
    @GetMapping("/main")
    public List<ProductListVo> getMainPage(@RequestParam("mc")
                                           @Size(min = 1, max = 5)
                                           List<Integer> imainCategory,
                                           @RequestParam("sc")
                                           @Size(min = 1, max = 5)
                                           List<Integer> isubCategory) {

        return productService.getProductListForMain(imainCategory, isubCategory);


    }

    @Operation(summary = "특정 카테고리의 제품목록 가져오기",
            description = "<strong>제품목록 조회</strong><br>" +
                          "[ [v] : 필수값 ]<br>" +
                          "sort: 정렬 기준<br> " +
                          "1: like 순으로 조회<br>" +
                          "2: 조회 많은순으로 보기 <br>" +
                          "sort 가 제공되지 않으면 최신순으로 조회<br><br>" +
                          "search: 검색어 조회 (특정 단어가 제목에 포함된 제품목록 조회<br><br>" +
                          "[v] icategory: 조회할 카테고리, <strong>필수값</strong><br><br>" +
                          "[v] page: 페이징<br>" +
                          "    addr: 주소로 검색시 (사용자 입력 검색이 아님 주의)<br>" +
                          "<br><br>" +
                          "성공시:<br>" +
                          "[<br>" +
                          "  iuser(판매자의)<br>" +
                          "  nick(판매자의)<br> " +
                          "  userPic(판매자의)<br>" +
                          "  iproduct<br> " +
                          "  title<br> " +
                          "  prodPic<br> " +
                          "  rentalPrice<br> " +
                          "  rentalStartDate<br>" +
                          "  rentalEndDate<br> " +
                          "  addr(rest_addr 까지 포함)<br> " +
                          "  prodLike(제품의 좋아요 수)<br>" +
                          "] (배열)<br>" +
                          "istatus: 해당 제품의 상태 -> 0: 활성화 됨, -1: 삭제됨, -2: 숨김<br>" +
                          "inventory: 해당 상품의 물품 총 보유량 (재고 - '전체 재고')" +
                          "<br><br>" +
                          "실패시:<br>" +
                          "message: 에러 발생 사유<br>errorCode: 에러 코드" +
                          "<br><br><br>" +
                          "3차 추가: addr 검색 기능 추가 - 주소 기반 검색<br>" +
                          "시, 군, 구 등의 구분은 ' 공백 ' 혹은 ' _ '(언더스코어) 로 구분.<br>" +
                          "광역시, 특별시 등의 명칭은 제외하고 보내야함.<br><br>" +
                          "<strong>" +
                          "ex) <br>" +
                          "대구 달서구 용산동 -> GOOD <br>" +
                          "대구_달서구_용산동 -> GOOD (언더스코어 포함 => 스웨거에서는 잘 표현이 안되지만, 언더스코어 로 구분된 예시임.) <br>" +
                          "대구_달서구 -> GOOD (언더스코어 포함 => 스웨거에서는 잘 표현이 안되지만, 언더스코어 로 구분된 예시임.) <br>" +
                          "대구 -> GOOD<br><br>" +
                          "대구광역시 달서구 용산동 -> BAD <br>" +
                          "대구광역시_달서구_용산동 -> BAD (언더스코어 포함 => 스웨거에서는 잘 표현이 안되지만, 언더스코어 로 구분된 예시임.) <br>" +
                          "대구광역시 달서구 -> BAD <br>" +
                          "대구광역시 -> BAD <br>" +
                          " </strong> ")
    @Validated
    @GetMapping
    public List<ProductListVo> getProductList(@RequestParam(name = "sort", required = false)
                                              @Range(min = 1, max = 2, message = BAD_SORT_EX_MESSAGE)
                                              Integer sort,
                                              @RequestParam(name = "search", required = false)
                                              @Length(min = 2, message = ILLEGAL_RANGE_EX_MESSAGE)
                                              String search,
                                              @RequestParam("page")
                                              @NotNull(message = CAN_NOT_BLANK_EX_MESSAGE)
                                              @Min(value = 1, message = ILLEGAL_RANGE_EX_MESSAGE)
                                              Integer page,
                                              @RequestParam(name = "mc", required = false)
                                              @Min(value = 1, message = ILLEGAL_RANGE_EX_MESSAGE)
                                              Integer imainCategory,
                                              @RequestParam(name = "sc", required = false)
                                              @Min(value = 1, message = ILLEGAL_RANGE_EX_MESSAGE)
                                              Integer isubCategory,
                                              @RequestParam(required = false)
                                              String addr) {

        return productService.getProductList(
                sort,
                search,
                imainCategory == null ? 0 : imainCategory,
                isubCategory == null ? 0 : isubCategory,
                addr,
                (page - 1) * Const.PROD_PER_PAGE,
                Const.PROD_PER_PAGE);
    }

    //

    @Operation(summary = "특정 상품 상세 조회",
            description = "<strong>특정 상품의 상세페이지 조회</strong><br>" +
                          "[ [v] : 필수값]<br>" +
                          "[v] icategory: 조회할 상품의 카테고리<br>" +
                          "[v] iproduct: 조회할 상품의 iproduct 값 (getProductList 에서 제공됨)<br><br>" +
                          "성공시:<br>" +
                          "iuser(판매자의)<br> " +
                          "nick(판매자의)<br> " +
                          "userPic(판매자의)<br> " +
                          "iauth(판매자의><br>" +
                          "title<br> " +
                          "contents<br> " +
                          "prodMainPic<br>" +
                          "prodSubPics<br> - 배열, pk와 사진이름 으로 구성. (수정에서 사진 삭제시 delPics 에 pk 전송) " +
                          "pics(배열)<br> " +
                          "deposit<br> " +
                          "buyDate<br> " +
                          "rentalStartDate<br> " +
                          "rentalEndDate<br> " +
                          "addr (rest_addr 까지 포함)<br> " +
                          "prodLike<br> " +
                          "x<br> " +
                          "y<br> " +
                          "isLiked(로그인한 유저가 좋아요를 눌렀는지 여부 - 0: 누르지 않음, 1: 누름<br> " +
                          "reviews: 해당 제품에 작성된 리뷰<br>" +
                          "disabledDates: 이미 거래진행중인 날짜들 (거래 불가능한 날짜들) <br>" +
                          "istatus: 해당 제품의 상태 -> 0: 활성화 됨, -1: 삭제됨, -2: 숨김<br>" +
                          "inventory: 해당 상품의 물품 총 보유량 (재고 - '전체 재고')" +
                          "<br><br>" +
                          "실패시: <br>" +
                          "message: 에러 발생 사유<br>errorCode: 에러 코드")
    @Validated
    @GetMapping("/{main-icategory}/{sub-icategory}/{iproduct}")
    public ProductVo getProduct(@PathVariable("main-icategory")
                                @NotNull(message = CAN_NOT_BLANK_EX_MESSAGE)
                                @Min(value = 1, message = ILLEGAL_RANGE_EX_MESSAGE)
                                int imainCategory,
                                @PathVariable("sub-icategory")
                                @NotNull(message = CAN_NOT_BLANK_EX_MESSAGE)
                                @Min(value = 1, message = ILLEGAL_RANGE_EX_MESSAGE)
                                int isubCategory,
                                @PathVariable
                                @NotNull(message = CAN_NOT_BLANK_EX_MESSAGE)
                                @Min(value = 1, message = ILLEGAL_RANGE_EX_MESSAGE)
                                Long iproduct) {
        return productService.getProduct(imainCategory, isubCategory, iproduct);
    }

    /*
    pics + mainPic 개수 검증 - 10개 이하 -> iuser 가 존재하는지 검증 -> category 존재여부 검증 ->
    price 양수 검증 -> buyDate 오늘보다 이전인지 검증 -> depositPer 70 이상 100 이하 검증 ->
    오늘이 rentalStartDate 보다 이전이 아닌지 검증 -> rentalEndDate 가 rentalStartDate 보다 이전이 아닌지 검증 ->
    depositPer 를 price 기준 퍼센트 금액으로 환산 ->  본 로직
    -> addr + restAddr 기준으로 x, y 좌표 획득 -> insert model 객체 생성 -> insert
     */

    @Operation(summary = "제품 등록",
            description = "<strong>제품 등록</strong><br>" +
                          "[ [v] : 필수값 ]<br>" +
                          "[v] title: 제품의 제목 -> null, \"\", \" \" 불가능<br>" +
                          "[v] contents: 제품 내용 -> null, \"\", \" \" 불가능<br>" +
                          "[v] addr: 거래 가능 주소 -> null, \"\", \" \" 불가능<br>" +
                          "[v] restAddr: 거래 가능 나머지 주소 -> null, \"\", \" \" 불가능<br>" +
                          "[v] mainPic: 대표사진<br>" +
                          "pic: 나머지 사진<br>" +
                          "[v] price: 자신이 평가한 제품의 절대가격 -> null 불가능<br>" +
                          "[v] rentalPrice: 1일당 대여 가격 -> null 불가능<br>" +
                          "[v] depositPer: 보증금 가격 비율 (price 기준 비율) -> null 불가능<br>" +
                          "[v] buyDate: 자신이 제품을 구매한 날짜<br>" +
                          "[v] rentalStartDate: 대여 시작 가능 날짜<br>" +
                          "[v] rentalEndDate: 대여 마감 날짜<br>" +
                          "[v] icategory: 카테고리 (숫자) -> null 불가능, 1 이상<br>" +
                          "[v] inventory: 해당 재품 총 보유량 (전체 재고)" +
                          "<br><br>" +
                          "성공시: <br>" +
                          "result: 등록된 제품의 PK" +
                          "<br><br>" +
                          "실패시:<br>" +
                          "message: 에러 발생 사유<br>errorCode: 에러 코드")
    @Validated
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResVo postProduct(@RequestPart
                             @NotNull(message = ErrorMessage.CAN_NOT_BLANK_EX_MESSAGE)
                             MultipartFile mainPic,
                             @RequestPart(required = false)
                             @Size(max = 9)
                             List<MultipartFile> pics,
                             @Validated
                             @RequestPart
                             ProductInsDto dto) {

        return productService.postProduct(mainPic, pics, dto);


    }

    @Operation(summary = "특정 제품 수정",
            description = "<strong>특정 제품 수정</strong><br>" +
                          "[ [v] : 필수값 -> 필수값이 아닌 데이터들은 수정할 경우에 수정된 데이터만 제공, 수정되지 않은 데이터는 제공하지 않아도 됨.]<br>" +
                          "[v] iproduct: 수정할 제품의 PK<br>" +
                          "icategory: 수정할 카테고리 (숫자) -> null 불가능, 1 이상<br>" +
                          "addr: 수정할 거래 가능 주소<br>" +
                          "restAddr: 수정할 거래 가능 나머지 주소<br>" +
                          "title: 수정할 제품 제목<br>" +
                          "contents: 수정할 제품 내용<br>" +
                          "mainPic: 수정할 대표 사진<br>" +
                          "pic: 수정할 사진<br>" +
                          "price: 수정할 제품 가격<br>" +
                          "rentalPrice: 1일당 대여 가격<br>" +
                          "depositPer: 수정할 보증금 가격 비율 (price 가 수정되면 수정된 price 기준, 수정되지 않으면 이미 저장된 price 기준<br>" +
                          "buyDate: 수정할 자신이 제품을 구매한 날짜<br>" +
                          "inventory: 해당 재품 총 보유량 (전체 재고)<br>" +
                          "rentalStartDate: 수정할 대여 시작 가능 날짜<br>" +
                          "rentalEndDate: 수정할 대여 마감 날짜<br>" +
                          "delPics: 삭제된 사진들의 pk들 -> 제품 조회시 사진의 pk들이 제공됨. <br>  " +
                          "  ㄴ> 메인사진은 삭제는 불가능하고 변경만 가능함으로 delPics 에 기재하지 " +
                          "않고, 변경된" +
                          " 사진만 넘기면 됨." +
                          "<br><br>" +
                          "성공시: <br>" +
                          "result: 1<br><br>" +
                          "실패시: <br>" +
                          "message: 에러 발생 사유<br>errorCode: 에러 코드")
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResVo putProduct(@RequestPart(required = false) MultipartFile mainPic,
                            @RequestPart(required = false) List<MultipartFile> pics,
                            @Validated @RequestPart ProductUpdDto dto) {

        return productService.putProduct(mainPic, pics, dto);
    }

    @Operation(summary = "제품 숨김 or 삭제",
            description = "<strong>제품의 수정 또는 삭제 요청</strong><br>" +
                          "[ [v] : 필수값 ]<br>" +
                          "[v] iproduct: 수정 또는 삭제 요청할 제품의 PK<br>" +
                          "[v] div: 삭제 또는 수정 요청 식별값<br>" +
                          "     ㄴ> 1: 삭제 요청, 2: 숨김 요청" +
                          "<br><br>" +
                          "성공시: <br>" +
                          "result: 1<br><br>" +
                          "실패시:<br>" +
                          "message: 에러 발생 사유<br>errorCode: 에러 코드")
    @Validated
    @DeleteMapping("/{iproduct}")
    public ResVo delProduct(@PathVariable
                            @NotNull(message = CAN_NOT_BLANK_EX_MESSAGE)
                            @Min(value = 1, message = ILLEGAL_RANGE_EX_MESSAGE)
                            Long iproduct,
                            @RequestParam
                            @NotNull(message = CAN_NOT_BLANK_EX_MESSAGE)
                            @Range(min = 1, max = 2, message = ILLEGAL_RANGE_EX_MESSAGE)
                            Integer div) {
        return productService.delProduct(iproduct, div);
    }

    @Operation(summary = "특정 유저의 모든 제품 목록 조회",
            description = "<strong>특정 유저의 모든 제품목록 조회</strong><br>" +
                          "[ [v] : 필수값]<br>" +
                          "iuser: 대상 유저의 PK값 (제공되지 않을시 기본적으로 로그인 유저의 모든 제품이 조회됨)<br>" +
                          "[v] page: 페이징" +
                          "<br><br>" +
                          "성공시: <br>" +
                          "[<br>" +
                          "  nick<br> " +
                          "  userPic<br> " +
                          "  iproduct<br> " +
                          "  category<br> " +
                          "  title<br> " +
                          "  prodPic<br> " +
                          "  rentalPrice<br> " +
                          "  rentalStartDate<br> " +
                          "  rentalEndDate<br> " +
                          "  addr (rest_addr 까지 포함)<br>" +
                          "] (배열) <br><br>" +
                          "실패시: <br>" +
                          "message: 에러 발생 사유<br>errorCode: 에러 코드<br><br><br>" +
                          "<strong>로그인 유저의 상품을 조회할때는 iuser 를 제공하지 않아야 함. 그래야 숨김까지 결과에 포함됨.</strong>")
    @Validated
    @GetMapping("/list")
    public List<ProductUserVo> getUserProductList(@RequestParam(required = false)
                                                  @Min(value = 1, message = ILLEGAL_RANGE_EX_MESSAGE)
                                                  Long iuser,
                                                  @RequestParam
                                                  @NotNull(message = CAN_NOT_BLANK_EX_MESSAGE)
                                                  @Min(value = 1, message = ILLEGAL_RANGE_EX_MESSAGE)
                                                  @NotNull Integer page) {
        return productService.getUserProductList(iuser, (page - 1) * Const.PROD_PER_PAGE);

    }

    @Operation(summary = "해당 제품에 작성된 모든 리뷰",
            description = "성공시:<br>" +
                          "[<br>" +
                          "  ireview: 리뷰의 PK<br>" +
                          "  contents: 리뷰 내용<br>" +
                          "  rating: 평가한 별점 (0~5)<br>" +
                          "  iuser: 리뷰를 작성한 유저의 PK<br>" +
                          "  nick: 리뷰를 작성한 유저의 닉네임<br>" +
                          "  profPic: 리뷰를 작성한 유저의 사진<br>" +
                          "] (배열)<br><br>" +
                          "실패시:<br>" +
                          "message: 에러 발생 사유<br>errorCode: 에러 코드")
    @Validated
    @GetMapping("/review/{iproduct}")
    public List<ReviewResultVo> getAllReviews(@PathVariable
                                              @NotNull(message = CAN_NOT_BLANK_EX_MESSAGE)
                                              @Min(value = 1, message = ILLEGAL_RANGE_EX_MESSAGE)
                                              Long iproduct,
                                              @RequestParam
                                              @NotNull(message = CAN_NOT_BLANK_EX_MESSAGE)
                                              @Min(value = 1, message = ILLEGAL_RANGE_EX_MESSAGE)
                                              Integer page) {
        return productService.getAllReviews(iproduct, (page - 1) * Const.TOTAL_REVIEW_PER_PAGE);
    }


    @Operation(summary = "월별 대여 불가능한 날짜들 조회",
            description = "성공시:<br>" +
                          "[<br>" +
                          "  거래 불가능한 개별 날짜들 (yyyy-MM-dd)" +
                          "] (배열)<br><br>" +
                          "실패시:<br>" +
                          "message: 에러 발생 사유<br>errorCode: 에러 코드")
    @Validated
    @GetMapping("/disabled-date/{iproduct}")
    public List<LocalDate> getDisabledDate(@PathVariable
                                           @NotNull(message = CAN_NOT_BLANK_EX_MESSAGE)
                                           @Min(value = 1, message = ILLEGAL_RANGE_EX_MESSAGE)
                                           Long iproduct,
                                           @RequestParam
                                           @NotNull(message = CAN_NOT_BLANK_EX_MESSAGE)
                                           @Range(min = 2000, max = 9999, message = ILLEGAL_RANGE_EX_MESSAGE)
                                           Integer y,
                                           @RequestParam
                                           @NotNull(message = CAN_NOT_BLANK_EX_MESSAGE)
                                           @Range(min = 1, max = 12, message = ILLEGAL_RANGE_EX_MESSAGE)
                                           Integer m) {
        return productService.getDisabledDate(iproduct, y, m);

    }


}
