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
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author frank.lizh
 */
public class StoreDataO {
    private static final Log logger = LogFactory.getLog(StoreDataO.class);

    private StoreDataO() {
    }

    private static Statement  stmt;
    private static Connection con;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            // 建立连接
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/stock?useUnicode=true&characterEncoding=UTF-8",
                    "store", "store");

            stmt = con.createStatement();
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void executeSave(String sql) {
        try {
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            logger.error("SQL Exception: ", e);
        }

    }

    public static void closeConnection() {
        try {
            con.close();
        } catch (SQLException e) {
            logger.error("Exception when close connection: ", e);
        }
    }

    public static List queryCodeList() {
        List list = new ArrayList();
        ResultSet rs;
        try {
            rs = stmt.executeQuery(" select code from stock_data group by code");

            while (rs.next()) {
                list.add(rs.getString(1));
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    public static List queryRecord(String sql) {
        // 执行SQL语句，返回结果集
        List list = new ArrayList();
        ResultSet rs;
        try {
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Map map = new HashMap();
                map.put("trade_date", rs.getDate("trade_date"));
                map.put("open_price", rs.getFloat("open_price"));
                map.put("high_price", rs.getFloat("high_price"));
                map.put("low_price", rs.getFloat("low_price"));
                map.put("close_price", rs.getFloat("close_price"));
                map.put("volume", rs.getFloat("volume"));
                list.add(map);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;

    }

}
