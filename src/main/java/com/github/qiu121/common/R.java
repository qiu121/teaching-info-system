package com.github.qiu121.common;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/3/14
 */
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
public class R<T> {
    @NonNull
    private Integer code;
    @NonNull
    private String msg;
    private T data;

}
