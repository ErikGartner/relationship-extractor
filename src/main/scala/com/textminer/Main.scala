package com.textminer
import edu.stanford.nlp.simple._;

object Main extends App {
    val doc: Document = new Document("Harry Potter likes Cat. He is also a wizard. The wizard does not like witches. Hermione is a witch. She is in fact, a mudblood.")
    println(doc.coref())
    val sent: Sentence = new Sentence("Lucy is in the sky with diamonds.")
    println(s"governor: ${sent.governor(0)}")
    val nerTags = Seq(sent.nerTags()) // [PERSON, O, O, O, O, O, O, O]
    val firstPOSTag: String = sent.posTag(0)   // NNP
    println(firstPOSTag)
}