package relationshipextractor

import scala.collection.mutable

/**
  * Created by erik on 20/04/16.
  */

case class Person(var name: String, sentence: String,
                  relations: mutable.Set[Relation] = mutable.Set[Relation](),
                  mentions: mutable.Set[String] = mutable.Set[String]()) {

  override def toString(): String = {
    return s"Name: $name (a.k.a. $mentions)\n$relations"
  }

  }

case class Relation(subject: Person, relationship: String, obj: Person, sentence: String) {

  override def toString(): String = {
    return s"Relation: $relationship\n\nObject: ${obj.name}\n\nSentence: $sentence"
  }

  override def hashCode(): Int = {
    return sentence.hashCode()
  }

}
