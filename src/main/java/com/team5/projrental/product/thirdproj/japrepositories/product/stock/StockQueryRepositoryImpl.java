package com.team5.projrental.product.thirdproj.japrepositories.product.stock;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.projrental.entities.Product;
import com.team5.projrental.entities.Stock;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.team5.projrental.entities.QStock.stock;

@RequiredArgsConstructor
public class StockQueryRepositoryImpl implements StockQueryRepository {

    private final JPAQueryFactory query;


    @Override
    public List<Long> countByProducts(List<Product> products) {

        return query.select(stock.id)
                .from(stock)
                .where(stock.product.in(products))
                .fetch();
    }
}
