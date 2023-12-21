package shapesafe.core.debugging

import shapesafe.core.AdHocPoly1
import shapesafe.core.debugging.DebugConst.Stripe
import shapesafe.core.debugging.Reporters.PeekCTAux
import shapesafe.m.Emit
import singleton.ops.+

trait Refutes {

  type WHEN_PROVING

  type Refute0[SELF <: CanRefute, O] = Refute0.Auxs.=>>[SELF, O]

  object Refute0 extends AdHocPoly1 {

    implicit def get[I <: CanRefute, V <: String](
        implicit
        expr2Str: PeekCTAux[I#Notation, V]
    ): I =>> (
      I#RefuteTxt +
        WHEN_PROVING +
        V
    ) = forAll[I].=>> { _ =>
      null
    }
  }

  class NotFoundInfo[T, R <: CanRefute]

  trait NotFoundInfo_Imp0 {

    implicit def refute[
        T,
        R <: CanRefute,
        MSG
    ](
        implicit
        refute0: Refute0[R, MSG],
        msg: Emit.Error[MSG]
    ): NotFoundInfo[T, R] =
      ???
  }

  object NotFoundInfo extends NotFoundInfo_Imp0 {

    implicit def prove[
        T,
        R <: CanRefute
    ](
        implicit
        iff: T
    ): NotFoundInfo[T, R] = {
      new NotFoundInfo[T, R]
    }
  }
}

object Refutes {

  object ForArity extends Refutes {

    type WHEN_PROVING = "\n\n" + Stripe["... when proving arity"]
  }

  object ForShape extends Refutes {

    type WHEN_PROVING = "\n\n" + Stripe["... when proving shape"]
  }
}
