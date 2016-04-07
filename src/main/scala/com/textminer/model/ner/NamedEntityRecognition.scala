package com.textminer.model.ner

/**
  * Created by erik on 04/04/16.
  */
trait NamedEntityRecognition {

  def persons(): Seq[Person]

}
