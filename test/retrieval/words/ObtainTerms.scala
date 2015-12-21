package retrieval.words

import org.wltea.analyzer.lucene.IKAnalyzer
import org.apache.lucene.analysis.TokenStream
import java.io.StringReader
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.util.Version
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute

import org.apache.lucene.search.highlight.Highlighter
import org.apache.lucene.search.highlight.SimpleHTMLFormatter
import org.apache.lucene.search.highlight.QueryScorer
import org.apache.lucene.queryparser.classic.QueryParser;

/**
 * 高亮测试
 */
object HL {
  def highLighter(text: String, queryString: String) = {
    val analyzer = new StandardAnalyzer()
    val tokenStreams = analyzer.tokenStream("title", new StringReader(text))
    val parser = new QueryParser("title", new StandardAnalyzer())
    val query = parser.parse(queryString)
    val simpleHTMLFormatter = new SimpleHTMLFormatter("<font color='red'>", "</font>")
    val scorer = new QueryScorer(query)
    val hl = new Highlighter(simpleHTMLFormatter, scorer)
    hl.getBestFragment(tokenStreams, text)
  }

  def main(args: Array[String]) {
    val text = "北京安监部门：清华大学实验室爆炸事故在使用氢气做化学实验中发生"
    val queryString = "清华大学"
    println(highLighter(text, queryString))

  }
}