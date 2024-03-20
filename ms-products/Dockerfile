# Select image
FROM eclipse-temurin:17-jre-alpine

# Create variables
ENV APPDIR=/app

# Set app home
WORKDIR ${APPDIR}

# Define meta info
LABEL dev.ime.ms-products.version="1.0"
LABEL dev.ime.ms-products.maintainer="IvanM"
LABEL dev.ime.ms-products.description="Just a simple dockerfile"

# Copy files
COPY ./target/*.jar ./app.jar

# Init command
ENTRYPOINT ["java","-jar","./app.jar"]
