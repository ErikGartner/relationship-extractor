package com.textminer.model

import com.textminer.model.ner.CoreNLPNER
import com.textminer.model.taggers.CoreNLPTagger

/**
  * Created by erik on 04/04/16.
  */
object Document {



}

abstract class Document {

}

class EnglishDocument extends Document with CoreNLPTagger with CoreNLPNER {

}
