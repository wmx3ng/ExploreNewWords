

package test.test1

object TestTuple {
  def toTuple[A <: Object](as : List[A]) : Product = {
    val tupleClass = Class.forName("scala.Tuple" + as.size)
    tupleClass.getConstructors.apply(0).newInstance(as : _*).asInstanceOf[Product]
  }

  def main(args : Array[String]) {
    val l = List(1)
    val a = ("b")
    val t = l match {
      case List(a, b, c, d, e, _*) => (a, b, c, d, e)
      case List(a, b, c, d, _*)    => (a, b, c, d)
      case List(a, b, c, _*)       => (a, b, c)
      case List(a, b, _*)          => (a, b)
      case _                       => Nil
    }
    println(t)
  }
}