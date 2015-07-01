<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="BIG5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.emp.model.*" %>
<jsp:useBean id="empSvc" scope="page" class="com.emp.model.EmpService"/>
<%
	String empid = (String)request.getSession().getAttribute("empid");
	EmpVO empVO = empSvc.getOneEmp(empid);
	pageContext.setAttribute("empVO", empVO);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<style>
	label{
		width:150px;
	}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
</head>
<body>
	<img src="<%= request.getContextPath() %>/images/img05.jpg">
	<table>
			<tr>
				<th><img src="<%=request.getContextPath()%>/showImage.do?action=emp&empid=${empVO.empid}" style="width:200px;height:200px"></th>
				<td>
					<h3>
						<label>姓名:</label>
						<b>${empVO.ename}</b>
					</h3>
					<h3>
						<label>到職日期:</label>
						<b>${empVO.hiredate}</b>
					</h3>
					<h3>
						<label>E-mail:</label>
						<b>${empVO.email}</b>
					</h3>
					<h3>
						<label>就職狀態:</label>
						<b>${emp_status[empVO.status]}</b>
					</h3>
				</td>
			</tr>
			
	</table>
</body>
</html>