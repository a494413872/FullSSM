package com.lvmama.scenic.comm.utils;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;
//import com.lvmama.config.common.ZooKeeperConfigProperties;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

public class MemcachedUtil {
	private static Object LOCK = new Object();
	private static Object SESSION_LOCK = new Object();
	private static Object PROD_GROUP_DATE_LOCK = new Object();
	private static Object VST_COMM_LOCK = new Object();

	private final static Log log = LogFactory.getLog(MemcachedUtil.class);
	private Properties properties;
	private MemCachedClient memCachedClient;
	public final static int HALF_HOUR=1800;
	public final static int ONE_HOUR=3600;
	public final static int TWO_HOUR=7200;
	public final static int TWENTY_FOUR_HOUR=86400;
	public final static String memcacheMapKey="MEMACACHE_MAP_KEY";
	
	//distribution lock
	private static final int DEFAULT_LOCK_SECCONDS=5;
	private static final int DEFAULT_TRYLOCK_TIMEOUT_SECONDS=5;
	
	private static MemcachedUtil instance;

    private boolean isSweet = true;

	public enum DataResourceEnum{
		COMM("dataServer", "cache.server", "默认源")/*,
		VST_COMM("vstDataServer", "vst.cache.server", "vst默认源"),
		SESSION("sessionServer", "session.cache.server", "SESSION源"),
		PROD_GROUP_DATE("prodGroupDateServer", "prodGroupDate.cache.server", "团期源")*/;

		private String dataSource;
		private String url;
		private String desp;
		DataResourceEnum(String dataSource, String url, String desp){
			this.dataSource = dataSource;
			this.url = url;
			this.desp = desp;
		}

		public String getDataSource() {
			return dataSource;
		}

		public void setDataSource(String dataSource) {
			this.dataSource = dataSource;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getDesp() {
			return desp;
		}

		public void setDesp(String desp) {
			this.desp = desp;
		}
	}

    private void init() {
        try {
            String serverStr = getProperty("cache.server");
            if (StringUtils.isBlank(serverStr)) {
                log.warn("sweet config load memcached is failed");
                isSweet = false;

                properties = new Properties();
                Resource resource = new ClassPathResource("memcached.properties");
                properties.load(resource.getInputStream());

                serverStr = getProperty("cache.server");
            }
            //数据缓存服务器，“,”表示配置多个memcached服务
            String[] servers = serverStr.replaceAll(" ", "").split(",");

            SockIOPool pool = SockIOPool.getInstance("dataServer");

            pool.setServers(servers);
            pool.setFailover(true);
            pool.setInitConn(10);
            pool.setMinConn(5);
            pool.setMaxConn(50);
            pool.setMaintSleep(30);
            pool.setNagle(false);
            pool.setSocketTO(30000);
            pool.setBufferSize(1024*1024*5);
            pool.setAliveCheck(true);
            pool.initialize(); /* 建立MemcachedClient实例 */

            memCachedClient = new MemCachedClient("dataServer");

        } catch (Exception ex) {
            log.error(ex, ex);
        }
    }

//    private void initVst() {
//
//        try {
//            String serverStr = getProperty("vst.cache.server");
//
//            if (StringUtils.isBlank(serverStr)) {
//                log.warn("sweet config load memcached is failed");
//                isSweet = false;
//
//                properties = new Properties();
//                Resource resource = new ClassPathResource("memcached.properties");
//                properties.load(resource.getInputStream());
//
//                serverStr = getProperty("vst.cache.server");
//            }
//			//数据缓存服务器，“,”表示配置多个memcached服务
//			String[] servers = serverStr.replaceAll(" ", "").split(",");
//			SockIOPool pool = SockIOPool.getInstance("vstDataServer");
//			pool.setServers(servers);
//			pool.setFailover(true);
//			pool.setInitConn(10);
//			pool.setMinConn(5);
//			pool.setMaxConn(50);
//			pool.setMaintSleep(30);
//			pool.setNagle(false);
//			pool.setSocketTO(3000);
//			pool.setBufferSize(1024*1024*5);
//			pool.setAliveCheck(true);
//			pool.initialize(); /* 建立MemcachedClient实例 */
//			vstMemCachedClient = new MemCachedClient("vstDataServer");
//		} catch (IOException e) {
//			log.error(ExceptionFormatUtil.getTrace(e));
//		}
//		catch (Exception ex) {
//			log.error(ex, ex);
//		}
//    }

//
//	private void initSessionMemCached() {
//
//        try {
//            String serverStr = getProperty("session.cache.server");
//            if (StringUtils.isBlank(serverStr)) {
//                log.warn("sweet config load session memcached is failed");
//                isSweet = false;
//
//                properties = new Properties();
//                Resource resource = new ClassPathResource("memcached.properties");
//                properties.load(resource.getInputStream());
//
//                serverStr = getProperty("session.cache.server");
//            }
//			//Session缓存服务器，“,”表示配置多个memcached服务
//			String[] sessionServers = serverStr.replaceAll(" ", "").split(",");
//			SockIOPool sessionPool = SockIOPool.getInstance("sessionServer");
//			sessionPool.setServers(sessionServers);
//			sessionPool.setFailover(true);
//			sessionPool.setInitConn(10);
//			sessionPool.setMinConn(5);
//			sessionPool.setMaxConn(50);
//			sessionPool.setMaintSleep(30);
//			sessionPool.setNagle(false);
//			sessionPool.setSocketTO(3000);
//			sessionPool.setBufferSize(1024*1024*5);
//			sessionPool.setAliveCheck(true);
//			sessionPool.initialize(); /* 建立MemcachedClient实例 */
//			sessionMemCachedClient = new MemCachedClient("sessionServer");
//		} catch (Exception ex) {
//            log.error(ExceptionFormatUtil.getTrace(ex));
//        }
//    }

    private MemCachedClient initBySource(DataResourceEnum dataResourceEnum) {

        try {
            String serverStr = getProperty(dataResourceEnum.getUrl());
            if (StringUtils.isBlank(serverStr)) {
                log.warn("sweet config load session memcached is failed");
                isSweet = false;

                properties = new Properties();
                Resource resource = new ClassPathResource("memcached.properties");
                properties.load(resource.getInputStream());

                serverStr = getProperty(dataResourceEnum.getUrl());
            }
			//Session缓存服务器，“,”表示配置多个memcached服务
			String[] sessionServers = serverStr.replaceAll(" ", "").split(",");
			SockIOPool sessionPool = SockIOPool.getInstance(dataResourceEnum.getDataSource());
			sessionPool.setServers(sessionServers);
			sessionPool.setFailover(true);
			sessionPool.setInitConn(10);
			sessionPool.setMinConn(5);
			sessionPool.setMaxConn(50);
			sessionPool.setMaintSleep(30);
			sessionPool.setNagle(false);
			sessionPool.setSocketTO(3000);
			sessionPool.setBufferSize(1024 * 1024 * 5);
			sessionPool.setAliveCheck(true);
			sessionPool.initialize(); /* 建立MemcachedClient实例 */
			return new MemCachedClient(dataResourceEnum.getDataSource());
		} catch (IOException e) {
			log.error(ExceptionFormatUtil.getTrace(e));
		}
		catch (Exception ex) {
            log.error(ex, ex);
        }
		return null;
	}

	private MemcachedUtil(){
		init();
//		initVst();
	}
	
	private boolean isCacheEnabled() {

        boolean useCache = false;
        try {
            useCache = Boolean.valueOf(getProperty("cache.enable"));
        } catch (Exception e) {
            useCache = false;
            log.info("Please enable memcached");
        }
        return useCache;
	}
	
	/**
	 * 改用嵌套类静态实始化
	 * @return
	 */
	public static MemcachedUtil getInstance() {
		if(instance == null){
			synchronized(LOCK) {
				if (instance==null) {
					instance=new MemcachedUtil();
				}
			}
		}
		return instance;
	}

	private MemCachedClient getMemCachedClient(boolean isForSession) {
//		if(isForSession){
//			if(sessionMemCachedClient == null){
//				synchronized(SESSION_LOCK) {
//					if (sessionMemCachedClient==null) {
//						initSessionMemCached();
//					}
//				}
//			}
//			return sessionMemCachedClient;
//		}
//		else
			return memCachedClient;
	}

	private MemCachedClient getMemCachedClient(DataResourceEnum dataResourceEnum) {
//		if(DataResourceEnum.SESSION.equals(dataResourceEnum)) {
//			if (sessionMemCachedClient == null){
//				synchronized(SESSION_LOCK) {
//					if (sessionMemCachedClient==null) {
//						initSessionMemCached();
//					}
//				}
//			}
//			return sessionMemCachedClient;
//		}
//		else if(DataResourceEnum.PROD_GROUP_DATE.equals(dataResourceEnum)){
//			if(prodGroupDateMemCachedClient == null){
//				synchronized(PROD_GROUP_DATE_LOCK) {
//					if (prodGroupDateMemCachedClient==null) {
//						prodGroupDateMemCachedClient = initBySource(DataResourceEnum.PROD_GROUP_DATE);
//					}
//				}
//			}
//			return prodGroupDateMemCachedClient;
//		}
//
//		else if(DataResourceEnum.VST_COMM.equals(dataResourceEnum)){
//			if(vstMemCachedClient == null){
//				synchronized(VST_COMM_LOCK) {
//					if (vstMemCachedClient==null) {
//						vstMemCachedClient = initBySource(DataResourceEnum.VST_COMM);
//					}
//				}
//			}
//			return vstMemCachedClient;
//		}

		//默认 | 未命中时，返回默认缓存源
		return memCachedClient;
	}
	
	public boolean replace(String key, int seconds, Object obj) {
		return replace(key, seconds, obj, false);
	}

	/**
	 * 替换
	 * @param key
	 * @param seconds 过期秒数
	 * @param obj
	 * @return
	 */
	public boolean replace(String key, int seconds, Object obj,boolean isForSession) {
		if(StringUtils.isEmpty(key)){
			return false;
		}
		if(obj==null){
			return true;
		}
//		Transaction t = getTransaction(isForSession, key + ":replace");
		try{
			if (isCacheEnabled()) {
				Date  expDate = getDateAfter(seconds);
				boolean result = getMemCachedClient(isForSession).replace(key, obj, expDate);
				if(log.isDebugEnabled()) {
					log.debug("SET A OBJECT: KEY:" + key + ", OBJ:" + obj + ", exp:" + expDate  + ", result:" + result);
				}
//				t.setStatus(Transaction.SUCCESS);
				return result;
			}
			return true;
		} catch (Exception e) {
//			Cat.logError(e);
//			t.setStatus(e);
			log.error("MemcachedUtil.replace error:", e);
		} finally {
//			t.complete();
		}
		return false;
	}

	/**
	 * 放
	 * @param key
	 * @param seconds 过期秒数
	 * @param obj
	 * @return
	 */
	public boolean set(String key, int seconds, Object obj,boolean isForSession) {
		return set(key, getDateAfter(seconds), obj,isForSession);
	}

	/**
	 * 放
	 * @param key
	 * @param seconds 过期秒数
	 * @param obj
	 * @return
	 */
	public boolean set(String key, int seconds, Object obj,DataResourceEnum dataResourceEnum) {
		return set(key, getDateAfter(seconds), obj,dataResourceEnum);
	}

	public boolean set(String key, int seconds, Object obj) {
		return set(key, getDateAfter(seconds), obj,false);
	}

	
	/**
	 * 将KEY保存到memcache中
	 * 
	 * @param key
	 * @param exp
	 * @param obj
	 * @return
	 */
	public boolean set(String key,Date exp,Object obj,boolean isForSession){
		if(StringUtils.isEmpty(key)){
			return false;
		}
		if(obj==null){
			return true;
		}
//		Transaction t = getTransaction(isForSession, key + ":set");
		try{
			if (isCacheEnabled()) {
				boolean result = getMemCachedClient(isForSession).set(key, obj, exp);
				if(log.isDebugEnabled()) {
					log.debug("SET A OBJECT: KEY:" + key + ", exp:" + exp + ", result:" + result);
				}
//				t.setStatus(Transaction.SUCCESS);
				return result;
			}
			return true;
		} catch (Exception e) {
//			Cat.logError(e);
//			t.setStatus(e);
			log.error("MemcachedUtil.set error:", e);
		} finally {
//			t.complete();
		}
		return false;
	}

	/**
	 * 将KEY保存到memcache中
	 *
	 * @param key
	 * @param exp
	 * @param obj
	 * @return
	 */
	public boolean set(String key,Date exp,Object obj,DataResourceEnum dataResourceEnum){
		if(StringUtils.isEmpty(key)){
			return false;
		}
		if(obj==null){
			return true;
		}
		try{
			if (isCacheEnabled()) {
				boolean result = getMemCachedClient(dataResourceEnum).set(key, obj, exp);
				if (log.isDebugEnabled()) {
					log.debug("SET(enum) A OBJECT: KEY:" + key + ", exp:" + exp + ", result:" + result);
				}
				return result;
			}
			return true;
		}catch(Exception e) {
			log.error(e, e);
		}
		return false;
	}
	
	public boolean set(String key,Date exp,Object obj){
		return set(key,exp,obj,false);
	}

    /**
     * 存放没有时长限制的信息
     * @param key
     * @param object
     * @param isForSession
     * @return
     */
    public boolean setNoTimeLimit(String key, Object object, boolean isForSession) {
        if(StringUtils.isEmpty(key)){
            return false;
        }
        if(object == null){
            return true;
        }
//		Transaction t = getTransaction(isForSession, key + ":set");
		try{
            if (isCacheEnabled()) {
                boolean result = getMemCachedClient(isForSession).set(key, object);
                if(log.isDebugEnabled()) {
                    log.debug("SET A OBJECT: KEY:" + key + ", result:" + result);
                }
//				t.setStatus(Transaction.SUCCESS);
				return result;
            }
            return true;
		} catch (Exception e) {
//			Cat.logError(e);
//			t.setStatus(e);
			log.error("MemcachedUtil.setNoTimeLimit error:", e);
		} finally {
//			t.complete();
		}
		return false;
    }

	/**
	 * @deprecated 此方法严重影响流量，应该被废弃 <a>
	 * @author Brian
	 * 
	 * 保存key和描述信息；
	 * 
	 * @param key
	 * @param obj
	 * @return
	 */
	@Deprecated
	public boolean setWithDis(String key,int seconds,Object obj,String discript){
		if(StringUtils.isEmpty(key)){
			return false;
		}
		if(obj==null){
			return true;
		}
		try{
			if (isCacheEnabled()) {
				Date expDate = getDateAfter(seconds);
				//this.putKeyDisMap(key,discript);
				boolean result = memCachedClient.set(key, obj, expDate);
				if(log.isDebugEnabled()) {
					log.debug("SET A OBJECT: KEY:" + key + ", OBJ:" + obj + ", expDate:" + expDate) ;
				}
				return result;
			}
			return true;
		}catch(Exception e) {
			log.error(ExceptionFormatUtil.getTrace(e));
		}
		return false;
	}
	
	/**
	 * 把相应的Key值和描述存到此方法中；
	 * 
	 * @param key
	 * @param discript
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean putKeyDisMap(String key,String discript) {
		Map<String,String> memMap ;
		Object obj = memCachedClient.get(memcacheMapKey);
		if(obj == null) {
			memMap = new HashMap<String, String>();
		} else {
			memMap = (HashMap<String, String>) obj;
		}
		memMap.put(key, discript);
		memCachedClient.set(memcacheMapKey,memMap,getDateAfter(60*60*48));
		
		return true;
	}
	
	/**
	 * 放
	 * @param key
	 * @param obj
	 * @return
	 */
	public boolean set(String key, Object obj) {
		return set(key,ONE_HOUR,obj);
	}
	
	/**
	 * 取
	 * @param key
	 * @return
	 */
	public Object get(String key,boolean isForSession) {
//		Transaction t = getTransaction(isForSession, key + ":get");
		try{
			if (isCacheEnabled()) {
				Object obj = getMemCachedClient(isForSession).get(key);
				if(log.isDebugEnabled()) {
					log.debug("GET A OBJECT: KEY:" + key ) ;
				}
//				t.setStatus(Transaction.SUCCESS);
				return obj;
			}
		} catch (Exception e) {
//			Cat.logError(e);
//			t.setStatus(e);
			log.error("MemcachedUtil.get error:", e);
		} finally {
//			t.complete();
		}
		return null;
	}

	/**
	 * 取
	 * @param key
	 * @return
	 */
	public Object get(String key,DataResourceEnum dataResourceEnum) {
		try{
			if (isCacheEnabled()) {
				Object obj = getMemCachedClient(dataResourceEnum).get(key);
				if(log.isDebugEnabled()) {
					log.debug("GET(enum) A OBJECT: KEY:" + key ) ;
				}
				return obj;
			}
		}catch(Exception e) {
			log.error(e, e);
		}
		return null;
	}
	
	/**
	 * 变为泛型,避免处处都需要强制转换导致警告
	 * <功能详细描述>
	 * @param key
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings("unchecked")
    public <T> T  get(String key) {
		try
        {
            return (T)get(key,false);
        }
        catch (Exception e)//转型出错,返回空
        {
            return null;
        }
	}
	
	/**
	 * 判断key是否超出mencached的最大key长度
	 * @param key
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static final boolean keyIsNotTooLong(String key) 
	{
		try {
			String urlEcodeKey = URLEncoder.encode(key, "utf-8");
			return StringUtils.length(urlEcodeKey) < 250;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			return true;
		}
	}
	
	/**
	 * 取
	 * @param key
	 * @return
	 */
	public boolean remove(String key,boolean isForSession) {
		if(StringUtils.isEmpty(key)){
			return false;
		}
//		Transaction t = getTransaction(isForSession, key + ":remove");
		try{
			if (isCacheEnabled()) {
				if(log.isDebugEnabled()) {
					log.debug("delete memcached key: " + key);
				}
				if(!isForSession){
//					vstMemCachedClient.delete(key);
				}
				boolean isSuccess = getMemCachedClient(isForSession).delete(key);
//				t.setStatus(Transaction.SUCCESS);
				return isSuccess;
			}
		}catch(Exception e) {
//			Cat.logError(e);
//			t.setStatus(e);
			log.error("MemcachedUtil.remove error:", e);
		} finally {
//			t.complete();
		}
		return true;
	}
	public boolean remove(String key) {
		return remove(key,false);
	}

	/**
     * 获得当前开始活参数秒的时间日期
    * @Title: getDateAfter
    * @Description:
    * @param
    * @return Date    返回类型
    * @throws
     */
    public static Date getDateAfter(int second) {
        Calendar cal = Calendar.getInstance();
 		cal.add(Calendar.SECOND, second);
 		return cal.getTime();
 	}
    
    /**
     * 当前key对应的缓存是否存在
     * @param key
     * @return
     */
    public boolean keyExists(String key) {
    	return keyExists(key, false);
    }

	public boolean keyExists(String key,DataResourceEnum resource) {
		return getMemCachedClient(resource).keyExists(key);
	}

	public boolean remove(String key,DataResourceEnum resource) {
		return getMemCachedClient(resource).delete(key);
	}
    
    public boolean keyExists(String key, boolean isForSession) {
//		Transaction t = getTransaction(isForSession, key + ":keyExists");
		try {
			if (isCacheEnabled()) {
                boolean isSuccess = getMemCachedClient(isForSession).keyExists(key);
//				t.setStatus(Transaction.SUCCESS);
				return isSuccess;
			}
		} catch (Exception e) {
//			Cat.logError(e);
//			t.setStatus(e);
			log.error("MemcachedUtil.keyExists error:", e);
		} finally {
//			t.complete();
		}
		return false;
    }

    public long incr(String key) {
        return getMemCachedClient(false).incr(key);
    }

    public long incr(String key, long inc) {
        return getMemCachedClient(false).incr(key, inc);
    }

    public long decr(String key) {
        return getMemCachedClient(false).decr(key);
    }

    public long decr(String key, Long inc) {
        return getMemCachedClient(false).decr(key, inc);
    }

    public boolean storeCounter(String key, Long counter) {
        return getMemCachedClient(false).storeCounter(key, counter);
    }

    private String getProperty(String clusterName) {
        String prop = null;
//        if (isSweet) {
//            try {
//                prop = ZooKeeperConfigProperties.getProperties(clusterName);
//            } catch (Throwable t) {
//                log.warn(t);
//            }
//        } else {
		if(properties!=null){
			prop = properties.getProperty(clusterName);
		}
//        }
        return prop;
    }

    @SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		Map<String, String> map = (Map<String, String>) MemcachedUtil.getInstance().get("MEM_TEST_KEY_LL_6");
		if(map == null){
			map = new HashMap<String, String>();
		}

		for(int i=0;i<1;i++) {
			Random r =  new Random(System.currentTimeMillis());
			String key = "KEY_" + r.nextLong();
//			System.out.println("keys: " +key);

			byte[] bt = new byte[0];
			String value= new String(bt,"UTF-8");
			for(int j=0;j<10000;j++) {
				value= value + "qweqweqweqweqweqwewequyrqwieurpasjdflkasdfasdrwqioeurpqwerqweqwertyuiopqwertyuiopqwertyuiopqwertqqqq";
			}
			//System.out.println(key + " : " + MemcachedUtil.getInstance().get(key));
			map.put(key, value);
		}
		
		boolean a = MemcachedUtil.getInstance().set("MEM_TEST_KEY_LL_6", map);
		
		boolean b = MemcachedUtil.getInstance().set("MEM_TEST_KEY_LL_2", "2");

		
		System.out.println("keys: " + map.size());
		
		System.out.println(a + ", " + b);

	}
/**
 * add 方式存放，同步的
 * @param key
 * @param i
 * @param string
 * @return
 */
	public synchronized boolean addSynchronized(String key, int i, String string) {
		return add(key, getDateAfter(i), string,false);
	}
	public boolean add(String key,Date exp,Object obj,boolean isForSession){

		if(StringUtils.isEmpty(key)){
			return false;
		}
		if(obj==null){
			return true;
		}
		try{
			if (isCacheEnabled()) {
				boolean result = getMemCachedClient(false).add(key, obj, exp);
				if(log.isDebugEnabled()) {
					log.debug("add A OBJECT: KEY:" + key + ", OBJ:" + obj + ", exp:" + exp + ", result:" + result);
				}
				return result;
			}
			return true;
		}catch(Exception e) {
			log.error(e, e);
		}
		return false;
	
	}
	
	/**
	 * add 方式存放，同步的  Exception
	 * @param key
	 * @param i
	 * @param string
	 * @return
	 * @throws Exception 
	 */
		public synchronized boolean addSynchronizedWithException(String key, int i, String string) throws Exception {
			Date exp = getDateAfter(i);
			Object obj = string;
			
			if(StringUtils.isEmpty(key)){
				return false;
			}
			if(obj==null){
				return true;
			}
			try{
				if (isCacheEnabled()) {
					boolean result = getMemCachedClient(false).add(key, obj, exp);
					if(log.isDebugEnabled()) {
						log.debug("add A OBJECT: KEY:" + key + ", OBJ:" + obj + ", exp:" + exp + ", result:" + result);
					}
					return result;
				}
				return true;
			}catch(Exception e) {
				log.error(e, e);
				throw new Exception(e);
			}
		}
		 /**
	     * 获取计数器值 
	     * 获取key的计数器，如果不存在返回-1。
	     * @param key
	     * @param count
	     * @return
	     */
	    public long getCounter(String key) {
			try{
				return getMemCachedClient(false).getCounter(key);
			}catch(Exception e) {
				log.error(e);
			}
			return -1;
		}
	    
	    public boolean tryLock(String lockKey){
	    	return tryLock(lockKey,DEFAULT_LOCK_SECCONDS,DEFAULT_TRYLOCK_TIMEOUT_SECONDS);
	    }
	    
	    public boolean tryLock(String lockKey, int lockSec, int timeOutSec){
	    	MemCachedClient client=getMemCachedClient(false);
	    	
	    	long start=System.currentTimeMillis();
	    	while(true){
	    		boolean  locked=client.add(lockKey, lockKey, getDateAfter(lockSec));
	    		if(locked){
	    			return true;
	    		}else{
	    			long now=System.currentTimeMillis();
	    			long costed = now-start;
					if(costed>=timeOutSec*1000){
						Object lockValue = client.get(lockKey);
						log.info("MemcachedUtil tryLock :" + costed + ", LockKey: " + lockKey + ", LockValue: " + lockValue);
						return false;
					}
	    		}
	    	}
	    }

	/**
	 * 获取CAT Transaction
	 */
//	private Transaction getTransaction(boolean isForSession, String methodKey) {
//		String serverName = "dataServer";
//		if (isForSession) {
//			serverName = "sessionServer";
//		}
//		return Cat.newTransaction("Cache.memcached-" + serverName, methodKey);
//	}
}
