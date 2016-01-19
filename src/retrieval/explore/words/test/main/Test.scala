package retrieval.explore.words.test.main

import scala.collection.mutable.Set
import scala.collection.mutable.HashMap
import scala.io.Source
import retrieval.explore.words.util.obtainterms.ObtainTermInfo
import retrieval.explore.words.struct.TermLibrary
import retrieval.explore.words.struct.NewTerm
import retrieval.explore.words.util.constval.ExploreConstVal
import org.nlpcn.commons.lang.occurrence.Count
import retrieval.explore.words.util.Util

object Test {
  def main(args : Array[String]) {
    val path = "yuliao/ssw"

    //从文件中读取文本;
    val lines = Source.fromFile(new java.io.File(path)).getLines().toList.mkString
    //    val lines = "加入特别提款权"
    //文本分词统计结果;
    val library = new TermLibrary
    //候选词集合;
    val candidate = new NewTerm

    //文本分词链;
    println("分词...")
    //分词结果;
    val words = ObtainTermInfo.obtainTerms(lines)
    println("建立词位置的倒排索引...")
    //建立词位置的倒排索引;offsetEnd->word;
    for (word <- words) {
      library.addInvertedIndex(word.getOffsetEnd, word.getWord)
    }

    println("统计分词信息...")
    for (word <- words) {
      library.addTerm(word.getWord)
      //拉出与该词相邻的左边的词;
      val left = library.getInvertedIndex(word.getOffsetStart).toList
      for (w <- left) {
        library.addLeftTerm(word.getWord, w)
        library.addRigthTerm(w, word.getWord)
      }
    }

    //第二次分析,添加候选词;扩展到多个词(5)
    println("计算候选词...")
    for (word <- words) {
      //添加两个词的; 拉出与该词相邻的左边的词集合;
      val singleLeft = library.getInvertedIndex(word.getOffsetStart).toList

      for (sl <- singleLeft; nw = sl + word.getWord) {
        //该词是否为分词产生的词;若是,则不处理; 
        if (!(library existsTerm nw)) {
          val t = (sl, word.getWord)

          candidate.addCandidateTerm(nw, t)
          candidate.addNewTermInvertedIndex(word.getOffsetEnd, t)
          candidate.addDupNewTerm(nw, word.getOffsetEnd)
        }
      }

      //扩展到多个词;
      val multiLeft = candidate.getInvertedNewTerm(word.getOffsetStart).toList.filter { x => x.productIterator.size <= (ExploreConstVal.maxLength - 1) }

      for (ml <- multiLeft; originList = ml.productIterator.toList; nw = originList.mkString + word.getWord) {
        //生成的词不是分词产生的词;
        if (!(library existsTerm nw)) {
          val l = (word.getWord :: originList.reverse).reverse
          val t = Util.list2Tuple(l)
          candidate.addCandidateTerm(nw, t)
          candidate.addNewTermInvertedIndex(word.getOffsetEnd, t)
          candidate.addDupNewTerm(nw, word.getOffsetEnd)
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