<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="BIG5"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
 <%@ page import="java.util.*"%>
 <%@ page import="com.mail.model.*"%>
 <%@ page import="com.tran.model.*" %>
 <%
 List<TranVO> tranList=null;
 if(session.getAttribute("member_id")!=null){
 	TranService tranSvc=new TranService();
 	tranList=tranSvc.getOneByResMember_idUn((String)session.getAttribute("member_id"));
 	pageContext.setAttribute("tranList", tranList);
 }
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
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
<script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } </script>
<!--fonts-->
<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700,800' rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Oswald:400,700,300' rel='stylesheet' type='text/css'>
</head>
<body>
 <div class="header">
		<div class="header-top">
			<div class="container">
				<div class="header-grid">
					<ul>
						<li><a href="<%= request.getContextPath() %>/front/index/index.jsp" class="scroll"><img src="<%= request.getContextPath() %>/images/exlogo.png"></a></li>
						<li><a href="<%= request.getContextPath() %>/front/post/post.jsp" class="scroll">���i</a></li>
						<li><a
							href="<%=request.getContextPath()%>/front/customerCenter/contact.jsp"
							class="scroll">�ȪA����</a></li>
                        <li><a href="<%= request.getContextPath() %>/front/member/show_one_member.jsp" class="scroll">�|���M�� </a></li>
                        <li><a href="<%=request.getContextPath()%>/front/cart/cartServlet.do?action=showCart" class="scroll"><!--<img src="<%= request.getContextPath() %>/images/car.png">-->������ </a></li>	
					</ul>
				</div> 
				<div class="header-grid-right">
					
					<c:if test="${not empty sessionScope.member_id }">
						<a href="<%= request.getContextPath() %>/front/member/show_one_member.jsp" >
						<img src="<%= request.getContextPath() %>/showImage.do?member_id=${sessionScope.member_id}&action=member"
							weight="50" height="50"></a>
						<a href="<%= request.getContextPath() %>/front/member/MemberServlet.do?requestURL=<%= request.getServletPath() %>&action=logout&whichPage=<%= request.getParameter("whichPage") %>" class="sign-up">�n�X</a>
					</c:if>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<c:if test="${not empty tranList}">
					<a href="<%=request.getContextPath()%>/front/transaction/receive.jsp"><font color="red"><b>�z�ثe��<%= tranList.size() %>������ݽT�{</b></font></a>
					</c:if>
				</div>
				<div class="clearfix"> </div>
			</div>
		</div>
		
	
		
		<div class="container">
		<div class="header-bottom">			
				<div class="logo">
				<!--	<a href="index.html"><img src="<%= request.getContextPath() %>/images/exlogo.png" alt=" " ></a>-->
				</div>
					<div class="ad-right">
					<!--<img class="img-responsive" src="<%= request.getContextPath() %>/images/ad.png" alt=" " >-->
				</div>
				<div class="clearfix"> </div>
			</div>	
	
		</div>
	</div>
	<!--content-->
	<div class="content">
		<div class="container">
<!-- 		----------------------------------------main -->
<div class="privacy-top">
<h3>
�ȪA����
</h3>
</div>
<%-- ���~��C --%>
<c:if test="${not empty errorMsgs}">
	<font color='red'>
	<ul>
		<c:forEach var="message" items="${errorMsgs}">
			<li>${message}</li>
		</c:forEach>
	</ul>
	</font>
</c:if>
<script>
	$(document).ready(function (){
		$("#btnSubmit").click(function(){
			if($("#titleId").val().length != 0  && $("#content").val().length != 0){
				alert('�H��w�e�X');
				qForm.submit();
			}else if($("#titleId").val().length == 0){
				alert('�п�ܰ��D�ﶵ');
			}else if($("#content").val().length == 0){
				alert('�ж�J���D���e');
			}
		});
	});
</script>
<form action="<%= request.getContextPath() %>/front/customerCenter/mail.do" method="post" name="qForm">
<select name="title" class="form-control" id="titleId">
<option value="">�п��</option>
<option value="�t�ΰ��D">�t�ΰ��D</option>
<option value="�ӫ~���D">�ӫ~���D</option>
<option value="�|�����D">�|�����D</option>
<option value="������D">������D</option>
<option value="�I�ư��D">�I�ư��D</option>
</select><br/>
<textarea  cols="90" rows="9" name="question" id="content"  class="form-control"></textarea>
<input type="hidden" name="action" value="addQuestion">
<input type="hidden" name="member_id" value="<%= session.getAttribute("member_id")%>">
<input type="button" value="�e�X" id="btnSubmit" class="btn btn-info" style="margin:10px; float:right;">
</form>
<!-- -------------------------------main end -->
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