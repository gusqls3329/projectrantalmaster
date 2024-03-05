package com.team5.projrental.mypage;

import com.team5.projrental.entities.BoardComment;
import com.team5.projrental.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyProductRepository extends JpaRepository<Product, Long>, MyProdQueryRepository{

}
