<%@ page contentType="text/html; charset=UTF-8" pageEncoding="Big5"%>
<%@ page import="com.emp.model.*"%>
<%@ page import="com.post.model.*"%>
<%-- 此頁練習採用 Script 的寫法取值 --%>

<%-- 取出 Concroller PostServlet.java已存入request的PostVO物件--%>
<%PostVO postVO = (PostVO) request.getAttribute("postVO");%>

<%-- 取出 對應的EmpVO物件--%>
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
<title>公告訊息 - listOnePost.jsp</title>
</head>
<body bgcolor='white'>
<table border='1' cellpadding='5' cellspacing='0'  class="table">
	<tr bgcolor='#CCCCFF' align='center' valign='middle' height='20'>
		<td>
		<h3>公告訊息 - listOnePost.jsp</h3>
		<a href="<%=request.getContextPath()%>/back/post/select_page.jsp"><img src="images/back1.gif" width="100" height="32" border="0">回首頁</a>
		</td>
	</tr>
</table>

<table border='1' class="table table-hover">
	<tr class="warning">
		<th>公告編號</th>		
		<th>公告標題</th>
		<th>公告內容</th>
		<th>公告日期/時間</th>
		<th>公告者</th>
	</tr>
	<tr align='center' valign='middle'>
		<td>${postVO.pid}</td>
		<td>${postVO.title}</td>
		<td>${postVO.post}</td>
		<td>${postVO.postdate}</td>
		<td><%=postVO.getEmpid()%>【<%=empVO.getEname()%>】</td>
	</tr>
</table>

</body>
</html>
