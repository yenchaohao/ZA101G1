<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.post.model.*"%>
<%@ page import="com.cart.controller.*" %>
<%@ page import="java.util.*"%>

<% 
if( request.getParameter("whichPage") != null)
	pageContext.setAttribute("whichPage",request.getParameter("whichPage"));

Vector<ReqAndRes> cartList = (Vector<ReqAndRes>) session.getAttribute("cart");
pageContext.setAttribute("cartList", cartList);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link
	href="<%=request.getContextPath()%>/javascript_css/front_main_css/bootstrap.css"
	rel="stylesheet" type="text/css" media="all" />
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script
	src="<%=request.getContextPath()%>/javascript_css/front_main_js/jquery.min.js"></script>
<link
	href="<%=request.getContextPath()%>/javascript_css/front_main_css/style.css"
	rel="stylesheet" type="text/css" media="all" />
<!--//theme-style-->
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="application/x-javascript">
	 addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } 
</script>
<link
	href='http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700,800'
	rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Oswald:400,700,300'
	rel='stylesheet' type='text/css'>
<script src="<%=request.getContextPath()%>/javascript_css/front_main_js/facebookAPI.js"></script>	
</head>
<body>

	<div class="header">
		<div class="header-top">
			<div class="container">
				<div class="header-grid">
					<ul>
						<li><a
							href="<%=request.getContextPath()%>/front/index/index.jsp"
							class="scroll"><img
								src="<%=request.getContextPath()%>/images/exlogo.png"></a></li>
						<li><a
							href="<%=request.getContextPath()%>/front/post/post.jsp"
							class="scroll">公告</a></li>
						<li><a
							href="<%=request.getContextPath()%>/front/customerCenter/contact.jsp"
							class="scroll">客服中心</a></li>
						<li><a
							href="<%=request.getContextPath()%>/front/member/show_one_member.jsp"
							class="scroll">會員專區 </a></li>
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
				 	<script>
				 		$(document).ready(function(){
				 			$("#password").click(function(){
				 				$("#password").attr("type","password");
				 			});	
				 		});
				 	</script>
					<form action="<%= request.getContextPath() %>/front/member/MemberServlet.do" method="post">
						<input type="text" name="email" id="email" class="email" placeholder="帳號" onfocus="this.value='';" onblur="if (this.value == '') {this.value ='';}">
						<input type="text" name="password" id="password"  placeholder="密碼" onfocus="this.value='';" onblur="if (this.value == '') {this.value ='';}">
						
						 
						<input type="hidden" name="action" value="login">
						<input type="hidden" name="url" value="<%= request.getServletPath() %>"> <!--回原頁面用-->
						<input type="hidden" name="gid" value="${gid}"  >  <!--商品詳情用 -->
						<input type="hidden" name="mid" value="${mid}"> <!--討論區用-->
						<input type="hidden" name="pid" value="${pid}"> <!--公告用-->
						<input type="hidden" name="member_id" value="${member_id}"> <!--許願池用-->
						<input type="hidden" name="whichPage" value="${ whichPage }"> <!--上下頁用-->
						<input type="submit" value="Go" >
					
					</form>
					<a href="<%=request.getContextPath()%>/front/member/addMember.jsp"
							class="sign-up">註冊</a>
						
						<fb:login-button scope="public_profile,email" onlogin="checkLoginState();">
						</fb:login-button><div id="status"></div>
						 
						
						<form action="<%= request.getContextPath() %>/front/member/MemberServlet.do" 
							  method="post" name="fbSubmit"> 
						
						<input type="hidden" id="fbId" name="fbId" >
						<input type="hidden" id="fbEmail" name="fbEmail" >
						<input type="hidden" id="fbName" name="fbName" >
					    <input type="hidden" name="action" value="FBlogin">
					    <input type="hidden" name="requestURL" value="<%= request.getServletPath() %>">
						<input type="hidden" name="url" value="<%= request.getServletPath() %>"> <!--回原頁面用-->
						<input type="hidden" name="gid" value="${gid}"  >  <!--商品詳情用 -->
						<input type="hidden" name="mid" value="${mid}"> <!--討論區用-->
						<input type="hidden" name="pid" value="${pid}"> <!--公告用-->
						<input type="hidden" name="member_id" value="${member_id}"> <!--許願池用-->
						<input type="hidden" name="whichPage" value="${ whichPage }"> <!--上下頁用-->
						
						</form> 
					
					</c:if>
					
					<c:if test="${not empty sessionScope.member_id }">
						<a
							href="<%=request.getContextPath()%>/front/member/show_one_member.jsp">
							<img
							src="<%= request.getContextPath() %>/showImage.do?member_id=${sessionScope.member_id}&action=member"
							weight="50" height="50">
						</a>
						<a
							href="<%=request.getContextPath()%>/front/member/MemberServlet.do?requestURL=<%=request.getServletPath()%>&action=logout"
							class="sign-up">登出</a>
					</c:if> 
				</div>
				<div class="clearfix"></div>
			</div>
		</div>
		<div class="container">
			<div class="header-bottom">
				<div class="logo">
					<!--	<a href="index.html"><img src="<%=request.getContextPath()%>/images/exlogo.png" alt=" " ></a>-->
				</div>
				<div class="ad-right">
					<span
  						class="fb-like"
 						data-share="true"
 						data-width="450"
	  					data-show-faces="true">
					</span>
				</div>
				<div class="clearfix"></div>
			</div>
		</div>
	</div>
	<!--content-->
	<div class="content"> 
		<div class="container">
<!-- ------------------------------------------------------------------------------------------------------------ -->

			<div class="privacy-top">
				<h3>Privacy policy for the Exchange Facebook app</h3>
				<h4 style="margin-top:5px;"><font size="5">The general privacy policy published by <font color="blue"><a href="http://52.68.193.16:8081/ZA101G1/front/index/">exChange website</a></font> applies to content provided by and your use of the exChange Facebook application. <br>As a registered Facebook user, you are also subject to Facebook's privacy policy. Please check your Facebook account privacy settings for further information.</font></h4>
					<div class="privacy-bottom">
						<h5><a href="#">Collecting information about you</a></h5>
						<p>We collect different types of information about website users for four main reasons:</p>
						<p>1. To personalise the content of the Application for individual users.</p>
						<p>2. To help us to monitor and improve the Application.</p>
						<p>3. To advertise on the Application.</p>
						<p>4. With user consent, to market services to individual users.</p>
					</div>
					<div class="privacy-bottom">
						<h5><a href="#">Our principles</a></h5>
						<p>1. We do our utmost to protect user privacy through the appropriate use of security technology.</p>
						<p>2. We will respect your privacy. You should receive marketing emails only from <font color="blue"><a href="http://52.68.193.16:8081/ZA101G1/front/index/">exChange website</a></font> and, if you choose, from carefully chosen third parties. It will be made clear to you where you have these choices,We may, however, email you occasionally with information or queries about the App.</p>
					    <p>3. We will collect and use individual user details only where we have legitimate business reasons and are legally entitled to do so.</p>	
					    <p>4. We will be transparent in our dealings with you as to what information about you we will collect and how we will use your information</p>	
					    <p>5. We will use personal data only for the purpose(s) for which they were originally collected and we will ensure personal data are securely disposed of.</p>											
					</div>
					<div class="privacy-bottom">
						<h5><a href="#">Sharing your information with us and others</a></h5>
						<p>When you first access the App, for example login into <font color="blue"><a href="http://52.68.193.16:8081/ZA101G1/front/index/">exChange website</a></font> from Facebook account, you will be presented with a Facebook permissions page, which will advise you about the Facebook information you will be sharing with the App and other Facebook users. You can then decide whether or not to share your Facebook information by using the App. If you decide not to grant permission you will not be able to use the App.
							By granting permission you will be agreeing to share your Facebook user details (including your name, gender, user ID and any other information you choose to share according to your Facebook account settings) as well as the user details of your Facebook friends, and information about your use of the App.</p>
					</div>
					<div class="privacy-bottom">
						<h5><a href="#">Legal information and how to contact us</a></h5>
						<p>address:<b>No.300, Zhongda Rd., Zhongli Dist., Taoyuan City 320, Taiwan (R.O.C.)</b></p>
						<p>If you would like to obtain a copy of the personal data we hold on you, or have any queries regarding <font color="blue"><a href="http://52.68.193.16:8081/ZA101G1/front/index/">exChange website</a></font> use of personal data please contact the data protection manager at the above address, or by email to <b>za101g1@gmail.com</b></p>
					</div>
			</div>

<!-- ------------------------------------------------------------------------------------------------------------ -->		
		
		</div>
	</div>
	<!---->
	<div class="footer">
		<div class="container">
			<div class="footer-class">
				<div class="class-footer">
					<ul>
						<li><a
							href="<%=request.getContextPath()%>/front/index/index.jsp"
							class="scroll">首頁 </a><label>|</label></li>
						<li><a
							href="<%=request.getContextPath()%>/front/goods/show_all_goods.jsp"
							class="scroll">商品專區</a><label>|</label></li>
						<li><a
							href="<%=request.getContextPath()%>/front/member/desired.jsp"
							class="scroll">許願池</a><label>|</label></li>
						<li><a
							href="<%=request.getContextPath()%>/front/message/listAllMessageTitle.jsp"
							class="scroll">討論區</a></li>
					</ul>
					<p class="footer-grid">
						&copy; 2014 Template by <a href="http://w3layouts.com/"
							target="_blank">W3layouts</a>
					</p>
				</div>
				<div class="footer-left">
					<a href="<%=request.getContextPath()%>/front/index/index.jsp"><img
						src="<%=request.getContextPath()%>/images/exlogo.png" alt=" " /></a>
				</div>
				<div class="clearfix"></div>
			</div>
		</div>
	</div>


</body>
</html>