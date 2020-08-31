FROM openjdk:11
WORKDIR /app
COPY target/reddit-clone-backend-1.0.0-SNAPSHOT.jar /app/reddit-clone.jar
ENTRYPOINT ["java", "-jar", "reddit-clone.jar"]
