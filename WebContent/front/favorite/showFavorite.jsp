<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.goods.model.*" %>
<%@ page import="com.member.model.*" %>
<%@ page import="java.util.*" %>
<jsp:useBean id="favSvc" scope="page" class="com.favorite.model.FavoriteService"/>
<jsp:useBean id="groupSvc" scope="page" class="com.group.model.GroupService"/>
<%
	GoodsService goodsSvc = new GoodsService();
	List<GoodsVO> goodsList = goodsSvc.getAll();
	pageContext.setAttribute("goodsList", goodsList);
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
	<div>
		<c:if test="${not empty errorMessage}">
			<ul>
				<c:forEach var="message" items="${errorMessage}">
					<li>${message}</li>
				</c:forEach>
			</ul>
		</c:if>
	</div>
	<c:if test="${favSvc.getAllByMember(sessionScope.member_id).size() > 0}">
	<!-- 審核商品有沒有下架，假如有，我的最愛中刪除該商品 -->
	<c:forEach var="favVO" items="${favSvc.getAllByMember(sessionScope.member_id)}">
		<c:forEach var="goodsVO" items="${goodsList}">
			<c:if test="${goodsVO.goods_status != 1 && goodsVO.gid == favVO.gid}">
				${favSvc.deleteFavorite(favVO.fid)}
			</c:if>
		</c:forEach>
	</c:forEach>
	<!--  -->
	<div><%@ include file="page1.file"%></div>
	<%
		int count = 0;
		pageContext.setAttribute("count", count);
	%>
	<form method="post" action="<%= request.getContextPath() %>/front/favorite/FavoriteServlet.do" name="form">
		<c:forEach var="favVO" items="${favSvc.getAllByMember(sessionScope.member_id)}" begin="<%= pageIndex %>"
							end="<%= pageIndex + rowPerPage -1 %>">
			<c:if test="${count % 2 == 0 }">
					<div class="col-md-4 you-men">
						
						<c:forEach var="goodsVO" items="${goodsList}">
							<c:if test="${goodsVO.gid == favVO.gid}">
								<a href="<%= request.getContextPath() %>/front/goods/goods_detail.jsp?gid=${favVO.gid}">
									<img src="<%= request.getContextPath() %>/showImage.do?gid=${favVO.gid}&action=goods" style="width:150px; height:150px;">
								</a>
										<p>
											<label style=" width: 100px;">商品擁有人:</label>
											<b><font size="5" >${goodsVO.member_id}</font></b>
										</p>
								  		<p>
									  		<label style=" width: 100px;">商品名稱:</label>
											<b><font size="5" color="blue">${goodsVO.g_name}</font></b>
										</p>
										<p>
											<label style=" width: 100px;">商品押金:</label>
											<b><font size="5" color="red">${goodsVO.g_price}</font></b>
										</p>
										<p style="margin-left:60px;"><input type="checkbox" name="box" value="${favVO.fid}" style="width:20px;height:20px;"></p>
										<input type="hidden" name="gid${favVO.fid}" value="${favVO.gid}"> 
							</c:if>
					</c:forEach>
					</div>
			</c:if>
			<c:if test="${count % 2 != 0 }">
					<div class="col-md-4 you-men para-grid">
						<c:forEach var="goodsVO" items="${goodsList}">
							<c:if test="${goodsVO.gid == favVO.gid}">
								<a href="<%= request.getContextPath() %>/front/goods/goods_detail.jsp?gid=${favVO.gid}">
									<img src="<%= request.getContextPath() %>/showImage.do?gid=${favVO.gid}&action=goods" style="width:150px; height:150px;">
								</a>
										<p>
											<label style=" width: 100px;">商品擁有人:</label>
											<b><font size="5" >${goodsVO.member_id}</font></b>
										</p>
								  		<p>
									  		<label style=" width: 100px;">商品名稱:</label>
											<b><font size="5" color="blue">${goodsVO.g_name}</font></b>
										</p>
										<p>
											<label style=" width: 100px;">商品押金:</label>
											<b><font size="5" color="red">${goodsVO.g_price}</font></b>
										</p>
										<p style="margin-left:60px;"><input type="checkbox" name="box" value="${favVO.fid}" style="width:20px;height:20px;"></p>
										<input type="hidden" name="gid${favVO.fid}" value="${favVO.gid}">
							</c:if>
					</c:forEach>
					</div>
			</c:if>
			<%
				count++;
				pageContext.setAttribute("count", count);
			%>
		</c:forEach>
	<input type="hidden" name="action" value="deleteFavorite">
	</form>
	<%@ include file="page2.file"%>
	<div>
		<input type="buttom" value="確認刪除" style="float:right;margin-right:250px;width:100px" class="btn btn-danger" id="buttom">
		<script type="text/javascript">
			$(document).ready(function(){
				$("#buttom").click(function(){
					form.submit();
				});
			});
		</script>
	</div>
	</c:if>
	<c:if test="${favSvc.getAllByMember(sessionScope.member_id).size() == 0}">
		我的最愛中沒有商品
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