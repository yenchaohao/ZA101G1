<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.member.model.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.group.model.*"%>
<%
	List<MemberVO> list = new ArrayList();
	list = (List<MemberVO>)session.getAttribute("listMember");
	
	List<GroupVO> groupList = new GroupService().getAll();
	pageContext.setAttribute("list", list); 
	pageContext.setAttribute("groupList", groupList); 
	
	String location = (String)session.getAttribute("location");
	if(location != null)
		session.removeAttribute("location");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link
	href="<%=request.getContextPath()%>/javascript_css/front_main_css/bootstrap.css"
	rel="stylesheet" type="text/css" media="all" />
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script
	src="<%=request.getContextPath()%>/javascript_css/front_main_js/jquery.min.js"></script>
<!-- Custom Theme files -->
<!--theme-style-->
<link
	href="<%=request.getContextPath()%>/javascript_css/front_main_css/style.css"
	rel="stylesheet" type="text/css" media="all" />
<!--//theme-style-->
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords"
	content="Fashion Store Responsive web template, Bootstrap Web Templates, Flat Web Templates, Andriod Compatible web template, 
Smartphone Compatible web template, free webdesigns for Nokia, Samsung, LG, SonyErricsson, Motorola web design" />
<script type="application/x-javascript">
	 addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } 
</script>
<!--fonts-->
<link
	href='http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700,800'
	rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Oswald:400,700,300'
	rel='stylesheet' type='text/css'>
<!--//fonts-->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
td {
	text-align: center;
}
</style>
<title>會員資料</title>
</head>
<body>

	<%@ include  file="/front/indexFile.file" %>

			<c:if test="${not empty errorMessage }">
				<ul>
					<c:forEach var="message" items="${errorMessage }">
						<li>${message}</li>
					</c:forEach>
				</ul>
			
			</c:if>
			
			<div><a href="<%= request.getContextPath() %>/front/member/show_one_member.jsp">增加/修改我的願望</a></div>
			
			<form method="post" action="<%= request.getContextPath() %>/front/wish/wish.do">
			
			<label for="groupid">選擇願望類別:</label> 
				<select name="groupid">
					<option value="">請選擇</option>
						<c:forEach var="group" items="${groupList}">
							<option value="${group.groupid}" ${(id == null)? '' : (group.groupid == id)? 'selected' : '' } >${group.group_name}</option>
						</c:forEach>
				</select>
				
				<label for="my_wish">願望關鍵字:</label>
				<input type="text" id="my_wish" name="my_wish" size="7" >
				<label for="my_wish">會員名稱:</label>
				<input type="text" id="mem_name" name="mem_name" size="7" >
			
			<input type="hidden" value="wishSearch" name="action">	
			<input type="submit" value="送出查詢" class="btn btn-info">	 
			</form>
			
		 <c:if test="${not empty list}">
			
			<div><%@ include file="page1.file"%></div>
			<div style="float:right; margin:20px; width:1100px;">
		
			</div>
			
			<%
				int count = 0;
				pageContext.setAttribute("count", count);
			%>
			
			<c:forEach var="MemberVO" items="${list}" begin="<%= pageIndex %>"
				end="<%= pageIndex + rowPerPage -1 %>">

				<c:if test="${count % 2 == 0 }">
					<div class="col-md-4 you-men">
						<a
							href="<%= request.getContextPath() %>/front/goods/show_goods_for_desired.jsp?member_id=${MemberVO.member_id}">
							<img
							src="<%= request.getContextPath() %>/showImage.do?member_id=${MemberVO.member_id}&action=member"
							weight="150" height="150">
						</a>
						<% 
							String my_wish = "";	
								if(list.get(count).getMy_wish() != null)
							my_wish =  list.get(count).getMy_wish().substring(1,list.get(count).getMy_wish().length()); 
						%>
						<p>我想要<font size="5" color="blue"><b><%= my_wish %></b></font></p>
							<span>${MemberVO.mem_name}</span>
					</div>
				</c:if>
				<c:if test="${count % 2 != 0 }">
					<div class="col-md-4 you-men para-grid">
						<a
							href="<%= request.getContextPath() %>/front/goods/show_goods_for_desired.jsp?member_id=${MemberVO.member_id}">
							<img
							src="<%= request.getContextPath() %>/showImage.do?member_id=${MemberVO.member_id}&action=member"
							weight="150" height="150">
						</a>
							<% 
							String my_wish = "";	
								if(list.get(count).getMy_wish() != null)
									my_wish =  list.get(count).getMy_wish().substring(1,list.get(count).getMy_wish().length()); 
							%>
						<p>我想要<font size="5" color="blue"><b><%= my_wish %></b></font></p>
							<span>${MemberVO.mem_name}</span>
					</div>

				</c:if>
				<%
					count++;
						pageContext.setAttribute("count", count);
				%>


			</c:forEach>
			
			
			<%@ include file="page2.file"%>
			</c:if>
			<c:if test="${empty list}">
				查無任何願望
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