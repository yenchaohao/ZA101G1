<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.goods.model.*"%>
<%@ page import="com.group.model.*"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<%
	//清掉SESSION的LOCATUION 這樣登出又登入後才不會回上一個登入存的路徑
	String location = (String)session.getAttribute("location");
	if(location != null)
	session.removeAttribute("location");
	
	List<GoodsVO> list = null;
	String member_id = (String)request.getSession().getAttribute("member_id");
	
	//建立一個變數query 從  action 取值. 如果使用者有使用查詢商品的功能, 此action會有值 如果否則沒有. 將此值放到page2.file 用 el 表示法 
	String query = request.getParameter("action");
	
	//將此值放到page2.file 的"下一頁"超連結上面,這樣可以依照此值知道這次的"換頁請求"是要顯示 "查詢後的結果" 還是 "全部" 
	//  用 el 表示法 可以避免掉如果 其他頁面也有include page2.file 而不會發生找不到 query 這個變數然後錯誤的情況 
	pageContext.setAttribute("query", query);
	
	//如果使用者有做查詢的的請求 , 會進入這個if 代表頁面要顯示 查詢後的結果 
	if(request.getParameter("action") != null && request.getParameter("action").equals("query")){
		
		//這邊是要判斷 是否要做查詢,如果使用者有輸入條件就進入這個if 然後開始查詢
		if(request.getParameter("g_name") != null || request.getParameter("goods_status")!= null){
			list = new GoodsService().getAllMapByMember(request.getParameterMap());
			//如果查詢不到任何結果
			if(list == null){
				List<String> errorMessage = new LinkedList();
				errorMessage.add("查無任何商品");
				pageContext.setAttribute("errorMessage", errorMessage);
				list = new GoodsService().findGoodsByMember_id(member_id);
				pageContext.setAttribute("searchlist", list);
			//如果查詢的到
			}else
				session.setAttribute("searchlist", list);
		//如果沒有要做查詢,就顯示上一次查詢的結果 (主要用的時機是在換頁的時候)
		}else
			list = (List<GoodsVO>)session.getAttribute("searchlist");
	
	//頁面要直接顯示全部
	}else{ 
		list = new GoodsService().findGoodsByMember_id(member_id);
		pageContext.setAttribute("searchlist", list);
		pageContext.removeAttribute("errorMessage");
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
<title>商品專區</title>
<style>
td {
	text-align: center;
}
</style>
</head>
<body> 
	<%@ include  file="/front/memberFile.file" %>
	<div><%@ include file="page1.file"%></div>		
			
			<c:if test="${not empty errorMessage}">
				<ul>
					<c:forEach var="message" items="${errorMessage}">
						<li>${message}</li>
					</c:forEach>
				</ul>
			</c:if>

			<a href="<%=request.getContextPath()%>/front/goods/addGoods.jsp"><b><font
					size="5" color="red" face="verdana">點我上傳商品</font></b></a>
			
		<span style="margin-left:10px;">
			
			<form action="<%= request.getContextPath() %>/front/goods/show_goods_by_member.jsp" method="post" class="form-inline">
			
			<select name="goods_status" class="form-control"> 
				<option value="">請選擇</option>
				<option value="0">未上架</option>
				<option value="1">已上架</option>
				<option value="3">完成交易的商品</option>
			</select>
			<div class="form-group"> 
			<label for="g_name" class="sr-only" >商品名稱</label>
			<input type="text" name="g_name" id="g_name" class="form-control" placeholder="商品名稱">
			</div>
			<input type="hidden" value="<%= member_id %>" name="member_id">
			<input type="hidden" value="<%= whichPage %>" name="whichPage">
			
			<input type="hidden" value="query" name="action">			
			<input type="submit" value="查詢" class="btn btn-info"> 
			</form> 
			
			</span>
			
			<br>
					
			
			
			<input type="hidden" id="url" value="<%=request.getContextPath()%>">
			<div id="MyGoods">
			
			<c:if test="${not empty searchlist }">

				

				<div class="sale_grid">
					<form
						action="<%=request.getContextPath()%>/front/goods/GoodsServlet.do"
						method="post">
						<%
							int count = 0;
						%>
						<c:forEach var="goodsVO" items="${searchlist}" begin="<%= pageIndex %>"
							end="<%= pageIndex + rowPerPage -1 %>">

							<ul class="grid_1">
								<li class="grid_1-img" style="width:150px;"><a
									href="<%= request.getContextPath() %>/front/goods/GoodsServlet.do?action=update_goods&
																							gid=${goodsVO.gid}&
																							requestURL=<%= request.getServletPath()  %>&
																							whichPage=<%= whichPage %>&
																							member_id=<%= member_id %>">
										<img
										src="<%= request.getContextPath() %>/showImage.do?gid=${goodsVO.gid}&action=goods"
										 style="width:150px; height:150px;">
								</a>
								
							</li>
							<li class="grid_1-img" style="width:150px;">
							<p><label style=" width: 100px;">商品名稱:</label></p><p><b><font size="5" color="green">${goodsVO.g_name}</font></b></p>
								<p><label style=" width: 100px;">押金:</label></p><p><b><font size="5" color="red">${goodsVO.g_price}</font></b></p>
								<p><label style=" width: 150px;">
								<a
									href="<%= request.getContextPath() %>/front/goods/GoodsServlet.do?action=update_goods&
																							gid=${goodsVO.gid}&
																							requestURL=<%= request.getServletPath()  %>&
																							whichPage=<%= whichPage %>&
																							member_id=<%= member_id %>">
								點我修改商品資料
								</a></label></p>
							</li>
								<li class="grid_1-img" style="width:150px;">
									<c:forEach var="groupvo"
										items="${groupService.all }">
										<c:if test="${goodsVO.groupid == groupvo.groupid}">
										<p style="width:500px;"><label style=" width: 100px;">商品類別:</label>${groupvo.group_name}</p>
									</c:if>
									</c:forEach> 
									
									<p style=" width: 250px;"><label style=" width: 100px;">外觀等級:</label>${goods_level[goodsVO.g_level]}</p> 
									<p style=" width: 250px;"> <label style=" width: 100px;">商品狀態:</label>${goods_status[goodsVO.goods_status]}</p> 
									<c:if test="${ goodsVO.goods_status != 0  }">
										<p style="width:300px;">
										<label style=" width: 100px;">上架日期:</label>
										<font color="red">${goodsVO.getJoindateFormat()}</font></p>
										<p style="width:300px;"><label style=" width: 100px;">下架日期:</label>
										<b><font color="red">${goodsVO.getQuitdateFormat()}</font></b></p>
										</c:if>
									<p style="width:500px;"><label style=" width: 100px;">商品描述:</label>${goodsVO.g_describe}</p>	 
								</li>
								<li class="grid_1-img" style="width:150px; margin-left:200px;" >
										<p>
										<label style=" width: 100px;">狀態修改:</label>
									<select name="status<%=count%>">
										<option value="9">請選擇</option>
										<c:if test="${goodsVO.goods_status == 0}">
											<option value="1">上架</option>
											<option value="2">刪除</option>
										</c:if>
										<c:if test="${goodsVO.goods_status == 1}">
											<option value="0">下架</option>
										</c:if>
									</select>
									</p>
								</li>
								
								
								<div class="clearfix"> </div>	

							</ul>
							<input type="hidden" name="gid<%= count %>"
								value="${goodsVO.gid }">
							<%
								count++;
							%>
						</c:forEach>
						<input type="hidden" name="count" value="<%=count + 1%>">

						<%@ include file="page2.file"%>
				</div>


			</c:if>
			<c:if test="${empty searchlist }">
				查無任何商品
			</c:if>

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