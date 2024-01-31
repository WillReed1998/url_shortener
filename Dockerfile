FROM openjdk:17-jdk-alpine
LABEL authors="mani123"

WORKDIR /app

RUN apk add --no-cache postgresql-client

ENV DB_URL=jdbc:postgresql://db:5432/appdb

COPY build/libs/url_shortener-0.0.1.jar /opt/app.jar

EXPOSE 9999

CMD ["java", "-jar", "/opt/app.jar"]