
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
import java.text.SimpleDateFormat;
import java.util.Date;

import com.webrelax.exception.HinDroidException;
import com.webrelax.service.Extractor;
import com.webrelax.service.MetaPath;
import com.webrelax.util.CmdUtil;
import com.webrelax.util.Encrypt;

import brut.androlib.AndrolibException;
import brut.androlib.ApkDecoder;
import brut.directory.DirectoryException;

/**   
 * @ClassName:  Test   
 * @Description:TODO()       
 */

public class Test {
	    
	private static void svmTest() {
		String kernelfilePath="output"+File.separator+"PrecomputedKernels";
		String outputPath="output"+File.separator+"svm"+File.separator+"svm_test.output";
		File file=new File(kernelfilePath);
		File outFile=new File(outputPath);
		String command="SVM_Predict.exe "
				+ file.getAbsolutePath()+File.separator+"y_test "
				+ file.getAbsolutePath()+File.separator+"model_file "
				+ file.getAbsolutePath()+File.separator+"prediction"
				+ ">"+outFile.getAbsolutePath();
		CmdUtil.cmd("exe", command);
	}
	public static void main(String[] args) {
//		Extractor ext=null;
//		try {
//			ext= new Extractor("test");
//			System.out.println("反编译...");
//			ext.decompilation();
//			System.out.println("反编译完成");
//			System.out.println("提取api...");
//			ext.extractApi();
//			System.out.println("提取完成");
//			System.out.println("生成AB矩阵...");
//			ext.outputAB_MatrixCsv();
//			System.out.println("生成AB矩阵完成");
//		} catch (HinDroidException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println(e.getMessage());
//		}
//		MetaPath mp=new MetaPath("test");
//		mp.dealMetaPath();
		svmTest();
	}

	
	

}
