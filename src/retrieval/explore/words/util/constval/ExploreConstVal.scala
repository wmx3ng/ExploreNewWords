package retrieval.explore.words.util.constval

import org.wltea.analyzer.lucene.IKAnalyzer
import org.apache.lucene.analysis.standard.StandardAnalyzer

object ExploreConstVal {
  //IK分词器;
  //  val analyzer = new IKAnalyzer
  val analyzer = new StandardAnalyzer
  //新词的词频;
  val showNum = 3
  //结果展示的过滤分数;
  val score = 1000
  //新词的最大长度;(词的个数)
  val maxLength = 4
}