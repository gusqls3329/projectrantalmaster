package com.team5.projrental.product.thirdproj.japrepositories.product.like;

import com.team5.projrental.entities.ProdLike;
import com.team5.projrental.entities.Product;
import com.team5.projrental.entities.User;
import com.team5.projrental.product.thirdproj.model.ProductLikeCountAndMyLikeDto;

import java.util.List;

public interface ProductLikeQueryRepository {

    ProductLikeCountAndMyLikeDto findLikeCountAndIsLikedBy(Product product, User user);


    List<ProdLike> countByIuserAndInIproduct(Long iuser, List<Long> iproducts);


    List<Long> findByUserAndProductIn(User user, List<Product> products);

}
