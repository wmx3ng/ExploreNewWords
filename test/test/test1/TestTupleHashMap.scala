package test.test1

object TestTupleHashMap {
  def main(args : Array[String]) {
    var hm = collection.mutable.HashMap[Product, Int]()
    hm += ((1, 2) -> 3)
    hm += ((1, 2, 3, 4, 5) -> 5)
    val tuple = (1, 3)
    if (hm contains tuple) {
      println(true)
    }
    //    val hm = collection.mutable.HashMap((1, 2) -> 3, (2, 3) -> 4, (3, 4, 5) -> 5)
    for (h <- hm) {
      println("----------")
      println(h._1.productIterator.size)
      h._1.productIterator.foreach { x => println(x.toString()) }
    }
  }
}