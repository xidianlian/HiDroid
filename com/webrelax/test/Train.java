
/**  
 * @Title:  Train.java   
 * @Package com.webrelax.test   
 * @Description:    TODO()   
 * @author: Lian
 * @date:   2018年8月14日 下午7:40:14   
 * @version V1.0  
 */
    
package com.webrelax.test;
import java.io.File;

import com.webrelax.exception.HinDroidException;
import com.webrelax.service.Extractor;
import com.webrelax.service.MetaPath;
import com.webrelax.util.CmdUtil;
/**   
 * @ClassName:  Train   
 * @Description:TODO()       
 */
public class Train {
	public static void SvmTrain(int kernelNum) {
		String kernelfilePath="output"+File.separator+"PrecomputedKernels";
		String outputPath="output"+File.separator+"svm"+File.separator+"svm_train.output";
		File file=new File(kernelfilePath);
		File outFile=new File(outputPath);
		String command="svm-train.exe -s 0 -h 0 -m 400 -o 2.0 -a "+kernelNum+" -c 10.0 -l 1.0 -f 0 -j 1 -g 3 -k "
				+ file.getAbsolutePath()+File.separator+"kernelfile "
				+ file.getAbsolutePath()+File.separator+"y_train "
				+ file.getAbsolutePath()+File.separator+"model_file "
				+ ">"+outFile.getAbsolutePath();
		CmdUtil.cmd("exe", command);
	}
	public static void main(String[] args) {
//		Extractor ext=null;
//		try {
//			ext= new Extractor("train");
//			System.out.println("反编译...");
//			ext.decompilation();
//			System.out.println("反编译完成");
//			System.out.println("提取api...");
//			ext.extractApi();
//			System.out.println("提取完成");
//			System.out.println("mrmr算法...");
//			ext.mrmr();
//			System.out.println("mrmr算法完成");
//			System.out.println("生成AB矩阵...");
//			ext.outputAB_MatrixCsv();
//			System.out.println("生成AB矩阵完成");
//		} catch (HinDroidException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println(e.getMessage());
//		}
		MetaPath mp=new MetaPath("train");
		mp.dealMetaPath();
		SvmTrain(2);
		
	}
}
