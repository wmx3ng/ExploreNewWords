package retrieval.explore.words.test.main

import scala.collection.mutable.Set
import scala.collection.mutable.HashMap
import scala.io.Source
import retrieval.explore.words.util.obtainterms.ObtainTermInfo
import retrieval.explore.words.struct.TermLibrary
import retrieval.explore.words.struct.NewTerm

object Test {

  def main(args : Array[String]) {
    //  val text = "清华大学化学实验室"

    //文件路径;
    //    val path = "/home/wang/sg"
    //    val path = "/home/wang/Documents/ShareFolder/sgyy2.txt"
    //    val path = "/home/wang/Documents/ShareFolder/xjp"
    val path = "yuliao/sdmz"

    //从文件中读取文本;
    val lines = Source.fromFile(new java.io.File(path)).getLines().toList.mkString

    //文本分词统计结果;
    val library = new TermLibrary
    //候选词集合;
    val candidate = new NewTerm

    //文本分词链;
    println("分词...")
    val words = ObtainTermInfo.obtainTerms(lines)
    println("建立词位置的倒排索引...")
    //建立词位置的倒排索引;
    for (word <- words) {
      library.addInvertedIndex(word.getOffsetEnd, word.getWord)
    }
    //文本长度;
    library.textLength = lines.length()

    println("统计分词信息...")
    for (word <- words) {
      library.addTerm(word.getWord)
      val left = library.getInvertedIndex(word.getOffsetStart).toList
      for (w <- left) {
        library.addLeftTerm(word.getWord, w)
        library.addRigthTerm(w, word.getWord)
      }
    }

    //第二次分析,添加候选词;扩展到多个词(5)
    println("计算候选词...")
    for (word <- words) {
      val left = library.getInvertedIndex(word.getOffsetStart).toList
      for (w <- left; nw = w + word.getWord if !(library existsTerm nw)) {
        candidate.addCandidateTerm(nw, (w, word.getWord))
      }
    }

    //计算分词的左右信息熵;用于评估结合自由度;
    println("计算分词的左右信息熵...")
    library.calInfoEntropy()
    //  library.print()
    println("计算内部凝合度...")
    candidate.filterNewTerm(library)
    //    candidate.print()
    println(candidate.result(library))
    //    println(library.getTopKTerms(300))
  }

}