package org.shapesafe.core.logic

trait HasProposition {

  sealed trait PAuxSupport[P <: Proposition] {

    type Aux[O] = P {

      type Repr = O
    }

    type Lt[+O] = P {

      type Repr <: O
    }

    type ^[O] <: Aux[O]
  }

  trait Proposition extends Serializable {

    type Repr
  }
  case object Proposition extends PAuxSupport[Proposition] {

    abstract class ^[O] extends Proposition {
      final type Repr = O
    }
  }

  case class Aye[O](value: O) extends Proposition.^[O] {

    //    def upcast[_O >: O ]: Aye[_O] = this.asInstanceOf[Aye[_O]]
  }

  case class Nay[O]() extends Proposition.^[O] {}

  case class Abstain[O]() extends Proposition.^[O] {}
  // TODO: for Nay & Grey, add type-level reason

  case class Absurd[O](
      aye: Aye[O],
      nay: Nay[O]
  ) extends Proposition.^[O] {}
}

object HasProposition {}
