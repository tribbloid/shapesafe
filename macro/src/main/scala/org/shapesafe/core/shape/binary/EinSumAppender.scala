package org.shapesafe.core.shape.binary

import org.shapesafe.core.arity.binary.AssertEqual
import org.shapesafe.core.arity.ops.ArityOps.=!=
import org.shapesafe.core.arity.{Arity, LeafArity}
import org.shapesafe.core.axis.Axis.->>
import shapeless.ops.record.{Keys, Selector}
import shapeless.{::, HList, NotContainsConstraint, Witness}

trait EinSumAppender_Imp0 extends FieldAppender {
  // TODO: should be a Poly2?
  // TODO: merge into EinSumIndexed.Cons

  import org.shapesafe.core.arity.ProveArity._

  implicit def ifOldName[
      OLD <: HList,
      N <: String,
      A1 <: Arity,
      A2 <: Arity,
      O <: LeafArity
  ](
      implicit
      name: Witness.Aux[N],
      selector: Selector.Aux[OLD, N, A1],
      lemma: A1 =!= A2 =>>^^ Proof.Aux[O]
  ): ==>[(OLD, N ->> A2), (N ->> O) :: OLD] = {

    from[(OLD, N ->> A2)].==> {

      case (old, field) =>
        import shapeless.record._

        val d1 = old.apply(name)
        val d2 = field

        val d_new: O = lemma.apply(AssertEqual(d1, d2)).out

        d_new.asInstanceOf[N ->> O] :: old
    }
  }
}

trait EinSumAppender extends EinSumAppender_Imp0 with DistinctAppender {}
object EinSumAppender extends EinSumAppender
