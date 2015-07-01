<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.goods.model.*" %>
<%@ page import="com.goodsimage.model.*" %>
<%@ page import="com.group.model.*" %> 
<%@ page import="java.util.*" %>
<% 
   //從請求參數取得商品
   GoodsVO goodsvo =(GoodsVO) request.getAttribute("GoodsVO"); 
   pageContext.setAttribute("goodsvo", goodsvo);	
	//用商品的編號去尋找該商品的附圖
   List<GoodsImageVO> list = new GoodsImageService().findGoodsImageByGid(goodsvo.getGid());
   pageContext.setAttribute("list", list);
   //取得商品群組供選擇
   List<GroupVO> grouplist = new GroupService().getAll();
   pageContext.setAttribute("grouplist",grouplist );
   
	String[] level = {"非常好","好","普通"};
	pageContext.setAttribute("level", level);
%>
<html>
<head>
<link href="<%= request.getContextPath() %>/javascript_css/front_main_css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
<script src="<%= request.getContextPath() %>/javascript_css/front_main_js/jquery.min.js"></script>
<link href="<%= request.getContextPath() %>/javascript_css/front_main_css/style.css" rel="stylesheet" type="text/css" media="all" />	
<meta name="viewport" content="width=device-width, initial-scale=1">
<script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } </script>
<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700,800' rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Oswald:400,700,300' rel='stylesheet' type='text/css'>
<link rel="stylesheet" href="<%= request.getContextPath() %>/javascript_css/goods/addgoods.css">
<!-- 參照到更新商品資料的js檔 -->
<script src="<%= request.getContextPath() %>/javascript_css/goods/updateGoods.js" ></script>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<link rel="stylesheet" href="/resources/demos/style.css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改商品資料</title>
<style>
td {
	text-align: center;
}
</style>
</head>
<body>
	<%@ include  file="/front/memberFile.file" %>
	<!--content-->
	
	
	<c:if	test="${not empty errorMessage }">
		<ul>
			<c:forEach var="message" items="${errorMessage}">
				<li>${message }</li>
			</c:forEach>
		</ul>
	
	</c:if>


		<form action="<%= request.getContextPath() %>/front/goods/GoodsServlet.do" method="post" enctype="multipart/form-data" name="addGoodsForm">
		
		<label for="pic" >請上傳商品主照片:</label>
		<input type="file" name="pic" id="pic0" class="pic" ><br>
		
		<label for="pic"id="picTitle1" >請上傳商品附加照片1:</label>
		<input type="file" name="pic1" id="pic1" class="pic" >
		<!-- 刪除的按鈕, 由前端控制是否要隱藏 (updateGoods.js)-->
		<input type="button" name="1delete" id="1delete" value="刪除" class="delete">
		<br>
		
		<label for="pic" id="picTitle2">請上傳商品附加照片2:</label>
		<input type="file" name="pic2" id="pic2" class="pic" >
		<input type="button" name="2delete" id="2delete" value="刪除" class="delete">
		<br>
		 
		<label for="pic" id="picTitle3" >請上傳商品附加照片3:</label>
		<input type="file" name="pic3" id="pic3" class="pic" >
		<input type="button" name="3delete" id="3delete" value="刪除" class="delete">
		<br>
		<!-- 顯示新增圖按鈕的區塊,由updateGoods.js 控制 -->
		<span id="buttonArea">
		</span>
		
		<nav id="previewArea" >
			<img src="<%= request.getContextPath() %>/showImage.do?gid=<%= goodsvo.getGid() %>&action=goods"  width="120" height="120" id="imgpic0" style="border:solid 2px red">
		<!-- 計算從資料庫撈出的商品附圖數量 -->
		<% int n = 0; %>
		<c:if test="${ not empty list}">
			<c:forEach var="goodsImagevo" items="${list }">
				<!-- 顯示附圖,n++,"imgpic1" <-- 前端會抓此id來控制顯示或隱藏圖片 -->
				<img src="<%= request.getContextPath() %>/showImage.do?pic_number=${ goodsImagevo.pic_number }&action=goodsImage"  width="120" height="120" id="imgpic<%= ++n %>" > 
				<!-- 這張圖片的id , 作為後端更新,刪除的依據  pidId1 -->
				<input type="hidden" id="picId<%= n %>" name="picId<%= n %>" value="${goodsImagevo.pic_number}">
				<!-- 這張圖片的修改狀態  "pic1change" <-- 預設為0,使用者操作修改或刪除會在前端 updateGoods.js 控制,如果使用者修改這張圖
				如果使用者修改這張圖 此欄位的值會是1,如果是刪除則會是2 -->
				<input type="hidden" id="pic<%= n %>change" name="pic<%= n %>change" value="0" >
			</c:forEach>
		<!-- 資料庫撈出來的圖有幾張,updateGoods.js 在網頁初始載入時會依此數字去做要顯示幾個input type="file" 和 是否要顯示新增圖片按鈕  -->
			<input type="hidden" name="imageCount" id="imageCount" value="${list.size()}">
		</c:if>
	<!--  如果原有附圖數量不到3張的話 , 要把picId 和 picchange 的hidden欄位補上,後端getParameter 才會拿到的值做參考,前端去增加圖也才可以修改change欄位的值  -->
			<% if(n < 3){ 
				for(int i = n ; i < 3 ; i++){ %>
					<input type="hidden" id="picId<%= i+1 %>" name="picId<%= i+1 %>" value="">
					<input type="hidden" id="pic<%= i+1 %>change" name="pic<%= i+1 %>change" value="0" >
			<% 
				}				
			}%>
		</nav>
		
		
		
		<label for="groupid">請選擇商品類別</label>
		<select name="groupid">
			<c:forEach var="group" items="${grouplist}">
				<option value="${group.groupid }" ${(group.groupid == goodsvo.groupid)? 'selected' : '' } >${group.group_name }</option>
			</c:forEach>
		</select>
		
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
		 ><br>
		<label for="g_level" >請選擇商品外觀等級:</label>
		<select name="g_level">
			<c:forEach var="i" begin="0" end="2">
				<option value="${i}"  ${(goodsvo==null)? '' : (goodsvo.g_level == i)? 'selected' : '' }  >${level[i]}</option>
			</c:forEach>
		</select><br>
		
		<input type="hidden" name="action" value="update_goods_input" >
		<input type="hidden" name="requestURL" value="<%= request.getParameter("requestURL") %>">
		<input type="hidden" name="gid" value="${goodsvo.gid}">
		<input type="hidden" name="whichPage" value="<%= request.getParameter("whichPage") %>">
		<input type="hidden" name="member_id" value="<%= request.getParameter("member_id") %>">
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