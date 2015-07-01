<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.member.model.*" %>   
<%@ page import="com.cart.controller.*" %>
<%@ page import="java.util.*"%> 
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
 <%
 	Vector<ReqAndRes> cartList = (Vector<ReqAndRes>) session.getAttribute("cart");
	pageContext.setAttribute("cartList", cartList);
 %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="<%= request.getContextPath() %>/javascript_css/member/addmember.css">
<script src="<%= request.getContextPath() %>/javascript_css/member/addmember.js"  ></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>會員登入</title>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<link rel="stylesheet" href="/resources/demos/style.css" />

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
<script src="<%=request.getContextPath()%>/javascript_css/front_main_js/facebookAPI.js"></script>
</head>
<body>

	<div class="header">
		<div class="header-top">
			<div class="container">
				<div class="header-grid">
					<ul>
						<li><a href="<%= request.getContextPath() %>/front/index/index.jsp" class="scroll"><img src="<%= request.getContextPath() %>/images/exlogo.png"></a></li>
						<li><a href="<%= request.getContextPath() %>/front/post/post.jsp" class="scroll">公告</a></li>
						<li><a href="../customerCenter/contact.html" class="scroll">客服中心</a></li>	
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
					<a href="<%= request.getContextPath() %>/front/member/member_login.jsp?url=<%= request.getParameter("url") %>&whichPage=<%= request.getParameter("whichPage") %>&gid=<%= request.getParameter("gid") %>" class="sign-in">登入</a>
					<a href="<%= request.getContextPath() %>/front/member/addMember.jsp" class="sign-up">註冊</a>
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

	<c:if test="${not empty errorMessage}" >
		<ul>
			<c:forEach  var="message" items="${errorMessage }">
				<li>${message}</li>
			</c:forEach>
		</ul>
	</c:if>
	<h1>請輸入帳號密碼</h1>

	<form action="<%= request.getContextPath() %>/front/member/MemberServlet.do" method="post">
		<label for="email" >請輸入帳號</label>
		<input type="email" name="email" id="email" class="email" ><br>
		<label for="password" >請輸入密碼</label>
		<input type="password" name="password" id="password"><br>
		<input type="hidden" name="action" value="login">
		<input type="hidden" name="url" value="<%= request.getParameter("url") %>"> <!--回原頁面用-->
		<input type="hidden" name="gid" value="<%= request.getParameter("gid") %>"> <!--商品詳情用 -->
		<input type="hidden" name="mid" value="<%= request.getParameter("mid") %>"> <!--討論區用-->
		<input type="hidden" name="pid" value="<%= request.getParameter("pid") %>"> <!--公告用-->
		<input type="hidden" name="member_id" value="<%= request.getParameter("member_id") %>"> <!--許願池用-->
		<input type="hidden" name="whichPage" value="<%= request.getParameter("whichPage") %>"> <!--上下頁用-->
		<input type="submit" value="送出">
	</form>

	<div>
	<label>Facebook登入</label>
	<fb:login-button scope="public_profile,email" onlogin="checkLoginState();">
	</fb:login-button><div id="status"></div>
	</div>					
	<form action="<%= request.getContextPath() %>/front/member/MemberServlet.do" 
							  method="post" name="fbSubmit"> 
						
	<input type="hidden" id="fbId" name="fbId" >
	<input type="hidden" id="fbEmail" name="fbEmail" >
	<input type="hidden" id="fbName" name="fbName" >
	<input type="hidden" name="action" value="FBlogin">
	<input type="hidden" name="requestURL" value="<%= request.getParameter("url") %>">
	<input type="hidden" name="url" value="<%= request.getParameter("url") %>"> <!--回原頁面用-->
	<input type="hidden" name="gid" value="<%= request.getParameter("gid") %>"> <!--商品詳情用 -->
	<input type="hidden" name="mid" value="<%= request.getParameter("mid") %>"> <!--討論區用-->
	<input type="hidden" name="pid" value="<%= request.getParameter("pid") %>"> <!--公告用-->
	<input type="hidden" name="member_id" value="<%= request.getParameter("member_id") %>"> <!--許願池用-->
	<input type="hidden" name="whichPage" value="<%= request.getParameter("whichPage") %>"> <!--上下頁用-->
						
	</form> 
	
	
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