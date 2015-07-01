<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.goods.model.*"%>
<%@ page import="com.group.model.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.goodsimage.model.*"%>
<!DOCTYPE html>
<%
	
	List<GoodsVO> list =  new GoodsService().getAllAlive();
	pageContext.setAttribute("list", list);
	
	//清掉SESSION的LOCATUION 這樣登出又登入後才不會回上一個登入存的路徑
	String location = (String)session.getAttribute("location");
	if(location != null)
		session.removeAttribute("location");
	
	//取得現在的時間
	Calendar cal =  Calendar.getInstance();
	pageContext.setAttribute("cal", cal);
	
	//為了要和show_goods_for_desired.jsp 共用同一個page2.file 多寫一個 member_id 才不會跳錯
	String member_id = null;
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
<script src="<%=request.getContextPath()%>/javascript_css/front_main_js/bootstrap.min.js"></script>
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
	 <%@ include file="/front/goods/change.file"  %>	
	 <%@ include  file="/front/indexFile.file" %>
			<c:if test="${not empty errorMessage}">
				<ul>
					<c:forEach var="message" items="${errorMessage}">
						<li>${message}</li>
					</c:forEach>
				</ul>
			</c:if>

			<c:if test="${not empty list }">
				<div><%@ include file="page1.file"%>
		</div>
		<%
			int count = 0;
						pageContext.setAttribute("count", count);
		%>


		<%-- 這邊是要顯示上面快下架的商品class="product_banner"--%>
		<div class="product_banner" style="height: 300px; width: 1140; margin-left:340px;">
			<div class="in-product">
				<div class="wmuSlider example1" style="height: 452px;">
					<div class="wmuSliderWrapper">
						<%
							//取得第一個商品的附圖(快要下架的)	
													List<GoodsImageVO> imageList  = new GoodsImageService().findGoodsImageByGid(list.get(0).getGid());
													pageContext.setAttribute("imageList", imageList);
																			//取得第一個商品的主圖(如果該商品沒有附圖就用這個)			
													GoodsVO good1 = list.get(0);
													pageContext.setAttribute("good1", good1);
						%>
						<%-- 如果該商品有附圖--%>
						<c:if test="${not empty imageList }">
							<%-- 先顯示主圖在顯示附圖,總共跑的次數 (1(主圖)+1~3(附圖張數)) 次--%>
							<article style="position: absolute; width: 100%; opacity: 0;">
								<div class="banner-wrap">
									<div class="banner_left">
											<a
										href="<%= request.getContextPath() %>/front/goods/goods_detail.jsp?gid=${good1.gid}"
										id="imageLink<%= count %>">
										<img
											src="<%=request.getContextPath()%>/showImage.do?gid=<%=good1.getGid()%>&action=goods"
											class="img-responsive" alt=""
											style="width: 300px; height: 300px;"></a>
									</div>
								</div>
							</article>
							<c:forEach var="imageVO" items="${imageList }">
								<article style="position: absolute; width: 100%; opacity: 0;">
									<div class="banner-wrap">
										<div class="banner_left">
											<a
										href="<%= request.getContextPath() %>/front/goods/goods_detail.jsp?gid=${good1.gid}"
										id="imageLink<%= count %>">
											<img
												src="<%= request.getContextPath() %>/showImage.do?pic_number=${ imageVO.pic_number }&action=goodsImage"
												class="img-responsive" alt=""
												style="width: 300px; height: 300px;"></a>
										</div>
									</div>
								</article>
							</c:forEach>
						</c:if>
						<%-- 如果該商品沒有附圖--%>
						<c:if test="${empty imageList }">
							<%-- 顯示主圖就好--%>
							<article style="position: absolute; width: 100%; opacity: 0;">
								<div class="banner-wrap">
									<div class="banner_left">
										<a
										href="<%= request.getContextPath() %>/front/goods/goods_detail.jsp?gid=${good1.gid}"
										id="imageLink<%= count %>">
										<img
											src="<%=request.getContextPath()%>/showImage.do?gid=<%=good1.getGid()%>&action=goods"
											class="img-responsive" alt=""
											style="width: 250px; height: 250px;"></a>
									</div>
								</div>
							</article>
						</c:if>

					</div>
					<a class="wmuSliderPrev">Previous</a> <a class="wmuSliderNext">Next</a>
				</div>


				<script
					src="<%=request.getContextPath()%>/javascript_css/front_main_js/jquery.wmuSlider.js"></script>
				<script>
					$('.example1').wmuSlider({
						pagination : false,
					});
				</script>
			</div>
			<%-- 快下架商品的資訊--%>
			<div class="banner_right">
				<h3>
					<a
						href="<%= request.getContextPath() %>/front/goods/goods_detail.jsp?gid=${good1.gid}"
						id="imageLink<%= count %>"> ${good1.g_name} </a>
				</h3>
				<p class="nine">押金:${good1.g_price}</p>
				<p class="sit-in">${good1.g_describe}</p>
				<div id="quitdate<%=count%>"></div>
				
				<script>
					$(document).ready(function(){
						$(".btn").click(function(){
							if($("#member_id").val() == 'null'){
							login.submit();
						}
					});
				});
				</script>		
				
				<span>
					<c:if test="${good1.member_id != sessionScope.member_id}">
														<form method="post" name="login"
															action="<%=request.getContextPath()%>/front/member/member_login.jsp?url=<%= request.getServletPath() %>?whichPage=<%= whichPage %>"
															style="float: left; margin-left: 5px">
															<input type="button" value="交換"  class="btn btn-info" 
								 	 							   data-toggle="modal" href="<%= request.getContextPath() %>/javascript_css/goods/change.jsp?res_member_id=${good1.member_id}&res_gid=${good1.gid}" data-target="#change"> 
															<input type="hidden" id="url" value="<%=request.getContextPath()%>">
															<input type="hidden" id="res_gid" value="${good1.gid}">
															<input type="hidden" id="res_member_id" value="${good1.member_id}"> 
															<input type="hidden" id="member_id" value="<%=session.getAttribute("member_id")%>">
														</form>
					</c:if>
												<c:if test="${good1.member_id == sessionScope.member_id}">
												<a href="<%= request.getContextPath() %>/front/goods/GoodsServlet.do?action=update_goods&
																							gid=${good1.gid}&
																							requestURL=/front/goods/show_all_goods.jsp&
																							whichPage=<%= whichPage %>&
																							member_id=${good1.member_id}"><font color="green">修改資料</font></a>
												</c:if>
				</span>
				<input type="hidden" id="goods_time<%= count %>"
					value="${good1.quitdate.getTime() - cal.getTimeInMillis()}">
				<%-- 產生一件商品就+1--%>
				<%
					count++;
						pageContext.setAttribute("count", count);
				%>
			</div>
			<div class="clearfix" style="height: 300px; width: 1140;"></div>
		</div>
		<%-- 這邊是要顯示上面快下架的商品class="product_banner"--%>
		
		<% 
			//把第一件商品移除,因為上面已經顯示過了
			list.remove(0);
		   pageContext.setAttribute("list", list);
		%>
		
		<%-- 底下的所有商品 class="products_top"--%>
		<div class="products_top" style="margin-left:200px;">
			<div class="col-md-9 price-on">
				<div class="product_box1">

					<c:forEach var="goodsVO" items="${list}" begin="<%= pageIndex %>"
						end="<%= pageIndex + rowPerPage -1 %>">
						<c:if test="${count % 2 == 0 }">
							<div class="col-md-4 grid-4">
								<a href="<%= request.getContextPath() %>/front/goods/goods_detail.jsp?gid=${goodsVO.gid}"> 
								<img src="<%= request.getContextPath() %>/showImage.do?gid=${goodsVO.gid}&action=goods"
									class="img-responsive" alt="" style="width: 150px; height: 150px;">
								</a>
								<div class="tab_desc1">
									<h3>
										<a href="<%= request.getContextPath() %>/front/goods/goods_detail.jsp?gid=${goodsVO.gid}">${goodsVO.g_name}</a>
									</h3>
									<p>押金:${goodsVO.g_price}</p>
									<div id="quitdate<%=count%>"></div>
									<input type="hidden" id="goods_time<%= count %>"
									 value="${goodsVO.quitdate.getTime() - cal.getTimeInMillis()}">
								</div>
							</div>
						</c:if>
						<c:if test="${count % 2 != 0 }">
							<div class="col-md-4 grid-4 grid-in">
								<a href="<%= request.getContextPath() %>/front/goods/goods_detail.jsp?gid=${goodsVO.gid}"> 
								<img src="<%= request.getContextPath() %>/showImage.do?gid=${goodsVO.gid}&action=goods"
									class="img-responsive" alt="" style="width: 150px; height: 150px;">
								</a>
								<div class="tab_desc1">
									<h3>
										<a href="<%= request.getContextPath() %>/front/goods/goods_detail.jsp?gid=${goodsVO.gid}">${goodsVO.g_name}</a>
									</h3>
									<p>押金:${goodsVO.g_price}</p>
									<div id="quitdate<%=count%>"></div>
									<input type="hidden" id="goods_time<%= count %>"
									 value="${goodsVO.quitdate.getTime() - cal.getTimeInMillis()}">
								</div>
							</div>
						</c:if>
						
						<%
							count++;
							pageContext.setAttribute("count", count);
						%>
					
					
					</c:forEach>

				</div>

			</div>

		</div>





		<input type="hidden" id="count" value="<%=count%>">
		<%@ include file="page2.file"%>
		</c:if>
		<c:if test="${empty list }">
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