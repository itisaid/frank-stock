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
package net.frank.stock.data.yahoo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.frank.stock.data.store.StoreData;
import net.frank.stock.data.store.StoreDataO;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * TODO Comment of DataInit
 * 
 * @author frank.lizh
 * 
 */
public class InitData {
	public HttpClient httpClient;

	public InitData() {
		httpClient = new HttpClient();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InitData initData = new InitData();
		Map codeMap = MakeCodeList.getCodeMap();
		Set<Map.Entry<String, String>> mt = codeMap.entrySet();
		for (Map.Entry<String, String> entry : mt) {
			String code = entry.getKey();
			String name = entry.getValue();
			List l = initData.getData(code);
			if (l != null) {
				String sql = null;
				long b = System.currentTimeMillis();
				for (int i = 0; i < l.size(); i++) {
//					sql = initData.makeSql(entry.getKey(), (List) l.get(i));
//				
//					StoreDataO.executeSave(sql);
					
					StoreData.executeSave(code, name, (List)(l.get(i)));
					
				}
				long e = System.currentTimeMillis();
				System.out.println("time:"+(e-b));
			}
		}
		StoreDataO.closeConnection();

	}

	public List getData(String code) {
		// ����GET������ʵ��

		GetMethod getMethod = new GetMethod(
				"http://table.finance.yahoo.com/table.csv?s=" + code);
		System.out.println("http://table.finance.yahoo.com/table.csv?s=" + code);
		// ʹ��ϵͳ�ṩ��Ĭ�ϵĻָ�����
		// getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
		// new DefaultHttpMethodRetryHandler());
		try {
			// ִ��getMethod
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "
						+ getMethod.getStatusLine());
			} else {
				// ��ȡ����
				InputStream is = getMethod.getResponseBodyAsStream();
				BufferedReader br = new BufferedReader(
						new InputStreamReader(is));
				// ��������
				String s = null;
				br.readLine();
				List list = new ArrayList();
				while ((s = br.readLine()) != null) {
					String[] item = s.split(",");
					List l = new ArrayList();
					for (int i = 0; i < 6; i++) {
						l.add(item[i]);
					}
					list.add(l);
				}
				return list;
			}
		} catch (HttpException e) {
			// �����������쳣��������Э�鲻�Ի��߷��ص�����������
			System.out.println("Please check your provided http address!");
			e.printStackTrace();
		} catch (IOException e) {
			// ���������쳣
			e.printStackTrace();
		} finally {
			// �ͷ�����
			getMethod.releaseConnection();
		}
		return null;
	}

	public String makeSql(String code, List list) {
		StringBuffer buf = new StringBuffer(
				"insert into stock_data(code,trade_date,open_price,high_price,low_price,close_price,volume) values('");
		buf.append(code);
		buf.append("',");
		for (int i = 0; i < list.size(); i++) {
			if (i == 0) {
				buf.append("'");
			}
			buf.append((String) (list.get(i)));
			if (i == 0) {
				buf.append("'");
			}
			if (i < (list.size() - 1)) {
				buf.append(",");
			}
		}
		buf.append(")");
		// System.out.println(buf);
		return buf.toString();

	}

}
