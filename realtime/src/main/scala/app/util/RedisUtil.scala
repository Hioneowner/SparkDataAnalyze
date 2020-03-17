package app.util

import redis.clients.jedis.{Jedis, JedisPool, JedisPoolConfig}

object RedisUtil {


  val jedisPool:JedisPool = null


  def getJedisPoll:Jedis = {


    val prop = PropertiesUtil.loadProperties("conf.properties")
    val port = prop.getProperty("redis.port")
    val host = prop.getProperty("redis.host")
    val conf = new JedisPoolConfig()


    conf.setMaxWaitMillis(2000)
    conf.setMaxTotal(100)
    conf.setMinIdle(20)
    conf.setBlockWhenExhausted(true)
    conf.setTestOnBorrow(true)
    val jedisPool = new JedisPool(conf,host,port.toInt)

    jedisPool.getResource
  }
}
