package com.team5.projrental.product.like;

import com.team5.projrental.common.model.ResVo;
import com.team5.projrental.product.model.ProductToggleFavDto;
import com.team5.projrental.product.thirdproj.model.ProductListForMainDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductLikeMapper {


    int insFav(ProductToggleFavDto dto);
    int delFav(ProductToggleFavDto dto);
    int delFavForDelUser(ProductToggleFavDto dto);

}
