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
    List<MessageVO> list =(List<MessageVO>) request.getAttribute("list");
    List<RemessageVO> re_list = remsgSvc.getAll();
   
%>
<%@ include file="/back/message/pages/page1.file" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
<style type="text/css">
a{text-decoration:none;}
a:link{color:blue;}
a:visited{color:gray;}
a:hover{text-decoration:underline;}
</style>
<link rel="stylesheet" href="<%= request.getContextPath() %>/javascript_css/message/pageStyle.css" media="screen" type="text/css" />
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
<script type="text/javascript">
$(document).ready(function(){
	$("a[class]:contains('<%= whichPage_M %>')").removeClass("page gradient");
	$("a[class]:contains('<%= whichPage_M %>')").addClass("page active");
});
</script>
</head>
<body>

<!-- ----------------------------------------main---------------------------------------------------------------------- -->
<%@ include file="/back/message/pages/page2.file" %>
<span style="float:right;">搜尋:${title}</span>
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
			<td><a href="<%= request.getContextPath() %>/back/remessage/listAllRemessage.jsp?mid=<%= messageVO.getMid() %>"><%= messageVO.getTitle() %></a></td>
			<td ><font style="color:blue;"><%= memVO.getMem_name() %></font><br><font style="color:gray;"><%= messageVO.getM_date_str() %> </font></td>
			<td><%= reCount %></td>
			<td><font style="color:blue;"><%= rememVO.getMem_name() %></font><br><font style="color:gray;"><%= remessageVO.getR_date_str() %> </font></td>
		<td>	
		<form action="<%= request.getContextPath() %>/back/message/message.do" method="post">
		<input type="submit" value="刪除">
		<input type="hidden"  name="action" value="del_msg">
		<input type="hidden"  name="mid" value="<%= messageVO.getMid() %>">
		<input type="hidden" name="source" value="<%= request.getServletPath()%>">
		</form>
		</td>
		</tr>
<% } %>
</table>

<!-- -----------------------------------------end-------------------------------------------------------------------- -->



</body>
</html>