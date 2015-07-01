<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.goods.model.*"%>
<%@ page import="com.group.model.*"%>
<%@ page import="com.member.model.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.cart.controller.*" %>
<%@ page import="com.tran.model.*" %>
<!DOCTYPE html>
<% 
	//清掉SESSION的LOCATUION 這樣登出又登入後才不會回上一個登入存的路徑
	String location = (String)session.getAttribute("location");
	if(location != null)
	session.removeAttribute("location");
	
	List<GoodsVO> list = null;
	String member_id = request.getParameter("member_id");
	MemberVO membervo = new MemberService().getOneMemberByMemberId(member_id);
	pageContext.setAttribute("membervo", membervo);
	pageContext.setAttribute("member_id", member_id);
	
	list = new GoodsService().findGoodsByMember_idAlive(member_id);
	pageContext.setAttribute("list", list);
	
	//取得現在的時間
	Calendar cal =  Calendar.getInstance();
	pageContext.setAttribute("cal", cal);
	
	//為了顯示換物車商品數量
	Vector<ReqAndRes> cartList = (Vector<ReqAndRes>) session.getAttribute("cart");
	pageContext.setAttribute("cartList", cartList);
	//交易
	List<TranVO> tranList=null;
	if(session.getAttribute("member_id")!=null){
		TranService tranSvc=new TranService();
		tranList=tranSvc.getOneByResMember_idUn((String)session.getAttribute("member_id"));
		pageContext.setAttribute("tranList", tranList);
	}
%>
<jsp:useBean id="groupService" scope="page"
	class="com.group.model.GroupService" />
<html>
<head>
<link
	href="<%=request.getContextPath()%>/javascript_css/front_main_css/bootstrap.css"
	rel="stylesheet" type="text/css" media="all" />
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script
	src="<%=request.getContextPath()%>/javascript_css/front_main_js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/javascript_css/front_main_js/facebookAPI.js"></script>
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
<!--//fonts-->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商品專區</title>
<style>
td {
	text-align: center;
}
</style>
<script
	src="<%=request.getContextPath()%>/javascript_css/goods/countTime.js"></script>
</head>
<body>
	
		<%
	String g_name = request.getParameter("g_name");
%>

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
						<font color="blue" size="4"><b>你好,${sessionScope.member.mem_name}</b></font></a>
						<a
							href="<%=request.getContextPath()%>/front/member/MemberServlet.do?requestURL=<%=request.getServletPath()%>&action=logout&member_id=<%= member_id %>"
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
			<div class="header-bottom-bottom">
				<div class="top-nav">
					<span class="menu"> </span>
					<ul>
						<li id="mainMenu"><a
							href="<%=request.getContextPath()%>/front/index/index.jsp">首頁
						</a></li>
						<li id="mainMenu"><a
							href="<%=request.getContextPath()%>/front/goods/show_all_goods.jsp">商品專區</a></li>
						<li id="mainMenu" class="active"><a
							href="<%=request.getContextPath()%>/front/member/wish.jsp">許願池</a></li>
						<li id="mainMenu"><a
							href="<%=request.getContextPath()%>/front/ranking/showRanking.jsp">排行榜</a></li>
						<li id="mainMenu"><a
							href="<%=request.getContextPath()%>/front/message/listAllMessageTitle.jsp">討論區</a></li>
					</ul>
					<div class="clearfix"></div>
					<script>
						$("span.menu").click(function() {
							$(".top-nav ul").slideToggle(500, function() {
							});
						});
					</script>
					<div class="clearfix"></div>
				 
				</div>
				 	<span style="float:right; margin:15px;">
					<a href="<%=request.getContextPath()%>/front/goods/select_page.jsp">進階查詢</a>
					</span>
				<div class="search">
					<form action="<%=request.getContextPath()%>/front/goods/GoodsServlet.do" method="post">
						<input type="text" placeholder="<%=(g_name == null) ? "找商品" : g_name%>" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = '';}"  name="g_name">
						<input type="hidden" name="action" value="searchGoodsByName">
						<input type="hidden" name="requestURL" value="<%= request.getServletPath() %>">
						<input type="submit"  value="">
					</form>
				</div>
				
				<div class="clearfix"> </div>
				
				
			</div>
		</div>
	</div>
	<!--content-->
	<div class="content">
		<div class="container">		
			
			
<!-- ----------------------------------------------------------------------------------------------------------------------------------->			
			
			
			
			
			
			
			<c:if test="${not empty errorMessage}">
				<ul>
					<c:forEach var="message" items="${errorMessage}">
						<li>${message}</li>
					</c:forEach>
				</ul>
			</c:if>
			
			<% String my_wish = "";	
			   if(membervo.getMy_wish() != null)
			   my_wish =  membervo.getMy_wish().substring(1,membervo.getMy_wish().length()); 
						%>
			<div>會員: <font size="5" color="blue"><b>${membervo.mem_name }</b></font>
				的願望是:我想要<font size="5" color="red"><b><%= my_wish %></b></font><br>
				<font size="3" color="green"><b>${membervo.mem_name } 的商品:</b></font></div>
			
			<c:if test="${not empty list }">
			
					
			 
				
					<div><%@ include file="page1.file"%></div>  
				<%
					int count = 0;
						pageContext.setAttribute("count", count);
				%>
				<c:forEach var="goodsVO" items="${list}" begin="<%= pageIndex %>"
					end="<%= pageIndex + rowPerPage -1 %>">

					<c:if test="${count % 2 == 0 }">
						<div class="col-md-4 you-men">
							<a
								href="<%= request.getContextPath() %>/front/goods/goods_detail.jsp?gid=${goodsVO.gid}"
								id="imageLink<%= count %>"> <img
								class="img-responsive pic-in"
								src="<%= request.getContextPath() %>/showImage.do?gid=${goodsVO.gid}&action=goods"
								alt=" " style="width: 200px; height: 200px;"></a>

							<p>
								<font size="5" color="blue"><b>${goodsVO.g_name}</b></font>
							</p>
							<span>商品價格:${goodsVO.g_price} |
								<div id="quitdate<%=count%>"></div>
							</span>
						</div>
						<input type="hidden" id="goods_time<%= count %>"
							value="${goodsVO.quitdate.getTime() - cal.getTimeInMillis()}">
					</c:if>
					<c:if test="${count % 2 != 0 }">
						<div class="col-md-4 you-men para-grid">
							<a
								href="<%= request.getContextPath() %>/front/goods/goods_detail.jsp?gid=${goodsVO.gid}"
								id="imageLink<%= count %>"> <img
								class="img-responsive pic-in"
								src="<%= request.getContextPath() %>/showImage.do?gid=${goodsVO.gid}&action=goods"
								alt=" " style="width: 200px; height: 200px;"></a>

							<p>
								<font size="5" color="blue"><b>${goodsVO.g_name}</b></font>
							</p>
							<span>商品價格:${goodsVO.g_price} |
								<div id="quitdate<%=count%>"></div>
							</span>
						</div>
						<input type="hidden" id="goods_time<%= count %>"
							value="${goodsVO.quitdate.getTime() - cal.getTimeInMillis()}">
					</c:if>

					<%
						count++;
								pageContext.setAttribute("count", count);
					%>
				</c:forEach>
				<input type="hidden" id="count" value="<%=count%>">

				<%@ include file="page2.file"%>


				</c:if>
	<c:if test="${empty list }">
		查無任何商品
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