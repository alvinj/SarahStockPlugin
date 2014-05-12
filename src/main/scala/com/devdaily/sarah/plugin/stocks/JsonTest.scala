package com.devdaily.sarah.plugin.stocks

import com.devdaily.sarah.plugins._
import java.io._
import scala.collection.mutable.StringBuilder
import java.util.Properties
import scala.collection.mutable.ListBuffer
import akka.actor.ActorSystem
import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import net.liftweb.json.DefaultFormats
import net.liftweb.json._
import scala.collection.mutable.ArrayBuffer

object JsonTest extends App {

  implicit val formats = DefaultFormats  // for json handling
  val stocksJsonString ="""
{
  "phrases": [
    { "phrase" :  "get stock prices" },
    { "phrase" :  "get stocks" },
  },
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

  case class Stock(val symbol: String, val name: String, var price: String)
  case class Config2(val key: String, val value: String)
  case class Config(val phrase: String)
  
  def getStocks(stocksJsonString: String): Array[Stock] = {
    val stocks = ArrayBuffer[Stock]()
    val json = parse(stocksJsonString)
    //val phrases = json.extract[Config2]

    // phrases
    val phrases = (json \\ "phrases").children
    for (p <- phrases) {
      val phrase = p.extract[Config2]
      println(phrase)
    }

    val elements = (json \\ "stock").children
    for ( acct <- elements ) {
      val stock = acct.extract[Stock]
      stocks += stock
    }
    stocks.toArray
  }
  
  getStocks(stocksJsonString).foreach(println)

}















