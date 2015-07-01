package com.send.model;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class SendUtil_CompositeQuery {
	public static String get_Condition(String column, String[] value) {
		// �]���|����ƥi�঳�ܦh�� �ҥH�� StringBuffer �i�H�걵
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
			// �⤣�n�� parameter �ư�
			if (!"action".equals(key) && !"requestURL".equals(key) && values != null 
					&& values[0].trim().length() != 0 && values.length != 0
					&& !"whichPage".equals(key) ) {
				//System.out.println(key+" "+map.get(key)[0]);
				count++;
				// ������values [�}�C] �]���d�X�Ӫ��|���i��|���ܦh �� �����q����C�]��2�����
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

		// �t�X req.getParameterMap()��k �^��
		// java.util.Map<java.lang.String,java.lang.String[]> ������
		Map<String, String[]> map = new TreeMap<String, String[]>();
		map.put("sid", new String[] { "1015" });
		map.put("mem_address", new String[] { "��" });
		map.put("mem_name", new String[] { "c" });
	

		String finalSQL = "select * from emp2 "
				+ SendUtil_CompositeQuery.getWhereCondition(map)
				+ "order by empno";
		System.out.println("����finalSQL = " + finalSQL);

	}

}
