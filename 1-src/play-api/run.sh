lsof -i :9000
export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)
sbt -mem 4098 run

# sbt -java-home /usr/local/opt/openjdk/bin/java
#export JAVA_HOME=`/usr/libexec/java_home -v 1.8.0_282`

# /usr/libexec/java_home -V

# Heap size
# edit in this file for osx/brew
# /usr/local/etc/sbtopts



# Broken?