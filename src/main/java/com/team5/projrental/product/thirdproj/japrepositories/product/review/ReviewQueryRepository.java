package com.team5.projrental.product.thirdproj.japrepositories.product.review;

import com.team5.projrental.entities.Product;
import com.team5.projrental.entities.Review;

import java.util.List;

public interface ReviewQueryRepository {

    List<Review> findTop4ByProduct(Product findProduct);

    List<Review> findByProductOffsetLimit(Product product, Integer page, Integer reviewPerPage);
}
