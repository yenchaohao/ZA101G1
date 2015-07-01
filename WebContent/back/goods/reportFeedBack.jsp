<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.mail.model.*" %>  
<%@ page import="java.util.*" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">檢舉詳情:</h4>
      </div>
	<% 
		String gid  = request.getParameter("gid");
		List<MailVO> list = new MailService().findByAnswerForReport(gid);
		pageContext.setAttribute("list", list);
	%>
	<c:if test="${not empty list }">
		<table> 
		<c:forEach var="mailvo" items="${list }">
			<tr>
				<td>原因:</td>
				<td>${mailvo.question}</td>
				<td>&nbsp;&nbsp;</td>
				<td>檢舉時間:</td>
				<td>${mailvo.getQ_date_str()}</td>
			</tr>
		</c:forEach>
		</table>
	</c:if>
		<div class="modal-footer"  style="background-color:white;">
			<button type="button" class="btn btn-default" data-dismiss="modal">關閉</button>
		</div>
	
</body>
</html>