package app

import java.text.SimpleDateFormat
import java.util.Date

import app.util.{KafkaUtil, RedisUtil}
import bean.Startuplog
import com.alibaba.fastjson.JSON
import common.Constant
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.glassfish.jersey.server.Broadcaster
import scalautil.{EsUtil, MyEsUtil}

object DauApp {


  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[*]").setAppName("DauApp")
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc,Seconds(3))

    val kafkaStream: InputDStream[ConsumerRecord[String,String]] = KafkaUtil.getKafkaStream(Array(Constant.KAFKA_TOPIC_EVENT), ssc)



    val startuplogStream = kafkaStream.map(rdd=>{
//      val value = rdd.value()
      val startuplog = JSON.parseObject(rdd.value(),classOf[Startuplog])
      val ts = startuplog.ts

      val date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(ts))
//      val dateStr =
      startuplog.logDate = date.split(" ")(0)
      startuplog.logHour = date.split(" ")(1).split(":")(0)
      startuplog.logHourMinute = date.split(" ")(1)

      startuplog
    })

    val filterDstream =startuplogStream.transform(rdd=>{
      val redis = RedisUtil.getJedisPoll
      val currentTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date())
      val key = "dau:" + currentTime

      val dateSet:java.util.Set[String] = redis.smembers(key)
      val midSetBroadcast : Broadcast[java.util.Set[String]] = ssc.sparkContext.broadcast(dateSet)


//        .broadcast()
      val filterRdd = rdd.filter(startuplogRdd =>{
        val mid = startuplogRdd.mid
        val midSet = midSetBroadcast.value
        !midSet.contains(mid)
      })
      filterRdd
    })

    val keyDstream: DStream[(String,Iterable[Startuplog])] = filterDstream.map(rdd=>(rdd.mid,rdd)).groupByKey()

    val resultDStream:DStream[Startuplog] = keyDstream.flatMap(rdd=>{
      val key = rdd._1
      val value = rdd._2.take(1)
      (key,value)
      value
    })


    resultDStream.foreachRDD(rdd=>{



      rdd.foreachPartition(r=>{

        val redis = RedisUtil.getJedisPoll

        val newR = r.toList
//        println(redis)
        for (i <- newR) {
          val key = "dau:" + i.logDate
          val value = i.mid
          redis.sadd(key,value)
        }

        MyEsUtil.indexBulk("newlog",newR.toList)
        redis.close()

      })
    })









//    startuplogStream.foreachRDD(rdd=>{
//
//      rdd.foreach(r=>{
//        println(r.logDate)
//      })
//    })


    ssc.start()
    ssc.awaitTermination()





  }
}
