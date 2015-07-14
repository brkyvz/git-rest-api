// Databricks notebook source exported at Tue, 14 Jul 2015 23:29:22 UTC
import org.apache.spark.SparkEnv

// COMMAND ----------

SparkEnv.get.blockManager.stop()

// COMMAND ----------

sc.parallelize(1 to 10).collect()

// COMMAND ----------

SparkEnv.get.broadcastManager.stop()

// COMMAND ----------

SparkEnv.get.httpFileServer.stop()

// COMMAND ----------

sc.dagScheduler

// COMMAND ----------

