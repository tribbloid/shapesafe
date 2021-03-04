package org.shapesafe.core.shape

import org.shapesafe.core.arity.Arity
import org.shapesafe.core.tuple.{CanInfix_><, StaticTuples}

import scala.language.implicitConversions

object Dimensions extends StaticTuples.Total[Arity] with CanInfix_>< {}
