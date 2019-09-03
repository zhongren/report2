package com.newproj.core.cache;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.newproj.core.json.JsonSerializer;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisCache implements Cache{
	private JedisPoolConfig poolConfig = new JedisPoolConfig() ;
	private JedisPool pool ;
	private JsonSerializer serializer = new JsonSerializer().filterNullValues();

	private void initPool( int maxActive , int maxIdle , long maxWait , String host , int port ){
		poolConfig.setMaxTotal( maxActive );
		poolConfig.setMaxIdle( maxIdle );
		poolConfig.setMaxWaitMillis( maxWait );
		pool = new JedisPool( poolConfig , host , port  ) ;
	}
	
	public RedisCache( int maxActive , int maxIdle , long maxWait , String host , int port ){
		initPool(maxActive, maxIdle, maxWait, host, port);
	}
	
	private Jedis getClient(){
		return pool.getResource() ;
	}
	
	private void close( Jedis client){
		try{
			client.close();
		}catch(Exception e){
			
		}
	}
	
	protected String serialize( Object data ){
		if( data == null ) return null ;
		return serializer.toJson( data ) ;
	}
	
	protected <T> T deserialize( String data , Class<T> clazz ){
		try{
			return serializer.fromJson( data , clazz) ;
		}catch(Exception e){
			return null ;
		}
	}
	
	@Override
	public <T> T put(String key, T value, Long expire) {
		Jedis client = getClient() ;
		if( StringUtils.isEmpty( key ) ||  value == null ) {
			return null ;
		}
		if( expire == null || expire <= 0 ){
			client.set( key , serialize( value ) , "nx" ) ;
			return value ;
		}
		client.set( key , serialize( value ) , "nx" , "ex" , expire ) ;
		close( client ) ;
		return value ;
	}
	
	@Override
	public boolean exists(String key) {
		Jedis client = getClient() ;
		if( StringUtils.isEmpty( key ) ){
			return false ;
		}
		boolean exists = client.exists( key ) ; 
		close( client ) ;
		return exists ;
	}

	@Override
	public <T> T get(String key, Class<T> clazz) {
		if( StringUtils.isEmpty( key ) ) {
			return null ;
		}
		Jedis client = getClient() ;
		T t = deserialize( client.get( key ) , clazz ) ; 
		close( client ) ;
		return t ;
	}
	
	@Override
	public void del(String key) {
		if( StringUtils.isEmpty( key ) ) {
			return  ;
		}
		Jedis client = getClient() ;
		client.del( key ) ;
		close( client ) ;
	}

	@Override
	public <T> T recache(String key, Class<T> clazz, Long unixTime ) {
		Jedis client = getClient() ;
		T value = null ;
		if( ( value = get( key , clazz ) ) == null ){
			return null ;
		}
		client.expireAt( key , unixTime ) ;
		close( client ) ;
		return value;
	}

	@Override
	public Set<String> keys(String pattern) {
		// TODO Auto-generated method stub
		Jedis client = getClient() ;
		Set<String>  keys = null ;
		try{
			keys = client.keys(pattern) ;
		}catch(Exception e){
			//Ignore .
		}finally{
			close( client ) ;
		}
		return keys;
	}



}
