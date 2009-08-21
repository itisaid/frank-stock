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

import net.frank.stock.data.store.StoreDataO;

/**
 * TODO Comment of AnalizerA
 * 
 * @author frank.lizh
 * 
 */
public class AnalizerA {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List l = StoreDataO.queryCodeList();
		System.out.println("total stock:" + l.size());

		List resultD[] = new ArrayList[7];
		List resultU[] = new ArrayList[7];
		for (int m = 0; m < 7; m++) {
			resultD[m] = new ArrayList();
			resultU[m] = new ArrayList();
		}
		int len = l.size();
		for (int i = 0; i < l.size(); i++) {
			String sql = "select trade_date,open_price,high_price,low_price,close_price,volume from stock_data where code = '"
					+ (l.get(i)).toString() + "'";
			List records = StoreDataO.queryRecord(sql);
			// System.out.print((l.get(i)).toString() + ":");
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
			System.out.println("totalD: " + m + " : " + (av / ((float) len)));
			av = 0F;
			for (int j = 0; j < resultU[m].size(); j++) {
				av += Float.valueOf(resultU[m].get(j).toString());
			}
			System.out.println("totalU: " + m + " : " + (av / ((float) len)));
		}
		StoreDataO.closeConnection();
	}

	public static String[] getStocksCode() {
		return null;
	}

	public static void analize(List list, List result) {
		Float success = 0F;
		Float fail = 0F;
		for (int i = 1; i < (list.size() - 1); i++) {
			float c1 = Float.valueOf(((Map) list.get(i - 1)).get("close_price")
					.toString());
			float c2 = Float.valueOf(((Map) list.get(i)).get("close_price")
					.toString());
			float c3 = Float.valueOf(((Map) list.get(i + 1)).get("close_price")
					.toString());

			float v1 = Float.valueOf(((Map) list.get(i - 1)).get("volume")
					.toString());
			float v2 = Float.valueOf(((Map) list.get(i)).get("volume")
					.toString());
			float v3 = Float.valueOf(((Map) list.get(i + 1)).get("volume")
					.toString());

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

	public static void analizeP(List list, List result) {
		Float successP = 0F;
		Float failP = 0F;
		for (int i = 1; i < (list.size() - 1); i++) {
			float c1 = Float.valueOf(((Map) list.get(i - 1)).get("close_price")
					.toString());
			float c2 = Float.valueOf(((Map) list.get(i)).get("close_price")
					.toString());
			float c3 = Float.valueOf(((Map) list.get(i + 1)).get("close_price")
					.toString());

			float v1 = Float.valueOf(((Map) list.get(i - 1)).get("volume")
					.toString());
			float v2 = Float.valueOf(((Map) list.get(i)).get("volume")
					.toString());

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

	public static void analizeD(List list, List result) {
		Float success = 0F;
		Float fail = 0F;
		for (int i = 1; i < (list.size() - 1); i++) {
			float c1 = Float.valueOf(((Map) list.get(i - 1)).get("close_price")
					.toString());
			float c2 = Float.valueOf(((Map) list.get(i)).get("close_price")
					.toString());
			float c3 = Float.valueOf(((Map) list.get(i + 1)).get("close_price")
					.toString());

			float v1 = Float.valueOf(((Map) list.get(i - 1)).get("volume")
					.toString());
			float v2 = Float.valueOf(((Map) list.get(i)).get("volume")
					.toString());
			float v3 = Float.valueOf(((Map) list.get(i + 1)).get("volume")
					.toString());

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

	public static void analizeDP(List list, List result) {
		Float successP = 0F;
		Float failP = 0F;
		for (int i = 1; i < (list.size() - 1); i++) {
			float c1 = Float.valueOf(((Map) list.get(i - 1)).get("close_price")
					.toString());
			float c2 = Float.valueOf(((Map) list.get(i)).get("close_price")
					.toString());
			float c3 = Float.valueOf(((Map) list.get(i + 1)).get("close_price")
					.toString());

			float v1 = Float.valueOf(((Map) list.get(i - 1)).get("volume")
					.toString());
			float v2 = Float.valueOf(((Map) list.get(i)).get("volume")
					.toString());

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

	public static List getStockList() {
		return null;
	}

	public static void analizeTopD(List list, List result, int cap) {
		Float success = 0F;
		Float fail = 0F;

		Queue q = new ArrayBlockingQueue(cap);
		Float max = 0F;
		for (int i = 0; i < cap; i++) {
			Float temp = Float.valueOf(((Map) list.get(i)).get("volume")
					.toString());
			q.add(temp);

			if (temp > max) {
				max = temp;
			}
		}
		for (int i = cap; i < (list.size() - 1); i++) {
			Float cur = Float.valueOf(((Map) list.get(i)).get("volume")
					.toString());
			if (cur > max) {
				float c1 = Float.valueOf(((Map) list.get(i - 1)).get(
						"close_price").toString());
				float c2 = Float.valueOf(((Map) list.get(i)).get("close_price")
						.toString());
				float c3 = Float.valueOf(((Map) list.get(i + 1)).get(
						"close_price").toString());
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

			Iterator it = q.iterator();
			max = 0f;
			while (it.hasNext()) {
				Float temp = (Float) (it.next());
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

	public static void analizeTopU(List list, List result, int cap) {
		Float success = 0F;
		Float fail = 0F;

		Queue q = new ArrayBlockingQueue(cap);
		Float max = 0F;
		for (int i = 0; i < cap; i++) {
			Float temp = Float.valueOf(((Map) list.get(i)).get("volume")
					.toString());
			q.add(temp);

			if (temp > max) {
				max = temp;
			}
		}
		for (int i = cap; i < (list.size() - 1); i++) {
			Float cur = Float.valueOf(((Map) list.get(i)).get("volume")
					.toString());
			if (cur > max) {
				float c1 = Float.valueOf(((Map) list.get(i - 1)).get(
						"close_price").toString());
				float c2 = Float.valueOf(((Map) list.get(i)).get("close_price")
						.toString());
				float c3 = Float.valueOf(((Map) list.get(i + 1)).get(
						"close_price").toString());
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

			Iterator it = q.iterator();
			max = 0f;
			while (it.hasNext()) {
				Float temp = (Float) (it.next());
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
