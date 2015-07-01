<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="BIG5"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
 <%@ page import="java.util.*"%>
 <%@ page import="com.mail.model.*"%>
 <%@ page import="com.member.model.*"%>
 <%
 		MailVO mailVO=(MailVO)request.getAttribute("mailVO");
		 String member_id=mailVO.getMember_id();
		MemberService memSvc=new MemberService();
		MemberVO memVO=memSvc.getOneMemberByMemberId(member_id);
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
</head>
<body>
會員: <%= memVO.getMem_name() %><br>
問題類型:<%= mailVO.getTitle() %><br>
問題內容:<%= mailVO.getQuestion() %><p>
回覆內容:<br>
<%-- 錯誤表列 --%>
<c:if test="${not empty errorMsgs}">
	<font color='red'>	
		<c:forEach var="message" items="${errorMsgs}">
		${message}
		</c:forEach>
	</font>
</c:if>
<form action="<%= request.getContextPath()%>/back/mail/mail.do" method="post">
<textarea  cols="90" rows="9" name="answer" class="form-control"></textarea><br>
<input type="submit" value="確定回覆" class="btn btn-success">
<input type="hidden"  name="action" value="answered">
<input type="hidden"  name="cmid" value="<%= mailVO.getCmid()%>">
<input type="hidden"  name="empid" value="<%= session.getAttribute("empid")%>">
</form>

</body>
</html>