package com.team5.projrental.product.model.review;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResultVo {

    private Long ireview;
    private String contents;
    private Integer rating;
    private Long iuser;
    @JsonIgnore // todo jpa 완성후 삭제
    private Integer auth;
    private String nick;
    private String userProfPic;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer imainCategory;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer isubCategory;
    private Integer status;
}
