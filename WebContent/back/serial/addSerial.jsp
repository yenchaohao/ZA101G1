<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="BIG5"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
 <%@ page import="com.serial.model.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
</head>
<body>
<a href="<%= request.getContextPath() %>/back/serial/listAllSerial.jsp" target="iframe">�Ҧ��I�ƦC��</a><p>
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
�s�W�I��
<form action="serial.do" method="post">
�ƶq:<input type="text" name="quantity" class="form-control" style="width:100px">
���B:<select name="money" class="form-control" style="width:100px">
<option >1000</option>
<option >2000</option>
<option >5000</option>
<option >10000</option>
<option >50000</option>
</select><br>
<input type="submit" value="�e�X" class="btn btn-info">
<input type="hidden" name="action" value="new_serial">
</form>

</body>
</html>