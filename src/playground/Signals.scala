package playground

import RichProduct._ 

object Signals {
    def WeightString(s: String): Double = s map (_.toByte.toDouble) reduceRight {(a, b) => a + b / 0xFF} 
    
    def prod2doubles(p: Product): List[Double] = {
      val flat = p.getClass :: p.toList
      flat map { x: Any => x match {
        case c: Class[_] => c.hashCode.toDouble / Int.MaxValue.toDouble
        case i: Int => i.toDouble / Int.MaxValue
        case s: String => WeightString(s)
        case undefined => 0.0
      }} 
    } 
}
