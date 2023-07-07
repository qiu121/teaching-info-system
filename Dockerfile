FROM openjdk:8-jre-alpine
MAINTAINER to_Geek <qiu0089@foxmail.com>
WORKDIR /app
COPY target/*.jar app.jar
# 安装字体解决图形验证码绘制问题!!!
RUN apk update && \
    apk add --no-cache ttf-dejavu
ENTRYPOINT ["java", "-jar", "app.jar","--spring.profiles.active=prod"]

