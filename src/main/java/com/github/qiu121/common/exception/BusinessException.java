package com.github.qiu121.common.exception;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/04/04
 * @description 自定义业务异常
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 自定义异常没必要显示层级堆栈，不写入堆栈信息，提高性能
     *
     * @return
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
