
/**  
 * @Title:  Main.java   
 * @Package com.webrelax.test   
 * @Description:    TODO()   
 * @author: Lian
 * @date:   2018年8月15日 下午3:14:33   
 * @version V1.0  
 */
    
package com.webrelax.test;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.webrelax.entity.Api;
import com.webrelax.exception.HinDroidException;
import com.webrelax.service.Extractor;
import com.webrelax.service.MetaPath;
import com.webrelax.util.CmdUtil;

/**   
 * @ClassName:  Main   
 * @Description:TODO()       
 */

public class Main {
	private static Extractor ext=null;
	
	public  void svmTrain(int kernelNum) {
		String kernelfilePath="output"+File.separator+"PrecomputedKernels";
		String outputPath="output"+File.separator+"svm";
		File file=new File(kernelfilePath);
		File outFile=new File(outputPath);
		if(!outFile.exists()) {
			outFile.mkdirs();
		}
		String command="svm-train.exe -s 0 -h 0 -m 400 -o 2.0 -a "+kernelNum+" -c 10.0 -l 1.0 -f 0 -j 1 -g 3 -k "
				+ file.getAbsolutePath()+File.separator+"kernelfile "
				+ file.getAbsolutePath()+File.separator+"y_train "
				+ file.getAbsolutePath()+File.separator+"model_file "
				+ ">"+outFile.getAbsolutePath()+File.separator+"svm_train.output";
		CmdUtil.cmd("exe", command);
	}
	private  void svmTest() {
		String kernelfilePath="output"+File.separator+"PrecomputedKernels";
		String outputPath="output"+File.separator+"svm";
		File file=new File(kernelfilePath);
		File outFile=new File(outputPath);
		if(!outFile.exists()) {
			outFile.mkdirs();
		}
		String command="SVM_Predict.exe "
				+ file.getAbsolutePath()+File.separator+"y_test "
				+ file.getAbsolutePath()+File.separator+"model_file "
				+ file.getAbsolutePath()+File.separator+"prediction"
				+ " >"+outFile.getAbsolutePath()+File.separator+"svm_test.output";
		CmdUtil.cmd("exe", command);
	}
	public  void train() throws HinDroidException {
		System.out.println("------------train------------");
		ext.setDataType("train");
		System.out.println("反编译...");
		ext.decompilation();
		System.out.println("反编译完成");
		System.out.println("提取api...");
		ext.extractApi();
		System.out.println("提取完成");
		System.out.println("mrmr算法...");
		ext.mrmr();
		System.out.println("mrmr算法完成");
		System.out.println("生成ABcsv文件...");
		ext.outputAB_MatrixCsv();
		System.out.println("生成ABcsv文件完成");
	
		MetaPath mp=new MetaPath("train");
		mp.dealMetaPath();
		svmTrain(2);
		System.out.println("------------train end------------");
	}
	public  void test() throws HinDroidException {
		System.out.println("------------test------------");
		ext.setDataType("test");
		System.out.println("反编译...");
		ext.decompilation();
		System.out.println("反编译完成");
		System.out.println("提取api...");
		ext.extractApi();
		System.out.println("提取完成");
		System.out.println("生成ABcsv文件...");
		ext.outputAB_MatrixCsv();
		System.out.println("生成ABcsv文件完成");
		
		System.out.println("生成交换矩阵...");
		MetaPath mp=new MetaPath("test");
		mp.dealMetaPath();
		System.out.println("生成交换矩阵完成");
		svmTest();
		System.out.println("------------test end------------");
	}
	public void detect() throws HinDroidException {
		ext.setDataType("detect");
		System.out.println("反编译...");
		ext.decompilation();
		System.out.println("反编译完成");
		System.out.println("提取api...");
		ext.extractApi();
		System.out.println("提取完成");
		System.out.println("生成ABcsv文件...");
		ext.outputAB_MatrixCsv();
		System.out.println("生成ABcsv文件完成");
		
		MetaPath mp=new MetaPath("detect");
		mp.dealMetaPath();
		svmTest();
	}
	public static void run()  {
		ext=new Extractor();
		Main obj=new Main();
		try {
			obj.train();
			obj.test();
		} catch (HinDroidException e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		
		run();
		
	}

}
