<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="BIG5"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
 <%@ page import="java.util.*"%>
 <%@ page import="com.mail.model.*"%>
 <%@ page import="com.member.model.*"%>
 <%
 		MailService mailSvc=new MailService();
 		List<MailVO> list=mailSvc.getAll();
 		String empid=(String)session.getAttribute("empid");
 		
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
<table border="1" class="table table-hover">
<tr class="warning"><td>會員</td><td>問題類型</td><td>問題內容</td><td>發問時間</td><td>狀態</td></tr>
<% for(MailVO mailVO:list){ 
		String member_id=mailVO.getMember_id();
		MemberService memSvc=new MemberService();
		MemberVO memVO=memSvc.getOneMemberByMemberId(member_id);
%>
<tr>
<td><%= memVO.getMem_name() %></td>
<td><%= mailVO.getTitle() %></td>
<td><%= mailVO.getQuestion() %></td>
<td><%= mailVO.getQ_date() %></td>
<% if(mailVO.getStatus().equals(0)){ %>
<td>未回覆</td>
<td>
<form action="<%=request.getContextPath()%>/back/mail/mail.do" method="post">
<input type="submit" value="回覆" class="btn btn-success">
<input type="hidden" name="action" value="answerMail">
<input type="hidden" name="cmid" value="<%= mailVO.getCmid()%>">
</form>
</td>
<% } else{%>
<td>已回覆</td>
<% } %>
</tr>
<% } %>
</table>
</body>
</html>