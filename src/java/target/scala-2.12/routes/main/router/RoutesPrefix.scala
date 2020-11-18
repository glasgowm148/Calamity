// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/pseudo/Documents/GitHub/HelpMe/src/java/conf/routes
// @DATE:Wed Nov 18 09:05:46 GMT 2020


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
