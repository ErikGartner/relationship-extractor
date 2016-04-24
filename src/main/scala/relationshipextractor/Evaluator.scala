package relationshipextractor

/**
  * Created by erik on 24/04/16.
  */
object Evaluator {

  def levenshtein[A](a: Iterable[A], b: Iterable[A]) =
    ((0 to b.size).toList /: a)((prev, x) =>
      (prev zip prev.tail zip b).scanLeft(prev.head + 1) {
        case (h, ((d, v), y)) => Math.min(Math.min(h + 1, v + 1), d + (if (x == y) 0 else 1))
      }) last


  def fscore(recall: Int, precision: Int) = return 2 * precision.toFloat * recall / (precision + recall)

  def recall(truePositives: Int, falsePositives: Int) = return truePositives.toFloat / (truePositives + falsePositives)

  def precision(truePositives: Int, falseNegatives: Int) = return truePositives.toFloat / (truePositives + falseNegatives)

}
