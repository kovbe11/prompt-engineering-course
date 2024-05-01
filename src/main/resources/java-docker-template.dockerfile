FROM openjdk:17

WORKDIR /app
COPY Coderunner.java .
RUN javac Coderunner.java
