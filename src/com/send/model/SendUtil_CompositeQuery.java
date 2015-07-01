package com.send.model;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class SendUtil_CompositeQuery {
	public static String get_Condition(String column, String[] value) {
		// 因為會員資料可能有很多筆 所以用 StringBuffer 可以串接
		StringBuffer addCondition = new StringBuffer();

		if ("sid".equals(column)) {
			addCondition.append(column +" = "+value[0]);
		}
		else if ("mem_address".equals(column)) {
			addCondition.append(" upper( membervo.address ) like upper('%"+value[0]+"%')");
		} else if ("mem_name".equals(column)) {
			addCondition.append(" upper(membervo."+column +")  like upper('%"+value[0]+"%')");
		}
		else if ("status".equals(column)) {
			addCondition.append(column +" = "+value[0]);
		}
		return addCondition.toString();
	}

	public static String getWhereCondition(Map<String, String[]> map) {
		Set<String> keys = map.keySet();
		StringBuffer sb = new StringBuffer();
		int count = 0;

		for (String key : keys) {

			String[] values = map.get(key);
			// 把不要的 parameter 排除
			if (!"action".equals(key) && !"requestURL".equals(key) && values != null 
					&& values[0].trim().length() != 0 && values.length != 0
					&& !"whichPage".equals(key) ) {
				//System.out.println(key+" "+map.get(key)[0]);
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
		map.put("sid", new String[] { "1015" });
		map.put("mem_address", new String[] { "中" });
		map.put("mem_name", new String[] { "c" });
	

		String finalSQL = "select * from emp2 "
				+ SendUtil_CompositeQuery.getWhereCondition(map)
				+ "order by empno";
		System.out.println("●●finalSQL = " + finalSQL);

	}

}
