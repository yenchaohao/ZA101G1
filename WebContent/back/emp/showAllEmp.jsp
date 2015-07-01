<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="BIG5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.emp.model.*"%>
<%@ page import="com.init.*"%>
<%@ page import="java.util.*"%>
<%  
	EmpService empSvc = new EmpService();
	List<EmpVO> list = empSvc.getAll();
	pageContext.setAttribute("list", list);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
<style>
	label,.form-control{
		float:left;
	}
</style>
</head>
<body>
	<script>
		$(function() {
			$("#from").datepicker({
				dateFormat: 'yy-mm-dd',
				defaultDate: "+1w",
				changeMonth: true,
				numberOfMonths: 1,
				onClose: function(selectedDate) {
					$("#to").datepicker("option", "minDate", selectedDate);
				}
			});
			
			$("#to").datepicker({
				dateFormat: 'yy-mm-dd',
				defaultDate: "+1w",
				changeMonth: true,
				numberOfMonths: 1,
				onClose: function(selectedDate) {
					$("#from").datepicker("option", "maxDate", selectedDate);
				}
			});
		});
	</script>

			<form method="post" action="<%= request.getContextPath() %>/back/emp/EmpServlet.do">
				<label for="empid">請輸入員工帳號:</label>
				<input type="text" name="empid" class="form-control" style="width:100px">
				<label for="ename">請輸入員工姓名:</label>
				<input type="text" name="ename" class="form-control" style="width:100px">
				<label for="status">請選擇就職狀態:</label>
				<select size="1" name="status" class="form-control" style="width:100px">
					<option value="">請選擇
					<option value="0">離職
					<option value="1">在職
				</select><p>
				<label for="hiredate">請選擇到職日期:從</label>
				<input type="text" name="hiredate" id="from" class="form-control" style="width:100px">
				<label>到</label>
				<input type="text" name="hiredate" id="to" class="form-control" style="width:100px">
				<p>
				<input type="submit" value="進階查詢" class="btn btn-success" id="submit">
				<input type="hidden" name="action" value="emp_compositeQuery">
			</form>
	<table border="1" class="table table-hover">
		<tr class="warning">
			<th>員工圖片</th>
			<th>員工帳號</th>
			<th>員工姓名</th>
			<th>員工密碼</th>
			<th>到職日期</th>
			<th>員工E-mail</th>
			<th>員工狀態</th>
			<th>修改</th>
			<th>現有權限</th>
		</tr>
		<%@ include file="page1.file" %> 
		<c:forEach var="empVO" items="${list}" begin="<%=pageIndex%>" end="<%=pageIndex+rowsPerPage-1%>">
			<tr align="center" ${(empVO.empid==param.empid)?'bgcolor=pinkyellow':''}> <!-- 與updateEmp.jsp請求參數empid做匹配 -->
				<td><img src="<%= request.getContextPath() %>/showImage.do?empid=${empVO.empid}&action=emp" height="120" width="120"></td>
				<td style="padding-top:60px">${empVO.empid}</td>
				<td style="padding-top:60px">${empVO.ename}</td>
				<td style="padding-top:60px">${empVO.password}</td>
				<td style="padding-top:60px">${empVO.hiredate}</td>
				<td style="padding-top:60px">${empVO.email}</td>
				<td style="padding-top:60px">
					${emp_status[empVO.status]} <!-- 使用Map工具顯示員工狀態 -->
				</td>
				<td style="padding-top:50px">
					<form method="post" action="<%= request.getContextPath() %>/back/emp/EmpServlet.do">
						<input type="submit" value="修改" class="btn btn-info">
						<input type="hidden" name="empid" value="${empVO.empid}">
						<input type="hidden" name="requestURL" value="<%=request.getServletPath()%>"><!--送出本網頁的路徑給Controller-->
						<input type="hidden" name="whichPage" value="<%=whichPage%>"> 				 <!--送出當前是第幾頁給Controller-->
						<input type="hidden" name="action" value="update_For_One">
					</form>
				</td>
				<td style="padding-top:50px">
					<form method="post" action="<%= request.getContextPath() %>/back/authority/authorityServlet.do">
						<input type="submit" value="權限" class="btn btn-danger">
						<input type="hidden" name="empid" value="${empVO.empid}">
						<input type="hidden" name="action" value="showAuthority">
					</form>
				</td>
			</tr>
		</c:forEach>
	</table>
	<%@ include file="page2.file" %>
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