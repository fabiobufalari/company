# 🌱 Etapa 1: Build com Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copia o pom.xml e baixa as dependências (cache otimizado)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o restante do projeto e constrói o JAR
COPY . .
RUN mvn clean package -DskipTests

# 🐳 Etapa 2: Imagem leve para execução
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
WORKDIR /app

# Copia o JAR gerado
COPY --from=build /app/target/company-0.0.1-SNAPSHOT.jar app.jar

# 🟢 Expõe a porta
EXPOSE 8084

# 🏁 Comando de inicialização
ENTRYPOINT ["java","-jar","/app/app.jar"]
