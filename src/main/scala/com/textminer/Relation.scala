package com.textminer

import play.api.libs.json._
import scala.io.Source.{ fromFile }
import scala.collection.mutable.ListBuffer

case class Relation(
  title: String,
  id: String,
  description: String,
  english: Option[Seq[String]],
  swedish: Option[Seq[String]]
  )

object Relation {
  implicit val jsonFormat = Json.format[Relation]

  private def readConfig(): JsResult[Seq[Relation]] = {
    val configFile = fromFile("data/relations.json").getLines mkString "\n"
    val rawJson = Json.parse(configFile)
    rawJson.validate[Seq[Relation]]
  }

  def getRelations(): Option[Seq[Relation]] = {
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

  def getRelationLabels(relation: Relation): Seq[String] = {
    relation.english.getOrElse(Nil)
  }

  def getSwedishRelationLabels(relation: Relation): Seq[String] = {
    relation.swedish.getOrElse(Nil)
  }
}
