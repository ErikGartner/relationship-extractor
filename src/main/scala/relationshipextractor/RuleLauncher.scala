package relationshipextractor

import org.easyrules.api._
import org.easyrules.core._

class MyListener extends RuleListener {
  override def beforeExecute(rule: Rule) {
    println("beforeExecute")
  }

  override def onSuccess(rule: Rule) {
    println("onSuccess")
  }

  override def onFailure(rule: Rule, exception: Exception) {
    println("onFailure")
  }
}


object Launcher extends App {
  val theSon = Person("the son", "the sentence")
  val theFather = Person("the father", "the sentence")
  val relation = Relation(theSon, "son", RelationDefinition.getRelations.get(0), theFather, "the sentence")
  Graph.addRelation(relation)

  val inferFather: InferFather = new InferFather
  inferFather.input(theSon)
  val rulesEngine = RulesEngineBuilder.aNewRulesEngine().build()
  rulesEngine.registerRule(inferFather)
  rulesEngine.fireRules


}