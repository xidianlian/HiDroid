
/**  
 * @Title:  MetaPath.java   
 * @Package com.webrelax.service   
 * @Description:    TODO()   
 * @author: Lian
 * @date:   2018年9月2日 下午5:02:46   
 * @version V1.0  
 */
    
package com.webrelax.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import com.webrelax.entity.Api;
import com.webrelax.util.Matrix;

/**   
 * @ClassName:  MetaPath   
 * @Description:TODO()       
 */

public class MetaPath {
	private static final int FEATURE_SIZE=200;
	private Matrix cMatrix=null;//交换矩阵
	private String kernelFilePath=null;//核文件路径
	private String dataType=null;//数据类型 train|test|detect
	private Matrix I_Matrix=null;
	private Matrix P_Matrix=null;
	private Set<Api> newApi=null;//mrmr算法得到的api
	public MetaPath(String dataType) {
		this.dataType=dataType;
		P_Matrix=new Matrix(FEATURE_SIZE+1,FEATURE_SIZE+1);
		I_Matrix=new Matrix(FEATURE_SIZE+1,FEATURE_SIZE+1);
		//核文件输出路径
		kernelFilePath="output"+File.separator+"PrecomputedKernels";
		File file=new File(kernelFilePath);
		if(!file.exists()) {
			file.mkdirs();
		}
		
		Mrmr mrmr=new Mrmr();
		try {
			newApi=mrmr.getNewApi("output"+File.separator+"mrmr"+File.separator+"mrmr.out");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void getPI_Matrix() {
		int row=1;
		for(Api api1:newApi) {
			int col=1;
			for(Api api2:newApi) {
				if(api1.getPackName().equals(api2.getPackName())) {
					P_Matrix.setX(row, col, 1);
				}else {
					P_Matrix.setX(row, col, 0);
				}
				if(api1.getInvokeMethod().equals(api2.getInvokeMethod())) {
					I_Matrix.setX(row, col, 1);
				}else {
					I_Matrix.setX(row, col, 0);
				}
				col++;
			}
			row++;
		}
	}
	public void  dealMetaPath() {
		//得到PI矩阵
		getPI_Matrix();
		
		String[] metaPathName= {"AA", "ABA"};
		//, "APA", "AIA", "ABPBA", "APBPA", "ABIBA", "AIBIA", "APIPA", "AIPIA",
		//"ABPIPBA","APBIPBA", "ABIPIBA", "AIBPBIA", "AIPBPIA", "APIBIPA"
		//清空 kernelfile
		if("train".equals(dataType)) {
			File kernelfile=new File(kernelFilePath+File.separator+"kernelfile");
			kernelfile.delete();
			try {
				kernelfile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		for(int i=0;i<metaPathName.length;i++) {
			generateC_Matrix(metaPathName[i]);
			String kernelFileName=null;
			if(dataType.equals("train")) {
				kernelFileName="kernel_"+i;
			}else {
				kernelFileName="kernel_"+i+".test";
			}
			outputKernelFile(kernelFileName);
		}
		outputYFile();
	}
	private void sortFileList(File [] files) {
		List<File> fileList = Arrays.asList(files);
	    Collections.sort(fileList, new Comparator<File>() {
	        @Override
	        public int compare(File o1, File o2) {
	            if (o1.isDirectory() && o2.isFile())
	                return -1;
	            if (o1.isFile() && o2.isDirectory())
	                return 1;
	            return o1.getName().compareTo(o2.getName());
	        }
	    });
	}
	/**
	 * @Title: generateC_Matrix
	 * @Description: TODO(得到交换矩阵)
	 * @param metaPath 元路径
	 * @return void    返回类型
	 */
	private void generateC_Matrix(String metaPath) {
		//交换矩阵 行 所对应的APP的路径
		String rowMalwarePath=null;
		String rowBenignPath=null;
		//交换矩阵 列 所对应的APP的路径
		String colMalwarePath="dataset"+File.separator+"traindata"+File.separator+"malwarematrix";
		String colBenignPath="dataset"+File.separator+"traindata"+File.separator+"benignmatrix";
		
		if("train".equals(dataType)) {
			rowMalwarePath=colMalwarePath;
			rowBenignPath=colBenignPath;
		}else if("test".equals(dataType)) {
			rowMalwarePath="dataset"+File.separator+"testdata"+File.separator+"malwarematrix";
			rowBenignPath="dataset"+File.separator+"testdata"+File.separator+"benignmatrix";
		}else if("detect".equals(dataType)) {
			rowMalwarePath="dataset"+File.separator+"detectdata"+File.separator+"matrix";
		}
		File[] colFiles1 = new File(colMalwarePath).listFiles();
		File[] colFiles2 = new File(colBenignPath).listFiles();
		File[] rowFiles1 = new File(rowMalwarePath).listFiles();
		File[] rowFiles2=null;
		if(rowBenignPath!=null) {
			rowFiles2 = new File(rowBenignPath).listFiles();
			sortFileList(rowFiles2);
		}
		//根据文件名排序
		sortFileList(colFiles1);
		sortFileList(colFiles2);
		sortFileList(rowFiles1);
		sortFileList(rowFiles2);
		
		int rowAppNum=rowFiles1.length+(rowFiles2==null?0:rowFiles2.length);
		int colAppNum=colFiles1.length+colFiles2.length;
		cMatrix=new Matrix(rowAppNum+1,colAppNum+1);
		Matrix A1,A2,B1,B2,res;
		rowAppNum=1;
		colAppNum=1;
			
		for(int i=0;i<rowFiles1.length;i++,rowAppNum++) {
			A1=ABCsv.getA_Matrix(rowFiles1[i].getPath());
			B1=ABCsv.getB_Matrix(rowFiles1[i].getPath());
			colAppNum=1;
			for(int j=0;j<colFiles1.length;j++,colAppNum++) {
				A2=ABCsv.getA_Matrix(colFiles1[j].getPath());
				B2=ABCsv.getB_Matrix(colFiles1[j].getPath());
				res=calculate(A1,A2,B1,B2,metaPath);
//				System.out.println(rowAppNum+" "+colAppNum+" "+res.getX()[1][1]);
				cMatrix.setX(rowAppNum, colAppNum, res.getX()[1][1]);
			}
			for(int j=0;j<colFiles2.length;j++,colAppNum++) {
				A2=ABCsv.getA_Matrix(colFiles2[j].getPath());
				B2=ABCsv.getB_Matrix(colFiles2[j].getPath());
				res=calculate(A1,A2,B1,B2,metaPath);
				cMatrix.setX(rowAppNum, colAppNum, res.getX()[1][1]);
			}
		}
		for(int i=0;rowFiles2!=null&&i<rowFiles2.length;i++,rowAppNum++) {
			A1=ABCsv.getA_Matrix(rowFiles2[i].getPath());
			B1=ABCsv.getB_Matrix(rowFiles2[i].getPath());
			colAppNum=1;
			for(int j=0;j<colFiles1.length;j++,colAppNum++) {
				A2=ABCsv.getA_Matrix(colFiles1[j].getPath());
				B2=ABCsv.getB_Matrix(colFiles1[j].getPath());
				res=calculate(A1,A2,B1,B2,metaPath);
				cMatrix.setX(rowAppNum, colAppNum, res.getX()[1][1]);
			}
			for(int j=0;j<colFiles2.length;j++,colAppNum++) {
				A2=ABCsv.getA_Matrix(colFiles2[j].getPath());
				B2=ABCsv.getB_Matrix(colFiles2[j].getPath());
				res=calculate(A1,A2,B1,B2,metaPath);
				cMatrix.setX(rowAppNum, colAppNum, res.getX()[1][1]);
			}
		}
	}

	/**@Title: calculate
	 * @Description: TODO()
	 * @param a1
	 * @param a2
	 * @param b1
	 * @param b2
	 * @param metaPath    参数
	 * @return void    返回类型
	 */
	private Matrix calculate(Matrix a1, Matrix a2, Matrix b1, Matrix b2, String metaPath) {
		int mid=0;//标记过半
		int len=metaPath.length();
		b1=b1.andMatrix(b2);
		Matrix res=new Matrix(a1);
		for(int i=1;i<len;i++) {
			if(len%2==0) {
				if(i>=len/2)mid=1;
			}else {
				if(i>len/2)mid=1;
			}
			switch(metaPath.charAt(i)) {
				case 'A':
					if(mid==1) {
						res=res.mulMatrix(a2.T());
					}
					break;
				case 'B':
					if(mid==0) {
						res=res.mulMatrix(b1);
					}else {
						if(res==null)System.out.println("res");
						res=res.mulMatrix(b1.T());
					}
					break;
				case 'P':
					if(mid==0) {
						res=res.mulMatrix(P_Matrix);
					}else {
						res=res.mulMatrix(P_Matrix.T());
					}
					break;
				case 'I':
					if(mid==0) {
						res=res.mulMatrix(I_Matrix);
					}else {
						res=res.mulMatrix(I_Matrix.T());
					}
					break;
				default:
					System.out.println("metaPath.charAt() 无此字符");
			}
		}
		return res;
		
	}
	/**@Title: outputKernelFile
	 * @Description: TODO()
	 * @param kernelFileName    参数
	 * @return void    返回类型
	 */
	private void outputKernelFile(String kernelFileName) {
		//kernel文件
		if("train".equals(dataType)) {
			File kernelfile=new File(kernelFilePath+File.separator+"kernelfile");
			FileWriter kf;
			try {
				if(!kernelfile.exists()) {
					kernelfile.createNewFile();
				}
				kf = new FileWriter(kernelfile,true);
				BufferedWriter bwkf=new BufferedWriter(kf);
				bwkf.write("-t 4 -f "+new File(kernelFilePath).getAbsolutePath()+File.separator+kernelFileName+"\r\n");
				bwkf.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		File kernel=new File(kernelFilePath+File.separator+kernelFileName);
		kernel.delete();
		try {
			kernel.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("创建新核文件失败");
		}
		try {
			FileWriter fwKernel=new FileWriter(kernel);
			BufferedWriter bwKernel=new BufferedWriter(fwKernel);
			int n=cMatrix.getR();
			int m=cMatrix.getC();
			bwKernel.write(""+(n-1)+" "+(m-1)+"\r\n");
			for(int i=1;i<n;i++) {
				String res="";
				for(int j=1;j<m;j++) {
					res+=cMatrix.getX()[i][j];
					if(j!=m-1) {
						res+=" ";
					}else {
						res+="\r\n";
					}
				}
				bwKernel.write(res);
			}
			bwKernel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**@Title: outputYFile
	 * @Description: TODO()
	 * @param length1
	 * @param length2
	 * @param length3
	 * @param length4    参数
	 * @return void    返回类型
	 */
	private void outputYFile() {
		String fileName="";
		int malwareNum=0;
		int benignNum=0;
		String malwarePath="";
		String benignPath="";
		if("train".equals(dataType)) {
			fileName="y_train";
			malwarePath="dataset"+File.separator+"traindata"+File.separator+"malwarematrix";
			benignPath="dataset"+File.separator+"traindata"+File.separator+"benignmatrix";
		}else if("test".equals(dataType)){
			fileName="y_test";
			malwarePath="dataset"+File.separator+"testdata"+File.separator+"malwarematrix";
			benignPath="dataset"+File.separator+"testdata"+File.separator+"benignmatrix";
		}else if("detect".equals(dataType)){
			fileName="y_test";
			malwarePath="dataset"+File.separator+"detectdata"+File.separator+"matrix";
			benignPath=malwarePath;
		}
		File file1=new File(malwarePath);
		File file2=new File(benignPath);
		malwareNum=file1.listFiles().length;
		benignNum=file2.listFiles().length;
		File kernel=new File(kernelFilePath+File.separator+fileName);
		kernel.delete();
		try {
			kernel.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("创建新核文件失败");
		}
		FileWriter fw;
		try {
			fw = new FileWriter(kernel);
			BufferedWriter bw=new BufferedWriter(fw);
			for(int i=1;i<=malwareNum;i++) {
				bw.write("1"+"\r\n");
			}
			for(int i=1;!"detect".equals(dataType)&&i<=benignNum;i++) {
				bw.write("-1"+"\r\n");
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
