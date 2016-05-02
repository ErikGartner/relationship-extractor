package relationshipextractor

import org.graphstream.graph._
import org.graphstream.graph.implementations._

object GraphTest extends App {
  val graph: Graph = new SingleGraph("Tutorial 1")

  graph.addNode("A")
  graph.addNode("B")
  graph.addNode("C")
  graph.addEdge("AB", "A", "B")
  graph.addEdge("BC", "B", "C")
  graph.addEdge("CA", "C", "A")
  val nodeA: Node = graph.getNode("A")
  nodeA.setAttribute("foo", "bar")

  graph.display()
}
