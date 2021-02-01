FROM adoptopenjdk:8
RUN mkdir /project/
COPY . /project/
WORKDIR /project/1-src/TweetMinner-master 2
RUN echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
RUN curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | sudo apt-key add
RUN sudo apt-get update
RUN sudo apt-get install sbt
CMD ["sbt", "run"]
