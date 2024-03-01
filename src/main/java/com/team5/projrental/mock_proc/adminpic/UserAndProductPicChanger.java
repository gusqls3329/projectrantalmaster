package com.team5.projrental.mock_proc.adminpic;

import com.team5.projrental.common.Const;
import com.team5.projrental.common.exception.checked.FileNotContainsDotException;
import com.team5.projrental.common.utils.MyFileUtils;
import com.team5.projrental.entities.ProdPics;
import com.team5.projrental.entities.Product;
import com.team5.projrental.entities.User;
import com.team5.projrental.product.thirdproj.japrepositories.product.ProductRepository;
import com.team5.projrental.user.UserRepository;
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
public class UserAndProductPicChanger {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final MyFileUtils fileUtils;

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
            fileUtils.delFolderTrigger(delPath.substring(0, delPath.lastIndexOf("/")));
            storedPicPath = fileUtils.savePic(pic, Const.CATEGORY_USER, String.valueOf(findUser.getId()));
        } catch (FileNotContainsDotException e) {
            throw new RuntimeException(e);
        }
        findUser.getBaseUser().setStoredPic(storedPicPath);

        return "SAVE_SUCCESS::" + iuser;
    }

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
            fileUtils.delFolderTrigger(delPath.substring(0, delPath.lastIndexOf("/")));
            storedPicPath = fileUtils.savePic(pic, Const.CATEGORY_PRODUCT_MAIN, String.valueOf(findProduct.getId()));
        } catch (FileNotContainsDotException e) {
            throw new RuntimeException(e);
        }

        findProduct.setStoredPic(storedPicPath);

        return "SAVE_SUCCESS::" + iproduct;
    }

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
                fileUtils.delFolderTrigger(delPath.substring(0, delPath.lastIndexOf("/")));
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
