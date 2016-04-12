package com.textminer
import edu.stanford.nlp.simple._;

object Main extends App {
    val sent: Sentence = new Sentence("Lucy is in the sky with diamonds.")
    val nerTags = Seq(sent.nerTags()) // [PERSON, O, O, O, O, O, O, O]
    val firstPOSTag: String = sent.posTag(0)   // NNP
    println(firstPOSTag)
}