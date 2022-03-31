FROM openjdk:11
VOLUME /tmp
EXPOSE 8088
#ENV GOOGLE_APPLICATION_CREDENTIALS=fintech-delivery-dev-firebase-adminsdk-pl8qi-695e1a9bc4.json
ARG JAR_FILE=target/images-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} images.jar
ENTRYPOINT ["java","-jar","images.jar"]

