
/**  
 * @Title:  Test.java   
 * @Package com.webrelax.test   
 * @Description:    TODO()   
 * @author: Lian
 * @date:   2018年8月13日 下午3:04:17   
 * @version V1.0  
 */
    
package com.webrelax.test;

import java.io.File;
import java.io.IOException;

import com.webrelax.exception.HinDroidException;
import com.webrelax.service.Extractor;
import com.webrelax.util.Encrypt;

import brut.androlib.AndrolibException;
import brut.androlib.ApkDecoder;
import brut.directory.DirectoryException;

/**   
 * @ClassName:  Test   
 * @Description:TODO()       
 */

public class Test {

	public static void main(String[] args) {
		try {
			Extractor ext= new Extractor("test");
			ext.decompilation();
		} catch (HinDroidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

}
