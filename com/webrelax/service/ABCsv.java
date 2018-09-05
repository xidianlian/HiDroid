
/**  
 * @Title:  RelationMatrix.java   
 * @Package com.webrelax.service   
 * @Description:    TODO()   
 * @author: Lian
 * @date:   2018年8月16日 下午5:19:00   
 * @version V1.0  
 */
    
package com.webrelax.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.webrelax.entity.Api;
import com.webrelax.entity.App;
import com.webrelax.util.CsvReader;
import com.webrelax.util.CsvWriter;
import com.webrelax.util.Matrix;
/**   
 * @ClassName:  RelationMatrix   
 * @Description:TODO()       
 */
public class ABCsv {
	private static final int FEATURE_SIZE=200;
	public static void outputABCsv(App app,String outputPath) {
		Map<String, Api> containApis = app.getContainApis();
		String[] csvValues=new String[containApis.size()];
		CsvWriter cw=new CsvWriter(outputPath+File.separator+"A.csv",',',Charset.forName("UTF-8"));
		int cnt=0;
		for(Api api:containApis.values()) {
			csvValues[cnt++]=api.getApiName()+":"+api.getPackName()+":"+api.getInvokeMethod();
		}
		try {
			cw.writeRecord(csvValues);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cw.close();
		
		List<HashSet<Api>> commonBlockApis = app.getCommonBlockApis();
		cw=new CsvWriter(outputPath+File.separator+"B.csv",',',Charset.forName("UTF-8"));
		for(HashSet<Api> block:commonBlockApis) {
			String[] out=new String[block.size()];
			int num=0;
			for(Api api:block) {
				out[num++]=api.getApiName()+":"+api.getPackName()+":"+api.getInvokeMethod();
			}
			try {
				cw.writeRecord(out);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		cw.close();
	}
	public static Map<String, Api> readACsv(String csvFilePath) throws IOException {
		
		Map<String, Api> apis=new HashMap<String,Api>();
		CsvReader csvReader = new CsvReader(csvFilePath+File.separator+"A.csv");
		while (csvReader.readRecord()){
		    // 读一整行
			String line=csvReader.getRawRecord();
			String[] res=line.split(",");
			
			for(int i=0;i<res.length;i++) {
				Api api=new Api();
				String[] str=res[i].split(":");
				api.setApiName(str[0]);
				api.setPackName(str[1]);
				api.setInvokeMethod(str[2]);
				apis.put(str[0], api);
			}
		}
		csvReader.close();
		return apis;
	}
	public static List<HashSet<Api>> readBCsv(String csvFilePath) throws IOException{
		List<HashSet<Api>> res=new ArrayList<HashSet<Api>>();
		CsvReader csvReader = new CsvReader(csvFilePath+File.separator+"B.csv");
		while (csvReader.readRecord()){
		    // 读一整行
			String line=csvReader.getRawRecord();
			HashSet<Api>s=new HashSet<Api>();
			String[] str=line.split(",");
			for(int i=0;i<str.length;i++) {
				String[] a=str[i].split(":");
				Api api=new Api();
				api.setApiName(a[0]);
				api.setPackName(a[1]);
				api.setInvokeMethod(a[2]);
				s.add(api);
			}
			res.add(s);
		}
		csvReader.close();
		return res;
	}
	public static void outputA_MatrixCsv(String inputFilePath) {
		File file1=new File("output"+File.separator+"mrmr"+File.separator+"mrmr.out");
		File file2=new File(inputFilePath+File.separator+"A.csv");
		if(!file1.exists()||!file2.exists())return;
		try {
			
			Map<String, Api> apis = readACsv(inputFilePath);
			Mrmr mrmr=new Mrmr();
			Set<Api> newApi = mrmr.getNewApi("output"+File.separator+"mrmr"+File.separator+"mrmr.out");
			CsvWriter cw=new CsvWriter(inputFilePath+File.separator+"A_Matrix.csv",',',Charset.forName("UTF-8"));
			String[] csvValues=new String[newApi.size()];
			int cnt=0;
			for(Api api:newApi) {
				if(apis.containsValue(api)) {
					csvValues[cnt++]="1";
				}else {
					csvValues[cnt++]="0";
				}
			}
			cw.writeRecord(csvValues);
			cw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void outputB_MatrixCsv(String inputFilePath) {
		File file1=new File("output"+File.separator+"mrmr"+File.separator+"mrmr.out");
		File file2=new File(inputFilePath+File.separator+"B.csv");
		if(!file1.exists()||!file2.exists())return;
		try {
			List<HashSet<Api>> blocks = readBCsv(inputFilePath);
			Mrmr mrmr=new Mrmr();
			Set<Api> newApi = mrmr.getNewApi("output"+File.separator+"mrmr"+File.separator+"mrmr.out");
			CsvWriter cw=new CsvWriter(inputFilePath+File.separator+"B_Matrix.csv",',',Charset.forName("UTF-8"));
			
			Matrix B_Matrix=new Matrix(newApi.size(),newApi.size());
			String[] csvValues=new String[newApi.size()];
			int row=0;
			int col=0;
			for(Api api1:newApi) {
				col=0;
				for(Api api2:newApi) {
					if(api1.equals(api2)) {
						B_Matrix.setX(row, col, 1);
						col++;
						continue;
					}
					B_Matrix.setX(row, col, 0);
					for(HashSet<Api> s:blocks) {
						if(s.contains(api1)&&s.contains(api2)) {
							B_Matrix.setX(row, col, 1);
						}
					}
					col++;
				}
				row++;
			}
			for(int i=0;i<B_Matrix.getR();i++) {
				for(int j=0;j<B_Matrix.getC();j++) {
					csvValues[j]=new Integer(B_Matrix.getX()[i][j]).toString();
				}
				cw.writeRecord(csvValues);
			}
			cw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Matrix getA_Matrix(String filePath) {
		Matrix res=null;
		CsvReader csvReader;
		try {
			csvReader = new CsvReader(filePath+File.separator+"A_Matrix.csv");
			while (csvReader.readRecord()){
			    // 读一整行
				String line=csvReader.getRawRecord();
				String[] str=line.split(",");
				res=new Matrix(2,str.length+1);
				for(int i=0;i<str.length;i++) {
					res.setX(1, i+1, Integer.parseInt(str[i]));
				}
			}
			csvReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}
	public static Matrix getB_Matrix(String filePath) {
		Matrix res=new Matrix(FEATURE_SIZE+1,FEATURE_SIZE+1);
		CsvReader csvReader;
		try {
			csvReader = new CsvReader(filePath+File.separator+"B_Matrix.csv");
			int row=1;
			while (csvReader.readRecord()) {
				// 读一整行
				String line=csvReader.getRawRecord();
				String[] str=line.split(",");
				for(int i=0;i<str.length;i++) {
					res.setX(row, i+1, Integer.parseInt(str[i]));
				}
				row++;
			}
			csvReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return res;
	}
}
