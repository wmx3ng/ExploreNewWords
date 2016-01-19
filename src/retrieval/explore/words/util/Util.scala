package retrieval.explore.words.util

object Util {

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