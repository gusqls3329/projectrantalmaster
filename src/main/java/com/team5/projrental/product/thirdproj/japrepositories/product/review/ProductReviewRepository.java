package com.team5.projrental.product.thirdproj.japrepositories.product.review;

import com.team5.projrental.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReviewRepository extends JpaRepository<Review, Long>, ReviewQueryRepository {
}
