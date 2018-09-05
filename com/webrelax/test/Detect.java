
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
import com.webrelax.service.MetaPath;

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
		Extractor ext=null;
		try {
			ext= new Extractor("detect");
			System.out.println("反编译...");
			ext.decompilation();
			System.out.println("反编译完成");
			System.out.println("提取api...");
			ext.extractApi();
			System.out.println("提取完成");
			System.out.println("生成AB矩阵...");
			ext.outputAB_MatrixCsv();
			System.out.println("生成AB矩阵完成");
		} catch (HinDroidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
//		MetaPath mp=new MetaPath("detect");
//		mp.dealMetaPath();

	}

}
