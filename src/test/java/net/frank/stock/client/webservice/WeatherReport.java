package net.frank.stock.client.webservice;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WeatherReport {
    /**
     * ��ȡSOAP������ͷ�����滻���еı�־����Ϊ�û�����ĳ��� ��д�ߣ�������
     * 
     * @param city �û�����ĳ�������
     * @return �ͻ���Ҫ���͸���������SOAP����
     */
    private static String getSoapRequest(String city) {
        StringBuilder sb = new StringBuilder(256);
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
                + "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
                + "<soap:Body>    <getWeatherbyCityName xmlns=\"http://WebXml.com.cn/\">"
                + "<theCityName>" + city + "</theCityName>    </getWeatherbyCityName>"
                + "</soap:Body></soap:Envelope>");
        return sb.toString();
    }

    /**
     * �û���SOAP�����͸��������ˣ������ط������㷵�ص������� ��д�ߣ�������
     * 
     * @param city �û�����ĳ�������
     * @return �������˷��ص������������ͻ��˶�ȡ
     * @throws Exception
     */
    private static InputStream getSoapInputStream(String city) throws Exception {
        try {
            String soap = getSoapRequest(city);
            if (soap == null) {
                return null;
            }
            URL url = new URL("http://www.webxml.com.cn/WebServices/WeatherWebService.asmx");
            URLConnection conn = url.openConnection();
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Length", Integer.toString(soap.length()));
            conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            conn.setRequestProperty("SOAPAction", "http://WebXml.com.cn/getWeatherbyCityName");

            OutputStream os = conn.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "utf-8");
            osw.write(soap);
            osw.flush();
            osw.close();

            InputStream is = conn.getInputStream();
            return is;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * �Է������˷��ص�XML���н��� ��д�ߣ�������
     * 
     * @param city �û�����ĳ�������
     * @return �ַ��� ��,�ָ�
     */
    public static String getWeather(String city) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(getSoapInputStream(city));

            NodeList nl = doc.getElementsByTagName("string");
            StringBuffer sb = new StringBuffer(256);
            for (int count = 0; count < nl.getLength(); count++) {
                Node n = nl.item(count);
                if (n.getFirstChild().getNodeValue().equals("��ѯ���Ϊ�գ�")) {
                    sb = new StringBuffer("#");
                    break;
                }
                sb.append(n.getFirstChild().getNodeValue() + "#\n");
            }
            getSoapInputStream(city).close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * ������
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println(getWeather("��ɳ"));
    }
}
