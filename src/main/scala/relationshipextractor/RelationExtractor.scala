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
    val relations = Relation.getRelations.get

    val props = new Properties()
    props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, mention, coref, natlog, openie")
    props.setProperty("timeout", "600000")
    val pipeline = new StanfordCoreNLP(props)
    return new RelationExtractor(pipeline, relations)
  }

}


class RelationExtractor(pipeline: StanfordCoreNLP, relationships: Seq[Relation]) {

  def extractRelationsFromText(text: String) = {

      val relations = relationships.flatMap(rel => Relation.getRelationLabels(rel))

      // create an empty Annotation just with the given text
      val document = new Annotation(text)

      // run all Annotators on this text
      pipeline.annotate(document)

      // These are all corefchains in the document used to resolve corefs.
      val chains = document.get(classOf[CorefCoreAnnotations.CorefChainAnnotation])

      // We store relationsships as ("Subject" "Relationship" "Object")
      val foundRelationships = mutable.Set[Tuple3[String, String, String]]()

      // Get all sentences.
      val sentences = document.get(classOf[SentencesAnnotation]).asScala
      for (sentence: CoreMap <- sentences) {

        // Get relationship triples in the sentence
        val oie = sentence.get(classOf[RelationTriplesAnnotation])

        if(oie != null) {


          for (triple: RelationTriple <- oie.asScala) {
            for (relation <- relations) {

              // Check if the relation is one if the sought after
              if (triple.relationLemmaGloss.toLowerCase.contains(relation)) {

                // Check if subject or object is an coreference
                val subjectCorefId = triple.subjectHead().get(classOf[CorefCoreAnnotations.CorefClusterIdAnnotation])
                val objectCorefId = triple.objectHead().get(classOf[CorefCoreAnnotations.CorefClusterIdAnnotation])

                var subj = ""
                var obj = ""

                // resolve coreferences
                if (subjectCorefId == null) {
                  subj = triple.subjectLemmaGloss()
                } else {
                  subj = chains.get(subjectCorefId).getRepresentativeMention().mentionSpan
                }

                if (objectCorefId == null) {
                  obj = triple.objectLemmaGloss()
                } else {
                  obj = chains.get(objectCorefId).getRepresentativeMention().mentionSpan
                }

                foundRelationships += Tuple3(subj, relation, obj)

              }
            }
          }
        }

      }
      if(foundRelationships.size > 0)
        println(foundRelationships)
    }

}
