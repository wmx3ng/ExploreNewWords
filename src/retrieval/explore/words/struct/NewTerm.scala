package retrieval.explore.words.struct

import scala.collection.mutable.HashMap
import retrieval.explore.words.util.constval.ExploreConstVal
import scala.collection.mutable.ListBuffer

/**
 * 产生的新词；
 * 需要求每个新词的内部凝固度;即一个词可能是由多种形式组成的,根据算法,找出其中一组;
 */
class NewTerm {

  //最终形成的新词;及其形成条件概率,词频;
  private var newTerm = HashMap[String, (Double, Int)]()
  //记录组成新词的词组;Product用来保存长度不一的Tuple.
  private var newTuple = HashMap[String, Product]()
  //形成的新词的倒排索引;
  private var invertedNewTerm = HashMap[Int, ListBuffer[Product]]()

  //词组信息;目前新词由两个词组成;统计每种组成的概率,选择出最小的那组;
  private var candidateTerm = HashMap[String, Map[Product, Int]]()

  //计算新词及其概率;
  def filterNewTerm(library : TermLibrary) {
    val cnt = library.getTermCnt
    for (candidate <- candidateTerm.keySet) {
      val sum = candidateTerm(candidate).foldLeft(0)((x : Int, y : (Any, Int)) => x + y._2)

      val ratio = for (group <- candidateTerm(candidate)) yield {
        val demon = group._1.productIterator.map { x => library.getTermFre(x.toString()) }.foldLeft(1.0)((x : Double, y : Int) => x * y)

        //算法2,用分词的个数计算概率;
        (1.0 * sum * math.pow(cnt, group._1.productIterator.size - 1) / demon, group._1)
      }
      val res = ratio.reduce((x, y) => if (x._1 > y._1) y else x)
      newTerm += (candidate -> (res._1, sum))
      newTuple += (candidate -> res._2)
    }
  }

  def getNewTermTuple(nw : String) = {
    newTuple(nw)
  }

  //添加新词的倒排;
  def addNewTermInvertedIndex(pos : Int, nw : Product) {
    if (invertedNewTerm contains pos) {
      invertedNewTerm(pos) += nw
    } else {
      var ll = new ListBuffer[Product]()
      ll += nw
      invertedNewTerm += (pos -> ll)
    }
  }

  //根据位置获取新词;
  def getInvertedNewTerm(pos : Int) = {
    if (invertedNewTerm contains pos) {
      invertedNewTerm(pos)
    } else {
      new ListBuffer[Product]()
    }
  }

  //增加新的词组;统计其数量;
  def addCandidateTerm(nw : String, termGroup : Product) {

    if (candidateTerm contains nw) {
      var cnt = 1
      if (candidateTerm(nw) contains termGroup) {
        cnt += candidateTerm(nw)(termGroup)
      }
      candidateTerm(nw) += (termGroup -> cnt)
    } else {
      var newGroup = collection.immutable.HashMap[Product, Int]()
      newGroup += (termGroup -> 1)
      candidateTerm += (nw -> newGroup)
    }
  }

  //需要用懒加载;
  def result(library : TermLibrary) = {
    val l = for (term <- newTerm if term._2._1 >= ExploreConstVal.score) yield { (term._1, term._2) }
    (l.filter(p => p._2._2 >= ExploreConstVal.showNum).toList.sortWith((x, y) => x._2._1 < y._2._1).map(
      x =>
        x._1 + " " + x._2 + " " + getNewTermTuple(x._1))).mkString("\n")
  }
}