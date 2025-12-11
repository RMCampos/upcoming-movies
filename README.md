# Upcoming Movies

A full-stack web application that displays upcoming movies for cinemas in Joinville-SC, Brazil. The application scrapes movie data from GNC Cinemas and presents it in a clean, user-friendly interface with embedded trailers.

## Features

- Real-time scraping of upcoming movies from GNC Cinemas website
- Display of movie information including:
  - Portuguese and original titles
  - Cast and directors
  - Synopsis
  - Launch dates
  - Embedded YouTube trailers
- Fallback to mock data if scraping fails
- Responsive Angular frontend
- RESTful Spring Boot API
- Docker support for easy deployment
- GraalVM native image support for optimized performance

## Tech Stack

### Backend
- **Java 17**
- **Spring Boot 3.4.5**
- **Maven** (with wrapper)
- **Jsoup** - HTML parsing for web scraping
- **Lombok** - Reduce boilerplate code
- **Spring Boot Actuator** - Health checks and monitoring

### Frontend
- **Angular 19.2**
- **TypeScript**
- **RxJS** - Reactive programming
- **Angular CLI** - Development tools

## Architecture

```
┌─────────────────┐         HTTP          ┌──────────────────┐
│                 │ ──────────────────────>│                  │
│  Angular App    │   GET /api/upcoming   │  Spring Boot API │
│  (port 4200)    │        -movies         │  (port 8080)     │
│                 │<────────────────────── │                  │
└─────────────────┘      JSON Response     └──────────────────┘
                                                    │
                                                    │ Jsoup
                                                    ▼
                                           ┌─────────────────┐
                                           │  GNC Cinemas    │
                                           │  Website        │
                                           └─────────────────┘
```

## Prerequisites

- **Java 17** or higher
- **Node.js 18+** and npm
- **Docker** and Docker Compose (optional, for containerized deployment)

## Getting Started

### Local Development

#### 1. Run the Backend

```bash
cd api
./mvnw spring-boot:run
```

The API will start at `http://localhost:8080`

#### 2. Run the Frontend

In a new terminal:

```bash
cd app
npm install
npm run dev
```

The application will be available at `http://localhost:4200`

### Using Docker Compose

Run both services with a single command:

```bash
docker-compose up
```

This will start:
- API on `http://localhost:8080`
- API debugging on port `5005`

Note: The docker-compose setup is configured for API development. For the frontend, you'll still need to run it separately or build your own Docker configuration.

## API Documentation

### Endpoints

#### Get Upcoming Movies

```
GET /api/upcoming-movies
```

**Response Example:**

```json
[
  {
    "i18nTitle": "Oppenheimer",
    "originalTitle": "Oppenheimer",
    "cast": ["Cillian Murphy", "Emily Blunt", "Robert Downey Jr."],
    "directors": ["Christopher Nolan"],
    "summary": "A dramatization of the life story of J. Robert Oppenheimer.",
    "launchDate": "15-12-2025",
    "youtubePreviewUrl": "https://www.youtube.com/embed/uYPbbksJxIg"
  }
]
```

#### Health Check

```
GET /actuator/health
```

## Environment Variables

### Backend

| Variable | Description | Default |
|----------|-------------|---------|
| `CORS_ALLOWED_ORIGINS` | Comma-separated list of allowed CORS origins | `http://localhost` |
| `SERVER_PORT` | API server port | `8080` |

Example:
```bash
export CORS_ALLOWED_ORIGINS="http://localhost:4200,https://myapp.com"
./mvnw spring-boot:run
```

### Frontend

Environment configurations are in:
- `app/src/environments/environment.ts` (development)
- `app/src/environments/environment.prod.ts` (production)

## Building for Production

### Backend - Standard JAR

```bash
cd api
./mvnw clean package
java -jar target/upcoming-movies-api.jar
```

### Backend - Native Image (GraalVM)

```bash
cd api
./mvnw clean package -Pnative
./target/upcoming-movies-api
```

Native images offer:
- Faster startup time
- Lower memory footprint
- No JVM required at runtime

### Frontend

```bash
cd app
npm run build
```

Build artifacts will be in `app/dist/` directory.

### Docker Images

Build API image:
```bash
docker build -t upcoming-movies-api:latest ./api
```

Build app image:
```bash
docker build -t upcoming-movies-app:latest ./app
```

## Project Structure

```
upcoming-movies/
├── api/                          # Spring Boot backend
│   ├── src/main/java/br/dev/ricardocampos/
│   │   ├── UpcomingMoviesApi.java    # Main application class
│   │   ├── Controller.java           # REST controller
│   │   ├── MoviesServices.java       # Business logic & scraping
│   │   ├── Movie.java                # Data model
│   │   └── CorsConfig.java           # CORS configuration
│   ├── src/main/resources/
│   │   └── application.yml           # Application config
│   ├── pom.xml                       # Maven dependencies
│   └── Dockerfile                    # API container image
├── app/                          # Angular frontend
│   ├── src/app/
│   │   ├── app.component.ts          # Main component
│   │   ├── app.component.html        # Main template
│   │   ├── app.component.css         # Styles
│   │   └── safe-url.pipe.ts          # URL sanitization pipe
│   ├── src/environments/             # Environment configs
│   ├── package.json                  # npm dependencies
│   └── Dockerfile                    # App container image
├── docker-compose.yml            # Multi-container setup
├── CLAUDE.md                     # AI assistant guidance
└── README.md                     # This file
```

## Development

### Running Tests

Backend:
```bash
cd api
./mvnw test
```

Frontend:
```bash
cd app
npm test
```

### Debug Mode

The API can be run in debug mode with remote debugging on port 5005:

```bash
cd api
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=*:5005"
```

### Generating Angular Components

```bash
cd app
ng generate component component-name
```

## Notes

- The application is specifically configured for Joinville-SC cinemas (cidade_id=5 in cookies)
- Web scraping depends on GNC Cinemas website structure - changes to their site may require updates to `MoviesServices.getMoviesFromGcn()`
- Mock data is automatically returned if the scraping fails, ensuring the application always has data to display

## License

This project is for educational and personal use.
