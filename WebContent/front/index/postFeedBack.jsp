<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>      
<%@ page import="com.post.model.*"%>
<%@ page import="java.util.*"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link
	href="<%=request.getContextPath()%>/javascript_css/front_main_css/bootstrap.css"
	rel="stylesheet" type="text/css" media="all" />
<script
	src="<%=request.getContextPath()%>/javascript_css/front_main_js/jquery.min.js"></script>

<link
	href="<%=request.getContextPath()%>/javascript_css/front_main_css/style.css"
	rel="stylesheet" type="text/css" media="all" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="application/x-javascript">
 addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } 
</script>
<link
	href='http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700,800'
	rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Oswald:400,700,300'
	rel='stylesheet' type='text/css'>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
		<%
						List<PostVO> listPost = new PostService().getAllLatest();
									if(listPost.size() > 0)
										pageContext.setAttribute("listPost", listPost);
					%>
					<c:if test="${not empty listPost }">
						
						<% int postCount = 0; pageContext.setAttribute("postCount", postCount); %>
						<c:forEach var="post" items="${listPost}">

							<c:if test="${not empty post.pic && postCount < 2}">
								 <% ++postCount; pageContext.setAttribute("postCount", postCount); %>
								<div class="discount">
									<span><font color="blue"><b>${post.title}</b></font> </span> 
									
						            <img class="img-responsive pic-in" src="<%= request.getContextPath() %>/showImage.do?pid=${post.pid}&action=post" alt=" "> 
										<a href="<%= request.getContextPath() %>/front/post/postDetail.jsp?pid=${post.pid}" class="know-more">看更多...</a>
								</div>
							</c:if>
							
						</c:forEach>

					</c:if>
</body>
</html>