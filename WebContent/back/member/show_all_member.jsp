<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.member.model.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%

	List<MemberVO> list = new ArrayList();
	String member_id = request.getParameter("member_id");
	
	if (member_id == null || "getAll".equals(member_id))
		list = new MemberService().getAll();
	else
		list.add(new MemberService().getOneMemberByMemberId(member_id));

	pageContext.setAttribute("list", list);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>會員資料</title>
</head>
<body>
	<c:if test="${not empty errorMessage }">
		<ul>
			<c:forEach var="message" items="${errorMessage }">
				<li>${message}</li>
			</c:forEach>
		</ul>
	</c:if>

	<table border="1" class="table table-hover">
		<tr class="warning">
			<th>會員照片</th>
			<th>電子郵件</th>
			<th>會員姓名</th>
			<th>電話</th>
			<th>地址</th>
			<th>生日</th>
			<th>加入日期</th>
			<th>會員評價</th>
			<th>擁有點數</th>
			<th>被扣押的點數</th>
			<th>帳戶狀態</th>
			<th>狀態修改</th>
			
		</tr>
		<%@ include file="page1.file"%>
		<form
			action="<%=request.getContextPath()%>/front/member/MemberServlet.do"
			method="post">
			<% int count = 0; %>
			<c:forEach var="MemberVO" items="${list}" begin="<%= pageIndex %>"
				end="<%= pageIndex + rowPerPage -1 %>">
				
				<tr align='center' valign='middle'
					${(MemberVO.member_id == param.member_id)? 'bgcolor=#CCCCFF' : '' }>
					<td><img src="<%= request.getContextPath() %>/showImage.do?member_id=${MemberVO.member_id}&action=member"
							weight="120" height="120">
					</td>
					<td width="18%">${MemberVO.email}</td>
					<td>${MemberVO.mem_name}</td>
					<td>${MemberVO.tel}</td>
					<td>${MemberVO.address}</td>
					<td>${MemberVO.birthday}</td>
					<td>${MemberVO.joindate}</td>
					<td>${MemberVO.credit}</td>
					<td>${MemberVO.having_p}</td>
					<td>${MemberVO.pending_p}</td>
					<td>${member_status[MemberVO.mem_status]}</td>
					<td>
						<select name="status<%= count %>"  class="form-control" style="width:120px">
							<option value="0" >請選擇狀態</option>
							<c:if test="${MemberVO.mem_status == 10}">
								<option value="20">接受申請</option>
								<option value="11">駁回申請</option>
							</c:if>	
							<c:if test="${MemberVO.mem_status == 20}">
								<option value="21">會員停權</option>
							</c:if>
							<c:if test="${MemberVO.mem_status == 30}">
								<option value="31">會員停權</option>
							</c:if>
							<c:if test="${MemberVO.mem_status == 21 }">
								<option value="20">會員權限恢復</option>
							</c:if>
							<c:if test="${MemberVO.mem_status == 31 }">
								<option value="30">會員權限恢復</option>
							</c:if>
							</select>
					</td>
					<input type="hidden" name="member_id<%= count %>" value="${MemberVO.member_id}">
					
				</tr>
				<% count++; %>
			</c:forEach>
			<input type="hidden" name="count" value="<%= count+1 %>">
	</table>
	<%@ include file="page2.file"%>






</body>
</html>