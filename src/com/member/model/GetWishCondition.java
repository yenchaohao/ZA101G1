package com.member.model;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.goods.model.GoodsUtil_CompositeQuery;

public class GetWishCondition {
	
	//select * from ex_member where my_wish like '%1%' and my_wish not like '1%';
	public static String Compose(String column,String[] value){
		StringBuffer sb = new StringBuffer();
			if("groupid".equals(column)){
				sb.append(" my_wish like "+"'"+value[0]+"%' ");
			}else if("my_wish".equals(column)){
				sb.append(" ( ( upper(my_wish) like upper('%"+value[0]+"%') or  lower(my_wish) like lower('%"+value[0]+"%') )  "
						+ "and ( upper(my_wish) not like upper('"+value[0]+"%' ) or   lower(my_wish) not like lower('"+value[0]+"%' ) )      ) ");
			}
			
//			else if("member_id".equals(column)){
//				for(int i = 0 ; i < value.length ; i++){
//					if(value.length == 1){
//						sb.append(column+" = '"+value[0]+"'");
//						break;
//					}
//					else{
//						if(i==(value.length-1))
//							sb.append(column+" = '"+value[i]+"' )");
//						else if(i == 0)
//							sb.append(" ( "+column+" = '"+value[i]+"' or ");
//						else 
//							sb.append(column+" = '"+value[i]+"' or ");
//					}
//				}
//				
//			}
			else if("mem_name".equals(column)){
				sb.append(" ( upper("+column+") like  upper('%"+value[0].trim()+"%') or  lower("+column+") like  lower('%"+value[0].trim()+"%') )" );
				//sb.append(column+" like '%"+value[0]+"%' " );
			}
		
		return sb.toString();
		
	}
	
	public static String getWishCondition(Map<String,String[]> map){
		Set<String> keys = map.keySet();
		StringBuffer sb = new StringBuffer();
		int count = 0;
		
		for(String key : keys){
			String[] values = map.get(key);
			if(!"action".equals(key) && values != null && values[0].trim().length() != 0 && values.length != 0 && !"requestURL".equals(key)){
				count++;
				String condition = Compose(key,values);
				if(count == 1)
					sb.append(" where "+condition);
				else
					sb.append(" and "+condition);
			
			}
		}
		
		return sb.toString();
	}
	
	public static void main(String argv[]) {

		// 配合 req.getParameterMap()方法 回傳 java.util.Map<java.lang.String,java.lang.String[]> 之測試
		Map<String, String[]> map = new TreeMap<String, String[]>();
		//map.put("groupid", new String[] { "2" });
		//map.put("my_wish", new String[] { "1" });
		//map.put("member_id", new String[] { "M1010","M1002","M1003" });
		map.put("mem_name",new String[] { "C" });
		 // 注意Map裡面會含有action的key

		String finalSQL = "select * from emp2 "
				          + GetWishCondition.getWishCondition(map)
				          + "order by empno";
		System.out.println("●●finalSQL = " + finalSQL);

	}
	
}
