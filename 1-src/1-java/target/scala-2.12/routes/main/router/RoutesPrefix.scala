// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/mark/HelpMe-clone/1-src/1-java/conf/routes
// @DATE:Sun Dec 20 17:59:21 GMT 2020


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
