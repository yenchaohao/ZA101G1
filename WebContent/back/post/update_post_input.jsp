<%@ page contentType="text/html; charset=UTF-8" pageEncoding="Big5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.post.model.*"%>
<%
	PostVO postVO = (PostVO) request.getAttribute("postVO"); //PostServlet.java (Controller), 存入req的postVO物件 (包括幫忙取出的postVO, 也包括輸入資料錯誤時的postVO物件)
	pageContext.setAttribute("postVO", postVO);
%>
<html>
<head>
<title>公告訊息修改 - update_post_input.jsp</title>
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script
	src="<%=request.getContextPath()%>/javascript_css/post/addpost.js"></script>
</head>


<body bgcolor='white'>



	<h3>資料修改:</h3>
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

	<FORM METHOD="post" ACTION="post.do" name="form1"  enctype="multipart/form-data">
		<table border="0">
			<tr>
				<td>公告編號:</td>
			</tr>
			<tr>
				<td><%=postVO.getPid()%></td>
			</tr>
			<tr>
				<td>公告標題:</td>
			</tr>
			<tr>
				<td><input type="TEXT" name="title" size="78"
					value="<%=postVO.getTitle()%>" class="form-control" style="width:500px"/></td>
			</tr>
			<tr>
				<td>公告內容:</td>
			</tr>
			<tr>
				<td><textarea name="post" rows="10" cols="80" class="form-control">
				<%=postVO.getPost()%>
				</textarea></td>
			</tr>

			<tr>
				<td><label for="pic">公告圖片:</label> <input type="file"
					name="pic" id="pic"></td>
			</tr>
			<tr>
				<td id="preview"><c:if test="${not empty postVO.pic}">

						<img
							src="<%= request.getContextPath() %>/showImage.do?pid=${postVO.pid}&action=post"
							height="120" width="120">

					</c:if></td>
			</tr>

			<tr>
				<td>修改者:<font color=red><b>*</b></font></td>
			</tr>
			<jsp:useBean id="empSvc" scope="page"
				class="com.emp.model.EmpService" />
			<tr>
				<td><select size="1" name="empid" class="form-control" style="width:100px">
						<c:forEach var="empVO" items="${empSvc.all}">
							<option value="${empVO.empid}"
								${(postVO.empid==empVO.empid)?'selected':'' }>${empVO.ename}
						</c:forEach>
				</select></td>
			</tr>

		</table>
		<br> <input type="hidden" name="action" value="update"> <input
			type="hidden" name="pid" value="<%=postVO.getPid()%>"> <input
			type="hidden" name="requestURL"
			value="<%=request.getParameter("requestURL")%>">
		<!--接收原送出修改的來源網頁路徑後,再送給Controller準備轉交之用-->
		<input type="hidden" name="whichPage"
			value="<%=request.getParameter("whichPage")%>">
		<!--只用於:istAllEmp.jsp-->
		<input type="submit" value="送出修改" class="btn btn-info">
	</FORM>


</body>
</html>
