package retrieval.words.library

import scala.collection.mutable.Set
import scala.collection.mutable.HashMap
import scala.collection.mutable.Map

/**
 * 记录分词器分出的结果;
 * 每个Term的左邻字,右邻字及其频率;
 */
class TermLibrary {
  //当前处理的文本片断长度;
  var textLength : Int = _
  //存储分词器生成的Term; 
  private var terms = HashMap[String, Int]()

  //Term的左结合,右结合 信息熵;
  private var termEntropy = HashMap[String, (Double, Double)]()
  //Term的左邻词;
  private var leftTerms = new HashMap[String, Map[String, Int]]()
  //Term的右邻词;
  private var rightTerms = new HashMap[String, Map[String, Int]]()

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
  //添加分词;
  def addTerm(word : String) {
    var cnt = 1
    if (terms contains word)
      cnt += terms(word)
    terms += (word -> cnt)
  }

  def getTermFre(word : String) = if (terms contains word) terms(word) else 0

  def getTermCnt() = terms.size

  def existsTerm(word : String) =
    if (terms contains word) true else false

  //添加左邻词; 
  def addLeftTerm(word : String, left : String) {
    if (leftTerms contains word) {
      var cnt = 1
      if (leftTerms(word) contains left)
        cnt += leftTerms(word)(left)

      leftTerms(word) += (left -> cnt)
    } else {
      var left_br = new HashMap[String, Int]()
      left_br += (left -> 1)
      leftTerms += (word -> left_br)
    }
  }

  //添加右邻词;
  def addRigthTerm(word : String, right : String) {
    if (rightTerms contains word) {
      var cnt = 1
      if (rightTerms(word) contains right)
        cnt += rightTerms(word)(right)

      rightTerms(word) += (right -> cnt)
    } else {
      var right_br = new HashMap[String, Int]()
      right_br += (right -> 1)
      rightTerms += (word -> right_br)
    }
  }

  //打印
  def print() {
    println("terms:")
    terms.keySet.foreach { x => println("  " + x + "  " + terms(x) + " leftEntropy:" + termEntropy(x)._1 + " rightEntropy:" + termEntropy(x)._2) }
    println("left_br:")
    for (l <- leftTerms.keySet) {
      println("  " + l)
      leftTerms(l).keySet.foreach { x => println("    " + x + " " + leftTerms(l)(x)) }
    }
    println("right_br:")
    for (r <- rightTerms.keySet) {
      println("  " + r)
      rightTerms(r).keySet.foreach { x => println("    " + x + " " + rightTerms(r)(x)) }
    }
  }
}