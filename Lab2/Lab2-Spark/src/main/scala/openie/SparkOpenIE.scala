package openie

import java.io.{BufferedWriter, File, FileWriter, PrintWriter}

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Mayanka on 27-Jun-16.
  */
object SparkOpenIE {

  def main(args: Array[String]) {

    // For Windows Users
    System.setProperty("hadoop.home.dir", "C:\\winutils")

    // Configuration
    val sparkConf = new SparkConf().setAppName("SparkWordCount").setMaster("local[*]")

    val sc = new SparkContext(sparkConf)

    val inputf = sc.wholeTextFiles("mental_illness_abstracts", 50)
    val input = inputf.map(abs => {
      abs._2
    }).cache()

//    val allTriplets = input.flatMap(abstracts=> {abstracts.split("\n")}).map(singleAbs => {
//      CoreNLP.returnSentences(singleAbs)
//    }).map(sentences => {
//      CoreNLP.returnTriplets(sentences)
//    }).cache()

    val allTriplets = input.map(singleAbs => {
      CoreNLP.returnTriplets(singleAbs)
    }).cache()

    allTriplets.saveAsTextFile("output/triplets")

    val collect = allTriplets.collect()

    var counter = 0
    collect.foreach(triplets => {
      val tripletWriter = new BufferedWriter(new FileWriter("data/triplets/triplets_abstract_" + counter + ".txt"))
      tripletWriter.append(triplets)
      tripletWriter.close()
      counter = counter + 1
  })

  }

}
