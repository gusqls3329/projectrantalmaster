package com.team5.projrental.product.thirdproj.japrepositories.product.stock;

import com.team5.projrental.entities.Product;

import java.util.List;

public interface StockQueryRepository {

    List<Long> countByProducts(List<Product> products);
}
