<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.member.model.*" %>
<%@ page import="com.group.model.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.cart.controller.*" %>
<%
   MemberVO membervo =(MemberVO) request.getAttribute("MemberVO"); 
   pageContext.setAttribute("membervo", membervo);
   
   MemberVO sesssionMember = (MemberVO)session.getAttribute("member");
   pageContext.setAttribute("sesssionMember", sesssionMember);
   
   List<GroupVO> list = new GroupService().getAll();
   pageContext.setAttribute("list", list);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="<%= request.getContextPath() %>/javascript_css/member/addmember.css">
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

<script src="<%= request.getContextPath() %>/javascript_css/member/addmember.js"  ></script>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改會員資料</title>
</head>
<body>


<div class="header">
		<div class="header-top">
			<div class="container">
				<div class="header-grid">
					<ul>
						<li><a href="<%= request.getContextPath() %>/front/index/index.jsp" class="scroll"><img src="<%= request.getContextPath() %>/images/exlogo.png"></a></li>
						<li><a href="<%= request.getContextPath() %>/front/post/post.jsp" class="scroll">公告</a></li>
						<li><a href="<%=request.getContextPath()%>/front/customerCenter/contact.jsp" class="scroll">客服中心</a></li>	
                        <li><a href="<%= request.getContextPath() %>/front/member/show_one_member.jsp" class="scroll">會員專區 </a></li>	
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
			<c:forEach var="message" items="${errorMessage}">
				<li>${message}</li>
			</c:forEach>
		</ul>
	</c:if>
	
	
	
	<form action="<%= request.getContextPath() %>/front/member/MemberServlet.do" method="post" enctype="multipart/form-data">
		
		<label for="pic" >請上傳個人照片</label>
		<input type="file" name="pic" id="pic" ><font color="blue">(非必填)</font><br>
		<nav id="previewArea">
		<c:if test="${not empty sesssionMember.address }">
		<img src="<%= request.getContextPath() %>/showImage.do?member_id=<%= membervo.getMember_id() %>&action=member"  width="120" height="120" >
		</c:if>
		</nav>
		
		<label for="email" >請輸入電子郵件</label>
		<input type="email" name="email" id="email" class="email" 
			   value="<%= membervo.getEmail() %>"
		><font color="red">(必填)</font>
		
		<nav id="ems"></nav>
	
		
		<!-- fb會員 id 長度會大於5 -->
		<% if(membervo.getMember_id().trim().length() > 5) { %>
			<input type="hidden" name="password" value="facebookuser"><br>
			<input type="hidden" name="rePwd" value="facebookuser"><br>
		<% } %>
		
		<label for="tel" >請輸入電話</label>
		<input type="text" name="tel" id="tel"
			 value="<%= ( membervo.getTel() == null )? "" : membervo.getTel() %>"	
		><font color="red">(必填)</font><br>
		
		<label for="address" >請輸入地址</label>
		<input type="text" name="address" id="address"
			 value="<%= (membervo.getAddress() == null)? "" : membervo.getAddress() %>"	
		><font color="red">(必填)</font><br>
	
		<font color="blue">(非必填)</font><label for="my_wish" >請輸入願望 我想要:</label>
	
		<% 
		String my_wish = null;
		String  id = null;
		if(membervo.getMy_wish() != null && membervo.getMy_wish().length() != 0){	
		   my_wish =  membervo.getMy_wish().substring(1,membervo.getMy_wish().length());
			id = membervo.getMy_wish().substring(0,1);
		   }
		   pageContext.setAttribute("id", id);
		  
		%>
		<input type="text" name="my_wish" id="my_wish"
			 value="<%= (membervo.getMy_wish() == null)? "" : (my_wish == null)? "" : my_wish %>"	
		>  
		
		<label for="groupid" style="width:70px;" >願望類別:</label>
		<select name="groupid">
			<option value="9">請選擇</option>
			<c:forEach var="groupvo" items="${list}">
				<option value="${groupvo.groupid}"  ${(id == null)? '' : (groupvo.groupid == id)? 'selected' : '' }>${groupvo.group_name}</option>
			</c:forEach>
		</select><br>
	
		
		<input type="hidden" value="update_confirm" name="action">
		<input type="hidden" name="requestURL"  value="<%= request.getParameter("requestURL") %>">
		<input type="hidden" name="whichPage" value="<%= request.getParameter("whichPage") %>">
		<input type="hidden" value="<%= membervo.getMember_id() %>" name="member_id">
		<input type="submit" value="確認修改" class="btn btn-info">
	</form>	
	<script>
			$(document).ready(function(){
				$("#btnAuto").click(function(){
					$("#my_wish").val('相機');
					$("#address").val('中壢火車站');
					$("#tel").val('0911722722');
					
				});
			});
		</script>
		<input type="button" id="btnAuto" value="自動完成">
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