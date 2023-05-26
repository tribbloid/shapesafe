package shapesafe.m.viz

import ai.acyclic.prover.commons.meta2.format.{Formats0, TypeFormat}

import scala.language.experimental.macros

case object TypeVizCT extends VizCTSystem {

  override lazy val typeFormat: TypeFormat =
    TypeFormat.Default

  override def useTree: Boolean = true

  implicit def infoOf[I]: Info[I] = macro VizCTSystem.Macros.infoOf[I, this.type]

  case object Short extends SubSystem {

    override lazy val typeFormat: TypeFormat = Formats0.TypeInfo.HidePackage.recursively.DeAlias

    implicit def infoOf[I]: Info[I] = macro VizCTSystem.Macros.infoOf[I, this.type]
  }
}
