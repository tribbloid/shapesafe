package com.tribbloids.shapesafe.m.tuple

/**
  * TODO: Not sure if this thing is ever going to be useful, maybe HType1 => HList => mapped => HType2 is sufficient
  *
  * @tparam UBI Upper bound type of input system
  * @tparam UBO Upper bound type of output system
  */
trait TupleLambdaSystem[UBI, UBO] {

  val sysI: TupleSystem[UBI]
  val sysO: TupleSystem[UBO]

  // Implementations of HTypedSystem MUST declare type classes for this trait
  trait UnitLambda[
      -I <: UBI,
      +O <: UBO
  ] {

    def apply(in: I): O
  }

  trait ~~>[-IN <: sysI.Impl, +OUT <: sysO.Impl] {

    def apply(in: IN): OUT
  }

  object ~~> {

    implicit def eye2eye: sysI.Eye ~~> sysO.Eye = { _ =>
      sysO.Eye
    }

    implicit def recursive[
        TAIL_I <: sysI.Impl,
        HEAD_I <: UBI,
        TAIL_O <: sysO.Impl,
        HEAD_O <: UBO
    ](
        implicit
        forTail: TAIL_I ~~> TAIL_O,
        forHead: HEAD_I UnitLambda HEAD_O
    ): sysI.><[TAIL_I, HEAD_I] ~~> sysO.><[TAIL_O, HEAD_O] = { v =>
      val tailI = v.tail
      val headI = v.head

      new sysO.><(forTail(tailI), forHead(headI))
    }
  }
}
