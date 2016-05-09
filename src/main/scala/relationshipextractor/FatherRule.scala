package relationshipextractor

import org.easyrules.annotation._

@Rule(name= "InferFather",
  description = "Infer Father and Son/Daughter relations, adds to the Graph")
class InferFather extends RelationshipRule {
  var fired = false
  var person: Person = _

  @Condition
  def checkIfFatherRelation(): Boolean = {
    // TODO: check gender of parent
    person.relations exists (_.relationship == "son")
  }

  override def input(person: Person) {
    this.person = person
  }

  override def resetHasFired() {
    fired = false
  }

  override def hasFired(): Boolean = fired

  /**
  * subject is son to obj, create a new relation that is a copy
  * of the old but with subject the father of obj
  */
  @Action
  @throws(classOf[Exception])
  def addRelation() {
    person.relations.filter(_.relationship == "son").foreach(relation => {
      fired = true
      // TODO also change the RelationDefinition
      val newRelation = relation.copy(subject = relation.obj, obj = relation.subject, relationship = "father")
      relation.obj.relations.add(newRelation)
      // Graph.addRelation(newRelation)
      println(s"calling Graph.addRelation with ${newRelation}")
    })
  }
}