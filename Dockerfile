FROM openjdk:17

ARG JAR_FILE=build/libs/qampus-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

RUN apt-get update && apt-get install -y tzdata \
    && ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime \
    && echo "Asia/Seoul" > /etc/timezone \
    && dpkg-reconfigure -f noninteractive tzdata

ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "/app.jar"]
