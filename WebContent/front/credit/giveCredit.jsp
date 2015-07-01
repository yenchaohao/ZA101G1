<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="BIG5"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
 <%@ page import="com.member.model.*"%>
 <%@ page import="com.tran.model.*"%>
 <%@ page import="com.goods.model.*"%>
 <%@ page import="java.util.*" %>
<%
				// 				評價會員的VO
				String given_member_id=(String)request.getAttribute("given_member_id");
				MemberService memSvc=new MemberService();
				MemberVO memVO=memSvc.getOneMemberByMemberId(given_member_id);
				String given_name=memVO.getMem_name();
				
				TranVO tranVO=(TranVO)request.getAttribute("tranVO");
				GoodsVO goodVO=(GoodsVO)request.getAttribute("goodVO");
				
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="<%= request.getContextPath() %>/javascript_css/credit/jquery-1.11.2.min.js"></script>
<link href="<%= request.getContextPath() %>/javascript_css/front_main_css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="<%= request.getContextPath() %>/javascript_css/front_main_js/bootstrap.min.js"></script>
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
<!-- -----------------starBar -->
	   <link rel="stylesheet" href="<%= request.getContextPath() %>/javascript_css/credit/jquery.raty.css">
		
		<script src="<%= request.getContextPath() %>/javascript_css/credit/jquery.raty.js"></script>
		<script>
			$(function(){
				$('#rating').raty({ number: 10 });
			});
			
		</script>
<!-- -----------------starBar -->

</head>
<body>
<%@ include  file="/front/memberFile.file" %>

<!-- ------------------------------main------------------------------------------------------------ -->

		<form action="<%= request.getContextPath() %>/front/credit/credit.do" method="post">
		<p style="margin-left:400px;font-family:'微軟正黑體';font-size:20px;font-weight:bolder;width:330px;padding:10px;"class="bg-info">評價會員:<%= given_name%>　　　　物品:<%= goodVO.getG_name()%></p>
		
		<br/>
		<div id="rating" ></div>
<!-- 		<input type="number" name="creditValue"> -->
		<input type="hidden" name="given_member_id" value="<%= given_member_id%>">
		<input type="hidden" name="cid" value="<%= request.getParameter("cid")%>">
		<input type="hidden" name="tid" value="<%= request.getParameter("tid")%>">
		<input type="hidden" name="member_id" value="<%= session.getAttribute("member_id") %>">
		<input type="hidden" name="action" value="new_credit">
		<input style="margin-left:530px;margin-top:70px;"type="submit" value="送出"  class="btn btn-success">
		</form>
<!-- 	-----------------------------main end------------------------------------------------------------------- -->
</div>
	</div>
	<!---->
	<div class="footer" style="margin-top:300px;">
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