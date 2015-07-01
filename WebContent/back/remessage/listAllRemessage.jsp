<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="BIG5"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
 <%@ page import="java.util.*"%>
  <%@ page import="java.text.*"%>
 <%@ page import="com.remessage.model.*"%>
  <%@ page import="com.member.model.*"%>
  <%
	//避免快取
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
	
	
	String member_id= (String) session.getAttribute("member_id");
	
    RemessageService msgSvc = new RemessageService();  
    MemberService memSvc=new MemberService();
    Integer mid=Integer.parseInt(request.getParameter("mid"));
    List<RemessageVO> list = msgSvc.getByMid(mid);
    //Collections.reverse(list);
    pageContext.setAttribute("list",list);
    request.setAttribute("mid",mid);
   
%>
<%@ include file="/back/remessage/pages/page1.file" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
<!-- -------------------------------------原有 -->
<style>
pre {
  width:875px;  
  white-space: pre-wrap; /* css-3 */
  white-space: -moz-pre-wrap; /* Mozilla, since 1999 */
  white-space: -pre-wrap; /* Opera 4-6 */
  white-space: -o-pre-wrap; /* Opera 7 */
  word-wrap: break-word; /* Internet Explorer 5.5+ */
}
font{color:#blue}

#container{
margin-top:0px;
}
</style>
<%--  <link rel="stylesheet" href="<%= request.getContextPath() %>/javascript_css/message/pageStyle.css" media="screen" type="text/css" /> --%>
<!-- -------------------------------------原有end -->
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
<!-- -------------------------------------原有 -->
<script type="text/javascript">
$(document).ready(function(){
<%-- 	$("a[class]:contains('<%= whichPage %>')").removeClass("page gradient"); --%>
	$("li:contains('<%= whichPage %>')").addClass("active");
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


<!-- ----------------------------主要------------------------------------------------------------------------- -->
<div id="bar" style="width:1176px;">
<%@ include file="/back/remessage/pages/page2.file" %>
</div>
<div id="container"   style="clear:both;">
<table>
<%-- <c:forEach var="remessageVO" items="${list}"> --%>

<%
	for(int i=pageIndex;i<=endIndex;i++) {
	RemessageVO remessageVO=list.get(i);
	MemberVO memVO=memSvc.getOneMemberByMemberId(remessageVO.getMember_id());		
%>
<tr stytle="height:150px;" >
	<td style="width:120px;height:150px;"valign="top" ><font style="color:blue;">發文者:<%= memVO.getMem_name() %>
	</font><br>
	<img src="<%= request.getContextPath() %>/showImage.do?member_id=<%= remessageVO.getMember_id() %>&action=member"
							weight="120" height="120">
	</td>
	<td style="width:1000px;" valign="top" ><font style="color:#0000CC;font-size:12px;">發表於:<%=  remessageVO.getR_date_str() %> </font><font style="color:#FF6600;font-size:14px;">
	  <%= ++floor %>樓</font>    
	  
	  <br>---------------------------------------------------------<br>
	<font style="color:white;"><pre style="margin-top:0px;font-size:16px;border:0px;""><%= remessageVO.getMessage() %></pre></font></td>
<!-- 	<td> -->
<%-- 	<form action="<%= request.getContextPath() %>/back/remessage/remessage.do" method="post"> --%>
<!-- 	<input type="submit"  value="修改"> -->
<!-- 	<input type="hidden" name="action" value="alter_remessage"> -->
<%-- 	<input type="hidden" name="mid" value="<%= mid %>"> --%>
<%-- 	<input type="hidden" name="whichPage" value="<%= whichPage%>"> --%>
<%-- 	<input type="hidden" name="rid" value="<%= remessageVO.getRid() %>"> --%>
<!-- 	</form> -->
<!-- 	</td> -->
	<td>
	<form  action="<%= request.getContextPath() %>/back/remessage/remessage.do" method="post" onsubmit="del()">
	<input type="submit"  value="刪除" class="btn btn-danger">
	<input type="hidden" name="action" value="del_remessage">
	<input type="hidden" name="mid" value="<%= mid %>">
	<input type="hidden" name="whichPage" value="<%= whichPage%>">
	<input type="hidden" name="rid" value="<%= remessageVO.getRid() %>">
	</form>
	</td>
</tr>
<% } %>
<%-- </c:forEach> --%>
</table>
</div>
<%@ include file="/back/remessage/pages/page2.file" %>
<div style="margin-top:20px;">
<% if(session.getAttribute("member_id")!=null){ %>
<%-- <jsp:include page="/back/remessage/newReply.jsp"/> --%>
<% } %>
</div>
<!-- ---------------------------------------------------------------------------------------------------- -->



</body>
</html>