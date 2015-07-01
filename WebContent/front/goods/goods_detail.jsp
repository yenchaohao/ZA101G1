<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.goods.model.*"%>
<%@ page import="com.member.model.*"%>
<%@ page import="com.goodsimage.model.*"%>
<%@ page import="com.tran.model.*"%>
<%@ page import="com.report.model.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.cart.controller.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String member_id = (String)session.getAttribute("member_id");
	//此頁要顯示的商品id
	int  gid = Integer.parseInt(request.getParameter("gid"));
	pageContext.setAttribute("gid", gid);
	//goodsvo 存放此商品的訊息
	GoodsVO goodsvo = new GoodsService().findGoodsByGid(gid);
	pageContext.setAttribute("goodsvo", goodsvo);
	//此商品的擁有者
	MemberVO membervo = new MemberService().getOneMemberByMemberId(goodsvo.getMember_id());
	pageContext.setAttribute("membervo", membervo);
	//顯示此商品的附加圖片
	List<GoodsImageVO> list = new GoodsImageService()
	.findGoodsImageByGid(gid);
	pageContext.setAttribute("list", list);
	//清掉SESSION的LOCATUION 這樣登出又登入後才不會回上一個登入存的路徑
	String location = (String)session.getAttribute("location");
	if(location != null)
		session.removeAttribute("location");
	//為了顯示換物車商品數量
	Vector<ReqAndRes> cartList = (Vector<ReqAndRes>) session.getAttribute("cart");
	pageContext.setAttribute("cartList", cartList);
	
	List<TranVO> tranList=null;
	if(session.getAttribute("member_id")!=null){
		TranService tranSvc=new TranService();
		tranList=tranSvc.getOneByResMember_idUn((String)session.getAttribute("member_id"));
		pageContext.setAttribute("tranList", tranList);
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/javascript_css/goods/addgoods.css">
<link
	href="<%=request.getContextPath()%>/javascript_css/front_main_css/style.css"
	rel="stylesheet" type="text/css" media="all" />
<link
	href="<%=request.getContextPath()%>/javascript_css/front_main_css/bootstrap.css"
	rel="stylesheet" type="text/css" media="all" />
<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script src="<%=request.getContextPath()%>/javascript_css/goods/change.js"></script>
<script src="<%=request.getContextPath()%>/javascript_css/goods/report.js"></script>
<script src="<%=request.getContextPath()%>/javascript_css/front_main_js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/javascript_css/front_main_js/jquery.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/javascript_css/front_main_css/etalage.css">   <%--  ul: etalage 會給javascript用 --%>
<script src="<%=request.getContextPath()%>/javascript_css/front_main_js/jquery.etalage.min.js"></script> <%--  ul: etalage 會給javascript用 --%>
<script src="<%=request.getContextPath()%>/javascript_css/front_main_js/facebookAPI.js"></script>
<%--  ul: etalage 會給javascript用 --%>
<script>
jQuery(document).ready(function($){

	$('#etalage').etalage({
		thumb_image_width: 300,
		thumb_image_height: 400,
		source_image_width: 900,
		source_image_height: 1200,
		show_hint: true,
		click_callback: function(image_anchor, instance_id){
			alert('Callback example:\nYou clicked on an image with the anchor: "'+image_anchor+'"\n(in Etalage instance: "'+instance_id+'")');
		}
	});
	
});
</script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="/resources/demos/style.css" />
<title>商品詳情</title>
</head>
<body>
<%--  跳出檢舉商品視窗的內容 (report.js 開啟) --%>
<%@ include file="/front/goods/report.file"  %>
<%--  跳出交換商品視窗的內容 (change.js 開啟) --%>
<%@ include file="/front/goods/change.file"  %>

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
							<font color="blue" size="4"><b>你好,${sessionScope.member.mem_name}</b></font></a>
						</a>
						<a
							href="<%=request.getContextPath()%>/front/member/MemberServlet.do?requestURL=<%=request.getServletPath()%>&action=logout&gid=<%=gid%>"
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
						<li id="mainMenu" class="active"><a
							href="<%=request.getContextPath()%>/front/goods/show_all_goods.jsp">商品專區</a></li>
						<li id="mainMenu"><a
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
		    
			<div class="single_grid" > <%-- single_grid 先在外層用這個把要顯示的資訊包起來  --%>
				<%-- 如果該會員已經檢舉過商品,提醒訊息會秀在這個span (report.js) --%>
				<span id="reportMessage"></span>
				<div>
					<c:if test="${not empty errorMessage}">
						<ul>
							<c:forEach var="message" items="${errorMessage}">
								<%-- 把老師原本的ul 改成 span 就不會有"." --%>
								<span id="message">${message}</span>
							</c:forEach>
						</ul>
					</c:if>
				</div>
				

				<c:if test="${not empty goodsvo }">

					<div class="grid images_3_of_2" style="margin-top:20px;"> <%-- grid images_3_of_2  包圖片的 --%>
						<ul id="etalage">  <%--  ul: etalage 會給javascript用 --%>
							<li>
								<%--  一張圖片要2張圖給他,一張大圖一張小圖  --%>
								<%--  class="etalage_thumb_image img-responsive"  --%>
								<img class="etalage_thumb_image img-responsive"
								src="<%=request.getContextPath()%>/showImage.do?gid=<%=goodsvo.getGid()%>&action=goods&size=300"
								id="imgpic0" > 
								<%--  class="etalage_source_image img-responsive"  --%>
								<img class="etalage_source_image img-responsive"
								src="<%=request.getContextPath()%>/showImage.do?gid=<%=goodsvo.getGid()%>&action=goods&size=900" >
							</li>
							<%
								int n = 0;
							%>
							<c:forEach var="goodsImagevo" items="${list }">
								<li><img class="etalage_thumb_image img-responsive"
									src="<%= request.getContextPath() %>/showImage.do?pic_number=${ goodsImagevo.pic_number }&action=goodsImage&size=300"
									id="imgpic<%= ++n %>" > <img
									class="etalage_source_image img-responsive"
									src="<%= request.getContextPath() %>/showImage.do?pic_number=${ goodsImagevo.pic_number }&action=goodsImage&size=900" >
								</li>
							</c:forEach>
						</ul> <%-- ul: etalage 會給javascript用 --%>
						<div class="clearfix"> </div>		
					</div>  <%-- grid images_3_of_2  包圖片的 --%>
					
					
					<div class="span1_of_1_des" style="margin-top:50px;"> <%--  class="span1_of_1_des" 顯示在畫面右邊的資訊  --%>
					<div class="desc1 " >  <%--   class="desc1" 顯示在畫面右邊的資訊  --%>
					<span > 
						<font
						style="font-size: 250%; width: 400px; display: inline-block;">${goodsvo.g_name}</font><br>
						<font
						style="width: 400px; font-size: 120%; display: inline-block;">${goodsvo.g_describe}</font>
						<font color="blue"
						style="width: 400px; font-size: 150%; display: inline-block;"><font
							color="black" style="width: 100px; font-size: 50%;">交換押金:</font>${goodsvo.g_price}</font>
						<font color="blue"
						style="width: 400px; font-size: 150%; display: inline-block;"><font
							color="black" style="width: 100px; font-size: 50%;">商品外觀等級:</font>${goods_level[goodsvo.g_level]}</font>
						<font color="blue"
						style="width: 400px; font-size: 150%; display: inline-block;"><font
							color="black" style="width: 100px; font-size: 50%;">上架日期:</font>${goodsvo.getJoindateFormat()}</font>
						<font color="blue"
						style="width: 400px; font-size: 150%; display: inline-block;"><font
							color="black" style="width: 100px; font-size: 50%;">下架日期:</font>${goodsvo.getQuitdateFormat()}</font>
						<font color="blue"
						style="width: 400px; font-size: 150%; display: inline-block;"><font
							color="black" style="width: 100px; font-size: 50%;">商品擁有者:</font>
							<a
							href="<%= request.getContextPath() %>/front/goods/show_all_goods.jsp?member_id=${membervo.member_id}">${membervo.mem_name}</a>
					</font> <font color="blue"
						style="width: 400px; font-size: 150%; display: inline-block;"><font
							color="black" style="width: 100px; font-size: 50%;">會員評價:</font>${membervo.credit}</font>
						<font color="blue"
						style="width: 400px; font-size: 150%; display: inline-block;"><font
							color="black" style="width: 100px; font-size: 50%;">商品收藏數:</font>${goodsvo.g_hot}</font>	

						<c:if test="${goodsvo.member_id != sessionScope.member_id}">
							<div style="width:400px; height:50px; margin-top:50px;">
							<form method="post" name="changeForm"
								action="<%=request.getContextPath()%>/front/member/member_login.jsp?url=<%=request.getServletPath()%>&gid=<%=gid%>"
								style="float: left; margin-left: 5px">
								<input type="button" value="交換" id="btn" class="btn btn-info" 
								 	   data-toggle="modal" href="<%= request.getContextPath() %>/javascript_css/goods/change.jsp?res_member_id=${goodsvo.member_id}&res_gid=${goodsvo.gid}" data-target="#change"> 
								<input type="hidden" id="url" value="<%=request.getContextPath()%>">
								<input type="hidden" id="res_gid" value="${goodsvo.gid}">
								<input type="hidden" id="res_member_id"
									value="${goodsvo.member_id}"> <input type="hidden"
									id="member_id" value="<%=session.getAttribute("member_id")%>">
							</form>
							<div id="bg" ></div>
							<div id="content" ></div>
							
							<form style="float:left; margin-left: 5px"
								method="post" action="<%= request.getContextPath() %>/front/favorite/FavoriteServlet.do">
								<input type="hidden" name="gid" value="<%= gid %>">
								<input type="hidden" name="url" value="<%=request.getServletPath()%>">
								<input type="hidden" name="action" value="addFavorite">
								<input type="submit" value="加入收藏" class="btn btn-danger">
							</form>
							<% 
							//去資料庫找此會員有沒有檢舉過此商品
							ReportVO report = null;
							if(member_id != null && member_id.trim().length() != 0){
								report = new ReportService().findByGidAndMember_id(member_id, gid);
							}%>
							<%--  如果會員沒登入將此表單送出,此表單是連到會員登入頁面,登入完成後再繞回來 --%>
							<form style="float:left; margin-left: 5px" name="reportForm"
								method="post" action="<%=request.getContextPath()%>/front/member/member_login.jsp?url=<%=request.getServletPath()%>&gid=<%=gid%>">
								<input type="hidden" name="gid" value="<%= gid %>">
								<input type="hidden" name="url" value="<%=request.getServletPath()%>">
								<input type="hidden" name="action" value="addReport">
								<%--  這邊存放該會員如果已經檢舉過這個商品,這個欄位就會有值,然後給report.js去取值,若無就空值 --%>
								<input type="hidden" name="isReport" value="<%= (report == null)? "null" : report.getMember_id()  %>" id="isReport">
								<%--  report.js 會判定 member_id 是不是null (會員有沒有登入) , 和 此會員是否已經有檢舉過商品...詳情請看report.js --%>
								<input type="button" value="檢舉商品" class="btn btn-primary" id="report_button"  data-toggle="modal" >
							</form>
							</div>
						</c:if>
										
						

					</span> 
					</div> <%--   class="desc1" 顯示在畫面右邊的資訊  --%>
					</div> <%--  class="span1_of_1_des" 顯示在畫面右邊的資訊  --%>
					<br>



				</c:if>
				<c:if test="${empty goodsvo }">
		資料不正確無法顯示
	</c:if>

			</div><%-- single_grid 先在外層用這個把要顯示的資訊包起來  --%>
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