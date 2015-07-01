<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="BIG5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*" %>
<%@ page import="com.tran.model.*" %>
<%@ page import="com.member.model.*" %>
<%@ page import="com.cart.controller.*" %>
<%@ page import="com.rank.*" %>
<jsp:useBean id="tranSvcc" scope="page" class="com.tran.model.TranService"/>
<jsp:useBean id="memberSvc" scope="page" class="com.member.model.MemberService"/>
<%
	List<TranVO> transList = tranSvcc.getAllAlive();
	List<MemberVO> memberList = memberSvc.getAllAlive();
	//�ؤ@�ӱƦ�VO
	RankVO rankVO = null;
	//new�@��Queue�h��@Compareable������compareTo()���Ƨǥ\��A�A�h��������q�榨�ߪ��|��
	PriorityQueue<RankVO> rankList = new PriorityQueue();
	boolean isHave = false;
	//new�@��map�h�p��ӷ|���@���X������q�榨��
//	Map map = new HashMap();
	for(MemberVO memberVO : memberList){
		int count = 0;//�p�������ߦ���
		for(TranVO tranVO : transList){
			//�ӷ|���P����q�榨�ߪ�member_id�۲ŦX
			if(memberVO.getMember_id().equals(tranVO.getReq_member_id()) || memberVO.getMember_id().equals(tranVO.getRes_member_id())){
				count++;
				//�H�ӷ|��member_id��key
//				map.put(memberVO.getMember_id(), count);
				isHave = true;
			}
		}
		if(isHave){
			isHave = false;
			//�C�[�J�@�ӤH�A�Nnew�@�ӷs��
			rankVO = new RankVO(memberVO, count);
			//�⦳����q�槹�����|���[�J��list��
			rankList.offer(rankVO);
		}
	}	
	pageContext.setAttribute("rankList", rankList);
	pageContext.setAttribute("memberList", memberList);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link
	href="<%=request.getContextPath()%>/javascript_css/front_main_css/bootstrap.css"
	rel="stylesheet" type="text/css" media="all" />
<script
	src="<%=request.getContextPath()%>/javascript_css/front_main_js/jquery.min.js"></script>
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
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
</head>
<body>

	<%@ include file="/front/indexFile.file"%> 
	
	<table border="1" style="width:400px;z-index:1;position:absolute;margin-left:400px">
		<tr>
			<th style="text-align:center">�洫���Ʀ�]�e10�W</th>
		</tr>
		<%
			int count = 1;
			pageContext.setAttribute("count", count);
		%>
		<c:forEach var="rankVO" items="${rankList}">
			<c:if test="${count <= 10}">
			<tr>
				<td>
					<h3 style="margin:auto"><%=count++%>.
					<a href="<%= request.getContextPath() %>/front/goods/show_goods_for_desired.jsp?member_id=${rankVO.memberVO.member_id}">${rankVO.memberVO.mem_name}</a>
					<span style="float:right">�洫����:${rankVO.count}</span></h3>
				</td>
			</tr>
			</c:if>
			<%
				pageContext.setAttribute("count", count);
			%>
		</c:forEach>
	</table>
	<table border="1" style="width:400px;" >
		<tr>
			<th style="text-align:center">�����Ʀ�]�e10�W</th>
		</tr>
		<%
			int cunt = 1;
			pageContext.setAttribute("cunt", cunt);
		%>
			<c:forEach var="memberVO" items="${memberList}">
				<c:if test="${cunt <= 10}">
				<tr id="cunt<%=cunt%>">
					<td>
						<h3 style="margin:auto"><%=cunt++%>.
						<a href="<%= request.getContextPath() %>/front/goods/show_goods_for_desired.jsp?member_id=${memberVO.member_id}">${memberVO.mem_name}</a>
						<span style="float:right">��������:${memberVO.credit}</span></h3>
					</td>
				</tr>
				</c:if>
				<%
					pageContext.setAttribute("cunt", cunt);
				%>
			</c:forEach>
	</table>
	<!-- ------------------------------------------------------------------------------------------------------------------------------------------------------ -->
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
							class="scroll">���� </a><label>|</label></li>
						<li><a
							href="<%=request.getContextPath()%>/front/goods/show_all_goods.jsp"
							class="scroll">�ӫ~�M��</a><label>|</label></li>
						<li><a
							href="<%=request.getContextPath()%>/front/member/desired.jsp"
							class="scroll">�\�@��</a><label>|</label></li>
						<li><a
							href="<%=request.getContextPath()%>/front/message/listAllMessageTitle.jsp"
							class="scroll">�Q�װ�</a></li>
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