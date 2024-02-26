package com.team5.projrental.product.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.team5.projrental.product.model.proc.GetProductResultDto;
import com.team5.projrental.product.model.proc.PicsInfoVo;
import com.team5.projrental.product.model.review.ReviewResultVo;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class ProductVo extends ProductListVo{

    // 추가필드만 작성
    private String contents;
    private List<PicsInfoVo> prodSubPics;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnore
    private LocalDate buyDate;
    private Double x;
    private Double y;
//    private Integer isLiked;
    private List<ReviewResultVo> reviews;
    private Integer prodLike;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<LocalDate> disabledDates;

    public ProductVo(GetProductResultDto dto) {
        super(dto);

        this.contents = dto.getContents();
        this.buyDate = dto.getBuyDate();
        this.x = dto.getX();
        this.y = dto.getY();
    }
}
