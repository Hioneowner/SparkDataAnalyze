package scalautil

import java.util.Objects

import io.searchbox.client.config.HttpClientConfig
import io.searchbox.client.{JestClient, JestClientFactory}
import io.searchbox.core.{Bulk, Index}

object EsUtil {

  private val host = "http://dn-1"
  private val port = "9200"
  private var factory:JestClientFactory = null

  def getFactory(): JestClient = {
    if (factory == null) build()
    factory.getObject
  }

  def close(jestClient: JestClient): Unit = {
    if (!Objects.isNull(jestClient))
      try {
        jestClient.shutdownClient()
      }
      catch {
        case e: Exception => {
          e.printStackTrace()
        }
    }
  }



   def build(): Unit = {
    factory = new JestClientFactory
    val conf =new HttpClientConfig.Builder(host + ":" + port).multiThreaded(true).maxTotalConnection(20)
        .connTimeout(1000).readTimeout(1000).build()

    factory.setHttpClientConfig(conf)
  }

  def main(args: Array[String]): Unit = {
    val factory = getFactory()

    val  source="{\n  \"name\":\"li4111\",\n  \"age\":456,\n  \"amount\": 250.1,\n  \"phone_num\":\"138***2123\"\n}"
    val index = new Index.Builder(source).`type`("_doc").index("log").build()

    factory.execute(index)
    close(factory)
  }


  def inserBulk(indexName: String,infos:List[Any]): Unit = {
    val factory = getFactory()

    val buck = new Bulk.Builder().defaultIndex(indexName).defaultType("_doc")


    for (info <- infos) {
      val index = new Index.Builder(info).build()
      buck.addAction(index)
    }
    factory.execute(buck.build())

    close(factory)

  }


  def indexBulk(indexName:String ,list :List[Any]): Unit ={
    val jest: JestClient = getFactory()
    val bulkBuilder = new Bulk.Builder().defaultIndex(indexName).defaultType("_doc")
    for (doc <- list ) {
      val index: Index = new Index.Builder(doc).build()
      bulkBuilder.addAction(index)
    }
    jest.execute(bulkBuilder.build())

//    val items: util.List[BulkResult#BulkResultItem] = jest.execute(bulkBuilder.build()).getItems
//    println(s"保存 = ${items.size()}")
    close(jest)
  }


}
