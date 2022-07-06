
# temp container to build using gradle
FROM gradle:5.3.0-jdk-alpine AS TEMP_BUILD_IMAGE
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY build.gradle $APP_HOME

COPY gradle $APP_HOME/gradle
COPY --chown=gradle:gradle . /home/gradle/src
USER root
RUN chown -R gradle /home/gradle/src

RUN gradle build || return 0
COPY . .
RUN gradle clean build

# actual container
FROM adoptopenjdk/openjdk11:alpine-jre
ENV ARTIFACT_NAME=elg_ixa_pipes-0.0.1-SNAPSHOT.jar
ARG user=elg
ARG group=elg
ARG uid=1001
ARG gid=1001
ARG app_home=/ixa_pipes_pos
ARG version=1.5.2

RUN mkdir $app_home \
  && addgroup -g $gid $group \
  && chown $uid:$gid $app_home \
  && adduser -D -h "$app_home" -u $uid -G $group $user

COPY --from=TEMP_BUILD_IMAGE /usr/app/build/libs/$ARTIFACT_NAME $app_home

USER $user
WORKDIR $app_home
RUN mkdir -p ./models/
WORKDIR ./models/
#Download models
RUN wget http://ixa2.si.ehu.es/ixa-pipes/models/ud-morph-models-1.5.0.tar.gz
RUN tar xvzf ud-morph-models-1.5.0.tar.gz

WORKDIR $app_home
# ARG is not available in ENTRYPOINT, but ENV is
ENV version=$version
ENTRYPOINT java -jar $ARTIFACT_NAME

EXPOSE 8080

