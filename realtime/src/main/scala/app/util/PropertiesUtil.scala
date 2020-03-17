package app.util

import java.io.InputStreamReader
import java.util.Properties

object PropertiesUtil {


  def loadProperties(propertiesName:String): Properties = {
    val prop = new Properties()
    prop.load(new InputStreamReader(Thread.currentThread().getContextClassLoader.getResourceAsStream(propertiesName),"UTF-8"))

    prop

  }

  def main(args: Array[String]): Unit = {

    val prop = loadProperties("conf.properties")


    println(prop.getProperty("redis.host"))
  }

}
