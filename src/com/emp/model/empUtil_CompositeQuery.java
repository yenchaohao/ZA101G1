package com.emp.model;

import java.sql.Time;
import java.util.*;


public class empUtil_CompositeQuery {
	
	public static String get_Condition(String column, String[] value){
		
		String aCondition = null;
		
		java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
		
		if("empid".equals(column) || "ename".equals(column)){
			aCondition = column + " like '%" + value[0] + "%'";
		} else if("status".equals(column)){
			aCondition = column + "=" + value[0];
		} else if("hiredate".equals(column)){
			aCondition = "to_char(" + column + ",'yyyy-mm-dd')>='" + value[0] + "'" + " and to_char(" + column + ",'yyyy-mm-dd')<='" + value[1] + "'";
		}
		
		return aCondition + " ";
	}
	
	public static String get_WhereCondition(Map<String, String[]> map){
		Set<String> keys = map.keySet();
		StringBuffer whereCondition = new StringBuffer();
		int count = 0;
		
		for(String key : keys){
			String[] value = map.get(key);
			// 把不要的parameter 排除
			if(value != null && value[0].trim().length() != 0 && !"action".equals(key)){
				count++;
				String aCondition = get_Condition(key, value);
				
				if(count == 1)
					whereCondition.append(" where " + aCondition);
				else 
					whereCondition.append(" and " + aCondition);
			}
		}
		
		return whereCondition.toString();
		
	}
	
	public static void main(String argv[]) {

		// 配合 req.getParameterMap()方法 回傳 java.util.Map<java.lang.String,java.lang.String[]> 之測試
		Map<String, String[]> map = new TreeMap<String, String[]>();
		map.put("empid", new String[] { "E1001" });
		map.put("ename", new String[] { "周迅" });
		map.put("status", new String[] { "1" });
		map.put("hiredate", new String[] { "2015-05-01" , "2015-07-07"});
		map.put("action", new String[] { "getXXX" }); // 注意Map裡面會含有action的key

		String finalSQL = "select * from ex_emp "
				          + empUtil_CompositeQuery.get_WhereCondition(map)
				          + "order by empid";
		System.out.println("●●finalSQL = " + finalSQL);

	}
	
}
