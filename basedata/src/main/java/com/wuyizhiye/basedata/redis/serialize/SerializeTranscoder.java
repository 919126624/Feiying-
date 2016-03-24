package com.wuyizhiye.basedata.redis.serialize;

import java.io.Closeable;

import org.apache.log4j.Logger;

/**
 * @ClassName SerializeTranscoder
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public abstract class SerializeTranscoder {
	protected static Logger logger = Logger.getLogger(SerializeTranscoder.class);
	  
	  public abstract byte[] serialize(Object value);
	  
	  public abstract Object deserialize(byte[] in);
	  
	  public void close(Closeable closeable) {
	    if (closeable != null) {
	      try {
	        closeable.close();
	      } catch (Exception e) {
	         logger.info("Unable to close " + closeable, e); 
	      }
	    }
	  }
}
