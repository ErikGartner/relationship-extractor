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
    addPerson(Person("Turner", "Some setne"))
    Seq(
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
        ).foreach(addRelation _)

    def addRelation(relation: Relation) {
        val a: Node = addPerson(relation.subject)
        val b: Node = addPerson(relation.obj)
        val edge: Edge = graph.addEdge(relation.hashCode.toString, a, b, true)
        edge.setAttribute("ui.label", relation.relationship)
    }

    def addPerson(person: Person): Node = {
        println(s"Adding person to graph: ${person}!!!")
        val theNode: Node = graph.addNode(person.hashCode.toString)
        theNode.addAttribute("sentence", person.sentence)
        theNode.addAttribute("ui.label", person.name)
        theNode
    }
}