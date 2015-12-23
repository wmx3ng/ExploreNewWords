package retrieval.words.test

import scala.collection.mutable.Set
import scala.collection.mutable.HashMap
import retrieval.words.library.TermLibrary

object Test extends App {
  val text = "清华大学化学实验室"
  val words = ObtainTermInfo.obtainTerms(text)
  val library = new TermLibrary
  library.textLength = text.length

  var pos = 0
  for (word <- words) {
    library.addTerm(word.getWord)
    for (i <- 0 to pos - 1) {
      if (word.getOffsetStart == words(i).getOffsetEnd) {
        //计算一个词的左邻字,右邻字信自熵;
        library.addLeftTerm(word.getWord, words(i).getWord)
        library.addRigthTerm(words(i).getWord, word.getWord)
      }
    }
    pos += 1
  }

  library.print()
}