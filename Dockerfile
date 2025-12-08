FROM eclipse-temurin:21-jre

RUN apt-get update && apt-get install -y \
    libreoffice \
    libreoffice-writer \
    libreoffice-core \
    fonts-dejavu \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY build/libs/document-converter-service.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
