<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="BIG5"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
 <%@ page import="java.util.*"%>
 <%@ page import="com.message.model.*"%>
 <%@ page import="com.remessage.model.*"%>
  <%@ page import="com.member.model.*"%>
 <%
    MessageService msgSvc = new MessageService();
    RemessageService remsgSvc = new RemessageService();
 	MemberService memSvc=new MemberService();
    List<MessageVO> list =null;
 	String orderby=request.getParameter("orderby");
 	if(orderby==null){
 		orderby=(String)session.getAttribute("orderby");
 		}
 	if("dateline".equals(orderby)){
 		list = msgSvc.getAllLatest();
 		session.setAttribute("orderby",orderby);
 	}else{
 		list = msgSvc.getAllOrderByRe();
 		session.setAttribute("orderby",orderby);
 	}
    List<RemessageVO> re_list = remsgSvc.getAll();
    pageContext.setAttribute("list",list);
%>
<%@ include file="/front/message/pages/page1.file" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
<!-- -------------------------------------原有 -->
<style type="text/css">
a{text-decoration:none;}
a:link{color:blue;}
a:visited{color:gray;}
a:hover{text-decoration:underline;}
</style>
<%-- <link rel="stylesheet" href="<%= request.getContextPath() %>/javascript_css/message/pageStyle.css" media="screen" type="text/css" /> --%>
<!-- -------------------------------------原有 end-->
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
<!-- -------------------------------------原有 -->
<script type="text/javascript">
$(document).ready(function(){
	$("li:contains('<%= whichPage_M %>')").addClass("active");
});
</script>
<script>
function del(){
	var check=confirm("確定刪除?");
	if(check==false){
		event.returnValue=false;
		return ;
	}
}
</script>
<!-- -------------------------------------原有end -->
</head>
<body>
<%@ include  file="/front/indexFile.file" %>
<!-- ----------------------------------------main---------------------------------------------------------------------- -->

<div id="functions">
<div style="float:left;">
<%@ include file="/front/message/pages/page2.file" %>
</div>
<div class="dropdown" style="float:left;margin-top:20px;">
   <button type="button" class="btn dropdown-toggle" id="dropdownMenu1" 
      data-toggle="dropdown">
      <% if("dateline".equals(orderby)){%>
      發帖時間
      <% }else{%>
      最後發表
      <% }%>
      <span class="caret"></span>
   </button>
   <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
      <li role="presentation">
         <a href="<%= request.getContextPath() %>/front/message/listAllMessageTitle.jsp?orderby=dateline">發帖時間</a>
      </li>
      <li role="presentation">
         <a href="<%= request.getContextPath() %>/front/message/listAllMessageTitle.jsp?orderby=lastpost">最後發表</a>
      </li>
     
      
   </ul>
</div>
<div style="float:right; margin-top:20px;">
<form action="<%= request.getContextPath() %>/front/message/message.do" method="post">
<c:if test="${not empty errorMsgs}">
  <font color='red'>
<c:out value="${errorMsgs[0]}" />
  </font>
</c:if>
<label for="title">查詢:</label><input type="text" name="title">
<input type="submit" value="送出查詢"class="btn btn-info">
<input type="hidden" name="action" value="search">
<input type="hidden" name="source" value="<%= request.getServletPath()%>">
</form>

</div>
</div>
	<table border='1' bordercolor='#CCCCFF' width='800'>
	<tr><th>標題</th><th>作者</th><th>回復人數</th><th>最後發表</th></tr>
<% for(int i=pageIndex;i<=endIndex;i++) {
		MessageVO messageVO=list.get(i);
		MemberVO memVO=memSvc.getOneMemberByMemberId(messageVO.getMember_id());	
		//找到最後一個回覆者
		RemessageVO remessageVO=null;
		MemberVO rememVO=null;
		for(int j=(re_list.size()-1);j>=0;j--){
			if(re_list.get(j).getMid().equals(messageVO.getMid())){
				remessageVO=re_list.get(j);
				rememVO=memSvc.getOneMemberByMemberId(remessageVO.getMember_id());	
				break;
			}
		}
		//總回復數
		int reCount=-1;
		for(int j=0;j<re_list.size();j++){
			if(re_list.get(j).getMid().equals(messageVO.getMid())){
				reCount+=1;
				
			}
		}
%>
		<tr valign='middle'>		
			<td><a href="<%= request.getContextPath() %>/front/remessage/listAllRemessage.jsp?mid=<%= messageVO.getMid() %>&whichPage_M=<%= whichPage_M%>"><%= messageVO.getTitle() %></a></td>
			<td ><font style="color:blue;"><%= memVO.getMem_name() %></font><br><font style="color:gray;"><%= messageVO.getM_date_str() %> </font></td>
			<td><%= reCount %></td>
			<td><font style="color:blue;"><%= rememVO.getMem_name() %></font><br><font style="color:gray;"><%= remessageVO.getR_date_str() %> </font></td>
		
		</tr>
<% } %>
</table>
<%@ include file="/front/message/pages/page2.file" %>
<% if(session.getAttribute("member_id")!=null){ %>
<jsp:include page="/front/message/newMessage.jsp"/>
<% } %>

<!-- -----------------------------------------end-------------------------------------------------------------------- -->
</div>
	</div>
	<!---->
	<div class="footer" style="margin-top:100px;">
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