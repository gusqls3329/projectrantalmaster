package com.team5.projrental.user;

import com.team5.projrental.common.exception.ErrorMessage;
import com.team5.projrental.common.model.ResVo;
import com.team5.projrental.entities.VerificationInfo;
import com.team5.projrental.user.model.*;
import com.team5.projrental.user.verification.users.model.VerificationUserInfo;
import com.team5.projrental.user.verification.users.model.check.CheckResponseVo;
import com.team5.projrental.user.verification.users.model.ready.VerificationReadyDto;
import com.team5.projrental.user.verification.users.model.ready.VerificationReadyVo;
import com.team5.projrental.user.verification.users.repository.TossVerificationRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService service;


    // FIXME : 해당 부분의 리턴을 uuid 말고 그냥 본인인증 테이블의 generated key 를 리턴할까 싶다. -> 보안상 문제가 없을거 같고, 있다고해도 uuid 라고 다를것 없어 보인다.
    @PostMapping("/verification")
    @Operation(summary = "본인인증 요청", description = "본인 요청하기<br>" +
                                                  "모든 데이터는 구분자와 공백없이 제공")
    public VerificationReadyVo readyVerification(@RequestBody VerificationUserInfo userInfo){
        return service.readyVerification(userInfo);
    }
    // FIXME : 해당 부분의 리턴을 uuid 말고 그냥 본인인증 테이블의 generated key 를 리턴할까 싶다. -> 보안상 문제가 없을거 같고, 있다고해도 uuid 라고 다를것 없어 보인다.
    @GetMapping("/verification")
    @Operation(summary = "본인인증 결과 확인", description = "본인인증 수행 햇는지 확인")
    public CheckResponseVo checkVerification(Long id){
        return service.checkVerification(id);
    }

    @PostMapping("/check")
    @Operation(summary = "중복확인", description = "uid, nick 중복확인")
    @Parameters(value = {
            @Parameter(name="div", description = "div:1 - nick 중복확인 <br>div:2 - uid 중복확인")
            , @Parameter(name="uid", description = "아이디")
            , @Parameter(name="nick", description = "닉네임")
    })
    public ResVo CheckUserInfo(@RequestBody @Validated UserCheckInfoDto dto) {
        return service.checkUserInfo(dto);
    }

    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "회원가입", description = "유저 회원가입, 권한이 리턴됨")
    @Parameters(value = {
            @Parameter(name="addr", description = "동/면/읍까지의 주소")
            , @Parameter(name="restAddr", description = "나머지 주소")
            , @Parameter(name="uid", description = "아이디")
            , @Parameter(name="upw", description = "비밀번호")
            , @Parameter(name="nick", description = "닉네임")
            , @Parameter(name="pic", description = "사진")
            , @Parameter(name="phone", description = "휴대폰 번호 (형식 : 010-1111-2222)")
            , @Parameter(name="email", description = "이메일 (형식 : aaa@naver.com)")
            , @Parameter(name = "isValid", description = "중복체크 ( 완료되었다면 2) ")
            , @Parameter(name = "uuid", description = "본인인증 완료시 리턴해주는 uuid 값 다시 제공해주세요")
            , @Parameter(name = "iverificationInfo", description = "본인인증 후 정보 pk")

    })
    public ResVo postSignup(@RequestPart(required = false) MultipartFile pic, @RequestPart @Validated UserSignupDto dto) {
        dto.setPic(pic);
        log.info("dto : {}", dto);
        return new ResVo((long) service.postSignup(dto));
    }

    @PostMapping
    @Operation(summary = "로그인", description = "유저 로그인(iauth:권한)<br><br>" +
            "로그인 성공시 fireBaseToken 수정부분 수행 해야함.(Patch: /api/user/fcm")
    @Parameters(value = {
            @Parameter(name = "uid", description = "아이디(길이 : 4~15)")
            , @Parameter(name = "upw", description = "비밀번호(길이 : 8~20)")
    })
    public SigninVo postSignin(HttpServletResponse res, @RequestBody @Validated SigninDto dto) {
        return service.postSignin(res, dto);
    }

    @Operation(summary = "fireBaseToken 등록", description = "발급받은 해당 유저의 브라우저에 발급된 " +
            "fireBaseToken 을 로그인한 유저에 등록")
    @Parameters(value = {
            @Parameter(name = "firebaseToken", description = "토큰값")
    })
    @PatchMapping("/fcm")
    public ResVo patchToken(UserFirebaseTokenPatchDto dto) {

        return service.patchToken(dto);
    }

    @Operation(summary = "로그아웃", description = "토큰만료시 자동 로그아웃")
    @GetMapping("/signout")
    public ResVo getSignOut(HttpServletResponse res){
        return new ResVo((long) service.getSignOut(res));
    }

    @GetMapping("/refrech-token")
    public SigninVo getRefrechToken(HttpServletRequest req){
        return service.getRefrechToken(req);
    }

    @PatchMapping("/firebase-token")
    public ResVo patchUserFirebaseToken(@RequestBody UserFirebaseTokenPatchDto dto) {
        return service.patchUserFirebaseToken(dto);
    }

    @Operation(summary = "아이디 찾기", description = "유저 아이디 찾기(iauth:권한) ")
    @Parameters(value = {
            @Parameter(name = "id", description = "본인인증 후 정보 pk"),
//            @Parameter(name = "phone", description = "휴대폰 번호 (형식 : 010-1111-2222)")
    })
    @PostMapping("/id")
    public FindUidVo getFindUid(@RequestBody @Validated FindUidDto phone) {
        return service.getFindUid(phone);
    }
    @Operation(summary = "비밀번호 변경", description = "비밀번호 찾기 불가능, 비밀번호 수정, 권한이 리턴됨")
    @Parameters(value = {
            @Parameter(name = "id", description = "본인인증 후 정보 pk"),
            @Parameter(name = "uid", description = "아이디"),
            @Parameter(name = "upw", description = "변경을 위한 비밀번호(길이 : 8~20)")
    })
    @PatchMapping("/pw")
    public ResVo getFindUpw(@RequestBody @Validated FindUpwDto dto) {
        return new ResVo((long) service.getFindUpw(dto));
    }

    @Operation(summary = "회원정보 수정", description = "닉네임, 프로필 사진, 비밀번호 수정, 위치 수정")
    @Parameters(value = {
            @Parameter(name = "addr", description = "동/면/읍까지의 주소")
            , @Parameter(name = "restAddr", description = "나머지 주소")
            , @Parameter(name = "upw", description = "비밀번호")
            , @Parameter(name = "nick", description = "닉네임")
            , @Parameter(name = "pic", description = "사진")
            , @Parameter(name = "phone", description = "휴대폰 번호 (형식 : 010-1111-2222)")
            , @Parameter(name = "email", description = "이메일 (형식 : xxx@xxx.xxx)")

    })
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResVo putUser(@RequestPart(required = false) @Validated ChangeUserDto dto,
                         @RequestParam(required = false) MultipartFile pic) {
        return new ResVo((long) service.putUser(dto, pic));
    }

    @Operation(summary = "회원탈퇴", description = "유저 삭제 : 유저정보, 상품, 결제정보 등 삭제")
    @Parameters(value = {
            @Parameter(name = "uid", description = "아이디")
            , @Parameter(name = "upw", description = "비밀번호")
            , @Parameter(name = "phone", description = "휴대폰 번호 (형식 : 010-1111-2222)")
    })
    @PatchMapping
    public ResVo patchUser(@RequestBody @Validated DelUserDto dto) {
        return new ResVo((long) service.patchUser(dto));
    }

    @Operation(summary = "유저 정보 조회", description = "유저 개인 정보 조회, (iauth:권한)")
    @Parameters(value = {
            @Parameter(name = "tar", description = "유저 Pk값")
    })
    @Validated
    @GetMapping
    public SelUserVo getUSer(@RequestParam(value = "tar", required = false) @Min(1) Long iuser) {
        return service.getUser(iuser);
    }
}
