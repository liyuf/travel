package com.heima.travel.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ResourceBundle;

public class JedisUtil {
    private static JedisPool jedisPool=null;
    static{
        //获取properties流
        ResourceBundle jedis = ResourceBundle.getBundle("jedis");
        //获取参数
        String host = jedis.getString("jedis.host");
        String port = jedis.getString("jedis.port");
        String maxTotal = jedis.getString("jedis.maxTotal");
        String maxWaitMillis = jedis.getString("jedis.maxWaitMills");
        String maxIdle = jedis.getString("jedis.maxIdle");
        //获取jedisPoolConfig对象
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(Integer.parseInt(maxIdle));
        jedisPoolConfig.setMaxTotal(Integer.parseInt(maxTotal));
        jedisPoolConfig.setMaxWaitMillis(Long.parseLong(maxWaitMillis));
        //获取jedis连接池
        jedisPool = new JedisPool(jedisPoolConfig, host, Integer.parseInt(port));
    }

    //获取jedis连接对象
    public static Jedis getResource(){
        Jedis resource=null;
        resource = jedisPool.getResource();
        return resource;
    }

    public static void close(Jedis jedis) {
        jedis.close();
    }
}
