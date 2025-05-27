# Use Node.js 20 as the base image for building
FROM node:slim AS build

# Set working directory
WORKDIR /app

# Copy package.json and package-lock.json (if available)
COPY package*.json ./

# Install dependencies
RUN npm ci --only=production

# Copy source code
COPY . .

# Build the Angular app for production
RUN npm run build

# Use nginx to serve the built app
FROM nginx:alpine

# Copy the built app from the previous stage
COPY --from=build /app/dist/app/browser /usr/share/nginx/html

# Copy custom nginx configuration (optional)
# COPY nginx.conf /etc/nginx/nginx.conf

# Expose port 80
EXPOSE 80

# Start nginx
CMD ["nginx", "-g", "daemon off;"]
