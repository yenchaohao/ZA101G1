<%@ page contentType="text/html; charset=UTF-8" pageEncoding="Big5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.post.model.*"%>
<%-- �����m�߱ĥ� EL ���g�k���� --%>

<jsp:useBean id="listPosts_ByEmpid" scope="request" type="java.util.List" />
<jsp:useBean id="EmpSvc" scope="page" class="com.emp.model.EmpService" />
<html>
<head>
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<title>���u�o�G���i - listPosts_ByEmpid.jsp</title>
</head>
<body bgcolor='white'>

<table border='1' cellpadding='5' cellspacing='0' class="table">
	<tr bgcolor='#CCCCFF' align='center' valign='middle' height='20' >
		<td>
		<h3>���u�o�G���i - listPosts_ByEmpid.jsp</h3>
		<a href="<%=request.getContextPath()%>/back/post/select_page.jsp"><img src="images/back1.gif" width="100" height="32" border="0">�^����</a>
		</td>
	</tr>
</table>

<%-- ���~��C --%>
<c:if test="${not empty errorMsgs}">
	<font color='red'>�Эץ��H�U���~:
	<ul>
		<c:forEach var="message" items="${errorMsgs}">
			<li>${message}</li>
		</c:forEach>
	</ul>
	</font>
</c:if>

<table border='1' class="table table-hover">
	<tr class="warning">
		<th>���i�s��</th>		
		<th>���i���D</th>
		<th>���i���e</th>
		<th>���i���/�ɶ�</th>
		<th>���i��</th>
		<th>�ק�</th>
		<th>�R��</th>
	</tr>
	
	<c:forEach var="postVO" items="${listPosts_ByEmpid}" >
		<tr align='center' valign='middle' ${(postVO.pid==param.pid) ? 'bgcolor=#CCCCFF':''}><!--�N�ק諸���@���[�J����Ӥw-->
			<td>${postVO.pid}</td>
			<td>${postVO.title}</td>
			<td>${postVO.post}</td>	
			<td>${postVO.postdate}</td>	
			<td><c:forEach var="empVO" items="${EmpSvc.all}">
                    <c:if test="${postVO.empid==empVO.empid}">
	                    ${empVO.empid}�i${empVO.ename}�j
                    </c:if>
                </c:forEach>
			</td>
			<td>
			  <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/back/post/post.do">
			    <input type="submit" value="�ק�" class="btn btn-info"> 
			    <input type="hidden" name=pid value="${postVO.pid}">
			    <input type="hidden" name="requestURL"	value="<%=request.getServletPath()%>"><!--�e�X�����������|��Controller-->
			    <input type="hidden" name="action"	value="getOne_For_Update"></FORM>
			</td>
			<td>
			  <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/back/post/post.do">
			    <input type="submit" value="�R��" class="btn btn-danger">
			    <input type="hidden" name="pid" value="${postVO.pid}">
			    <input type="hidden" name="requestURL"	value="<%=request.getServletPath()%>"><!--�e�X�����������|��Controller-->
			    <input type="hidden" name="action"value="delete"></FORM>
			</td>
		</tr>
	</c:forEach>
</table>

<br>�����������|:<br><b>
   <font color=blue>request.getServletPath():</font> <%= request.getServletPath()%><br>
   <font color=blue>request.getRequestURI(): </font> <%= request.getRequestURI()%> </b>
</body>
</html>
