<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="BIG5"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
 <%@ page import="com.message.model.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
<script
	src="<%=request.getContextPath()%>/javascript_css/message/new.js"></script>
</head>
<body>
<%-- ���~��C --%>
<c:if test="${not empty errorMsgs}">
	<font color='red'>�Эץ��H�U���~:
	<ul>
		<c:forEach var="message" items="${errorMsgs}">
			<li>${message}</li>
		</c:forEach>
	</ul>
	</font>
</c:if>
<form action="<%= request.getContextPath() %>/back/message/message.do" method="post">

<!-- <label for="member_id">mem_id</label> -->
<input type="hidden"  name="member_id"  value="${member_id}" size="10" maxlength="10"><br>

<label for="title">���D</label>
<input type="text"  name="title" size="50" maxlength="50" value="${messageVO.title}"><br>

<textarea  cols="90" rows="9" name="message" >${messageVO.message}</textarea><br>

<input type="hidden" name="action" value="new_message">
<input type="submit" value="�e�X">

</form>

</body>
</html>