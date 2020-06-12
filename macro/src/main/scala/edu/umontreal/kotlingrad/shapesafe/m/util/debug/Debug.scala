package edu.umontreal.kotlingrad.shapesafe.m.util.debug

object Debug {

  //  def cartesianProductSet[T](xss: Seq[Set[T]]): Set[List[T]] = xss match {
  //    case Nil => Set(Nil)
  //    case h :: t =>
  //      for (xh <- h;
  //           xt <- cartesianProductSet(t))
  //        yield xh :: xt
  //  }
  //
  //  def cartesianProductList[T](xss: Seq[Seq[T]]): Seq[List[T]] = xss match {
  //    case Nil => List(Nil)
  //    case h :: t =>
  //      for (xh <- h;
  //           xt <- cartesianProductList(t))
  //        yield xh :: xt
  //  }

  private lazy val LZYCOMPUTE = "$lzycompute"
  private lazy val INIT = "<init>"

  def stackTracesShowStr(
      vs: Array[StackTraceElement],
      maxDepth: Int = 1
  ): String = {
    vs.slice(0, maxDepth)
      .mkString("\n\t< ")
  }

  private final val breakpointInfoBlacklist = {
    Seq(
      this.getClass.getCanonicalName,
      classOf[Thread].getCanonicalName
    ).map(_.stripSuffix("$"))
  }
  private def breakpointInfoFilter(vs: Array[StackTraceElement]) = {
    vs.filterNot { v =>
      val className = v.getClassName
      val outerClassName = className.split('$').head
      outerClassName.startsWith("scala") ||
      breakpointInfoBlacklist.contains(outerClassName)
    }
  }

  def getBreakpointInfo(
      filterInitializer: Boolean = true,
      filterLazyCompute: Boolean = true
  ): Array[StackTraceElement] = {
    val stackTraceElements: Array[StackTraceElement] = Thread.currentThread().getStackTrace
    var effectiveElements = breakpointInfoFilter(stackTraceElements)

    if (filterInitializer) effectiveElements = effectiveElements.filter(v => !(v.getMethodName == INIT))
    if (filterLazyCompute) effectiveElements = effectiveElements.filter(v => !v.getMethodName.endsWith(LZYCOMPUTE))

    effectiveElements
  }

  case class CallStackRef(
      depth: Int = 0,
      exclude: Seq[Class[_]] = Nil
  ) {

    val breakpointInfo: Array[StackTraceElement] = {
      val bp = getBreakpointInfo()
      val filteredIndex = bp.toSeq.indexWhere(
        { element =>
          !exclude.exists { v =>
            element.getClassName.startsWith(v.getCanonicalName)
          }
        },
        depth
      )
      bp.slice(filteredIndex, Int.MaxValue)
    }

    def showStr: String = {
      stackTracesShowStr(breakpointInfo)
    }

    def fnName: String = {
      val bp = breakpointInfo.head
      assert(!bp.isNativeMethod, "can only get fnName in def & lazy val blocks")
      bp.getMethodName
    }
  }

  def liftCamelCase(str: String): String = str.head.toUpper.toString + str.substring(1)
  def toCamelCase(str: String): String = str.head.toLower.toString + str.substring(1)

  def indent(text: String, str: String = "\t"): String = {
    text.split('\n').filter(_.nonEmpty).map(str + _).mkString("\n")
  }

}
