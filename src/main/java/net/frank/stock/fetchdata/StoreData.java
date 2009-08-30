/**
 * Project: Stock
 * 
 * File Created at 2009-1-15
 * $Id$
 */
package net.frank.stock.fetchdata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author frank.lizh
 */
public class StoreData {
    private static final Log        logger = LogFactory.getLog(StoreData.class);

    private final PreparedStatement stmt;
    private final Connection        con;

    public StoreData() throws ClassNotFoundException, SQLException, IOException {
        Properties properties = new Properties();
        properties.load(StoreData.class.getResourceAsStream("/jdbc.properties"));

        Class.forName(properties.getProperty("jdbc.driverClassName"));
        // 建立连接
        con = DriverManager.getConnection(properties.getProperty("jdbc.url"), properties
                .getProperty("jdbc.username"), properties.getProperty("jdbc.password"));

        // 创建状态
        String sql = "insert into stock_data(code, name, trade_date, open_price, high_price, low_price, close_price, volume) "
                + " values(?, ?, ?, ?, ?, ?, ?, ?)";
        stmt = con.prepareStatement(sql);
    }

    public void executeSave(String code, String name, List<String> list) {
        try {
            stmt.setString(1, code);
            stmt.setString(2, name);
            stmt.setDate(3, Date.valueOf(list.get(0)));

            for (int i = 1; i < (list.size() - 1); i++) {
                stmt.setFloat(i + 3, Float.valueOf(list.get(i)));
            }
            stmt.setInt(8, Integer.valueOf((list.get(5))));

            stmt.executeUpdate();
        } catch (Exception e) {
            logger.error("Exception when close executeSave: ", e);
        }
    }

    public void closeConnection() {
        try {
            con.close();
        } catch (SQLException e) {
            logger.error("Exception when close connection: ", e);
        }
    }

}
