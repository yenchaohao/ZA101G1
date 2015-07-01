<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.member.model.*" %>    
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
 <% MemberVO memberVO = (MemberVO) request.getAttribute("MemberVO"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<link rel="stylesheet" href="<%= request.getContextPath() %>/javascript_css/member/addmember.css">
<script src="<%= request.getContextPath() %>/javascript_css/member/addmember.js"  ></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>會員註冊</title>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>

<link href="<%= request.getContextPath() %>/javascript_css/front_main_css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->

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
<!--//fonts-->

</head>
<body>
	
	<div class="header">
		<div class="header-top">
			<div class="container">
				<div class="header-grid">
					<ul>
						<li><a href="<%= request.getContextPath() %>/front/index/index.jsp" class="scroll"><img src="<%= request.getContextPath() %>/images/exlogo.png"></a></li>
						
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

	<h1>請輸入你的基本資料</h1>
	
	<nav id="ee" style="background-color:lightBlue">
		<c:if test="${not empty errorMessage}"  >
			<c:forEach var="message" items="${errorMessage}">
				<li>${message}</li>
			</c:forEach>
		</c:if>
	</nav>
	
	
	<form action="<%= request.getContextPath() %>/front/member/MemberServlet.do" method="post" enctype="multipart/form-data" name="registerForm">
		<div>
		<label for="email" >請輸入帳號</label>
		<input type="email" name="email" id="email" class="email" 
			   value="<%= (memberVO==null) ? "" : memberVO.getEmail() %>"
		><font color="red">(必填)</font>
		<nav id="ems"></nav>
		</div> 
		<label for="password" >請輸入密碼</label>
		<input type="password" name="password" id="password"><font color="red">(必填)</font><br>
		<label for="rePwd" >請重新輸入密碼</label>
		<input type="password" name="rePwd" id="rePwd" ><font color="red">(必填)</font><br>
		
		<label for="mem_name" >請輸入姓名</label>
		<input type="text" name="mem_name" id="mem_name" 
			 value="<%= (memberVO==null) ? "" : memberVO.getMem_name() %>"
		><font color="red">(必填)</font><br>
		<label for="id_no" >請輸入身分證字號</label>
		<input type="text" name="id_no" id="id_no"
			 value="<%= (memberVO==null) ? "" : memberVO.getId_no() %>"	
		><font color="red">(必填)</font><br>
		<label for="tel" >請輸入電話</label>
		<input type="text" name="tel" id="tel"
			 value="<%= (memberVO==null) ? "" : memberVO.getTel() %>"	
		><font color="red">(必填)</font><br>
		<label for="address" >請輸入地址</label>
		<input type="text" name="address" id="address"
			 value="<%= (memberVO==null) ? "" : memberVO.getAddress() %>"	
		><font color="red">(必填)</font><br>
		<label for="birthday" >請選擇出生日期</label>
		<input type="text" name="birthday" id="birthday"
			    value="<%= (memberVO==null) ? "" : (memberVO.getBirthday() == null)? "" : memberVO.getBirthday() %>"	
		><font color="red">(必填)</font><br>
		<label for="pic" >請上傳個人照片</label>
		<input type="file" name="pic" id="pic"><font color="red">(必填)</font><br>
		<nav id="previewArea"></nav>
		
		
		<input type="submit" value="完成送出"><input type="button" id="btnAuto" style="margin-left:100px" value="自動完成"><br> 
		<input type="hidden" value="registerForm" id="action" name="action">
		<input type="hidden" value="<%= request.getContextPath() %>" id="url" name="url">
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