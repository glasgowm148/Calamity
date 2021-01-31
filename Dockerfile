

FROM adoptopenjdk:8
RUN mkdir /project/
COPY . /project/
WORKDIR /project/1-src/TweetMinner-master 2
CMD sbt run