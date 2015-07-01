<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="BIG5"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
 <%@ page import="com.serial.model.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<jsp:useBean id="svc" scope="page" class="com.serial.model.SerialService" />
<html>
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
</head>
<body>
<a href="<%= request.getContextPath() %>/back/serial/addSerial.jsp" target="iframe">�s�W�I��</a><p>
	<table border="1" class="table table-hover">
	<tr class="warning"><td>�Ǹ�</td><td>���B</td><td>�ϥΪ�</td></tr>
<c:forEach var="serialVO" items="${svc.all}">
	
	<tr>
			<td>${serialVO.serial_number}</td>
			<td>${serialVO.money}</td>
			<td>${serialVO.member_id}</td>
	</tr>
	
</c:forEach>
	</table>
</body>
</html>