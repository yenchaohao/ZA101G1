<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="Big5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.emp.model.*" %>
<% EmpVO empVO = (EmpVO) request.getAttribute("empVO"); %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="<%= request.getContextPath() %>/javascript_css/emp/addEmp.css">
<script src="<%= request.getContextPath() %>/javascript_css/emp/addEmp.js"></script>
<title>員工資料新增 - addEmp.jsp</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
</head>
<body>
	<h1>請輸入員工基本資料</h1>
	<form method="Post" action="EmpServlet.do" enctype="multipart/form-data">
		<label for="ename">請輸入員工姓名</label>
		<input type="text" name="ename" value="${empVO.ename}" id="ename" class="form-control" style="width:100px"> <%--EL寫法--%>
		<br>
		<label for="email">請輸入E-mail</label>
		<input type="email" name="email" id="email" class="form-control" style="width:200px">
		<br>
		<%java.sql.Date date_SQL = new java.sql.Date(System.currentTimeMillis());%>
		<label for="hiredate">請選擇日期</label>
		<input type="date" name="hiredate" value="<%= (empVO==null) ? date_SQL : empVO.getHiredate()%>" id="hiredate" class="form-control" style="width:180px"> <%--JSP寫法--%>
		<br>
		<label for="pic">請選擇圖片</label>
		<input type="file" name="pic" id="pic" class="btn btn-info">
		<br>
		<nav id="previewArea"></nav>
		<br>
		<input type="submit" value="送出" class="btn btn-info">
		<input type="hidden" name="action" value="insertEmp">
	</form>
	<div>
		<c:if test="${not empty errorMsgs}">
			<font color='red'>請修正以下錯誤:
			<ul>
				<c:forEach var="message" items="${errorMsgs}">
					<li>${message}</li>
				</c:forEach>
			</ul>
			</font>
		</c:if>
	</div>
</body>
</html>