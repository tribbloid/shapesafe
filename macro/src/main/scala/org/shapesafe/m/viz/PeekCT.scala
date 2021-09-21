package org.shapesafe.m.viz

import org.shapesafe.graph.commons.util.reflect.format.FormatOvrd
import org.shapesafe.graph.commons.util.reflect.format.Formats0.TypeInfo
import org.shapesafe.graph.commons.util.reflect.format.Formats1.{RecursiveForm, Trials}
import org.shapesafe.graph.commons.util.viz.TypeVizFormat

import scala.language.experimental.macros

case object PeekCT extends VizCTSystem {

  override lazy val format: TypeVizFormat = RecursiveForm(
    TypeInfo,
    { v =>
      Trials(
        FormatOvrd.Only.DeAlias,
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
