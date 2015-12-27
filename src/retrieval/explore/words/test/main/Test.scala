package retrieval.explore.words.test.main

import scala.collection.mutable.Set
import scala.collection.mutable.HashMap
import scala.io.Source
import retrieval.explore.words.util.obtainterms.ObtainTermInfo
import retrieval.explore.words.struct.TermLibrary
import retrieval.explore.words.struct.NewTerm
import retrieval.explore.words.util.constval.ExploreConstVal

object Test {
  //list 2 tuple.
  def list2Tuple(originList : List[Any]) = {
    val newTuple = originList match {
      case List(a, b, c, d, e, f, g, _*) => (a, b, c, d, e, f, g)
      case List(a, b, c, d, e, f, _*)    => (a, b, c, d, e, f)
      case List(a, b, c, d, e, _*)       => (a, b, c, d, e)
      case List(a, b, c, d, _*)          => (a, b, c, d)
      case List(a, b, c, _*)             => (a, b, c)
      case List(a, b, _*)                => (a, b)
      case _                             => Nil
    }
    newTuple
  }

  def main(args : Array[String]) {
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
      //添加两个词的; 
      val singleLeft = library.getInvertedIndex(word.getOffsetStart).toList
      for (sl <- singleLeft; nw = sl + word.getWord) {
        if (!(library existsTerm nw)) {
          val t = (sl, word.getWord)

          candidate.addCandidateTerm(nw, t)
          candidate.addNewTermInvertedIndex(word.getOffsetEnd, t)
        }
      }

      //扩展到多个词;
      val multiLeft = candidate.getInvertedNewTerm(word.getOffsetStart).toList.filter { x => x.productIterator.size <= (ExploreConstVal.maxLength - 1) }
      for (ml <- multiLeft; originList = ml.productIterator.toList; nw = originList.mkString + word.getWord) {
        if (!(library existsTerm nw)) {
          val l = (word.getWord :: originList.reverse).reverse
          val t = list2Tuple(l)
          candidate.addCandidateTerm(nw, t)
          candidate.addNewTermInvertedIndex(word.getOffsetEnd, t)
        }
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