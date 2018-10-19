package grouping

import java.io.{BufferedWriter, FileWriter}

import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

/**
  * Created by Mayanka on 27-Jun-16.
  */
object BioNLP_Grouping {

  def main(args: Array[String]) {

    // For Windows Users
    System.setProperty("hadoop.home.dir", "C:\\winutils")

    // Configuration
    val sparkConf = new SparkConf().setAppName("SparkWordCount").setMaster("local[*]")

    val sc = new SparkContext(sparkConf)

    val inputf = sc.wholeTextFiles("data/medWordsSeparate", 50)
    val input = inputf.map(words => {
      words._2
    }).cache()



    val medicalWords = input.distinct.map(line => {
      if(!line.isEmpty) {
        val splitLine = line.split("\t")
        val word = (splitLine(1), splitLine(2))
      }
    }).cache()

    val collect = medicalWords.distinct.collect()

    val medicalWordCategoryWriter = new BufferedWriter(new FileWriter("data/categories/allMedWords.txt"))

  }
}
