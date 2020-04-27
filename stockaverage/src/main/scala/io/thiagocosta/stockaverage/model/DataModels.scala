package io.thiagocosta.stockaverage.model

object DataModels {
  case class StockPrice(date: String, open: Double, high: Double, low: Double, close: Double, volume: Long, ticker: String)
  case class MonthlyStockPrice(ticker: String, average: Double)
}
