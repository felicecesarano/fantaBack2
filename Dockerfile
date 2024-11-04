# Usa un'immagine base con Java e Maven
FROM maven:3.8.6-openjdk-17 AS build

# Imposta la cartella di lavoro
WORKDIR /app

# Copia il pom.xml e il codice sorgente
COPY pom.xml .
COPY src ./src

# Costruisci l'applicazione
RUN mvn clean package -DskipTests

# Usa un'immagine leggera per il runtime
FROM openjdk:17-jdk-slim

# Copia l'applicazione costruita dall'immagine precedente
COPY --from=build /app/target/*.jar app.jar

# Comando per avviare l'applicazione
ENTRYPOINT ["java", "-jar", "/app.jar"]
