// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/mark/Uni/Project/HelpMe/1-src/api/conf/posts.routes
// @DATE:Mon Feb 08 08:51:23 GMT 2021

package posts

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._

import play.api.mvc._

import _root_.controllers.Assets.Asset
import _root_.play.libs.F

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:2
  PostController_0: v1.post.PostController,
  val prefix: String
) extends GeneratedRouter {

   @javax.inject.Inject()
   def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:2
    PostController_0: v1.post.PostController
  ) = this(errorHandler, PostController_0, "/")

  def withPrefix(addPrefix: String): Routes = {
    val prefix = play.api.routing.Router.concatPrefix(addPrefix, this.prefix)
    posts.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, PostController_0, prefix)
  }

  private[this] val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""GET""", this.prefix, """v1.post.PostController.list(request:Request)"""),
    ("""POST""", this.prefix, """v1.post.PostController.create(request:Request)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """""" + "$" + """id<[^/]+>""", """v1.post.PostController.show(request:Request, id:String)"""),
    ("""PUT""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """""" + "$" + """id<[^/]+>""", """v1.post.PostController.update(request:Request, id:String)"""),
    Nil
  ).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
    case l => s ++ l.asInstanceOf[List[(String,String,String)]]
  }}


  // @LINE:2
  private[this] lazy val v1_post_PostController_list0_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix)))
  )
  private[this] lazy val v1_post_PostController_list0_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      PostController_0.list(fakeValue[play.mvc.Http.Request]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "posts",
      "v1.post.PostController",
      "list",
      Seq(classOf[play.mvc.Http.Request]),
      "GET",
      this.prefix + """""",
      """""",
      Seq()
    )
  )

  // @LINE:3
  private[this] lazy val v1_post_PostController_create1_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix)))
  )
  private[this] lazy val v1_post_PostController_create1_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      PostController_0.create(fakeValue[play.mvc.Http.Request]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "posts",
      "v1.post.PostController",
      "create",
      Seq(classOf[play.mvc.Http.Request]),
      "POST",
      this.prefix + """""",
      """""",
      Seq()
    )
  )

  // @LINE:5
  private[this] lazy val v1_post_PostController_show2_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val v1_post_PostController_show2_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      PostController_0.show(fakeValue[play.mvc.Http.Request], fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "posts",
      "v1.post.PostController",
      "show",
      Seq(classOf[play.mvc.Http.Request], classOf[String]),
      "GET",
      this.prefix + """""" + "$" + """id<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:6
  private[this] lazy val v1_post_PostController_update3_route = Route("PUT",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val v1_post_PostController_update3_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      PostController_0.update(fakeValue[play.mvc.Http.Request], fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "posts",
      "v1.post.PostController",
      "update",
      Seq(classOf[play.mvc.Http.Request], classOf[String]),
      "PUT",
      this.prefix + """""" + "$" + """id<[^/]+>""",
      """""",
      Seq()
    )
  )


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:2
    case v1_post_PostController_list0_route(params@_) =>
      call { 
        v1_post_PostController_list0_invoker.call(
          req => PostController_0.list(req))
      }
  
    // @LINE:3
    case v1_post_PostController_create1_route(params@_) =>
      call { 
        v1_post_PostController_create1_invoker.call(
          req => PostController_0.create(req))
      }
  
    // @LINE:5
    case v1_post_PostController_show2_route(params@_) =>
      call(params.fromPath[String]("id", None)) { (id) =>
        v1_post_PostController_show2_invoker.call(
          req => PostController_0.show(req, id))
      }
  
    // @LINE:6
    case v1_post_PostController_update3_route(params@_) =>
      call(params.fromPath[String]("id", None)) { (id) =>
        v1_post_PostController_update3_invoker.call(
          req => PostController_0.update(req, id))
      }
  }
}
