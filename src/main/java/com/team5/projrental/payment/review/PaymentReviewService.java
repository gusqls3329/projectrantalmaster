package com.team5.projrental.payment.review;

import com.team5.projrental.common.Const;
import com.team5.projrental.common.exception.base.BadInformationException;
import com.team5.projrental.common.exception.thrid.ClientException;
import com.team5.projrental.common.security.AuthenticationFacade;
import com.team5.projrental.common.utils.CommonUtils;
import com.team5.projrental.entities.Payment;
import com.team5.projrental.entities.PaymentInfo;
import com.team5.projrental.entities.Review;
import com.team5.projrental.entities.User;
import com.team5.projrental.entities.enums.PaymentInfoStatus;
import com.team5.projrental.entities.enums.ReviewStatus;
import com.team5.projrental.entities.mappedsuper.BaseUser;
import com.team5.projrental.payment.review.model.*;
import com.team5.projrental.payment.thirdproj.PaymentRepository;
import com.team5.projrental.payment.thirdproj.paymentinfo.PaymentInfoRepository;
import com.team5.projrental.user.UserRepository;
import com.team5.projrental.user.model.CheckIsBuyer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.team5.projrental.common.exception.ErrorCode.*;
import static com.team5.projrental.common.exception.ErrorMessage.NO_SUCH_REVIEW_EX_MESSAGE;
import static com.team5.projrental.common.exception.ErrorMessage.REVIEW_ALREADY_EXISTS_EX_MESSAGE;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentReviewService {
    private final UserRepository usersRepository;
    private final ReviewRepository reviewRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentInfoRepository paymentInfoRepository;

    private final PaymentReviewMapper reviewMapper;
    private final AuthenticationFacade authenticationFacade;

    @Transactional
    public int postReview(RivewDto dto) {
        Long loginUserPk = authenticationFacade.getLoginUserPk();
        dto.setIuser(loginUserPk);

        // 0: 리뷰를 작성하지 않음
        // null: not null
        // contents 도 not null

        //결제상태가 완료됨 일때만 리뷰쓸수 있도록

        Optional<User> userOptional = usersRepository.findById(loginUserPk);
        User user = userOptional.get();
        Optional<Payment> paymentOptional = paymentRepository.findById(dto.getIpayment().longValue());
        Payment payment = paymentOptional.get();
        PaymentInfo PaymentEntity = paymentInfoRepository.findByPaymentAndUser(paymentOptional.get(), userOptional.get());
        //String istatus = reviewMapper.selReIstatus(dto.getIpayment(), loginUserPk);
        if (PaymentEntity.getStatus() == null) {
            throw new ClientException(ILLEGAL_EX_MESSAGE);
        }
        log.info("PaymentEntity.getStatus() : {}", PaymentEntity.getStatus());
        if (PaymentEntity.getStatus().equals(PaymentInfoStatus.COMPLETED)) {
            //로그인한 유저가 리뷰를 적었던건지 확인하는것
            Integer selReview = reviewRepository.countByPaymentAndUser(payment, user);
            //int selReview = reviewMapper.selReview(loginUserPk, payment.getId().intValue());
            if (selReview == 0) {
                payment = paymentRepository.selBuyRew(payment.getId(), loginUserPk);
                //CheckIsBuyer buyCheck = reviewRepository.countIsExistsAndIsBuyer(loginUserPk,payment.getId());
                //CheckIsBuyer buyCheck = reviewMapper.selBuyRew(loginUserPk, payment.getId().intValue());
                CommonUtils.ifAnyNullThrow(BadInformationException.class, BAD_INFO_EX_MESSAGE, payment);
                if (payment == null) {
                    throw new BadInformationException(ILLEGAL_EX_MESSAGE);
                }

                //int result = reviewMapper.insReview(dto);
                Review review = Review.builder()
                        .payment(payment)
                        .user(user)
                        .contents(dto.getContents())
                        .status(ReviewStatus.ACTIVATED)
                        .rating(dto.getRating())
                        .build();


                if (payment != null) {

                    if (dto.getRating() == null && (dto.getContents() == null || dto.getContents().equals(""))) {
                        throw new BadInformationException(ILLEGAL_EX_MESSAGE);
                    }
                    if (dto.getRating() == null) {
                        Review review1 = reviewRepository.save(review);
                        dto.setIreview(review1.getId().intValue());
                        return dto.getIreview();
                    }
                    user = paymentRepository.selUser(dto.getIpayment().longValue());
                    //int chIuser = reviewMapper.selUser(dto.getIpayment());
                    SelRatVo vo = paymentRepository.selRat(user.getId());
                    // SelRatVo vo = reviewMapper.selRat(user.getId().intValue());
                    double average = (vo.getCountIre()) * vo.getRating();
                    double averageRat = Math.round(((average + dto.getRating()) / (vo.getCountIre() + 1)) * 10.0) / 10.0;
                    UpRating uprating = new UpRating();
                    uprating.setIuser(user.getId().intValue());
                    uprating.setRating(averageRat);
                    user.getBaseUser().setRating(uprating.getRating());
                    user.setId(user.getId());
                    //int r = reviewMapper.upRating(uprating);
                    reviewRepository.save(review);
                }
                return review.getId().intValue();
            }
            throw new BadInformationException(REVIEW_ALREADY_EXISTS_EX_MESSAGE);
        }
        throw new BadInformationException(BAD_PRODUCT_ISTATUS_EX_MESSAGE);
    }

    @Transactional
    public int patchReview(UpRieDto dto) { //구매자면 잘못된요청
        CommonUtils.ifAllNullThrow(BadInformationException.class, BAD_INFO_EX_MESSAGE,
                dto.getContents(), dto.getRating());
        Long loginUserPk = authenticationFacade.getLoginUserPk();
        dto.setIuser(loginUserPk);
        //수정하려는 유저가 구매자가 맞는지
        //RiviewVo check = reviewMapper.selPatchRev(dto.getIreview());
        Optional<Review> check = reviewRepository.findById(dto.getIreview().longValue());
        if (check.isEmpty()) {
            throw new BadInformationException(BAD_INFO_EX_MESSAGE);
        }
        Payment payment = paymentRepository.selBuyRew(check.get().getPayment().getId(), loginUserPk);
        CommonUtils.ifAnyNullThrow(BadInformationException.class, BAD_INFO_EX_MESSAGE, payment);
        if (payment == null) {
            throw new BadInformationException(ILLEGAL_EX_MESSAGE);
        }
        //CheckIsBuyer buyCheck = reviewMapper.selBuyRew(loginUserPk, check.get().getPayment().getId().intValue());
        CommonUtils.ifAnyNullThrow(BadInformationException.class, BAD_INFO_EX_MESSAGE, payment);
        Optional<User> userOptional = usersRepository.findById(loginUserPk);
        Review review = reviewRepository.findById(dto.getIreview().longValue()).get();

        //수정전 리뷰를 작성한 사람이 iuser가 맞는지 확인


        if (dto.getRating() == null && (dto.getContents() == null || dto.getContents().equals(""))) {
            throw new BadInformationException(ILLEGAL_EX_MESSAGE);
        }
        User user = paymentRepository.selUser(review.getPayment().getId());
        if(dto.getRating() == null){
            dto.setRating(review.getRating());
        }
        if(user.getBaseUser().getRating() == 0){
            review.setRating(dto.getRating() == null ? 0 : dto.getRating());
            review.setContents(dto.getContents() == null ? review.getContents() : dto.getContents());
            user.getBaseUser().setRating(dto.getRating().doubleValue());
            return (int) Const.SUCCESS;
        }
        //int chIuser = reviewMapper.selUser(dto.getIpayment());
        SelRatVo vo = paymentRepository.selRat(user.getId());
        // SelRatVo vo = reviewMapper.selRat(user.getId().intValue());
        double average = (vo.getCountIre()) * vo.getRating();

        double averageRat = Math.round((((average-review.getRating()) + dto.getRating()) / vo.getCountIre()) * 10.0) / 10.0;
        UpRating uprating = new UpRating();
        uprating.setIuser(user.getId().intValue());
        uprating.setRating(averageRat);
        user.getBaseUser().setRating(uprating.getRating());

        BaseUser baseUser = user.getBaseUser();
        baseUser.setRating(uprating.getRating());
        user.setBaseUser(baseUser);


        review.setRating(dto.getRating() == null ? review.getRating() : dto.getRating());
        review.setContents(dto.getContents() == null ? review.getContents() : dto.getContents());
        //int r = reviewMapper.upRating(uprating);
        //reviewRepository.save(review);
        return (int) Const.SUCCESS;

    }

    @Transactional
    public int delReview(DelRivewDto dto) {
        Long loginUserPk = authenticationFacade.getLoginUserPk();
        dto.setIuser(loginUserPk);
        //삭제전 리뷰를 작성한 사람이 iuser가 맞는지 확인

        User user = usersRepository.findById(loginUserPk).get();
        Review check = reviewRepository.findByUser(user);
        if (check == null) {
            throw new BadInformationException(BAD_INFO_EX_MESSAGE);
        }
        if (check.getUser().getId().equals(loginUserPk)) {
            Review review = reviewRepository.findById((long) dto.getIreview()).get();
            user = paymentRepository.selUser(review.getPayment().getId());
            //int chIuser = reviewMapper.selUser(dto.getIpayment());
            SelRatVo vo = paymentRepository.selRat(user.getId());
            // SelRatVo vo = reviewMapper.selRat(user.getId().intValue());
            double average = vo.getCountIre() * vo.getRating();
            double v = (average - review.getRating()) / vo.getCountIre();
            double averageRat = Math.round(v * 10) / 10.0;
            UpRating uprating = new UpRating();
            uprating.setIuser(user.getId().intValue());
            uprating.setRating(averageRat);
            BaseUser baseUser = user.getBaseUser();
            baseUser.setRating(uprating.getRating());
            user.setBaseUser(baseUser);
            review.setStatus(ReviewStatus.DELETED);
            return (int) Const.SUCCESS;
        }

        throw new BadInformationException(ILLEGAL_EX_MESSAGE);
    }

}