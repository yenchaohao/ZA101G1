<%@page import="com.authority_list.model.*"%>
<%@page import="java.util.*"%>
<%@page import="com.sun.org.apache.bcel.internal.generic.RETURN"%>
<%@ page import="com.authority.model.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="BIG5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:useBean id="authority_listSvc" scope="page"
	class="com.authority_list.model.Authority_listService" />
<jsp:useBean id="authoritySvc" scope="page"
	class="com.authority.model.AuthorityService" />

<%
	String empid = request.getParameter("empid"); //取得authorityServlet的empid
	List<AuthorityVO> emp = authoritySvc.getOneAid(empid);
	pageContext.setAttribute("list", emp);
	List<Authority_listVO> all = authority_listSvc.getAll();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
</head>
<body>
	<h1><%= empid %>員工權限新增</h1>
	<!--來自authorityServlet的session-->
	<% if(emp.size() < all.size()){%>
	<form method="post"
		action="<%=request.getContextPath()%>/back/authority/authorityServlet.do">
		<table border="1" bordercolor="blue">
			<tr>
				<th>權限編號</th>
				<th>權限功能</th>
				<th><input type="submit" value="新增"></th>
			</tr>


			<c:if test="${empty list}">
				<c:forEach var="authority_listVO" items="${authority_listSvc.all}">
					<tr>
						<td>${authority_listVO.aid}</td>
						<td>${authority_listVO.fname}</td>
						<td><input type="checkbox" name="box"
							value="${authority_listVO.aid}"></td>
					</tr>
				</c:forEach>
			</c:if>
			<c:if test="${not empty list}">
				<%
					for (int i = 0; i < all.size(); i++) {
						int count = 0;	//匹配次數
								for (int y = 0; y < emp.size(); y++) {
									if(all.get(i).getAid().equals(emp.get(y).getAid())) //假如現有員工權限有一項與所有權限相同，並停止迴圈，後面不再繼續匹配
										break;
									else{
										count++;
										if(count == emp.size() && all.get(i).getAid() != emp.get(y).getAid()){ //假如匹配次數(count)與現有員工權限長度符合，員工並沒有此權限
											out.println("<tr>");
											out.println("<td>"+all.get(i).getAid()+"</td>");
											out.println("<td>"+all.get(i).getFname()+"</td>");
											out.println("<td><input type=\"checkbox\" name=\"box\" value=\""+all.get(i).getAid()+"\"></td>");
											out.println("</tr>");
										}
									}
								}
					}
				%>
			</c:if>
		</table>
		<input type="hidden" name="empid" value="<%=empid%>">
		<input type="hidden" name="action" value="insertAuthority">
	</form>
	<% 
		} else {
			out.println("所有權限已擁有");
		}
	%>
	<div>
		<c:if test="${not empty errorMsgs}">
			<font color='red'>請修正以下錯誤:
				<ul>
					<c:forEach var="message" items="${errorMsgs}">
						<li>${message}</li>
					</c:forEach>
				</ul>
			</font>
		</c:if>
	</div>
</body>
</html>