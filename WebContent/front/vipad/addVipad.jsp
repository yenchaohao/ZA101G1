<%@page import="com.goods.model.GoodsService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="BIG5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.goods.model.*" %>
<%@ page import="com.vipad.model.*" %>
<%@ page import="java.util.*" %>
<%
// 	String member_id = (String)session.getAttribute("member_id");
// 	GoodsService goodsSvc = new GoodsService();
// 	List<GoodsVO> list = goodsSvc.findGoodsByMember_idAlive(member_id);
// 	pageContext.setAttribute("list", list);
%>
<jsp:useBean id="goodsSvc" scope="page" class="com.goods.model.GoodsService"/>
<jsp:useBean id="vipadSvc" scope="page" class="com.vipad.model.VipadService" />
<jsp:useBean id="groupSvc" scope="page" class="com.group.model.GroupService"/>
<%
	List<GoodsVO> goodsList = goodsSvc.findByMember_idAllAlive((String)session.getAttribute("member_id"));
	List<VipadVO> vipadList = vipadSvc.getVipadByMember((String)session.getAttribute("member_id"));
	//new一個新的List去接goodsList的GoodsVO物件
	List<GoodsVO> list = new ArrayList<GoodsVO>();
	if(vipadList == null){
		pageContext.setAttribute("goodsList", goodsList);
	} else {
		for(GoodsVO goodsVO : goodsList){
			for(VipadVO vipadVO : vipadList){
				//商品有與VIP廣告商品相同便加入到list裡
				if(goodsVO.getGid().equals(vipadVO.getGid())){
					list.add(goodsVO);
				}
			}
		}
		//假如goodsList有跟list裡面有相同物件，並且做刪除
		goodsList.removeAll(list);
		pageContext.setAttribute("goodsList", goodsList);
	}
%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="<%= request.getContextPath() %>/javascript_css/front_main_css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="<%= request.getContextPath() %>/javascript_css/front_main_js/jquery.min.js"></script>
<!-- Custom Theme files -->
<!--theme-style-->
<link href="<%= request.getContextPath() %>/javascript_css/front_main_css/style.css" rel="stylesheet" type="text/css" media="all" />	
<!--//theme-style-->
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="Fashion Store Responsive web template, Bootstrap Web Templates, Flat Web Templates, Andriod Compatible web template, 
Smartphone Compatible web template, free webdesigns for Nokia, Samsung, LG, SonyErricsson, Motorola web design" />
<script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } </script>
<!--fonts-->
<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700,800' rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Oswald:400,700,300' rel='stylesheet' type='text/css'>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
</head>
<body>
<%@ include  file="/front/memberFile.file" %>
<!------------------------------------------------------------------------------- code -->
	<c:if test="${not empty errorMessage }">
		<ul>
			<c:forEach var="message" items="${errorMessage }">
				<li>${message}</li>
			</c:forEach>
		</ul>
	</c:if>
	<c:if test="${not empty goodsSvc.findGoodsByMember_id(sessionScope.member_id)}">
	<div><%@ include file="page1.file"%></div>
	<div class="sale_grid">
	<form method="post" action="<%=request.getContextPath()%>/front/vipad/vipadServlet.do">
			<c:forEach var="goodsVO" items="${goodsList}" begin="<%= pageIndex %>"
							end="<%= pageIndex + rowPerPage -1 %>">
				<c:if test="${empty vipadSvc.getVipadByMember(sessionScope.member_id)}">
					<%@ include file="addVipad.file"%>
				</c:if>
				<%
					int count = 0; //匹配次數
				%>
				<c:forEach var="vipadVO" items="${vipadSvc.getVipadByMember(sessionScope.member_id)}">
					<c:choose>
						<c:when test="${vipadVO.gid == goodsVO.gid }">
							<%-- 廣告排程中已有此商品 --%>
						</c:when>
						<c:otherwise>
							<% 
								count++;
								pageContext.setAttribute("count", count);
							%>
							<!-- 匹配次數(count)與vip廣告長度符合，此商品尚未加入廣告排程 -->
							<c:if test="${vipadVO.gid != goodsVO.gid && count == vipadSvc.getVipadByMember(sessionScope.member_id).size()}">
								<%@ include file="addVipad.file"%>
							</c:if>
						</c:otherwise>
					</c:choose>
					
				</c:forEach>
			</c:forEach>
		
		<%@ include file="page2.file"%>
		<input type="submit" value="確認新增廣告" style="float:right;margin-right:280px;" class="btn btn-info">
		<input type="hidden" name="action" value="addVipad">
	</form>
	</div>
	</c:if>
	<c:if test="${empty goodsSvc.findGoodsByMember_id(sessionScope.member_id)}">
		查無任何商品，請到商品管理上傳商品。
	</c:if>
	<!-------------------------------------------------------------- end -->	
	</div>
	</div>
	<!---->
	<div class="footer">
		<div class="container">
				<div class="footer-class">
				<div class="class-footer">
					<ul>
						<li ><a href="<%= request.getContextPath() %>/front/index/index.jsp" class="scroll">首頁 </a><label>|</label></li>
						<li><a href="<%= request.getContextPath() %>/front/goods/show_all_goods.jsp" class="scroll">商品專區</a><label>|</label></li>
						<li><a href="<%= request.getContextPath() %>/front/member/desired.jsp" class="scroll">許願池</a><label>|</label></li>
						<li><a href="<%= request.getContextPath() %>/front/message/listAllMessageTitle.jsp" class="scroll">討論區</a></li>
					</ul>
					 <p class="footer-grid">&copy; 2014 Template by <a href="http://w3layouts.com/" target="_blank">W3layouts</a> </p>
				</div>	 
				<div class="footer-left">
					<a href="<%=request.getContextPath()%>/front/index/index.jsp"><img src="<%= request.getContextPath() %>/images/exlogo.png" alt=" " /></a>
				</div> 
				<div class="clearfix"> </div>
			 	</div>
		 </div>
	</div>
</body>
</html>