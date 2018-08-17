
/**  
 * @Title:  App.java   
 * @Package com.webrelax.entity   
 * @Description:    TODO()   
 * @author: Lian
 * @date:   2018年8月14日 上午11:07:53   
 * @version V1.0  
 */
    
package com.webrelax.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**   
 * @ClassName:  App   
 * @Description:TODO()       
 */

public class App {
	private int id;
	private int type;//-1,0,1表示恶意，不确定，良性（不确定表示测试集）
	private String name;
	private Map<String,Api> containApis= new HashMap<String,Api>();
	private List<HashSet<Api>> commonBlockApis=new ArrayList<HashSet<Api>>();
	public App() {};
	public App(int id,int type,String name) {
		this.id=id;
		this.type=type;
		this.name=name;
	}
	public App(int type,String name) {
		this.type=type;
		this.name=name;
	}
	public Map<String, Api> getContainApis() {
		return containApis;
	}
	public void setContainApis(Map<String, Api> containApis) {
		this.containApis = containApis;
	}
	public List<HashSet<Api>> getCommonBlockApis() {
		return commonBlockApis;
	}
	public void setCommonBlockApis(List<HashSet<Api>> commonBlockApis) {
		this.commonBlockApis = commonBlockApis;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		App other = (App) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
