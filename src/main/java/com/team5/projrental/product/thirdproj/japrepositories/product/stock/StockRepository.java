package com.team5.projrental.product.thirdproj.japrepositories.product.stock;

import com.team5.projrental.entities.Product;
import com.team5.projrental.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long>, StockQueryRepository{



    Integer countByProduct(Product product);
}
