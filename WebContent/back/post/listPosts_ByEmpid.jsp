<%@ page contentType="text/html; charset=UTF-8" pageEncoding="Big5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.post.model.*"%>
<%-- 此頁練習採用 EL 的寫法取值 --%>

<jsp:useBean id="listPosts_ByEmpid" scope="request" type="java.util.List" />
<jsp:useBean id="EmpSvc" scope="page" class="com.emp.model.EmpService" />
<html>
<head>
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<title>員工發佈公告 - listPosts_ByEmpid.jsp</title>
</head>
<body bgcolor='white'>

<table border='1' cellpadding='5' cellspacing='0' class="table">
	<tr bgcolor='#CCCCFF' align='center' valign='middle' height='20' >
		<td>
		<h3>員工發佈公告 - listPosts_ByEmpid.jsp</h3>
		<a href="<%=request.getContextPath()%>/back/post/select_page.jsp"><img src="images/back1.gif" width="100" height="32" border="0">回首頁</a>
		</td>
	</tr>
</table>

<%-- 錯誤表列 --%>
<c:if test="${not empty errorMsgs}">
	<font color='red'>請修正以下錯誤:
	<ul>
		<c:forEach var="message" items="${errorMsgs}">
			<li>${message}</li>
		</c:forEach>
	</ul>
	</font>
</c:if>

<table border='1' class="table table-hover">
	<tr class="warning">
		<th>公告編號</th>		
		<th>公告標題</th>
		<th>公告內容</th>
		<th>公告日期/時間</th>
		<th>公告者</th>
		<th>修改</th>
		<th>刪除</th>
	</tr>
	
	<c:forEach var="postVO" items="${listPosts_ByEmpid}" >
		<tr align='center' valign='middle' ${(postVO.pid==param.pid) ? 'bgcolor=#CCCCFF':''}><!--將修改的那一筆加入對比色而已-->
			<td>${postVO.pid}</td>
			<td>${postVO.title}</td>
			<td>${postVO.post}</td>	
			<td>${postVO.postdate}</td>	
			<td><c:forEach var="empVO" items="${EmpSvc.all}">
                    <c:if test="${postVO.empid==empVO.empid}">
	                    ${empVO.empid}【${empVO.ename}】
                    </c:if>
                </c:forEach>
			</td>
			<td>
			  <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/back/post/post.do">
			    <input type="submit" value="修改" class="btn btn-info"> 
			    <input type="hidden" name=pid value="${postVO.pid}">
			    <input type="hidden" name="requestURL"	value="<%=request.getServletPath()%>"><!--送出本網頁的路徑給Controller-->
			    <input type="hidden" name="action"	value="getOne_For_Update"></FORM>
			</td>
			<td>
			  <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/back/post/post.do">
			    <input type="submit" value="刪除" class="btn btn-danger">
			    <input type="hidden" name="pid" value="${postVO.pid}">
			    <input type="hidden" name="requestURL"	value="<%=request.getServletPath()%>"><!--送出本網頁的路徑給Controller-->
			    <input type="hidden" name="action"value="delete"></FORM>
			</td>
		</tr>
	</c:forEach>
</table>

<br>本網頁的路徑:<br><b>
   <font color=blue>request.getServletPath():</font> <%= request.getServletPath()%><br>
   <font color=blue>request.getRequestURI(): </font> <%= request.getRequestURI()%> </b>
</body>
</html>
