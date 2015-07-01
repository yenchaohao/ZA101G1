<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ page import="java.util.*"%>
<%@ page import="com.member.model.*"%>
<%@ page import="com.goods.model.*"%>
<%@ page import="com.vipad.model.*"%>
<html>
<head>
</head>
<body>

				<%
					VipadService vipadSvc = new VipadService();
					List<VipadHibernateVO> vipadList = vipadSvc.getAllAliveAssociations();
					pageContext.setAttribute("vipadList", vipadList);
					//List<GoodsVO> goodsList = new GoodsService().getAll();
					//pageContext.setAttribute("goodsList", goodsList);
					List<MemberVO> memberList = new MemberService().getAll();
					pageContext.setAttribute("memberList", memberList);
				%>
		
			<c:forEach var="vipadVO" items="${vipadList}">
						<li><a href="<%= request.getContextPath() %>/front/goods/goods_detail.jsp?gid=${vipadVO.goodsVO.gid}"><img class="img-responsive women"
								src="<%= request.getContextPath() %>/showImage.do?action=goods&gid=${vipadVO.goodsVO.gid}"
								alt="" style="width: 200px; height: 200px;"></a>
							<div class="hide-in" style="text-align:center">
								<h3 style="color:blue;"><b>${vipadVO.goodsVO.g_name}</b></h3>
								<!-- 沒有使用聯合映射
								<c:forEach var="goodsVO" items="${goodsList}">
									<c:if test="${goodsVO.gid == vipadVO.gid}">
										<h3 style="color:blue;margin-left:70px;"><b>${goodsVO.g_name}</b></h3>
									</c:if>
								</c:forEach>
								-->
								<c:forEach var="memberVO" items="${memberList}">
									<c:if test="${memberVO.member_id == vipadVO.member_id}">
										<a href="<%= request.getContextPath() %>/front/goods/show_goods_for_desired.jsp?member_id=${memberVO.member_id}">
											<h4 style="color:green">${memberVO.mem_name}</h4>
										</a>
									</c:if>
								</c:forEach>
							</div></li>
					</c:forEach>
					
	
</body>
</html>