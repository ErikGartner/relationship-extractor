package relationshipextractor

/**
  * Created by erik on 20/04/16.
  */

case class Person(name: String, sentence: String)

case class Relation(subject: Person, relationship: String, obj: Person, sentence: String) {

  override def toString(): String = {
    return s"Subject: $subject\n\nRelation: $relationship\n\nObject: $obj\n\nSentence: $sentence"
  }

}
