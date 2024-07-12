FROM eclipse-temurin:17-jre-focal

COPY target/line-notify-in-3-minutes-1.0.0.jar /app/line-notify-in-3-minutes.jar

ENTRYPOINT ["java", "-jar", "/app/line-notify-in-3-minutes.jar"]