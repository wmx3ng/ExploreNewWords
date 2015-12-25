package retrieval.words.library

import scala.collection.mutable.HashMap

/**
 * 产生的新词；
 * 需要求每个新词的内部凝固度;即一个词可能是由多种形式组成的,根据算法,找出其中一组;
 */
class NewTerm {

  //最终形成的新词;及其形成条件概率;
  private var newTerm = HashMap[String, (Double, Int)]()
  //记录形成新词的词组;
  private var newTuple = HashMap[String, (String, String)]()
  //词组信息;目前新词由两个词组成;统计每种组成的概率,选择出最小的那组;
  private var candidateTerm = HashMap[String, Map[(String, String), Int]]()

  //计算新词及其概率;
  def filterNewTerm(library : TermLibrary) {
    val cnt = library.getTermCnt
    for (candidate <- candidateTerm.keySet) {
      val sum = candidateTerm(candidate).foldLeft(0)((x : Int, y : (Any, Int)) => x + y._2)

      val ratio = for (group <- candidateTerm(candidate)) yield {
        val w1 = group._1._1
        val w2 = group._1._2

        //算法2,用分词的个数计算概率;
        (1.0 * sum * cnt / (library.getTermFre(w1) * library.getTermFre(w2)), group._1)
      }
      val res = ratio.reduce((x, y) => if (x._1 > y._1) y else x)
      newTerm += (candidate -> (res._1, sum))
      newTuple += (candidate -> res._2)
    }
  }

  def getNewTermTuple(nw : String) = {
    newTuple(nw)
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

  //需要用懒加载;
  def result(library : TermLibrary) = {
    val l = for (term <- newTerm if term._2._1 >= 300) yield { (term._1, term._2) }
    (l.filter(p => p._2._2 > 3).toList.sortWith((x, y) => x._2._1 < y._2._1).map(x => x._1 + " " + x._2 + " " + newTuple(x._1)._1 + " " + library.getTermEntropy(newTuple(x._1)._1) + " " + newTuple(x._1)._2 + " " + library.getTermEntropy(newTuple(x._1)._1))).mkString("\n")
  }
}