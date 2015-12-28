package retrieval.explore.words.terminfo.test

import scala.collection.mutable.Set
import scala.collection.mutable.HashMap
import scala.io.Source
import retrieval.explore.words.util.obtainterms.ObtainTermInfo

object TestTermInfo extends App {

  val text = "加入特别提款权"
  //文本分词链;
  println("分词...")
  val words = ObtainTermInfo.obtainTerms(text)
  for (word <- words) {
    println(word)
  }
}