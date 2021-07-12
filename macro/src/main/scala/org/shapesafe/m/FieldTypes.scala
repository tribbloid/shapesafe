//package org.shapesafe.m
//
//import shapeless.ops.hlist.Mapper
//import shapeless.{Generic, HList, Poly1, Typeable}
//import scala.language.experimental.macros
//import scala.reflect.macros.whitebox
//
//// TODO: USELESS! remove
//trait FieldTypes[A <: Product] {
//  type Out <: HList
//}
//
//object FieldTypes {
//
//  case class Impl[A <: Product, O <: HList]() extends FieldTypes[A] {
//
//    type Out = O
//  }
//
//  type Aux[A <: Product, Out0 <: HList] = FieldTypes[A] { type Out = Out0 }
//
//  implicit def mkFieldTypes[A <: Product, L <: HList](
//      implicit
//      generic: Generic.Aux[A, L],
//      mapper: Mapper[typeablePoly.type, L]
//  ) = Impl[A, mapper.Out]()
//
//  object typeablePoly extends Poly1 {
//
//    implicit def cse[A](
//        implicit
//        typeable: Typeable[A]
//    ): Case[A] = macro cseImpl[A]
//
//    def cseImpl[A: c.WeakTypeTag](c: whitebox.Context)(typeable: c.Tree): c.Tree = {
//      import c.universe._
//      val tpA = weakTypeOf[A]
//
//      val describe = c.untypecheck(q"$typeable.describe")
//
//      println(describe)
//      val str = c.eval(
//        c.Expr[String](describe)
//      )
//      q"null.asInstanceOf[FieldTypes.typeablePoly.Case.Aux[$tpA, _root_.shapeless.labelled.FieldType[$str, $tpA]]]"
//    }
//  }
//}
