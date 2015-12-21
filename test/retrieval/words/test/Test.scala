package retrieval.words.test

import scala.collection.mutable.Set
import scala.collection.mutable.HashMap
import retrieval.words.library.TermsLibrary

object Test extends App {
  val text = "清华大学化学实验室"
  val words = ObtainTerms.obtainTerms(text)
  val library = new TermsLibrary
  library.textLength = text.length
  var newWords = HashMap[String, Int]()
  var pos = 0
  for (word <- words) {
    library.addTerm(word.getWord)
    for (i <- 0 to pos - 1) {
      if (word.getOffsetStart == words(i).getOffsetEnd) {
        val nw = words(i).getWord + word.getWord
        var cnt = 1
        if (newWords contains nw) {
          cnt += newWords(nw);
        }
        library.addLeftTerm(word.getWord, words(i).getWord)
        library.addRigthTerm(words(i).getWord, word.getWord)
        newWords += (nw -> cnt)
      }
    }
    pos += 1
  }

  for (key <- newWords.keySet) {
    if (library.existsWord(key)) {
      println(key)
    }
  }

  for (key <- newWords.keySet) {
    println(key + " " + newWords(key))
  }
  library.print()

}