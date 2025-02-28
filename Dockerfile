FROM openjdk:17

ARG JAR_FILE=build/libs/qampus-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

RUN apk add --no-cache tzdata \
    && ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime \
    && echo "Asia/Seoul" > /etc/timezone


ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "/app.jar"]
