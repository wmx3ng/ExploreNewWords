package retrieval.explore.words.util

object Test {
  def main(args : Array[String]) {
    val exValue = List(8, 11, 8, 6, 4)
    val sample = List(List(17, 25), List(50, 13, 13), List(12, 123), List(6, 36), List(29, 72, 6))
    for (i <- 0 until exValue.size) {
      println("----------")
      println(Util.evaluateWeight1(exValue(i), sample(i)) + " " + Util.evaluateWeight2(exValue(i), sample(i)) + " " + Util.evaluateWeight3(sample(i)) + " " + Util.evaluateWeight4(exValue(i), sample(i)))
    }
  }
}