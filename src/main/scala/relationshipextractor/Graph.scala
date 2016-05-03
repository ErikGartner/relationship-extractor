package relationshipextractor

import org.graphstream.graph._
import org.graphstream.graph.implementations._
import java.util.Iterator
import scala.collection.JavaConverters._

object Graph extends App {
    val graph: Graph = new SingleGraph("tutorial 1")
    val styleSheet: String =
    """
    node {
      fill-color: hotpink;
    }

    node.marked {
      fill-color: red;
    }

    node:clicked {
      fill-color: blue;
    }
    """
    graph.addAttribute("ui.stylesheet", styleSheet)
    graph.setAutoCreate(true)
    graph.setStrict(false)
    graph.display()
    addPersons(Seq(Person("Turner", "Some setne")))
    addRelations(Seq(
        Relation(Person("Turner", "Some sentence"),
            "child",
            Person("neighbouring landowner", "Some sentence"),
            "Some sentence"),
        Relation(Person("Turner", "Some sentence"),
            "wife",
            Person("Mr. James McCarthy", "Some other sentence"),
            "Some other sentence"),
        Relation(Person("Turner", "child of same age"),
            "child",
            Person("same age", "Some sm"),
            "Some sen")
        )
    )

    private def addRelationToGraph(relation: Relation) {
        val a: Node = addPersonToGraph(relation.subject)
        val b: Node = addPersonToGraph(relation.obj)
        val edge: Edge = graph.addEdge(relation.hashCode.toString, a, b, true)
        edge.setAttribute("ui.label", relation.relationship)
    }

    private def addPersonToGraph(person: Person): Node = {
        val theNode: Node = graph.addNode(person.hashCode.toString)
        theNode.addAttribute("sentence", person.sentence)
        theNode.addAttribute("ui.label", person.name)
        theNode
    }

    def addRelations(relations: Seq[Relation]) {
        relations.foreach(addRelationToGraph(_))
    }

    def addPersons(persons: Seq[Person]) {
        persons.foreach(addPersonToGraph(_))
    }
}