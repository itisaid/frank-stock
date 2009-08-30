/**
 * Project: Stock
 * 
 * File Created at 2009-1-15
 * $Id$
 */
package net.frank.stock.fetchdata;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author frank.lizh
 */
public class MakeCodeList {
    private static final Log logger = LogFactory.getLog(MakeCodeList.class);

    private static void putMap(Map<String, String> map, String code, String name) {
        if (code.startsWith("6")) {
            map.put(code + ".ss", name);
        }
        if (code.startsWith("0")) {
            map.put(code + ".sz", name);
        }
    }

    /**
     * @param args
     */
    public static Map<String, String> getCodeMap() {
        Document doc = null;
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            doc = docBuilder.parse(new File("StockList.xml"));
        } catch (Exception e) {
            logger.error("Fail to get code map: ", e);
            return null;
        }

        Map<String, String> map = new HashMap<String, String>();
        // normalize text redivsentation
        doc.getDocumentElement().normalize();
        logger.info("Root element of the doc is " + doc.getDocumentElement().getNodeName());

        NodeList trList = doc.getElementsByTagName("tr");
        for (int i = 0; i < trList.getLength(); i++) {
            try {
                Element trElement = (Element) trList.item(i);
                NodeList trL = trElement.getElementsByTagName("td");

                String code = (trL.item(0).getChildNodes().item(0)).getNodeValue();
                String name = (trL.item(1).getChildNodes().item(0)).getNodeValue();
                putMap(map, code, name);

                String code1 = (trL.item(2).getChildNodes().item(0)).getNodeValue();
                String name1 = (trL.item(3).getChildNodes().item(0)).getNodeValue();
                putMap(map, code, name);

                logger.info("Get code " + i + ":" + code + "/" + name + "/" + code1 + "/" + name1);
            } catch (Exception e) {
                // ignore
                logger.info("Exception when get code(" + i + "): ", e);
            }
        }

        logger.info("Stock count in List: " + map.size());
        return map;
    }
}
