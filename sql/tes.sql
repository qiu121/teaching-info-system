# 创建初始化数据库
CREATE DATABASE `tes_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 学生信息员账户表
CREATE TABLE `tes_db`.`student`
(
    `id`          BIGINT                                NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    `username`    VARCHAR(30)                           NOT NULL COMMENT '用户名',
    `password`    VARCHAR(100)                          NOT NULL COMMENT '用户密码',
    `college`     VARCHAR(30)                           NOT NULL COMMENT '学院',
    `class_name`  VARCHAR(10)                           NULL COMMENT '班级',
    `name`        VARCHAR(10)                           NULL COMMENT '姓名',
    `permissions` VARCHAR(20) DEFAULT 'student'         NOT NULL COMMENT '账号权限',
    `create_time` DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time` DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY (`username`)

) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4 COMMENT '学生信息员账户表';

# 教师账户表
CREATE TABLE `tes_db`.`teacher`
(
    `id`          BIGINT                                NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    `username`    VARCHAR(30)                           NOT NULL COMMENT '用户名',
    `password`    VARCHAR(100)                          NOT NULL COMMENT '用户密码',
    `permissions` VARCHAR(20) DEFAULT 'teacher'         NOT NULL COMMENT '账号权限信息',
    `create_time` DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time` DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY (`username`)

) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4 COMMENT '教师账户表';

CREATE TABLE `tes_db`.`admin`
(
    `id`          BIGINT                                NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    `username`    VARCHAR(30)                           NOT NULL COMMENT '用户名',
    `password`    VARCHAR(100)                          NOT NULL COMMENT '用户密码',
    `permissions` VARCHAR(20) DEFAULT 'admin'           NOT NULL COMMENT '账号权限信息',
    `create_time` DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time` DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY (`username`)

) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4 COMMENT '管理员账户表';

# 教学信息记录表
CREATE TABLE `tes_db`.`teach_info`
(
    `id`                    BIGINT                             NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    `submit_person`         VARCHAR(30)                        NOT NULL COMMENT '信息员账号',
    `submit_person_college` VARCHAR(30)                        NOT NULL COMMENT '信息员所在学院',
    `submit_person_class`   VARCHAR(10)                        NULL COMMENT '信息员所在班级',
    `submit_person_name`    VARCHAR(10)                        NULL COMMENT '信息员姓名',
    `course_name`           VARCHAR(50)                        NOT NULL COMMENT '课程名称',
    `teacher_name`          VARCHAR(15)                        NOT NULL COMMENT '教师姓名',
    `should_arrive_num`     TINYINT                            NOT NULL COMMENT '应到人数',
    `actual_arrive_num`     TINYINT                            NOT NULL COMMENT '实到人数',
    `class_location`        VARCHAR(20)                        NOT NULL COMMENT '上课地点',
    `record_class_date`     DATE                               NOT NULL COMMENT '记录时间-日期(_年_月_日)',
    `record_course_num`     VARCHAR(10)                        NOT NULL COMMENT '记录时间-上课节次(比如,一二节)',
    `feedback_good`         TINYTEXT                           NOT NULL COMMENT '教学信息反馈-好的方面',
    `feedback_not_enough`   TINYTEXT                           NOT NULL COMMENT '教学信息反馈-不足之处',
    `hopes_and_suggestions` TINYTEXT                           NOT NULL COMMENT '希望和建议',
    `create_time`           DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '提交时间',
    `update_time`           DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'

) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4 COMMENT '教学信息记录表';
