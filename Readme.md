### Document Converter Service

Для успешного запуска теста SystemDocumentConverterTest необходимо собрать docker контейнер
```
docker build -t document-converter-service:latest .
или
./gradlew bootBuildImage --imageName=document-converter-service:latest
```
