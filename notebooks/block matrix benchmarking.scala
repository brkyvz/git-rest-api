// Databricks notebook source exported at Tue, 15 Sep 2015 17:26:19 UTC
import java.util.Random

import org.json4s.JValue
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._

import org.apache.spark.SparkContext
import org.apache.spark.storage.StorageLevel
import org.apache.spark.mllib.linalg.Matrices
import org.apache.spark.mllib.linalg.distributed.BlockMatrix

// COMMAND ----------

def randn(
    m: Long,
    n: Long,
    blockSize: Int,
    numPartitions: Int,
    sparsity: Double,
    seed: Long): BlockMatrix = {
  val numRowBlocks = math.ceil(m / blockSize).toInt
  val numColBlocks = math.ceil(n / blockSize).toInt
  val sqrtParts = math.ceil(math.sqrt(numPartitions)).toInt
  val rowBlockIds = sc.parallelize(0 until numRowBlocks, sqrtParts)
  val colBlockIds = sc.parallelize(0 until numColBlocks, sqrtParts)
  val rng = new Random(seed)
  val blockIds = rowBlockIds.cartesian(colBlockIds)
  val blocks = blockIds.filter(_ => rng.nextDouble <= sparsity).mapPartitionsWithIndex { (idx, ids) =>
    val random = new Random(idx ^ seed)
    ids.map { case (rowBlockId, colBlockId) =>
      val mi = math.min(m - rowBlockId * blockSize, blockSize).toInt
      val ni = math.min(n - colBlockId * blockSize, blockSize).toInt
      ((rowBlockId, colBlockId), Matrices.randn(mi, ni, random))
    }
  }.persist(StorageLevel.MEMORY_AND_DISK)
  new BlockMatrix(blocks, blockSize, blockSize, m, n)
}

// COMMAND ----------

def createInputData(seed: Long, m: Long, k: Long, n: Long, blockSize: Int, sparsity: Double, numPartitions: Int): (BlockMatrix, BlockMatrix) = {
   val random = new Random(seed)

   (randn(m, k, blockSize, numPartitions, sparsity, seed ^ random.nextLong()),
     randn(k, n, blockSize, numPartitions, sparsity, seed ^ random.nextLong()))
}

// COMMAND ----------

def run(A: BlockMatrix, B: BlockMatrix): Double = {
  val start = System.currentTimeMillis()
  val C = A.multiply(B)
  C.blocks.count()
  val duration = (System.currentTimeMillis() - start) / 1e3
  duration
}

// COMMAND ----------

def runTest(m: Long, k: Long, n: Long, blockSize: Int, numPartitions: Int, sparsity: Double, numTrials: Int, seed: Long): JValue = {
  val (mat1, mat2) = createInputData(seed, m, k, n, blockSize, sparsity, numPartitions)
  var sum = 0.0
  (1 to numTrials).foreach { i =>
    sum += run(mat1, mat2)
    System.gc()
    Thread.sleep(1000)
  }
  val base: JValue = 
    ("m" -> m) ~ ("k" -> k) ~ ("n" -> n) ~ ("blockSize" -> blockSize) ~ 
    ("numPartitions" -> numPartitions) ~ ("seed" -> seed) ~
    ("duration" -> sum / numTrials)
   val json: JValue = base
  //  ("sparkConf" -> sc.getConf.getAll.toMap) ++ base
    
  println("results: " + compact(render(base)))
  json
}

// COMMAND ----------



// COMMAND ----------

val sizes = Seq(1e4)
val sparsities = Seq(0.01, 0.1, 1.0)
val blockSize = Seq(1024)
val numPartitions = Seq(128)


// COMMAND ----------

for (m <- sizes; s <- sparsities; bSize <- blockSize; nPartitions <- numPartitions) { 
  // runTest(1000, 1000, 1000, 100, 32, 1.0, 5, 42L) // warm-up
  val results = runTest(m.toLong, m.toLong, m.toLong, bSize, nPartitions, s, 5, 42L)
  dbutils.fs.put(s"burak/block-matrix/old-m_$m-s_$s-b_$bSize-p_$nPartitions", results.toString, true)
}
   

// COMMAND ----------



// COMMAND ----------

