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
				<label for="empid">�п�J���u�b��:</label>
				<input type="text" name="empid" class="form-control" style="width:100px">
				<label for="ename">�п�J���u�m�W:</label>
				<input type="text" name="ename" class="form-control" style="width:100px">
				<label for="status">�п�ܴN¾���A:</label>
				<select size="1" name="status" class="form-control" style="width:100px">
					<option value="">�п��
					<option value="0">��¾
					<option value="1">�b¾
				</select><p>
				<label for="hiredate">�п�ܨ�¾���:�q</label>
				<input type="text" name="hiredate" id="from" class="form-control" style="width:100px">
				<label>��</label>
				<input type="text" name="hiredate" id="to" class="form-control" style="width:100px">
				<p>
				<input type="submit" value="�i���d��" class="btn btn-success" id="submit">
				<input type="hidden" name="action" value="emp_compositeQuery">
			</form>
	<table border="1" class="table table-hover">
		<tr class="warning">
			<th>���u�Ϥ�</th>
			<th>���u�b��</th>
			<th>���u�m�W</th>
			<th>���u�K�X</th>
			<th>��¾���</th>
			<th>���uE-mail</th>
			<th>���u���A</th>
			<th>�ק�</th>
			<th>�{���v��</th>
		</tr>
		<%@ include file="page1.file" %> 
		<c:forEach var="empVO" items="${list}" begin="<%=pageIndex%>" end="<%=pageIndex+rowsPerPage-1%>">
			<tr align="center" ${(empVO.empid==param.empid)?'bgcolor=pinkyellow':''}> <!-- �PupdateEmp.jsp�ШD�Ѽ�empid���ǰt -->
				<td><img src="<%= request.getContextPath() %>/showImage.do?empid=${empVO.empid}&action=emp" height="120" width="120"></td>
				<td style="padding-top:60px">${empVO.empid}</td>
				<td style="padding-top:60px">${empVO.ename}</td>
				<td style="padding-top:60px">${empVO.password}</td>
				<td style="padding-top:60px">${empVO.hiredate}</td>
				<td style="padding-top:60px">${empVO.email}</td>
				<td style="padding-top:60px">
					${emp_status[empVO.status]} <!-- �ϥ�Map�u����ܭ��u���A -->
				</td>
				<td style="padding-top:50px">
					<form method="post" action="<%= request.getContextPath() %>/back/emp/EmpServlet.do">
						<input type="submit" value="�ק�" class="btn btn-info">
						<input type="hidden" name="empid" value="${empVO.empid}">
						<input type="hidden" name="requestURL" value="<%=request.getServletPath()%>"><!--�e�X�����������|��Controller-->
						<input type="hidden" name="whichPage" value="<%=whichPage%>"> 				 <!--�e�X��e�O�ĴX����Controller-->
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
		</c:forEach>
	</table>
	<%@ include file="page2.file" %>
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