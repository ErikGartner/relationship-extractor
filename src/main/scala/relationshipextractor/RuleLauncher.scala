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
  // val helloWorldRule: HelloWorldRule = new HelloWorldRule()
  // helloWorldRule.setInput("yes")
  // val rulesEngine: RulesEngine = RulesEngineBuilder.aNewRulesEngine().build()
  // rulesEngine.registerRule(helloWorldRule)
  // rulesEngine.fireRules()

  // val tom: Person = new Person("Tom", 14)
  // println("Tom: Hi! Can I have some vodka please?")

  // val rulesEngine: RulesEngine = RulesEngineBuilder.aNewRulesEngine()
  //   .withRuleListener(new MyListener)
  //   .named("shop rules engine")
  //   .build()
  // rulesEngine.registerRule(new AgeRule(tom))
  // rulesEngine.registerRule(new AlcoholRule(tom))

  // rulesEngine.fireRules

  val theSon = Person("the son", "the sentence")
  val theFather = Person("the father", "the sentence")
  val relation = Relation(theSon, "son", theFather, "the sentence")

  val inferFather: InferFather = new InferFather(relation)
  val rulesEngine = RulesEngineBuilder.aNewRulesEngine().build()
  rulesEngine.registerRule(inferFather)
  rulesEngine.fireRules


}