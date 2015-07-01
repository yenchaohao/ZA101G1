<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.goods.model.*"%>
<%@ page import="com.group.model.*" %>
<%@ page import="com.member.model.*" %>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<%	
	session.setAttribute("submit", "ok"); //防止重新整理商品加入換物車
	String res_gid = request.getParameter("res_gid"); //被請求方的gid
	String res_member_id = request.getParameter("res_member_id"); //被請求方的member_id
	List<GoodsVO> list = null;
	String req_member_id = (String)request.getSession().getAttribute("member_id"); //請求方的member_id
	
	Map<String,String[]> query = new HashMap();
	query.put("member_id", new String[]{req_member_id});
	query.put("goods_status", new String[]{"0","1"}); 
	
	list = new GoodsService().getAllMapByMember(query);
	pageContext.setAttribute("list", list);
	
	MemberVO memberVO = new MemberService().getOneMemberByMemberId(res_member_id);
	pageContext.setAttribute("memberVO", memberVO);
	GoodsVO goodsVO = new GoodsService().findGoodsByGid(Integer.parseInt(res_gid));
	pageContext.setAttribute("goodsVO", goodsVO);
	
%>
<jsp:useBean id="groupService" scope="page" class="com.group.model.GroupService" />
<html>
<head>
<link
	href="<%=request.getContextPath()%>/javascript_css/front_main_css/style.css"
	rel="stylesheet" type="text/css" media="all" />
<script>
	$(document).ready(function (){
		
			$("#cart").click(function(){
				if($("#req_gid").val().length == 0)
					alert('請選擇欲交換的商品');
				else{
					$("#requestForm").attr('action',$("#url").val()+"/front/cart/cartServlet.do");
					$("#action").val("addCart");
					requestForm.submit();
				}
					
			});
			
			$("#tran").click(function(){
				if($("#req_gid").val().length == 0)
					alert('請選擇欲交換的商品');
				else {
					$("#requestForm").attr('action',$("#url").val()+"/tranRequest.do");
					$("#action").val("tranRequest");
					requestForm.submit();
				}
			});
			
			var count = $("#count").val();
			
			$("img").click(function(event){
				for(var i = 0 ; i< count ; i ++){
					$("#item"+i).css('border','0');
				}
				$("#selected").html(
						'<b>' + event.target.name
								+ "已選取" + '</b>');
				$("#"+event.target.id).css('border', "solid 2px red");   
				$("#req_gid").val(event.target.className);
			});
			
			
	});
</script>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>商品專區</title>

</head> 
<body> 

	<c:if test="${not empty errorMessage}">
		<ul>
			<c:forEach var="message" items="${errorMessage}">
				<li>${message}</li>
			</c:forEach>
		</ul>
	</c:if>
	
	<div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel"><font size="5" color="blue"><b>交換</b></font></h4>
     </div>
	
	<c:if test="${not empty list }">
	<form method="post" name="requestForm" id="requestForm">
	<h2>請選擇一項自己的商品與 <b> ${goodsVO.g_name}</b> 交換</h2>
	<%
		int count = 0;
		pageContext.setAttribute("count", count);
	%>
		<div style="margin: 20px;">
			<c:forEach var="goodsVO" items="${list}">
				<c:if test="${count % 2 == 0}">
					<div class="col-md-4 you-men" >
						<img 
							src="<%= request.getContextPath() %>/showImage.do?gid=${goodsVO.gid}&action=goods"
							style="width:150px;height:150px;" id="item<%=count%>" name="${goodsVO.g_name}" class="${goodsVO.gid}">
						<p>商品名稱:${goodsVO.g_name}</p>
						<p>商品價格:${goodsVO.g_price}</p>
					</div>
				</c:if>
				<c:if test="${count % 2 != 0}">
					<div class="col-md-4 you-men para-grid">
						<img 
							src="<%= request.getContextPath() %>/showImage.do?gid=${goodsVO.gid}&action=goods"
							style="width:150px;height:150px;" id="item<%=count%>" name="${goodsVO.g_name}" class="${goodsVO.gid}">
						<p>商品名稱:${goodsVO.g_name}</p>
						<p>商品價格:${goodsVO.g_price}</p>
					</div>
				</c:if>
					
				<%
					count++;
					pageContext.setAttribute("count", count);
				%>
				
			</c:forEach>
		</div>
			<input type="hidden" name="gid" value="<%=res_gid%>">	<!-- 被請求方gid -->
			<input type="hidden" name="res_member_id" value="<%=res_member_id%>"> <!-- 被請求方member_id -->
			<input type="hidden" name="count" id="count" value="<%= count %>">
			<input type="hidden" id="url" value="<%= request.getContextPath() %>">
			<input type="hidden" name="req_gid" id="req_gid">
			<input type="hidden" name="requestURL" value="<%=request.getServletPath()%>">
			<input type="hidden" name="action" id="action">
	</form>
	
		<div class="clearfix"></div>
		<div class="modal-footer" style="background-color:white;" >
				<div id="selected" style="color: red;">
					<b>尚未選取任何商品</b>
				</div>
			<Button id="cart" class="btn btn-primary">加入換物車</Button>
			<Button id="tran" class="btn btn-primary">直接交換</Button>
			<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
		</div>
	
	</c:if>
	<c:if test="${empty list }">
		查無任何商品
	</c:if>




	
</body>
</html>