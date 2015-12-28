package test.test1

object TestListUnique extends App {
  val l = List(1, 1, 1, 2, 3, 4, 3, 2, 1)
  println(l.count(_ == 1))
}