FROM amazoncorretto:17-alpine
COPY "target/purchase-notice-process-1.0.0.jar" "app.jar"
ENV PORT 8080
EXPOSE $PORT
ENTRYPOINT ["java","-jar", "/app.jar"]






