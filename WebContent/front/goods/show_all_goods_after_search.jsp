<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.goods.model.*"%>
<%@ page import="com.group.model.*" %>
<%@ page import="java.util.*"%>

<!DOCTYPE html>
<%
	List<GoodsVO> list = (List<GoodsVO>)session.getAttribute("goodsList");
	pageContext.setAttribute("goodsList", list);
	
	//清掉SESSION的LOCATUION 這樣登出又登入後才不會回上一個登入存的路徑
	String location = (String)session.getAttribute("location");
	if(location != null)
		session.removeAttribute("location");
	
	Calendar cal =  Calendar.getInstance();
	pageContext.setAttribute("cal", cal);
	
	//為了要和show_goods_for_desired.jsp 共用同一個page2.file 多寫一個 member_id 才不會跳錯
		String member_id = null;
	
%>
<jsp:useBean id="groupService" scope="page" class="com.group.model.GroupService" />
<html>
<head>
<link
	href="<%=request.getContextPath()%>/javascript_css/front_main_css/bootstrap.css"
	rel="stylesheet" type="text/css" media="all" />
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script
	src="<%=request.getContextPath()%>/javascript_css/front_main_js/jquery.min.js"></script>
<script
	src="<%=request.getContextPath()%>/javascript_css/goods/countTime.js"></script>
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
<!--//fonts-->
<title>商品專區</title>
<style>
td {
	text-align: center;
}
</style>
</head>
<body> 
	 <%@ include  file="/front/indexFile.file" %>  
				<c:if test="${not empty errorMessage}">
		<ul>
			<c:forEach var="message" items="${errorMessage}">
				<li>${message}</li>
			</c:forEach>
		</ul>
	</c:if>

	<table border="1"> 

		<c:if test="${not empty goodsList }">
		

			<div><%@ include file="page1.file"%></div>
			
				<%
					int count = 0;
						pageContext.setAttribute("count", count);
				%>
			
			<c:forEach var="goodsVO" items="${goodsList}" begin="<%= pageIndex %>"
				end="<%= pageIndex + rowPerPage -1 %>">
				
					<c:if test="${count % 2 == 0 }">
						<div class="col-md-4 you-men">
							<a
								href="<%= request.getContextPath() %>/front/goods/goods_detail.jsp?gid=${goodsVO.gid}"
								id="imageLink<%= count %>"> <img
								class="img-responsive pic-in"
								src="<%= request.getContextPath() %>/showImage.do?gid=${goodsVO.gid}&action=goods"
								alt=" "  style="width:200px; height:200px;"></a>
							
							<p><font size="5" color="blue"><b>${goodsVO.g_name}</b></font></p>
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
								alt=" " style="width:200px; height:200px;"></a>
							
							<p><font size="5" color="blue"><b>${goodsVO.g_name}</b></font></p>
							<span>商品價格:${goodsVO.g_price} |
								<div id="quitdate<%=count%>"></div>
							</span>
						</div>
						<input type="hidden" id="goods_time<%= count %>"
							value="${goodsVO.quitdate.getTime() - cal.getTimeInMillis()}">
					</c:if>

					<%	count++;
						pageContext.setAttribute("count", count); %>

			</c:forEach>
			<input type="hidden" id="count" value="<%=count%>">
	</table>
	<%@ include file="page2.file"%>
	</c:if>
	
	<c:if test="${empty goodsList }">
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