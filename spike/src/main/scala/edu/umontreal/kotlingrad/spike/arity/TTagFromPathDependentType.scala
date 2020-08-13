//package edu.umontreal.kotlingrad.spike.arity
//
//import scala.reflect.ClassTag
//import scala.reflect.runtime.universe
//
//object TTagFromPathDependentType {
//
//  import universe._
//
//  def infer(): Unit = {
//    type U = (Int, String)
//
//    val ttg1 = implicitly[TypeTag[(Int, String)]]
//
//    val ttg2 = implicitly[TypeTag[U]]
//
//    val ctg = implicitly[ClassTag[U]]
//  }
//
//  def main(args: Array[String]): Unit = {}
//}
