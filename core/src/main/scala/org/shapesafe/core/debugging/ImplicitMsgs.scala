//package org.shapesafe.core.debugging
//
//import org.shapesafe.core.debugging.InfoCT.ErrorMsg
//
//object ImplicitMsgs {
//
//  trait Proto_Imp1 {
//
//    type ReportMsg[T] = EmitError[T]
//
//    trait Case {
//      type Self
//      type Msg
//
//      def self: Self
//    }
//
//    object Case {
//
//      class Impl[T, MSG](val self: T) extends Case {
//        override type Self = T
//        override type Msg = MSG
//      }
//
//      type Aux[T, MSG] = Case {
//        type Self = T
//        type Msg = MSG
//      }
//
//      type Lt[T, MSG] = Case {
//        type Self <: T
//        type Msg = MSG
//      }
//    }
//
////    implicit def found2[T, MSG]: Case.Impl[T, MSG] = ???
//  }
//
//  trait Proto_Imp0 extends Proto_Imp1 {
//
//    implicit def notFound[T, MSG](
//        implicit
//        report: ReportMsg[MSG]
//    ): Case.Impl[T, MSG] = {
//      ???
//    }
//  }
//
//  trait Proto extends Proto_Imp0 {
//
//    implicit def found[T, MSG](
//        implicit
//        self: T
//    ): Case.Impl[T, MSG] = new Case.Impl[T, MSG](self)
//  }
//
//  object ErrorIfNotFound extends Proto {}
//  type ErrorIfNotFound[T, MSG] = ErrorIfNotFound.Case.Lt[T, MSG]
//
////  object WarnIfNotFound extends Proto_Imp0 {
////
////    override type ReportMsg[T] = RequireMsgSym[FALSE.T, T, Warn]
////  }
//}
