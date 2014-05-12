package com.devdaily.stocks

import java.io._
import scala.io.Source

// get this from sarah
import org.htmlcleaner.HtmlCleaner

/**
 * TODO: Use the StockUtils project from GitHub, whenever I check it in there.
 */
object StockUtils {

  def getHtmlFromUrl(symbol: String) = {
    val url = "http://finance.yahoo.com/q/ks?s=%s+Key+Statistics".format(symbol.trim.toUpperCase)
    Source.fromURL(url).mkString
  }

  /*
   * <div class="yfi_rt_quote_summary_rt_top"><p>
   * <span class="time_rtq_ticker">
   * <span id="yfs_l84_hpq">19.70</span></span>
   */
  def extractPriceFromHtml(html: String, symbol: String): String = {
    val cleaner = new HtmlCleaner
    val rootNode = cleaner.clean(html)

    var keepLooking = true
    var price = "0.00"
    val elements = rootNode.getElementsByName("span", true)
    for (elem <- elements if keepLooking) {
      val classType = elem.getAttributeByName("id")
      if (classType != null && classType.equalsIgnoreCase("yfs_l84_" + symbol.toLowerCase)) {
        price = elem.getText.toString.trim
        keepLooking = false
      }
    }
    return price
  }

  def getHtmlFromFile(symbol: String) = {
    val filename = symbol + ".html"
    Source.fromFile(filename).getLines.mkString
  }

  def saveContentsToFile(contents: String, filename: String) {
    val pw = new PrintWriter(new File(filename))
    pw.write(contents)
    pw.close
  }

}


