# 创建初始化数据库
CREATE DATABASE `tes_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 学生信息员账户表
CREATE TABLE `tes_db`.`student`
(
    `id`              BIGINT                             NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    `username`        VARCHAR(30)                        NOT NULL COMMENT '用户名',
    `password`        VARCHAR(100)                       NOT NULL COMMENT '用户密码',
    `college`         VARCHAR(30)                        NOT NULL COMMENT '学院',
    `education_level` VARCHAR(30)                        NOT NULL COMMENT '层次级别(本、专)',
    `enrollment_year` SMALLINT                           NOT NULL COMMENT '入学年份(年级)',
    `class_name`      VARCHAR(30)                        NULL COMMENT '班级',
    `name`            VARCHAR(10)                        NULL COMMENT '姓名',
    `create_time`     DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time`     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY (`username`)

) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4 COMMENT '学生信息员账户表';

# 信息员组长账户表
CREATE TABLE `tes_db`.`stu_admin`
(
    `id`              BIGINT                                NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    `username`        VARCHAR(30)                           NOT NULL COMMENT '用户名',
    `password`        VARCHAR(100)                          NOT NULL COMMENT '用户密码',
    `college`         VARCHAR(30)                           NOT NULL COMMENT '学院',
    `education_level` VARCHAR(30) DEFAULT '本科'            NOT NULL COMMENT '层次级别(本、专)',
    `enrollment_year` SMALLINT                              NOT NULL COMMENT '入学年份(年级)',
    `class_name`      VARCHAR(30)                           NULL COMMENT '班级',
    `name`            VARCHAR(10)                           NULL COMMENT '姓名',
    `create_time`     DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time`     DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY (`username`)

) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4 COMMENT '信息员组长账户表';

# 管理员账户表
CREATE TABLE `tes_db`.`admin`
(
    `id`          BIGINT                             NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    `username`    VARCHAR(30)                        NOT NULL COMMENT '用户名',
    `password`    VARCHAR(100)                       NOT NULL COMMENT '用户密码',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY (`username`)

) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4 COMMENT '管理员账户表';

# 账户权限表
CREATE TABLE `tes_db`.`permission`
(
    `id`          BIGINT                             NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    `username`    VARCHAR(30)                        NOT NULL COMMENT '用户名',
    `type`        VARCHAR(15)                        NOT NULL COMMENT '权限级别',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '提交时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY (`username`)

) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4 COMMENT '账户权限记录表';

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
  DEFAULT CHARACTER SET utf8mb4 COMMENT '教学信息记录表(信息员提交)';

DROP TABLE IF EXISTS `tes_db`.`college`;
CREATE TABLE `tes_db`.`college`
(
    `id`          BIGINT                                 NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name`        VARCHAR(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '学院名称',
    `create_time` DATETIME                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME                               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='学院记录表';

INSERT INTO `tes_db`.`college`(`name`)
VALUES ('机械与车辆工程学院'),
       ('经济与管理学院'),
       ('信息与人工智能学院'),
       ('建筑与环境工程学院'),
       ('艺术与传媒设计学院'),
       ('马克思主义学院'),
       ('体育与教育学院'),
       ('教育学院')
;

INSERT INTO `tes_db`.`student` (`username`, `password`, `college`, `enrollment_year`, `education_level`)
VALUES ('test', '248ef48fc412eac3ee683d4ade175a92db95ee9a4e42cfb27c1790130adcbbfd', '教育学院', 2020, '本科');
INSERT INTO `tes_db`.`permission`(`username`, `type`)
VALUES ('test', 'stu');

INSERT INTO `tes_db`.`stu_admin` (`username`, `password`, `college`, `enrollment_year`, `education_level`)
VALUES ('stuadmin', 'ba458532cde997bc5258287ad40911a57e110a4db3854d1f664309598f2a3cf4', '教育学院', 2020, '本科');
INSERT INTO `tes_db`.`permission`(`username`, `type`)
VALUES ('test', 'stuAdmin');

INSERT INTO `tes_db`.`admin` (`username`, `password`)
VALUES ('admin', '3e673637f4bc2e4aa7496ad884df94c4fbbb8ed7a433b8692f4363829c0a7738');
INSERT INTO `tes_db`.`permission`(`username`, `type`)
VALUES ('admin', 'admin');
