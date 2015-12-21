package retrieval.words.library

import scala.collection.mutable.Set
import scala.collection.mutable.HashMap
import scala.collection.mutable.Map

/**
 * 记录分词器分出的结果;
 * 每个Term的左邻字,右邻字及其频率;
 */
class TermsLibrary {
  var textLength: Int = _
  //存储词项; 
  private var terms = HashMap[String, Int]()
  //存储左邻词;
  private var leftTerms = new HashMap[String, Map[String, Int]]()
  //右邻词;
  private var rightTerms = new HashMap[String, Map[String, Int]]()

  //添加分词;
  def addTerm(word: String) {
    var cnt = 1
    if (terms contains (word))
      cnt += terms(word)
    terms += (word -> cnt)
  }

  def existsWord(word: String) =
    if (terms contains word) true else false

  //添加左邻词; 
  def addLeftTerm(word: String, left: String) {
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
  def addRigthTerm(word: String, right: String) {
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
  def print() {
    println("terms:")
    terms.keySet.foreach(println)
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