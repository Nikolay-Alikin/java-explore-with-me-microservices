FROM bellsoft/liberica-openjre-alpine:21
VOLUME /tmp
RUN adduser -S spring-user
USER spring-user
COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
