<%@page import="com.goods.model.GoodsService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="BIG5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.goods.model.*" %>
<%@ page import="com.vipad.model.*" %>
<%@ page import="java.util.*" %>
<%
// 	String member_id = (String)session.getAttribute("member_id");
// 	GoodsService goodsSvc = new GoodsService();
// 	List<GoodsVO> list = goodsSvc.findGoodsByMember_idAlive(member_id);
// 	pageContext.setAttribute("list", list);
%>
<jsp:useBean id="goodsSvc" scope="page" class="com.goods.model.GoodsService"/>
<jsp:useBean id="vipadSvc" scope="page" class="com.vipad.model.VipadService" />
<jsp:useBean id="groupSvc" scope="page" class="com.group.model.GroupService"/>
<%
	List<GoodsVO> goodsList = goodsSvc.findByMember_idAllAlive((String)session.getAttribute("member_id"));
	List<VipadVO> vipadList = vipadSvc.getVipadByMember((String)session.getAttribute("member_id"));
	//new�@�ӷs��List�h��goodsList��GoodsVO����
	List<GoodsVO> list = new ArrayList<GoodsVO>();
	if(vipadList == null){
		pageContext.setAttribute("goodsList", goodsList);
	} else {
		for(GoodsVO goodsVO : goodsList){
			for(VipadVO vipadVO : vipadList){
				//�ӫ~���PVIP�s�i�ӫ~�ۦP�K�[�J��list��
				if(goodsVO.getGid().equals(vipadVO.getGid())){
					list.add(goodsVO);
				}
			}
		}
		//���pgoodsList����list�̭����ۦP����A�åB���R��
		goodsList.removeAll(list);
		pageContext.setAttribute("goodsList", goodsList);
	}
%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="<%= request.getContextPath() %>/javascript_css/front_main_css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="<%= request.getContextPath() %>/javascript_css/front_main_js/jquery.min.js"></script>
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
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
</head>
<body>
<%@ include  file="/front/memberFile.file" %>
<!------------------------------------------------------------------------------- code -->
	<c:if test="${not empty errorMessage }">
		<ul>
			<c:forEach var="message" items="${errorMessage }">
				<li>${message}</li>
			</c:forEach>
		</ul>
	</c:if>
	<c:if test="${not empty goodsSvc.findGoodsByMember_id(sessionScope.member_id)}">
	<div><%@ include file="page1.file"%></div>
	<div class="sale_grid">
	<form method="post" action="<%=request.getContextPath()%>/front/vipad/vipadServlet.do">
			<c:forEach var="goodsVO" items="${goodsList}" begin="<%= pageIndex %>"
							end="<%= pageIndex + rowPerPage -1 %>">
				<c:if test="${empty vipadSvc.getVipadByMember(sessionScope.member_id)}">
					<%@ include file="addVipad.file"%>
				</c:if>
				<%
					int count = 0; //�ǰt����
				%>
				<c:forEach var="vipadVO" items="${vipadSvc.getVipadByMember(sessionScope.member_id)}">
					<c:choose>
						<c:when test="${vipadVO.gid == goodsVO.gid }">
							<%-- �s�i�Ƶ{���w�����ӫ~ --%>
						</c:when>
						<c:otherwise>
							<% 
								count++;
								pageContext.setAttribute("count", count);
							%>
							<!-- �ǰt����(count)�Pvip�s�i���ײŦX�A���ӫ~�|���[�J�s�i�Ƶ{ -->
							<c:if test="${vipadVO.gid != goodsVO.gid && count == vipadSvc.getVipadByMember(sessionScope.member_id).size()}">
								<%@ include file="addVipad.file"%>
							</c:if>
						</c:otherwise>
					</c:choose>
					
				</c:forEach>
			</c:forEach>
		
		<%@ include file="page2.file"%>
		<input type="submit" value="�T�{�s�W�s�i" style="float:right;margin-right:280px;" class="btn btn-info">
		<input type="hidden" name="action" value="addVipad">
	</form>
	</div>
	</c:if>
	<c:if test="${empty goodsSvc.findGoodsByMember_id(sessionScope.member_id)}">
		�d�L����ӫ~�A�Ш�ӫ~�޲z�W�ǰӫ~�C
	</c:if>
	<!-------------------------------------------------------------- end -->	
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
					<a href="<%=request.getContextPath()%>/front/index/index.jsp"><img src="<%= request.getContextPath() %>/images/exlogo.png" alt=" " /></a>
				</div> 
				<div class="clearfix"> </div>
			 	</div>
		 </div>
	</div>
</body>
</html>