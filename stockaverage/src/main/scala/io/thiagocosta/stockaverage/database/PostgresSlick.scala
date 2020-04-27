package io.thiagocosta.stockaverage.database

import java.util.Properties

import io.thiagocosta.stockaverage.Model

import scala.slick.driver.PostgresDriver.simple._

object PostgresSlick {
  val connectionUrl = "jdbc:postgresql://postgres:5432/postgres?user=postgres&password=postgres"
  val stockPricesTableName = "stock_prices"
  val monthlyStockPriceTableName = "stock_monthly_prices"
  val connectionProperties = new Properties()
  connectionProperties.setProperty("Driver", "org.postgresql.Driver")
}

class PostgresSlick {

  def getByDate(date: String): List[String] = {
    Database.forURL(PostgresSlick.connectionUrl, driver = "org.postgresql.Driver") withTransaction{
      implicit session =>
      val prices = TableQuery[Model.StockPrices]
      prices.filter(_.date === date).map( _.ticker).list
    }
  }

  def debug: Unit = {
    Database.forURL(PostgresSlick.connectionUrl, driver = "org.postgresql.Driver") withSession {
      implicit session =>
        val prices = TableQuery[Model.StockPrices]

        prices.list foreach { row =>
          println("prices with date " + row._1 + " has ticker " + row._7)
        }

        prices.filter(_.ticker === "AAPL").list foreach { row =>
          println("prices whose ticker is 'AAPL' has volume " + row._6 )
        }
    }
  }

}
