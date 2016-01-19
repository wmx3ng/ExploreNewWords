package retrieval.explore.words.struct

import scala.collection.mutable.HashMap
import retrieval.explore.words.util.constval.ExploreConstVal
import scala.collection.mutable.ListBuffer
import retrieval.explore.words.util.Util

/**
 * 产生的新词；
 * 需要求每个新词的内部凝固度;即一个词可能是由多种形式组成的,根据算法,找出其中一组;
 */
class NewTerm {
  //最终形成的新词;及其形成条件概率,词频;用于展示;
  private var newTerm = HashMap[String, (Double, Int)]()
  //记录组成新词的词组;Product用来保存长度不一的Tuple.
  private var newTuple = HashMap[String, Product]()
  //形成的新词的倒排索引;
  private var invertedNewTerm = HashMap[Int, ListBuffer[Product]]()

  //记录由于多重分词导致的重复数;ListBuffer存储的是当前新词的最后一个字符的位置;
  private var dupNewTerm = HashMap[String, ListBuffer[Int]]()

  //词组信息;统计每种组成的概率,选择出最小的那组;
  private var candidateTerm = HashMap[String, Map[Product, Int]]()

  //add dup
  def addDupNewTerm(nw : String, pos : Int) {
    if (dupNewTerm contains nw) {
      dupNewTerm(nw) += pos
    } else {
      val posList = ListBuffer[Int](pos)
      dupNewTerm += (nw -> posList)
    }
  }

  def getNewTermFrq(nw : String) = {
    if (dupNewTerm contains nw) {
      dupNewTerm(nw).toList.distinct.size
    } else 0
  }

  //计算新词及其概率;
  def filterNewTerm(library : TermLibrary) {
    val cnt = library.getTermCnt
    for (candidate <- candidateTerm.keySet) {
      val sum = getNewTermFrq(candidate)
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
      val ll = new ListBuffer[Product]()
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

  //得到新词的各个部分的内部左右熵及词频;
  def getEntropy(tuple : Product, library : TermLibrary) = {
    val entropy = for (t <- tuple.productIterator; w = t.toString()) yield {
      "[" + w + " " + library.getTermFre(w) + " " + library.getTermEntropy(w) + "]"
    }
    entropy.mkString("|")
  }

  //打印结果;
  def result(library : TermLibrary) = {
    val l = for (term <- newTerm if term._2._2 >= ExploreConstVal.showNum) yield {
      val ex = term._2._2
      val sample = getNewTermTuple(term._1).productIterator.map { x => library.getTermFre(x.toString()) }.toList
      (term._1, term._2, (Util.evaluateWeight1(ex, sample), Util.evaluateWeight2(ex, sample), Util.evaluateWeight3(sample), Util.evaluateWeight4(ex, sample)))
    }

    val res = l.toList.sortWith((x, y) => if (x._3._2 == y._3._2) x._2._2 > y._2._2 else x._3._2 < y._3._2)
      .map(x => x._1).take(2000).sortWith((x, y) => x.length() > y.length())

    val res_fil = for (
      t <- res.reverse;
      set1 = res.filter { x => x.length() > t.length() } if (!set1.exists { x => x contains (t) })
    ) yield {
      t
    }

    val r1 = for (ct <- res_fil; calV = newTerm(ct)) yield {
      val ex = calV._2
      val sample = getNewTermTuple(ct).productIterator.map { x => library.getTermFre(x.toString()) }.toList
      (ct, calV, (Util.evaluateWeight1(ex, sample), Util.evaluateWeight2(ex, sample), Util.evaluateWeight3(sample), Util.evaluateWeight4(ex, sample)))
    }

    val r2 = r1.toList.sortWith((x, y) => if (x._3._2 == y._3._2) x._2._2 > y._2._2 else x._3._2 < y._3._2)

    r2.mkString("\n")
  }
}