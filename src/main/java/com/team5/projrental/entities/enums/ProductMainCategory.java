package com.team5.projrental.entities.enums;

import com.team5.projrental.common.exception.ErrorCode;
import com.team5.projrental.common.exception.thrid.ClientException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum ProductMainCategory {

    SMART(1), PC_AND_LAPTOP(2), CAMERA(3),
    SOUND(4), GAME(5), ECT(6);
    private int num;

    ProductMainCategory(int num) {
        this.num = num;
    }

    public static ProductMainCategory getByNum(int categoryNum) {
        return Arrays.stream(ProductMainCategory.values())
                .filter(e -> e.getNum() == categoryNum)
                .findAny()
                .orElseThrow(() -> new ClientException(ErrorCode.ILLEGAL_CATEGORY_EX_MESSAGE, "잘못된 카테고리 입력"));
    }

    public static List<ProductMainCategory> getByNums(List<Integer> categoryNums) {
        List<ProductMainCategory> result = new ArrayList<>();
        for (Integer categoryNum : categoryNums) {
            result.add(getByNum(categoryNum));
        }
        return result;
    }

}
