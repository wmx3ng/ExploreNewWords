package retrieval.words.library

/**
 * 分词器分词的信息;包括在单篇文档中词串,位置信息;位置信息用于判断是否相邻.
 */
class TermInfo(word : String, offset_start : Int, offset_end : Int) {
  def getWord = word
  def getOffsetStart = offset_start
  def getOffsetEnd = offset_end
  override def toString() = "term:" + word + " offset_start:" + offset_start + " offset_end:" + offset_end
}