package com.github.qiu121.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.qiu121.pojo.TeachInfo2;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="mailto:qiu0089@foxmail.com">qiu121</a>
 * @version 1.0
 * @date 2023/4/10
 */
@Transactional(rollbackFor = Throwable.class)
public interface TeachInfoService2 extends IService<TeachInfo2> {

}
