package org.shapesafe.m.viz

import org.shapesafe.graph.commons.util.reflect.format.{Formats0, TypeFormat}
import org.shapesafe.graph.commons.util.viz.TypeVizFormat

import scala.language.experimental.macros
import scala.reflect.macros.whitebox

case object TypeVizCT extends VizCTSystem {

  override def format: TypeVizFormat = TypeFormat.Default
  override def useTree: Boolean = true

  implicit def infoOf[I]: InfoOf[I] = macro VizCTSystem.Macros.infoOf[I, this.type]

//  case object Stub extends VizCTSystem {
//
//    override def vizFormat: TypeVizFormat = Formats.TypeInfo.Short
//    override def useTree: Boolean = false
//
//    implicit def infoOf[I]: InfoOf[I] = macro VizCTSystem.Macros.infoOf[I, this.type]
//  }
}
