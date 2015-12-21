package retrieval.words.test

import retrieval.words.library.TermsLibrary
import scala.collection.mutable.Set
import scala.collection.mutable.HashMap

object Test extends App {
  val text = "看这个文本片段出现的次数是否足够多。我们可以把所有出现频数超过某个阈值的片段提取出来，作为该语料中的词汇输出。不过，光是出现频数高还不够，一个经常出现的文本片段有可能不是一个词，而是多个词构成的词组。"
  val words = ObtainTerms.obtainTerms( text )
  val library = new TermsLibrary
  library.textLength = text.length
  var newWords = HashMap[String, Int]()
  var pos = 0
  for ( word <- words ) {
    library.addTerm( word.getWord )
    //仅向左处理即可;
    for ( i <- 0 to pos - 1 ) {
      if ( word.getOffsetStart == words( i ).getOffsetEnd ) {
        val nw = words( i ).getWord + word.getWord
        var cnt = 1
        if ( newWords contains nw ) {
          cnt += newWords( nw );
        }

        newWords += ( nw -> cnt )
      }
    }
    pos += 1
  }

  for ( key <- newWords.keySet ) {
    if ( library.existsWord( key ) ) {
      println( key )
    }
  }

  for ( key <- newWords.keySet ) {
    println( key + " " + newWords( key ) )
  }
}