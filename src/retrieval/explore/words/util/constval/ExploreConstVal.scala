package retrieval.explore.words.util.constval

import org.wltea.analyzer.lucene.IKAnalyzer
import org.apache.lucene.analysis.standard.StandardAnalyzer

object ExploreConstVal {
  //IK分词器;
  val analyzer = new IKAnalyzer
  //  val analyzer = new IKAnalyzer(true)
  //  val analyzer = new StandardAnalyzer
  //新词的词频;
  val showNum = 2

  //词长度过滤;(字符数或者是组成词项的个数)
  val termLen = 3;

  //结果展示的过滤分数;一般是概率分数;
  val score = 50

  //新词的最大长度;(词的个数)
  val maxLength = 5
}