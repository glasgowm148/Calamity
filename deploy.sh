cd src
rm -rf target/universal/play*
rm -rf target/universal/help*
sbt update

# This task will create a new zip under target/universal/directory.
sbt dist
# 
unzip target/universal/help-me-event-detection-1.0-SNAPSHOT.zip -d target/universal

cd target/universal/help-me-event-detection-1.0-SNAPSHOT/bin/
# Illegal reflective access
# ./play-java-seed
cd ..
cd ..
cd ..
cd ..

echo "### Docker:publishLocal"
sbt docker:publishLocal
# docker run --rm -p 9000:9000 play-java-seed:1.0-SNAPSHOT 
echo "### docker run "
docker run --rm -p 9000:9000 help-me-event-detection:latest 
