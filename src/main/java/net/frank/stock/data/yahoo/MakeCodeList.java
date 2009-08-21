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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * TODO Comment of MakeCodeList
 * 
 * @author frank.lizh
 * 
 */
public class MakeCodeList {

	/**
	 * @param args
	 */
	public static Map<String, String> getCodeMap() {
		Map<String, String> map = new HashMap<String, String>();
		try {

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File("StockList.xml"));

			// normalize text redivsentation
			doc.getDocumentElement().normalize();
			System.out.println("Root element of the doc is "
					+ doc.getDocumentElement().getNodeName());

			NodeList trList = doc.getElementsByTagName("tr");

			for (int i = 0; i < trList.getLength(); i++) {
				Element trElement = (Element) trList.item(i);
				NodeList trL = trElement.getElementsByTagName("td");
				String code = (trL.item(0).getChildNodes().item(0))
						.getNodeValue();
				String name = (trL.item(1).getChildNodes().item(0))
						.getNodeValue();
				putMap(map, code, name);
				String code1 = (trL.item(2).getChildNodes().item(0))
						.getNodeValue();
				String name1 = (trL.item(3).getChildNodes().item(0))
						.getNodeValue();
				putMap(map, code, name);
				System.out.println(i + ":" + code + name + code1 + name1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.print(map.size());
		// System.exit (0);
		return map;
	}

	public static void putMap(Map map, String code, String name) {
		if (code.startsWith("6")) {
			map.put(code + ".ss", name);
		}
		if (code.startsWith("0")) {
			map.put(code + ".sz", name);
		}
	}

	public static void main(String[] args) {
		getCodeMap();
	}
}
