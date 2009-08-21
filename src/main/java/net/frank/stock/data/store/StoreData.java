/**
 * Project: Stock
 * 
 * File Created at 2009-1-15
 * $Id$
 * 
 * Copyright 2008 Alibaba.com Croporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Alibaba Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Alibaba.com.
 */
package net.frank.stock.data.store;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * TODO Comment of StoreData
 * 
 * @author frank.lizh
 * 
 */
public class StoreData {

	private StoreData() {

	}

	private static PreparedStatement stmt ;
	
	private static Connection con;
	

	static{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// 建立连接
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/stock?useUnicode=true&characterEncoding=UTF-8", "store", "store");
			// 创建状态
			String sql = "insert into stock_data(code,name,trade_date,open_price,high_price,low_price,close_price,volume) values(?,?,?,?,?,?,?,?)";
			stmt = con.prepareStatement(sql);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public static void executeSave(String code, String name, List list) {
		ResultSet rs;
		try {
			stmt.setString(1, code);
			stmt.setString(2, name);
			stmt.setDate(3, Date.valueOf((String) list.get(0)));
			for (int i = 1; i < (list.size() - 1); i++) {
				stmt.setFloat(i + 3, Float.valueOf((String) (list.get(i))));
			}
			stmt.setInt(8, Integer.valueOf((String) (list.get(5))));
			stmt.executeUpdate();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void closeConnection(){
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public static void executeQuery(String sql) {
		// // 执行SQL语句，返回结果集
		// ResultSet rs;
		// try {
		// rs = stmt.executeQuery("SELECT * FROM person");
		// } catch (SQLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// // 对结果集进行处理
		// while (rs.next()) {
		// int id = rs.getInt("id");
		// String name = rs.getString("name");
		// Integer age = rs.getObject("age") == null ? null : rs.getInt("age");
		// System.out.println(id + ": " + name + " " + age);
		// }
		// // 释放资源
		// stmt.close();
	}

}
