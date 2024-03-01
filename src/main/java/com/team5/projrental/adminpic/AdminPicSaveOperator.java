package com.team5.projrental.adminpic;

import com.team5.projrental.common.exception.checked.FileNotContainsDotException;
import com.team5.projrental.common.utils.MyFileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AdminPicSaveOperator {

    private final MyFileUtils fileUtils;
    @Value("${file.package}")
    private String basePath;

    @PostMapping(value = "admin/pic-save/{code}", consumes = "multipart/form-data")
    public String savePic(@PathVariable String code, @RequestPart MultipartFile pic) {
        if (!code.equals("even_last_6::electro")) return "FALSE";

        File path = new File(basePath + "/admin/0");
        File[] files = path.listFiles();
        if (files != null && files.length > 0) {
            return "EXISTS";
        }

        try {
            fileUtils.savePic(pic, "admin", "0");
        } catch (FileNotContainsDotException e) {
            throw new RuntimeException(e);
        }


        return "OK";
    }

}
