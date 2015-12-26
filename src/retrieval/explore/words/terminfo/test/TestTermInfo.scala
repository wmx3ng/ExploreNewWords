package retrieval.explore.words.terminfo.test

import scala.collection.mutable.Set
import scala.collection.mutable.HashMap
import scala.io.Source
import retrieval.explore.words.util.obtainterms.ObtainTermInfo

object TestTermInfo extends App {

  val text = "中华人民共和国.....清华大学化学实验室.清华大学,化学实验室"
  //文本分词链;
  println("分词...")
  val words = ObtainTermInfo.obtainTerms(text)
  for (word <- words) {
    println(word)
  }

}