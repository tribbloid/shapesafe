package com.tribbloids.shapesafe.m

import com.tribbloids.shapesafe.m.arity.Expression

trait Proof extends Serializable {

  def self: Expression

  type Out

  def out: Out
}
