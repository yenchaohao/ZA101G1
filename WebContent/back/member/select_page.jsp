<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="com.member.model.*" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<% List<MemberVO> list = new MemberService().getAllAlive();
   pageContext.setAttribute("list", list);	 	
   String member_id = (String)request.getAttribute("member_id");
%>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<link rel="stylesheet" href="<%= request.getContextPath() %>/javascript_css/member/addmember.css">
<script src="<%= request.getContextPath() %>/javascript_css/member/addmember.js"  ></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>查詢會員</title>
</head>
<body>
	<c:if test="${not empty errorMessage }">
		<ul>
			<c:forEach var="message" items="${errorMessage}">
				<li>${message}</li>
			</c:forEach>
		</ul>
	</c:if>
	<a href="<%= request.getContextPath() %>/back/member/show_all_member.jsp" ><h3>查看所有會員</h3></a>

	
	<form action="<%= request.getContextPath() %>/front/member/MemberServlet.do" method="post">
		<label>輸入會員名稱</label>
		<input type="text" name="mem_name" class="form-control" placeholder="例:黃小華" style="width:500px;height:100px;"
				value="<%= (request.getAttribute("mem_name") == null) ? " " : request.getAttribute("mem_name") %>" >
		<input type="submit" value="查詢" id="submit" class="btn btn-success" style="width:500px;height:100px;" >
		<input type="hidden" name="action" value="searchMemberByname" >
		<input type="hidden" name="requestURL" value="<%= request.getServletPath() %>" >
	</form>
	
	<form action="<%= request.getContextPath() %>/front/member/MemberServlet.do" method="post">
		<label>選擇會員編號:</label>
		<select name="member_id" class="form-control"  style="width:500px;height:100px;">
			<c:forEach var="membervo" items="${list}">
				<option value="${membervo.member_id}" >${membervo.member_id}</option>
			</c:forEach>
		</select>
		<input type="submit" value="查詢" id="submit" class="btn btn-success" style="width:500px;height:100px;">
		<input type="hidden" name="action" value="searchMember" >
		<input type="hidden" name="requestURL" value="<%= request.getServletPath() %>" >
	</form>
	
	
	
	


</body>
</html>