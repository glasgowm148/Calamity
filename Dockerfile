# Java 8 is required
FROM adoptopenjdk:8

ENV SCALA_VERSION 2.13.1
ENV SBT_VERSION 0.13.17
#ENV SCALA_DEB http://www.scala-lang.org/files/archive/scala-$SCALA_VERSION.deb

#
RUN mkdir /project/

#
COPY . /project/

#
#WORKDIR /project/1-src/feature-extractor
#WORKDIR /project/1-src/1-java
WORKDIR /project/1-src/api

# install sbt
RUN mkdir -p /usr/local/share/sbt-launcher-packaging && \
    curl --progress-bar \
    --retry 3 \
    --retry-delay 15 \
    --location "https://github.com/sbt/sbt/releases/download/v${SBT_VERSION}/sbt-${SBT_VERSION}.tgz" \
    --output "/tmp/sbt-${SBT_VERSION}.tgz" && \
    tar -xzf "/tmp/sbt-${SBT_VERSION}.tgz" -C /usr/local/share/sbt-launcher-packaging --strip-components=1 && \
    ln -s /usr/local/share/sbt-launcher-packaging/bin/sbt /usr/local/bin/sbt && \
    rm -f "/tmp/sbt-${SBT_VERSION}.tgz"

#RUN \
#    wget --quiet --output-document=scala.deb $SCALA_DEB && \
#    dpkg -i scala.deb && \
#    rm -f *.deb
    
# Install Scala Build Tool sbt
#RUN apt-get install -y --force-yes sbt


# Run
CMD ["sbt clean"]
CMD ["sbt", "run"]
