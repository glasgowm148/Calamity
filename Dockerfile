FROM adoptopenjdk:8

#ENV JAVA_HOME /Library/Java/JavaVirtualMachines/adoptopenjdk-8/Contents/Home

RUN mkdir /project/
COPY . /project/
WORKDIR /project/1-src/play-api
CMD ["sbt", "run"]