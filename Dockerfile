FROM eclipse-temurin:21-jdk-alpine as builder
WORKDIR /workspace/novi-kotlin
COPY gradle gradle
COPY gradlew .
COPY src src
COPY build.gradle.kts .
COPY settings.gradle.kts .
RUN ./gradlew build -x test

FROM eclipse-temurin:21-jre-alpine
ARG APP_WEB_TARGET=/workspace/novi-kotlin/build
WORKDIR /Novi
COPY --from=builder ${APP_WEB_TARGET}/classes/kotlin/main .
COPY --from=builder ${APP_WEB_TARGET}/deps deps
COPY --from=builder ${APP_WEB_TARGET}/resources/main .
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -cp .:deps/* org.novi.NoviKotlinApplicationKt ${0} ${@}"]