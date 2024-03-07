package com.team5.projrental.product.thirdproj.japrepositories.product.like;

import com.team5.projrental.entities.ProdLike;
import com.team5.projrental.entities.Product;
import com.team5.projrental.entities.ids.ProdLikeIds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductLikeRepository extends JpaRepository<ProdLike, ProdLikeIds>, ProductLikeQueryRepository {


    @Query("select pl.product.id from ProdLike pl where pl.product in (:findProducts)")
    List<Long> findByProductIn(List<Product> findProducts);
}
