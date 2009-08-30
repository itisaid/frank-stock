/**
 * Project: Stock
 * 
 * File Created at 2009-1-16
 * $Id$
 */
package net.frank.stock.analize;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author frank.lizh
 */
public class AnalyzerTop {
    public static void analize(List list, List result, int cap) {
        Float success = 0F;
        Float fail = 0F;

        Queue q = new ArrayBlockingQueue(cap);
        int max = 0;
        for (int i = 0; i < cap; i++) {
            int temp = Integer.valueOf(((Map) list.get(i)).get("volume").toString());
            q.add(temp);

            if (temp > max) {
                max = temp;
            }
        }
        for (int i = cap; i < (list.size() - 1); i++) {
            int cur = Integer.valueOf(((Map) list.get(i)).get("volume").toString());
            if (cur > max) {
                float c2 = Float.valueOf(((Map) list.get(i)).get("close_price").toString());
                float c3 = Float.valueOf(((Map) list.get(i + 1)).get("close_price").toString());

                if (c3 > c2) {
                    success++;
                }
                if (c3 < c2) {
                    fail++;
                }
            }

            q.remove();
            q.add(list.get(i));

            Iterator it = q.iterator();
            max = 0;
            while (it.hasNext()) {
                int temp = (Integer) (it.next());
                if (temp > max) {
                    max = temp;
                }
            }

        }
        System.out.println(" s-" + success + " f-" + fail);
        if (fail != 0) {
            result.add(success / fail);
            System.out.println(success / fail);
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

    }
}
