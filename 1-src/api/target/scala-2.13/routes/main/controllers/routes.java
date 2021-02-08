// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/mark/Uni/Project/HelpMe/1-src/api/conf/routes
// @DATE:Mon Feb 08 08:51:23 GMT 2021

package controllers;

import router.RoutesPrefix;

public class routes {
  
  public static final controllers.ReverseHomeController HomeController = new controllers.ReverseHomeController(RoutesPrefix.byNamePrefix());

  public static class javascript {
    
    public static final controllers.javascript.ReverseHomeController HomeController = new controllers.javascript.ReverseHomeController(RoutesPrefix.byNamePrefix());
  }

}
