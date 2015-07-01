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
	String empid = request.getParameter("empid"); //���oauthorityServlet��empid
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
	<h1><%= empid %>���u�v���s�W</h1>
	<!--�Ӧ�authorityServlet��session-->
	<% if(emp.size() < all.size()){%>
	<form method="post"
		action="<%=request.getContextPath()%>/back/authority/authorityServlet.do">
		<table border="1" bordercolor="blue">
			<tr>
				<th>�v���s��</th>
				<th>�v���\��</th>
				<th><input type="submit" value="�s�W"></th>
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
						int count = 0;	//�ǰt����
								for (int y = 0; y < emp.size(); y++) {
									if(all.get(i).getAid().equals(emp.get(y).getAid())) //���p�{�����u�v�����@���P�Ҧ��v���ۦP�A�ð���j��A�᭱���A�~��ǰt
										break;
									else{
										count++;
										if(count == emp.size() && all.get(i).getAid() != emp.get(y).getAid()){ //���p�ǰt����(count)�P�{�����u�v�����ײŦX�A���u�èS�����v��
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
			out.println("�Ҧ��v���w�֦�");
		}
	%>
	<div>
		<c:if test="${not empty errorMsgs}">
			<font color='red'>�Эץ��H�U���~:
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