package com.team5.projrental.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.projrental.board.BoardRepository;
import com.team5.projrental.board.BoardService;
import com.team5.projrental.board.model.BoardDelDto;
import com.team5.projrental.common.Const;
import com.team5.projrental.common.SecurityProperties;
import com.team5.projrental.common.exception.*;
import com.team5.projrental.common.exception.base.*;
import com.team5.projrental.common.exception.checked.FileNotContainsDotException;
import com.team5.projrental.common.exception.thrid.ClientException;
import com.team5.projrental.common.model.ResVo;
import com.team5.projrental.common.model.restapi.Addrs;
import com.team5.projrental.common.utils.AxisGenerator;
import com.team5.projrental.common.utils.CommonUtils;
import com.team5.projrental.common.utils.CookieUtils;
import com.team5.projrental.common.security.AuthenticationFacade;
import com.team5.projrental.common.security.JwtTokenProvider;
import com.team5.projrental.common.security.SecurityUserDetails;
import com.team5.projrental.common.security.model.SecurityPrincipal;
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
import com.team5.projrental.payment.review.model.SelRatVo;
import com.team5.projrental.payment.review.model.UpRating;
import com.team5.projrental.payment.thirdproj.PaymentRepository;
import com.team5.projrental.payment.thirdproj.paymentinfo.PaymentInfoRepository;
import com.team5.projrental.product.thirdproj.japrepositories.product.ProductRepository;
import com.team5.projrental.user.model.*;
import com.team5.projrental.user.verification.users.TossVerificationRequester;
import com.team5.projrental.user.verification.users.model.VerificationRedisForm;
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
import java.util.ArrayList;
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

UserService {
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

    private final RedisTemplate<String, VerificationRedisForm> redisTemplate;

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
                    dto.setId(verificationInfo.getId());

                    // redis에 저장
                    VerificationRedisForm redisForm = getVerificationRedisForm(verificationInfo);
                    redisTemplate.opsForValue().set(redisForm.getId(), redisForm, Duration.ofSeconds(300L));

                    // debug
                    log.debug("redis = {}", redisTemplate.opsForValue().get(verificationInfo.getId()));
                },
                () -> {
                    VerificationInfo info = VerificationInfo.builder()
                            .txId(dto.getTxid())
                            .userName(userInfo.getUserName())
                            .userBirthday(userInfo.getUserBirthday())
                            .userPhone(userInfo.getUserPhone())
                            .build();

                    tossVerificationRepository.save(info);
                    dto.setId(info.getId());

                    // redis에 저장
                    VerificationRedisForm redisForm = getVerificationRedisForm(info);
                    redisTemplate.opsForValue().set(redisForm.getId(), redisForm, Duration.ofSeconds(300L));
                    // debug
                    log.debug("redis = {}", redisTemplate.opsForValue().get(info.getId()));
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

    private static VerificationRedisForm getVerificationRedisForm(VerificationInfo verificationInfo) {
        return VerificationRedisForm.builder()
                .id(String.valueOf(verificationInfo.getId()))
                .txId(verificationInfo.getTxId())
                .userName(verificationInfo.getUserName())
                .userPhone(verificationInfo.getUserPhone())
                .userBirthday(verificationInfo.getUserBirthday())
                .build();
    }

    @Transactional
    public CheckResponseVo checkVerification(Long id) {
        VerificationRedisForm baseInfo = redisTemplate.opsForValue().get(String.valueOf(id));
        // debug
        log.debug("baseInfo = {}", baseInfo);

        if (baseInfo == null) {
            throw new ClientException(ILLEGAL_EX_MESSAGE, "본인인증 내역이 없습니다.");
        }
//        VerificationInfo info = tossVerificationRepository.findById(id).orElseThrow(() -> new ClientException(ILLEGAL_EX_MESSAGE, "본인인증 내역이 없습니다."));
        VerificationInfo info = VerificationInfo.builder()
                .id(Long.parseLong(baseInfo.getId()))
                .userName(baseInfo.getUserName())
                .userPhone(baseInfo.getUserPhone())
                .userBirthday(baseInfo.getUserBirthday())
                .txId(baseInfo.getTxId())
                .build();

        // txId 만 사용하므로 문제 없음.
        CheckResponseVo responseVo = tossVerificationRequester.check(info);

        // 토스 본인인증이 테스트라서 데이터를 redis 에 미리 저장해둔 데이터로 변경작업 필요. (성별, 지역은 토스 응답 그대로 사용)
        return CheckResponseVo.builder()
                .id(Long.valueOf(baseInfo.getId()))
                .name(baseInfo.getUserName())
                .birthday(baseInfo.getUserBirthday())
                .gender(responseVo.getGender())
                .nationality(responseVo.getNationality())
                .build();
    }


    @Transactional
    public int postSignup(UserSignupDto dto) {

        VerificationInfo info = tossVerificationRepository.findById(dto.getIverificationInfo()).orElseThrow(() -> new ClientException(AUTHENTICATION_FAIL_EX_MESSAGE,
                "본인인증 후 진행해 주세요"));
        User userInfo = userRepository.findByVerificationInfo(info);
        if (userInfo != null) {
            throw new ClientException(AUTHENTICATION_FAIL_EX_MESSAGE, "이미 가입된 회원입니다.");
        }

        User userEx = userRepository.exFindByVerificationInfo(info);
        byte a = 0;
        if (userEx != null) {
            if (userEx.getStatus() == UserStatus.DELETED) {
                throw new ClientException(AUTHENTICATION_FAIL_EX_MESSAGE, "벌점누적으로 인해 탈퇴된 회원입니다.");
            }
            a = userEx.getPenalty();
            userEx.setPenalty((byte) 0);
        }
        Addrs addrs = axisGenerator.getAxis(dto.getAddr());
        CommonUtils.ifAnyNullThrow(BadAddressInfoException.class, BAD_ADDRESS_INFO_EX_MESSAGE,
                addrs, addrs.getAddress_name(), addrs.getX(), addrs.getY());
        BaseUser baseUser = new BaseUser();
        Address address = Address.builder()
                .addr(dto.getAddr())
                .restAddr(addrs.getAddress_name())
                .x(Double.parseDouble(addrs.getX()))
                .y(Double.parseDouble(addrs.getY()))
                .build();
        baseUser.setAddress(address);
        String hashedPw = passwordEncoder.encode(dto.getUpw());
        dto.setUpw(hashedPw);

        String path = Const.CATEGORY_USER + "/" + dto.getIuser();
        myFileUtils.delFolderTrigger(path);

        if (dto.getPic() != null) {
            try {
                String savedPicFileNm = String.valueOf(
                        myFileUtils.savePic(dto.getPic(), Const.CATEGORY_USER,
                                String.valueOf(dto.getIuser())));
                dto.setChPic(savedPicFileNm);
            } catch (FileNotContainsDotException e) {
                throw new BadInformationException(BAD_PIC_EX_MESSAGE);
            }
        }
        baseUser.setStoredPic(dto.getChPic());
        //baseUser.setStatus(dto.get);
        User user = new User();
        user.setStatus(UserStatus.ACTIVATED);
        user.setUid(dto.getUid());
        user.setUpw(hashedPw);
        user.setBaseUser(baseUser);
        user.setNick(dto.getNick());
        user.setProvideType(ProvideType.LOCAL);
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setAuth(Auth.USER);
        user.setVerificationInfo(info);
        user.setPenalty(a);
        userRepository.save(user);

        return (int) Const.SUCCESS;
    }


    public SigninVo postSignin(HttpServletResponse res, SigninDto dto) {
//        UserEntity entity = mapper.selSignin(dto);


        Users findUsers = usersRepository.findByUid(dto.getUid());
        if (findUsers == null) {
            throw new ClientException(ErrorCode.NO_SUCH_ID_EX_MESSAGE, "아이디가 존재하지 않음");
        }
        String password = "";
        Long iuser = 0L;
        Auth auth = findUsers.getAuth();


        password = findUsers.getUpw();
        iuser = findUsers.getId();

        QUsers users = QUsers.users;
        queryFactory.select(users)
                .from();

        if (findUsers == null) {
            throw new NoSuchDataException(NO_SUCH_ID_EX_MESSAGE);
        } else if (!passwordEncoder.matches(dto.getUpw(), password)) {
            throw new NoSuchDataException(NO_SUCH_PASSWORD_EX_MESSAGE);
        }


        String status = mapper.selLoginStatus(dto);
        if (UserStatus.HIDDEN.toString().equals(status)) {
            throw new ClientException(NO_SUCH_USER_HIDE_MESSAGE);
        }
        if (UserStatus.FAILED.toString().equals(status)) {
            throw new ClientException(NO_SUCH_USER_FAILED_MESSAGE);
        }
        SecurityPrincipal principal = SecurityPrincipal.builder()
                .iuser(iuser)
                .auth(auth.name())
                .build();
        principal.getRoles().add(auth.name());


        String at = jwtTokenProvider.generateAccessToken(principal);
        String rt = jwtTokenProvider.generateRefreshToken(principal);
        if (res != null) {
            int rtCookieMaxAge = (int) (securityProperties.getJwt().getRefreshTokenExpiry() / 1000);
            cookieUtils.deleteCookie(res, "rt");
            cookieUtils.setCookie(res, "rt", rt, rtCookieMaxAge);
        }

        return SigninVo.builder()
                .result(String.valueOf(Const.SUCCESS))
                .iuser(iuser)
                .auth(auth.getIauth())
//                .firebaseToken(entity.getFirebaseToken())
                .accessToken(at)
                .build();
    }

    public ResVo patchToken(UserFirebaseTokenPatchDto dto) {
        dto.setIuser(authenticationFacade.getLoginUserPk());
        return new ResVo((long) mapper.patchToken(dto));
    }

    public int getSignOut(HttpServletResponse res) {
        try {
            cookieUtils.deleteCookie(res, "rt");
        } catch (NullPointerException e) {
            throw new BadInformationException(AUTHENTICATION_FAIL_EX_MESSAGE);
        }
        return 1;
    }

    public SigninVo getRefrechToken(HttpServletRequest req) {
        Optional<String> optRt = cookieUtils.getCookie(req, "rt").map(Cookie::getValue);
        if (optRt.isEmpty()) {
            return SigninVo.builder()
                    .result(String.valueOf(Const.FAIL))
                    .accessToken(null)
                    .build();
        }
        String token = optRt.get();

        if (!jwtTokenProvider.isValidatedToken(token)) {
            return SigninVo.builder()
                    .result(String.valueOf(Const.FAIL))
                    .accessToken(null)
                    .build();
        }
        SecurityUserDetails UserDetails = (SecurityUserDetails) jwtTokenProvider.getUserDetailsFromToken(token);
        SecurityPrincipal Principal = UserDetails.getSecurityPrincipal();
        String at = jwtTokenProvider.generateAccessToken(Principal);

        return SigninVo.builder()
                .result(String.valueOf(Const.SUCCESS))
                .accessToken(at).build();
    }

    public ResVo patchUserFirebaseToken(UserFirebaseTokenPatchDto dto) { //FirebaseToken을 발급 : Firebase방식 : 메시지를 보낼때 ip대신 고유값(Firebase)을 가지고 있는사람에게 메시지 전달
        Long loginUserPk = authenticationFacade.getLoginUserPk();
        dto.setIuser(loginUserPk);
        int result = mapper.updUserFirebaseToken(dto);
        if (result == 1) {
            return new ResVo(Const.SUCCESS);
        }
        throw new BadInformationException(AUTHENTICATION_FAIL_EX_MESSAGE);
    }

    @Transactional
    public FindUidVo getFindUid(FindUidDto dto) {
        VerificationInfo info = tossVerificationRepository.findById(dto.getId()).orElseThrow(() -> new ClientException(AUTHENTICATION_FAIL_EX_MESSAGE,
                "본인인증 후 진행해 주세요"));
        if (info == null) {
            throw new ClientException(AUTHENTICATION_FAIL_EX_MESSAGE, "본인인증 내역이 없습니다.");
        }
        User users = userRepository.findByVerificationInfo(info);
        if (users == null) {
            throw new ClientException(BAD_ID_EX_MESSAGE, "회원이 존재하지 않습니다.");
        }
        return FindUidVo.builder()
                .uid(users.getUid())
                .auth(users.getAuth().getIauth())
                .iuser(users.getId().intValue())
                .build();
    }

    @Transactional
    public int getFindUpw(FindUpwDto dto) {
        VerificationInfo info = tossVerificationRepository.findById(dto.getId())
                .orElseThrow(() -> new ClientException(AUTHENTICATION_FAIL_EX_MESSAGE, "본인인증 후 진행해 주세요"));

        if (info != null) {
            User findUser = userRepository.findByVerificationInfo(info);
            if (!findUser.getUid().equals(dto.getUid())) {
                throw new ClientException(AUTHENTICATION_FAIL_EX_MESSAGE, "유저 아이디와 본인인증 정보가 일치하지 않음.");
            }
            String hashedPw = passwordEncoder.encode(dto.getUpw());
            findUser.setUpw(hashedPw);
            return (int) Const.SUCCESS;

        } else {
            throw new ClientException(AUTHENTICATION_FAIL_EX_MESSAGE, "본인인증 내역이 없습니다.");
        }
    }

/*
    public FindUidVo getFindUid(FindUidDto phone) {
        FindUidVo vo = mapper.selFindUid(phone);

        //vo.setIauth(authenticationFacade.getLoginUserAuth());
        if (vo == null) {
            throw new BadInformationException(NO_SUCH_USER_EX_MESSAGE);
        }

        vo.setUid(vo.getUid().substring(0, 4) + "*".repeat(vo.getUid().substring(4).length()));
        return vo;
    }*/
/*
    public int getFindUpw(FindUpwDto dto) {
        String hashedPw = BCrypt.hashpw(dto.getUpw(), BCrypt.gensalt());
        dto.setUpw(hashedPw);

        int result = mapper.upFindUpw(dto);

        if (result == 1) {
            return Const.SUCCESS;
        }
        throw new BadInformationException(NO_SUCH_USER_EX_MESSAGE);
    }*/

    public int putUser(ChangeUserDto dto, MultipartFile pic) {
        if (dto == null && pic == null) throw new BadInformationException(CAN_NOT_BLANK_EX_MESSAGE);
        if (dto == null) dto = new ChangeUserDto();
        Auth loginUserAuth = authenticationFacade.getLoginUserAuth();

        if (pic != null) {
            dto.setPic(pic);
        }

        CommonUtils.ifContainsBadWordThrow(BadWordException.class, BAD_WORD_EX_MESSAGE,
                dto.getNick() == null ? "" : dto.getNick(),
                dto.getRestAddr() == null ? "" : dto.getRestAddr());

        Long loginUserPk = authenticationFacade.getLoginUserPk();
        dto.setIuser(loginUserPk);

        if (checkNickOrId(1, dto.getNick()) == null) throw new BadInformationException(BAD_INFO_EX_MESSAGE);


        if (dto.getAddr() != null) {
            Addrs addrs = axisGenerator.getAxis(dto.getAddr());

            CommonUtils.ifAnyNullThrow(BadAddressInfoException.class, BAD_ADDRESS_INFO_EX_MESSAGE,
                    addrs, addrs.getAddress_name(), addrs.getX(), addrs.getY());
            dto.setX(Double.parseDouble(addrs.getX()));
            dto.setY(Double.parseDouble(addrs.getY()));
            dto.setAddr(addrs.getAddress_name());
        }

        String path = Const.CATEGORY_USER + "/" + dto.getIuser();
        myFileUtils.delFolderTrigger(path);
        if (pic != null) {
            try {
                String savedPicFileNm = String.valueOf(
                        myFileUtils.savePic(dto.getPic(), Const.CATEGORY_USER,
                                String.valueOf(dto.getIuser())));
                dto.setChPic(savedPicFileNm);
            } catch (FileNotContainsDotException e) {
                throw new BadInformationException(BAD_PIC_EX_MESSAGE);
            }
        }
        if (dto.getUpw() != null) {
            String hashedPw = BCrypt.hashpw(dto.getUpw(), BCrypt.gensalt());
            dto.setUpw(hashedPw);
        }
        int result = 1;
        if (CommonUtils.ifAllNullReturnFalse(
                dto.getNick(), dto.getChPic(), dto.getUpw(),
                dto.getPhone(), dto.getAddr(),
                dto.getRestAddr(), dto.getEmail())) {
            result = mapper.changeUser(dto);
        }

        if (result != 0) {
            Auth auth = authenticationFacade.getLoginUserAuth();
            return (int) Const.SUCCESS;
        }
        throw new BadDateInfoException(AUTHENTICATION_FAIL_EX_MESSAGE);
    }

    @Transactional
    public int patchUser(DelUserDto dto) {
        Long loginUserPk = authenticationFacade.getLoginUserPk();
        dto.setIuser(loginUserPk);

        User user = userRepository.findById(loginUserPk).orElseThrow(() -> new ClientException(NO_SUCH_ID_EX_MESSAGE));

        if (user == null) {
            throw new NoSuchDataException(NO_SUCH_ID_EX_MESSAGE);
        } else if (!passwordEncoder.matches(dto.getUpw(), user.getUpw())) {
            throw new NoSuchDataException(NO_SUCH_PASSWORD_EX_MESSAGE);
        }
        List<DelSelVo> vo = mapper.selStatusPay(loginUserPk);
        List<DelSelProcVo> procVo = mapper.selStatusPro(loginUserPk);
        Review review = reviewRepository.findByUser(user);
        if (review != null) {
            DelRivewDto reviewDto = new DelRivewDto();
            reviewDto.setIreview(review.getId().intValue());
            reviewDto.setIreview(review.getId().intValue());
            paymentReviewService.delReview(reviewDto);
        }
        Board board = boardRepository.findByUser(user);
        if (board != null) {
            BoardDelDto delDto = new BoardDelDto();
            delDto.setIboard(board.getId().intValue());
            boardService.delBoard(delDto.getIboard());
        }
        for (DelSelVo list : vo) {
            if (list == null) {
                Optional<Product> product = productRepository.findById(list.getIproduct());
                product.get().setStatus(ProductStatus.DELETED);
            }
            if (list.getStatus() != null && list.getStatus().equals(PaymentInfoStatus.ACTIVATED.name())) {
                throw new IllegalException(CAN_NOT_DEL_USER_EX_MESSAGE);
            }

            Optional<Product> product = productRepository.findById(list.getIproduct());
            product.get().setStatus(ProductStatus.DELETED);
        }

        for (DelSelProcVo list : procVo) {
            if (list == null) {
                Optional<Payment> payment = paymentRepository.findById(list.getIpayment());
                PaymentInfo paymentInfo = paymentInfoRepository.findByPaymentAndUser(payment.get(), user);
                paymentInfo.setStatus(PaymentInfoStatus.DELETED);
            }
            if (list != null) {
                if (list.getStatus() != null && list.getStatus().equals(PaymentInfoStatus.ACTIVATED.name())) {
                    throw new IllegalException(CAN_NOT_DEL_USER_EX_MESSAGE);
                }
                Optional<Payment> payment = paymentRepository.findById(list.getIpayment());
                PaymentInfo paymentInfo = paymentInfoRepository.findByPaymentAndUser(payment.get(), user);
                paymentInfo.setStatus(PaymentInfoStatus.DELETED);
            }
        }
        return (int) Const.SUCCESS;

    }

    /*public int patchUser(DelUserDto dto) {
        Long loginUserPk = authenticationFacade.getLoginUserPk();
        dto.setIuser(loginUserPk);

        SigninDto inDto = new SigninDto();
        inDto.setUid(dto.getUid());
        inDto.setUpw(dto.getUpw());
        UserEntity entity = mapper.selSignin(inDto);

        if (entity == null) {
            throw new NoSuchDataException(NO_SUCH_ID_EX_MESSAGE);
        } else if (!passwordEncoder.matches(dto.getUpw(), entity.getUpw())) {
            throw new NoSuchDataException(NO_SUCH_PASSWORD_EX_MESSAGE);
        }
        Integer check = mapper.selpatchUser(entity.getIuser());
        if (loginUserPk == entity.getIuser()) {
            String hashedPw = entity.getUpw();
            boolean checkPw = passwordEncoder.matches(dto.getUpw(), hashedPw);
            if (checkPw) {
                if (check != null && check != 0) {
                    throw new IllegalException(CAN_NOT_DEL_USER_EX_MESSAGE);
                } else {
                    // 채팅 개수 가져오기 && 채팅 삭제
                    if (!mapper.getUserChatCount(loginUserPk).equals(mapper.delUserChat(loginUserPk))) {
                        throw new IllegalException(CAN_NOT_DEL_USER_EX_MESSAGE);
                    }

                    List<SeldelUserPayDto> payDtos = mapper.seldelUserPay(entity.getIuser());

                    List<Integer> iproducts = new ArrayList<>();
                    List<Long> iusers = new ArrayList<>();

                    for (SeldelUserPayDto list : payDtos) {
                        iproducts.add(list.getIproduct());
                        iusers.add(list.getIuser());
                    }
                    iusers.add(loginUserPk);

                    if (!iproducts.isEmpty()) {
                        mapper.delUserProPic(iproducts);
                    }
                    mapper.delLike(iusers);
                    mapper.delRev(iusers);

                }

                int result = mapper.delUser(dto);
                if (result == 1) {
                    return (int) Const.SUCCESS;
                }
                throw new WrapRuntimeException(ILLEGAL_EX_MESSAGE);
            } else {
                throw new BadDateInfoException(AUTHENTICATION_FAIL_EX_MESSAGE);
            }
        }
        throw new BadDateInfoException(AUTHENTICATION_FAIL_EX_MESSAGE);
    }*/
    public SelUserVo getUser(Long iuser) {
        boolean checker = iuser == null || iuser == 0;
        Long actionIuser = checker ? authenticationFacade.getLoginUserPk() : iuser;

        SelUserVo vo = mapper.selUser(actionIuser);
        if (vo.getStatus().equals(UserStatus.FAILED.name()) || vo.getStatus().equals(UserStatus.HIDDEN.name()) || vo.getStatus().equals(UserStatus.DELETED.name())) {
            throw new ClientException(ErrorMessage.NO_SUCH_USER_EX_MESSAGE);
        }
        if ((!Objects.equals(iuser, authenticationFacade.getLoginUserPk()))) {
            vo.setPhone(null);
            vo.setEmail(null);
        }
        if (vo.getAuth().equals(Auth.USER.name())) {
            vo.setIauth(Auth.USER.getIauth());
        }
        return vo;
    }

    public ResVo checkUserInfo(UserCheckInfoDto dto) { // div = 1 || nick = "..."
        return new ResVo((long) checkNickOrId(dto.getDiv(), dto.getDiv() == 1 ? dto.getNick() : dto.getUid()));
    }


    private Integer checkNickOrId(Integer div, String obj) {
        Integer result = null;
        if (div == 1) {
            if (obj.contains("운영자")) {
                throw new BadInformationException(BAD_NICK_EX_MESSAGE);
            }
            result = mapper.checkUserNickUser(obj);
            if (result != 0) {
                throw new BadInformationException(BAD_NICK_EX_MESSAGE);
            }
        }
        if (div == 2) {
            result = mapper.checkUserUid(obj);
            if (result != 0) {
                throw new BadInformationException(BAD_ID_EX_MESSAGE);
            }
        }
        return result;
    }


}

