<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.member.model.*" %> 
<%@ page import="com.group.model.*" %>   
<%@ page import="com.goods.model.*" %>  
<%@ page import="java.util.*" %>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
 <% 
 	//從member_id 取得會員姓名
 	String member_id = (String) request.getSession().getAttribute("member_id");
 	MemberVO membervo = new MemberService().getOneMemberByMemberId(member_id);
 	pageContext.setAttribute("membervo", membervo);
 	
 	//取得商品類別
 	List<GroupVO> grouplist = new GroupService().getAll();
 	pageContext.setAttribute("grouplist", grouplist);
 	 
 	String[] level = {"非常好","好","普通"};
 	pageContext.setAttribute("level", level);
 	
 	GoodsVO goodsvo = (GoodsVO)request.getAttribute("GoodsVO");
 	pageContext.setAttribute("goodsvo", goodsvo);
 %>
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
<script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } </script>
<!--fonts-->
<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700,800' rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Oswald:400,700,300' rel='stylesheet' type='text/css'>
<!--//fonts-->

<link rel="stylesheet" href="<%= request.getContextPath() %>/javascript_css/goods/addgoods.css">
<script src="<%= request.getContextPath() %>/javascript_css/goods/addgoods.js"  ></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新增商品</title>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<link rel="stylesheet" href="/resources/demos/style.css" />
</head>
<body>
<div class="header">
		<%@ include  file="/front/memberFile.file" %>
	<!--content-->
	<c:if test="${not empty errorMessage }">
		<ul>
			<c:forEach var="message" items="${errorMessage}">
				<li>
					${message}
				</li>
			</c:forEach>
		</ul>
	</c:if>
	<c:if test="${not empty membervo}">
		<h1>
			${membervo.mem_name} 你好,請輸入商品資料
		</h1>
	</c:if>
	
	<form action="<%= request.getContextPath() %>/front/goods/GoodsServlet.do"  method="post" enctype="multipart/form-data" name="addGoodsForm">
		
		<label for="pic" >請上傳商品主照片:</label>
		<input type="file" name="pic" id="pic0" class="pic" ><font color="red">(必填)</font><br>
		<label for="pic" >請上傳商品附加照片1:</label>
		<input type="file" name="pic1" id="pic1" class="pic"  ><br>
		<label for="pic" >請上傳商品附加照片2:</label>
		<input type="file" name="pic2" id="pic2" class="pic" ><br>
		<label for="pic" >請上傳商品附加照片3:</label>
		<input type="file" name="pic3" id="pic3" class="pic" ><br>
		<nav id="previewArea" ></nav>
		
		<label for="group">請選擇商品類別: </label>
		<select name="groupid">
			<c:forEach var="group" items="${grouplist }">
				<option value="${group.groupid}"   ${(goodsvo==null)? '' : (goodsvo.groupid == group.groupid)? 'selected' : '' } >${group.group_name}</option>
			</c:forEach>
		</select><br>
		
		<label for="g_name" >請輸入商品名稱:</label>
		<input type="text" name="g_name" id="g_name"
			value="${(goodsvo==null)? '' : goodsvo.g_name }"
		 ><font color="red">(必填)</font><br>
		<label for="g_describe" >請輸入商品描述:</label><br>
		<textarea  name="g_describe" id="g_describe" rows="4" cols="50">
		${(goodsvo==null)? '' : goodsvo.g_describe}
		</textarea><font color="red">(必填)</font><br>
		<label for="g_price" >請輸入商品價格:</label>
		<input type="text" name="g_price" id="g_price" 
		value="${(goodsvo==null)? '' : goodsvo.g_price}"
		 ><font color="red">(必填)</font><br>
		<label for="g_level" >請選擇商品外觀等級:</label>
		<select name="g_level">
			<c:forEach var="i" begin="0" end="2">
				<option value="${i}"  ${(goodsvo==null)? '' : (goodsvo.g_level == i)? 'selected' : '' }  >${level[i]}</option>
			</c:forEach>
		</select><br>
		
		<input type="hidden" name="action" value="addGoods">
		<input type="button" value="送出"  id="send_button">
	</form>
	
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