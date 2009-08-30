/**
 * Project: Stock
 * 
 * File Created at 2009-1-15
 * $Id$
 */
package net.frank.stock.fetchdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author frank.lizh
 */
public class InitStockDatabase {
    private static final Log logger     = LogFactory.getLog(InitStockDatabase.class);

    private final HttpClient httpClient = new HttpClient();

    public List<List<String>> getData(String code) {
        // ����GET������ʵ��
        GetMethod getMethod = new GetMethod("http://table.finance.yahoo.com/table.csv?s=" + code);
        logger.info("url(" + code + "): http://table.finance.yahoo.com/table.csv?s=" + code);

        // ʹ��ϵͳ�ṩ��Ĭ�ϵĻָ�����
        // getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
        // new DefaultHttpMethodRetryHandler());

        try {
            // ִ��getMethod
            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode != HttpStatus.SC_OK) {
                logger.error("Method failed when get " + code + ": " + getMethod.getStatusLine());
                return null;
            }

            // ��ȡ����
            final InputStream is = getMethod.getResponseBodyAsStream();
            final BufferedReader br = new BufferedReader(new InputStreamReader(is));

            List<List<String>> retList = new ArrayList<List<String>>();

            br.readLine(); // ����CVS�ļ��е�һ�еı��� 
            for (String s; (s = br.readLine()) != null;) {
                String[] items = s.split(",");

                List<String> list = new ArrayList<String>();
                for (int i = 0; i < 6; i++) {
                    list.add(items[i]);
                }
                retList.add(list);
            }

            return retList;
        } catch (HttpException e) {
            // �����������쳣��������Э�鲻�Ի��߷��ص�����������
            logger.error("Please check your provided http address! Exception: ", e);
        } catch (IOException e) {
            // ���������쳣
            logger.error("Network failed, Exception: ", e);
        } finally {
            // �ͷ�����
            getMethod.releaseConnection();
        }

        return null;
    }

    public static void main(String[] args) throws Exception {
        InitStockDatabase initData = new InitStockDatabase();
        StoreData storeData = new StoreData();
        Map<String, String> codeMap = MakeCodeList.getCodeMap();

        for (Map.Entry<String, String> entry : codeMap.entrySet()) {
            String code = entry.getKey();
            String name = entry.getValue();

            List<List<String>> list = initData.getData(code);
            if (list == null) {
                continue;
            }

            long tick = System.currentTimeMillis();
            for (int i = 0; i < list.size(); i++) {
                storeData.executeSave(code, name, (list.get(i)));
            }
            logger.info("Time used for get(" + name + "," + code + "): "
                    + (System.currentTimeMillis() - tick) + "ms.");
        }

        storeData.closeConnection();
    }
}
