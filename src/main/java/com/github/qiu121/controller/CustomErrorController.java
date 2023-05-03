package com.github.qiu121.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/4/28
 * @description 处理404
 */
@RestController
@Slf4j
@CrossOrigin
public class CustomErrorController implements ErrorController {
    @RequestMapping("/error")
    public ResponseEntity<byte[]> handleError(HttpServletRequest request) throws IOException {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        log.info("statusCode :{}", status);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                File directory = new ClassPathResource("static/images/404-images").getFile();
                File[] files = directory.listFiles((dir, name) -> name.endsWith(".png"));
                if (files != null && files.length > 0) {
                    int randomIndex = new Random().nextInt(files.length);
                    byte[] imageBytes = Files.readAllBytes(files[randomIndex].toPath());
                    return ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .contentType(MediaType.IMAGE_PNG)
                            .body(imageBytes);
                }
            }
        }

        return ResponseEntity.badRequest().build();
    }

}
