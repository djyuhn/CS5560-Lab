package NCBO_BioNLP

import java.io.{BufferedWriter, File, FileWriter}

import org.apache.spark.{SparkConf, SparkContext}


/**
 * Created by DJ Yuhn on 9/17/18.
 */
object SparkBioNLP {

  def main(args: Array[String]) {

    System.setProperty("hadoop.home.dir","C:\\winutils")

    val sparkConf = new SparkConf().setAppName("SparkWordCount").setMaster("local[*]")
      .set("spark.executor.memory", "4g").set("spark.driver.memory", "2g")

    val sc=new SparkContext(sparkConf)

    val inputf = sc.wholeTextFiles("mental_illness_ids", 50)
    val input = inputf.map(abs => {
      abs._2
    }).cache()

    // val input=sc.textFile("input", 10)

    val sparkIDs=input.flatMap(ids=> {ids.split("[\r\n]+")}).map(id => {
      val list = RESTClientGet.getMedWords(id).mkString("")
      val medicalWordsWriter = new BufferedWriter(new FileWriter("data/medWordsSeparate/medWords_" + id + ".txt"))
      medicalWordsWriter.append(list)
      medicalWordsWriter.close()
      list
    })

    sparkIDs.saveAsTextFile("output/medWords")

    val o=sparkIDs.collect()

    val allMedicalWordsWriter = new BufferedWriter(new FileWriter("data/medWords/allMedWords.txt"))

    o.foreach(list => {
      allMedicalWordsWriter.append(list)
    })
    allMedicalWordsWriter.close()
  }

}
