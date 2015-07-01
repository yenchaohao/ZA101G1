<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="BIG5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.goods.model.*" %>
<%@ page import="java.util.*" %>

<jsp:useBean id="memberSvc" scope="page" class="com.member.model.MemberService"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="<%= request.getContextPath() %>/javascript_css/front_main_css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="<%= request.getContextPath() %>/javascript_css/front_main_js/jquery.min.js"></script>
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
	<script>
		function del(){
			var check=confirm("確定升級成為VIP會員?");
			if(check==false){
				event.returnValue=false;
				return ;
			}
		}
	</script>
<!------------------------------------------------------------------------------- code -->
	<c:if test="${not empty errorMessage }">
		<ul>
			<c:forEach var="message" items="${errorMessage }">
				<li>${message}</li>
				<li>欲想升級VIP會員，請按升級VIP按鈕，並扣除點數500點</li>
				<h3><font color="red"><b>升級成VIP會員可使用商品廣告上架功能,以及商品上架數量上限增至10件</b></font></h3>
				<li>
					<form method="post" action="<%=request.getContextPath()%>/front/vipad/vipadServlet.do" onsubmit="del()">
						<input type="hidden" name="action" value="upVip">
						<input type="submit" value="升級VIP" class="btn btn-info">
					</form>
				</li>
			</c:forEach>
		</ul>
	</c:if>
	<c:if test="${empty errorMessage }">
	<h2>VIP會員${memberSvc.getOneMemberByMemberId(sessionScope.member_id).mem_name}您好:</h2>
		<form method="post" action="<%=request.getContextPath()%>/front/vipad/addVipad.jsp" style="z-index:1;position:absolute;">
			<input type="submit" value="新增廣告" class="btn btn-info btn-lg">
		</form>
		<form method="post" action="<%=request.getContextPath()%>/front/vipad/vipadServlet.do" style="margin-left:150px">
			<input type="hidden" name="action" value="showVipad">
			<input type="submit" value="VIP廣告" class="btn btn-info btn-lg">
		</form>
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