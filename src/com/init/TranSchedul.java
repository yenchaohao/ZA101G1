package com.init;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goods.model.GoodsService;
import com.goods.model.GoodsVO;
import com.member.model.MemberService;
import com.member.model.MemberVO;
import com.tran.model.*;
 
/** 
 * Servlet implementation class TranSchedul
 */
@WebServlet(urlPatterns = { "/TranSchedul" }, loadOnStartup = 1)
public class TranSchedul extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public TranSchedul() {
        super();
        // TODO Auto-generated constructor stub
    }
    Timer timer = new Timer(); 
	public void init(ServletConfig config) throws ServletException {
		final TranService tranSvc=new TranService();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				//System.out.println("run");
				List<TranVO> list=tranSvc.getAll();
				for(TranVO tranVO:list){
					if(tranVO.getStatus().equals(3)){
						if((new Date().getTime()-tranVO.getRes_date().getTime())>(60*10000)){
							tranVO.setStatus(2);
							tranSvc.updateTranByObject(tranVO);
							//ÁÙ¿ú
							Integer pendPoint=getMaxPrice(tranVO); 
							returnPoint(pendPoint, tranVO.getRes_member_id());
						} 
					}
				}
			}
		};


		timer.scheduleAtFixedRate(task,new Date(), 1 * 1000);
	}
	public void destroy() {
		timer.cancel();
	}
	private void returnPoint(Integer pendPoint,String member_id){
		MemberService memSvc=new  MemberService();
		MemberVO memVO=memSvc.getOneMemberByMemberId(member_id);
		memVO.setPending_p(memVO.getPending_p()-pendPoint);
		memVO.setHaving_p(memVO.getHaving_p()+pendPoint);
		memSvc.updateMemberByObject(memVO);
	}
	private Integer getMaxPrice(TranVO tranVO){
		Integer res_gid=tranVO.getRes_gid();
		Integer req_gid=tranVO.getReq_gid();
		GoodsService goodsSvc=new GoodsService();
		GoodsVO res_good=goodsSvc.findGoodsByGid(res_gid);
		GoodsVO req_good=goodsSvc.findGoodsByGid(req_gid);
		Integer pendPoint=Math.max(res_good.getG_price(), req_good.getG_price());
		return pendPoint;
	}

}
