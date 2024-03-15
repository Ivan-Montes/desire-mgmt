FROM eclipse-temurin:17-jre-alpine

ENV APPDIR=/app

WORKDIR ${APPDIR}

LABEL dev.ime.ms-products.version="1.0"
LABEL dev.ime.ms-products.maintainer="IvanM"
LABEL dev.ime.ms-products.description="Just a simple dockerfile"

COPY ./target/*.jar ./app.jar

EXPOSE 7703

ENTRYPOINT [ "java", "-jar", "./app.jar" ]