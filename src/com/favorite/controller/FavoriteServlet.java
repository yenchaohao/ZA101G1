package com.favorite.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.favorite.model.FavoriteService;
import com.favorite.model.FavoriteVO;
import com.goods.model.GoodsService;
import com.goods.model.GoodsVO;

/**
 * Servlet implementation class FavoriteServlet
 */
@WebServlet("/FavoriteServlet")
public class FavoriteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	public void process(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		res.setContentType("text/html ; charset=UTF-8");
    	req.setCharacterEncoding("UTF-8");
    	PrintWriter out = res.getWriter();
    	ServletContext context = req.getServletContext();
    	
    	List<String> errorMessage = new LinkedList<String>();
		req.setAttribute("errorMessage", errorMessage);
		
		HttpSession session = req.getSession();
		String member_id = (String)session.getAttribute("member_id");
		
		String action = req.getParameter("action");
		
		if("addFavorite".equals(action)){ //來自goods_detail.jsp的請求
			
			int gid = Integer.parseInt(req.getParameter("gid"));
			
			if(member_id == null){
				RequestDispatcher view = req
						.getRequestDispatcher("/front/member/member_login.jsp");
				view.forward(req, res);
				return;
			}
			
			FavoriteService favSvc = new FavoriteService();
			List<FavoriteVO> favList = favSvc.getAllByMember(member_id);
			boolean result = false;
			if(favList.size() == 0){
				//收藏的context
					context.setAttribute("isAddFavorite",1 );
					favSvc.addFavorite(gid, member_id);
			} else {
				for(FavoriteVO favVO : favList){
					if(favVO.getGid().equals(gid)){
						errorMessage.add("此商品已加入收藏");
						result = true;
						break;
					} 
				}
				if(result == false){
					favSvc.addFavorite(gid, member_id);
				}
			}
			
			if (errorMessage.size() > 0) {
				RequestDispatcher error = req
						.getRequestDispatcher("/front/goods/goods_detail.jsp");
				error.forward(req, res);
				return;
			}
			
			GoodsService goodsSvc = new GoodsService();
			GoodsVO goodsVO = goodsSvc.findGoodsByGid(gid);
			goodsVO.setG_hot(goodsVO.getG_hot()+1);
			goodsSvc.updateGoodsByObject(goodsVO);
			
			errorMessage.add("此商品加入收藏成功");
			RequestDispatcher view = req
					.getRequestDispatcher("/front/goods/goods_detail.jsp?gid="+gid);
			view.forward(req, res);
		}
		
		if("deleteFavorite".equals(action)){ //來自showFavorite.jsp的請求
			
			String[] boxs = req.getParameterValues("box");
			
			if(boxs == null){
				errorMessage.add("請選擇刪除選項");
			}
			
			if (errorMessage.size() > 0) {
				RequestDispatcher error = req
						.getRequestDispatcher("/front/favorite/showFavorite.jsp");
				error.forward(req, res);
				return;
			}
			
			FavoriteService favSvc = new FavoriteService();
			int fid = 0;
			GoodsService goodsSvc = new GoodsService();
			GoodsVO goodsVO;
			int gid = 0;
			for(String box : boxs){
				fid = Integer.parseInt(box);
				gid = Integer.parseInt(req.getParameter("gid"+fid)); //取得商品收藏的gid
				goodsVO = goodsSvc.findGoodsByGid(gid);
				goodsVO.setG_hot(goodsVO.getG_hot()-1); //商品收藏度-1
				goodsSvc.updateGoodsByObject(goodsVO);
				favSvc.deleteFavorite(fid); //刪除剛選擇的我的最愛商品
			}
			context.setAttribute("isAddFavorite",1);
			RequestDispatcher view = req
					.getRequestDispatcher("/front/favorite/showFavorite.jsp");
			view.forward(req, res);
		}
	}
	
	
    public FavoriteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		process(req, res);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		process(req, res);
	}

}
