package com.team5.projrental.mypage;

import com.team5.projrental.common.exception.thrid.ClientException;
import com.team5.projrental.common.model.ResVo;
import com.team5.projrental.mypage.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

import static com.team5.projrental.common.exception.ErrorMessage.ILLEGAL_EX_MESSAGE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MypageController {
    private final MypageService service;

    @Validated
    @GetMapping("/prod")
    @Operation(summary = "대여리스트", description = "대여관련 내역")
    @Parameters(value = {
            @Parameter(name = "status", description = "status가 1이면 대여중(RESERVED, ACTIVATED, CANCELED ), -1이면 대여완료(EXPIRED, COMPLETED, HIDDEN)"),
            @Parameter(name = "page", description = "페이지")
    , @Parameter(name = "role", description = "role:1 -> iuser 가 구매한 상품들\n" +
            "role:2 -> iuser 가 판매한 상품들")})
    public List<BuyPaymentSelVo> getRentalList(@RequestParam(name = "role", defaultValue = "1") Long role, @RequestParam(name = "status", required = false) int status,
                                               @RequestParam(defaultValue = "1") @Range(min = 1) int page) {
        if(status != 1 && status != -1) {
            throw new ClientException(ILLEGAL_EX_MESSAGE);
        }
        PaymentSelDto dto = new PaymentSelDto();
        dto.setIstatus((long) status);
        dto.setPage(page);
        dto.setRole(role);
        return service.getRentalList(dto);
    }


    @Validated
    @GetMapping("/fav")
    @Operation(summary = "관심 목록", description = "로그인한 유저가 관심 등록한 제품 리스트")
    @Parameters(value = {@Parameter(name = "page", description = "페이지")})
    public List<MyFavListSelVo> getFavList(@RequestParam(defaultValue = "1") @Range(min = 1) int page) {
        MyFavListSelDto dto = new MyFavListSelDto();
        dto.setPage(page);
        return service.getFavList(dto);
    }


    @Validated
    @GetMapping("/review")
    @Operation(summary = "작성한 후기", description = "로그인 유저가 빌린내역 중 작성한 후기 리스트")
    @Parameters(value = {@Parameter(name = "page", description = "페이지")})
    public List<MyBuyReviewListSelVo> getReview(@RequestParam(defaultValue = "1") @Range(min = 1) int page) {
        MyBuyReviewListSelDto dto = new MyBuyReviewListSelDto();
        return service.getReview(dto);
    }

    @Validated
    @GetMapping("/dispute")
    @Operation(summary = "신고한 목록", description = "로그인 유저가 신고한 목록")
    @Parameters(value = {@Parameter(name = "page", description = "페이지")})
    public List<MyDisputeVo> getDispute(@RequestParam(defaultValue = "1") @Range(min = 1) int page) {

        return null;
    }

    @PatchMapping("/dispute")
    @Operation(summary = "신고 철회", description = "로그인 유저가 신고한 목록")
    @Parameters(value = {@Parameter(name = "idispute", description = "철회 할 분쟁pk")})
    public ResVo cancelDispute(@RequestBody int idispute) {
        return null;
    }


    @Validated
    @GetMapping("/board")
    @Operation(summary = "내가 쓴 게시글", description = "내가 작성한 자유게시글 조회")
    @Parameters(value = {@Parameter(name = "page", description = "페이지")})
    public List<MyBoardListVo> getBoard(@RequestParam(defaultValue = "1") @Min(1) int page) {
        return null;
    }
}
