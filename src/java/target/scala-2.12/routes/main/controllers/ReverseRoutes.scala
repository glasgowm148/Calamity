// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/pseudo/Documents/GitHub/HelpMe/src/java/conf/routes
// @DATE:Wed Nov 18 09:05:46 GMT 2020

import play.api.mvc.Call


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:6
package controllers {

  // @LINE:12
  class ReverseTweetController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:12
    def list(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "vec")
    }
  
  }

  // @LINE:6
  class ReverseHomeController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:7
    def increment(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "increment")
    }
  
    // @LINE:6
    def index(): Call = {
      
      Call("GET", _prefix)
    }
  
  }

  // @LINE:10
  class ReverseAssets(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:10
    def versioned(file:Asset): Call = {
      implicit lazy val _rrc = new play.core.routing.ReverseRouteContext(Map(("path", "/public"))); _rrc
      Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[play.api.mvc.PathBindable[Asset]].unbind("file", file))
    }
  
  }


}
