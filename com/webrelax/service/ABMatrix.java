
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.webrelax.dao.ApiDao;
import com.webrelax.entity.Api;
import com.webrelax.entity.App;
import com.webrelax.util.CsvReader;
import com.webrelax.util.CsvWriter;



/**   
 * @ClassName:  RelationMatrix   
 * @Description:TODO()       
 */

public class ABMatrix {
	public static void outputMatrixCsv(App app,String outputPath) {
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
	public static Map<String, Api> readAmatrix(String csvFilePath) throws IOException {
		
		Map<String, Api> apis=new HashMap<String,Api>();
		
		
		CsvReader csvReader = new CsvReader(csvFilePath);
		while (csvReader.readRecord()){
		    // 读一整行
			String line=csvReader.getRawRecord();
			String[] res=line.split(",");
			Api api=new Api();
			for(int i=0;i<res.length;i++) {
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
	public static List<HashSet<Api>> readBmatrix(String csvFilePath) throws IOException{
		List<HashSet<Api>> res=new ArrayList<HashSet<Api>>();
		CsvReader csvReader = new CsvReader(csvFilePath);
		while (csvReader.readRecord()){
		    // 读一整行
			String line=csvReader.getRawRecord();
			HashSet<Api>s=new HashSet<Api>();
			String[] str=line.split(",");
			for(int i=0;i<str.length;i++) {
				String[] a=str[i].split(":");
				Api api=new Api();
				api.setApiName(str[0]);
				api.setPackName(str[1]);
				api.setInvokeMethod(str[2]);
				s.add(api);
			}
			res.add(s);
		}
		csvReader.close();
		return res;
	}
}
