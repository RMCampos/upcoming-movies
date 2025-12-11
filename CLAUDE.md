# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a full-stack web application that displays upcoming movies for Joinville-SC cinemas. The backend is a Spring Boot REST API that scrapes movie data from GNC Cinemas website, and the frontend is an Angular application that displays the movies with trailers.

## Architecture

### Backend (Java Spring Boot)
- **Location**: `api/` directory
- **Stack**: Spring Boot 3.4.5, Java 17, Maven
- **Key Dependencies**: Jsoup (HTML parsing), Lombok, Spring Boot Actuator
- **Entry Point**: `br.dev.ricardocampos.UpcomingMoviesApi` (api/src/main/java/br/dev/ricardocampos/UpcomingMoviesApi.java:7)

### Frontend (Angular)
- **Location**: `app/` directory
- **Stack**: Angular 19.2, TypeScript
- **Main Component**: `app.component.ts` fetches movies from API on init

### Data Flow
1. Angular app calls `/api/upcoming-movies` endpoint
2. `Controller` (api/src/main/java/br/dev/ricardocampos/Controller.java:10) delegates to `MoviesServices`
3. `MoviesServices.getMoviesFromGcn()` scrapes https://www.gnccinemas.com.br using Jsoup
4. If scraping fails, returns mock data via `getMockMovies()`
5. Data returned as `Movie` records with i18nTitle, originalTitle, cast, directors, summary, launchDate, youtubePreviewUrl

### Configuration
- **CORS**: Configured via `CorsConfig` class, reads `CORS_ALLOWED_ORIGINS` env var (defaults to http://localhost)
- **API Config**: `api/src/main/resources/application.yml`
- **Angular Environments**:
  - Dev: `app/src/environments/environment.ts` (API at http://localhost:8080)
  - Prod: `app/src/environments/environment.prod.ts` (API at https://moviesapi.ricardocampos.dev.br)

## Development Commands

### Backend (from repository root)

Start API with Maven wrapper:
```bash
cd api && ./mvnw spring-boot:run
```

Run with debugging (port 5005):
```bash
cd api && ./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=*:5005"
```

Build native image (GraalVM):
```bash
cd api && ./mvnw clean package -Pnative
```

Run tests:
```bash
cd api && ./mvnw test
```

### Frontend (from repository root)

Install dependencies:
```bash
cd app && npm install
```

Run dev server (http://localhost:4200):
```bash
cd app && npm run dev
```

Build for production:
```bash
cd app && npm run build
```

Run tests:
```bash
cd app && npm test
```

Watch mode for development:
```bash
cd app && npm run watch
```

### Docker

Start full stack with docker-compose:
```bash
docker-compose up
```

Build API Docker image:
```bash
docker build --no-cache -t ghcr.io/rmcampos/upcoming-movies/api:$(date '+%Y-%m-%d-%H%M%S') ./api
```

Build app Docker image:
```bash
docker build --no-cache -t ghcr.io/rmcampos/upcoming-movies/app:$(date '+%Y-%m-%d-%H%M%S') ./app
```

Run API container:
```bash
docker run -d -p 8080:8080 --rm --name upcoming-movies-api -e CORS_ALLOWED_ORIGINS="http://localhost:4200" <image-tag>
```

Run app container:
```bash
docker run -d -p 1234:80 --rm --name upcoming-movies-app <image-tag>
```

## Important Implementation Details

### Web Scraping Logic
The scraping implementation in `MoviesServices.getMoviesFromGcn()` (api/src/main/java/br/dev/ricardocampos/MoviesServices.java:61) is tightly coupled to GNC Cinemas' HTML structure. It:
- Sets cookies for Joinville (cidade_id=5) and current date
- Parses specific CSS classes: `.padding30`, `.barravermelha300`, `.txtvermelho`, etc.
- Extracts movie data through multiple iterations over DOM elements
- Falls back to mock data if scraping fails

If the cinema website structure changes, the selectors in this method will need updating.

### CORS Configuration
The API allows CORS origins specified via `CORS_ALLOWED_ORIGINS` environment variable. For local development with Angular on port 4200:
```bash
export CORS_ALLOWED_ORIGINS="http://localhost:4200"
```

### Native Image Profile
The API includes a `native` Maven profile for building GraalVM native images. This creates a standalone native executable for faster startup and lower memory footprint. Use `-Pnative` flag when building.

### Health Check Endpoint
Spring Boot Actuator provides `/actuator/health` endpoint used by Docker healthchecks (see docker-compose.yml).
