
/**  
 * @Title:  Train.java   
 * @Package com.webrelax.test   
 * @Description:    TODO()   
 * @author: Lian
 * @date:   2018年8月14日 下午7:40:14   
 * @version V1.0  
 */
    
package com.webrelax.test;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.webrelax.dao.AppDao;
import com.webrelax.entity.Api;
import com.webrelax.entity.App;
import com.webrelax.exception.HinDroidException;
import com.webrelax.service.Extractor;
import com.webrelax.service.Mrmr;
import com.webrelax.util.Encrypt;

/**   
 * @ClassName:  Train   
 * @Description:TODO()       
 */

public class Train {
	public static void main(String[] args) {
		Extractor ext=null;
		try {
			ext= new Extractor("train");
			System.out.println("反编译...");
			ext.decompilation();
			System.out.println("反编译完成");
			System.out.println("提取api...");
			ext.extractApi();
			System.out.println("提取完成");
			System.out.println("mrmr算法...");
			ext.mrmr();
			System.out.println("mrmr算法完成");
			
		} catch (HinDroidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
}
