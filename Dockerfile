# ------------ 第一步：构建阶段 ------------
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# 首先只复制 pom 文件来利用 Docker 缓存
COPY **/pom.xml ./
COPY jbot-build/pom.xml jbot-build/
COPY jbot-bom/pom.xml jbot-bom/
COPY jbot-core/pom.xml jbot-core/
COPY jbot-impl/pom.xml jbot-impl/
COPY jbot-adapter/jbot-adapter-apad/pom.xml jbot-adapter/jbot-adapter-apad/

# 现在复制源代码
COPY . .

# 如果构建过程确实需要Redis
RUN  mvn install -f jbot-build/pom.xml -DskipTests \
 && mvn install -f jbot-bom/pom.xml -DskipTests \
 && mvn install -f jbot-core/pom.xml -DskipTests \
 && mvn clean package -f jbot-impl/pom.xml -DskipTests \
 && mvn clean package -f jbot-adapter/jbot-adapter-apad/pom.xml -DskipTests \
 && mvn clean package -f jbot-plugins/pom.xml -DskipTests


# ------------ 第二步：运行阶段 ------------
FROM eclipse-temurin:21-jre
WORKDIR /app

# 安装Redis（如果应用运行时需要）
RUN apt-get update && apt-get install -y redis

# 创建插件目录
RUN mkdir -p /app/plugin

# 拷贝构建产物
COPY --from=builder /app/jbot-impl/target/jbot-impl.jar .
COPY --from=builder /app/jbot-adapter/jbot-adapter-apad/target/jbot-adapter-apad-0.0.1.jar .
COPY --from=builder /app/jbot-plugins/*/target/*.jar /app/plugin/

# 如果应用需要监听特定端口，在这里定义
EXPOSE 9000

# 启动Redis和应用
CMD redis-server & java -cp jbot-impl.jar:jbot-adapter-apad-0.0.1.jar x.ovo.jbot.impl.Main
