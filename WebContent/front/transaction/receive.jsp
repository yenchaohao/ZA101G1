<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="BIG5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
 <%@ page import="com.tran.model.*"%>
  <%@ page import="com.member.model.*"%>
   <%@ page import="com.goods.model.*"%>
    <%@ page import="com.send.model.*"%>  
 <%@ page import="java.util.*"%>
 <%
 		String member_id= (String) session.getAttribute("member_id");
		//���o�A��
 		TranService transSvc=new TranService();
 		GoodsService goodsSvc=new GoodsService();
 		MemberService memSvc=new MemberService();
 		//���o����ШD�M��
//  		List <TranVO> req_list =transSvc.getOneByReqMember_id(member_id);
//  		pageContext.setAttribute("req_list",req_list);
 		List <TranVO> res_list =transSvc.getOneByResMember_id(member_id);
 		pageContext.setAttribute("res_list",res_list);
 		//�L�o��������\�M����
 				Iterator it=res_list.iterator();
 				while(it.hasNext()){
 					TranVO tranVO=(TranVO)it.next();
 					if(tranVO.getStatus().equals(1) || tranVO.getStatus().equals(2))
 						it.remove();
 				}
 		
 	
 			
 %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>���쪺�ШD</title>
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
 <%@ include  file="/front/transaction/tranModal.file" %>
<!-- ------------------------------------------------main -->
<!-- --------------------------------------------���쪺�ШD -->
<div id="contain" style="height:610px;">
<ul class="pagination pagination-lg">
<li><a href="<%= request.getContextPath() %>/front/transaction/transactionCenter.jsp">�e�X���ШD</a></li>
<li class="active"><a href="<%= request.getContextPath() %>/front/transaction/receive.jsp">���쪺�ШD</a></li>
<li><a href="<%= request.getContextPath() %>/front/transaction/successTran.jsp"">���\�����</a></li>
<li><a href="<%= request.getContextPath() %>/front/transaction/failTran.jsp"">���Ѫ����</a></li>
</ul>
<c:choose>
	<c:when test="${not empty res_list}">
		
		<table border="1">
		
		<% if(request.getAttribute("errorMsgs")!=null){ 
				List<String> errList=(List<String>)request.getAttribute("errorMsgs");
				if(!errList.isEmpty()){
		%>
				<div class="alert alert-danger alert-dismissable">
				   <button type="button" class="close" data-dismiss="alert" 
				      aria-hidden="true">
				      &times;
				   </button>
				 	�I�Ƥ���,���x��
				</div>
				<% } %>
		<% } %>
		<%		
				//���o�Ҧ�Res�ӫ~�W��
				ArrayList<Integer> res_goodGids=new ArrayList<Integer>();
				for(TranVO tranVO:res_list){
					if(!res_goodGids.contains(tranVO.getRes_gid())){
						res_goodGids.add(tranVO.getRes_gid());
					}
				}
				
				
		%>
		
		<tr>
			<td>��誺���~</td><td>���~����</td><td>���~�֦���</td><td>�ڪ����~</td><td>���~����</td><td>�ШD�ɶ�</td><td>������A</td><td>�Q�n</td><td>���Q�n</td>
		</tr>
		
		<% 	
		//���h�ְӫ~ �N���h�֤���
		for(int i=0;i<res_goodGids.size();i++){
					for(TranVO tranVO:res_list){ 
					GoodsVO req_goodsVO=goodsSvc.findGoodsByGid(tranVO.getReq_gid());
					GoodsVO res_goodsVO=goodsSvc.findGoodsByGid(tranVO.getRes_gid());
					MemberVO req_memVO=memSvc.getOneMemberByMemberId(tranVO.getReq_member_id());
					//���O���O����D�P�@��req�ӫ~�A�O���ܴN��ܡA��ܥ��^�Ъ��ӫ~
					if(res_goodGids.get(i).equals(res_goodsVO.getGid()) ){		
									
		%>		
		<tr>
			<td><a href="<%= request.getContextPath() %>/front/goods/goods_detail.jsp?gid=<%=req_goodsVO.getGid() %>"><%= req_goodsVO.getG_name() %></a></td>
			<td><%= req_goodsVO.getG_price() %></td>
			<td><%= req_memVO.getMem_name() %></td>	
			<td><a href="<%= request.getContextPath() %>/front/goods/goods_detail.jsp?gid=<%=res_goodsVO.getGid() %>"><%=  res_goodsVO.getG_name() %></a></td>
			<td><%=  res_goodsVO.getG_price() %></td>
			<td><%= tranVO.getReq_date_str() %></td>
			<td>
			<% if (tranVO.getStatus().equals(3)){ %>
				<a href="<%= request.getContextPath() %>/front/transaction/tranModal.jsp?tid=<%= tranVO.getTid()%>&source=<%=request.getServletPath()%>" data-toggle="modal"  data-target="#lock">
				<%= ((Map)application.getAttribute("tran_status")).get(tranVO.getStatus()) %>
				</a>
				<% }else{ %>
				<%= ((Map)application.getAttribute("tran_status")).get(tranVO.getStatus()) %>
				<% } %>
			</td>
			<td>
			<% if (!tranVO.getStatus().equals(3)){ %>
			
				<form action="<%= request.getContextPath() %>/front/transaction/tran.do"" method="post">
					<input type="submit" value="�ڭn���o��">
					<input type="hidden" name="action" value="check_tran">
					<input type="hidden" name="member_id" value="<%= member_id%>">
					<input type="hidden" name="tid" value="<%= tranVO.getTid()%>">				
				</form>
			
			<% }else{ %>
				<font color="red"><b>�����w��</b></font> 
			<% } %>
			</td>
			<td>
				<form action="<%= request.getContextPath() %>/front/transaction/tran.do"" method="post">
					<input type="submit" value="�ڤ��Q�n�o��">
					<input type="hidden" name="action" value="del_tran">
					<input type="hidden" name="tid" value="<%= tranVO.getTid()%>">				
					<input type="hidden" name="source" value="<%= request.getServletPath()%>">				
				</form>
			</td>
		</tr>
			<% } %>			
		  <% } %>
		<% } %>
		</table>
		
		
	</c:when>
	<c:otherwise>
		<h1>�S�����쪺�ШD</h1>
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
						<li ><a href="<%= request.getContextPath() %>/front/index/index.jsp" class="scroll">���� </a><label>|</label></li>
						<li><a href="<%= request.getContextPath() %>/front/goods/show_all_goods.jsp" class="scroll">�ӫ~�M��</a><label>|</label></li>
						<li><a href="<%= request.getContextPath() %>/front/member/desired.jsp" class="scroll">�\�@��</a><label>|</label></li>
						<li><a href="<%= request.getContextPath() %>/front/message/listAllMessageTitle.jsp" class="scroll">�Q�װ�</a></li>
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