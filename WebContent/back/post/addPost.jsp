<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.post.model.*"%>
<%
PostVO postVO = (PostVO) request.getAttribute("postVO");
%>

<html>
<head>
<title>新增公告 - addPost.jsp</title>
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script src="<%= request.getContextPath() %>/javascript_css/post/addpost.js"  ></script>

</head>
<body bgcolor='white'>


<h3>資料員工:</h3>
<%-- 錯誤表列 --%>
<c:if test="${not empty errorMsgs}">
	<font color='red'>請修正以下錯誤:
	<ul>
		<c:forEach var="message" items="${errorMsgs}">
			<li>${message}</li>
		</c:forEach>
	</ul>
	</font>
</c:if>

<FORM METHOD="post" ACTION="<%=request.getContextPath()%>/back/post/post.do" name="form1" enctype="multipart/form-data" >
<table border="0">

	<tr>
		<td>公告標題:</td>
	</tr>
	<tr>	
		<td><input type="text" name="title" 
			value="<%= (postVO==null)? "標題" : postVO.getTitle()%>" size="50" class="form-control" style="width:400px"/></td>
	</tr>
	<tr>
		<td>公告內容:</td>
	</tr>	
	<tr>
		<td>
			<textarea name="post"   rows="10" cols="80" class="form-control">
				<%= (postVO==null)? "內容" : postVO.getPost()%>" 
			</textarea>	
		</td>
	</tr>


	<jsp:useBean id="empSvc" scope="page" class="com.emp.model.EmpService" />
	<tr>
		<td>發佈者:<font color=red><b>*</b></font>
		<select size="1" name="empid" class="form-control" style="width:100px">
			<c:forEach var="empVO" items="${empSvc.all}">
				<option value="${empVO.empid}" ${(postVO.empid==empVO.empid)? 'selected':'' } >${empVO.ename}
			</c:forEach>
		</select></td>
	</tr>
	
	<tr>
		<td>
		<label for="pic" >公告圖片:</label>
		<input type="file" name="pic" id="pic">
		</td>
	</tr>
	<tr><td id="preview"></td></tr>



</table>
<br>
<input type="hidden" name="action" value="insert">
<input type="submit" value="送出新增" class="btn btn-success">
</FORM>


</body>

</html>
