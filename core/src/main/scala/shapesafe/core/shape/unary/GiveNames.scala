package shapesafe.core.shape.unary

import shapeless.HList
import shapeless.ops.hlist.ZipWithKeys
import shapesafe.core.debugging.{Notations, Reporters}
import shapesafe.core.shape.{LeafShape, Names, ShapeType, StaticShape}
import shapesafe.m.viz.VizCTSystem.EmitError

case class GiveNames[
    S1 <: ShapeType,
    N <: Names
](
    s1: S1 with ShapeType,
    newNames: N
) extends Conjecture1.^[S1] {

  override type Notation = Notations.:<<=[S1#Notation, N#Notation]

  override type _Refute = "Dimension mismatch"
}

trait GiveNames_Imp0 {

  import shapesafe.core.shape.ProveShape._

  implicit def refute[
      S1 <: ShapeType,
      P1 <: LeafShape,
      N <: Names,
      MSG
  ](
      implicit
      lemma: S1 |- P1,
      refute0: Reporters.ForShape.Refute0[GiveNames[P1, N], MSG],
      msg: EmitError[MSG]
  ): GiveNames[S1, N] |- LeafShape = {
    ???
  }
}

object GiveNames extends GiveNames_Imp0 {

  import shapesafe.core.shape.ProveShape._

  implicit def simplify[
      S1 <: ShapeType,
      P1 <: StaticShape,
      N <: Names,
      HO <: HList
  ](
      implicit
      lemma: S1 |- P1,
      zip: ZipWithKeys.Aux[N#Static, P1#_Dimensions#Static, HO],
      toShape: StaticShape.FromAxes.Case[HO]
  ): GiveNames[S1, N] |- toShape.Out = {
    forAll[GiveNames[S1, N]].=>> { src =>
      val keys: N#Static = src.newNames.static
      val p1: P1 = lemma.instanceFor(src.s1)

      val values: P1#_Dimensions#Static = p1.dimensions.static

      val zipped: HO = values.zipWithKeys(keys)(zip)
      StaticShape.FromAxes(zipped)
    }
  }

  // TODO: DEAD LOOP!
//  implicit def axiom[
//      P1 <: LeafShape,
//      N <: Names,
//      HO <: HList,
//      O <: LeafShape
//  ](
//      implicit
//      zip: ZipWithKeys.Aux[N#Keys, P1#_Dimensions#Static, HO],
//      toShape: LeafShape.FromRecord.==>[HO, O]
//  ): WithNames[P1, N] =>> O = {
//    from[WithNames[P1, N]].=>> { src =>
//      val keys: N#Keys = src.newNames.keys
//      val p1: P1 = src.s1
//
//      val values: P1#_Dimensions#Static = p1.dimensions.static
//
//      val zipped: HO = values.zipWithKeys(keys)
//      LeafShape.FromRecord(zipped)
//    }
//  }
//
//  implicit def theorem[
//      S1 <: Shape,
//      N <: Names,
//      P1 <: LeafShape,
//      O <: LeafShape
//  ](
//      implicit
//      lemma1: S1 ~~> P1,
//      lemma2: WithNames[P1, N] --> O
//  ): WithNames[S1, N] =>> O = {
//    from[WithNames[S1, N]].=>> { src =>
//      val p1: P1 = lemma1.valueOf(src.s1)
//
//      lemma2.valueOf(
//        src.copy(p1)
//      )
//    }
//  }
}
