package io.thiagocosta.stockaverage

import io.thiagocosta.stockaverage.database.PostgresSlick
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.apache.spark.SparkConf
import org.apache.spark.sql.functions._

object StockAverageLocal extends App {
  val conf = new SparkConf()
    .setMaster("local")
    .setAppName("stock-average")

  Runner.run(conf)
}

object StockAverage extends App {
  Runner.run(new SparkConf()
    .setMaster("spark://localhost:7077")
    .setAppName("cluster-average")
  )
}

object Runner {
  def run(conf: SparkConf): Unit = {
    val ss = SparkSession.builder().config(conf).getOrCreate()

    import ss.implicits._

    val df: DataFrame = ss.read.jdbc(PostgresSlick.connectionUrl, PostgresSlick.stockPricesTableName, PostgresSlick.connectionProperties)

    val x = df.select("ticker", "date", "high", "low")
      .groupBy("ticker")
      .avg("high", "low")
      .withColumn("avg", ($"avg(high)" + $"avg(low)")/2)
      .select("ticker", "avg")

    x.write
        .mode(SaveMode.Overwrite)
        .jdbc(PostgresSlick.connectionUrl, PostgresSlick.monthlyStockPriceTableName, PostgresSlick.connectionProperties)

    ss.close()
  }
}
