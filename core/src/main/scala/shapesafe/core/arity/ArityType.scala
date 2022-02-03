package shapesafe.core.arity

import shapesafe.core.arity.Arity.^
import shapesafe.core.debugging.{CanPeek, HasSymbolTxt}

import scala.util.Try

trait ArityType extends CanPeek with HasSymbolTxt {

  def runtimeValue: Int
  final lazy val runtimeValueTry: Try[Int] = Try(runtimeValue)

  lazy val valueStr: String = runtimeValueTry
    .map(_.toString)
    .getOrElse("???")
//    .recover {
//      case ee: Exception =>
//        ee.getMessage
//    }
//    .get

  lazy val fullStr: String = {

    valueStr + ":" + this.getClass.getSimpleName
  }

  final override def toString: String = fullStr
}

object ArityType {

  trait Verifiable extends ArityType {}

  implicit class converters[A <: ArityType](self: A) {

    def ^ : ^[A] = Arity.^(self)
  }
}
