# Use official node image as the base image
FROM node:latest as build

# Set the working directory
WORKDIR /usr/local/app

# Add the source code to app
COPY ./ /usr/local/app/

# Install all the dependencies
RUN npm install

EXPOSE 4200
# Generate the build of the application
# RUN npm run build

ENTRYPOINT [ "npm", "run", "docker-start" ]

# Stage 2: Serve app with nginx server

# # Use official nginx image as the base image
# FROM nginx:latest

# # Copy the build output to replace the default nginx contents.
# COPY --from=build /usr/local/app/dist/myproj /usr/share/nginx/html

# # Expose port
# EXPOSE 80