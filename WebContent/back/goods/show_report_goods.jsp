<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.goods.model.*"%>
<%@ page import="com.group.model.*" %>
<%@ page import="com.report.model.*" %>
<%@ page import="java.util.*"%>
<%
	List<GoodsVOHibernate> goodslist = new GoodsService().getAllAssociations();
	List<GoodsVOHibernate> list = new ArrayList<GoodsVOHibernate>();
	for (GoodsVOHibernate goodsvo : goodslist) {
			if (!goodsvo.getReports().isEmpty() && goodsvo.getGoods_status() == 1) {
			list.add(goodsvo);
		}
	}
	pageContext.setAttribute("list", list);
%>    
<jsp:useBean id="groupService" scope="page" class="com.group.model.GroupService" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<link
	href="<%=request.getContextPath()%>/javascript_css/front_main_css/bootstrap.css"
	rel="stylesheet" type="text/css" media="all" />
	<!--  jquey 在上面 bootstrap 在下面 -->
<script src="<%=request.getContextPath()%>/javascript_css/front_main_js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/javascript_css/front_main_js/bootstrap.min.js"></script>
 

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>  
</head>
<script>
$(function(){

	$("#report").on("hidden.bs.modal", function() {
	    $(this).removeData("bs.modal");
	});
	
});
</script>
<body>
	<%@ include file="/back/goods/report.file" %>
	
	 <input type="hidden" id="url" value="<%=request.getContextPath()%>">
		<table border="1" class="table table-hover">

		<c:if test="${not empty list }">
			<tr class="warning">
				<th>商品圖示</th>
				<th>商品類別</th>
				<th>商品名稱</th>
				<th>商品描述</th>
				<th>上架日期</th>
				<th>商品狀態</th>
				<th>檢舉狀態</th>
				<th>刪除</th>
				
			</tr>

			<%@ include file="page1.file"%>
			<form action="<%= request.getContextPath() %>/front/goods/GoodsServlet.do" method="post">
			<% int  count = pageIndex; pageContext.setAttribute("count", count); %>
			
			<c:forEach var="goodsVO" items="${list}" begin="<%= pageIndex %>"
				end="<%= pageIndex + rowPerPage -1 %>">
				
				<tr align='center' valign='middle' ${(goodsVO.gid == param.gid)? 'bgcolor=#CCCCFF' : '' }>
					<td>
					<img src="<%= request.getContextPath() %>/showImage.do?gid=${goodsVO.gid}&action=goods"
						weight="120" height="120"></td>
				<c:forEach var="groupvo" items="${groupService.all }">	
					<c:if test="${goodsVO.groupid == groupvo.groupid}">	
						<td>${groupvo.group_name}</td>
					</c:if>
				</c:forEach>	
					<td>${goodsVO.g_name}</td>
					<td>${goodsVO.getG_describe()}</td>
					
					<td>${goodsVO.getJoindateFormat()}</td>
					<td>${goods_status[goodsVO.goods_status]}</td>
					<% 
					   List<ReportVO> reportlist = new ReportService().findReportByGid(list.get(count).getGid());
					   pageContext.setAttribute("reportlist", reportlist);
					%>
					<c:if test="${not empty reportlist }">
					<td><input type="button" value="此商品有${reportlist.size()}筆檢舉"
					 class="btn btn-warning" id="${goodsVO.gid }" data-toggle="modal" data-target="#report" 
					 href="<%=request.getContextPath() %>/back/goods/reportFeedBack.jsp?gid=${goodsVO.gid}"
					></td> 
					</c:if>
					<c:if test="${empty reportlist }">
					<td>無檢舉</td>
					</c:if>
					
					<c:if test="${goodsVO.goods_status != 0 }">
					<td><input type="checkbox" name="deleteItem" value="${goodsVO.gid }" style="width:20px;height:20px;"></td>
					</c:if>
				</tr>
				<% count++; pageContext.setAttribute("count", count); %>
				
			</c:forEach>
	</table>
	<%@ include file="page2.file"%>
	
	
	
	</c:if>
	<c:if test="${empty list }">
		查無任何商品
	</c:if>
		
		
</body>
</html>