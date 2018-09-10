
/**  
 * @Title:  Mrmr.java   
 * @Package com.webrelax.service   
 * @Description:    TODO()   
 * @author: Lian
 * @date:   2018年8月15日 下午8:29:38   
 * @version V1.0  
 */
    
package com.webrelax.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.webrelax.dao.ApiDao;
import com.webrelax.entity.Api;
import com.webrelax.util.CmdUtil;
import com.webrelax.util.CsvWriter;


/**   
 * @ClassName:  Mrmr   
 * @Description:TODO()       
 */

public class Mrmr {
	
	private String outputPath;
	private String exePath="exe";
	public Mrmr(String outputPath) {
		this.outputPath=outputPath;
	}
	public Mrmr() {}
	
	/**@Title: generateCsvFile
	 * @Description: TODO()
	 * @param malwareMatrixPath
	 * @param benignMatrixPath    参数
	 * @return void    返回类型
	 */
	private void generateCsvFile(String malwareMatrixPath,String benignMatrixPath) {
		File file=new File(outputPath+File.separator+"mrmr.csv");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		ApiDao apidao=new ApiDao();
		CsvWriter cw=new CsvWriter(outputPath+File.separator+"mrmr.csv",',',Charset.forName("UTF-8"));
		List<Api> apis=null;
		try {
			apis=apidao.findAllApi();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(apis==null)return;
		int count=apis.size();
		String[] feature=new String[count+1];
		String[] csvValues=new String[count+1];
		csvValues[0]="class";
		//第0行
		int cnt=1;
		for(Api api:apis) {
			feature[cnt]=api.getApiName();
			csvValues[cnt++]=new Integer(api.getId()).toString();
//			if(cnt<=10)System.out.println(api.getApiName());
		}
		try {
			cw.writeRecord(csvValues);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writeCsvFile(malwareMatrixPath, cw, feature, csvValues,1);
		writeCsvFile(benignMatrixPath, cw, feature, csvValues,-1);
		cw.close();
	}
		
	/**@Title: readCsvFile
	 * @Description: TODO()
	 * @param matirxFilePath
	 * @param cw
	 * @param feature
	 * @param csvValues    参数
	 * @return void    返回类型
	 **/
	private void writeCsvFile(String matirxFilePath, CsvWriter cw, String[] feature, String[] csvValues,int appType) {
		File appFile =new File(matirxFilePath);
		File[] appFiles=appFile.listFiles();
		for(int i=0;i<appFiles.length;i++) {
			String fileName=appFiles[i].getName();
			Map<String, Api> readAcsv=null;
			try {
				readAcsv = ABCsv.readACsv(matirxFilePath+File.separator+fileName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(readAcsv!=null) {
				csvValues[0]=new Integer(appType).toString();
				for(int j=1;j<feature.length;j++) {
					if(readAcsv.containsKey(feature[j])) {
						csvValues[j]="1";
					}else {
						csvValues[j]="0";
					}
				}
				try {
					cw.writeRecord(csvValues);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	public  Set<Api> getNewApi(String filePath) throws IOException{
		if(filePath==null)
			filePath=outputPath+File.separator+"mrmr.out";
		//读取mrmr.out
		ApiDao apidao=new ApiDao();
		Set<Api> apis=new HashSet<Api>();
		File file=new File(filePath);
		if(!file.exists())return null;
		BufferedReader br=new BufferedReader(new FileReader(file));
		String line;
		boolean flag=false;
		for( line=br.readLine();line!=null;line=br.readLine()){
			line=line.trim();
			if(line.equals("*** mRMR features ***")){
				flag=true;
				continue;
			}
			if(flag==false)continue;
			if(line.equals("Order 	 Fea 	 Name 	 Score"))
				continue;
			if(line.equals("*** This program and the respective minimum Redundancy Maximum Relevance (mRMR)"))
				break;
			if(flag){
				String[] str=line.split("\\s+");
				if(str.length>=2)
				{
					Api api=null;
					try {
						api=apidao.findApiById(new Integer(str[2]));
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(api!=null)
						apis.add(api);
				}
	
			}
		}
		br.close();
		return apis;
	}
	public void mrmrAlgorithm(String malwareMatrixPath,String benignMatrixPath) {

		generateCsvFile(malwareMatrixPath,benignMatrixPath);
		File file=new File(outputPath);
		String inFile=file.getAbsolutePath()+File.separator+"mrmr.csv";
		String outFile=file.getAbsolutePath()+File.separator+"mrmr.out";
		String command="mrmr_win32.exe ./mrmr -v 50000 -i "+inFile+" -n 200 >"+outFile;
		CmdUtil.cmd(exePath, command);
	}
	
}
