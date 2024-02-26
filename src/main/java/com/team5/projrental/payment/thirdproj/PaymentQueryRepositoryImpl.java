package com.team5.projrental.payment.thirdproj;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.projrental.entities.Payment;
import com.team5.projrental.entities.User;
import com.team5.projrental.entities.mappedsuper.BaseUser;
import com.team5.projrental.payment.review.model.QSelRatVo;
import com.team5.projrental.payment.review.model.SelRatVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.team5.projrental.entities.QPayment.payment;
import static com.team5.projrental.entities.QProduct.product;
import static com.team5.projrental.entities.QReview.review;
import static com.team5.projrental.entities.QUser.user;
import static com.team5.projrental.entities.inheritance.QUsers.users;
import static com.team5.projrental.entities.mappedsuper.QBaseUser.baseUser;

@Slf4j
@RequiredArgsConstructor
public class PaymentQueryRepositoryImpl implements PaymentQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public Payment selBuyRew(Long ipayment, Long iuser) {
        Payment payments = query.selectFrom(payment)
                .where(payment.id.eq(ipayment).and(payment.user.id.eq(iuser))
                ).fetchOne();

        return payments;
    }

    @Override
    public User selUser(Long ipayment) {
        User user = query.select(payment.product.user)
                .from(payment).join(payment.product)
                .where(payment.id.eq(ipayment)).fetchOne();

        return user;
        /*User user= query.select(payment.user).from(payment.product)
                .where(payment.id.eq(ipayment)).fetchOne();

        return user;*/
    }
    /*
     SELECT COUNT(R.ireview) as countIre, U.rating
        from product P
        JOIN payment PA
        ON P.iproduct = PA.iproduct
        JOIN review R
        ON R.ipayment = PA.ipayment
        JOIN user U
        ON U.iuser = P.iuser
        AND R.rating IS NOT NULL and R.rating > 0
        WHERE P.iuser = #{iuser}만들자
     */
    @Override
    public SelRatVo selRat(Long iuser) {
        SelRatVo vo = query.select(Projections.fields(SelRatVo.class,
                review.id.count().as("countIre"),
                baseUser.rating.as("rating")))
                .from(product)
                .join(payment).on(product.id.eq(payment.product.id))
                .join(review).on(payment.id.eq(review.payment.id))
                .join(user).on(product.user.id.eq(user.id))
                .where(product.user.id.eq(iuser).and(review.rating.gt(0)))
                .fetchOne();

        return vo;
        /*SelRatVo vo = query.select(new QSelRatVo(review.id.count(),
                        review.payment.product.user.baseUser.rating))
                .from(review)
                .join(review.payment)
                .join(review.payment.product)
                .join(review.payment.product.user)
                .where(review.payment.user.id.eq(iuser).and(review.rating.gt(0)))
                .fetchOne();


            return vo;*/

    }
}
