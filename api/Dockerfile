### Builder
FROM ghcr.io/graalvm/native-image:22.3.3 AS build

# Copy
WORKDIR /app
COPY pom.xml mvnw ./
COPY src ./src
COPY .mvn/ ./.mvn

# Build
RUN ./mvnw -B package -Pnative -DskipTests

### Deployer
FROM debian:12.10-slim AS deploy
RUN apt-get update && apt-get install -y --no-install-recommends \
    curl \
    ca-certificates \
    && rm -rf /var/lib/apt/lists/*

# Copy
WORKDIR /app
COPY --from=build /app/target/upcoming-movies-api ./upcoming-movies-api

# User, port and health check
USER 1001
EXPOSE ${PORT}
HEALTHCHECK --interval=30s --timeout=5s CMD ["curl", "-f", "http://localhost:8080/actuator/health"]

# Startup
ENTRYPOINT ["/app/upcoming-movies-api", "-Dspring.profiles.active=prod"]
