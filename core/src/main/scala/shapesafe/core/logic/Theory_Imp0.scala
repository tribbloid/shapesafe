package shapesafe.core.logic

import shapesafe.m.Emit

trait Theory_Imp0 {
  self: Theory =>

  implicit def cannot_|-[I, O](
      implicit
      emit: Emit.Error["[CANNOT PROVE]: ${I} |- ${O}"]
  ): |-[I, O] = ???

  implicit def cannot_|-\-[I, O](
      implicit
      emit: Emit.Error["[CANNOT REFUTE]: ${I} |-\\- ${O}"]
  ): |-\-[I, O] = ???
}
