package ml;

import com.google.inject.Singleton
import org.apache.spark.SparkContext

/**
 * Model Prediction class
 */
@Singleton
class ModelPrediction {

    val sc = new SparkContext("local", "ModelPrediction - Application")

    val wordsFile = "public/words.txt" // Should be some file on your system
    lazy val data = sc.textFile(wordsFile, 2).cache()

    def init: Unit = {
        data
    }

    init //Important load during the app loader

    /**
     * This method will count the words
     * @return Word count list
     */
    def wordCount = {
            val counts = data.flatMap(line => line.split(" ")

    ).map(word => (word,1)).reduceByKey(_+_)

    counts.collect().map(r => s"${r._1}\t${r._2}").mkString("\n")
}

}