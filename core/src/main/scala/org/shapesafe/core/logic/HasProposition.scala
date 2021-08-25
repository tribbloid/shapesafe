package org.shapesafe.core.logic

trait HasProposition {

  type OUB

  sealed trait PAuxSupport[P <: Proposition] {

    type Aux[O <: OUB] = P {

      type Repr = O
    }

    type Lt[+O <: OUB] = P {

      type Repr <: O
    }

    type ^[O <: OUB] <: Aux[O]
  }

  trait Proposition extends Serializable {

    type Repr <: OUB
  }
  case object Proposition extends PAuxSupport[Proposition] {

    abstract class ^[O <: OUB] extends Proposition {
      final type Repr = O
    }
  }

  case class Aye[O <: OUB](value: O) extends Proposition.^[O] {

    //    def upcast[_O >: O <: OUB]: Aye[_O] = this.asInstanceOf[Aye[_O]]
  }

  case class Nay[O <: OUB]() extends Proposition.^[O] {}

  case class Abstain[O <: OUB]() extends Proposition.^[O] {}
  // TODO: for Nay & Grey, add type-level reason

  case class Absurd[O <: OUB](
      aye: Aye[O],
      nay: Nay[O]
  ) extends Proposition.^[O] {}
}

object HasProposition {}
