package shapesafe.m

import scala.language.experimental.macros
import scala.reflect.macros.whitebox

/**
  * a.k.a. non-singleton tightest upper bound
  * @tparam I input type
  */
trait PeerType[I] {

  final type In = I
  type Out
}

object PeerType {

  lazy val objectClassPath: String = {
    val fullName = this.getClass.getCanonicalName.stripSuffix("$")
    s"_root_.$fullName"
  }

  type Aux[A, Out0] = PeerType[A] {
    type Out = Out0
  }

  case class Make[I, O]() extends PeerType[I] {
    final type Out = O
  }

  def make[I, O]: Make[I, O] = {

    Make[I, O]()
  }

  def apply[I]: PeerType[I] = macro Macros.apply[I]

  final class Macros(val c: whitebox.Context) extends MWithReflection {

    def outer = PeerType.this

    import c.universe._

    def apply[A: WeakTypeTag]: Tree = {

      val tt: Type = weakTypeOf[A]
      val _tt = tt.dealias

      val out: Type = _tt match {

        case v: SingletonType =>
          val ttView = refl.typeView(v)
          val baseTypes = ttView.baseTypes.map(_.self)

          val chosen = baseTypes.flatMap {
            case NoType => None
            case v @ _ => Some(v)
          }.head

          chosen

        case v @ _ =>
          v
      }

      q"_root_.shapesafe.m.PeerType.make[$tt, $out]" // TODO: make the object Liftable
    }
  }
}
