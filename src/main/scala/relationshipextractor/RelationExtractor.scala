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
import edu.stanford.nlp.pipeline.{Annotation, StanfordCoreNLP}
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
    props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, mention, coref, natlog, openie")
    props.setProperty("timeout", "600000")
    val pipeline = new StanfordCoreNLP(props)
    return new RelationExtractor(pipeline, relations)
  }

}

class RelationExtractor(pipeline: StanfordCoreNLP, relDefs: Seq[RelationDefinition]) {

  def extractNamedEntities(document: Annotation): mutable.Set[Person] = {

    // Get all sentences.
    val sentences = document.get(classOf[SentencesAnnotation]).asScala
    val words = document.get(classOf[TokensAnnotation]).asScala
    val persons = for {word: CoreLabel <- words
         if word.ner() == "PERSON"
    } yield {
        Person(word.lemma(), sentences(word.sentIndex()).toString)
      }
    return mutable.Set[Person](persons.toArray:_*)
  }

  def extractRelationsFromText(text: String): Set[Person] = {

      // create an empty Annotation just with the given text
      val document = new Annotation(text)

      // run all Annotators on this text
      pipeline.annotate(document)

      // Find all NER
      val persons = extractNamedEntities(document)

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
      return persons.toSet
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
            getOrElse(Person(subj, subOrigin))

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
            getOrElse(Person(obj, objOrigin))

          val r = Relation(subjPerson, relation.title, objPerson, sentence.toString)
          subjPerson.relations.add(r)
          persons.add(subjPerson)
          persons.add(objPerson)

        }
      }

    }

}
