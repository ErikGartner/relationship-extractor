package relationshipextractor

trait RelationshipRule {
  def input(person: Person): Unit
  def hasFired(): Boolean
  def resetHasFired(): Unit
}
