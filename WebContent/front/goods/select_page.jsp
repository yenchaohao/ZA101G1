<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.group.model.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.cart.controller.*" %>

<%
	List<GroupVO> list = new GroupService().getAll();
	pageContext.setAttribute("list", list);
	String id = request.getParameter("groupid");
	String start_price = request.getParameter("mem_name");
	String end_price = request.getParameter("mem_name");

	
	//清掉SESSION的LOCATUION 這樣登出又登入後才不會回上一個登入存的路徑
	String location = (String)session.getAttribute("location");
	if(location != null)
		session.removeAttribute("location");
	
	//為了顯示換物車商品數量
	Vector<ReqAndRes> cartList = (Vector<ReqAndRes>) session.getAttribute("cart");
	pageContext.setAttribute("cartList", cartList);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="<%= request.getContextPath() %>/javascript_css/front_main_css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
<script src="<%= request.getContextPath() %>/javascript_css/front_main_js/jquery.min.js"></script>
<link href="<%= request.getContextPath() %>/javascript_css/front_main_css/style.css" rel="stylesheet" type="text/css" media="all" />	
<link rel="stylesheet" href="<%=request.getContextPath()%>/javascript_css/member/addmember.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商品查詢</title>
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
					</c:if>
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

<!-- -------------------------------------------------------------------------------------------------- -->	
	<c:if test="${not empty errorMessage}">
		<ul>
			<c:forEach var="message" items="${errorMessage }">
				<li>${message}</li>
			</c:forEach>
		</ul>
	</c:if>
	
	<script>
			$(document).ready(function(){
				
						$("#queryBtn").click(function (){
							//如果有輸入點數範圍查詢的話
							if($("#start_price").val().length != 0 || $("#end_price").val().length != 0 ){
								//先判斷是不是數字
								if(isNaN($("#start_price").val()) || isNaN($("#end_price").val())){
									alert('點數範圍必須為數字');
									return;
								}
								//判斷end或start有沒有輸入
								if($("#start_price").val().length != 0 &&  $("#end_price").val().length == 0 ){
									alert('請輸入點數最高範圍');
									return;
								}else if($("#end_price").val().length != 0 &&  $("#start_price").val().length == 0 ){
									alert('請輸入點數最低範圍');
									return;
								}
								//判斷最低金額是否筆最高金額小 
								var s = parseInt($("#start_price").val());
								var e = parseInt($("#end_price").val());
								if(s>e){
									alert('最低金額: '+s+' 比最高金額: '+e+' 還大,輸入錯誤請重新輸入');
									return;
								} 
							}
							
							queryForm.submit();
						});
						
			});
	</script>
	<form
		action="<%=request.getContextPath()%>/front/goods/GoodsServlet.do" method="post" name="queryForm" class="form-inline">
		
		<label for="g_name">請輸入商品名稱:</label> 
		<input type="text" name="g_name" class="form-control" ><br>
		
		<label for="g_name">請輸入會員姓名:</label> 
		<input type="text" name="mem_name" class="form-control" ><br>
		

		<label>選擇商品類別:</label> 
		<select name="groupid" class="form-control" >
		<option value="">請選擇</option>
			<c:forEach var="group" items="${list}">
				<option value="${group.groupid}" ${(id == null)? '' : (group.groupid == id)? 'selected' : '' } >${group.group_name}</option>
			</c:forEach>
		</select><br> 
	
		<label>押金點數範圍在:</label>
		  <input type="text" class="form-control" id="start_price" name="start_price" size="2" value="<%=(start_price == null) ? "" : start_price%>"><b>(點)<font color="red">最低</font>和</b>
		   <input type="text" class="form-control" id="end_price" name="end_price" size="2" value="<%=(end_price == null) ? "" : end_price%>" ><b>(點)<font color="red">最高</font>之間</b><br>	
		
		<input type="button" value="查詢" id="queryBtn" class="btn btn-info"> 
		<input type="hidden" name="action" value="CompositeQuery">
		<input type="hidden" name="requestURL" value="<%= request.getServletPath() %>">
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