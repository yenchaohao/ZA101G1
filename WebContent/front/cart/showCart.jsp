<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="BIG5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.goods.model.*" %>
<%@ page import="com.cart.controller.*" %>
<%@ page import="com.tran.model.*" %>
<%
	Vector<ReqAndRes> cartList = (Vector<ReqAndRes>) session.getAttribute("cart");
	pageContext.setAttribute("cartList", cartList);
	
	List<TranVO> tranList=null;
	if(session.getAttribute("member_id")!=null){
		TranService tranSvc=new TranService();
		tranList=tranSvc.getOneByResMember_idUn((String)session.getAttribute("member_id"));
		pageContext.setAttribute("tranList", tranList);
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
<style>td {text-align: center;}</style>
<title>Insert title here</title>
</head>
<body>
<div class="header">
		<div class="header-top">
			<div class="container">
				<div class="header-grid">
					<ul>
						<li><a href="<%= request.getContextPath() %>/front/index/index.jsp" class="scroll"><img src="<%= request.getContextPath() %>/images/exlogo.png"></a></li>
						<li><a
							href="<%=request.getContextPath()%>/front/post/post.jsp"
							class="scroll">公告</a></li>
						<li><a
							href="<%=request.getContextPath()%>/front/customerCenter/contact.jsp"
							class="scroll">客服中心</a></li>
                        <li><a href="<%= request.getContextPath() %>/front/member/show_one_member.jsp" class="scroll">會員專區 </a></li>
                        <li><a
							href="<%=request.getContextPath()%>/front/cart/cartServlet.do?action=showCart"
							class="scroll"> 換物車<img src="<%=request.getContextPath()%>/images/car.png">
							<c:if test="${cartList != null}">
								<font color="red"><%=cartList.size()%></font>
							</c:if>
						</a></li>
					</ul>
				</div>
				<div class="header-grid-right">
					<c:if test="${ empty sessionScope.member_id }">
					<a href="<%= request.getContextPath() %>/front/member/member_login.jsp?url=<%= request.getServletPath() %>&whichPage=<%= request.getParameter("whichPage") %>" class="sign-in">登入</a>
					<a href="<%= request.getContextPath() %>/front/member/addMember.jsp" class="sign-up">註冊</a>
					</c:if>
					<c:if test="${not empty sessionScope.member_id }">
						<a href="<%= request.getContextPath() %>/front/member/show_one_member.jsp" >
						<img src="<%= request.getContextPath() %>/showImage.do?member_id=${sessionScope.member_id}&action=member"
							weight="50" height="50"></a>
						<font color="blue" size="4"><b>你好,${sessionScope.member.mem_name}</b></font></a>
						<a href="<%= request.getContextPath() %>/front/member/MemberServlet.do?requestURL=<%= request.getServletPath() %>&action=logout&whichPage=<%= request.getParameter("whichPage") %>" class="sign-up">登出</a>
					</c:if>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<c:if test="${not empty tranList}">
					<a href="<%=request.getContextPath()%>/front/transaction/receive.jsp"><font color="red"><b>您目前有<%= tranList.size() %>筆交易待確認</b></font></a>
					</c:if>
					
				</div>
				<div class="clearfix"> </div>
			</div>
		</div>
		<div class="container">
		<div class="header-bottom">			
				<div class="logo">
				<!--	<a href="index.html"><img src="<%= request.getContextPath() %>/images/exlogo.png" alt=" " ></a>-->
				</div>
					<div class="ad-right">
					<!--<img class="img-responsive" src="<%= request.getContextPath() %>/images/ad.png" alt=" " >-->
				</div>
				<div class="clearfix"> </div>
			</div>	
		</div>
	</div>
	<!--content-->
	<div class="content">
		<div class="container">
<!-------------------------------------------------------程式碼如下--------------------------------------------------- -->
	<c:if test="${not empty errorMessage}">
		<ul>
			<c:forEach var="message" items="${errorMessage}">
				<li>${message}</li>
			</c:forEach>
		</ul>
	</c:if>
	<%
		if(cartList != null && (cartList.size() > 0)){ //判斷假如換物車有無商品
			
		GoodsService goodsSvc = new GoodsService();
		List<GoodsVO> res_goodsVO = new ArrayList<GoodsVO>();
		List<GoodsVO> req_goodsVO = new ArrayList<GoodsVO>();
	%>
	
	
	<div class="sale_grid">
		<form method="post" action="<%=request.getContextPath()%>/front/cart/cartServlet.do">
			<h3 style="float:right;margin-right:60px;color:red;">刪除選項</h3>
				<% 
					for(int i=0; i<cartList.size(); i++){
					res_goodsVO.add(goodsSvc.findGoodsByGid(cartList.get(i).getRes_gid()));
					req_goodsVO.add(goodsSvc.findGoodsByGid(cartList.get(i).getReq_gid()));
				%>
					<ul class="grid_1">
					<li class="grid_1-img" style="width:150px;">
						<img src="<%= request.getContextPath() %>/showImage.do?gid=<%= req_goodsVO.get(i).getGid() %>&action=goods" style="width:150px; height:150px;">
					</li>
					<li class="grid_1-img" style="width:150px;">
						<p><label style=" width: 100px;">我的商品名稱:</label></p>
						<p><b><font size="5" color="blue"><%=req_goodsVO.get(i).getG_name() %></font></b></p>
						<p><label style=" width: 100px;">我的商品押金:</label></p>
						<p><b><font size="5" color="red"><%=req_goodsVO.get(i).getG_price() %></font></b></p>
					</li>
					<li class="grid_1-img" style="width:50px;">
						<img src="<%= request.getContextPath() %>/images/change.jpg" style="width:50px;height:50px;margin-top:60px">
					</li>
					<li class="grid_1-img" style="width:150px;">
						<img src="<%= request.getContextPath() %>/showImage.do?gid=<%= res_goodsVO.get(i).getGid() %>&action=goods" style="width:150px; height:150px;">
					</li>
					<li class="grid_1-img" style="width:150px;">
						<p><label style=" width: 100px;">對方會員:</label></p>
						<p><b><font size="5" color="blue"><%=res_goodsVO.get(i).getMember_id() %></font></b></p>
						<p><label style=" width: 100px;">對方商品名稱:</label></p>
						<p><b><font size="5" color="blue"><%=res_goodsVO.get(i).getG_name() %></font></b></p>
						<p><label style=" width: 100px;">對方商品押金:</label></p>
						<p><b><font size="5" color="red"><%=res_goodsVO.get(i).getG_price() %></font></b></p>
					</li>
					<li class="grid_1-img" style="width:100px;">
						<p style=" margin-left:150px;"><input type="checkbox" name="box" value="<%= i %>" style="width:20px;height:20px;margin-top:80px"></p>
					</li>
					<div class="clearfix"> </div>
					</ul>
				<%}%>
			<input type="submit" value="刪除" style="float:right;margin-right:80px" class="btn btn-danger">
			<input type="hidden" name="action" value="deleteCart">
		</form>
	</div>
	<form method="post" action="<%=request.getContextPath()%>/front/cart/cartServlet.do">
		<input type="hidden" name="action" value="sendTran">
		<input type="submit" value="送出全部交換請求" class="btn btn-success">
	</form>
	<%}%>
	<c:if test="${empty cartList && empty errorMessage}">
		<h3 style=color:red>換物車內沒有商品</h3>
	</c:if>
<!-- ---------------------------------------------------程式碼結束----------------------------------------- -->
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