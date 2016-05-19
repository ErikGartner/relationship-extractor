package relationshipextractor

import java.util
import java.util.Properties

import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation
import edu.stanford.nlp.hcoref.CorefCoreAnnotations.CorefMentionsAnnotation
import edu.stanford.nlp.ie.util.RelationTriple
import edu.stanford.nlp.ling.CoreAnnotations._
import edu.stanford.nlp.ling.{CoreAnnotations, CoreLabel, Sentence}
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations.RelationTriplesAnnotation
import edu.stanford.nlp.naturalli.OpenIE
import edu.stanford.nlp.pipeline.{Annotation, StanfordCoreNLP, StanfordCoreNLPClient}
import edu.stanford.nlp.semgraph.SemanticGraph
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation
import edu.stanford.nlp.trees.Tree
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation
import edu.stanford.nlp.pipeline.{Annotation, StanfordCoreNLP, StanfordCoreNLPClient}
import edu.stanford.nlp.util.CoreMap
import edu.stanford.nlp.hcoref.CorefCoreAnnotations
import edu.stanford.nlp.hcoref.data.CorefChain
import edu.stanford.nlp.hcoref.data.Mention

import scala.collection.JavaConverters._
import scala.collection.mutable

object RelationExtractor {

  def apply(): RelationExtractor = {
    val relations = RelationDefinition.getRelations.get

    val props = new Properties()
    props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, mention, depparse, coref, natlog, openie")
    props.setProperty("timeout", "600000")
    val pipeline = new StanfordCoreNLP(props)
    // val pipeline = new StanfordCoreNLPClient(props, "localhost", 9000, 4)
    return new RelationExtractor(pipeline, relations)
  }

}

class RelationExtractor(pipeline: StanfordCoreNLP, relDefs: Seq[RelationDefinition]) {

  def extractNamedEntities(document: Annotation, persons: mutable.Set[Person]) = {

    // Get all sentences.
    val sentences = document.get(classOf[SentencesAnnotation]).asScala
    val words = document.get(classOf[TokensAnnotation]).asScala
    for {word: CoreLabel <- words
         if word.ner() == "PERSON"
    } {
        val person = persons.
        filter(p => p.name.equals(word.lemma()) || p.mentions.contains(word.lemma())).
        headOption.
        getOrElse(Person(word.lemma(), sentences(word.sentIndex()).toString))
        person.mentions.add(word.lemma())
        persons.add(person)
      }
  }

  def extractRelationsFromText(text: String, persons: mutable.Set[Person]) = {

      // create an empty Annotation just with the given text
      val document = new Annotation(text)

      // run all Annotators on this text
      pipeline.annotate(document)

      // Find all NER
      extractNamedEntities(document, persons)

      // These are all corefchains in the document used to resolve corefs.
      val chains = document.get(classOf[CorefCoreAnnotations.CorefChainAnnotation])

      // Get all sentences.
      val sentences = document.get(classOf[SentencesAnnotation]).asScala
      for (sentence: CoreMap <- sentences) {

        // Get relationship triples in the sentence
        val oie = sentence.get(classOf[RelationTriplesAnnotation])

        if(oie != null) {

          for (triple: RelationTriple <- oie.asScala) {

            matchOIETriple(triple, persons, sentence, sentences, chains)

          }
        }

      }
    }

    def matchOIETriple(triple: RelationTriple, persons: mutable.Set [Person], sentence: CoreMap, sentences: mutable.Buffer[CoreMap], chains: util.Map[Integer, CorefChain]) = {

      for {relation: RelationDefinition <- relDefs
           relString <- RelationDefinition.getRelationLabels(relation)} {

        val reg = s"\\b$relString\\b".r

        // Check if the relation is one if the sought after
        if (reg.findFirstIn(triple.relationLemmaGloss.toLowerCase).nonEmpty) {

          // Check if subject or object is an coreference
          val subjectCorefId = triple.subjectHead().get(classOf[CorefCoreAnnotations.CorefClusterIdAnnotation])
          val objectCorefId = triple.objectHead().get(classOf[CorefCoreAnnotations.CorefClusterIdAnnotation])

          var subj = ""
          var obj = ""
          var subOrigin = ""
          var objOrigin = ""

          // resolve coreferences
          if (subjectCorefId == null) {
            subj = triple.subjectLemmaGloss()
            subOrigin = sentence.toString
          } else {
            subj = chains.get(subjectCorefId).getRepresentativeMention().mentionSpan
            subOrigin = sentences(chains.get(subjectCorefId).getRepresentativeMention().sentNum).toString
          }

          val subjPerson = persons.
            filter(p => p.name.equals(subj) || p.mentions.contains(subj)).
            headOption.
            getOrElse(Person(subj, subOrigin, mentions = mutable.Set(subj)))

          if (objectCorefId == null) {
            obj = triple.objectLemmaGloss()
            objOrigin = sentence.toString
          } else {
            obj = chains.get(objectCorefId).getRepresentativeMention().mentionSpan
            objOrigin = sentences(chains.get(objectCorefId).getRepresentativeMention().sentNum).toString
          }

          val objPerson = persons.
            filter(p => p.name.equals(obj) || p.mentions.contains(obj)).
            headOption.
            getOrElse(Person(obj, objOrigin, mentions = mutable.Set(obj)))

          val r = Relation(subjPerson, relString, relation, objPerson, sentence.toString)
          subjPerson.relations.add(r)
          persons.add(subjPerson)
          persons.add(objPerson)

        }
      }

    }

}
