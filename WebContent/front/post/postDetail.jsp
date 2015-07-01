<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.post.model.*"%>
<%@ page import="com.cart.controller.*" %>
<%@ page import="java.util.*"%>
<%@ page import="com.tran.model.*"%>
<%
	int pid = 0 ;
	if (request.getParameter("pid") != null) {
		pid = Integer.parseInt(request.getParameter("pid"));
		PostVO postvo = new PostService().getOnePost(pid);
		pageContext.setAttribute("postvo", postvo);
	}
	pageContext.setAttribute("pid", pid);
	
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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link
	href="<%=request.getContextPath()%>/javascript_css/front_main_css/bootstrap.css"
	rel="stylesheet" type="text/css" media="all" />
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script
	src="<%=request.getContextPath()%>/javascript_css/front_main_js/jquery.min.js"></script>
<!-- Custom Theme files -->
<!--theme-style-->
<link
	href="<%=request.getContextPath()%>/javascript_css/front_main_css/style.css"
	rel="stylesheet" type="text/css" media="all" />
<!--//theme-style-->
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords"
	content="Fashion Store Responsive web template, Bootstrap Web Templates, Flat Web Templates, Andriod Compatible web template, 
Smartphone Compatible web template, free webdesigns for Nokia, Samsung, LG, SonyErricsson, Motorola web design" />
<script type="application/x-javascript">
	 addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } 
</script>
<!--fonts-->
<link
	href='http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700,800'
	rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Oswald:400,700,300'
	rel='stylesheet' type='text/css'>
<c:if test="${not empty postvo }">
	<title>${postvo.title}</title>
</c:if>
<c:if test="${empty postvo }">
	<title>此公告已經不存在</title>
</c:if>

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
						<input type="text" name="email" id="email" class="email" value="帳號" onfocus="this.value='';" onblur="if (this.value == '') {this.value ='';}">
						<input type="text" name="password" id="password"  value="密碼" onfocus="this.value='';" onblur="if (this.value == '') {this.value ='';}">
						
						 
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
					</c:if>
					
					<c:if test="${not empty sessionScope.member_id }">
						<a
							href="<%=request.getContextPath()%>/front/member/show_one_member.jsp">
							<img
							src="<%= request.getContextPath() %>/showImage.do?member_id=${sessionScope.member_id}&action=member"
							weight="50" height="50">
						</a>
						<font color="blue" size="4"><b>你好,${sessionScope.member.mem_name}</b></font></a>
						<a
							href="<%=request.getContextPath()%>/front/member/MemberServlet.do?requestURL=<%=request.getServletPath()%>&action=logout&pid=<%= pid %>"
							class="sign-up">登出</a>
					</c:if>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<c:if test="${not empty tranList}">
					<a href="<%=request.getContextPath()%>/front/transaction/receive.jsp"><font color="red"><b>您目前有<%= tranList.size() %>筆交易待確認</b></font></a>
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
					<!--<img class="img-responsive" src="<%=request.getContextPath()%>/images/ad.png" alt=" " >-->
				</div>
				<div class="clearfix"></div>
			</div>
		</div>
	</div>
	<!--content-->
	<div class="content">
		<div class="container">


			<c:if test="${not empty postvo }">
				<div class="article-top">
					<h3>${postvo.title}</h3>
					<div class="article-bottom">
						<c:if test="${not empty postvo.pic }">
							<div class="col-md-4 article-para">

								<img
									src="<%= request.getContextPath() %>/showImage.do?pid=${postvo.pid}&action=post" style="margin-bottom:20px;">
							</div>
						</c:if>
						<div class="col-md-8 article-head">

							<h6 style=" font-size: 150%;">${postvo.post}</h6>

						</div>
					</div>
				</div>
				
			</c:if>
			<c:if test="${empty postvo }">
				<h1>此公告已經不存在</h1>
			</c:if>

		
		
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