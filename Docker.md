# Building

Java API (Cloud Native image):

```bash
docker build --no-cache \
 -t ghcr.io/rmcampos/upcoming-movies/api:$(date '+%Y-%m-%d-%H%M%S') \
 ./api
```

> Latest image: ghcr.io/rmcampos/upcoming-movies/api:2025-05-27-094436

Angular App:

```bash
docker build --no-cache \
  -t ghcr.io/rmcampos/upcoming-movies/app:$(date '+%Y-%m-%d-%H%M%S') \
  ./app
```

> Latest image: ghcr.io/rmcampos/upcoming-movies/app:2025-05-27-091608

# Running

Java API:

```bash
docker run -d -p 8080:8080 --rm \
  --name upcoming-movies-api \
  -e CORS_ALLOWED_ORIGINS="http://localhost:4200" \
  ghcr.io/rmcampos/upcoming-movies/api:2025-05-27-094436
```

Angular App:

```bash
docker run -d -p 1234:80 --rm \
  --name upcoming-movies-app \
  ghcr.io/rmcampos/upcoming-movies/app:2025-05-27-091608
```

# Publishing

Login in:

```bash
export CR_PAT=YOUR_TOKEN
echo $CR_PAT | docker login ghcr.io -u RMCampos --password-stdin
```

Pushing the images:

```bash
docker push ghcr.io/rmcampos/upcoming-movies/api:2025-05-27-094436
docker push ghcr.io/rmcampos/upcoming-movies/app:2025-05-27-091608
```
