package com.team5.projrental.product.thirdproj.japrepositories.product;

import com.team5.projrental.entities.Product;
import com.team5.projrental.entities.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductQueryRepository {

    @Query("select p from Product p where p.id in (:iproducts)")
    List<Product> findByIdIn(List<Long> iproducts);


    @Query("select p from Product p join fetch p.user where p.id = :iproduct and p.user.id = :iuser")
    Optional<Product> findByIdAndIuser(Long iproduct, Long iuser);

    @Query("select p from Product p left join fetch p.prodLikes where p.id = :iproduct")
    Optional<Product> findByIproduct(Long iproduct);

    @Query("select distinct p from Product p join fetch p.stocks where p.id = :iproduct and p.status in (:status)")
    Optional<Product> findByIdJoinStock(Long iproduct, ProductStatus... status);

    @Query("select p from Product p join fetch p.user where p.id = :iproduct")
    Optional<Product> findJoinFetchById(Long iproduct);
}
