#######################################################
#         Prerequisites are SBT and Java 8            #
#         We recommend Sdkman to manage this          #
#######################################################



# Kill existing processes running on port 9000
sudo kill -9 $(sudo lsof -t -i:9000)

# SBT Options
export SBT_OPTS="-Xmx5G -XX:+UseConcMarkSweepGC -XX:+CMSClassUnloadingEnabled"

# Number of Embeddings to use; options are {50, 100, 200, 300}
export NUMBER_OF_EMBEDDINGS="50"
echo $NUMBER_OF_EMBEDDINGS "embeddings"
# Number of lines each Actor should process
export NUMBER_OF_LINES="50"
echo $NUMBER_OF_LINES "Actors"
# The directory with the tweet sets; options {"data/test-tweets/test", "data/tweets/"}
export TWEET_DIR="data/test-tweets/test"
echo "Reading from " $TWEET_DIR
# Run
sbt -mem 20000 run

