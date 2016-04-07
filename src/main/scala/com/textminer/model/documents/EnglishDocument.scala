package com.textminer.model.documents

import com.textminer.model.ner.{CoreNLPNER, Person}
import com.textminer.model.taggers.CoreNLPTagger
import edu.stanford.nlp.simple.{Document => CoreNLPDocument}

/**
  * Created by erik on 04/04/16.
  */
class EnglishDocument(override val text:String) extends Document(text) with CoreNLPTagger with CoreNLPNER {

  override var coreNlpDocument = new CoreNLPDocument(text)

}
