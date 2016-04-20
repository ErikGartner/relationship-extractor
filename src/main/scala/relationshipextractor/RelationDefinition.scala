package relationshipextractor

import play.api.libs.json._
import scala.io.Source.{ fromFile }
import scala.collection.mutable.ListBuffer

case class RelationDefinition(
  title: String,
  id: String,
  description: String,
  english: Option[Seq[String]],
  swedish: Option[Seq[String]]
  )

object RelationDefinition {
  implicit val jsonFormat = Json.format[RelationDefinition]

  private def readConfig(): JsResult[Seq[RelationDefinition]] = {
    val configFile = fromFile("data/relations.json").getLines mkString "\n"
    val rawJson = Json.parse(configFile)
    rawJson.validate[Seq[RelationDefinition]]
  }

  def getRelations(): Option[Seq[RelationDefinition]] = {
    readConfig().fold(
      invalid = {
        fieldErrors => fieldErrors.foreach(x => println(s"field: ${x._1} errors: ${x._2}"))
        None
      },
      valid = {
        relations => Some(relations)
      }
    )
  }

  def getRelationLabels(relation: RelationDefinition): Seq[String] = {
    relation.english.getOrElse(Nil)
  }

  def getSwedishRelationLabels(relation: RelationDefinition): Seq[String] = {
    relation.swedish.getOrElse(Nil)
  }
}
