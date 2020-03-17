package app.util

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}

object KafkaUtil {

  def getKafkaStream(topics:Array[String],ssc:StreamingContext):  InputDStream[ConsumerRecord[String,String]] = {

    val prop = PropertiesUtil.loadProperties("conf.properties")
//    val broker_list = prop.getProperty("kafka.broker.list")
    val broker_list = prop.getProperty("kafka.broker.list")
//    bootstrap.servers
    val kafkaParam = Map("group.id"->"kafkalog",
//      "bootstrap.servers" -> broker_list,
              "bootstrap.servers"-> broker_list,
    "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
    "value.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
      "auto.offset.reset" -> "earliest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )
    val kafkaStream = KafkaUtils.createDirectStream[String,String](ssc,LocationStrategies.PreferConsistent,ConsumerStrategies.Subscribe[String,String](topics,kafkaParam))


    //    kafka

    kafkaStream

  }

}
