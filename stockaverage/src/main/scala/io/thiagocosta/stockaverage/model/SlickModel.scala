package io.thiagocosta.stockaverage

import scala.slick.driver.PostgresDriver.simple._

object Model {

  // 2016-11-02,111.4,112.35,111.23,111.59,28331709,AAPL
  class StockPrices(tag: Tag) extends Table[(String, Double, Double, Double, Double, Long, String)](tag, "stock_prices") {
    def date = column[String]("date")
    def open = column[Double]("open")
    def high = column[Double]("high")
    def low = column[Double]("low")
    def close = column[Double]("close")
    def volume = column[Long]("volume")
    def ticker = column[String]("ticker")
    def * = (date, open, high, low, close, volume, ticker)
  }

  class StockMonthlyPrices(tag: Tag) extends Table[(String, Double)](tag, "stock_monthly_prices") {
    def ticker = column[String]("ticker")
    def average = column[Double]("average")
    def * = (ticker, average)
  }

}
