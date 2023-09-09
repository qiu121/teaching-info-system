# 校教学信息员反馈系统

## A CMS to manage teaching information about teachers and their classes.

## 🔧 技术选型

| 框架              |     说明      |            版本            |                                             官网                                             |
|-----------------|:-----------:|:------------------------:|:------------------------------------------------------------------------------------------:|
| Spring Boot2    |    基础框架     |          2.7.9           |                          <https://spring.io/projects/spring-boot>                          |
| Spring MVC      |    MVC框架    |          2.7.9           |                       <https://spring.io/projects/spring-framework>                        |
| Spring Data JPA |    ORM框架    |          2.7.9           |                        <https://spring.io/projects/spring-data-jpa>                        |
| MyBatis-Plus    |    持久层框架    |          3.5.3           |                                  <https://baomidou.com/>                                   |
| Maven           |    构建工具     |           3.8            |                                 <https://maven.apache.org>                                 |
| MySQL8.0        |   关系型数据库    |          8.0.31          |                                  <https://www.mysql.com>                                   |
| Druid           |   数据库连接池    |          1.2.16          |                             <https://github.com/alibaba/druid>                             |
| Sa-token        |   权限认证框架    |          1.34.0          |                                   <https://sa-token.cc>                                    |
| easyexcel       |  Excel处理工具  |          3.3.2           |                        <https://easyexcel.opensource.alibaba.com/>                         |
| Hutool          |     工具类     |          5.8.19          |                                    <https://hutool.cn/>                                    |                                                                                                
| Junit           |   单元测试框架    |           5.8            |                                <https://junit.org/junit5/>                                 |
| Redis           |  非关系型内存数据库  |                          |                                     <https://redis.io>                                     |
| Jedis           |  Redis客户端   |                          |                              <https://github.com/redis/jedis>                              |
| Slf4j           |   门面日志框架    |                          |                                  <https://www.slf4j.org>                                   |
| `RESTful`       |    接口规范     |                          |                                 <https://restfulapi.net/>                                  |
| `Knife4j`       | Api文档增强解决方案 | Spring Boot 2 + OpenAPI3 |                                <https://doc.xiaominfo.com/>                                |
| Lombok          |   代码增强工具    |                          |                                <https://projectlombok.org/>                                |
| devtools        |     热部署     |                          | <https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.devtools> |
| Docker          |    容器化技术    |          24.0.3          |                                  <https://www.docker.com>                                  |

## 👨‍💻 开发环境

- IDE: IntelliJ IDEA 2023
- SDK: JDK 1.8+
- OS: Ubuntu 22.04LTS
- MySQL: 8.0+
- Maven: 3.8+
- 数据库可视化工具： Navicat Premium 16
- 接口测试工具： `Apifox`
- 浏览器: Chrome
- others: ChatGPT、Google

## 🐳 构建

```bash
sudo docker build -t geek/tes:final . 
```

## 🚀 启动

```bash
sudo docker run -p 8963:9999 geek/tes:final
```

## 🖥 访问

- url入口

> `http://localhost:8080/login/index.html`

- 二维码入口

> `http://localhost:8080/api/v1/QRcode?url=http://[ your LAN ip:port]/login/index.html`

## 📝TODO

- [x] Excel数据导出
- [x] 角色权限拦截校验
- [ ] 数据加密传输
- [ ] 分离前端页面工程化
- [ ] 接口数据缓存
