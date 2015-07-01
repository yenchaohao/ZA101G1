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
<%@ include file="/back/message/pages/page1.file" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
<!-- -------------------------------------Τ -->
<style type="text/css">
a{text-decoration:none;}
a:link{color:blue;}
a:visited{color:gray;}
a:hover{text-decoration:underline;}
</style>
<%-- <link rel="stylesheet" href="<%= request.getContextPath() %>/javascript_css/message/pageStyle.css" media="screen" type="text/css" /> --%>
<!-- -------------------------------------Τ end-->
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
<!-- -------------------------------------Τ -->
<script type="text/javascript">
$(document).ready(function(){
	$("li:contains('<%= whichPage_M %>')").addClass("active");
});
</script>
<script>
function del(){
	var check=confirm("絋﹚埃?");
	if(check==false){
		event.returnValue=false;
		return ;
	}
}
</script>
<!-- -------------------------------------Τend -->
<!-- ----------------------------------------main---------------------------------------------------------------------- -->
<div id="functions">
<div style="float:left;">
<%@ include file="/back/message/pages/page2.file" %>
</div>
<div class="dropdown" style="float:left;margin-top:20px;">
   <button type="button" class="btn dropdown-toggle" id="dropdownMenu1" 
      data-toggle="dropdown">
      <% if("dateline".equals(orderby)){%>
      祇┇丁
      <% }else{%>
      程祇
      <% }%>
      <span class="caret"></span>
   </button>
   <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
      <li role="presentation">
         <a href="<%= request.getContextPath() %>/back/message/listAllMessageTitle.jsp?orderby=dateline">祇┇丁</a>
      </li>
      <li role="presentation">
         <a href="<%= request.getContextPath() %>/back/message/listAllMessageTitle.jsp?orderby=lastpost">程祇</a>
      </li>
     
      
   </ul>
</div>
<div style="float:right; margin-top:20px;">
<form action="<%= request.getContextPath() %>/back/message/message.do" method="post">
<c:if test="${not empty errorMsgs}">
  <font color='red'>
<c:out value="${errorMsgs[0]}" />
  </font>
</c:if>
<label for="title">琩高:</label><input type="text" name="title">
<input type="submit" class="btn btn-info">
<input type="hidden" name="source" value="<%= request.getServletPath()%>">
<input type="hidden" name="action" value="search">
</form>

</div>
</div>
	<table border='1' bordercolor='#CCCCFF' width='800'>
	<tr><th>夹肈</th><th></th><th>確计</th><th>程祇</th></tr>
<% for(int i=pageIndex;i<=endIndex;i++) {
		MessageVO messageVO=list.get(i);
		MemberVO memVO=memSvc.getOneMemberByMemberId(messageVO.getMember_id());	
		//т程滦
		RemessageVO remessageVO=null;
		MemberVO rememVO=null;
		for(int j=(re_list.size()-1);j>=0;j--){
			if(re_list.get(j).getMid().equals(messageVO.getMid())){
				remessageVO=re_list.get(j);
				rememVO=memSvc.getOneMemberByMemberId(remessageVO.getMember_id());	
				break;
			}
		}
		//羆確计
		int reCount=-1;
		for(int j=0;j<re_list.size();j++){
			if(re_list.get(j).getMid().equals(messageVO.getMid())){
				reCount+=1;
				
			}
		}
%>
		<tr valign='middle'>		
			<td><a href="<%= request.getContextPath() %>/back/remessage/listAllRemessage.jsp?mid=<%= messageVO.getMid() %>&whichPage_M=<%= whichPage_M%>"><%= messageVO.getTitle() %></a></td>
			<td ><font style="color:blue;"><%= memVO.getMem_name() %></font><br><font style="color:gray;"><%= messageVO.getM_date_str() %> </font></td>
			<td><%= reCount %></td>
			<td><font style="color:blue;"><%= rememVO.getMem_name() %></font><br><font style="color:gray;"><%= remessageVO.getR_date_str() %> </font></td>
		<td>	
		<form action="<%= request.getContextPath() %>/back/message/message.do" method="post" onsubmit="del()">
		<input type="submit" value="埃" class="btn btn-danger">
		<input type="hidden"  name="action" value="del_msg">
		<input type="hidden"  name="whichPage_M" value="<%= whichPage_M %>">
		<input type="hidden"  name="mid" value="<%= messageVO.getMid() %>">
		<input type="hidden" name="source" value="<%= request.getServletPath()%>">
		</form>
		</td>
		</tr>
<% } %>
</table>
<%@ include file="/back/message/pages/page2.file" %>
<% if(session.getAttribute("member_id")!=null){ %>
<%-- <jsp:include page="/back/message/newMessage.jsp"/> --%>
<% } %>
<!-- -----------------------------------------end-------------------------------------------------------------------- -->




</body>
</html>