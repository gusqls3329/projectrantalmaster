package com.team5.projrental.product.thirdproj.japrepositories.product.review;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.projrental.common.Const;
import com.team5.projrental.entities.Product;
import com.team5.projrental.entities.Review;
import com.team5.projrental.entities.enums.ReviewStatus;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.team5.projrental.entities.QPayment.payment;
import static com.team5.projrental.entities.QProduct.product;
import static com.team5.projrental.entities.QReview.review;

@RequiredArgsConstructor
public class ReviewQueryRepositoryImpl implements ReviewQueryRepository{

    private final EntityManager em;
    private final JPAQueryFactory query;

    @Override
    public List<Review> findTop4ByProduct(Product findProduct) {

        // product -> payment , payment , payment ... -> each -> review

        return query.select(review)
                .from(product)
                .join(payment).fetchJoin().on(payment.product.eq(findProduct).and(payment.product.eq(product)))
                .join(review).fetchJoin().on(review.payment.eq(payment))
                .where(product.eq(findProduct))
                .limit(Const.IN_PRODUCT_REVIEW_PER_PAGE)
                .fetch();
    }

    @Override
    public List<Review> findByProductOffsetLimit(Product product, Integer page, Integer reviewPerPage) {

        return query.selectFrom(review)
                .where(review.payment.product.eq(product).and(review.status.eq(ReviewStatus.ACTIVATED)))
                .offset(page)
                .limit(reviewPerPage)
                .fetch();

    }
}
