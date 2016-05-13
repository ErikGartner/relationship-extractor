package relationshipextractor

import scala.collection.mutable

object Main extends App {

    val extractor = RelationExtractor()
    val text: String = scala.io.Source.fromFile(
        "data/corpus/sherlock_the_boscombe_valley_mystery/corpus.txt",
        "UTF-8").mkString
    val paragraphs = text.split("\n\n")

    val persons: mutable.Set[Person] = mutable.Set[Person]()
    paragraphs.foreach(p => extractor.extractRelationsFromText(p, persons))
    RelationInferrer.infer(persons)
    persons.foreach(p => {
        Graph.addPerson(p)
        p.relations foreach (Graph.addRelation(_))
    })
}

