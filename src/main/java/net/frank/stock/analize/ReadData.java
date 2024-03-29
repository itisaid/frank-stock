/**
 * Project: Stock
 * 
 * File Created at 2009-1-15
 * $Id$
 */
package net.frank.stock.analize;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author frank.lizh
 */
public class ReadData {
    private static final Log logger = LogFactory.getLog(ReadData.class);

    private final Statement  stmt;
    private final Connection con;

    public ReadData() throws ClassNotFoundException, SQLException, IOException {
        Properties properties = new Properties();
        properties.load(ReadData.class.getResourceAsStream("/jdbc.properties"));

        Class.forName(properties.getProperty("jdbc.driverClassName"));
        // 建立连接
        con = DriverManager.getConnection(properties.getProperty("jdbc.url"), properties
                .getProperty("jdbc.username"), properties.getProperty("jdbc.password"));

        stmt = con.createStatement();
    }

    public void closeConnection() {
        try {
            con.close();
        } catch (SQLException e) {
            logger.error("Exception when close connection: ", e);
        }
    }

    public List<String> queryCodeList() {
        List<String> list = new ArrayList<String>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery(" select code from stock_data group by code");

            while (rs.next()) {
                list.add(rs.getString(1));
            }

        } catch (SQLException e) {
            logger.error("Exception when queryCodeList: ", e);
        }
        return list;
    }

    public List<Map<String, Object>> queryRecord(String sql) {
        // 执行SQL语句，返回结果集
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();

                map.put("trade_date", rs.getDate("trade_date"));
                map.put("open_price", rs.getFloat("open_price"));
                map.put("high_price", rs.getFloat("high_price"));
                map.put("low_price", rs.getFloat("low_price"));
                map.put("close_price", rs.getFloat("close_price"));
                map.put("volume", rs.getFloat("volume"));

                list.add(map);
            }
        } catch (SQLException e) {
            logger.error("Exception when queryRecord: ", e);
        }

        return list;
    }
}
