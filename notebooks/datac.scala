// Databricks notebook source exported at Fri, 5 Jun 2015 19:05:56 UTC
2 + 4 + 4

// COMMAND ----------

// MAGIC %sql show tables

// COMMAND ----------

// MAGIC %sql select * from (
// MAGIC   select t, sum(quantity) / (24 * 60 * 60 * 1000) as shards
// MAGIC from (
// MAGIC   select date as t, quantity
// MAGIC   from usageLogs
// MAGIC   where date > "2015-05-00" and metric = "serviceUptime" and tags.projectName = "webapp" and tags.shardName != "shard-logfood") u
// MAGIC group by t
// MAGIC order by t desc limit 1001) a
// MAGIC order by t asc

// COMMAND ----------

// MAGIC %sql select t, branchName, sum(quantity) / (24 * 60 * 60 * 1000) as shards
// MAGIC from (
// MAGIC   select date as t, tags.branchName, quantity
// MAGIC   from usageLogs
// MAGIC   where date > "2015-05-00" and metric = "serviceUptime" and tags.projectName = "webapp" and tags.shardName != "shard-logfood") u
// MAGIC group by t, branchName
// MAGIC order by branchName desc, t asc

// COMMAND ----------

// MAGIC %sql select tags.shardName, count(quantity) as q from usageLogs where timestamp > (1432555200000) and timestamp < (1432621800000) and date > "2015-05-10" and metric = "serviceUptime" and tags.projectName = "webapp" group by tags.shardName order by q desc

// COMMAND ----------

// MAGIC %sql select tags.shardName, count(quantity) as q from usageLogs where timestamp > (1432688400000) and timestamp < (1432755000000) and date > "2015-05-10" and metric = "serviceUptime" and tags.projectName = "webapp" group by tags.shardName order by q desc

// COMMAND ----------

display(dbutils.fs.ls("/mnt/raw-logs/databricks-prod-storage-virginia/shard-celtra-databricks/logs/universe/2015/week-21/"))

// COMMAND ----------

display(dbutils.fs.ls("/mnt/raw-logs/databricks-prod-storage-virginia/shard-celtra-databricks/logs/customer/2015/week-21/"))

// COMMAND ----------

display(dbutils.fs.ls("/mnt/raw-logs/databricks-prod-storage-oregon/shard-assistant-databricks/logs/customer/2015/week-21"))

// COMMAND ----------

display(dbutils.fs.ls("/mnt/raw-logs/databricks-prod-storage-virginia/shard-collokia-databricks/logs/universe/2015/week-21"))

// COMMAND ----------

