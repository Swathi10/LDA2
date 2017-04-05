name := "ldaTest"

version := "1.0"

scalaVersion := "2.10.4"


libraryDependencies += "org.apache.spark" % "spark-core_2.10" % "2.1.0"

libraryDependencies += "org.apache.spark" % "spark-sql_2.10" % "2.1.0"
libraryDependencies += "org.apache.spark" % "spark-streaming_2.10" % "2.1.0"
libraryDependencies += "org.apache.spark" % "spark-mllib_2.10" % "2.1.0"
libraryDependencies += "org.apache.spark" % "spark-graphx_2.10" % "2.1.0"
libraryDependencies += "org.apache.spark" % "spark-graphx_2.10" % "2.1.0"
libraryDependencies ++= Seq(

  "edu.stanford.nlp" % "stanford-corenlp" % "3.6.0",
  "edu.stanford.nlp" % "stanford-corenlp" % "3.6.0" classifier "models",
  "com.google.protobuf" % "protobuf-java" % "2.6.1"
)