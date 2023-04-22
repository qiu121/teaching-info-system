package com.github.qiu121.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/04/18
 * @description 生成二维码入口
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/QRcode")
public class QRCodeController {
    @GetMapping(produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generate(@RequestParam("url") String url) throws IOException, WriterException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Map<EncodeHintType, Object> hint = new HashMap<>();

        hint.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8);
        hint.put(EncodeHintType.MARGIN, 0);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 200, 200, hint);
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", stream);

        byte[] imageBytes = stream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(imageBytes.length);

        log.info("二维码已生成,内容为: {}", url);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        //final String decode = URLEncoder.encode("baidu.com", StandardCharsets.UTF_8);
        //System.out.println(decode);

        String[] imageTypes = ImageIO.getReaderFileSuffixes();
        for (String imageType : imageTypes) {
            System.out.println(imageType);
        }
    }

}
