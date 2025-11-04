FROM eclipse-temurin:17-jdk

ARG JAR_FILE=build/libs/qampus-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

RUN apt-get update && apt-get install -y tzdata && \
    ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime && \
    echo "Asia/Seoul" > /etc/timezone && \
    apt-get clean
    
ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "/app.jar"]
