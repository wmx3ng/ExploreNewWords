package retrieval.words

import org.wltea.analyzer.lucene.IKAnalyzer
import org.apache.lucene.analysis.TokenStream
import java.io.StringReader
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.util.Version
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute
import org.apache.lucene.analysis.WordlistLoader
import org.apache.lucene.search.highlight.Highlighter
import org.apache.lucene.search.highlight.SimpleHTMLFormatter
import org.apache.lucene.search.highlight.QueryScorer

object Test {
  def main(args: Array[String]) {
    val test = " ’‚ «IK∑÷¥ ∆˜£¨≤‚ ‘”Ôæ‰£¨«Î∑÷¥ "
    val analyzer = new StandardAnalyzer(Version.LUCENE_34)
    val tokenStreams = analyzer.tokenStream("title", new StringReader(test))
    val simpleHTMLFormatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
    while (tokenStreams.incrementToken()) {
    }
    println("explore")
  }
}