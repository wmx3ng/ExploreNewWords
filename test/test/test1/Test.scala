package retrieval.analysis.test.ansj

import org.ansj.splitWord.analysis.ToAnalysis

object TestAnsj extends App {
  val text = "北京雾霾北京华悦科技有限公司中国武林风"
  val terms = ToAnalysis.parse(text)
  for (i <- 0 until terms.size()) {
    println(terms.get(i).getName + " " + terms.get(i).getNatureStr + " " + terms.get(i).score())
  }
}