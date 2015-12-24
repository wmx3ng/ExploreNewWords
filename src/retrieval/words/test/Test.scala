package retrieval.words.test

import scala.collection.mutable.Set
import scala.collection.mutable.HashMap
import retrieval.words.library.TermLibrary
import retrieval.words.util.ObtainTermInfo
import scala.io.Source
import retrieval.words.library.NewTerm

object Test {

  def main(args : Array[String]) {
    //  val text = "清华大学化学实验室"

    //文件路径;
    //    val path = "/home/wang/sg"
    val path = "/home/wang/Documents/ShareFolder/shz2.txt"

    //从文件中读取文本;
    val lines = Source.fromFile(new java.io.File(path)).getLines().toList.mkString

    //文本分词统计结果;
    val library = new TermLibrary
    //候选词集合;
    val candidate = new NewTerm

    //文本分词链;
    println("分词...")
    val words = ObtainTermInfo.obtainTerms(lines)
    //文本长度;
    library.textLength = lines.length()

    //统计文本片断的分词信息;
    //    for (
    //      i <- 0 until words.length; postW = words(i);
    //      j <- 0 until i; prevW = words(j);
    //      if postW.getOffsetStart == prevW.getOffsetEnd
    //    ) {
    //      library.addLeftTerm(postW.getWord, prevW.getWord)
    //      library.addRigthTerm(prevW.getWord, postW.getWord)
    //    }

    println("统计分词信息")
    var pos = 0
    for (word <- words) {
      library.addTerm(word.getWord)
      for (i <- pos - 6 to pos - 1) {
        if (i >= 0 && word.getOffsetStart == words(i).getOffsetEnd) {
          //统计一个词的左邻字;
          library.addLeftTerm(word.getWord, words(i).getWord)
          library.addRigthTerm(words(i).getWord, word.getWord)
        }
      }
      pos += 1
    }

    //第二次分析,添加候选词;
    println("计算候选词")
    for (
      i <- 0 until words.length; postW = words(i);
      j <- i - 6 until i;
      if j >= 0;
      prevW = words(j);
      if prevW.getOffsetEnd == postW.getOffsetStart;
      nw = prevW.getWord + postW.getWord if !(library existsTerm nw)
    ) {
      candidate.addCandidateTerm(nw, (prevW.getWord, postW.getWord))
    }

    //计算分词的左右信息熵;用于评估结合自由度;
    println("计算分词的左右信息熵")
    library.calInfoEntropy()
    //  library.print()
    println("计算内部凝合度")
    candidate.filterNewTerm(library)
    //    candidate.print()
    println(candidate.result)

  }

}