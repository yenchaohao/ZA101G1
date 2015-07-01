<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.member.model.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%
	String member_id = (String) request.getSession().getAttribute(
			"member_id");
	MemberVO membervo = new MemberService()
			.getOneMemberByMemberId(member_id);
	pageContext.setAttribute("MemberVO", membervo);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
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
<!--//fonts-->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
td {text-align: center;}
label{width: 130px;}
h6{ font-size: 150%; }					
</style>
<title>會員資料</title>
</head>
<body>
	<%@ include  file="/front/memberFile.file" %>


			<c:if test="${not empty errorMessage }">
				<ul>
					<c:forEach var="message" items="${errorMessage }">
						<li>${message}</li>
					</c:forEach>
				</ul>
			</c:if>


			<div class="article-top">
				<div class="col-md-4 article-para">
					
						<img
						src="<%= request.getContextPath() %>/showImage.do?member_id=${MemberVO.member_id}&action=member"
						weight="120" height="120">
					
					<h3 style="margin-top:20px;"><a
						href="<%= request.getContextPath() %>/front/member/MemberServlet.do?action=update_member&
																							member_id=${MemberVO.member_id}&
																							requestURL=<%= request.getServletPath() %>"><font size="5">點我修改資料/增加願望</font></a>
					</h3>
				</div>
			
				<div class="col-md-8 article-head">
					<h6 ><label >姓名:</label> ${MemberVO.mem_name}</h6>
					<h6 ><label >email:</label> ${MemberVO.email}</h6> 
					<h6 ><label >電話:</label> ${MemberVO.tel}</h6>
					<h6 ><label >地址:</label> ${MemberVO.address}</h6>
					<h6 ><label >生日:</label>  ${MemberVO.birthday}</h6>
					<h6 ><label>加入日期:</label> ${MemberVO.joindate}</h6>
					<h6 ><label >信用評價:</label> ${MemberVO.credit}</h6>
					<h6 ><label >擁有點數: </label> ${MemberVO.having_p}</h6>
					<h6 ><label >被扣押點數:</label> ${MemberVO.pending_p}</h6>
					<h6 ><label >會員狀態:</label> ${member_status[MemberVO.mem_status]}</h6>
					<% 
					String my_wish = "";	
					if(membervo.getMy_wish() != null)
						my_wish =  membervo.getMy_wish().substring(1,membervo.getMy_wish().length()); 
					%>
					<h6 ><label >我的願望: </label> <%= my_wish %> </h6>
					
				</div>
			</div>



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