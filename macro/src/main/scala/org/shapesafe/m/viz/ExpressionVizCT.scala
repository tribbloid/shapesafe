package org.shapesafe.m.viz
import org.shapesafe.graph.commons.util.reflect.format.{FormatOvrd, FormatProtos, Formats}
import org.shapesafe.graph.commons.util.viz.TypeVizFormat

import scala.language.experimental.macros

case object ExpressionVizCT extends VizCTSystem {

  override def format: TypeVizFormat = FormatProtos.TransformText(
    FormatProtos.Trials(
      FormatOvrd.Only,
      Formats.TypeInfo.DeAlias.HideStatic.recursively
    )
  )

  override def useTree: Boolean = true

  implicit def infoOf[I]: InfoOf[I] = macro VizCTSystem.Macros.infoOf[I, this.type]

  case object NoTree extends Updated {

    override def useTree: Boolean = false

    implicit def infoOf[I]: InfoOf[I] = macro VizCTSystem.Macros.infoOf[I, this.type]
  }
}
