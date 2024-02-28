package com.team5.projrental.product.thirdproj.japrepositories.product;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.projrental.entities.HashTag;
import com.team5.projrental.entities.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import static com.team5.projrental.entities.QDisputeProduct.disputeProduct;
import static com.team5.projrental.entities.QHashTag.hashTag;

@Slf4j
@RequiredArgsConstructor
public class HashTagQueryRepositoryImpl implements HashTagQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public List<HashTag> findByOpt(List<Product> product, String search) {

        return query.selectFrom(hashTag)
                .where(whereClausFindByOpt(product, search))
                .fetch();
    }

    private BooleanBuilder whereClausFindByOpt(List<Product> product, String search) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(hashTag.product.in(product));
        if (search != null) {
            String searchClaus = search;

            if (search.charAt(0) == '#') {
                searchClaus = searchClaus.replaceFirst("#", "");
            }
            builder.and(hashTag.tag.like("%" + searchClaus + "%"));
        }
        return builder;

    }
}
