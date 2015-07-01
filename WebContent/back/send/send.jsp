<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.send.model.*"  %>  
<%@ page import="com.member.model.*"  %>
<%@ page import="com.goods.model.*"  %>   
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ page import="java.util.*" %>  
<%@ page import="org.json.*" %>
<%@ page import="com.tool.*" %>      
<%
	List<SendVO> list = new SendService().getAllAlive();
	pageContext.setAttribute("list", list);

	List<SendMember> sendList = new ArrayList();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<link
	href="<%=request.getContextPath()%>/javascript_css/front_main_css/bootstrap.css"
	rel="stylesheet" type="text/css" media="all" />
	<script src="<%=request.getContextPath()%>/javascript_css/front_main_js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/javascript_css/front_main_js/bootstrap.min.js"></script>
<script src="http://maps.google.com/maps/api/js?sensor=false"></script>
  
<script src="<%=request.getContextPath()%>/back/send/allLocation.js"></script>
<script src="<%=request.getContextPath()%>/back/send/sendMap.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商品專區</title>
<style>
td {
	text-align: center;
}
</style>
</head> 
<body>
<input type="hidden" id="url" value="<%= request.getContextPath() %>">

<%--  跳出地圖視窗內容 --%>
<%@ include file="/back/send/mapCantainer.file"  %>
<%@ include file="page1.file" %>	
<% ServletContext context = request.getServletContext(); %>
<input type="hidden" id="exicon" value='<%= request.getContextPath()+"/showImage.do?member_id=M1010&action=member" %>'>
	<c:if test="${not empty errorMessage}">
		<ul>
			<c:forEach var="message" items="${errorMessage}">
				<li>${message}</li>
			</c:forEach>
		</ul>
	</c:if>
	<c:if test="${not empty list }">
		
		<form method="post"  class="form-inline" action="<%= request.getContextPath() %>/back/send/send.do">
			
			<select name="status" class="form-control"> 
				    <option value="">派送狀態</option>
					<option value="0">未派送</option>
					<option value="1">派送中</option>
					<option value="2">派送完成</option>
			</select>
			
			<select name="sid" class="form-control"> 
				<option value="">派送編號</option>
				<c:forEach var="sendVO" items="${list}">
					<option value="${sendVO.sid}">${sendVO.sid}</option>
				</c:forEach>
			</select>
			 
			<div class="form-group"> 
				<input type="text" name="mem_address" id="mem_address" class="form-control" placeholder="地址關鍵字">
				<input type="text" name="mem_name" id="mem_name" class="form-control" placeholder="會員姓名">
			</div>	
			
			<input type="hidden" name="action" value="search">
			<input type="hidden" name="requestURL" value="<%= request.getServletPath() %>">
			<input type="hidden" name="whichPage" value="<%= whichPage %>">
			<input type="submit" value="查詢" class="btn btn-info" > 
		</form>
				
		<div style="width:auto; height:auto; margin-top:10px; ">
		<table border="1" class="table table-hover" >
		
		<tr class="warning">
			<td>變更狀態</td>
			<td>派送狀態</td>
			<td>派送編號</td>
			<td>派送地址</td>
			<td>距離(公里)</td>
			<td>商品編號</td>
			<td>商品名稱</td>
			<td>商品照片</td>
			<td>會員編號</td>
			<td>會員姓名</td>
			<td>會員照片</td>
			<td>交易編號</td>
			<td>派送開始時間</td>
			<td>派送結束時間</td>
			
		</tr>
		
		
		<form action="<%= request.getContextPath() %>/back/send/send.do" method="post"> 
		<% int index = pageIndex; pageContext.setAttribute("index", pageIndex); %>
		<% int count =0 ; pageContext.setAttribute("count", count); %>
		<c:forEach var="sendVO" items="${list}" begin="<%= pageIndex %>" end="<%= pageIndex + rowsPerPage -1 %>">
			<% SendMember sendMember = new SendMember(); %>
			<tr>
			<c:if test="${count % 2 == 0}">
			 	<td rowspan="2">
			 		<c:if test="${sendVO.status == 0}">
			 			<select name="status<%= count %>" class="form-control" style="width:120px">
			 				<option value="9" >請選擇</option>
			 				<option value="1" >開始派送</option>
			 				<option value="3" >取消派送</option>
			 			</select>
			 		</c:if>
			 		<c:if test="${sendVO.status == 1}">
			 			<select name="status<%= count %>" class="form-control" style="width:120px">
			 				<option value="9" >請選擇</option>
			 				<option value="0" >停止派送</option> 
			 			</select>
			 		</c:if>
			 	</td> 
			 </c:if>
				<td >${send_status[sendVO.status] }</td>
				<input type="hidden" name="sid<%= count %>" value="${sendVO.sid}">
		
				<td>${sendVO.sid}</td>
				
				<%
					MemberVO membervo = new MemberService().getOneMemberByMemberId(list.get(index).getMember_id());
					pageContext.setAttribute("membervo", membervo); 
				%>
				<td>${membervo.address}
				<input type="button"  class="btn btn-info"  data-toggle="modal"  data-target="#mapModal" value="派送詳情" id="${membervo.address}"></td>
				
				<td><span id="distance<%= count %>">distance</span></td>
				<td>${sendVO.gid}</td>
					<%
				GoodsVO goodsvo = new GoodsService().findGoodsByGid(list.get(index).getGid());
				pageContext.setAttribute("goodsvo", goodsvo); %>
				<td >${goodsvo.g_name}</td>	
				<c:if test="${sendVO.status == 0 }">
					<% sendMember.setGoods_name(goodsvo.getG_name()); %>
				</c:if>
				
				<td><img src="<%= request.getContextPath() %>/showImage.do?gid=${goodsvo.gid}&action=goods"
						weight="50" height="50"></td>
				<c:if test="${sendVO.status == 0 }">
					<% sendMember.setGoodsImage(request.getContextPath()+"/showImage.do?gid="+goodsvo.getGid()+"&action=goods&size=50"); %>
				</c:if>
				<td>${sendVO.member_id}</td>
					
				<td > ${membervo.mem_name }</td>
				<td> <img src="<%= request.getContextPath() %>/showImage.do?member_id=${membervo.member_id}&action=member"
							weight="50" height="50"></td>
				<c:if test="${sendVO.status == 0 }">
					<% sendMember.setMem_name(membervo.getMem_name()); %>
					<% sendMember.setMemberImage(request.getContextPath()+"/showImage.do?member_id="+membervo.getMember_id()+"&action=member&size=60"); %>
				</c:if>
				
				<td> ${sendVO.tid}</td>
				<td> ${sendVO.getStart_date_format()}</td>
				<td> ${sendVO.getEnd_date_format()}</td>
		
			 
			 </tr>
			<c:if test="${sendVO.status == 0 }">
				<% sendMember.setAddress(membervo.getAddress()); %>
			</c:if>
			
			<script> 
					
			</script>
			<% count++; pageContext.setAttribute("count", count); index++; pageContext.setAttribute("index", index); %>
			<c:if test="${sendVO.status == 0 }">
			<% sendList.add(sendMember);%>
			</c:if>
	 </c:forEach>
		
		<input type="hidden" name="count" value="<%= count %>">		
		<% String sendJson = (new JSONArray(sendList)).toString(); %> 
		<input type="hidden" id="sendList" value='<%= sendJson %>' >
	
	</table>
	<%@ include file="page2.file"%>
		</div>
	<span><b>未派送地圖: <font color="blue"><%= sendList.size() %></font> 筆未派送</b></span>
	<div id="allLocation" style= "width:1100px; height:500px;  margin-top:10px;" >
	</div>	
	
	</c:if>
	
	<c:if test="${empty list}">
		<h1>目前沒有任何派送請求</h1>
	</c:if>
	
	
	
		
</body>
</html>