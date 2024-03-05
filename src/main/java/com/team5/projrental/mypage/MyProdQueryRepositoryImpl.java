package com.team5.projrental.mypage;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.projrental.entities.Product;
import com.team5.projrental.entities.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.team5.projrental.common.Const.PROD_PER_PAGE;
import static com.team5.projrental.entities.QBoard.board;
import static com.team5.projrental.entities.QProduct.product;

@Slf4j
@RequiredArgsConstructor
public class MyProdQueryRepositoryImpl implements MyProdQueryRepository{

    private final JPAQueryFactory query;
    @Override
    public List<Product> findByUser(User user, Integer page) {
        return query.select(product)
                .from(product)
                .join(product.user).fetchJoin()
                .leftJoin(product.prodLikes).fetchJoin()
                .where(product.user.eq(user))
                .offset(page)
                .limit(PROD_PER_PAGE)
                .fetch();

    }
}
