// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/mark/Documents/GitHub/HelpMe-clone/1-src/1-java/conf/routes
// @DATE:Wed Dec 16 15:55:46 GMT 2020


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
