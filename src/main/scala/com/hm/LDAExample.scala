package com.hm
import org.apache.spark
import org.apache.spark.ml.feature._
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.clustering.{DistributedLDAModel, LDA}
import org.apache.spark.rdd.RDD

import org.apache.spark.ml.attribute.Attribute
import org.apache.spark.ml.feature.{IndexToString, StringIndexer}
import org.apache.spark.sql.Row
// $example off$
import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.feature.StopWordsRemover



object LDAExample {


  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("LatentDirichletAllocationExample").setMaster("local")
    val sc = new SparkContext(conf)
    val spark = SparkSession
      .builder
      .appName("CountVectorizerExample")
      .getOrCreate()



    val sentenceDataFrame = spark.createDataFrame(Seq(
      (0, "Hi I heard about Spark"),
      (1, "I wish Java could use case classes"),
      (2, "Logistic,Java,regression,Java,models,are,neat")
    )).toDF("id", "sentence")
    sentenceDataFrame.show()


    val regexTokenizer = new RegexTokenizer()
      .setInputCol("sentence")
      .setOutputCol("words")
      .setPattern("\\W")

    val regexTokenized = regexTokenizer.transform(sentenceDataFrame)
    regexTokenized.select("sentence", "words").show(false)

    val remover = new StopWordsRemover()
      .setInputCol("words")
      .setOutputCol("filtered")

   val a= remover.transform(regexTokenized)
    a.select( "words","filtered").show(false)
    var z=a.select("filtered").toString()
println(".......",z)


    val cvModel: CountVectorizerModel = new CountVectorizer()
      .setInputCol("filtered")
      .setOutputCol("features")
      .setVocabSize(20)
      .setMinDF(0)
      .fit(a)
   val b= cvModel.transform(a)

   val f= b.select("features")




    val res=b.select("features","id")

    res.show()



    import spark.implicits._

    val corpus = res.map(i=>{

     val vector = i.get(0).asInstanceOf[org.apache.spark.ml.linalg.SparseVector]

      val vc:org.apache.spark.mllib.linalg.Vector = Vectors.dense(vector.toArray)

        println(vector.getClass.getCanonicalName)
      (i.getInt(1).toLong,vc)
    }).toJavaRDD.rdd
println("###")

 val ldaModel = new LDA().setK(4).run(corpus)
    println("Learned topics (as distributions over vocab of " + ldaModel.vocabSize + " words):")
    val topics = ldaModel.topicsMatrix


    for (topic <- Range(0, 4)) {
      print("Topic " + topic + ":")
      for (word <- Range(0, ldaModel.vocabSize)) { print(" " + topics(word, topic)); }
      println()
    }

    val topicIndices = ldaModel.describeTopics(maxTermsPerTopic = 10)
    topicIndices.foreach { case (terms, termWeights) =>
      println("TOPIC:")
      terms.zip(termWeights).foreach { case (term, weight) =>

        println(s"${(term)}\t$weight")
      }
      println()
    }
    // Shows the result.

   /* val indexer = new StringIndexer()
      .setInputCol("filtered")
      .setOutputCol("categoryIndex")
      .fit(a)
    val indexed = indexer.transform(a)
println(indexed)
    println(s"Transformed string column '${indexer.getInputCol}' " +
      s"to indexed column '${indexer.getOutputCol}'")
    indexed.show()
*/
 /*   val inputColSchema = indexed.schema(indexer.getOutputCol)
    println(s"StringIndexer will store labels in output column metadata: " +
      s"${Attribute.fromStructField(inputColSchema).toString}\n")

    val converter = new IndexToString()
      .setInputCol("categoryIndex")
      .setOutputCol("originalCategory")

    val converted = converter.transform(indexed)

    println(s"Transformed indexed column '${converter.getInputCol}' back to original string " +
      s"column '${converter.getOutputCol}' using labels in metadata")
    converted.select("id", "categoryIndex", "originalCategory").show()*/


//
//ldaModel.save(sc, "/home/swathi/Desktop/cod.txt")
//    val sameModel = DistributedLDAModel.load(sc,
//      "/home/swathi/Desktop/cod.txt")

//    val newDataDF = spark.
//      read.parquet("/home/swathi/Desktop/cod.txt/data/topicCounts/part-00000-bef54e6f-7b81-4537-85cb-d3f319bde2be.snappy.parquet")        // read back parquet to DF
//    newDataDF.show()

    //    val h =sc.parallelize(df1).map(Row(_)).collect()(0)(0)
//   print(h)


  }}