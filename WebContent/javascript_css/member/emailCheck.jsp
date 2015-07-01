<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.member.model.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
</head>
<body>
<%
 	
    String email = request.getParameter("email");
	String result = "";
    MemberDAO memberdao = new MemberDAO();
	MemberVO membervo = memberdao.findByEmail(email);
	if(membervo.getEmail() != null){
		result = "此帳號已有人使用";
	}else{
	result = "帳號可用";
	}
	
	out.println(result);
%>

</body>
</html>