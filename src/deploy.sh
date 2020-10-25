cd src
rm -rf target/universal/play*
rm -rf target/universal/help*
rm -rf target/docker*


## These should be uncommented when any structural changes are made 
## (adding dependencies, Java version, etc)
sbt clean
sbt update

# This task will create a new zip under target/universal/directory.
echo "### sbt dist"
sbt dist


unzip target/universal/helpme-akka-1.0-SNAPSHOT.zip -d target/universal

## cd target/universal/helpme-akka-1.0-SNAPSHOT/bin/
# ./play-java-seed
# cd .. # cd .. # cd .. # cd ..
# Illegal reflective access
sbt run


# echo "### Docker:publishLocal"
# echo "(Docker must be running locally)"
# sbt docker:publishLocal

# echo "### docker run "
#docker run --rm -p 9000:9000 helpme-akka:latest 
# docker run --rm -p 9000:9000 helpme-akka:1.0-SNAPSHOT

# src/target/universal/helpme-akka-1.0-SNAPSHOT/bin/helpme-akka -Dconfig.file=local1.conf
# 