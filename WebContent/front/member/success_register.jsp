<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.member.model.*" %>
<% 
	
	MemberVO membervo = new MemberService().getOneMemberByMemberId((String)request.getAttribute("member_id"));
	pageContext.setAttribute("MemberVO", membervo);
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

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/javascript_css/member/addmember.css">
<script
	src="<%=request.getContextPath()%>/javascript_css/member/addmember.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
td {
	text-align: center;
}
</style>
<title>成功送出註冊申請</title>
</head>
<body>

<div class="header">
		<div class="header-top">
			<div class="container">
				<div class="header-grid">
					<ul>
						<li><a href="<%= request.getContextPath() %>/front/index/index.jsp" class="scroll"><img src="<%= request.getContextPath() %>/images/exlogo.png"></a></li>
						<li><a href="<%= request.getContextPath() %>/front/post/post.jsp" class="scroll">公告</a></li>
						<li><a
							href="<%=request.getContextPath()%>/front/customerCenter/contact.jsp"
							class="scroll">客服中心</a></li>
                        <li><a href="<%= request.getContextPath() %>/front/member/show_one_member.jsp" class="scroll">會員專區 </a></li>
                        <li><a href="<%=request.getContextPath()%>/front/cart/cartServlet.do?action=showCart" class="scroll"><!--<img src="<%= request.getContextPath() %>/images/car.png">-->換物車 </a></li>	
					</ul>
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


<c:if test="${not empty MemberVO }">
	<h1>你的註冊申請已經送出,請等待審核.以下是你的資料:</h1>
	<table border="1">
		<tr>
			<th>會員照片</th>
			<th>電子郵件</th>
			<th>會員姓名</th>
			<th>電話</th>
			<th>地址</th>
			<th>生日</th>
		</tr>
		<tr>
			<td><img src="<%= request.getContextPath() %>/showImage.do?member_id=${MemberVO.member_id}&action=member"
							weight="120" height="120"></td>
			<td width="18%">${MemberVO.email}</td>
					<td>${MemberVO.mem_name}</td>
					<td>${MemberVO.tel}</td>
					<td>${MemberVO.address}</td>
					<td>${MemberVO.birthday}</td>				
		</tr>
	</table>
</c:if>
<c:if test="${empty  MemberVO }">
	<h1>申請失敗</h1>
</c:if>


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