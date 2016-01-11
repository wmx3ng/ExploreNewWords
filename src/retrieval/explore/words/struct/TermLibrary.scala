package retrieval.explore.words.struct

import scala.collection.mutable.Set
import scala.collection.mutable.HashMap
import scala.collection.mutable.Map
import scala.collection.mutable.ListBuffer

/**
 * 记录分词器分出的结果;
 * 每个Term的左邻字,右邻字及其频率;
 */
class TermLibrary {
  //存储分词器生成的Term; 
  private var terms = new HashMap[String, Int]()
  //offsetEnd->(word,offsetStart);Int->(String,Int)
  private var invertedIndex = new HashMap[Int, collection.mutable.ListBuffer[String]]()

  //Term的左结合,右结合 信息熵;
  private var termEntropy = new HashMap[String, (Double, Double)]()
  //Term的左邻词;
  private var leftTerms = new HashMap[String, Map[String, Int]]()
  //Term的右邻词;
  private var rightTerms = new HashMap[String, Map[String, Int]]()

  //get topK terms
  def getTopKTerms(cnt : Int) = {
    terms.map(x => (x._1, x._2)).filter(_._1.length() != 1).toList.sortWith((x, y) => x._2 > y._2).slice(0, cnt).map(_.toString()).mkString("\n")
  }

  //offset_End->word
  def addInvertedIndex(pos : Int, word : String) {
    if (invertedIndex contains pos) {
      invertedIndex(pos) += word
    } else {
      var l = ListBuffer[String]()
      l += word
      invertedIndex += (pos -> l)
    }
  }

  def getInvertedIndex(pos : Int) = {
    if (invertedIndex contains pos) {
      invertedIndex(pos)
    } else {
      ListBuffer[String]()
    }
  }

  //计算内部聚合度;
  def calInfoEntropy() {
    for (term <- terms.keySet) {
      //左熵;
      var leftEntropy = 0.0D
      var rightEntropy = 0.0D
      var sum = 0
      if (leftTerms contains term) {
        for (left <- leftTerms(term)) {
          sum += left._2
        }
        for (left <- leftTerms(term)) {
          val p = 1.0 * left._2 / sum
          leftEntropy += (-p * math.log(p))
        }
      }

      if (rightTerms contains term) {
        sum = 0
        for (right <- rightTerms(term)) {
          sum += right._2
        }
        for (right <- rightTerms(term)) {
          val p = 1.0 * right._2 / sum
          rightEntropy += (-p * math.log(p))
        }
      }
      termEntropy += (term -> (leftEntropy, rightEntropy))
    }
  }

  def getTermEntropy(word : String) = {
    termEntropy(word)
  }

  //添加分词;
  def addTerm(word : String) {
    var cnt = 1
    if (terms contains word)
      cnt += terms(word)
    terms += (word -> cnt)
  }

  def getTermFre(word : String) = if (terms contains word) terms(word) else 0

  def getTermCnt() = terms.size

  //查询分词器的词集中是否存在某一个词；
  def existsTerm(word : String) =
    if (terms contains word) true else false

  private def addNighTerm(basicWord : String, destWord : String, neighTerm : Map[String, Map[String, Int]]) {
    if (neighTerm contains basicWord) {
      var cnt = 1
      if (neighTerm(basicWord) contains destWord)
        cnt += neighTerm(basicWord)(destWord)

      neighTerm(basicWord) += (destWord -> cnt)
    } else {
      val neigh_br = new HashMap[String, Int]()
      neigh_br += (destWord -> 1)
      neighTerm += (basicWord -> neigh_br)
    }
  }

  //添加左邻词; 
  def addLeftTerm(basicWord : String, destWord : String) {
    addNighTerm(basicWord, destWord, leftTerms)
  }

  //添加右邻词;
  def addRigthTerm(basicWord : String, destWord : String) {
    addNighTerm(basicWord, destWord, rightTerms)
  }
}