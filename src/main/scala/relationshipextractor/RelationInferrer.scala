package relationshipextractor

import org.easyrules.core.RulesEngineBuilder

import scala.collection.mutable

/**
  * Created by erik on 05/05/16.
  */
object RelationInferrer {

  def infer(persons: mutable.Set[Person]) = {

    val rulesEngine = RulesEngineBuilder.aNewRulesEngine().build()
    val rules = Seq[RelationshipRule](new InferFather())
    rules.foreach(r => rulesEngine.registerRule(r))

    do {
      rules.foreach(r => r.resetHasFired())

      for(person <- persons){
        rules.foreach(r => r.input(person))
        rulesEngine.fireRules
      }
      
    } while(rules.exists(r => r.hasFired()))

  }


}
