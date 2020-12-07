
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
import scala.collection.JavaConverters._

object index extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template1[Int,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(hitCount: Int):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*2.1*/("""
"""),_display_(/*3.2*/main("Welcome to Play")/*3.25*/ {_display_(Seq[Any](format.raw/*3.27*/("""
  """),format.raw/*4.3*/("""<h1>Welcome to Play!</h1>
  <br/>
  Counter: """),_display_(/*6.13*/hitCount),format.raw/*6.21*/("""
  """),format.raw/*7.3*/("""<br/>
  <button   onclick="document.location='increment'">Increment</button>

""")))}),format.raw/*10.2*/("""
"""))
      }
    }
  }

  def render(hitCount:Int): play.twirl.api.HtmlFormat.Appendable = apply(hitCount)

  def f:((Int) => play.twirl.api.HtmlFormat.Appendable) = (hitCount) => apply(hitCount)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: 2020-12-06T15:37:29.773315
                  SOURCE: /Users/pseudo/Documents/GitHub/HelpMe/1-src/1-java/app/views/index.scala.html
                  HASH: b743cbab92f94be9dd3756e5e9b29e8a8c53f162
                  MATRIX: 905->1|1014->17|1041->19|1072->42|1111->44|1140->47|1212->93|1240->101|1269->104|1378->183
                  LINES: 27->1|32->2|33->3|33->3|33->3|34->4|36->6|36->6|37->7|40->10
                  -- GENERATED --
              */
          