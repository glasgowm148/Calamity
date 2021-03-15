// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/mark/Documents/project-docs/play-api/conf/routes
// @DATE:Fri Mar 12 09:06:15 GMT 2021


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
