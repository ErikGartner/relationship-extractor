package relationshipextractor

import scala.collection.mutable

/**
  * Created by erik on 20/04/16.
  */

case class Person(var name: String, sentence: String,
                  relations: mutable.Set[Relation] = mutable.Set[Relation](),
                  mentions: mutable.Set[String] = mutable.Set[String]())

case class Relation(subject: Person, relationship: String, obj: Person, sentence: String) {

  override def toString(): String = {
    return s"Subject: $subject\n\nRelation: $relationship\n\nObject: $obj\n\nSentence: $sentence"
  }

}
