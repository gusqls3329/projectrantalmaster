package com.team5.projrental.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.projrental.board.BoardRepository;
import com.team5.projrental.board.BoardService;
import com.team5.projrental.board.model.BoardDelDto;
import com.team5.projrental.common.Const;
import com.team5.projrental.common.SecurityProperties;
import com.team5.projrental.common.exception.BadAddressInfoException;
import com.team5.projrental.common.exception.BadWordException;
import com.team5.projrental.common.exception.ErrorCode;
import com.team5.projrental.common.exception.ErrorMessage;
import com.team5.projrental.common.exception.base.BadDateInfoException;
import com.team5.projrental.common.exception.base.BadInformationException;
import com.team5.projrental.common.exception.base.IllegalException;
import com.team5.projrental.common.exception.base.NoSuchDataException;
import com.team5.projrental.common.exception.checked.FileNotContainsDotException;
import com.team5.projrental.common.exception.thrid.ClientException;
import com.team5.projrental.common.model.ResVo;
import com.team5.projrental.common.model.restapi.Addrs;
import com.team5.projrental.common.security.AuthenticationFacade;
import com.team5.projrental.common.security.JwtTokenProvider;
import com.team5.projrental.common.security.SecurityUserDetails;
import com.team5.projrental.common.security.model.SecurityPrincipal;
import com.team5.projrental.common.utils.AxisGenerator;
import com.team5.projrental.common.utils.CommonUtils;
import com.team5.projrental.common.utils.CookieUtils;
import com.team5.projrental.common.utils.MyFileUtils;
import com.team5.projrental.entities.*;
import com.team5.projrental.entities.embeddable.Address;
import com.team5.projrental.entities.enums.*;
import com.team5.projrental.entities.inheritance.QUsers;
import com.team5.projrental.entities.inheritance.Users;
import com.team5.projrental.entities.mappedsuper.BaseUser;
import com.team5.projrental.payment.review.PaymentReviewService;
import com.team5.projrental.payment.review.ReviewRepository;
import com.team5.projrental.payment.review.model.DelRivewDto;
import com.team5.projrental.payment.thirdproj.PaymentRepository;
import com.team5.projrental.payment.thirdproj.paymentinfo.PaymentInfoRepository;
import com.team5.projrental.product.thirdproj.japrepositories.product.ProductRepository;
import com.team5.projrental.user.model.*;
import com.team5.projrental.user.verification.users.TossVerificationRequester;
import com.team5.projrental.user.verification.users.model.VerificationUserInfo;
import com.team5.projrental.user.verification.users.model.check.CheckResponseVo;
import com.team5.projrental.user.verification.users.model.ready.VerificationReadyDto;
import com.team5.projrental.user.verification.users.model.ready.VerificationReadyVo;
import com.team5.projrental.user.verification.users.repository.TossVerificationRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.team5.projrental.common.exception.ErrorCode.*;
import static com.team5.projrental.common.exception.ErrorMessage.BAD_NICK_EX_MESSAGE;
import static com.team5.projrental.common.exception.ErrorMessage.NO_SUCH_USER_FAILED_MESSAGE;

@Slf4j
@Service
@RequiredArgsConstructor
public class

UserService_min {
    private final UserMapper mapper;
    private final UserRepository userRepository;
    private final UsersRepository usersRepository;
    private final ProductRepository productRepository;
    private final PaymentInfoRepository paymentInfoRepository;
    private final PaymentRepository paymentRepository;
    private final ReviewRepository reviewRepository;
    private final BoardRepository boardRepository;

    private final BoardService boardService;
    private final PaymentReviewService paymentReviewService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final SecurityProperties securityProperties;
    private final CookieUtils cookieUtils;
    private final AuthenticationFacade authenticationFacade;
    private final AxisGenerator axisGenerator;
    private final MyFileUtils myFileUtils;
    private final TossVerificationRequester tossVerificationRequester;
    private final JPAQueryFactory queryFactory;
    private final TossVerificationRepository tossVerificationRepository;

    //    @Value("${spring.config.activate.on-profile}")
    private String profile;



    //

    private final RedisTemplate<Long, VerificationInfo> redisTemplate;

    @Transactional
    public VerificationReadyVo readyVerification(VerificationUserInfo userInfo) {

        Long id = tossVerificationRepository.findByUserInfos(userInfo.getUserName(),
                userInfo.getUserBirthday(),
                userInfo.getUserPhone());


        VerificationReadyDto dto = tossVerificationRequester.verificationRequest(userInfo);
        if (dto.getSuccess() == null) {
            throw new ClientException(ErrorCode.AUTHENTICATION_FAIL_EX_MESSAGE, "본인인증에 실패하였습니다.");
        }

        // 만약 존재할 경우 update, 존재하지 않을경우 save
        tossVerificationRepository.findByUserNameAndUserPhoneAndUserBirthday(userInfo.getUserName(), userInfo.getUserPhone(),
                userInfo.getUserBirthday()).ifPresentOrElse(verificationInfo -> {
                    verificationInfo.setTxId(dto.getTxid());
                    // redis에 저장
                    redisTemplate.opsForValue().set(verificationInfo.getId(), verificationInfo, Duration.ofSeconds(300L));
                },
                () -> {
                    VerificationInfo info = VerificationInfo.builder()
                            .id(dto.getId())
                            .txId(dto.getTxid())
                            .userPhone(userInfo.getUserPhone())
                            .build();
                    tossVerificationRepository.save(info);
                    // redis에 저장
                    redisTemplate.opsForValue().set(info.getId(), info, Duration.ofSeconds(300L));
                });

        if (id != null && id > 0) {
            return VerificationReadyVo.builder()
                    .resultType(dto.getResultType())
                    .success(dto.getSuccess())
                    .fail(dto.getFail())
                    .id(id)
                    .build();
        }
        return VerificationReadyVo.builder()
                .resultType(dto.getResultType())
                .success(dto.getSuccess())
                .fail(dto.getFail())
                .id(dto.getId())
                .build();
    }

    @Transactional
    public CheckResponseVo checkVerification(Long id) {
        VerificationInfo baseInfo = redisTemplate.opsForValue().get(id);
        if (baseInfo == null) {
            throw new ClientException(ILLEGAL_EX_MESSAGE, "본인인증 내역이 없습니다.");
        }
        VerificationInfo info = tossVerificationRepository.findById(id).orElseThrow(() -> new ClientException(ILLEGAL_EX_MESSAGE, "본인인증 내역이 없습니다."));

        CheckResponseVo responseVo = tossVerificationRequester.check(info);

        info.setUserName(baseInfo.getUserName());
        info.setUserBirthday(baseInfo.getUserBirthday());

        return responseVo;
    }


}

