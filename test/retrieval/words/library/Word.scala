package retrieval.words.library

class Word( word : String, offset_start : Int, offset_end : Int ) {
  def getWord = word
  def getOffsetStart = offset_start
  def getOffsetEnd = offset_end
  override def toString() = "term:" + word + " offset_start:" + offset_start + " offset_end:" + offset_end
}