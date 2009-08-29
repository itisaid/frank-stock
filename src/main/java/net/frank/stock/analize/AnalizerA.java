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
package net.frank.stock.analize;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author frank.lizh
 */
public class AnalizerA {
    private static final Log logger = LogFactory.getLog(AnalizerA.class);

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        ReadData readData = new ReadData();

        @SuppressWarnings("unchecked")
        List<Float>[] resultD = new List[7];
        @SuppressWarnings("unchecked")
        List<Float>[] resultU = new List[7];
        for (int m = 0; m < 7; m++) {
            resultD[m] = new ArrayList<Float>();
            resultU[m] = new ArrayList<Float>();
        }

        List<String> codeList = readData.queryCodeList();
        logger.info("total stock: " + codeList.size());
        int len = codeList.size();

        for (int i = 0; i < codeList.size(); i++) {
            String sql = "select trade_date, open_price, high_price, low_price, close_price, volume"
                    + " from stock_data" + " where code = '" + (codeList.get(i)).toString() + "'";
            List<Map<String, Object>> records = readData.queryRecord(sql);

            logger.info(codeList.get(i).toString() + ":");

            for (int k = 3; k < 10; k++) {
                analizeTopD(records, resultD[k - 3], k);
                analizeTopU(records, resultU[k - 3], k);
            }
        }

        for (int m = 0; m < 7; m++) {
            Float av = 0F;
            for (int j = 0; j < resultD[m].size(); j++) {
                av += Float.valueOf(resultD[m].get(j).toString());
            }
            logger.info("totalD: " + m + " : " + (av / (len)));

            av = 0F;
            for (int j = 0; j < resultU[m].size(); j++) {
                av += Float.valueOf(resultU[m].get(j).toString());
            }
            logger.info("totalU: " + m + " : " + (av / (len)));
        }

        readData.closeConnection();
    }

    public static String[] getStocksCode() {
        return null;
    }

    public static void analize(List<Map<String, Object>> records, List<Float> result) {
        Float success = 0F;
        Float fail = 0F;
        for (int i = 1; i < (records.size() - 1); i++) {
            float c1 = Float.valueOf((records.get(i - 1)).get("close_price").toString());
            float c2 = Float.valueOf((records.get(i)).get("close_price").toString());
            float c3 = Float.valueOf((records.get(i + 1)).get("close_price").toString());

            float v1 = Float.valueOf((records.get(i - 1)).get("volume").toString());
            float v2 = Float.valueOf((records.get(i)).get("volume").toString());
            float v3 = Float.valueOf((records.get(i + 1)).get("volume").toString());

            if (c2 > c1 && v2 < v1) {
                if (c3 > c2) {
                    success++;
                }
                if (c3 < c2) {
                    fail++;
                }
            }

        }
        System.out.println(" s-" + success + " f-" + fail);
        if (fail != 0) {
            result.add(success / fail);
            System.out.println(success / fail);
        }
    }

    public static void analizeP(List<Map<String, Object>> records, List<Float> result) {
        Float successP = 0F;
        Float failP = 0F;
        for (int i = 1; i < (records.size() - 1); i++) {
            float c1 = Float.valueOf((records.get(i - 1)).get("close_price").toString());
            float c2 = Float.valueOf((records.get(i)).get("close_price").toString());
            float c3 = Float.valueOf((records.get(i + 1)).get("close_price").toString());

            float v1 = Float.valueOf((records.get(i - 1)).get("volume").toString());
            float v2 = Float.valueOf((records.get(i)).get("volume").toString());

            if (c2 > c1 && v2 < v1) {
                if (c3 > c2) {
                    successP += (c3 - c2);
                }
                if (c3 < c2) {
                    failP += (c2 - c3);
                }
            }

        }
        System.out.println(" s-" + successP + " f-" + failP);
        if (failP != 0) {
            result.add(successP / failP);
            System.out.println(successP / failP);
        }
    }

    public static void analizeD(List<Map<String, Object>> records, List<Float> result) {
        Float success = 0F;
        Float fail = 0F;
        for (int i = 1; i < (records.size() - 1); i++) {
            float c1 = Float.valueOf((records.get(i - 1)).get("close_price").toString());
            float c2 = Float.valueOf((records.get(i)).get("close_price").toString());
            float c3 = Float.valueOf((records.get(i + 1)).get("close_price").toString());

            float v1 = Float.valueOf((records.get(i - 1)).get("volume").toString());
            float v2 = Float.valueOf((records.get(i)).get("volume").toString());
            float v3 = Float.valueOf((records.get(i + 1)).get("volume").toString());

            if (c2 < c1 && v2 > v1) {
                if (c3 > c2) {
                    success++;
                }
                if (c3 < c2) {
                    fail++;
                }
            }

        }
        System.out.println(" s-" + success + " f-" + fail);
        if (fail != 0) {
            result.add(success / fail);
            System.out.println("D: " + success / fail);
        }
    }

    public static void analizeDP(List<Map<String, Object>> records, List<Float> result) {
        Float successP = 0F;
        Float failP = 0F;
        for (int i = 1; i < (records.size() - 1); i++) {
            float c1 = Float.valueOf((records.get(i - 1)).get("close_price").toString());
            float c2 = Float.valueOf((records.get(i)).get("close_price").toString());
            float c3 = Float.valueOf((records.get(i + 1)).get("close_price").toString());

            float v1 = Float.valueOf((records.get(i - 1)).get("volume").toString());
            float v2 = Float.valueOf((records.get(i)).get("volume").toString());

            if (c2 < c1 && v2 > v1) {
                if (c3 > c2) {
                    successP += (c3 - c2);
                }
                if (c3 < c2) {
                    failP += (c2 - c3);
                }
            }

        }
        System.out.println(" s-" + successP + " f-" + failP);
        if (failP != 0) {
            result.add(successP / failP);
            System.out.println("DP: " + successP / failP);
        }
    }

    public static void analizeTopD(List<Map<String, Object>> records, List<Float> result, int cap) {
        Float success = 0F;
        Float fail = 0F;

        Queue<Float> q = new ArrayBlockingQueue<Float>(cap);
        Float max = 0F;
        for (int i = 0; i < cap; i++) {
            Float temp = Float.valueOf(records.get(i).get("volume").toString());
            q.add(temp);

            if (temp > max) {
                max = temp;
            }
        }
        for (int i = cap; i < (records.size() - 1); i++) {
            Float cur = Float.valueOf(records.get(i).get("volume").toString());
            if (cur > max) {
                float c1 = Float.valueOf(records.get(i - 1).get("close_price").toString());
                float c2 = Float.valueOf(records.get(i).get("close_price").toString());
                float c3 = Float.valueOf(records.get(i + 1).get("close_price").toString());
                if (c2 < c1) {
                    if (c3 > c2) {
                        success++;
                    }
                    if (c3 < c2) {
                        fail++;
                    }
                }
            }
            q.remove();
            q.add(cur);

            Iterator<Float> it = q.iterator();
            max = 0f;
            while (it.hasNext()) {
                Float temp = (it.next());
                if (temp > max) {
                    max = temp;
                }
            }

        }
        // System.out.println(" s-" + success + " f-" + fail);
        if (fail != 0) {
            result.add(success / fail);
            // System.out.println("TOPD£º " + success / fail);
        }
    }

    public static void analizeTopU(List<Map<String, Object>> records, List<Float> result, int cap) {
        Float success = 0F;
        Float fail = 0F;

        Queue<Float> q = new ArrayBlockingQueue<Float>(cap);
        Float max = 0F;
        for (int i = 0; i < cap; i++) {
            Float temp = Float.valueOf((records.get(i)).get("volume").toString());
            q.add(temp);

            if (temp > max) {
                max = temp;
            }
        }
        for (int i = cap; i < (records.size() - 1); i++) {
            Float cur = Float.valueOf(records.get(i).get("volume").toString());
            if (cur > max) {
                float c1 = Float.valueOf(records.get(i - 1).get("close_price").toString());
                float c2 = Float.valueOf(records.get(i).get("close_price").toString());
                float c3 = Float.valueOf(records.get(i + 1).get("close_price").toString());
                if (c2 > c1) {
                    if (c3 > c2) {
                        success++;
                    }
                    if (c3 < c2) {
                        fail++;
                    }
                }
            }

            q.remove();
            q.add(cur);

            Iterator<Float> it = q.iterator();
            max = 0f;
            while (it.hasNext()) {
                Float temp = (it.next());
                if (temp > max) {
                    max = temp;
                }
            }

        }
        //System.out.println(" s-" + success + " f-" + fail);
        if (fail != 0) {
            result.add(success / fail);
            //System.out.println("TOPU£º " + success / fail);
        }
    }
}
