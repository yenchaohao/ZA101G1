<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="BIG5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.emp.model.*"%>
<%@ page import="com.init.*"%>
<%  
	EmpVO empVO = (EmpVO) request.getAttribute("empVO"); //EmpServlet.java (Concroller), 存入req的empVO物件 (包括幫忙取出的empVO, 也包括輸入資料錯誤時的empVO物件)
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="<%= request.getContextPath() %>/javascript_css/emp/updateEmp.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
</head>
<body>
	<form method="post" action="EmpServlet.do" enctype="multipart/form-data">
		<table>
			<tr>
				<td>員工帳號:</td>
				<td><%= empVO.getEmpid() %></td>
			</tr>
			<tr>
				<td>員工姓名:</td>
				<td><input type="text" name="ename" value="<%= empVO.getEname() %>" class="form-control" style="width:100px"></td>
			</tr>
			<tr>
				<td>員工密碼:</td>
				<td><input type="password" name="password" value="${empVO.password}" class="form-control" style="width:100px"></td>
			</tr>
			<tr>
				<td>重複輸入密碼:</td>
				<td><input type="password" name="rePwd" value="${empVO.password}" class="form-control" style="width:100px"></td>
			</tr>
			<tr>
				<td>到職日期:</td>
				<td>
					<%= empVO.getHiredate() %>
					<input type="hidden" name="hiredate" value="<%= empVO.getHiredate() %>">
				</td>
			</tr>
			<tr>
				<td>員工圖片:</td>
				<td>
					<img src="<%= request.getContextPath() %>/showImage.do?empid=${empVO.empid}&action=emp" height="120" width="120" id="previewArea">
					<input type="file" name="pic" value="<%= empVO.getPic() %>" id="pic" class="btn btn-info" style="width:100px">
				</td>
			</tr>
			<tr>
				<td>員工E-mail:</td>
				<td>
					<input type="email" name="email" value="${empVO.email}" class="form-control" style="width:200px">
				</td>
			</tr>
			<tr>
				<td>員工狀態:</td>
				<td>
					<select size="1" name="status" class="form-control" style="width:80px">
						<option value="${(empVO.status==1)?1:0}" ${(empVO.status==empVO.status)?'selected':'' } >${(empVO.status==1)?'在職':'離職' }
						<option value="${(empVO.status==1)?0:1}" ${(empVO.status!=empVO.status)?'selected':'' } >${(empVO.status!=1)?'在職':'離職' }
					</select>
				</td>
			</tr>
		</table>
		<br>
		<input type="hidden" name="action" value="updateEmp">
		<input type="hidden" name="empid" value="<%=empVO.getEmpid()%>">
		<input type="hidden" name="requestURL" value="<%=request.getParameter("requestURL")%>"><!--接收原送出修改的來源網頁路徑後,再送給Controller準備轉交之用-->
		<input type="hidden" name="whichPage" value="<%=request.getParameter("whichPage")%>">  <!--只用於:showAllEmp.jsp-->
		<input type="submit" value="送出修改" class="btn btn-info">
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