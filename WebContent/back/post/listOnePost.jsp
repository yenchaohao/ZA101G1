<%@ page contentType="text/html; charset=UTF-8" pageEncoding="Big5"%>
<%@ page import="com.emp.model.*"%>
<%@ page import="com.post.model.*"%>
<%-- �����m�߱ĥ� Script ���g�k���� --%>

<%-- ���X Concroller PostServlet.java�w�s�Jrequest��PostVO����--%>
<%PostVO postVO = (PostVO) request.getAttribute("postVO");%>

<%-- ���X ������EmpVO����--%>
<%
  EmpService empSvc = new EmpService();
  EmpVO empVO = empSvc.getOneEmp(postVO.getEmpid());
%>
<html>
<head>
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<title>���i�T�� - listOnePost.jsp</title>
</head>
<body bgcolor='white'>
<table border='1' cellpadding='5' cellspacing='0'  class="table">
	<tr bgcolor='#CCCCFF' align='center' valign='middle' height='20'>
		<td>
		<h3>���i�T�� - listOnePost.jsp</h3>
		<a href="<%=request.getContextPath()%>/back/post/select_page.jsp"><img src="images/back1.gif" width="100" height="32" border="0">�^����</a>
		</td>
	</tr>
</table>

<table border='1' class="table table-hover">
	<tr class="warning">
		<th>���i�s��</th>		
		<th>���i���D</th>
		<th>���i���e</th>
		<th>���i���/�ɶ�</th>
		<th>���i��</th>
	</tr>
	<tr align='center' valign='middle'>
		<td>${postVO.pid}</td>
		<td>${postVO.title}</td>
		<td>${postVO.post}</td>
		<td>${postVO.postdate}</td>
		<td><%=postVO.getEmpid()%>�i<%=empVO.getEname()%>�j</td>
	</tr>
</table>

</body>
</html>
