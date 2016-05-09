package relationshipextractor

import org.easyrules.annotation._

@Rule(name= "InferFather",
  description = "Infer Father and Son relations, adds to the Graph")
class InferFather(relation: Relation) {

  @Condition
  def checkIfFatherRelation(): Boolean = {
    relation.relationship == "son"
  }

  /**
  * subject is son to obj, create a new relation that is a copy
  * of the old but with subject the father of obj
  */
  @Action
  @throws(classOf[Exception])
  def addRelation() {
    Graph.addRelation(
      relation.copy(subject = relation.obj, obj = relation.subject, relationship = "father"))
  }
}