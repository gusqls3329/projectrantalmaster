package com.team5.projrental.product.thirdproj.japrepositories.product;

import com.team5.projrental.entities.HashTag;
import com.team5.projrental.entities.Product;
import com.team5.projrental.entities.QProduct;

import java.util.List;

public interface HashTagQueryRepository  {
    List<HashTag> findByOpt(List<Product> product, String search);
}
