package org.shapesafe.core.shape

import org.shapesafe.core.tuple._
import shapeless.Witness

import scala.language.implicitConversions

trait Names extends IndicesLike with Names.proto.Impl {}

object Names extends TupleSystem with CanCons with CanFromStatic {

  object proto extends StaticTuples.Total[String] with CanInfix_>< {}

  type Impl = Names
  type UpperBound = proto.UpperBound

  class Eye extends proto.Eye with Names {
    override type Canonical = Indices.Eye

    override def canonical = Indices.Eye
  }
  lazy val Eye = new Eye

  class ><[
      TAIL <: Impl,
      HEAD <: UpperBound
  ](
      override val tail: TAIL,
      override val head: HEAD
  ) extends proto.><[TAIL, HEAD](tail, head)
      with Impl {

    val headW: Witness.Aux[HEAD] = Witness[HEAD](head).asInstanceOf[Witness.Aux[HEAD]]

    override type Canonical = Indices.><[tail.Canonical, Index.Name[HEAD]]

    override def canonical: Canonical =
      tail.canonical >< Index.Name[HEAD](headW)
  }

  implicit def consW[TAIL <: Impl, HEAD <: String]: Cons.FromFn2[TAIL, HEAD, TAIL >< HEAD] = {

    Cons.from[TAIL, HEAD].to { (tail, head) =>
      new ><(tail, head)
    }
  }

  implicit class Infix[SELF <: Impl](self: SELF) {

    def ><(name: Witness.Lt[String]): SELF >< name.T = {

      new ><(self, name.value)
    }
  }

  implicit def toEyeInfix(s: Names.type): Infix[Eye] = Infix(Eye)

  trait Syntax {

    implicit def literalToNames(v: String)(
        implicit
        w: Witness.Aux[v.type]
    ): Eye >< v.type = {

      Eye >< w
    }

    implicit def literalToInfix(v: String)(
        implicit
        w: Witness.Aux[v.type]
    ): Infix[Eye >< v.type] = {

      Infix(Eye >< w)
    }
  }

  object Syntax extends Syntax
}

//trait Names extends Names.Proto {
//
//  type Keys <: HList
//  def keys: Keys
//}
//
//object Names extends TupleSystem with CanCons with CanFromStatic {
//
//  override type UpperBound = String
//
//  val proto = Indices // every Names can be used in place of Indices
//  type Proto = proto.Impl
//
//  final type Impl = Names
//
//  class Eye extends proto.Eye with Names {
//
//    override type Keys = HNil
//    override def keys = HNil
//  }
//  override lazy val Eye = new Eye
//
//  class ><[
//      TAIL <: Impl,
//      HEAD <: String
//  ](
//      override val tail: TAIL,
//      val headW: Witness.Aux[HEAD]
//  ) extends proto.><[TAIL, Name[HEAD]](tail, new Name(headW))
//      with Names {
//
//    def headName: HEAD = headW.value
//
//    override type Keys = HEAD :: tail.Keys
//
//    override def keys: Keys = headW.value :: tail.keys
//  }
//
//  implicit def consW[TAIL <: Impl, HEAD <: String]: Cons.FromFn2[TAIL, HEAD, TAIL >< HEAD] = {
//
//    Cons.from[TAIL, HEAD].to { (tail, head) =>
//      new ><(tail, Witness[HEAD](head).asInstanceOf[Witness.Aux[HEAD]])
//    }
//  }
//
//  implicit class Infix[SELF <: Impl](self: SELF) {
//
//    def ><(name: Witness.Lt[String]): SELF >< name.T = {
//
//      new ><(self, name)
//    }
//  }
//
//  implicit def toEyeInfix(s: Names.type): Infix[s.Eye] = Infix(Eye)
//
//  trait Syntax {
//
//    implicit def literalToNames(v: String)(
//        implicit
//        w: Witness.Aux[v.type]
//    ): Eye >< v.type = {
//
//      Eye >< w
//    }
//
//    implicit def literalToInfix(v: String)(
//        implicit
//        w: Witness.Aux[v.type]
//    ): Infix[Eye >< v.type] = {
//
//      Infix(Eye >< w)
//    }
//  }
//
//  object Syntax extends Syntax
//}
