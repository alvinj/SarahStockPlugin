package com.devdaily.sarah.plugin.stocks

import com.devdaily.sarah.plugins._
import com.devdaily.stocks.StockUtils
import com.devdaily.sarah.actors.ShowTextWindow
import java.io._
import scala.collection.mutable
import scala.collection.mutable.StringBuilder
import scala.collection.mutable.ArrayBuffer
import akka.actor.ActorSystem
import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import net.liftweb.json.DefaultFormats
import net.liftweb.json._
import scala.concurrent.ExecutionContext.Implicits.global


/**
 * 
 * The JSON this code expects looks like this, now allowing multiple phrases:
 * 
    """
    {
      "phrases" : [
          "check stock prices", 
          "get stock prices", 
          "get stocks"
      ],
      "stocks": [
      { "stock": {
        "symbol": "AAPL",
        "name": "Apple",
        "price": "0"
      }},
      { "stock": {
        "symbol": "GOOG",
        "name": "Google",
        "price": "0"
      }}
      ]
    }
    """
 * 
 */
class StockPlugin extends SarahPlugin {

  val relativePropertiesFileName = "Stocks.properties"
  val phrasesICanHandle = mutable.Set[String]()
  var canonPluginDirectory = ""

  // TODO a better way to handle this?
  var stocks: Array[Stock] = _
 
  // sarah callbacks
  def textPhrasesICanHandle: List[String] = phrasesICanHandle.toList
  override def setPluginDirectory(dir: String) { 
      canonPluginDirectory = dir 
      readConfigFile(getCanonPropertiesFilename)
  }
  def getCanonPropertiesFilename: String = canonPluginDirectory + PluginUtils.getFilepathSeparator + relativePropertiesFileName
  def startPlugin {}  // no longer called
  
  // sarah callback. handle our phrases when we get them
  def handlePhrase(phrase: String): Boolean = {
      for (p <- phrasesICanHandle) {
          if (p.trim.equalsIgnoreCase(phrase.trim)) {
              handleMatchedPhrase
              true
          }
      }
      false
  }
  
  def handleMatchedPhrase {
      // TODO re-read config file here so i can add/rm stocks without restarting sarah
      // this should be 0) tell sarah to speak 'stand by' phrase, 1) get list of stocks, 
      // 2) retrieve prices, 3) have sarah read prices
      //val f = Future { brain ! PleaseSay("Stand by.") }
      brain ! PleaseSay("Stand by.")
      populateStockPricesFromDataSource
      
      // no longer speaking stock prices. (seems to slow)
      //val f2 = Future { brain ! PleaseSay(createStringForSarahToSpeak) }

      //val f3 = Future { brain ! ShowTextWindow(createStringForSarahToShow) }
      brain ! PleaseSay("Here you go.")
      brain ! ShowTextWindow(createStringForSarahToShow)
  }
  
  case class Stock(symbol: String, name: String, var price: String)

  def createStringForSarahToSpeak = {
      var sb = new StringBuilder
      stocks.foreach(s => sb.append("%s is $%s. ".format(s.name, s.price)) ) 
      sb.toString
  }
  
  def createStringForSarahToShow = {
      var sb = new StringBuilder
      sb.append("<table>\n")
      for (s <- stocks) {
          sb.append(s"<tr><td>${s.name}</td><td>${s.price}</td></tr>\n")
      }
      sb.append("</table>\n")
      sb.toString
  }
  
  def populateStockPricesFromDataSource {
      // TODO there are better ways to do this
      for(s <- stocks) {
          val future = Future {
              s.price = retrieveStockPrice(s.symbol)
          }
          Await.result(future, 5 seconds)
      }
  }

  // given a symbol like "AAPL", it returns the price as a string
  def retrieveStockPrice(symbol: String): String = {
      val html = StockUtils.getHtmlFromUrl(symbol)
      val price = StockUtils.extractPriceFromHtml(html, symbol)
      price
  }

  /**
   * Properties File Code
   * --------------------
   */
  def readConfigFile(canonConfigFilename: String) {
      val fileContents = PluginUtils.getFileContentsAsString(canonConfigFilename)
      stocks = loadDataFromJson(fileContents)
  }

  implicit val formats = DefaultFormats  // for json handling
  def loadDataFromJson(stocksJsonString: String): Array[Stock] = {
      val json = parse(stocksJsonString)
      configureWhatUserCanSay(json)
      extractDesiredStocksFromJson(json)
  }

  def configureWhatUserCanSay(json: JValue) {
      val supportedPhrases = extractWhatUserCanSayFromJson(json)
      phrasesICanHandle ++= supportedPhrases
  }

  def extractWhatUserCanSayFromJson(json: JValue): List[String] = {
      val phrases = (json \\ "phrases").children  // List[JString]
      for {
          p <- phrases
          val x = p.extract[String]
      } yield x
  }
  
  def extractDesiredStocksFromJson(json: JValue): Array[Stock] = {
      val tmpStocks = ArrayBuffer[Stock]()
      val elements = (json \\ "stock").children
      for (e <- elements) {
          tmpStocks += e.extract[Stock]
      }
      tmpStocks.toArray
  }
  
}






