package com.textminer.model.ner

import com.textminer.model.bases.CoreNLPBase
import edu.stanford.nlp.simple.Sentence

import scala.collection.mutable

/**
  * Created by erik on 04/04/16.
  */
trait CoreNLPNER extends NamedEntityRecognition with CoreNLPBase{

  override def persons(): Seq[Person] = {

    val persons:mutable.MutableList[Person] = mutable.MutableList()

    for(sentence <- scala.collection.JavaConversions.asScalaBuffer(coreNlpDocument.sentences())){
      val tags = List(sentence.nerTags())

      for((tag, i) <- tags.view.zipWithIndex
          if tag.equals("PERSON")) {
        persons += new Person(sentence.word(i), sentence.toString)
      }

    }

    return persons

  }

}
