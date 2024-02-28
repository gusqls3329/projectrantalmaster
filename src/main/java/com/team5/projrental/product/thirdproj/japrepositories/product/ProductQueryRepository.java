package com.team5.projrental.product.thirdproj.japrepositories.product;

import com.team5.projrental.entities.Product;
import com.team5.projrental.entities.User;
import com.team5.projrental.entities.enums.ProductMainCategory;
import com.team5.projrental.entities.enums.ProductSubCategory;
import com.team5.projrental.product.model.CanNotRentalDateVo;
import com.team5.projrental.product.model.ProductListVo;
import com.team5.projrental.product.model.jpa.ActivatedStock;
import com.team5.projrental.product.thirdproj.model.ProductListForMainDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductQueryRepository {
    Map<Long, List<ActivatedStock>> getActivatedStock(LocalDateTime refDate);

    List<ProductListVo> findAllBy(Integer sort,
                                  String search,
                                  ProductMainCategory mainCategory,
                                  ProductSubCategory subCategory,
                                  String addr,
                                  int page,
                                  Long iuser,
                                  int prodPerPage);


    List<ProductListForMainDto> findEachTop8ByCategoriesOrderByIproductDesc(int limitNum);


    List<Product> findByUser(User user, Integer page);

    List<CanNotRentalDateVo> findCanNotRentalDateVoBy(Product product, LocalDate refStartDate, LocalDate refEndDate);

    Long countBySearchAndMainCategoryAndSubCategory(String search, Integer imainCategory, Integer isubCategory, String addr);

    Long findByIuser(long iuser);

    Long getReviewCount(Long iproduct);

    LocalDateTime findLastProductCreatedAtBy(User user);

    List<Product> findAllLimitPage(int page, Integer type, String search, Integer sort);

    Optional<Product> findByIdJoinFetch(Long iproduct);

    Long totalCountByOptions(Integer type, String search);

    Optional<Product> findByIdFetchUser(Long iproduct);
}
