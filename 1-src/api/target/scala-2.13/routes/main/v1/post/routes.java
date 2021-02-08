// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/mark/Uni/Project/HelpMe/1-src/api/conf/posts.routes
// @DATE:Mon Feb 08 08:51:23 GMT 2021

package v1.post;

import posts.RoutesPrefix;

public class routes {
  
  public static final v1.post.ReversePostController PostController = new v1.post.ReversePostController(RoutesPrefix.byNamePrefix());

  public static class javascript {
    
    public static final v1.post.javascript.ReversePostController PostController = new v1.post.javascript.ReversePostController(RoutesPrefix.byNamePrefix());
  }

}
