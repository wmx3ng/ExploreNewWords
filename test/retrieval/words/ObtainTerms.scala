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
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.analysis.tokenattributes.OffsetAttributeImpl
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttributeImpl
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute
import org.apache.lucene.analysis.tokenattributes.CharTermAttributeImpl
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.apache.lucene.analysis.tokenattributes.TypeAttribute

/**
 * 获取分词词项;
 */
object HL {
  def obtainTerms( text : String ) {
    val analyzer = new IKAnalyzer
    val tokenStreams = analyzer.tokenStream( "title", new StringReader( text ) )
    tokenStreams.reset()

    val termAttr = tokenStreams.addAttribute( classOf[CharTermAttribute] )
    val offset = tokenStreams.addAttribute( classOf[OffsetAttribute] )
    val pos = tokenStreams.addAttribute( classOf[PositionIncrementAttribute] )
    val termType = tokenStreams.addAttribute( classOf[TypeAttribute] )

    while ( tokenStreams.incrementToken() ) {
      println( "positionIncre:" + pos.getPositionIncrement + " type:" + termType + " term:" + termAttr + " offset_begin:" + offset.startOffset() + " offset_end:" + offset.endOffset() )
    }
  }

  def main( args : Array[String] ) {
    val text = "现在社会上有很多坑爹的货"
    obtainTerms( text )
  }
}