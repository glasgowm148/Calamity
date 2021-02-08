// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/mark/Uni/Project/HelpMe/1-src/api/conf/routes
// @DATE:Mon Feb 08 08:51:23 GMT 2021


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
