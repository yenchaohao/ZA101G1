<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="BIG5"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
 <%@ page import="com.message.model.*"%>
<%
	MessageVO msgVO=(MessageVO)request.getAttribute("messageVO");
	String message=(String)request.getAttribute("message");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
<script
	src="<%=request.getContextPath()%>/javascript_css/message/new.js"></script>
	<link href="<%= request.getContextPath() %>/javascript_css/front_main_css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="<%= request.getContextPath() %>/javascript_css/front_main_js/jquery.min.js"></script>
<script src="<%= request.getContextPath() %>/javascript_css/front_main_js/bootstrap.min.js"></script>
</head>
<body>
<%-- 錯誤表列 --%>
<c:if test="${not empty new_errorMsgs}">
	<font color='red'>
	<ul style="list-style-type: none;">
		<c:forEach var="message" items="${new_errorMsgs}">
			<li>${message}</li>
		</c:forEach>
	</ul>
	</font>
</c:if>
<form action="<%= request.getContextPath() %>/front/message/message.do" method="post">

<!-- <label for="member_id">mem_id</label> -->
<input type="hidden"  name="member_id"  value="${member_id}" size="10" maxlength="10"><br>

<label for="title">標題</label>
<input type="text"  name="title" size="50" maxlength="50" value="<%= (msgVO!=null)?msgVO.getTitle():""%>"><br>

<textarea  cols="90" rows="9" name="message" ><%= (message!=null)?message:"" %></textarea><br>

<input type="hidden" name="action" value="new_message">
<input type="submit" value="送出"  class="btn btn-info">

</form>

</body>
</html>