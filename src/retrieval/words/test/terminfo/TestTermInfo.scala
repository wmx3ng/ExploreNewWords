package retrieval.words.test.terminfo

import scala.collection.mutable.Set
import scala.collection.mutable.HashMap
import retrieval.words.library.TermLibrary
import retrieval.words.util.ObtainTermInfo
import scala.io.Source
import retrieval.words.library.NewTerm

object TestTermInfo extends App {

  val text = "中华人民共和国.....清华大学化学实验室.清华大学,化学实验室"
  //文本分词链;
  println("分词...")
  val words = ObtainTermInfo.obtainTerms(text)
  for (word <- words) {
    println(word)
  }

}