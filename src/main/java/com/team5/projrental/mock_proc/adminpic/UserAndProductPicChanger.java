package com.team5.projrental.mock_proc.adminpic;

import com.team5.projrental.common.Const;
import com.team5.projrental.common.exception.checked.FileNotContainsDotException;
import com.team5.projrental.common.utils.MyFileUtils;
import com.team5.projrental.entities.ProdPics;
import com.team5.projrental.entities.Product;
import com.team5.projrental.entities.User;
import com.team5.projrental.product.thirdproj.japrepositories.product.ProductRepository;
import com.team5.projrental.user.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "백엔드 목데이터 작업용 - 무시하시면 됩니다!")
public class UserAndProductPicChanger {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final MyFileUtils fileUtils;

    @Operation(summary = "백엔드 목데이터 작업용 - 유저 메인 사진 변경",
            description = "백엔드 목데이터 작업용 컨트롤러 입니다.<br> 사용하지 말아주세요!<br><br><br" +
                          "유저 메인 사진 변경 - 단일 파일만 저장 가능, 기존에 존재할 경우 기존 파일 삭제하고 새로 저장함. <br>")
    @Transactional
    @PostMapping(value = "user/main-pic-change/{iuser}/{code}", consumes = "multipart/form-data")
    public String changeMainPic(@RequestPart MultipartFile pic,
                                @PathVariable String code,
                                @PathVariable Long iuser) {
        if (!code.equals("even_last_6::electro")) return "FALSE";

        User findUser = userRepository.findById(iuser).orElseThrow(RuntimeException::new);
        String storedPicPath;
        try {
            String delPath = findUser.getBaseUser().getStoredPic();
            if(delPath != null && (delPath.contains("/"))) {
                fileUtils.delFolderTrigger(delPath.substring(0, delPath.lastIndexOf("/")));
            }
            storedPicPath = fileUtils.savePic(pic, Const.CATEGORY_USER, String.valueOf(findUser.getId()));
        } catch (FileNotContainsDotException e) {
            throw new RuntimeException(e);
        }
        findUser.getBaseUser().setStoredPic(storedPicPath);

        return "SAVE_SUCCESS::" + iuser;
    }

    @Operation(summary = "백엔드 목데이터 작업용 - 상품 메인 사진 변경",
            description = "백엔드 목데이터 작업용 컨트롤러 입니다.<br> 사용하지 말아주세요!<br><br><br>" +
                          "상품 메인 사진 변경 - 단일 파일만 저장 가능, 기존에 존재할 경우 기존 파일 삭제하고 새로 저장함. <br>")
    @Transactional
    @PostMapping(value = "product/main-pic-change/{iproduct}/{code}", consumes = "multipart/form-data")
    public String changeProductPic(@RequestPart MultipartFile pic,
                                   @PathVariable String code,
                                   @PathVariable Long iproduct) {
        if (!code.equals("even_last_6::electro")) return "FALSE";

        Product findProduct = productRepository.findById(iproduct).orElseThrow(RuntimeException::new);
        String storedPicPath;
        try {
            String delPath = findProduct.getStoredPic();
            if(delPath != null && (delPath.contains("/"))) {
                fileUtils.delFolderTrigger(delPath.substring(0, delPath.lastIndexOf("/")));
            }
            storedPicPath = fileUtils.savePic(pic, Const.CATEGORY_PRODUCT_MAIN, String.valueOf(findProduct.getId()));
        } catch (FileNotContainsDotException e) {
            throw new RuntimeException(e);
        }

        findProduct.setStoredPic(storedPicPath);

        return "SAVE_SUCCESS::" + iproduct;
    }

    @Operation(summary = "백엔드 목데이터 작업용 - 상품 서브 사진 변경",
            description = "백엔드 목데이터 작업용 컨트롤러 입니다.<br> 사용하지 말아주세요!<br><br><br>" +
                          "상품 서브 사진 변경 - 최대 9개의 파일만 저장 가능, 기존에 존재할 경우 기존 파일 '모두' 삭제하고 새로 저장함. <br>")
    @Transactional
    @PostMapping(value = "product/sub-pics-change/{iproduct}/{code}", consumes = "multipart/form-data")
    public String changeSubPics(@RequestPart List<MultipartFile> pics,
                                @PathVariable String code,
                                @PathVariable Long iproduct) {
        if (!code.equals("even_last_6::electro")) return "FALSE";
        if (pics.size() > 9) return "SIZE_OVER";

        Product findProduct = productRepository.findById(iproduct).orElseThrow(RuntimeException::new);

        List<String> savePicsPath;
        try {
            findProduct.getPics().forEach(prodPics -> {
                String delPath = prodPics.getStoredPic();
                if(delPath != null && (delPath.contains("/"))) {
                    fileUtils.delFolderTrigger(delPath.substring(0, delPath.lastIndexOf("/")));
                }
            });
            savePicsPath = fileUtils.savePic(pics, Const.CATEGORY_PRODUCT_SUB, String.valueOf(findProduct.getId()));
        } catch (FileNotContainsDotException e) {
            throw new RuntimeException(e);
        }

        List<ProdPics> prodPics = new ArrayList<>();

        for (String path : savePicsPath) {
            prodPics.add(ProdPics.builder()
                    .product(findProduct)
                    .storedPic(path)
                    .build());
        }

        List<ProdPics> findPics = findProduct.getPics();
        findPics.clear();
        findPics.addAll(prodPics);

        return "SAVE_SUCCESS::" + iproduct;
    }
}
