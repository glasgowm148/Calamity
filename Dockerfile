FROM adoptopenjdk:8
RUN mkdir /project/
COPY . /project/
WORKDIR /project/1-src/TweetMinner-master 2
# install sbt
RUN mkdir -p /usr/local/share/sbt-launcher-packaging && \
    curl --progress \
    --retry 3 \
    --retry-delay 15 \
    --location "https://github.com/sbt/sbt/releases/download/v${SBT_VERSION}/sbt-${SBT_VERSION}.tgz" \
    --output "/tmp/sbt-${SBT_VERSION}.tgz" && \
    tar -xzf "/tmp/sbt-${SBT_VERSION}.tgz" -C /usr/local/share/sbt-launcher-packaging --strip-components=1 && \
    ln -s /usr/local/share/sbt-launcher-packaging/bin/sbt /usr/local/bin/sbt && \
    rm -f "/tmp/sbt-${SBT_VERSION}.tgz"
CMD ["sbt", "run"]
