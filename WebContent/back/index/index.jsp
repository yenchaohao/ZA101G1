<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="BIG5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="empSvc" scope="page" class="com.emp.model.EmpService"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Ex-Change</title>
<link href='http://fonts.googleapis.com/css?family=Oswald:400,300' rel='stylesheet' type='text/css' />
<link href='http://fonts.googleapis.com/css?family=Abel|Satisfy' rel='stylesheet' type='text/css' />
<link href="<%= request.getContextPath() %>/javascript_css/emp/default.css" rel="stylesheet" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/javascript_css/front_main_js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/javascript_css/front_main_js/jquery.min.js"></script>
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
</head>
<body>
<div id="header-wrapper">
	<div id="header">
		<div id="logo">
		<a href="index.jsp" accesskey="1" title=""><img id="icon"src="<%= request.getContextPath() %>/images/logo.png"></a>
			<!-- <h1><a href="#">後端管理</a></h1> -->
			
		</div>
		<div id="menu">
			<ul>
				<li>
					<img src="<%= request.getContextPath() %>/showImage.do?action=emp&empid=${sessionScope.empid}" height="70" width="70">
				</li>
				<li class="current_page_item"><a href="index.jsp" accesskey="1" title="">回首頁</a></li>
				<li><a href="<%= request.getContextPath() %>/back/emp/EmpServlet.do?action=logout" accesskey="4" title="">登出</a></li>
				<!-- <li><a href="#" accesskey="5" title="">許願池</a></li> -->
			</ul>
		</div>
	</div>
</div>
<div id="footer-wrapper">
	<div id="footer-content" style="width:1500px;height:1000px; ">
		<div id="fbox1">
			<h2><span></span>功能選單 </h2>
	
			<p>&nbsp;</p>
			<ul class="style2">
				<c:forEach var="authority" items="${authority}">
				<c:if test="${authority.aid==0}">
				<li><a href="<%= request.getContextPath() %>/back/emp/select_page.jsp" target="iframe">員工帳戶管理</a></li>
				</c:if>
				<c:if test="${authority.aid==1}">
				<li><a href="<%= request.getContextPath() %>/back/serial/addSerial.jsp" target="iframe">點數管理</a></li>
				</c:if>
				<c:if test="${authority.aid==2}">
				<li><a href="<%= request.getContextPath() %>/back/post/select_page.jsp" target="iframe">管理公告訊息</a></li>
				</c:if>
				<c:if test="${authority.aid==3}">
				<li><a href="<%= request.getContextPath() %>/back/goods/select_page.jsp"  target="iframe">商品管理</a></li>
				</c:if>
				<c:if test="${authority.aid==4}">
				<li><a href="<%= request.getContextPath() %>/back/mail/checkMail.jsp" target="iframe">查詢客戶訊息</a></li>
				</c:if>
				<c:if test="${authority.aid==5}">
				<li><a href="<%= request.getContextPath() %>/back/message/listAllMessageTitle.jsp" target="iframe">管理留言板</a></li>
				</c:if>
				<c:if test="${authority.aid==6}">
				<li><a href="<%= request.getContextPath() %>/back/send/send.jsp" target="iframe">商品派送</a></li>
				</c:if>
				<c:if test="${authority.aid==7}">
				<li><a href="<%= request.getContextPath() %>/back/member/select_page.jsp" target="iframe">會員管理</a></li>
				</c:if>
				</c:forEach>
			</ul> 
			
		</div>		
		<iframe   src="<%= request.getContextPath() %>/back/index/indexDetail.jsp" name="iframe" id="iframe" frameborder="0" border="0" 
				 style="width:1200px;height:1000px; margin-left:100px;"	>
				 
		</iframe> 
	</div>
</div>


</body>
</html>
