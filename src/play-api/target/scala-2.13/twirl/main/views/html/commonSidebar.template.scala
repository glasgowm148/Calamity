
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

object commonSidebar extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template0[play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/():play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*2.1*/("""    """),_display_(/*2.6*/defining(play.core.PlayVersion.current)/*2.45*/ { version =>_display_(Seq[Any](format.raw/*2.58*/("""
        """),format.raw/*3.9*/("""<h3>Table of Contents</h3>
        <ul>
            <li><a href=""""),_display_(/*5.27*/routes/*5.33*/.HomeController.index),format.raw/*5.54*/("""#Introduction">Welcome</a>
        <li><a href=""""),_display_(/*6.23*/routes/*6.29*/.HomeController.explore),format.raw/*6.52*/("""">Play application overview</a>
            <li><a href=""""),_display_(/*7.27*/routes/*7.33*/.HomeController.tutorial),format.raw/*7.57*/("""">Implementing Hello World</a>
        </ul>
        <h3>Related Resources</h3>
        <ul>
            <li><a href="https://playframework.com/documentation/"""),_display_(/*11.67*/version),format.raw/*11.74*/("""" target="_blank">Play documentation</a></li>
            <li><a href="https://discuss.lightbend.com/c/play/" target="_blank">Forum</a></li>
            <li><a href="https://gitter.im/playframework/playframework" target="_blank">Gitter Channel</a></li>
            <li><a href="https://stackoverflow.com/questions/tagged/playframework" target="_blank">Stackoverflow</a></li>
            <li><a href="https://lightbend.com/how" target="_blank">Professional support</a></li>
        </ul>
    """)))}))
      }
    }
  }

  def render(): play.twirl.api.HtmlFormat.Appendable = apply()

  def f:(() => play.twirl.api.HtmlFormat.Appendable) = () => apply()

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: 2021-03-16T10:53:45.676
                  SOURCE: /Users/mark/Documents/Calamity/src/play-api/app/views/commonSidebar.scala.html
                  HASH: f3a0e23740c59f0a6263ccf9724f99c02e0d848b
                  MATRIX: 908->1|1004->4|1034->9|1081->48|1131->61|1166->70|1258->136|1272->142|1313->163|1388->212|1402->218|1445->241|1529->299|1543->305|1587->329|1773->488|1801->495
                  LINES: 27->1|32->2|32->2|32->2|32->2|33->3|35->5|35->5|35->5|36->6|36->6|36->6|37->7|37->7|37->7|41->11|41->11
                  -- GENERATED --
              */
          