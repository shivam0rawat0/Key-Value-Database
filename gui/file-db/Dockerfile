FROM eclipse-temurin:17-jdk-alpine
ARG DB_CONFIG
COPY $DB_CONFIG db-config.txt
COPY keydb.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar","db-config.txt"]