package com.aizuda.boot.modules.common.controller;

import com.aizuda.oss.IFileStorage;
import com.aizuda.oss.model.OssResult;
import com.baomidou.kisso.annotation.LoginIgnore;
import com.baomidou.kisso.annotation.Permission;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Tag(name = "oss文件存储")
@Slf4j
@RestController
@RequestMapping("/v1/oss")
public class OssController {
    @Resource
    private IFileStorage fileStorage;

    @Permission(ignore = true)
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public OssResult upload(@RequestPart("file") MultipartFile file) {
        // 其它参数 @ParameterObject AttachmentDTO dto
        OssResult ossResult = null;
        try {
            ossResult = fileStorage.upload(file);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ossResult;
    }

    @LoginIgnore
    @Permission(ignore = true)
    @GetMapping("/download")
    public void download(HttpServletResponse response, String objectName) {
        try {
            String contentType = null;
            if (objectName.contains(".jpeg")) {
                contentType = MediaType.IMAGE_JPEG_VALUE;
            } else if (objectName.contains(".gif")) {
                contentType = MediaType.IMAGE_GIF_VALUE;
            } else if (objectName.contains(".png")) {
                contentType = MediaType.IMAGE_PNG_VALUE;
            }
            if (null != contentType) {
                response.setContentType(contentType);
            }
            response.setHeader("Content-Disposition", "attachment;filename=" +
                    URLEncoder.encode(objectName, StandardCharsets.UTF_8));
            fileStorage.download(response, objectName);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Permission(ignore = true)
    @GetMapping("/delete")
    public boolean delete(String objectName) {
        boolean result = false;
        try {
            result = fileStorage.delete(objectName);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }
}
