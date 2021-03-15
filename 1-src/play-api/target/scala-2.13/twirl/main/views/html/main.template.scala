
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

object main extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template2[String,Html,play.twirl.api.HtmlFormat.Appendable] {

  /*
* This template is called from the `index` template. This template
* handles the rendering of the page header and body tags. It takes
* two arguments, a `String` for the title of the page and an `Html`
* object to insert into the body of the page.
*/
  def apply/*7.2*/(title: String)(content: Html):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*8.1*/("""
"""),format.raw/*9.1*/("""<!DOCTYPE html>
<html lang="en">

    <head>
        <title>"""),_display_(/*13.17*/title),format.raw/*13.22*/("""</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" media="screen" href='"""),_display_(/*15.54*/routes/*15.60*/.Assets.versioned("stylesheets/main.css")),format.raw/*15.101*/("""'>
        <link rel="stylesheet" media="screen" href='"""),_display_(/*16.54*/routes/*16.60*/.Assets.versioned("stylesheets/prism.css")),format.raw/*16.102*/("""'>
        <link rel="shortcut icon" type="image/png" href='"""),_display_(/*17.59*/routes/*17.65*/.Assets.versioned("images/favicon.png")),format.raw/*17.104*/("""'>
        <script src='"""),_display_(/*18.23*/routes/*18.29*/.Assets.versioned("javascripts/hello.js")),format.raw/*18.70*/("""' type="text/javascript"></script>
        <script src='"""),_display_(/*19.23*/routes/*19.29*/.Assets.versioned("javascripts/prism.js")),format.raw/*19.70*/("""' type="text/javascript"></script>
    </head>

    <body>
        <section id="top">
            <div class="wrapper">
                <img class="resize" src="assets/images/play_icon_reverse.svg" alt="logo" />
                <h1>Hello imad</h1>
            </div>
        </section>
        """),_display_(/*29.10*/content),format.raw/*29.17*/("""
    """),format.raw/*30.5*/("""</body>

</html>
"""))
      }
    }
  }

  def render(title:String,content:Html): play.twirl.api.HtmlFormat.Appendable = apply(title)(content)

  def f:((String) => (Html) => play.twirl.api.HtmlFormat.Appendable) = (title) => (content) => apply(title)(content)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: 2021-03-15T18:56:51.476
                  SOURCE: /Users/mark/Documents/project-docs/HelpMe/1-src/play-api/app/views/main.scala.html
                  HASH: ff8c7cbef9145d3157f59628333e6f87077735cb
                  MATRIX: 1160->255|1284->286|1311->287|1399->348|1425->353|1593->494|1608->500|1671->541|1754->597|1769->603|1833->645|1921->706|1936->712|1997->751|2049->776|2064->782|2126->823|2210->880|2225->886|2287->927|2609->1222|2637->1229|2669->1234
                  LINES: 32->7|37->8|38->9|42->13|42->13|44->15|44->15|44->15|45->16|45->16|45->16|46->17|46->17|46->17|47->18|47->18|47->18|48->19|48->19|48->19|58->29|58->29|59->30
                  -- GENERATED --
              */
          