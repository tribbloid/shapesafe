//package org.shapesafe.core
//
//import org.shapesafe.core.arity.ArityAPI
//import org.shapesafe.core.debugging.{CanPeek, Reporters}
//
//trait ProofAPI {
//
//  val scope: ProofScope
//  import scope._
//
//  final type VerifyTo = scope.OUB with CanPeek
//  type EvalTo <: VerifyTo
//
//  object _Reporters extends Reporters[scope.type](scope)
//
//  object PeekReporter extends _Reporters.PeekReporter[VerifyTo, EvalTo]
//  object InterruptReporter extends _Reporters.InterruptReporter[VerifyTo, EvalTo]
//
//  trait APIProto {
//
//    type _Out <: VerifyTo
//    def out: _Out
//
//    def verify[
//        O <: VerifyTo
//    ](
//        implicit
//        prove: _Out |- O
//    ): ArityAPI.^[O] = prove.apply(out).value.^
//
//    def eval[
//        O <: EvalTo
//    ](
//        implicit
//        prove: _Out |- O
//    ): ArityAPI.^[O] = verify(prove)
//
//    def peek(
//        implicit
//        reporter: PeekReporter.Case[_Out]
//    ): this.type = this
//
//    def interrupt(
//        implicit
//        reporter: InterruptReporter.Case[_Out]
//    ): this.type = this
//
//    def reason[
//        O <: EvalTo
//    ](
//        implicit
//        reporter: PeekReporter.Case[_Out],
//        prove: _Out |- O
//    ): ArityAPI.^[O] = verify(prove)
//  }
//
//  type Aux[O <: VerifyTo] = APIProto {}
//
//  def create[O <: VerifyTo](v: O):
//}
