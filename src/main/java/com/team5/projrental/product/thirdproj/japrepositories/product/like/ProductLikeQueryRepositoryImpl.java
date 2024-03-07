package com.team5.projrental.product.thirdproj.japrepositories.product.like;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.projrental.entities.ProdLike;
import com.team5.projrental.entities.Product;
import com.team5.projrental.entities.User;
import com.team5.projrental.product.thirdproj.model.ProductLikeCountAndMyLikeDto;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.team5.projrental.entities.QProdLike.prodLike;

@RequiredArgsConstructor
public class ProductLikeQueryRepositoryImpl implements ProductLikeQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public ProductLikeCountAndMyLikeDto findLikeCountAndIsLikedBy(Product product, User user) {
        return null;
    }

    @Override
    public List<ProdLike> countByIuserAndInIproduct(Long iuser, List<Long> iproducts) {
        return query.selectFrom(prodLike)
                .where(prodLike.prodLikeIds.iuser.eq(iuser).and(prodLike.prodLikeIds.iproduct.in(iproducts)))
                .fetch();

    }

    @Override
    public List<Long> findByUserAndProductIn(User user, List<Product> products) {


        return user == null ? new ArrayList<>() :
                query.select(prodLike.prodLikeIds.iproduct)
                        .from(prodLike)
                        .where(prodLike.user.eq(user).and(prodLike.product.in(products)))
                        .fetch();

    }

}
