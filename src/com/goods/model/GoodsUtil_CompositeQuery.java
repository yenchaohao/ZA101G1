package com.goods.model;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class GoodsUtil_CompositeQuery {
	public static String get_Condition(String column, String[] value) {
		// 因為會員資料可能有很多筆 所以用 StringBuffer 可以串接
		StringBuffer addCondition = new StringBuffer();

		if ("g_name".equals(column)) {
			addCondition.append(" upper( " + column + " ) like upper('%"
					+ value[0] + "%') ");
		}

		else if ("member_id".equals(column)) {

			for (int i = 0; i < value.length; i++) {
				if (value.length == 1) {
					addCondition.append(column + " = '" + value[i] + "' ");
					break;
				} else {
					// ex: (member_id = 'M1012' or member_id = 'M1011' or
					// member_id = 'M1001')

					// i==(value.length-1) 判斷是不是最後一筆資料 最後一筆資料 後面要加上 ")" ex:
					// member_id = 'M1001')
					if (i == (value.length - 1))
						addCondition
								.append(column + " = '" + value[i] + "' ) ");
					// i==0 第一筆加上 "(" ex: (member_id = 'M1012' or
					else if (i == 0)
						addCondition.append(" ( " + column + " = '" + value[i]
								+ "'  or ");
					// 中間的筆數都不要印括號 ex: member_id = 'M1011' or
					else
						addCondition.append(column + " = '" + value[i]
								+ "'  or ");

				}
			}
		} else if ("groupid".equals(column)) {

			addCondition.append(column + " = " + value[0]);
		}
		// 金額的範圍
		else if ("g_price".equals(column)) {
			addCondition.append(column + " between " + value[0] + " and "
					+ value[1]);

		} else if ("goods_status".equals(column)) {
			for (int i = 0; i < value.length; i++) {
				if (value.length == 1) {
					addCondition.append(column + " = " + value[i]);
					break;
				} else {
					if (i == (value.length - 1))
					addCondition.append(column + " = " + value[i]+" )");
					else if(i == 0)
						addCondition.append(" ( "+column + " = " + value[i]+" or ");
					else
						addCondition.append(column + " = " + value[i]+" or ");
					
				}
			}
		}

		return addCondition.toString();
	}

	public static String getWhereCondition(Map<String, String[]> map) {
		Set<String> keys = map.keySet();
		StringBuffer sb = new StringBuffer();
		int count = 0;

		for (String key : keys) {

			String[] values = map.get(key);
			// 把不要的parameter 排除
			if (!"action".equals(key) && !"requestURL".equals(key)
					&& !"start_price".equals(key) && !"end_price".equals(key)
					&& !"mem_name".equals(key) && values != null
					&& values[0].trim().length() != 0 && values.length != 0
					&& !"whichPage".equals(key)) {
				// System.out.println(key+" "+map.get(key)[0]);
				count++;
				// 直接給values [陣列] 因為查出來的會員可能會有很多 個 價錢從高到低也有2筆資料
				String aCondition = get_Condition(key, values);
				if (count == 1)
					sb.append(" where " + aCondition);
				else
					sb.append(" and " + aCondition + " ");

			}
		}

		return sb.toString();
	}

	public static void main(String argv[]) {

		// 配合 req.getParameterMap()方法 回傳
		// java.util.Map<java.lang.String,java.lang.String[]> 之測試
		Map<String, String[]> map = new TreeMap<String, String[]>();
		map.put("g_name", new String[] { "Canon" });
		map.put("mem_name", new String[] { "ken" });
		map.put("member_id", new String[] { "M1001", "M1002", "M1003" });
		map.put("groupid", new String[] { "1" });
		map.put("start_price", new String[] { "100" });
		map.put("end_price", new String[] { "500" });
		map.put("action", new String[] { "CompositeQuery" });
		map.put("requestURL", new String[] { "requestURL" });
		map.put("g_price", new String[] { "100", "500" }); // 注意Map裡面會含有action的key
		map.put("goods_status", new String[] { "1", "0" ,"2"});

		String finalSQL = "select * from emp2 "
				+ GoodsUtil_CompositeQuery.getWhereCondition(map)
				+ "order by empno";
		System.out.println("●●finalSQL = " + finalSQL);

	}

}
