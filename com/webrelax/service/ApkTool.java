
/**  
 * @Title:  ApkTool.java   
 * @Package com.webrelax.util   
 * @Description:    TODO()   
 * @author: Lian
 * @date:   2018年8月13日 下午3:43:25   
 * @version V1.0  
 */
    
package com.webrelax.service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import com.webrelax.dao.AppDao;
import com.webrelax.entity.App;

import brut.androlib.AndrolibException;
import brut.androlib.ApkDecoder;
import brut.directory.DirectoryException;

/**   
 * @ClassName:  ApkTool   
 * @Description:TODO()       
 */

public class ApkTool {
	/**
	 * @Title: decompilation
	 * @Description: TODO(反编译一个文件)
	 * @param filePath ../xx.apk apk文件
	 * @param outPath  ../xx 反编译后的文件
	 * @return void    返回类型
	 */
	public static void decompilation(String filePath,String outPath) {
//		System.out.println("filePath: "+filePath);
//		System.out.println("outPath: "+outPath);
		File inFile = new File(filePath);
		ApkDecoder decoder = new ApkDecoder();
		try {
			decoder.setOutDir(new File(outPath));
			decoder.setApkFile(inFile);
			decoder.decode();
		} catch (AndrolibException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DirectoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @Title: decompilationAll
	 * @Description: TODO(反编译整个文件夹)
	 * @param inDir 此文件夹下所有.apk被反编译
	 * @param outDir //输出路径
	 * @param appType    app类型，-1，0，1
	 * @param dataType 数据类型 train,test,detect
	 * @return void    返回类型
	 */
	public static void decompilationAll(String inDir,String outDir,int appType,String dataType) {
		AppDao appDao=new AppDao();
		File file=new File(inDir);
		File[] files=file.listFiles();
		for(int i=0;i<files.length;i++) {
			if(files[i].isFile()==true){
				String fileName=files[i].getName();
				if(fileName.endsWith(".apk")) {
					String subFileName=fileName.substring(0, fileName.length()-4);
					File outputPath=new File(outDir+File.separator+subFileName);
					if(!outputPath.exists())
						decompilation(inDir+File.separator+fileName, outDir+File.separator+subFileName);
					//如果是训练数据，才存储数据库
					if("train".equals(dataType)) {
						AppDao appdao=new AppDao();
						App app=null;
						try {
							app=appdao.findAppByName(subFileName);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(app==null) {
							app=new App();
							app.setName(subFileName);
							app.setType(appType);
							try {
								appDao.addApp(app);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
					}
					
				}
			}
		}
	}
}
