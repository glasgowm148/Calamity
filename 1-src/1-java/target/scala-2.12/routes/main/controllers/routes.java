// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/mark/Documents/GitHub/HelpMe/1-src/1-java/conf/routes
// @DATE:Mon Dec 07 23:04:04 GMT 2020

package controllers;

import router.RoutesPrefix;

public class routes {
  
  public static final controllers.ReverseTweetController TweetController = new controllers.ReverseTweetController(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseHomeController HomeController = new controllers.ReverseHomeController(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseAssets Assets = new controllers.ReverseAssets(RoutesPrefix.byNamePrefix());

  public static class javascript {
    
    public static final controllers.javascript.ReverseTweetController TweetController = new controllers.javascript.ReverseTweetController(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseHomeController HomeController = new controllers.javascript.ReverseHomeController(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseAssets Assets = new controllers.javascript.ReverseAssets(RoutesPrefix.byNamePrefix());
  }

}
