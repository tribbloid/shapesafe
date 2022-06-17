package shapesafe.m.viz

import ai.acyclic.graph.commons.reflect.format.FormatOvrd
import ai.acyclic.graph.commons.reflect.format.Formats0.TypeInfo
import ai.acyclic.graph.commons.reflect.format.Formats1.{RecursiveForm, Trials}
import ai.acyclic.graph.commons.viz.TypeVizFormat

import scala.language.experimental.macros

case object PeekCT extends VizCTSystem {

  override lazy val format: TypeVizFormat = RecursiveForm(
    TypeInfo,
    { v =>
      Trials(
        FormatOvrd.SingletonName.DeAlias,
        v.HideStatic.DeAlias
      )
    }
  )

  override def useTree: Boolean = true

  implicit def infoOf[I]: Info[I] = macro VizCTSystem.Macros.infoOf[I, this.type]

  case object NoTree extends SubSystem {

    override def useTree: Boolean = false

    implicit def infoOf[I]: Info[I] = macro VizCTSystem.Macros.infoOf[I, this.type]
  }
}
