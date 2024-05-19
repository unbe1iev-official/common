FROM openjdk:22-bullseye

RUN apt-get update && \
    apt-get install -y maven

ENV MAVEN_CLI_OPTS '-B -DskipTests -Dmaven.repo.local=/opt/.m2/repository'
WORKDIR /tmp/maven

ADD pom.xml /tmp/maven
RUN mvn $MAVEN_CLI_OPTS verify --fail-never

ADD ./src /tmp/maven/src
RUN mvn $MAVEN_CLI_OPTS install