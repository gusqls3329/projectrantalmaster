package com.team5.projrental.mypage;

import com.team5.projrental.entities.Product;
import com.team5.projrental.entities.User;

import java.util.List;

public interface MyProdQueryRepository {
    List<Product> findByUser(User user, Integer page);
}
