
/**  
 * @Title:  Api.java   
 * @Package com.webrelax.domain   
 * @Description:    TODO()   
 * @author: Lian
 * @date:   2018年8月13日 下午4:10:28   
 * @version V1.0  
 */
    
package com.webrelax.entity;


/**   
 * @ClassName:  Api   
 * @Description:TODO()       
 */

public class Api {
	private int id;
	private String apiName;
	private String packName;
	private String invokeMethod;
	
	
	public Api() {}
	public Api(int id,String apiName,String packName,String invokeMethod) {
		this.id=id;
		this.apiName=apiName;
		this.packName=packName;
		this.invokeMethod=invokeMethod;
	}
	public Api(String apiName,String packName,String invokeMethod) {
		this.apiName=apiName;
		this.packName=packName;
		this.invokeMethod=invokeMethod;
	}
	public String getInvokeMethod() {
		return invokeMethod;
	}
	public void setInvokeMethod(String invokeMethod) {
		this.invokeMethod = invokeMethod;
	}
	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getPackName() {
		return packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apiName == null) ? 0 : apiName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Api other = (Api) obj;
		if (apiName == null) {
			if (other.apiName != null)
				return false;
		} else if (!apiName.equals(other.apiName))
			return false;
		return true;
	}
	
	
}
