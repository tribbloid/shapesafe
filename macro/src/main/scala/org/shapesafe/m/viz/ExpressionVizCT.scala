package org.shapesafe.m.viz

import org.shapesafe.graph.commons.util.reflect.format.{FormatOvrd, Formats0, Formats1}
import org.shapesafe.graph.commons.util.viz.TypeVizFormat

import scala.language.experimental.macros

case object ExpressionVizCT extends VizCTSystem {

  override def format: TypeVizFormat = Formats1.TransformText(
    Formats1.Trials(
      FormatOvrd.Only,
      Formats0.TypeInfo.DeAlias.HideStatic.recursively
    )
  )

  override def useTree: Boolean = true

  implicit def infoOf[I]: InfoOf[I] = macro VizCTSystem.Macros.infoOf[I, this.type]

  case object NoTree extends Updated {

    override def useTree: Boolean = false

    implicit def infoOf[I]: InfoOf[I] = macro VizCTSystem.Macros.infoOf[I, this.type]
  }
}
