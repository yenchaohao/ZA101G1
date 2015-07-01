<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="BIG5"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
 <%@ page import="java.util.*"%>
  <%@ page import="java.text.*"%>
 <%@ page import="com.remessage.model.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
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

</head>
<body>

 <div class="header">
		<div class="header-top">
			<div class="container">
				<div class="header-grid">
					<ul>
						<li><a href="<%= request.getContextPath() %>/front/index/index.jsp" class="scroll"><img src="<%= request.getContextPath() %>/images/exlogo.png"></a></li>
						<li><a href="../articles/articles.html" class="scroll">公告</a></li>
						<li><a href="../customerCenter/contact.html" class="scroll">客服中心</a></li>	
                        <li><a href="<%= request.getContextPath() %>/front/member/memberCenter.jsp" class="scroll">會員專區 </a></li>
                        <li><a href="../car/car.html" class="scroll"><!--<img src="<%= request.getContextPath() %>/images/car.png">-->換物車 </a></li>	
					</ul>
				</div> 
				<div class="header-grid-right">
					
					<c:if test="${not empty sessionScope.member_id }">
						<a href="<%= request.getContextPath() %>/front/member/show_one_member.jsp" >
						<img src="<%= request.getContextPath() %>/showImage.do?member_id=${sessionScope.member_id}&action=member"
							weight="50" height="50"></a>
						<a href="<%= request.getContextPath() %>/front/member/MemberServlet.do?requestURL=<%= request.getServletPath() %>&action=logout&whichPage=<%= request.getParameter("whichPage") %>" class="sign-up">登出</a>
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
		<div class="header-bottom-bottom">
					<div class="top-nav">
					<span class="menu"> </span>
					<ul >
						<li id="mainMenu" ><a href="<%= request.getContextPath() %>/front/member/show_one_member.jsp">我的帳戶 </a></li>
						<li id="mainMenu" ><a href="<%= request.getContextPath() %>/front/goods/show_goods_by_member.jsp" >商品管理</a></li>
						<li class="active" id="mainMenu" ><a href="credit.html" >信用評價</a></li>
						<li id="mainMenu" ><a href="VIP.html" >VIP專區</a></li>
						<li id="mainMenu" ><a href="myPoint.html" >我的點數</a></li>
						<li id="mainMenu" ><a href="transaction.html" >交易管理</a></li>
						<li id="mainMenu" ><a href="favorite.html" >我的最愛</a></li>
						<li id="mainMenu" ><a href="desire.html" >我的願望</a></li>
					</ul>	
				<script>
					$("span.menu").click(function(){
						$(".top-nav ul").slideToggle(500, function(){
						});
					});
				</script>
					    
					<div class="clearfix"> </div>			
				</div>
				
				<div class="clearfix"> </div>
				</div>
		</div>
	</div>
	<!--content-->
	<div class="content">
		<div class="container">

<!-- ------------------------------------------------------------------------------------------------- -->
<%-- 錯誤表列 --%>
<c:if test="${not empty errorMsgs}">
	<font color='red'>
	<ul>
		<c:forEach var="remessage" items="${errorMsgs}">
			<li>${remessage}</li>
		</c:forEach>
	</ul>
	</font>
</c:if>
<form action="<%= request.getContextPath() %>/front/remessage/remessage.do" method="post">

<textarea  cols="90" rows="9" name="message" >${remsg.message}</textarea><br>

<input type="hidden" name="action" value="finishAlter">
<input type="hidden" name="mid" value="<%= request.getParameter("mid")%>">
<input type="hidden" name="rid" value="${remsg.rid}">
<input type="hidden" name="whichPage" value="<%= request.getParameter("whichPage")%>">
<input type="submit" value="完成修改">

</form>
<!-- ---------------------------------------------------------------------------------------------- -->
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
					<a href="../index.html"><img src="<%= request.getContextPath() %>/images/exlogo.png" alt=" " /></a>
				</div> 
				<div class="clearfix"> </div>
			 	</div>
		 </div>
	</div>

</body>
</html>