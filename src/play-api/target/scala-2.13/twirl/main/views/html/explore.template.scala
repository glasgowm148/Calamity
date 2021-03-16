
package views.html

import _root_.play.twirl.api.TwirlFeatureImports._
import _root_.play.twirl.api.TwirlHelperImports._
import _root_.play.twirl.api.Html
import _root_.play.twirl.api.JavaScript
import _root_.play.twirl.api.Txt
import _root_.play.twirl.api.Xml
import models._
import controllers._
import play.api.i18n._
import views.html._
import play.api.templates.PlayMagic._
import java.lang._
import java.util._
import play.core.j.PlayMagicForJava._
import play.mvc._
import play.api.data.Field
import play.data._
import play.core.j.PlayFormsMagicForJava._
import scala.jdk.CollectionConverters._

object explore extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template0[play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/():play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.4*/("""

"""),_display_(/*3.2*/main("Hello World")/*3.21*/ {_display_(Seq[Any](format.raw/*3.23*/("""
    """),_display_(/*4.6*/defining(play.core.PlayVersion.current)/*4.45*/ { version =>_display_(Seq[Any](format.raw/*4.58*/("""

        """),format.raw/*6.9*/("""<section id="content">
            <div class="wrapper doc">
                <article>
                    <h2>Play application overview</h2>

                    <p>This tutorial is implemented as a Play application that demonstrates Play's basics. We started with the Play
                        Java seed template, which set up the application project structure and the configuration to build with sbt.
                        We added stylesheets with Play's colors and a Table of Contents.</p>
                    <p>Let's start by looking at what happens at runtime. When you entered the server name and port number, <a target="play-docs"
                    href="http://localhost:9000/">http://localhost:9000/</a>, in your browser:</p>
                    <ul>
                        <li>The browser requested the root <code>/</code> URI from the HTTP server using the <code>GET</code>
                            method.</li>
                        <li>The Play internal HTTP Server received the request.</li>
                        <li>Play resolved the request using the <code>routes</code>
                            file, which maps URIs to controller action methods.</li>
                        <li>The action method used Twirl templates to render the <code>index</code> page.</li>
                        <li>The HTTP server returned the response as an HTML page.</li>
                    </ul>
                    <p> At a high level, the flow looks something like this:</p>
                    <p><img src="assets/images/play-request-response.png" alt="Request and response" class="small-5 medium-4 large-3" /></p>
                    <h3>Explore the project</h3>
                    <p>Next, let's look at the tutorial project to locate the implementation for the following:</p>
                    <ul>
                        <li>The controller action method that defines how to handle a request to the root URI.</li>
                        <li>The <code>conf/routes</code> file that maps the request to the controller method.</li>
                        <li>The Twirl template that the action method calls to render the HTML markup.</li>
                    </ul>
                    <p>Using a command window or a GUI, start with the top-level project directory. The following directories contain
                        application components:</p>

                    <blockquote>Note: When changing directories in Windows shells, substitute <code>/</code> for <code>
                        \</code> in
                        path names.</blockquote>
                    <p>
                <ol>
                    <li>The <code>app</code>
                        subdirectory is where you put your Java code and packages. It contains directories
                        for <code>controllers</code> and <code>views</code>
                        , which will be familiar to those experienced with the
                        Model View Controller (MVC) architecture. Since this simple project does not need an external data
                        repository, it does not contain a <code>models</code>
                        directory, but this is where you would add it. You
                        could also add a <code>service</code> package and <code>utils</code> here. </li>
                    <li>The <code>public</code> subdirectory contains directories for <code>images</code>, <code>
                        javascripts</code>,
                        and <code>stylesheets</code>.</li>
                    <li>The <code>conf</code>
                        directory contains application configuration. For a more detailed explanation of
                        the project's structure, see <a href="https://www.playframework.com/documentation/"""),_display_(/*55.108*/version),format.raw/*55.115*/("""/Anatomy#The-Play-application-layout"
                        target="blank" />Play Application Layout</a>.</li>

                    <li>
                        <p>To locate the controller action method, open <code>app/controllers/HomeController.java</code>
                            file with
                            your favorite text editor.</p>

                        <p>The <code>Homecontroller</code> class includes the <code>index</code>
                            action method, as shown below.
                            This is a very simple action method that generate an HTML page from the <code>
                                index.scala.html</code>
                            Twirl template file.</p>
                        <pre><code class="language-java">public Result index() """),format.raw/*68.80*/("""{"""),format.raw/*68.81*/("""
                            """),format.raw/*69.29*/("""return ok(views.html.index.render());
                            """),format.raw/*70.29*/("""}"""),format.raw/*70.30*/("""</code></pre>
                    </li>
                    <li>To view the route that maps the browser request to the controller method, open the <code>
                        conf/routes</code>
                        file.
                        <p>A route consists of an HTTP method, a path, and an action. This control over the URL schema makes it
                            easy to
                            design clean, human-readable, bookmarkable URLs. The following line maps a GET request for the root URL
                            <code>/</code>
                            to the <code>index</code> action in <code>HomeController</code>:</p>
                        <code>GET     /                           controllers.HomeController.index</code></li>
                    <li>Open <code>app/views/index.scala.html</code> with your text editor.
                        <p>The main directive in this file calls the main template <code>main.scala.html</code>
                            with the string
                            <code>"Welcome"</code>
                            to generate the page.
                            You can open <code>app/views/main.scala.html</code> to see how a <code>String</code>
                            parameter sets the
                            page
                            title.</p>
                    </li>
                </ol>
                    </p>
                    <h3 id="next-steps">Next steps</h3>
                    <p>With this overview of the tutorial application, you are ready to <a href=""""),_display_(/*94.99*/routes/*94.105*/.HomeController.tutorial),format.raw/*94.129*/("""">
                        add
                        your own "Hello World" greeting</a>.</p>
                </article>
                <aside>
                """),_display_(/*99.18*/commonSidebar()),format.raw/*99.33*/("""
                """),format.raw/*100.17*/("""</aside>
            </div>
        </section>

    """)))}),format.raw/*104.6*/("""
""")))}),format.raw/*105.2*/("""
"""))
      }
    }
  }

  def render(): play.twirl.api.HtmlFormat.Appendable = apply()

  def f:(() => play.twirl.api.HtmlFormat.Appendable) = () => apply()

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: 2021-03-16T10:53:45.705
                  SOURCE: /Users/mark/Documents/Calamity/src/play-api/app/views/explore.scala.html
                  HASH: 8f929a3cd8aa102ac04bce579fa3d1c875296c61
                  MATRIX: 902->1|998->3|1028->8|1055->27|1094->29|1126->36|1173->75|1223->88|1261->100|5131->3942|5160->3949|6010->4771|6039->4772|6097->4802|6192->4869|6221->4870|7854->6476|7870->6482|7916->6506|8112->6675|8148->6690|8195->6708|8283->6765|8317->6768
                  LINES: 27->1|32->1|34->3|34->3|34->3|35->4|35->4|35->4|37->6|86->55|86->55|99->68|99->68|100->69|101->70|101->70|125->94|125->94|125->94|130->99|130->99|131->100|135->104|136->105
                  -- GENERATED --
              */
          