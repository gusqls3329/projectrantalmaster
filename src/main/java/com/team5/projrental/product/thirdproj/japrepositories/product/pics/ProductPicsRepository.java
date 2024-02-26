package com.team5.projrental.product.thirdproj.japrepositories.product.pics;

import com.team5.projrental.entities.ProdPics;
import com.team5.projrental.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductPicsRepository extends JpaRepository<ProdPics, Long> {
    List<ProdPics> findByProduct(Product product);


}
