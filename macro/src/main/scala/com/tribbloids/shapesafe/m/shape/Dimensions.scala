package com.tribbloids.shapesafe.m.shape

import com.tribbloids.shapesafe.m.arity.Expression
import com.tribbloids.shapesafe.m.tuple.{CanInfix, StaticTuples}

import scala.language.implicitConversions

object Dimensions extends StaticTuples.Total[Expression] with CanInfix {}
