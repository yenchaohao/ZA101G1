<%@ page contentType="text/html; charset=UTF-8" pageEncoding="Big5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
<title>ExChange Post: Home</title>
</head>
<body bgcolor='white'>





<h3>公告查詢:</h3>
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

<ul>
  <li><a href='<%=request.getContextPath()%>/back/post/listAllPost.jsp'>查詢全部公告</a></li> <br>
  
  <li>
    <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/back/post/post.do" >
        <b>輸入公告編號 (如1001):</b>
        <input type="text" name="pid" class="form-control" style="width:100px">
        <input type="submit" value="送出" class="btn btn-info">
        <input type="hidden" name="action" value="getOne_For_Display">
    </FORM>
  </li>

  <jsp:useBean id="postSvc" scope="page" class="com.post.model.PostService" />
   
  <li>
     <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/back/post/post.do" >
       <b>選擇公告編號:</b>
       <select size="1" name="pid" class="form-control" style="width:100px">
         <c:forEach var="postVO" items="${postSvc.all}" > 
          <option value="${postVO.pid}">${postVO.pid}
         </c:forEach>   
       </select>
       <input type="submit" value="送出" class="btn btn-info">
       <input type="hidden" name="action" value="getOne_For_Display">
    </FORM>
  </li>
  
    <li>
     <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/back/post/post.do" >
       <b>選擇公告標題:</b>
       <select size="1" name="pid" class="form-control" style="width:600px">
         <c:forEach var="postVO" items="${postSvc.all}" > 
          <option value="${postVO.pid}">${postVO.title}
         </c:forEach>   
       </select>
       <input type="submit" value="送出" class="btn btn-info">
       <input type="hidden" name="action" value="getOne_For_Display">
    </FORM>
  </li>
  
   <jsp:useBean id="empSvc" scope="page" class="com.emp.model.EmpService" />
  
  <li>
     <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/back/post/post.do" >
       <b><font color=orange>選擇發佈員工:</font></b>
       <select size="1" name="empid" class="form-control" style="width:100px">
         <c:forEach var="empVO" items="${empSvc.all}" > 
          <option value="${empVO.empid}">${empVO.ename}
         </c:forEach>   
       </select>
       <input type="submit" value="送出" class="btn btn-info">
       <input type="hidden" name="action" value="listPosts_ByEmpid">
     </FORM>
  </li>
</ul>


<h3>公告新增</h3>

<ul>
  <li><a href='<%=request.getContextPath()%>/back/post/addPost.jsp'>Add</a> a new Post.</li>
</ul>



</body>

</html>
