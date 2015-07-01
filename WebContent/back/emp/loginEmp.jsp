<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.emp.model.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<link rel="stylesheet" href="<%= request.getContextPath() %>/javascript_css/emp/login.css">
<script src=""></script>
<title>My Website</title>
</head>
<body>
	<div id="anime">
			<img src="<%= request.getContextPath() %>/images/Logo1.png" id="logo1">
	</div>	
	<form method="post" action="EmpServlet.do">
		<label id="id">帳號:</label>
		<input type="text" name="empid" value="${empVO.empid}" id="empid">
		<label id="pas">密碼:</label>
		<input type="password" name="password" id="password">
		<input type="submit" value="登入" id="submit">
		<input type="hidden" name="action" value="empLogin">
		<c:if test="${not empty errorMsgs}">
			<font color='red'>
				<c:forEach var="message" items="${errorMsgs}">
					${message}
				</c:forEach>
			</font>
		</c:if>
	</form>
	
</body>
</html>