# Building

Java API (Cloud Native image):

```bash
docker build --no-cache \
 -t rmcampos/upcoming-movies-api:api-$(date '+%Y.%m.%d') \
 ./api
```

> Latest image: rmcampos/upcoming-movies:api-latest

Angular App:

```bash
docker build --no-cache \
  -t rmcampos/upcoming-movies-app:app-$(date '+%Y.%m.%d') \
  ./app
```

> Latest image: rmcampos/upcoming-movies:app-latest

# Running

Java API:

```bash
docker run -d -p 8080:8080 --rm \
  --name upcoming-movies-api \
  -e CORS_ALLOWED_ORIGINS="http://localhost:4200" \
  rmcampos/upcoming-movies:api-latest
```

Angular App:

```bash
docker run -d -p 4200:80 --rm \
  --name upcoming-movies-app \
  rmcampos/upcoming-movies:app-latest
```

# Publishing

Login in:

```bash
export CR_PAT=YOUR_TOKEN
echo $CR_PAT | docker login docker.io -u RMCampos --password-stdin
```

Pushing the images:

```bash
docker push rmcampos/upcoming-movies-api:api-2025.05.27
docker push rmcampos/upcoming-movies-app:app-2025.05.27
```
