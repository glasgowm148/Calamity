# Java 8 is required
FROM adoptopenjdk:8

ENV SCALA_VERSION 2.13.1
ENV SBT_VERSION 0.13.17
ENV SCALA_DEB http://www.scala-lang.org/files/archive/scala-$SCALA_VERSION.deb
#
RUN mkdir /project/

#
COPY . /project/

#
WORKDIR /project/1-src/TweetMinner-master 2

#RUN apt-get install wget
# Install Scala


RUN \
    wget --quiet --output-document=scala.deb $SCALA_DEB && \
    dpkg -i scala.deb && \
    rm -f *.deb
    
# Install Scala Build Tool sbt
RUN apt-get install -y --force-yes sbt


# Run
CMD ["sbt", "run"]
