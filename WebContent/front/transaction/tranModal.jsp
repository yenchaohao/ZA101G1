<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 <%@ page import="com.tran.model.*"%>
  <%@ page import="com.member.model.*"%>
   <%@ page import="com.goods.model.*"%>
 <%@ page import="java.util.*"%>
 <%@ page import="java.text.*"%>
 <%
 			Integer tid=Integer.valueOf(request.getParameter("tid"));
 			String source=request.getParameter("source");
 			TranService tranSvc=new TranService();
 			MemberService memSvc=new MemberService();
 			TranVO tranVO=tranSvc.getOneTran(tid);
 			//getMaxPrice
 			Integer res_gid=tranVO.getRes_gid();
 			Integer req_gid=tranVO.getReq_gid();
 			GoodsService goodsSvc=new GoodsService();
 			GoodsVO res_good=goodsSvc.findGoodsByGid(res_gid);
 			GoodsVO req_good=goodsSvc.findGoodsByGid(req_gid);
 			Integer pendPoint=Math.max(res_good.getG_price(), req_good.getG_price());
 			//期限
 			Calendar cal=Calendar.getInstance();
 			cal.setTime(tranVO.getRes_date());
 			cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)+1);
 			SimpleDateFormat df=new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
			String deadline=df.format(cal.getTime());
 			
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
</head>
<body>



         <div class="modal-header">
            <button type="button" class="close" 
               data-dismiss="modal" aria-hidden="true">
                  &times;
            </button>

            <h4 class="modal-title" id="myModalLabel">
              鎖定中的交易
            </h4>
         </div>
		
		
		<% if(source.equals("/front/transaction/receive.jsp")){ %>
         <div class="modal-body">
         	你目前被扣押<%= pendPoint %>點<br>
            因對方點數不足，因此目前交易鎖定等待<%= memSvc.getOneMemberByMemberId(tranVO.getReq_member_id()).getMem_name() %>儲值<br>
        	若<%= deadline %>前對方尚未確認交易，系統將自動取消
		</div>
          <div class="modal-footer">
            <form action="<%= request.getContextPath() %>/front/transaction/tran.do"" method="post" style="display:inline;float:left;">
            <input type="submit" class="btn btn-danger" value="取消交易">
            <input type="hidden" name="action" value="del_tran">
            <input type="hidden" name="tid" value="<%= tid%>">
            <input type="hidden" name="member_id" value="<%=session.getAttribute("member_id")%>"> 
            <input type="hidden" name="source" value="<%= source%>">	          
            </form>
            <button type="button" class="btn btn-default" 
               data-dismiss="modal">關閉
            </button>
         </div>
		<% }else{ %>
		 <div class="modal-body">
         		因為您的點數不足，所以目前交易鎖定中<br>
         		請儲值<%= pendPoint %>點，然後再按下面的確認交易<br>
         		若<%= deadline %>前尚未確認交易，系統將自動取消
         </div>
          <div class="modal-footer">
          <form action="<%= request.getContextPath() %>/front/transaction/tran.do"" method="post" style="display:inline;float:left;">
            <input type="submit" class="btn btn-danger" value="取消交易">
            <input type="hidden" name="action" value="del_tran">
            <input type="hidden" name="tid" value="<%= tid%>">
            <input type="hidden" name="member_id" value="<%=session.getAttribute("member_id")%>"> 
            <input type="hidden" name="source" value="<%= source%>">	          
            </form>
            <button type="button" class="btn btn-default"   data-dismiss="modal">
            	關閉
            </button>
            <form action="<%= request.getContextPath() %>/front/transaction/tran.do"" method="post" style="display:inline;">
            <input type="submit" class="btn btn-primary" value="確認交易">
            <input type="hidden" name="action" value="fillTran">
            <input type="hidden" name="tid" value="<%= tid%>">
            <input type="hidden" name="member_id" value="<%=session.getAttribute("member_id")%>">
            
            </form>
         </div>
		<% } %>
        

</body>
</html>