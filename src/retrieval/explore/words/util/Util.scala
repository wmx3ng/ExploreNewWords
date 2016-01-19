package retrieval.explore.words.util

object Util {
  //得到重复数;
  def getDupCnt(multiLeft : List[Product]) = {
    val nws = multiLeft.map { x => x.productIterator.mkString }
    nws.size - nws.distinct.size
  }

  //list 2 tuple.
  def list2Tuple(originList : List[Any]) = {
    val newTuple = originList match {
      case List(a, b, c, d, e, f, g, h, i, j, _*) => (a, b, c, d, e, f, g, h, i, j)
      case List(a, b, c, d, e, f, g, h, i, _*)    => (a, b, c, d, e, f, g, h, i)
      case List(a, b, c, d, e, f, g, h, _*)       => (a, b, c, d, e, f, g, h)
      case List(a, b, c, d, e, f, g, _*)          => (a, b, c, d, e, f, g)
      case List(a, b, c, d, e, f, _*)             => (a, b, c, d, e, f)
      case List(a, b, c, d, e, _*)                => (a, b, c, d, e)
      case List(a, b, c, d, _*)                   => (a, b, c, d)
      case List(a, b, c, _*)                      => (a, b, c)
      case List(a, b, _*)                         => (a, b)
      case _                                      => Nil
    }
    newTuple
  }

  private def calStandardDeviation(avg : Double, sample : List[Double]) = {
    val tmp = sample./:(0D)((sum, s) => sum + math.pow(s - avg, 2))
    math.sqrt(tmp / (sample.size - 1))
  }

  //得分评估1;将新词出现的次数c0与其组成因子(t1,t2,t3...)出现在次数放在一起,求标准差;
  def evaluateWeight1(c0 : Int, c_l : List[Int]) = {
    val tmp = c0.toDouble :: c_l.map { x => x.toDouble }
    calStandardDeviation(tmp.sum / tmp.size, tmp)
  }

  //得分评估2;将新词出现的次数c0做为均值与其组成因子(t1,t2,t3...)出现在次数放在一起,求标准差;
  def evaluateWeight2(c0 : Int, c_l : List[Int]) = {
    val tmp = c_l.map { x => x.toDouble }
    calStandardDeviation(c0.toDouble, tmp)
  }

  //得分评估3;将新词组成因子(t1,t2,t3...)求标准差;
  def evaluateWeight3(c_l : List[Int]) = {
    val tmp = c_l.map { x => x.toDouble }
    calStandardDeviation(tmp.sum / tmp.size, tmp)
  }

  //得分评估4;将新词出现的次数c0做为分母与其组成因子(t1,t2,t3...)组成概率,求标准差;
  def evaluateWeight4(c0 : Int, c_l : List[Int]) = {
    val tmp = c_l.map { x => c0.toDouble / x.toDouble }
    calStandardDeviation(tmp.sum / tmp.size, tmp)
  }

}