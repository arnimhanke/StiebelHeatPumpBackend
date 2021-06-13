FROM arm32v7/java11:latest

ARG JAR_FILE=target/Collector-1.0-SNAPSHOT-jar-with-dependencies.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]