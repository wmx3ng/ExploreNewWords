package retrieval.words.test

import scala.collection.mutable.Set
import scala.collection.mutable.HashMap
import retrieval.words.library.TermLibrary
import retrieval.words.util.ObtainTermInfo
import scala.io.Source
import retrieval.words.library.NewTerm

object Test extends App {
  //  val text = "清华大学化学实验室"
  val path = "/home/wang/sg"
  var tmpLine = "";
  Source.fromFile(new java.io.File(path)).getLines().toList.foreach(tmpLine += _)
  val lines = tmpLine.toString()
  //  val text = ""
  val words = ObtainTermInfo.obtainTerms(lines)
  val library = new TermLibrary
  val candidate = new NewTerm

  library.textLength = lines.length()

  var pos = 0
  for (word <- words) {
    library.addTerm(word.getWord)
    for (i <- 0 to pos - 1) {
      if (word.getOffsetStart == words(i).getOffsetEnd) {
        //统计一个词的左邻字;
        library.addLeftTerm(word.getWord, words(i).getWord)
        library.addRigthTerm(words(i).getWord, word.getWord)
      }
    }
    pos += 1
  }

  pos = 0
  for (word <- words) {
    for (i <- 0 to pos - 1) {
      if (word.getOffsetStart == words(i).getOffsetEnd) {
        val nw = words(i).getWord + word.getWord
        if (!(library existsTerm nw)) {
          candidate.addCandidateTerm(nw, (words(i).getWord, word.getWord))
        }
      }
    }
    pos += 1
  }

  //计算分词的左右信息熵;用于评估结合自由度;
  library.calInfoEntropy()
  //  library.print()
  candidate.filterNewTerm(library)
  candidate.print()
  //  println(candidate.result)
}