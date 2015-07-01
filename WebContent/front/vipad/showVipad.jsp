<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="BIG5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.goods.model.*" %>
<%@ page import="com.vipad.model.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.member.model.*" %>
<%
	VipadService vipadSvc = new VipadService();
	List<VipadHibernateVO> vipadList = vipadSvc.getVipadByMemberAllAlive((String)session.getAttribute("member_id"));
	pageContext.setAttribute("vipadList", vipadList);
%>

<jsp:useBean id="goodsSvc" scope="page" class="com.goods.model.GoodsService"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="<%= request.getContextPath() %>/javascript_css/front_main_css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="<%= request.getContextPath() %>/javascript_css/front_main_js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/javascript_css/goods/countTime.js"></script>
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

<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
</head>
<body>
<%@ include  file="/front/memberFile.file" %>
<!------------------------------------------------------------------------------- code -->
	<c:if test="${not empty errorMessage }">
		<ul>
			<c:forEach var="message" items="${errorMessage }">
				<li>${message}</li>
			</c:forEach>
		</ul>
	</c:if>
	<c:if test="${not empty vipadList}">
	
			<%
					List<GoodsVO> goodsList = new GoodsService().getAll();
					pageContext.setAttribute("goodsList", goodsList);
					List<MemberVO> memberList = new MemberService().getAll();
					pageContext.setAttribute("memberList", memberList);
			%>
			<form method="post" action="<%= request.getContextPath() %>/front/vipad/vipadServlet.do">
			<div class="lady-in-on">
				<ul id="flexiselDemo1">
					<c:forEach var="vipadVO" items="${vipadList}">
						<li><a href="<%= request.getContextPath() %>/front/goods/goods_detail.jsp?gid=${vipadVO.goodsVO.gid}"><img class="img-responsive women"
								src="<%= request.getContextPath() %>/showImage.do?action=goods&gid=${vipadVO.goodsVO.gid}"
								alt="" style="width: 150px; height: 150px;"></a>
							<div class="hide-in">
								<p style=" margin-left:50px;"><label style="width:100px">商品名稱:</label>${vipadVO.goodsVO.g_name}</p>
								<p style=" margin-left:50px;"><label style="width:100px">廣告上架日期:</label><font color="red">${vipadVO.getJoindateFormat()}</font></p>
								<p style=" margin-left:50px;"><label style="width:100px">廣告下架日期:</label><b><font color="red">${vipadVO.getQuitdateFormat()}</font></b></p>
								<p style=" margin-left:150px;"><input type="checkbox" name="box" value="${vipadVO.vid}" style="width:20px;height:20px"></p>
							</div></li>
					</c:forEach>
				</ul>
					<input type="submit" value="確認刪除" style="float:right" class="btn btn-danger">
					<input type="hidden" name="action" value="delete">
					</form>
				<script type="text/javascript">
					$(window).load(function() {
						$("#flexiselDemo1").flexisel({
							visibleItems : 3,
							animationSpeed : 1000,
							autoPlay : true,
							autoPlaySpeed : 3000,
							pauseOnHover : true,
							enableResponsiveBreakpoints : true,
							responsiveBreakpoints : {
								portrait : {
									changePoint : 480,
									visibleItems : 1
								},
								landscape : {
									changePoint : 640,
									visibleItems : 2
								},
								tablet : {
									changePoint : 768,
									visibleItems : 3
								}
							}
						});

					});
				</script>
				<script type="text/javascript"
					src="<%=request.getContextPath()%>/javascript_css/front_main_js/jquery.flexisel.js"></script>
			</div>
	</c:if>
	<c:if test="${empty vipadList && empty errorMessage}">
		所有廣告已刪除
	</c:if>
<!-------------------------------------------------------------- end -->	
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