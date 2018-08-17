
/**  
 * @Title:  CmdUtil.java   
 * @Package com.webrelax.util   
 * @Description:    TODO()   
 * @author: Lian
 * @date:   2018年8月13日 下午4:00:09   
 * @version V1.0  
 */
    
package com.webrelax.util;

import java.io.File;
import java.io.IOException;

/**   
 * @ClassName:  CmdUtil   
 * @Description:TODO()       
 */

public class CmdUtil {
	
	public static void cmd(String strdir,String command) {
		command="cmd /c "+command;
		File dir=new File(strdir);
		Runtime runtime=Runtime.getRuntime();
		Process process;
		try {
			process=runtime.exec(command,null,dir);
			process.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*命令模式：
		String command="cmd /c apktool.jar d *.apk";
		File dir=new File(fileDir);
		Runtime runtime=Runtime.getRuntime();
		Process process=runtime.exec(command,null,dir);
		InputStream is = process.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		process.waitFor();
		
		if (process.exitValue() != 0) {
		    System.out.println("命令执行失败");
		}
		//打印输出信息
		String s = null;
		while ((s = reader.readLine()) != null) {
		    System.out.println(s);
		}
		*/
		
	}
}
