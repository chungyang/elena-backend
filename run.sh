./mvnw package
docker build -t elena .
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE='dev' elena