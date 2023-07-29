FROM core:latest

MAINTAINER Pseudow

WORKDIR /usr/minecraft/

ENV MC_SERVER_TYPE=parkour

COPY src/main/resources/translations parkour/translations
COPY templates .

ENV PROJECT_NAME=altarise-parkour
ENV FOLDER=/usr/minecraft/plugins/

COPY src/main/resources/maps plugins/Parkour/maps

RUN bash download_artifacts.sh -C

# Downloading dependencies....

RUN ls -C
RUN ls plugins/ -C

RUN echo Starting minecraft server...

ENV MC_SERVER_GAME_ID=park

ENTRYPOINT ["java", "-Xmx3072M", "-XX:+UseG1GC", "-XX:+DisableExplicitGC", "-jar", "paper.jar"]
