package relationshipextractor

object Main extends App {

  val extractor = RelationExtractor()
  val text:String = scala.io.Source.fromFile("data/corpus/sherlock_the_boscombe_valley_mystery.txt").mkString
  val paragraphs = text.split("\n\n")

  paragraphs.foreach(p => extractor.extractRelationsFromText(p).foreach(r =>
    println(s"---------------------------\n$r")
  ))
}