FROM openjdk:11
# ARG JAR_FILE=./build/libs/restService-1.0.0.jar
# COPY ${JAR_FILE} app.jar
COPY ./build/libs/restService-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
