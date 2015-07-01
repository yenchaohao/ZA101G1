<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>  
<%@ page import="java.util.*"%>
<%@ page import="com.message.model.*"%>
<%@ page import="com.member.model.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
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
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
</head>
<body>
	
		<%MessageService msgSvc = new MessageService();
		  List<MessageVO> list = msgSvc.getAllLatest();
		  pageContext.setAttribute("messageList", list);
	     int count = 0;
					%>
					<c:if test="${not empty messageList }">
						<div class="twitter-in">
							<h5>最新討論</h5>
							<span class="twitter-ic"> </span>

							<c:if test="${messageList.size() >= 3  }">
								<c:forEach var="message" items="${messageList}" begin="0"
									end="2">

									<div class="up-date">
										<p>
											<a
												href="<%= request.getContextPath() %>/front/remessage/listAllRemessage.jsp?mid=${message.mid}">${message.title}</a>
										</p>
										<%
											MemberVO membervo = new MemberService()
																								.getOneMemberByMemberId(list.get(count)
																										.getMember_id());
																						pageContext.setAttribute("membervo", membervo);
										%>
										<p id="member">${membervo.mem_name}</p>
										<p class="ago">${message.getM_date_str()}</p>
									</div>
									<%
										++count;
									%>
								</c:forEach>
							</c:if>
							<c:if test="${messageList.size() < 3  }">
								<c:forEach var="message" items="${messageList}">

									<div class="up-date">
										<p>
											<a
												href="<%= request.getContextPath() %>/front/remessage/listAllRemessage.jsp?mid=${message.mid}">${message.title}</a>
										</p>
										<%
											MemberVO membervo = new MemberService()
																								.getOneMemberByMemberId(list.get(count)
																										.getMember_id());
																						pageContext.setAttribute("membervo", membervo);
										%>
										<p id="member">${membervo.mem_name}</p>
										<p class="ago">${message.getM_date()}</p>
									</div>
									<%
										++count;
									%>
								</c:forEach>
							</c:if>

							<a
								href="<%=request.getContextPath()%>/front/message/listAllMessageTitle.jsp"
								class="tweets">更多討論</a>
							<div class="clearfix"></div>
						</div>
					</c:if>
</body>
</html>