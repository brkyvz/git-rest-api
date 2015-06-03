// Databricks notebook source exported at Wed, 3 Jun 2015 18:35:59 UTC
// MAGIC %md # How to use IntelliJ IDEA with Databricks Cloud
// MAGIC ### Not comfortable with Notebooks? Do you prefer development on IDE's like IntelliJ?
// MAGIC ### Deploying your work on an IDE to Databricks Cloud is now a single click process!

// COMMAND ----------

// MAGIC %md ## Requirements
// MAGIC 1. Open up the port 34563 for DB Api.
// MAGIC 2. SBT Plugin for IntelliJ.
// MAGIC  - To install the SBT Plugin for IntelliJ, go to `IntelliJ IDEA` -> `Preferences`. Then in `Plugins`, search for `sbt` and install the `SBT Plugin` as shown below.
// MAGIC  
// MAGIC  

// COMMAND ----------

// MAGIC %md ## Usage

// COMMAND ----------

// MAGIC %md ### 1. In project/plugins.sbt, add:
// MAGIC ```
// MAGIC addSbtPlugin("com.databricks" %% "sbt-databricks" % "0.1.1")
// MAGIC ```

// COMMAND ----------

// MAGIC %md ### 2. In your sbt build file, set the following keys:
// MAGIC ```scala
// MAGIC // Your username to login to Databricks Cloud
// MAGIC dbcUsername := // e.g. "admin"
// MAGIC 
// MAGIC // Your password (Can be set as an environment variable)
// MAGIC dbcPassword := // e.g. "admin" or System.getenv("DBCLOUD_PASSWORD")
// MAGIC 
// MAGIC // The URL to the Databricks Cloud DB Api. Don't forget to set the port number to 34563!
// MAGIC dbcApiUrl := // https://organization.cloud.databricks.com:34563/api/1.1
// MAGIC 
// MAGIC // Add any clusters that you would like to deploy your work to. e.g. "My Cluster"
// MAGIC dbcClusters += // Add "ALL_CLUSTERS" if you want to attach your work to all clusters
// MAGIC ```
// MAGIC **Gotcha: Setting environment variables in IDE's may differ. IDE's usually don't pick up environment variables from .bash_profile or .bashrc **
// MAGIC 
// MAGIC Other optional parameters are:
// MAGIC ```scala
// MAGIC // The location to upload your libraries to in the workspace e.g. "/home"
// MAGIC dbcLibraryPath := // Default is "/"
// MAGIC 
// MAGIC // Whether to restart the clusters everytime a new version is uploaded to Databricks Cloud
// MAGIC dbcRestartOnAttach := // Default true
// MAGIC ```
// MAGIC 
// MAGIC ![sample-build]()

// COMMAND ----------

// MAGIC %md ### 3. Open up the sbt console. In IntelliJ IDEA go to "View" -> "Tool Windows" -> "SBT Console".
// MAGIC 
// MAGIC ![intellij-sbt-console]()

// COMMAND ----------

// MAGIC %md ### 4. Simply use 'dbcDeploy' whenever you would like to run your library on Databricks Cloud.
// MAGIC 
// MAGIC ![dbc-deploy1]()
// MAGIC 
// MAGIC Each time you use `dbcDeploy`, the older version of the library will be deleted, a new version will be uploaded. The library will be attached to the specified clusters, and if the previous version was already attached to the clusters, and the setting `dbcRestartOnAttach` is `true` (by default it is `true`), the clusters will be restarted.
// MAGIC 
// MAGIC ![dbc-deploy2]()
// MAGIC 
// MAGIC `dbcDeploy` encapsulates the following commands:
// MAGIC  - `dbcUpload`: Uploads your Library to Databricks Cloud. Deletes the older version.
// MAGIC  - `dbcAttach`: Attaches your Library to the specified clusters.
// MAGIC  - `dbcRestartClusters`: Restarts the specified clusters.
// MAGIC 
// MAGIC Other helpful commands are:
// MAGIC  - `dbcListClusters`: View the states of available clusters.