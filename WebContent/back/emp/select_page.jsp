<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="BIG5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.emp.model.*"%>
<%@ page import="java.util.*"%>
<% 
	EmpService empSvc = new EmpService();
	pageContext.setAttribute("empSvc", empSvc);
%>
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
	<h3>員工資料查詢:</h3>
	<ul>
		<li>
			<a href="showAllEmp.jsp">顯示全部員工</a>
		</li>
		<p>
		<li>
			<form method="post" action="EmpServlet.do">
				<label for="empid">請輸入員工帳號(如E1001):</label>
				<input type="text" name="empid" class="form-control" style="width:100px">
				<input type="submit" value="送出" class="btn btn-info btn-sm">
				<input type="hidden" name="action" value="get_one_emp">
			</form>
		</li>
		<br>
		<li>
			<form method="post" action="EmpServlet.do">
				<b>請選擇員工帳號:</b>
				<select size="1" name="empid" class="form-control" style="width:100px">
		         <c:forEach var="empVO" items="${empSvc.all}" > 
		          <option value="${empVO.empid}">${empVO.empid}
		         </c:forEach>   
		       </select>
				<input type="submit" value="送出" class="btn btn-info btn-sm">
				<input type="hidden" name="action" value="get_one_emp">
			</form>
		</li>
		<br>
		<li>
			<form method="post" action="EmpServlet.do">
				<b>請選擇員工姓名:</b>
				<select size="1" name="empid" class="form-control" style="width:100px">
		         <c:forEach var="empVO" items="${empSvc.all}" > 
		          <option value="${empVO.empid}">${empVO.ename}
		         </c:forEach>   
		       </select>
				<input type="submit" value="送出" class="btn btn-info btn-sm">
				<input type="hidden" name="action" value="get_one_emp">
			</form>
		</li>
		<p>
		<li>
			<a href="addEmp.jsp">新增員工</a>
		</li>
	</ul>
	<c:if test="${not empty errorMsgs}">
	<font color='red'>請修正以下錯誤:
	<ul>
		<c:forEach var="message" items="${errorMsgs}">
			<li>${message}</li>
		</c:forEach>
	</ul>
	</font>
</c:if>
</body>
</html>