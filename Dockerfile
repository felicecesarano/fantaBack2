# Usa un'immagine base con JDK e Maven
FROM maven:3.8.6-openjdk-17 AS build

# Imposta la directory di lavoro
WORKDIR /app

# Copia i file di pom.xml e il codice sorgente
COPY pom.xml ./
COPY src ./src

# Esegui la build
RUN mvn clean package

# Usa un'immagine leggera per eseguire l'applicazione
FROM openjdk:17-jdk-slim

# Copia il JAR generato dal primo stadio
COPY --from=build /app/target/*.jar app.jar

# Espone la porta su cui l'applicazione ascolta
EXPOSE 8080

# Comando per avviare l'applicazione
ENTRYPOINT ["java", "-jar", "/app.jar"]
