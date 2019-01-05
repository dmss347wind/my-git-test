package com.jt.common.factory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

public class JedisClusterFactory implements FactoryBean<JedisCluster>{
	
	private Resource propertySource;
	private JedisPoolConfig poolConfig;
	private String redisNodePrefix;
	

	@Override
	public JedisCluster getObject() throws Exception {
		Set<HostAndPort> nodes = getNodes();
		return new JedisCluster(nodes, poolConfig);
	}

	private Set<HostAndPort> getNodes() {
		Set<HostAndPort> nodes = new HashSet<>();
		
		Properties properties = new Properties();
		try {
			properties.load(propertySource.getInputStream());
			
			for (Object key:properties.keySet()) {
				String strKey=(String)key;
				if(strKey.startsWith(redisNodePrefix)) {
					//获取redis节点数据
					String value = (String) properties.get(key);
					String[] args = value.split(":");
					HostAndPort hostAndPort = new HostAndPort(args[0], Integer.parseInt(args[1]));
					nodes.add(hostAndPort);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nodes;
	}

	@Override
	public Class<?> getObjectType() {
		return JedisCluster.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}
	public Resource getPropertySource() {
		return propertySource;
	}
	
	public void setPropertySource(Resource propertySource) {
		this.propertySource = propertySource;
	}
	
	public JedisPoolConfig getPoolConfig() {
		return poolConfig;
	}
	
	public void setPoolConfig(JedisPoolConfig poolConfig) {
		this.poolConfig = poolConfig;
	}
	
	public String getRedisNodePrefix() {
		return redisNodePrefix;
	}
	
	public void setRedisNodePrefix(String redisNodePrefix) {
		this.redisNodePrefix = redisNodePrefix;
	}

}
