<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="BIG5"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
 <%@ page import="com.message.model.*"%>
  <%@ page import="com.remessage.model.*"%>
  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
</head>
<body>

回覆<c:if test="${not empty errorMsgs}">
	<font color='red'>	
		<c:forEach var="remessage" items="${errorMsgs}">
		${remessage}
		</c:forEach>
	
	</font>
</c:if>

<form action="<%= request.getContextPath() %>/front/remessage/remessage.do" method="post" ">

<!-- <label for="remember_id">remem_id</label> -->
<input type="hidden"  name="remember_id"   value="${member_id}"  size="10" maxlength="10" value="M1005"><br>

<!-- <label for="mid">mid</label> -->
<!-- <input type="text"  name="mid" size="50" maxlength="50" value="1001"><br> -->

<textarea  cols="90" rows="9" name="remessage"  style="margin-top:-15px;"></textarea><br>

<input type="hidden" name="action" value="new_remessage">
<input type="hidden" name="mid" value="<%= request.getParameter("mid")%>">
<input type="hidden" name="whichPage" value="9999">
<input type="submit" value="送出">

</form>
</body>
</html>