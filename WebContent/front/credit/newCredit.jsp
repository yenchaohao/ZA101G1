<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="BIG5"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
 <%@ page import="com.member.model.*"%>
 <%@ page import="com.credit.model.*"%>
 <%@ page import="com.tran.model.*"%>
 <%@ page import="com.goods.model.*"%>
 <%@ page import="java.util.*"%>
 <%
 			String member_id=(String)session.getAttribute("member_id");
 			String given_member_id=(String)request.getAttribute("given_member_id");
 			Integer pendPoint=(Integer)request.getAttribute("pendPoint");
 			CreditService creSvc=new CreditService();
 			List<CreditVO>list=creSvc.getAll();
 			List<CreditVO>listA=new ArrayList<CreditVO>();//A未完成
 			List<CreditVO>listB=new ArrayList<CreditVO>();//B未完成
 			//把已經評價完成的去掉  
 			Iterator iterator=list.iterator();
 			while(iterator.hasNext()){
 				CreditVO creVO=(CreditVO)iterator.next();
 				//AB都沒有
 				if(!(creVO.getMemberA_id().equals(member_id))&&!(creVO.getMemberB_id().equals(member_id))){
 					iterator.remove();
 				}
 				//A有未評價
 				else if((creVO.getMemberA_id().equals(member_id))&&((creVO.getStatus().equals(0))||(creVO.getStatus().equals(2)))){
 				
 						listA.add(creVO);
 						iterator.remove();
 				}
				//B有未評價
				else if((creVO.getMemberB_id().equals(member_id))&&((creVO.getStatus().equals(0))||(creVO.getStatus().equals(1)))){
 	 				
						listB.add(creVO);
						iterator.remove();
				}		
 			}
 		//System.out.println("listA.size()="+listA.size());
 		//System.out.println("listB.size()="+listB.size());
 		//System.out.println("list.size()="+list.size());
 
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
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
<!-- 		------------------------------------------main -->
<% if(given_member_id!=null){ 
	MemberVO givenVO=new MemberService().getOneMemberByMemberId(given_member_id);

%>
<div class="alert alert-success alert-dismissable">
   <button type="button" class="close" data-dismiss="alert" 
      aria-hidden="true">
      &times;
   </button>
 	已經成功對<%=givenVO.getMem_name() %>評價，並歸還<%= pendPoint %>點給您
</div>
<% } %>
<% if (!(listA.size()==0) || !(listB.size()==0)){ %>
	<table>
	<th>評價列表</th>
	<tr><td>對方</td><td>物品</td><td>評價去</td></tr>
	<% for(CreditVO creVO :listA){ 
			MemberVO memB=new MemberService().getOneMemberByMemberId(creVO.getMemberB_id());
			TranVO tranVO=new TranService().getOneTran(creVO.getTid());	
	%>
	<tr>
		<td><%= memB.getMem_name() %></td>
		<td><%= (tranVO.getReq_member_id().equals(member_id))?
				new GoodsService().findGoodsByGid(tranVO.getRes_gid()).getG_name():
				new GoodsService().findGoodsByGid(tranVO.getReq_gid()).getG_name() %></td>
		<td>
			<form  action="<%= request.getContextPath() %>/front/credit/credit.do" method="post">
				<input type="submit" value="評價">
				<input type="hidden" name="action" value="gotoCredit">
				<input type="hidden" name="member_id" value="<%= member_id%>">		
				<input type="hidden" name="tid" value="<%= creVO.getTid() %>">
				<input type="hidden" name="cid" value="<%= creVO.getCid() %>">
			</form>
		</td>
	</tr>
	<% } %>
	<% for(CreditVO creVO :listB){ 
		MemberVO memA=new MemberService().getOneMemberByMemberId(creVO.getMemberA_id());
		TranVO tranVO=new TranService().getOneTran(creVO.getTid());
	%>
	<tr>
		<td><%= memA.getMem_name() %></td>
		<td><%= (tranVO.getReq_member_id().equals(member_id))?
				new GoodsService().findGoodsByGid(tranVO.getRes_gid()).getG_name():
				new GoodsService().findGoodsByGid(tranVO.getReq_gid()).getG_name() %></td>
		<td>
			<form action="<%= request.getContextPath() %>/front/credit/credit.do" method="post">
				<input type="submit" value="評價" class="btn btn-info">
				<input type="hidden" name="action" value="gotoCredit">
				<input type="hidden" name="member_id" value="<%= member_id%>">				
				<input type="hidden" name="tid" value="<%= creVO.getTid() %>">
				<input type="hidden" name="cid" value="<%= creVO.getCid() %>">
			</form>
		</td>
	</tr>
	<% } %>
	</table>
<% } else{ %>
		<div class="alert alert-info">目前沒有評價</div>
<% } %>
<!-- -----------------------------main end -->
</div>
	</div>
	<!---->
	<div class="footer" style="margin-top:500px;">
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