
/**  
 * @Title:  Extractor.java   
 * @Package com.webrelax.service   
 * @Description:    TODO()   
 * @author: Lian
 * @date:   2018年8月14日 下午5:06:09   
 * @version V2.0  
 */
    
package com.webrelax.service;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import com.webrelax.dao.ApiDao;
import com.webrelax.entity.Api;
import com.webrelax.entity.App;
import com.webrelax.exception.HinDroidException;

public class Extractor {
	//训练集和测试集
	private String malwarePath=null;
	private String benignPath=null;
	private String malwareDecompilePath=null;
	private String benignDecompilePath=null;
	//存关系矩阵的路径
	private String malwareMatrixPath=null;
	private String benignMatrixPath=null;
	//检测集
	private String detectDataPath=null;
	private String detectDecompilePath=null;
	private String detectMatrixPath=null;
	
	private String mrmrOutputPath=null;
	private String dataType;//训练集、测试集、检测集
	
	private App app=null;

	//三个数据集中是否输出AB矩阵，只有训练集加了数据才置为true
	private static boolean outTrainABMatrix;
	private static boolean outTestABMatrix;
	private static boolean outDetectABMatrix;
	public void setDataType(String dataType) throws HinDroidException{
		this.dataType=dataType;
		String dataPath=null;
		 File file=null;
		if("train".equals(dataType)) {
			dataPath="dataset"+File.separator+"traindata";
		}else if("test".equals(dataType)){
			dataPath="dataset"+File.separator+"testdata";
		}else if("detect".equals(dataType)) {
			dataPath="dataset"+File.separator+"detectdata";
		}else {
			throw new HinDroidException("无此数据路径");
		}
		if(!"detect".equals(dataType)) {
			malwarePath=dataPath+File.separator+"malwaredata";
			benignPath=dataPath+File.separator+"benigndata";
			malwareDecompilePath=dataPath+File.separator+"malwaredecompile";
			benignDecompilePath=dataPath+File.separator+"benigndecompile";
			malwareMatrixPath=dataPath+File.separator+"malwarematrix";
			benignMatrixPath=dataPath+File.separator+"benignmatrix";
			
			file=new File(malwarePath);
			if(!file.exists()) {
				file.mkdirs();
			}
			file=new File(benignPath);
			if(!file.exists()) {
				file.mkdirs();
			}
			file=new File(malwareDecompilePath);
			if(!file.exists()) {
				file.mkdirs();
			}
			file=new File(benignDecompilePath);
			if(!file.exists()) {
				file.mkdirs();
			}
			file =new File(malwareMatrixPath);
			if(!file.exists()) {
				file.mkdirs();
			}
			file =new File(benignMatrixPath);
			if(!file.exists()) {
				file.mkdirs();
			}
		}else {
			detectDataPath=dataPath+File.separator+"data";
			detectDecompilePath=dataPath+File.separator+"decompile";
			detectMatrixPath=dataPath+File.separator+"matrix";
			file=new File(detectDataPath);
		    if(!file.exists()) {
		    	file.mkdirs();
			}
			file=new File(detectDecompilePath);
			if(!file.exists()) {
				file.mkdirs();
			}
			file=new File(detectMatrixPath);
		    if(!file.exists()) {
		    	file.mkdirs();
			}
		}
		
		mrmrOutputPath="output"+File.separator+"mrmr";
		file=new File(mrmrOutputPath);
	    if(!file.exists()) {
	    	file.mkdirs();
		}
	}
	
	public void decompilation() {
		if("detect".equals(dataType)) {
			ApkTool.decompilationAll(detectDataPath,detectDecompilePath,0,"detect");
		}else if("train".equals(dataType)){
			ApkTool.decompilationAll(malwarePath,malwareDecompilePath,1,"train");
			ApkTool.decompilationAll(benignPath,benignDecompilePath,-1,"train");
		}else if("test".equals(dataType)) {
			ApkTool.decompilationAll(malwarePath,malwareDecompilePath,1,"test");
			ApkTool.decompilationAll(benignPath,benignDecompilePath,-1,"test");
		}
	}
	/**
	 * @Title: readFilesAndStore
	 * @Description: TODO(读取.smali文件，并且存储提取到的API)
	 * @param  filePath 该文件路径
	 * @return void    返回类型
	 * @throws
	 */
	
	private void readFilesAndStore(String filePath,String appName) throws IOException, SQLException{
		
		List<HashSet<Api>> commonBlockApis = app.getCommonBlockApis();
		Map<String, Api> containApis = app.getContainApis();
		HashSet<Api> apis=null;
		ApiDao apiDao=new ApiDao();
		
		FileInputStream fis=new FileInputStream(filePath);
		InputStreamReader isr=new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
        for (String line = br.readLine(); line != null; line = br.readLine()){
        	if(line.startsWith(".method")) {
        		apis=new HashSet<Api>();
        		continue;
        	}
        	else if(line.startsWith(".end method")) {
        		//相同块的api
        		if(apis.size()>=1)
        			commonBlockApis.add(apis);
        		apis=null;
        		continue;
        	}
        	if(line.indexOf('<')!=-1)continue;
        	line=line.trim();
        	if(line.startsWith("invoke")){
        		int begin=line.indexOf('L');
        		int end=line.indexOf('(');
        		if(begin+1>end){
        			//System.out.println(line+"***");
        			continue;
        		}
        		String apiName=line.substring(begin+1,end);
        		
        		if(!apiName.startsWith("java")&&!apiName.startsWith("org")){       		
        			continue;
        		}
        		end=apiName.indexOf(";");
				String packName=apiName.substring(0,end);
				String invoke=null;
				if(line.startsWith("invoke-direct"))
					invoke="invoke-direct";
				else if(line.startsWith("invoke-virtual"))
					invoke="invoke-virtual";
				else if(line.startsWith("invoke-static"))
					invoke="invoke-static";
				else if(line.startsWith("invoke-super"))
					invoke="invoke-super";
				else if(line.startsWith("invoke-interface"))
					invoke="invoke-interface";
				containApis.put(apiName, new Api(apiName,packName,invoke));
				apis.add(new Api(apiName,packName,invoke));
				if("train".equals(dataType)) {
					
					Api api=apiDao.findApiByName(apiName);
					if(api==null) {
						apiDao.addApi(new Api(apiName,packName,invoke));
					}
				}
        	}
        }
        br.close();
	}
	/**
	 * @Title: iterateFileDir
	 * @Description: TODO(递归遍历反编译后的samli目录下的所有文件，当查到该文件是.smali就提取其中的API)
	 * @param dir    参数
	 * @return void    返回类型
	 * @throws
	 */
	private void iterateFileDir(String dir,String appName) {
		File file=new File(dir);
		File[] files=file.listFiles();
		for(int i=0;i<files.length;i++){
			String fileName=files[i].getName();
			if(files[i].isDirectory()){
				iterateFileDir(dir+File.separator+fileName,appName);
			}
			else if(files[i].isFile()){
				if(fileName.endsWith(".smali")){
					try {
						readFilesAndStore(dir+File.separator+fileName,appName);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
				
		}
	}
	public void extractApi() {
		File file;
		File[] files;
		if("detect".equals(dataType)) {
			file=new File(detectDecompilePath);
			files=file.listFiles();
			for(int i=0;i<files.length;i++) {
				String dirName=files[i].getName();
//				apps.put(dirName, new App(0,dirName));
				//如果提取过，就跳过
				File f=new File(detectMatrixPath+File.separator+dirName);
				if(f.exists())
					continue;
				else {
					f.mkdirs();
				}
				app=new App();
				iterateFileDir(detectDecompilePath+File.separator+dirName,dirName);
				ABCsv.outputABCsv(app,detectMatrixPath+File.separator+dirName);
			}
		}else {
			file=new File(malwareDecompilePath);
			files=file.listFiles();
			for(int i=0;i<files.length;i++) {
				String dirName=files[i].getName();
				File f=new File(malwareMatrixPath+File.separator+dirName);
				if(f.exists())
					continue;
				else {
					f.mkdirs();
				}
				//如果是训练集，并加了数据，才输出AB_Matrix
				if("train".equals(dataType)) {
					Extractor.outTrainABMatrix=true;
					Extractor.outTestABMatrix=true;
					Extractor.outDetectABMatrix=true;
				}
				app=new App();
				iterateFileDir(malwareDecompilePath+File.separator+dirName,dirName);
				ABCsv.outputABCsv(app,malwareMatrixPath+File.separator+dirName);
			}
			
			
			file=new File(benignDecompilePath);
			files=file.listFiles();
			for(int i=0;i<files.length;i++) {
				String dirName=files[i].getName();
				File f=new File(benignMatrixPath+File.separator+dirName);
				if(f.exists())
					continue;
				else {
					f.mkdirs();
				}
				//如果是训练集，并加了数据，才输出AB_Matrix
				if("train".equals(dataType)) {
					Extractor.outTrainABMatrix=true;
					Extractor.outTestABMatrix=true;
					Extractor.outDetectABMatrix=true;
				}
				app=new App();
				iterateFileDir(benignDecompilePath+File.separator+dirName,dirName);
				ABCsv.outputABCsv(app,benignMatrixPath+File.separator+dirName);
			}
		}
	}
	public void mrmr() {
		if(!"train".equals(dataType)||!Extractor.outTrainABMatrix==true) {
			return;
		}
		Mrmr mrmr = new Mrmr(mrmrOutputPath);
		mrmr.mrmrAlgorithm(malwareMatrixPath,benignMatrixPath);
		
	}
	public void outputAB_MatrixCsv() {
		if(!"detect".equals(dataType)) {
			File malware=new File(malwareMatrixPath);
			File[] malwareFiles=malware.listFiles();
			File benign=new File(benignMatrixPath);
			File[] benignFiles=benign.listFiles();
			boolean flag=true;
			
			if("train".equals(dataType)) {
				flag=Extractor.outTrainABMatrix;
			}
			if("test".equals(dataType)) {
				flag=Extractor.outTestABMatrix;
			}
			System.out.println("flag:"+flag);
			for(int i=0;i<malwareFiles.length;i++) {
				ABCsv.outputA_MatrixCsv(malwareMatrixPath+File.separator+malwareFiles[i].getName(),flag);
				ABCsv.outputB_MatrixCsv(malwareMatrixPath+File.separator+malwareFiles[i].getName(),flag);
			}
			for(int i=0;i<benignFiles.length;i++) {
				ABCsv.outputA_MatrixCsv(benignMatrixPath+File.separator+benignFiles[i].getName(),flag);
				ABCsv.outputB_MatrixCsv(benignMatrixPath+File.separator+benignFiles[i].getName(),flag);
			}
			if("train".equals(dataType)) {
				Extractor.outTrainABMatrix=false;
			}
			if("test".equals(dataType)) {
				Extractor.outTestABMatrix=false;
			}
			
		}else {
			File malware=new File(detectMatrixPath);
			File[] files=malware.listFiles();
			boolean flag=Extractor.outDetectABMatrix;
			for(int i=0;i<files.length;i++) {
				ABCsv.outputA_MatrixCsv(detectMatrixPath+File.separator+files[i].getName(),flag);
				ABCsv.outputB_MatrixCsv(detectMatrixPath+File.separator+files[i].getName(),flag);
			}
			Extractor.outDetectABMatrix=false;
		}
	}
}

