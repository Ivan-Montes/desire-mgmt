# Select image
FROM eclipse-temurin:17-jre-alpine

# Create variables
ENV APPDIR=/app

# Set home
WORKDIR ${APPDIR}

# Define meta info
LABEL dev.ime.eureka-server.version="1.0"
LABEL dev.ime.eureka-server.maintainer="IvanM"
LABEL dev.ime.eureka-server.description="Just a simple dockerfile"

# Copy files
COPY target/*.jar ./app.jar

# Port
EXPOSE 8761

# Set the command to run the application
ENTRYPOINT ["java","-jar","./app.jar"]