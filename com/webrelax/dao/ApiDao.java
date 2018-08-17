
/**  
 * @Title:  ApiDao.java   
 * @Package com.webrelax.dao   
 * @Description:    TODO()   
 * @author: Lian
 * @date:   2018年8月13日 下午4:12:51   
 * @version V1.0  
 */
    
package com.webrelax.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.webrelax.entity.Api;
import com.webrelax.util.C3P0Util;

/**   
 * @ClassName:  ApiDao   
 * @Description:TODO()       
 */

public class ApiDao {
	public void addApi(Api api) throws SQLException {
		QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
		String sql = "insert into api(apiName,packName,invokeMethod) values(?,?,?)";
		qr.update(sql,api.getApiName(),api.getPackName(),api.getInvokeMethod());
	}
	public Api findApiById(int id) throws SQLException {
		QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
		return qr.query("select * from api where id=?", new BeanHandler<Api>(Api.class),id);
	}
	public Api findApiByName(String name) throws SQLException {
		QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
		return qr.query("select * from api where apiName=?", new BeanHandler<Api>(Api.class),name);
	}
	public List<Api> findAllApi() throws SQLException{
		QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
		return qr.query("select * from api", new BeanListHandler<Api>(Api.class));
	}
}
