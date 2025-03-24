FROM bellsoft/liberica-openjre-alpine-musl:latest
LABEL authors="ovo"
# 设置时区为上海
ENV TZ Asia/Shanghai
#拷贝字体文件
#COPY ./simfang.ttf /usr/share/fonts/simfang.ttf
# 安装字体管理器
#RUN apk add --update fontconfig \
#    # 删除安装缓存包
#    && rm -rf /var/cache/apk/* \
#    # 刷新字体缓存
#    && fc-cache

WORKDIR /app
# 请将jar包放倒与此文件同级目录下
ADD ./target/jbot-impl-1.0-SNAPSHOT.jar app.jar
VOLUME /app

ENTRYPOINT ["java", "-jar", "/app/wechat-bot.jar"]