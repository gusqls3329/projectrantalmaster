package com.team5.projrental.adminpic;

import com.team5.projrental.common.exception.checked.FileNotContainsDotException;
import com.team5.projrental.common.utils.MyFileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AdminPicSaveOperator {

    private final MyFileUtils fileUtils;

    @PostMapping(value = "admin/pic-save", consumes = "multipart/form-data")
    public String savePic(@RequestPart MultipartFile pic) {
        try {
            fileUtils.savePic(pic, "admin", "0");
        } catch (FileNotContainsDotException e) {
            throw new RuntimeException(e);
        }


        return "OK";
    }

}
