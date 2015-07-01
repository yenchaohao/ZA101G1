<%@ page contentType="text/html; charset=UTF-8" pageEncoding="Big5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.post.model.*"%>
<%
	PostVO postVO = (PostVO) request.getAttribute("postVO"); //PostServlet.java (Controller), �s�Jreq��postVO���� (�]�A�������X��postVO, �]�]�A��J��ƿ��~�ɪ�postVO����)
	pageContext.setAttribute("postVO", postVO);
%>
<html>
<head>
<title>���i�T���ק� - update_post_input.jsp</title>
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script
	src="<%=request.getContextPath()%>/javascript_css/post/addpost.js"></script>
</head>


<body bgcolor='white'>



	<h3>��ƭק�:</h3>
	<%-- ���~��C --%>
	<c:if test="${not empty errorMsgs}">
		<font color='red'>�Эץ��H�U���~:
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
				<td>���i�s��:</td>
			</tr>
			<tr>
				<td><%=postVO.getPid()%></td>
			</tr>
			<tr>
				<td>���i���D:</td>
			</tr>
			<tr>
				<td><input type="TEXT" name="title" size="78"
					value="<%=postVO.getTitle()%>" class="form-control" style="width:500px"/></td>
			</tr>
			<tr>
				<td>���i���e:</td>
			</tr>
			<tr>
				<td><textarea name="post" rows="10" cols="80" class="form-control">
				<%=postVO.getPost()%>
				</textarea></td>
			</tr>

			<tr>
				<td><label for="pic">���i�Ϥ�:</label> <input type="file"
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
				<td>�ק��:<font color=red><b>*</b></font></td>
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
		<!--������e�X�ק諸�ӷ��������|��,�A�e��Controller�ǳ���椧��-->
		<input type="hidden" name="whichPage"
			value="<%=request.getParameter("whichPage")%>">
		<!--�u�Ω�:istAllEmp.jsp-->
		<input type="submit" value="�e�X�ק�" class="btn btn-info">
	</FORM>


</body>
</html>
