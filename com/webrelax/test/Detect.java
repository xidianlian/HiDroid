
/**  
 * @Title:  Detect.java   
 * @Package com.webrelax.test   
 * @Description:    TODO()   
 * @author: Lian
 * @date:   2018年8月14日 下午7:57:15   
 * @version V1.0  
 */
    
package com.webrelax.test;

import com.webrelax.exception.HinDroidException;
import com.webrelax.service.Extractor;

/**   
 * @ClassName:  Detect   
 * @Description:TODO()       
 */

public class Detect {

	/**@Title: main
	 * @Description: TODO()
	 * @param args    参数
	 * @return void    返回类型
		 */

	public static void main(String[] args) {
		try {
			Extractor ext= new Extractor("detect");
			ext.decompilation();
		} catch (HinDroidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

	}

}
