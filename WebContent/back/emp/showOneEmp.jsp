<%@page import="com.emp.model.EmpService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="BIG5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.init.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
	<table border="1" class="table table-hover">
		<tr class="warning">
			<th>���u�Ӥ�</th>
			<th>���u�b��</th>
			<th>���u�m�W</th>
			<th>���u�K�X</th>
			<th>��¾���</th>
			<th>���u���A</th>
			<th>�ק�</th>
			<th>�{���v��</th>
		</tr>
		<tr align="center">
			<td>
				<img src="<%= request.getContextPath() %>/showImage.do?empid=${empVO.empid}&action=emp" height="120" width="120">
			</td>
			<td style="padding-top:60px">${empVO.empid}</td>
			<td style="padding-top:60px">${empVO.ename}</td>
			<td style="padding-top:60px">${empVO.password}</td>
			<td style="padding-top:60px">${empVO.hiredate}</td>
			<td style="padding-top:60px">${emp_status[empVO.status]}</td>
			<td style="padding-top:50px">
				<form method="post" action="<%= request.getContextPath() %>/back/emp/EmpServlet.do">
					<input type="submit" value="�ק�" class="btn btn-info">
			 		<input type="hidden" name="empid" value="${empVO.empid}">
					<input type="hidden" name="requestURL" value="<%=request.getServletPath()%>"><!--�e�X�����������|��Controller-->
					<input type="hidden" name="action" value="update_For_One">
				</form>
			</td>
			<td style="padding-top:50px">
				<form method="post" action="<%= request.getContextPath() %>/back/authority/authorityServlet.do">
					<input type="submit" value="�v��" class="btn btn-danger">
					<input type="hidden" name="empid" value="${empVO.empid}">
					<input type="hidden" name="action" value="showAuthority">
				</form>
			</td>
		</tr>
	</table>
</body>
</html>