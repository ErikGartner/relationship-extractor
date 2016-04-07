package com.textminer.model.ner

import com.textminer.model.documents.EnglishDocument
import org.scalatest._

class CoreNLPNERTests extends FlatSpec with Matchers {

  "Persons" should "extract lists of persons" in {

    val sentence = "Harry Potter is a wizard from London."
    val doc = new EnglishDocument(sentence)
    val actual:Seq[Person] = doc.persons()
    val expected = Seq("Harry")

    actual.size should be (1)
    actual(0).name should be (expected(0))

  }

  "Hello" should "have tests" in {
    true should === (true)
  }

}
