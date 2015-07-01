<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ page import="java.util.*"%>
<%@ page import="com.member.model.*"%>
<%@ page import="com.goods.model.*"%>
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
<script src="<%=request.getContextPath()%>/javascript_css/goods/report.js"></script>
<script src="<%=request.getContextPath()%>/javascript_css/front_main_js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/javascript_css/front_main_js/jquery.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/javascript_css/front_main_css/etalage.css">   <%--  ul: etalage 會給javascript用 --%>
<script src="<%=request.getContextPath()%>/javascript_css/front_main_js/jquery.etalage.min.js"></script> <%--  ul: etalage 會給javascript用 --%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script>
$(document).ready(function(){
	
	$(".btn").click(function(){
	if($("#member_id").val() == 'null'){
		login0.submit();
	}
});
});
</script>

</head>
<body>
	<%@ include file="/front/goods/change.file"  %>
			<% 	List<GoodsVO> goodslist = new GoodsService().getAllOrderByG_hot();
				pageContext.setAttribute("goodlist", goodslist);
				Calendar cal =  Calendar.getInstance();
				pageContext.setAttribute("cal", cal); %>
					
					<c:if test="${ not empty goodlist}">
								
								<%
									int goodscount = 0;
									pageContext.setAttribute("goodscount", goodscount);
								%>
								<c:forEach var="goodsvo" items="${goodlist}" begin="0" end="5">
									
									<c:if test="${goodscount%2 != 0 }">

										<div class="col-md-4 you-para">
											<a
												href="<%= request.getContextPath() %>/front/goods/goods_detail.jsp?gid=${goodsvo.gid}">
												<img
												src="<%= request.getContextPath() %>/showImage.do?gid=${goodsvo.gid}&action=goods"
												width="200" height="200">
											</a> 


											<%
												MemberVO membervo = new MemberService().getOneMemberByMemberId(goodslist.get(goodscount).getMember_id());
												pageContext.setAttribute("membervo", membervo);
											%>

											<div style="margin-top:5px;">
												<font size="5" color="blue"><b>${goodsvo.g_name}</b></font>
												<c:if test="${goodsvo.member_id != sessionScope.member_id}">
														
														<form method="post" name="login${goodscount}"
															action="<%=request.getContextPath()%>/front/member/member_login.jsp?url=/front/index/index.jsp"
															style="float: left; margin-left: 5px">
															<input type="button" value="交換"  class="btn btn-info" 
								 	 							   data-toggle="modal" href="<%= request.getContextPath() %>/javascript_css/goods/change.jsp?res_member_id=${goodsvo.member_id}&res_gid=${goodsvo.gid}" data-target="#change" > 
															<input type="hidden" id="url" value="<%=request.getContextPath()%>">
															<input type="hidden" id="res_gid" value="${goodsvo.gid}">
															<input type="hidden" id="res_member_id" value="${goodsvo.member_id}"> 
															<input type="hidden" id="member_id" value="<%=session.getAttribute("member_id")%>">
														</form>
													
												</c:if>
												<c:if test="${goodsvo.member_id == sessionScope.member_id}">
												<a href="<%= request.getContextPath() %>/front/goods/GoodsServlet.do?action=update_goods&
																							gid=${goodsvo.gid}&
																							requestURL=/front/index/index.jsp&
																							member_id=${goodsvo.member_id}"><font color="green">修改資料</font></a>
												</c:if>
											</div>

											<div><span id="quitdate<%=goodscount%>"> </span> </div>
											
										</div>
									</c:if>
									
									<c:if test="${goodscount%2 == 0 }">
										<div class="col-md-4 you-para para-grid">
											<a
												href="<%= request.getContextPath() %>/front/goods/goods_detail.jsp?gid=${goodsvo.gid}"><img
												src="<%= request.getContextPath() %>/showImage.do?gid=${goodsvo.gid}&action=goods"
												width="200" height="200"></a>
											<%
												MemberVO membervo = new MemberService().getOneMemberByMemberId(goodslist.get(goodscount)
																											.getMember_id());
																							pageContext.setAttribute("membervo", membervo);
											%>
											<div  style="margin-top:5px;">
												<font size="5" color="blue"><b>${goodsvo.g_name}</b></font>
												<c:if test="${goodsvo.member_id != sessionScope.member_id}">
														<form method="post" name="login${goodscount}" 
															action="<%=request.getContextPath()%>/front/member/member_login.jsp?url=/front/index/index.jsp"
															style="float: left; margin-left: 5px">
															<input type="button" value="交換" class="btn btn-info" 
								 	 							   data-toggle="modal" href="<%= request.getContextPath() %>/javascript_css/goods/change.jsp?res_member_id=${goodsvo.member_id}&res_gid=${goodsvo.gid}" data-target="#change"> 
															<input type="hidden" id="url" value="<%=request.getContextPath()%>">
															<input type="hidden" id="res_gid" value="${goodsvo.gid}">
															<input type="hidden" id="res_member_id" value="${goodsvo.member_id}"> 
															<input type="hidden" id="member_id" value="<%=session.getAttribute("member_id")%>">
														</form>
												</c:if>
												<c:if test="${goodsvo.member_id == sessionScope.member_id}">
												<a href="<%= request.getContextPath() %>/front/goods/GoodsServlet.do?action=update_goods&
																							gid=${goodsvo.gid}&
																							requestURL=/front/index/index.jsp&
																							member_id=${goodsvo.member_id}"><font color="green">修改資料</font></a>
												</c:if> 
											</div> 

											<div><span id="quitdate<%=goodscount%>"> </span> </div>
									
										</div>
									</c:if>
									<input type="hidden" id="goods_time<%= goodscount %>"
										value="${goodsvo.quitdate.getTime() - cal.getTimeInMillis()}">

									<%
										++goodscount;
										pageContext.setAttribute("goodscount", goodscount);
									%>
								
								
								</c:forEach>
								<input type="hidden" id="count" value="<%=goodscount%>">
								<div class="clearfix"></div>
							
							
					</c:if>
							


						
</body>
</html>