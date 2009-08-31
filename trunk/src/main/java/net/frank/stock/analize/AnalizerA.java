/**
 * Project: Stock
 * 
 * File Created at 2009-1-15
 * $Id$
 */
package net.frank.stock.analize;

import java.util.ArrayList;
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
            logger.info("Start to analize " + codeList.get(i) + " (" + i + "/" + codeList.size()
                    + ")");

            String sql = "select trade_date, open_price, high_price, low_price, close_price, volume"
                    + " from stock_data" + " where code = '" + (codeList.get(i)).toString() + "'";
            List<Map<String, Object>> recordList = readData.queryRecord(sql);

            logger.info(codeList.get(i) + " has " + recordList.size() + " records.");

            for (int k = 3; k < 10; k++) {
                analizeTopD(recordList, resultD[k - 3], k);
                analizeTopU(recordList, resultU[k - 3], k);
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
        for (int i = 1; i < records.size() - 1; i++) {
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

    /**
     * ���㽻�׳����¸ߡ����̱�ǰһ���µ�������£���һ�����̷����ļ���
     * 
     * @param recordList
     * @param result
     * @param cap
     */
    public static void analizeTopD(List<Map<String, Object>> recordList, List<Float> result, int cap) {
        Float success = 0F;
        Float fail = 0F;

        // �ҳ����׹��� ǰN��Ԫ��������ֵ����Ϊ��ʷ����
        Queue<Float> volumeQueue = new ArrayBlockingQueue<Float>(cap);
        Float max = 0F;
        for (int i = 0; i < cap; i++) {
            Float volume = (Float) recordList.get(i).get("volume");
            volumeQueue.add(volume);

            if (volume > max) {
                max = volume;
            }
        }

        // ����N֮���Ԫ��
        for (int i = cap; i < recordList.size() - 1; i++) {
            Float cur = (Float) recordList.get(i).get("volume");

            // ���׹�������֮ǰ��ʷ�����ֵ��Ҳ���� �����¸�
            if (cur > max) {
                // ǰ������� ���̼����� 
                float c1 = (Float) recordList.get(i - 1).get("close_price");
                float c2 = (Float) recordList.get(i).get("close_price");
                float c3 = (Float) recordList.get(i + 1).get("close_price");

                // �������µ�
                if (c2 < c1) {
                    if (c2 == c3) {
                        continue;
                    }

                    // ��һ������ݷ���
                    if ((c3 > c2)) {
                        success++;
                    }
                    // ��һ������ݻ����µ�
                    else {
                        fail++;
                    }
                }
            }

            // �޸�Queue�����µ��Ǹ�����
            volumeQueue.remove();
            volumeQueue.add(cur);

            // �����ҳ�����ֵ
            max = 0f;
            for (Float temp : volumeQueue) {
                if (temp > max) {
                    max = temp;
                }
            }

        }
        logger.info(" s-" + success + " f-" + fail);

        if (fail != 0) {
            result.add(success / fail);
            logger.info("TOPD: " + success / fail);
        } else {
            logger.info("TOPD: ALL success!");
        }

    }

    /**
     * ���㽻�׳����¸ߡ����̱�ǰһ�����������£���һ������� �ٶ�����ļ���
     * 
     * @param recordList
     * @param result
     * @param cap
     */
    public static void analizeTopU(List<Map<String, Object>> recordList, List<Float> result, int cap) {
        Float success = 0F;
        Float fail = 0F;

        Queue<Float> volumeQueue = new ArrayBlockingQueue<Float>(cap);

        Float max = 0F;
        for (int i = 0; i < cap; i++) {
            Float volume = (Float) recordList.get(i).get("volume");
            volumeQueue.add(volume);

            if (volume > max) {
                max = volume;
            }
        }

        for (int i = cap; i < recordList.size() - 1; i++) {
            Float cur = (Float) recordList.get(i).get("volume");
            if (cur > max) {
                float c1 = (Float) recordList.get(i - 1).get("close_price");
                float c2 = (Float) recordList.get(i).get("close_price");
                float c3 = (Float) recordList.get(i + 1).get("close_price");

                // ��������������
                if (c2 > c1) {
                    // ��һ������� �ٶ�����
                    if (c3 > c2) {
                        success++;
                    }
                    // ��һ��������µ�
                    if (c3 < c2) {
                        fail++;
                    }
                }
            }

            volumeQueue.remove();
            volumeQueue.add(cur);

            max = 0f;
            for (Float temp : volumeQueue) {
                if (temp > max) {
                    max = temp;
                }
            }
        }
        logger.info(" s-" + success + " f-" + fail);

        if (fail != 0) {
            result.add(success / fail);

            logger.info("TOPU: " + success / fail);
        } else {
            logger.info("TOPU: : ALL success!");
        }
    }
}
