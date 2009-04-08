package playground  
  
object RichProduct {
    implicit def productRicher(p: Product) = new {
      def toSeq[T]: Seq[T] = 0 until p.productArity map { el => p.productElement(el).asInstanceOf[T] }  
      def toList[T]: List[T] = toSeq.toList 
    } 
}
