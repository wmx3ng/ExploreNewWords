package retrieval.explore.words.util.obtainterms

import org.wltea.analyzer.lucene.IKAnalyzer
import java.io.StringReader
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import scala.collection.mutable.ListBuffer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute
import retrieval.explore.words.library.TermInfo

/**
 * 获取分词词项;
 */
object ObtainTermInfo {
  def obtainTerms(text : String) : List[TermInfo] = {
    val analyzer = new IKAnalyzer
    val tokenStreams = analyzer.tokenStream("title", new StringReader(text))
    //必须有这一步;否则报错;
    tokenStreams.reset()

    //词项信息;
    val termAttr = tokenStreams.addAttribute(classOf[CharTermAttribute])
    //偏移信息;
    val offset = tokenStreams.addAttribute(classOf[OffsetAttribute])
    //位置增量信息;
    //    val posIncr = tokenStreams.addAttribute(classOf[PositionIncrementAttribute])
    //词项类型;
    //    val termType = tokenStreams.addAttribute( classOf[TypeAttribute] )

    var wordsBuffer = ListBuffer[TermInfo]()
    while (tokenStreams.incrementToken()) {
      wordsBuffer += (new TermInfo(termAttr.toString(), offset.startOffset, offset.endOffset))
    }
    wordsBuffer.toList
  }
}