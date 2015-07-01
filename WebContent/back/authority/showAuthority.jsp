<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="BIG5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.authority.model.*"%>
<%@ page import="com.authority_list.model.*"%>
<%@ page import="java.util.*"%>

<jsp:useBean id="authority_listSvc" scope="page" class="com.authority_list.model.Authority_listService" />
<jsp:useBean id="authoritySvc" scope="page" class="com.authority.model.AuthorityService" />
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
<div style="z-index:1;position:absolute;">
	<h1>${empid}員工現有權限</h1> <!--來自authorityServlet的req-->
	<c:if test="${not empty showAuthority}">
	<form method="post" action="<%= request.getContextPath() %>/back/authority/authorityServlet.do">
		<table border="1" class="table table-hover">
			<tr class="info">
				<th>員工帳號</th>
				<th>權限編號</th>
				<th>權限功能</th>
				<th><input type="submit" value="刪除" class="btn btn-danger"></th>
			</tr>
			<c:forEach var="authorityVO" items="${showAuthority}">
				<tr align="center">
					<td>${authorityVO.empid}</td>
					<td>${authorityVO.aid}</td>
					<td><c:forEach var="authority_listVO" items="${authority_listSvc.all}">
						<c:if test="${authorityVO.aid==authority_listVO.aid}">
							${authority_listVO.fname}
						</c:if>
					</c:forEach></td>
					<td>
						<input type="checkbox" name="box" value="${authorityVO.aid}" style="width:20px;height:20px;">
					</td>
				</tr>
			</c:forEach>
		</table>
		<input type="hidden" name="empid" value="${empid}">
		<input type="hidden" name="action" value="deleteAuthority">
	</form>
	</c:if>
</div>
	<div style="z-index:2;position:absolute;margin-left:300px;margin-top:60px">
		<c:if test="${not empty errorMsgs}">
			<font color='red'>
				<c:forEach var="message" items="${errorMsgs}">
					${message}
				</c:forEach>
			</font>
		</c:if>
	</div>
	<c:if test="${not empty empid}">
		<div style="z-index:2;position:absolute;margin-left:400px;"><%@ include file="addAuthority.file"%></div>
	</c:if>

</body>
</html>