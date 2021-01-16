package com.tribbloids.shapesafe.m.shape

import com.tribbloids.shapesafe.m.arity.Expression
import com.tribbloids.shapesafe.m.tuple.{CanCrossAlways, CanInfix, CanonicalTupleSystem}

import scala.language.implicitConversions

object Dimensions extends CanonicalTupleSystem[Expression] with CanInfix with CanCrossAlways[Expression] {}
