package retrieval.explore.words.util.constval

import org.wltea.analyzer.lucene.IKAnalyzer

object ExploreConstVal {
  //IK分词器;
  val analyzer = new IKAnalyzer
  //新词的词频;
  val showNum = 2
  //结果展示的过滤分数;
  val score = 600
  //新词的最大长度;(词的个数)
  val maxLength = 5
}