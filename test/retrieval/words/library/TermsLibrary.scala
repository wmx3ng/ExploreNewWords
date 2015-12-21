package retrieval.words.library

import scala.collection.mutable.Set
import scala.collection.mutable.HashMap

/**
 * 记录分词器分出的结果;
 * 每个Term的左邻字,右邻字及其频率;
 */
class TermsLibrary {
  var textLength : Int = _
  //存储词项; 
  private var terms = HashMap[String, Int]()
  //存储左邻词;
  private var leftTerms = new HashMap[String, Int]()
  //右邻词;
  private var rightTerms = new HashMap[String, Int]()

  //添加分词;
  def addTerm( word : String ) {
    var cnt = 1
    if ( terms contains ( word ) )
      cnt += terms( word )
    terms += ( word -> cnt )
  }

  def existsWord( word : String ) =
    if ( terms contains word ) true else false

  //添加左邻词; 
  def addLeftTerm( word : String ) {
    var cnt = 1
    if ( leftTerms contains word ) {
      cnt += leftTerms( word )
    }
    leftTerms += ( word -> cnt )
  }

  //添加右邻词;
  def addRigthTerm( word : String ) {
    var cnt = 1
    if ( rightTerms contains word ) {
      cnt += rightTerms( word )
    }
    rightTerms += ( word -> cnt )
  }
}