// Databricks notebook source exported at Thu, 31 Dec 2015 15:55:34 UTC
import org.apache.spark.SparkEnv

// COMMAND ----------

SparkEnv.get.blockManager.stop()

// COMMAND ----------

sc.parallelize(1 to 10).collect()

// COMMAND ----------

val a = "abcd"

// COMMAND ----------

SparkEnv.get.httpFileServer.stop()

// COMMAND ----------

sc.dagScheduler

// COMMAND ----------

3 + 2