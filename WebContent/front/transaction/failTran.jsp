<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="BIG5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
 <%@ page import="com.tran.model.*"%>
  <%@ page import="com.member.model.*"%>
   <%@ page import="com.goods.model.*"%>
  <%@ page import="com.send.model.*"%>     
 <%@ page import="java.util.*"%>
 <%
 		String member_id= (String) session.getAttribute("member_id");
		//取得服務
 		TranService transSvc=new TranService();
 		GoodsService goodsSvc=new GoodsService();
 		MemberService memSvc=new MemberService();
 		//取得交易請求清單
 		List <TranVO> fail_list =transSvc.getAllByMember_idFail(member_id);
 		pageContext.setAttribute("fail_list",fail_list);
//  		List <TranVO> res_list =transSvc.getOneByResMember_id(member_id);
//  		pageContext.setAttribute("res_list",res_list);
 		
 	
 			
 %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>失敗的交易</title>
<link href="<%= request.getContextPath() %>/javascript_css/front_main_css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="<%= request.getContextPath() %>/javascript_css/front_main_js/jquery.min.js"></script>
<script src="<%= request.getContextPath() %>/javascript_css/front_main_js/bootstrap.min.js"></script>
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

</head>
<body>
 <%@ include  file="/front/memberFile.file" %>
<!-- ------------------------------------------------main -->
<!-- -------------------------------------------------發出的請求 -->
<div id="contain" style="height:610px;">
<ul class="pagination pagination-lg">
<li ><a href="<%= request.getContextPath() %>/front/transaction/transactionCenter.jsp">送出的請求</a></li>
<li><a href="<%= request.getContextPath() %>/front/transaction/receive.jsp"">收到的請求</a></li>
<li ><a href="<%= request.getContextPath() %>/front/transaction/successTran.jsp"">成功的交易</a></li>
<li class="active"><a href="<%= request.getContextPath() %>/front/transaction/failTran.jsp"">失敗的交易</a></li>
</ul>
<c:choose>
	<c:when test="${not empty fail_list}">
		
		<table border="1">

<!-- 		錯誤列表 -->
		<% if(request.getAttribute("errorMsgs")!=null){ 
				List<String> errList=(List<String>)request.getAttribute("errorMsgs");
				if(!errList.isEmpty()){
		%>
				<div class="alert alert-danger alert-dismissable">
				   <button type="button" class="close" data-dismiss="alert" 
				      aria-hidden="true">
				      &times;
				   </button>
				 	點數不足,請儲值
				</div>
				<% } %>
		<% } %>
		
		<%
		//取得所有Req商品名稱
		ArrayList<Integer> req_goodGids=new ArrayList<Integer>();
		for(TranVO tranVO:fail_list){
			if(!req_goodGids.contains(tranVO.getReq_gid())){
				req_goodGids.add(tranVO.getReq_gid());
			}
		}
				
		%>
		
		<tr>
			<td>我的物品</td><td>押金</td><td>對方的物品</td><td>押金</td><td>物品擁有者</td><td>請求時間</td><td>對方回覆時間</td><td>交易狀態</td>
		</tr>
		
		<% 	
			
					for(TranVO tranVO:fail_list){ 
					GoodsVO req_goodsVO=goodsSvc.findGoodsByGid(tranVO.getReq_gid());
					GoodsVO res_goodsVO=goodsSvc.findGoodsByGid(tranVO.getRes_gid());
					MemberVO res_memVO=memSvc.getOneMemberByMemberId(tranVO.getRes_member_id());
					MemberVO req_memVO=memSvc.getOneMemberByMemberId(tranVO.getReq_member_id());
									
		%>		
		<tr>
				<!-- 判斷目前登入的會員是請求方還是回應方 -->
			<% if(req_goodsVO.getMember_id().equals(member_id)){ %>
			<td><a href="<%= request.getContextPath() %>/front/goods/goods_detail.jsp?gid=<%=req_goodsVO.getGid() %>"><%= req_goodsVO.getG_name() %></a></td>
			<td><%= req_goodsVO.getG_price() %></td>
			<td><a href="<%= request.getContextPath() %>/front/goods/goods_detail.jsp?gid=<%=res_goodsVO.getGid() %>"><%= res_goodsVO.getG_name() %></a></td>	
			<td><%= res_goodsVO.getG_price() %></td>	
			<td><%= res_memVO.getMem_name() %></td>
			<td><%= tranVO.getReq_date_str() %></td>
			<% }else{  %>
				<td><a href="<%= request.getContextPath() %>/front/goods/goods_detail.jsp?gid=<%=res_goodsVO.getGid() %>"><%= res_goodsVO.getG_name() %></a></td>
				<td><%= res_goodsVO.getG_price() %></td>
				<td><a href="<%= request.getContextPath() %>/front/goods/goods_detail.jsp?gid=<%=req_goodsVO.getGid() %>"><%= req_goodsVO.getG_name() %></a></td>	
				<td><%= req_goodsVO.getG_price() %></td>	
				<td><%= req_memVO.getMem_name() %></td>
				<td><%= tranVO.getReq_date_str() %></td>
			<%  } %>
			<%
				if(tranVO.getRes_date_str()==null){
			%>
			尚未回覆
			<% }else{ %>
			<td><%= tranVO.getRes_date_str() %></td>
			<% } %> 
		</td>
			<td>
				<%= ((Map)application.getAttribute("tran_status")).get(tranVO.getStatus()) %>
			</td>
		</tr>

				<% } %>

		</table>
	</c:when>
	<c:otherwise>
		<h1>沒有失敗的交易</h1>
	</c:otherwise>
</c:choose>
</div>
<!-- ----------------------------------------------end----------------------------------------------------- -->
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
					<a href="../index.html"><img src="<%= request.getContextPath() %>/images/exlogo.png" alt=" " /></a>
				</div> 
				<div class="clearfix"> </div>
			 	</div>
		 </div>
	</div>

</body>
</html>