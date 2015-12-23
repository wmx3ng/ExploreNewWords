package retrieval.words.library

import scala.collection.mutable.HashMap

/**
 * 产生的新词；
 * 需要求每个新词的内部凝固度;即一个词可能是由多种形式组成的,根据算法,找出其中一组;
 */
class NewTerm {

  //最终形成的新词;及其形成条件概率;
  private var newTerm = HashMap[String, Double]()
  //词组信息;目前新词由两个词组成;统计每种组成的概率,选择出最小的那组;
  private var candidateTerm = HashMap[String, Map[(String, String), Int]]()

  //计算新词及其概率;
  def filterNewTerm(library : TermLibrary) {
    val cnt = library.getTermCnt
    for (candidate <- candidateTerm.keySet) {
      val sum = candidateTerm(candidate).reduce((x, y) => (x._1, x._2 + x._2))._2
      val ratio = for (group <- candidateTerm(candidate)) yield {
        //        group._2 * 1.0 / library.textLength
        val w1 = group._1._1
        val w2 = group._1._2
        //算法1,用文章长度计算概率;
        //        1.0 * candidate.size * sum * library.textLength / (w1.size * library.getTermFre(w1) * w2.size * library.getTermFre(w2))
        println(cnt + " " + sum + " " + library.getTermFre(w1) + " " + library.getTermFre(w2))
        1.0 * sum * cnt / (library.getTermFre(w1) * library.getTermFre(w2))
      }
      newTerm += (candidate -> ratio.reduce((x, y) => if (x > y) y else x))
    }
  }

  //增加新的词组;统计其数量;
  def addCandidateTerm(nw : String, termGroup : (String, String)) {
    if (candidateTerm contains nw) {
      var cnt = 1
      if (candidateTerm(nw) contains termGroup) {
        cnt += candidateTerm(nw)(termGroup)
      }
      candidateTerm(nw) += (termGroup -> cnt)
    } else {
      var newGroup = collection.immutable.HashMap[(String, String), Int]()
      newGroup += (termGroup -> 1)
      candidateTerm += (nw -> newGroup)
    }
  }

  def print() {
    for (term <- newTerm.keySet; if newTerm(term) > 100) {
      println(term + " " + newTerm(term))
    }
  }
}