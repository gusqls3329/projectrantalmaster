package com.team5.projrental.mock_proc.adminpic;

import com.team5.projrental.admin.AdminRepository;
import com.team5.projrental.common.exception.checked.FileNotContainsDotException;
import com.team5.projrental.common.utils.MyFileUtils;
import com.team5.projrental.entities.Admin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AdminPicSaveOperator {

    private final MyFileUtils fileUtils;
    private final AdminRepository adminRepository;
    @Value("${file.base-package}")
    private String basePath;

    @PostMapping(value = "admin/pic-save/{code}", consumes = "multipart/form-data")
    public String savePic(@PathVariable String code, @RequestPart MultipartFile pic) {
        if (!code.equals("even_last_6::electro")) return "FALSE";

        File path = new File(basePath + "/admin/0");
        File[] files = path.listFiles();
        if (files != null && files.length > 0) {
            if (files[0].getName().equals("logo.jpg")) {
                return "EXISTS";
            }
        }

        try {
            fileUtils.savePic(pic, "admin", "0");
        } catch (FileNotContainsDotException e) {
            throw new RuntimeException(e);
        }
        File pathAfterSave = new File(basePath + "/admin/0");
        File[] pathsAfterSave = path.listFiles();
        if (pathsAfterSave != null && pathsAfterSave.length > 0) {

            Path afterPath = Paths.get(basePath + "/admin/0/logo.jpg");
            try {
                Path move = Files.move(pathsAfterSave[0].toPath(), afterPath);
                System.out.println("move = " + move.getFileName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        return "OK";
    }


    @Transactional
    @GetMapping("/admin/pic/setting/{code}")
    public String setting(@PathVariable String code) {
        if (!code.equals("even_last_6::electro")) return "FALSE";

        adminRepository.findAll().forEach(admin -> {
            admin.setStoredAdminPic(basePath + "/admin/0/logo.jpg");
        });

        return "OK";
    }
}
