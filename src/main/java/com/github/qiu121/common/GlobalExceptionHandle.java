package com.github.qiu121.common;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/3/14
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandle {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R<Object> handleException(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage());
        return new R<>(HttpStatus.HTTP_BAD_REQUEST, "操作不允许,请重试");
    }

    @ExceptionHandler(cn.dev33.satoken.exception.SaTokenException.class)
    public SaResult handleSaException(cn.dev33.satoken.exception.SaTokenException e) {
        String message;
        if (e instanceof NotLoginException) {
            log.error("账号未登录: {}", e.getMessage());
            if (((NotLoginException) e).getType().equals(NotLoginException.TOKEN_TIMEOUT_MESSAGE)) {
                message = "账号登录已过期，请重新登录";
            } else {
                message = "请登录后再进行操作";
            }
            return SaResult.error(message);
        }
        if (e instanceof NotPermissionException) {
            message = "当前用户无访问权限";
        } else {
            message = "权限异常！";
        }
        return SaResult.error(message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Object> handleException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append(fieldError.getField())
                    .append(":")
                    .append(fieldError.getDefaultMessage()).append(", ");
        }
        String msg = builder.toString();
        return new R<>(HttpStatus.HTTP_BAD_REQUEST, msg);

    }

    @ExceptionHandler(NullPointerException.class)
    public R<Object> handleException(NullPointerException e) {
        log.error(e.getMessage());
        return new R<>(HttpStatus.HTTP_BAD_GATEWAY, "操作异常");
    }

    @ExceptionHandler(org.springframework.dao.DuplicateKeyException.class)
    public R<Object> handleException(org.springframework.dao.DuplicateKeyException e) {
        log.error(e.getMessage());
        return new R<>(HttpStatus.HTTP_BAD_GATEWAY, "账号已存在,不可重复添加");
    }

    @ExceptionHandler(RuntimeException.class)
    public R<Object> handleException(RuntimeException e) {
        log.error(e.getMessage());
        return new R<>(HttpStatus.HTTP_BAD_GATEWAY, "系统繁忙，请稍后再试");
    }
}
