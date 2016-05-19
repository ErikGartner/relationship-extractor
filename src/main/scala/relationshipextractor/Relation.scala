package relationshipextractor

import scala.collection.mutable

/**
 * Created by erik on 20/04/16.
 */

case class Person(var name: String, sentence: String,
    relations: mutable.Set[Relation] = mutable.Set[Relation](),
    mentions: mutable.Set[String] = mutable.Set[String]()) {

    override def toString(): String = {
        s"Name: $name a.k.a: {$mentions}\n\t$relations"
    }

}

case class Relation(subject: Person, relationship: String, relationDefinition: RelationDefinition, obj: Person, sentence: String) {

    override def toString(): String = {
        return s"${subject.name}; $relationship; ${obj.name}"
    }

    override def hashCode(): Int = {
      sentence.hashCode()
    }

}
