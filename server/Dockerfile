FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copy from server directory
COPY server/mvnw .
COPY server/.mvn .mvn
COPY server/pom.xml .
COPY server/src src

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

ARG DEPENDENCY=/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.university.management.system.ServerApplication"]