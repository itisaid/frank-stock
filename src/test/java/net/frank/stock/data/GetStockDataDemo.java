/**
 * Project: stock.parent
 * 
 * File Created at Aug 27, 2009
 * $Id$
 */
package net.frank.stock.data;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author ding.lid
 */
public class GetStockDataDemo {
    private static final Log logger = LogFactory.getLog(GetStockDataDemo.class);

    public static void main(String[] args) throws Exception {
        // 创建GET方法的实例
        GetMethod getMethod = new GetMethod(
                "http://table.finance.yahoo.com/table.csv?s=ibm&d=6&e=22&f=2009&g=d&a=1&b=2&c=2008&ignore=.csv");

        try {
            // 执行getMethod
            int statusCode = new HttpClient().executeMethod(getMethod);
            if (statusCode != HttpStatus.SC_OK) {
                logger.error("Method failed: " + getMethod.getStatusLine());
                return;
            }

            final InputStream is = getMethod.getResponseBodyAsStream();
            final BufferedReader br = new BufferedReader(new InputStreamReader(is));
            for (String s; (s = br.readLine()) != null;) {
                System.out.println(s);
            }
        } finally {
            getMethod.releaseConnection();
        }
    }
}
