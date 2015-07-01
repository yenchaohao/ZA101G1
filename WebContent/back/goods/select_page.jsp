<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.group.model.*"%>
<%@ page import="java.util.*"%>

<%
	List<GroupVO> list = new GroupService().getAll();
	pageContext.setAttribute("list", list);
	String g_name = request.getParameter("g_name");
	String id = request.getParameter("groupid"); 
	pageContext.setAttribute("id", id);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<link
	href="<%=request.getContextPath()%>/javascript_css/front_main_css/bootstrap.css"
	rel="stylesheet" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/javascript_css/front_main_js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/javascript_css/front_main_js/jquery.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商品查詢</title>
</head>
<body>
	<c:if test="${not empty errorMessage}">
		<ul>
			<c:forEach var="message" items="${errorMessage }">
				<li>${message}</li>
			</c:forEach>
		</ul> 
	</c:if>
	<a href="<%= request.getContextPath() %>/back/goods/show_all_goods.jsp">查看所有商品</a> &nbsp;&nbsp;&nbsp;
	<a href="<%= request.getContextPath() %>/back/goods/show_report_goods.jsp">查看被檢舉的商品</a>
	<form
		action="<%=request.getContextPath()%>/front/goods/GoodsServlet.do" method="post">
		<label for="g_name">請輸入商品名稱:</label> 
		<input type="text" name="g_name" value="<%=(g_name == null) ? "" : g_name%>" class="form-control" style="width:200px">
		<input type="submit" value="查詢" id="submit" class="btn btn-success"> 
		<input type="hidden" name="action" value="searchGoodsByName">
		<input type="hidden" name="requestURL" value="<%= request.getServletPath() %>">
	</form>

	<form
		action="<%=request.getContextPath()%>/front/goods/GoodsServlet.do" method="post">
		<label>選擇商品類別:</label> 
		<select name="groupid" class="form-control" style="width:150px">
			<c:forEach var="group" items="${list}">
				<option value="${group.groupid}" ${(id == null)? '' : (group.groupid == id)? 'selected' : '' } >${group.group_name}</option>
			</c:forEach>
		</select> <input type="submit" value="查詢" id="submit" class="btn btn-success"> 
		<input type="hidden" name="action" value="CompositeQuery">
		<input type="hidden" name="requestURL" value="<%= request.getServletPath() %>">
	</form>
	






</body>
</html>