
/**  
 * @Title:  HinDroidException.java   
 * @Package com.webrelax.exception   
 * @Description:    TODO()   
 * @author: Lian
 * @date:   2018年8月14日 下午7:43:43   
 * @version V1.0  
 */
    
package com.webrelax.exception;


/**   
 * @ClassName:  HinDroidException   
 * @Description:TODO()       
 */

public class HinDroidException extends Exception{
	    /**
	     * @Fields field:field:{todo}()
	     */
	private static final long serialVersionUID = 1L;

		/**
	     * 创建一个新的实例 HinDroidException.
	     *
	     */
	public HinDroidException() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	    /**
	     * 创建一个新的实例 HinDroidException.
	     *
	     * @param message
	     * @param cause
	     * @param enableSuppression
	     * @param writableStackTrace
	     */
	    
	public HinDroidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	
	    /**
	     * 创建一个新的实例 HinDroidException.
	     *
	     * @param message
	     * @param cause
	     */
	    
	public HinDroidException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	
	    /**
	     * 创建一个新的实例 HinDroidException.
	     *
	     * @param message
	     */
	    
	public HinDroidException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	
	    /**
	     * 创建一个新的实例 HinDroidException.
	     *
	     * @param cause
	     */
	    
	public HinDroidException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
