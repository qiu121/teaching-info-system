package com.github.qiu121.common;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.http.HttpStatus;
import com.github.qiu121.common.exception.BusinessException;
import com.github.qiu121.common.exception.DuplicateException;
import com.github.qiu121.common.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/3/14
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandle {

    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    public R<Object> handleException(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage());
        return new R<>(HttpStatus.HTTP_BAD_REQUEST, "操作不允许,请重试");
    }

    @ExceptionHandler(cn.dev33.satoken.exception.SaTokenException.class)
    public SaResult handleSaException(SaTokenException e) {
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

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
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
    public R<Object> handleException(DuplicateKeyException e) {
        log.error(e.getMessage());
        return new R<>(HttpStatus.HTTP_BAD_GATEWAY, "记录已存在,不可重复添加");
    }

    @ExceptionHandler(org.springframework.web.bind.MissingPathVariableException.class)
    public R<Object> handleException(MissingPathVariableException e) {
        log.error(e.getMessage());
        return new R<>(HttpStatus.HTTP_BAD_GATEWAY, "接口方法请求异常");
    }

    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    public R<Object> handleException(MethodArgumentTypeMismatchException e) {
        log.error(e.getMessage());
        return new R<>(HttpStatus.HTTP_BAD_GATEWAY, "操作异常，数据类型不匹配");
    }

    @ExceptionHandler(Throwable.class)
    public R<Object> handleException(Throwable e) {

        int status = HttpStatus.HTTP_INTERNAL_ERROR;
        String message = "系统内部异常，请稍后再试";

        if (e instanceof BusinessException) {
            status = HttpStatus.HTTP_BAD_GATEWAY;
            message = e.getMessage();
        } else if (e instanceof NotFoundException) {
            status = HttpStatus.HTTP_NOT_FOUND;
            message = e.getMessage();
        } else if (e instanceof DuplicateException) {
            status = HttpStatus.HTTP_UNAVAILABLE;
            message = e.getMessage();
        }
        log.info("异常类型： {}", message);
        return new R<>(status, message);
    }

}
