<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.send.model.*"%>
<%@ page import="com.goods.model.*"%>
<%@ page import="com.member.model.*"%>
<%@ page import="java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String member_id = (String)session.getAttribute("member_id");
	pageContext.setAttribute("member_id", member_id);
	
	SendVO sendvo = new SendService().getOneSend(Integer.parseInt(request.getParameter("sid")));
    pageContext.setAttribute("sendvo", sendvo);
     
    List<SendVO> list = new SendService().getSendByTid(Integer.parseInt(request.getParameter("tid")));
   	pageContext.setAttribute("list", list);
    
 	//要收到的物品
   	GoodsVO res_goods = null;
    //要交出的物品
 	GoodsVO req_goods = null;
    for(SendVO send : list){

    	if(send.getMember_id().equals(member_id)){
    		req_goods = new GoodsService().findGoodsByGid(send.getGid());
    		pageContext.setAttribute("req_goods", req_goods);
    	}else{
    		res_goods = new GoodsService().findGoodsByGid(send.getGid());
    		pageContext.setAttribute("res_goods", res_goods); 
    	}
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
<script type="application/x-javascript">
	
	 addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } 

</script>
<!--fonts-->
<link
	href='http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700,800'
	rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Oswald:400,700,300'
	rel='stylesheet' type='text/css'>
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
						<li><a href="../customerCenter/contact.html" class="scroll">客服中心</a></li>
						<li><a
							href="<%=request.getContextPath()%>/front/member/show_one_member.jsp"
							class="scroll">會員專區 </a></li>
						<li><a
							href="<%=request.getContextPath()%>/front/cart/cartServlet.do?action=showCart"
							class="scroll"> <!--<img src="<%=request.getContextPath()%>/images/car.png">-->換物車
						</a></li>
					</ul>
				</div>
				<div class="header-grid-right">

					<c:if test="${not empty sessionScope.member_id }">
						<a
							href="<%=request.getContextPath()%>/front/member/show_one_member.jsp">
							<img
							src="<%= request.getContextPath() %>/showImage.do?member_id=${sessionScope.member_id}&action=member"
							weight="50" height="50">
						</a>
						<a
							href="<%=request.getContextPath()%>/front/member/MemberServlet.do?requestURL=<%=request.getServletPath()%>&action=logout&whichPage=<%=request.getParameter("whichPage")%>"
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
					<!--<img class="img-responsive" src="<%=request.getContextPath()%>/images/ad.png" alt=" " >-->
				</div>
				<div class="clearfix"></div>
			</div>

		</div>
	</div>
	<!--content-->
	<div class="content">
		<div class="container">

			<c:if test="${ not empty list }">
				<div class="single_grid" >
				<div class="grid images_3_of_2" style="margin-top:20px;">
				<h1>恭喜你,交易已完成</h1>
				<h2>請出示此QRcode給送貨員用來驗證你的身份</h2>
				<img
					src="<%=request.getContextPath()%>/showImage.do?action=qrcode&sid=<%=request.getParameter("sid")%>"  style="margin-bottom:50px; margin-top:50px; ">
				
				<div class="clearfix"> </div>	
				</div>
				
				<div class="span1_of_1_des" style="margin-top:50px;">
				<div class="desc1 "  >	
					<h2>你的物品</h2>
					<table>
						<tr>
							<th>商品圖示</th>
							<th>商品名稱</th>
						</tr>
						<tr>
							<td><img
								src="<%= request.getContextPath() %>/showImage.do?gid=${req_goods.gid}&action=goods"
								width="120" height="120"></td>
							<td>${req_goods.g_name}</td>
						</tr>
					</table>


					<h2>想要換的物品
					</h2>
					<table>
						<tr>
							<th>商品圖示</th>
							<td>商品名稱</td>
							<td>擁有人</td>
						</tr>
						<tr>
							<td><img
								src="<%= request.getContextPath() %>/showImage.do?gid=${res_goods.gid}&action=goods"
								width="120" height="120"></td>
							<td>${res_goods.g_name}</td>
							<%
								MemberVO membervo = new MemberService()
											.getOneMemberByMemberId(res_goods.getMember_id());
									pageContext.setAttribute("membervo", membervo);
							%>
							<td>${membervo.mem_name}</td>
						</tr>
					</table>

					<h1>派送狀態</h1>
					<b>${send_status[sendvo.status]}</b>
					<c:if test="${sendvo.status == 1 }">
						<b>派送開始時間:</b><h2>${sendvo.getStart_date_format()}</h2>
					</c:if>
						</div>
					</div>
				</div>
			</c:if>



			<c:if test="${empty list }">
				<h1>資訊載入錯誤,請再試一次</h1>
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