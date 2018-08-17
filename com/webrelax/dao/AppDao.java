
/**  
 * @Title:  AppDao.java   
 * @Package com.webrelax.dao   
 * @Description:    TODO()   
 * @author: Lian
 * @date:   2018年8月14日 下午4:02:03   
 * @version V1.0  
 */
    
package com.webrelax.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.webrelax.entity.App;
import com.webrelax.util.C3P0Util;

/**   
 * @ClassName:  AppDao   
 * @Description:TODO()       
 */

public class AppDao {
	public void addApp(App app) throws SQLException {
		QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
		String sql = "insert into app(id,type,name) values(?,?,?)";
		qr.update(sql,app.getId(),app.getType(),app.getName());
	}
	public App findAppById(int id) throws SQLException {
		QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
		return qr.query("select * from app where id=?", new BeanHandler<App>(App.class),id);
	}
	public App findAppByName(String name) throws SQLException {
		QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
		return qr.query("select * from app where name=?", new BeanHandler<App>(App.class),name);
	}
}
