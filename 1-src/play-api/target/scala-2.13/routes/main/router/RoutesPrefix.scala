// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/mark/Documents/project-docs/HelpMe/1-src/play-api/conf/routes
// @DATE:Tue Mar 16 10:11:41 GMT 2021


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
