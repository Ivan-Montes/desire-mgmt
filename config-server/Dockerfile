# Select image
FROM eclipse-temurin:17-jre-alpine

# Create variables
ENV APPDIR=/app

# Set home
WORKDIR ${APPDIR}

# Define meta info
LABEL dev.ime.config-server.version="1.0"
LABEL dev.ime.config-server.maintainer="IvanM"
LABEL dev.ime.config-server.description="Just a simple dockerfile"

# Copy files
COPY config-repo /app/config-repo
COPY target/*.jar ./app.jar

# Port
EXPOSE 8888

# Set the command to run the application
ENTRYPOINT ["java","-jar","./app.jar"]