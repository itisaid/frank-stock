/**
 * Project: WebService
 * 
 * File Created at 2009-1-14
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
package net.frank.stock.client.webservice;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author frank.lizh
 */
public class ServiceMain {
    private static final Log    log                    = LogFactory.getLog(ServiceMain.class);

    private static final String STOCK_SERVICE_ENDPOINT = "http://www.webxml.com.cn/WebServices/ChinaStockWebService.asmx?wsdl";

    public void callSayHello() {
        try {
            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(new java.net.URL(STOCK_SERVICE_ENDPOINT));
            call.setOperationName(new QName("http://www.webxml.com.cn/", "sayHello"));
            call.setReturnType(org.apache.axis.Constants.XSD_STRING);
            try {
                String ret = (String) call.invoke(new Object[] {});
                System.out.println("The return value is:" + ret);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        log.error("call sayHello service error!");
    }

    public void callSayHelloToPerson() {
        try {
            Service service = new Service();

            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(new java.net.URL(STOCK_SERVICE_ENDPOINT));
            call.setOperationName(new QName(
                    "http://www.webxml.com.cn/WebServices/ChinaStockWebService.asmx",
                    "getStockInfoByCode"));
            call.addParameter("getStockInfoByCodeSoapIn", org.apache.axis.Constants.XSD_STRING,
                    javax.xml.rpc.ParameterMode.IN);
            call.setReturnType(org.apache.axis.Constants.XSD_STRING);
            call.setUseSOAPAction(true);
            // call.setSOAPActionURI("http://www.webxml.com.cn/WebServices/ChinaStockWebService.asmx");

            try {
                String ret = (String) call.invoke(new Object[] { "sh000001" });
                log.error("1!");
                System.out.println("The return value is:" + ret);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        log.error("call sayHello service error!");
    }

    public static void main(String[] args) {
        ServiceMain tester = new ServiceMain();
        // tester.callSayHello();
        tester.callSayHelloToPerson();
    }
}
