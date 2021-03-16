
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

object index extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template0[play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/():play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*2.1*/("""

"""),_display_(/*4.2*/main("Welcome")/*4.17*/ {_display_(Seq[Any](format.raw/*4.19*/("""
    """),_display_(/*5.6*/defining(play.core.PlayVersion.current)/*5.45*/ { version =>_display_(Seq[Any](format.raw/*5.58*/("""

        """),format.raw/*7.9*/("""<section id="content">
            <div class="wrapper doc">
                <article>
                    <h2>Welcome to the Hello World Tutorial!</h2>
                    <p>This tutorial introduces Play Framework, describes how Play web applications work, and walks you through steps
                        to create page that displays a Hello World greeting.</p>
                    <p>If you loaded this page from the web server running on <code>localhost:9000</code>
                        , congratulations! You have
                        successfully built and run a Play application. If not, you likely opened the source <code>
                            index.scala.html</code>
                        file. Please follow the directions in the <code>README.md</code>
                        file in the top-level project directory to run
                        the tutorial.</p>

                    <h3 id="introduction">Introduction to Play</h3>
                    <p>As illustrated below, Play is a full-stack framework with all of the components you need to build a Web
                        Application or a REST service, including:</p>
                    <ul>
                        <li>An integrated HTTP server</li>
                        <li>Form handling</li>
                        <li>Cross-Site Request Forgery (CSRF) protection</li>
                        <li>A powerful routing mechanism</li>
                        <li>I18n support, and more.</li>
                    </ul>

                    <img src="assets/images/play-stack.png" alt="Play Stack" class="small-5 medium-4 large-3" />

                    <p>Play integrates with many object relational mapping (ORM) layers. It has out-of-the-box support for <a href="https://www.playframework.com/documentation/"""),_display_(/*34.178*/version),format.raw/*34.185*/("""/Anorm"
                    target="_blank">Anorm</a>
                        , <a href="https://www.playframework.com/documentation/"""),_display_(/*36.81*/version),format.raw/*36.88*/("""/JavaEbean" target="_blank">
                            JavaEbean</a>,
                        <a href="https://www.playframework.com/documentation/"""),_display_(/*38.79*/version),format.raw/*38.86*/("""/PlaySlick" target="_blank">
                            PlaySlick</a>, and
                        <a href="https://www.playframework.com/documentation/"""),_display_(/*40.79*/version),format.raw/*40.86*/("""/JavaJPA" target="_blank">JPA</a>
                        . See <a href="https://www.playframework.com/documentation/"""),_display_(/*41.85*/version),format.raw/*41.92*/("""/JavaDatabase"
                        target="_blank">Accessing an SQL Database</a>
                        for more information. Many customers use NoSQL, other ORMs or
                        even access data from a REST service.</p>


                    <p>Play APIs are available in both Java and Scala. The Framework uses <a href="https://akka.io" target="_blank">
                        Akka</a>
                        and <a href="https://doc.akka.io/docs/akka-http/current/index.html" target="_blank">Akka HTTP</a>
                        under the
                        hood. This endows Play applications with a stateless, non-blocking, event-driven architecture that provides
                        horizontal and vertical scalability and uses resources more efficiently. Play projects contain Scala
                        components, but because Play has a Java API, Java developers do not need to learn Scala to use Play
                        successfully.</p>

                    <p>Here are just a few of the reasons developers love using Play Framework:</p>
                    <ul>
                        <li>Its Model-View-Controller (MVC) architecture is familiar and easy to learn.</li>
                        <li>Direct support of common web development tasks and hot reloading saves precious development time.</li>
                        <li>A large active community promotes knowledge sharing.</li>
                        <li>Use of <a href="https://github.com/playframework/twirl">Twirl templates</a>
                            to render pages. The Twirl
                            template language is:
                            <ol>
                                <li>Easy to learn</li>
                                <li>Requires no special editor</li>
                                <li>Provides type safety</li>
                                <li>Is compiled so that errors display in the browser</li>
                            </ol>
                        </li>
                    </ul>

                    <p>To learn more about Play's benefits, visit the <a href="https://www.playframework.com" target="_blank">
                        Play
                        website</a>.</p>

                    <h3 id="next-steps">Next steps</h3>
                    <p>Now, let's <a href=""""),_display_(/*78.45*/routes/*78.51*/.HomeController.explore),format.raw/*78.74*/("""">explore the tutorial Play application</a>.</p>
                </article>
                <aside>
                """),_display_(/*81.18*/commonSidebar()),format.raw/*81.33*/("""
                """),format.raw/*82.17*/("""</aside>
            </div>
        </section>
    """)))}),format.raw/*85.6*/("""
""")))}),format.raw/*86.2*/("""
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
                  DATE: 2021-03-16T10:53:45.731
                  SOURCE: /Users/mark/Documents/Calamity/src/play-api/app/views/index.scala.html
                  HASH: 689140b0c46a35023271865ff2101850fbdaae85
                  MATRIX: 900->1|996->4|1024->7|1047->22|1086->24|1117->30|1164->69|1214->82|1250->92|3085->1899|3114->1906|3275->2040|3303->2047|3480->2197|3508->2204|3689->2358|3717->2365|3862->2483|3890->2490|6260->4833|6275->4839|6319->4862|6463->4979|6499->4994|6544->5011|6626->5063|6658->5065
                  LINES: 27->1|32->2|34->4|34->4|34->4|35->5|35->5|35->5|37->7|64->34|64->34|66->36|66->36|68->38|68->38|70->40|70->40|71->41|71->41|108->78|108->78|108->78|111->81|111->81|112->82|115->85|116->86
                  -- GENERATED --
              */
          