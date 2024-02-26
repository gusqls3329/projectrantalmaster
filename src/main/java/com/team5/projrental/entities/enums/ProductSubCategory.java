package com.team5.projrental.entities.enums;

import com.team5.projrental.common.exception.ErrorCode;
import com.team5.projrental.common.exception.thrid.ClientException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum ProductSubCategory {

    SMART_WATCH(ProductMainCategory.SMART, 1, "스마트 워치"),
    TABLET(ProductMainCategory.SMART, 2, "태블릿"),
    GALAXY(ProductMainCategory.SMART, 3, "갤럭시"),
    IPHONE(ProductMainCategory.SMART, 4, "아이폰"),
    //
    LAPTOP(ProductMainCategory.PC_AND_LAPTOP, 1, "노트북"),
    PC(ProductMainCategory.PC_AND_LAPTOP, 2, "PC"),
    MOUSE(ProductMainCategory.PC_AND_LAPTOP, 3, "마우스"),
    KEYBOARD(ProductMainCategory.PC_AND_LAPTOP, 4, "키보드"),
    //
    BEAM_PROJECTOR(ProductMainCategory.CAMERA, 1, "빔프로젝터"),
    SET_TOP_BOX(ProductMainCategory.CAMERA, 2, "셋톱박스"),
    CAMERA(ProductMainCategory.CAMERA, 3, "카메라"),
    CAMCORDER(ProductMainCategory.CAMERA, 4, "캠코더"),
    DSLR(ProductMainCategory.CAMERA, 5, "DSLR"),
    //
    SPEAKER(ProductMainCategory.SOUND, 1, "스피커"),
    EARPHONE(ProductMainCategory.SOUND, 2, "이어폰"),
    HEADPHONE(ProductMainCategory.SOUND, 3, "헤드폰"),
    MIC(ProductMainCategory.SOUND, 4, "마이크"),
    //
    PLAYSTATION(ProductMainCategory.GAME, 1, "플레이스테이션"),
    NINTENDO(ProductMainCategory.GAME, 2, "닌텐도"),
    WII(ProductMainCategory.GAME, 3, "Wii"),
    XBOX(ProductMainCategory.GAME, 4, "XBOX"),
    ECT(ProductMainCategory.GAME, 5, "기타");

    private ProductMainCategory mainCategory;
    private int num;
    private String name;

    ProductSubCategory(ProductMainCategory ProductMainCategory, int num, String name) {
        this.mainCategory = ProductMainCategory;
        this.num = num;
        this.name = name;
    }

    public static ProductSubCategory getByNum(ProductMainCategory mainCategory, int categoryNum) {
        return Arrays.stream(ProductSubCategory.values())
                .filter(sub -> sub.getMainCategory() == mainCategory && sub.getNum() == categoryNum)
                .findAny()
                .orElseThrow(() -> new ClientException(ErrorCode.ILLEGAL_CATEGORY_EX_MESSAGE, "잘못된 카테고리 입력"));
    }

    public static List<ProductSubCategory> getByNums(List<ProductMainCategory> mainCategories, List<Integer> categoryNums) {
        List<ProductSubCategory> result = new ArrayList<>();
        for (int i = 0; i < categoryNums.size(); i++) {
            result.add(getByNum(mainCategories.get(i), categoryNums.get(i)));
        }
        return result;
    }
}
