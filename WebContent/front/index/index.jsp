<%@page import="com.vipad.model.VipadService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.message.model.*"%>
<%@ page import="com.member.model.*"%>
<%@ page import="com.goods.model.*"%>
<%@ page import="com.vipad.model.*"%>
<%@ page import="com.post.model.*"%>
<%@ page import="java.util.*"%>
<%
	String location = (String) session.getAttribute("location");
	if (location != null)
		session.removeAttribute("location");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>	 
<link
	href="<%=request.getContextPath()%>/javascript_css/front_main_css/bootstrap.css"
	rel="stylesheet" type="text/css" media="all" />
<script
	src="<%=request.getContextPath()%>/javascript_css/front_main_js/jquery.min.js"></script>
<!--商品倒數計時器寫這裡,不能寫ajax 會造成延遲-->
<script
	src="<%=request.getContextPath()%>/javascript_css/goods/countTime.js"></script>
<script src="<%=request.getContextPath()%>/javascript_css/front_main_js/bootstrap.min.js"></script>	
<link
	href="<%=request.getContextPath()%>/javascript_css/front_main_css/style.css" rel="stylesheet" type="text/css" media="all" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } </script>
<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700,800' rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Libre+Baskerville:400,700,400italic' rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Oswald:400,700,300' rel='stylesheet' type='text/css'>
<title>exChange</title>
</head>
<body>
	

	<%@ include file="/front/indexFile.file"%>     

	<div class="women-in">
		<div class="col-md-9 col-d">
			<!--主頁面-->

			<div class="banner">
				<div class="banner-matter">
					<label>二手交換網</label>
					<h2>看見</h2>
					<p>
						<b><font color="green">綠色的未來</font></b>
					</p>

				</div>
				<!--<div class="you">
							<span>40<label>%</label></span>
							<small>off</small>
						</div>		-->
				<p class="para-in">Some text regarding the featured product.</p>
			</div>

			<div class="in-line">
				<div class="para-an">
					<h3>本日精選</h3>
					<p>點擊商品查看更多訊息</p> 
				</div>

				<!---------------------------動態顯示商品的區域----------------------------------------------------------------------------------------------------------------------------------------------------------------------->
				<!--讓script 抓到路徑-->
				<input type="hidden" id="url" value="<%=request.getContextPath()%>">
				<script>
				$(document).ready(
									function() {
										//ready 後直接顯示一次
										$.ajax({
												type : "POST",
												url : $("#url").val()+"/front/index/goodsFeedBack.jsp",
												dataType : "text",
												success : function(data) {
													$("#goodsFeedBack").html(decodeURI(data))
													}
												});

										//每1秒就去做一次下面的動作這樣才不會miss掉
										setInterval(
												
												function() {
													/* 呼叫這支 getIsAddContext.jsp  去查看看servletContext 的 參數是多少 
													   "isAddFavorite" 然後把值塞在$("#isAddFavorite")裡面  */ 
													$.ajax({
														type : "POST",
														url : $("#url").val()+ "/front/index/getIsAddContext.jsp",
														dataType : "text",
														success : function(data) {
															$("#isAddFavorite").val(decodeURI(data))
														}
													});
													//如果值是 1 代表使用者有去變更收藏(新增或刪除) 在: Favorite.java ,Controller.作變更
													//有變更收藏的話就去連資料庫看看商品的順序需不需要變更(goodsFeedBack.jsp)
													if($("#isAddFavorite").val() == 1){
													
														$.ajax({
																type : "POST",
																url : $("#url").val()+ "/front/index/goodsFeedBack.jsp",
																dataType : "text",
																success : function(data) {
																	$("#goodsFeedBack").html(decodeURI(data))
																}
															});
													//查詢完了之後,再呼叫(setIsAddContext0.jsp)這之幫你把context變回0,並且把值塞到hidden;
														$.ajax({
															type : "POST",
															url : $("#url").val()+ "/front/index/setIsAddContext0.jsp",
															dataType : "text",
															success : function(data) {
																$("#isAddFavorite").val(0)
															}
														});
													}
												}

												//每1秒就去做一次上述的動作這樣才不會miss掉
												, 1000);
									});
				</script>
			
				
				<!--ajax顯示出來的結果會放在-- div class="lady-in" id="goodsFeedBack" 裡面 -->
				<div class="lady-in" id="goodsFeedBack"></div>
				<input type="hidden" id="isAddFavorite" >
			
	<!------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------->



			</div>
		</div>


		<div class="col-md-3 col-m-left">
			<!--最新公告-->
			<!---------------------------------------------POST---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------->
			<script>
				$(document).ready(
						function() {

							$.ajax({
								type : "POST",
								url : $("#url").val()
										+ "/front/index/postFeedBack.jsp",
								dataType : "text",
								success : function(data) {
									$("#postResponse").html(decodeURI(data))
								}
							});

							//之後抓更新的資料用 setInterval 去顯示每10秒跟jsp要一次資料 
							setInterval(function() {
								$.ajax({
									type : "POST",
									url : $("#url").val()
											+ "/front/index/postFeedBack.jsp",
									dataType : "text",
									success : function(data) {
										$("#postResponse")
												.html(decodeURI(data))
									}
								});
							}

							, 10000);
						});
			</script>
			<div id="postResponse"></div>

			<!------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------->
			<!---------------------------------------------Message---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------->
			<script>
				$(document)
						.ready(
								function() {

									$
											.ajax({
												type : "POST",
												url : $("#url").val()
														+ "/front/index/messageFeedBack.jsp",
												dataType : "text",
												success : function(data) {
													$("#messageResponse").html(
															decodeURI(data))
												}
											});

									//之後抓更新的資料用 setInterval 去顯示每10秒跟jsp要一次資料 
									setInterval(
											function() {
												$
														.ajax({
															type : "POST",
															url : $("#url")
																	.val()
																	+ "/front/index/messageFeedBack.jsp",
															dataType : "text",
															success : function(
																	data) {
																$(
																		"#messageResponse")
																		.html(
																				decodeURI(data))
															}
														});
											}

											, 10000);
								});
			</script>
			<div id="messageResponse"></div>

			<!------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------->
		</div>
		<div class="clearfix"></div>
	</div>

	<!--------------------------------------------------vip--------------------------------------------------------------------------------------------------------------------------->

	<div class="buy-an">
		<h3>強力推薦</h3>
		<p>點擊商品查看更多訊息.</p>
	</div>
	<!-- VIP 的最外層主框架: <div id="vipParent">  -->
	<div id="vipParent" style="width: 1200px; height: 350px;">
		
		<!--<div class="lady-in-on"> 是主要樣式,在更新內容的時候會整個移除然後重建  -->
		<div class="lady-in-on" id="vipArea">
			
			<!--<ul id="flexiselDemo1"> 生成廣告主要內容的元件   -->
			<ul id="flexiselDemo1"></ul>
			
			<script>
			//	用來記錄當前廣告內容的元件
			var vipData;
				
			$(document).ready(function() {
				
				//頁面第一次載入生成廣告內容
				$.ajax({
						type : "POST",
						url : $("#url").val() + "/front/index/vipResponse.jsp",
						dataType : "text",
						success : function(data) {
							vipData = data;
							//將vipResponse.jsp 的內容放到  <ul id="flexiselDemo1"></ul> 裡面
							$("#flexiselDemo1").html(decodeURI(data));
							// 呼叫flex(); 方法  啟動script 插件 flexisel 將廣告內容(<ul id="flexiselDemo1"></ul>)動態化
							flex();
						}
					});
				});
				
				//每5秒做一次的排程
				setInterval(function() {
					$.ajax({
						type : "POST",
						url : $("#url").val() + "/front/index/vipResponse.jsp",
						dataType : "text",
						success : function(data) {
							
							//如果當前的內容不等於AJAX拉回來的最新內容 就進入if 裡面開始更新
							if (vipData != data) {
								//alert('不等於,開始更新');
								//先把<div class="lady-in-on">框架移除  (為了把 script 插件 flexisel 一併更新移除所以要做此動作)
								$("#vipArea").remove();
								
								//取得最外層框架的元件
								var vipParent = document
										.getElementById("vipParent");
								
								//重新建構<div class="lady-in-on">框架
								var node = document.createElement('div');
								
								node.class = "lady-in-on";
								node.id = "vipArea";
								
								//把建構好的<div class="lady-in-on">框架 放到 外層框架內("vipParent")
								vipParent.appendChild(node);
								
								//取得建構好的 <div class="lady-in-on">框架
								var vipArea = document
										.getElementById("vipArea");
								//重新建構 <ul id="flexiselDemo1"></ul> 框架
								var flexise = document.createElement('ul');
								
								flexise.id = "flexiselDemo1";
								
								//把建構好的 <ul id="flexiselDemo1"></ul> 框架 放到<div class="lady-in-on">內
								vipArea.appendChild(flexise);
								
								$("#flexiselDemo1").html(decodeURI(data));
								flex();
								vipData = data;
							}else{
								//alert('等於,不更新');
							}
						}

					});
				}

				, 2000);

				function flex() {
					
							$.ajax({
								type : "GET",
								url : $("#url").val()
										+ "/javascript_css/front_main_js/jquery.flexisel.js",
								dataType : "script",
								success : function() {

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

								}
							});
				}
			</script>






		</div>
	</div>
	<!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------->


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