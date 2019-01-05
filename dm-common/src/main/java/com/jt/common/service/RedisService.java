package com.jt.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Service
public class RedisService {

	//有的工程需要，有的工程不需要。设置required=false，有就注入，没有就不注入。
    @Autowired(required = false)
    private ShardedJedisPool shardedJedisPool;
    @Autowired(required = false)//用的时候才注入不用的时候不注入
    private JedisSentinelPool jedisSentinelPool;
   //实现哨兵
    
    public String get(String key) {
    	Jedis jedis = jedisSentinelPool.getResource();
    	String result = jedis.get(key);
    	jedisSentinelPool.returnResource(jedis);
    	return result;
    }
    
    public void set(String key,String value) {
    	 Jedis jedis = jedisSentinelPool.getResource();
    	 jedis.set(key, value);
    	 jedisSentinelPool.returnResource(jedis);
    }
    //有过期时间的值
    public void set(String key,String value,Integer seconds) {
    	Jedis jedis = jedisSentinelPool.getResource();
//    	jedis.set(key, value);
    	jedis.setex(key, seconds, value);
    	jedisSentinelPool.returnResource(jedis);
    }
/*    //实现分片set操作
    public void set(String key,String value) {
    	ShardedJedis jedis = shardedJedisPool.getResource();
    	jedis.set(key, value);
    	shardedJedisPool.returnResource(jedis);
    }
    
    //实现分片get操作
    public String get(String key) {
    	ShardedJedis jedis = shardedJedisPool.getResource();
    	String result = jedis.get(key);
    	shardedJedisPool.returnResource(jedis);
    	return result;
    }
    */

}
