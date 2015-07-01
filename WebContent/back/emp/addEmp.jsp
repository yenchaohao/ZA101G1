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
<title>���u��Ʒs�W - addEmp.jsp</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
</head>
<body>
	<h1>�п�J���u�򥻸��</h1>
	<form method="Post" action="EmpServlet.do" enctype="multipart/form-data">
		<label for="ename">�п�J���u�m�W</label>
		<input type="text" name="ename" value="${empVO.ename}" id="ename" class="form-control" style="width:100px"> <%--EL�g�k--%>
		<br>
		<label for="email">�п�JE-mail</label>
		<input type="email" name="email" id="email" class="form-control" style="width:200px">
		<br>
		<%java.sql.Date date_SQL = new java.sql.Date(System.currentTimeMillis());%>
		<label for="hiredate">�п�ܤ��</label>
		<input type="date" name="hiredate" value="<%= (empVO==null) ? date_SQL : empVO.getHiredate()%>" id="hiredate" class="form-control" style="width:180px"> <%--JSP�g�k--%>
		<br>
		<label for="pic">�п�ܹϤ�</label>
		<input type="file" name="pic" id="pic" class="btn btn-info">
		<br>
		<nav id="previewArea"></nav>
		<br>
		<input type="submit" value="�e�X" class="btn btn-info">
		<input type="hidden" name="action" value="insertEmp">
	</form>
	<div>
		<c:if test="${not empty errorMsgs}">
			<font color='red'>�Эץ��H�U���~:
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