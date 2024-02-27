package com.team5.projrental.administration;

import com.team5.projrental.administration.model.*;
import com.team5.projrental.common.Const;
import com.team5.projrental.common.exception.ErrorCode;
import com.team5.projrental.common.exception.thrid.ClientException;
import com.team5.projrental.common.model.ResVo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/admin")
public class AdministrationController {

    private final AdministrationService administrationService;

    @Operation(summary = "유저 특정 결제정보 조회",
            description = "<strong>유저 특정 결제정보 조회</strong><br>" +
                          "[ [v] : 필수값 ]" +
                          "[v] ipayment: 제품의 PK<br>" +
                          "<br>" +
                          "성공시: <br>" +
                          "ipayment: 결제의 PK<br>" +
                          "iproduct: 제품의 PK<br>" +
                          "title: 제품의 제목<br>" +
                          "pic: 제품의 대표사진<br>" +
                          "price: 전체 대여 기간동안 필요한 가격<br>" +
                          "rentalStartDate: 대여 시작 일<br>" +
                          "rentalEndDate: 제품 반납 일<br>" +
                          "rentalDuration: 제품 대여 기간<br>" +
                          "deposit: 보증금<br>" +
                          "payment: 결제 수단 -> credit-card, kakao-pay<br>" +
                          "istatus: 제품 상태<br>" +
                          "code: 제품 고유 코드<br>" +
                          "role: 로그인한 유저가 판매자인지 구매자인지 여부 (1: 판매자, 2: 구매자) - 리뷰 분기용" +
                          "createdAt: 결제 일자<br>" +
                          "iuser: 거래 상대 유저의 PK<br>" +
                          "nick: 거래 상대 유저의 닉네임<br>" +
                          "phone: 거래 상대 유저의 핸드폰 번호<br>" +
                          "userPic: 거래 상대 유저의 프로필 사진<br>" +
                          "<br>" +
                          "실패시: <br>" +
                          "message: 에러 발생 사유<br>errorCode: 에러 코드")
    @GetMapping("payment")
    public PaymentByAdminVo getPayment(@RequestParam String code) {

        return administrationService.getPayment(code);

    }

    @Operation(summary = "자유게시글 강제 삭제",
            description = "<strong>신고당한 자유게시글이나 잘못된 게시글 강제 삭제</strong><br>")
    @DeleteMapping("board/{iboard}")
    public ResVo delBoard(@PathVariable Long iboard, @RequestParam Integer reason) {
        return administrationService.delBoard(iboard, reason);
    }

    @Operation(summary = "전체 *일반* 유저목록 조회",
            description = """
                    전체 *일반* 유저목록 조회</strong><br><br>
                    page: 페이지네이션 숫자
                    type: 검색 종류 <br>
                    ㄴ> 1: 전체, 2: 이름, 3: 아이디 * 제공되지 않을경우, 검색하지 않음
                    search: 검색어 키워드, search-type 이 제공되지 않으면 무시됨.
                    status: 검색 조건(유저 상태)<br>
                    ㄴ> 1: 전체 / 2: 정상 / 3:정지
                    ㄴ> 제공되지 않을 경우에도 전체 조회.
                    """)
    @GetMapping("user")
    public UserByAdminVo getAllUsers(@RequestParam
                                     Integer page,
                                     @RequestParam(required = false)
                                     Integer type,
                                     @RequestParam(required = false)
                                     String search,
                                     @RequestParam(required = false)
                                     Integer status) {
        if (search != null && search.isEmpty()) {
            throw new ClientException(ErrorCode.CAN_NOT_BLANK_EX_MESSAGE,
                    "search 는 빈 값일수 없습니다.");
        }
        return administrationService.getAllUsers((page - 1) * Const.ADMIN_PER_PAGE,
                type, search, status);
    }

    @Operation(summary = "회원 강제 탈퇴",
            description = "(자동화는 별도로 존재) 운영자 재량으로 유저 탈퇴")
    @DeleteMapping("user/{iuser}")
    public ResVo delUser(@PathVariable Long iuser, @RequestParam Integer reason) {
        return administrationService.delUser(iuser, reason);
    }

    @Operation(summary = "전체 신고내역 조회",
            description = "전체 신고내역 조회<br>" +
                          "[ 필수 : [v] ]" +
                          "[v] div: 분쟁신고 or 사고신고 -> 1: 분쟁신고, -1: 사고신고 (필수)<br>" +
                          "    search: 아이디로 조회<br>" +
                          "    category: -1: 파손, -2: 분실 || " +
                          "1: 잠수(구매전), 2: 잠수(구매후), 3: 허위매물, 4: 바꿔치기, 5: 매너, 6: 지연<br>" +
                          "    status: 상태 -> 1: 수리, 0: 미처리, -1: 반려")
    @Validated
    @GetMapping("/dispute")
    public DisputeByAdminVo getAllDispute(@RequestParam Integer page,
                                          @NotNull
                                          @Range(min = -1, max = 1)
                                          @RequestParam Integer div,

                                          @RequestParam(required = false) String search,
                                          @Range(min = -2, max = 6)
                                          @RequestParam(required = false) Integer category,
                                          @Range(min = -1, max = 1)
                                          @RequestParam(required = false) Integer status) {
        if (div == 0) throw new ClientException(ErrorCode.BAD_DIV_INFO_EX_MESSAGE,
                "잘못된 div 값(1 또는 -1)");
        if (category != null && category == 0) {
            throw new ClientException(ErrorCode.ILLEGAL_EX_MESSAGE,
                    "잘못된 category 값");
        }
        if (search != null && search.isEmpty()) {
            throw new ClientException(ErrorCode.CAN_NOT_BLANK_EX_MESSAGE,
                    "search 는 빈 값일수 없습니다.");
        }
        return administrationService.getAllDispute((page - 1) * Const.ADMIN_PER_PAGE,
                div, search, category, status);
    }

    @Operation(summary = "신고 수리 또는 반려",
            description = """
                    idispute: 처리할 신고의 식별숫자<br>
                    type: 수리: 1, 반려: -1
                    """)
    @PostMapping("dispute/{idispute}")
    public ResVo postDispute(@PathVariable Long idispute,
                             Integer type) {
        return administrationService.postDispute(idispute, type);
    }

    @Operation(summary = "운영자 권한 전체 제품 조회 (삭제된 제품은 제외)",
            description = "[필수: [v]]<br>" +
                          "[v] page: 페이징 <br>" +
                          "    type: 검색 타입 -> 1: 닉네임, 2: 카테고리<br>" +
                          "    search: 검색어 키워드")
    @Validated
    @GetMapping("product")
    public ProductByAdminVo getAllProducts(@RequestParam Integer page,
                                           @Range(min = 1, max = 2)
                                           @RequestParam(required = false) Integer type,
                                           @RequestParam(required = false) String search) {
        if (search != null && search.isEmpty()) {
            throw new ClientException(ErrorCode.CAN_NOT_BLANK_EX_MESSAGE,
                    "search 는 빈 값일수 없습니다.");
        }
        return administrationService.getAllProducts((page - 1) * Const.ADMIN_PER_PAGE,
                type, search);
    }

    @Operation(summary = "상품 강제 숨김 처리",
            description = "운영자가 해당 상품을 강제 삭제(숨김처리)")
    @DeleteMapping("product/{iproduct}")
    public ResVo patchProduct(@PathVariable Long iproduct, @RequestParam Integer reason) {
        return administrationService.patchProduct(iproduct, reason);
    }

    @Operation(summary = "운영자 권한 전체 자유 게시글 조회 (삭제된 게시글은 제외)",
            description = "[필수: [v]]<br>" +
                          "[v] page: 페이징 <br>" +
                          "    type: 검색 타입 -> 1: 닉네임, 2: 제목<br>" +
                          "    search: 검색어 키워드<br>" +
                          "    sort: 제공하지 않으면 최신순, 1: 조회수 많은순 내림차순, (조회수 오름차순도 필요하면 말해주세요)")
    @Validated
    @GetMapping("board")
    public BoardByAdminVo getAllBoards(@RequestParam Integer page,
                                       @Range(min = 1, max = 2)
                                       @RequestParam(required = false) Integer type,
                                       @Length(min = 2)
                                       @RequestParam(required = false) String search,
                                       @Range(min = 1, max = 1)
                                       @RequestParam(required = false) Integer sort) {
        if (search != null && search.isEmpty()) {
            throw new ClientException(ErrorCode.CAN_NOT_BLANK_EX_MESSAGE,
                    "search 는 빈 값일수 없습니다.");
        }
        return administrationService.getAllBoards((page - 1) * Const.ADMIN_PER_PAGE,
                type, search, sort);
    }

    @Operation(summary = "운영자 권한 반환 예정금 조회",
            description = "[필수: [v]]<br>" +
                          "[v] page: 페이징 <br>" +
                          "    status: 특정 상태로 조회(1: 처리됨, 0: 대기중, -1: 반려됨)")
    @Validated
    @GetMapping("refund")
    public RefundByAdminVo getAllRefunds(@RequestParam Integer page,
                                         @Range(min = -1, max = 1)
                                         @RequestParam(required = false) Integer status) {
        return administrationService.getAllRefunds(page, status);
    }

    @Operation(summary = "운영자 권한 반환 예정금 처리",
            description = "[필수: [v]]<br>" +
                          "[v] irefund: refund 식별값<br>" +
                          "[v] div: 처리 종류 -> 1: 수락, -1: 거절<br><br>" +
                          "성공시:<br>" +
                          "result: 환불 된 금액 (거절시 -1)")
    @DeleteMapping("refund/{irefund}")
    public ResVo patchRefund(@PathVariable Long irefund,
                             @Range(min = -1, max = 1)
                             @RequestParam Integer div) {
        if (div == 0) throw new ClientException(ErrorCode.BAD_DIV_INFO_EX_MESSAGE,
                "잘못된 div 값(1 또는 -1)");

        return administrationService.patchRefund(irefund, div);
    }

    @Operation(summary = "운영자 권한 신고된 채팅 목록 조회",
            description = "[필수: [v]]<br>" +
                          "[v] page: 페이징 <br>")
    @Validated
    @GetMapping("chat")
    public ChatByAdminVo getAllChats(@RequestParam Integer page) {
        return administrationService.getAllChats(page);
    }

    // ???

    @Operation(summary = "운영자 권한 신고된 채팅 삭제",
            description = "[필수: [v]]<br>" +
                          "[v] ichat: 삭제할 채팅 <br>")
    @Validated
    @DeleteMapping("chat/{ichat}")
    public ResVo delChat(@PathVariable Long ichat) {
        return administrationService.delChat(ichat);
    }

}
