package shapesafe.core.debugging

trait CanReason {

  val reasoning: Reasoning.Canonical

  final val theory: reasoning.theory.type = reasoning.theory
}
