<%@ page contentType="text/html; charset=UTF-8" pageEncoding="Big5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.post.model.*"%>
<%-- �����m�߱ĥ� EL ���g�k���� --%>

<%
	PostService postSvc = new PostService();
	List<PostVO> list = postSvc.getAll();
	pageContext.setAttribute("list",list);
%>
<jsp:useBean id="EmpSvc" scope="page" class="com.emp.model.EmpService" />

<html>
<head>
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<title>�Ҧ����i�T�� - listAllPost.jsp</title>
</head>
<body bgcolor='white'>

 

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
		<th>���i�Ӥ�</th>
		<th>�ק�</th>
		<th>�R��</th>
	</tr>
	<%@ include file="page1.file" %> 
	<c:forEach var="postVO" items="${list}" begin="<%=pageIndex%>" end="<%=pageIndex+rowsPerPage-1%>">
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
			<c:if test="${not empty postVO.pic}">
				<td><img src="<%= request.getContextPath() %>/showImage.do?pid=${postVO.pid}&action=post" height="120" width="120"></td>
			</c:if>
			<c:if test="${empty postVO.pic}">
				<td></td>
			</c:if>
			<td>
			  <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/back/post/post.do">
			     <input type="submit" value="�ק�" class="btn btn-info"> 
			     <input type="hidden" name=pid value="${postVO.pid}">
			     <input type="hidden" name="requestURL"	value="<%=request.getServletPath()%>"><!--�e�X�����������|��Controller-->
			   	 <input type="hidden" name="whichPage"	value="<%=whichPage%>">               <!--�e�X��e�O�ĴX����Controller-->
			     <input type="hidden" name="action"	value="getOne_For_Update"></FORM>
			</td>
			<td>
			  <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/back/post/post.do">
			    <input type="submit" value="�R��" class="btn btn-danger">
			    <input type="hidden" name="pid" value="${postVO.pid}">
			    <input type="hidden" name="requestURL"	value="<%=request.getServletPath()%>"><!--�e�X�����������|��Controller-->
			    <input type="hidden" name="whichPage"	value="<%=whichPage%>">               <!--�e�X��e�O�ĴX����Controller-->
			    <input type="hidden" name="action"value="delete"></FORM>
			</td>
		</tr>
	</c:forEach>
</table>
<%@ include file="page2.file" %>


</body>
</html>
