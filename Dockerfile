FROM openjdk:8-jre-alpine
RUN mkdir /app

# adding and creating rootless user
RUN addgroup --system javauser && adduser -S -s /bin/false -G javauser javauser
COPY /target/*SNAPSHOT.jar /app/restdocker-0.0.1-SNAPSHOT.jar
WORKDIR /app

# giving permissions to the user
RUN chown -R javauser:javauser /app
USER javauser
EXPOSE 8080
CMD "java" "-jar" "restdocker-0.0.1-SNAPSHOT.jar"
