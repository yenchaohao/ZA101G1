<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<link
	href="<%=request.getContextPath()%>/javascript_css/front_main_css/bootstrap.css"
	rel="stylesheet" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/javascript_css/front_main_js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/javascript_css/front_main_js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/back/send/sendMap.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="http://maps.google.com/maps/api/js?sensor=false"></script>  
<title>Insert title here</title>
</head>
<body>
<input type="hidden" id="url" value="<%= request.getContextPath() %>">
<% request.setCharacterEncoding("UTF-8");
   String add = request.getParameter("address");
   String address = new String(add.getBytes("ISO-8859-1"),"UTF-8");
%>
 <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">地圖</h4>
      </div>

		<div class="modal-footer"  style="background-color:white;">
			<button type="button" class="btn btn-default" data-dismiss="modal">關閉</button>
</div>
</body>
</html>