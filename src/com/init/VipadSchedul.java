package com.init;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jni.Time;






import com.goods.model.GoodsService;
import com.goods.model.GoodsVO;
import com.vipad.model.VipadService;
import com.vipad.model.VipadVO;

/**
 * Servlet implementation class VipadSchedul
 */
@WebServlet(urlPatterns = { "/VipadSchedul" }, loadOnStartup = 1)
public class VipadSchedul extends HttpServlet implements Runnable{
	private static final long serialVersionUID = 1L;
    
	List<VipadVO> vipadList = new VipadService().getAll();//取出VIP所有廣告
	List<GoodsVO> goodsList = null;//取出所有商品
	boolean isEnd = false;
	Timer timer;
	int count = 0;
	ServletContext context;
	Thread vipadThread;
	
    @Override
    public void init() throws ServletException {
    	// TODO Auto-generated method stub
    	super.init();
    	
    	vipadThread = new Thread(this);
    	vipadThread.setPriority(Thread.MIN_PRIORITY); //最小優先權
    	
    	context = getServletContext();
    	context.setAttribute("count", count);
    	Calendar cal = Calendar.getInstance();
    	// timertask 排程器
    	TimerTask task = null;
    	//--------------------------------------------------
    	
    	task = new TimerTask() {
    		
			@Override
			public void run() {
				if(context.getAttribute("isAddVipad") == null || (int)context.getAttribute("isAddVipad") == 1){
					vipadList = new VipadService().getAll();
					int start = (int)context.getAttribute("count"); //目前輪播廣告3筆
					// TODO Auto-generated method stub
					if(vipadList.size() > 0){
						
						if(isEnd){ //判斷VIP廣告全部跑完才再重新撈資料庫
							vipadList = new VipadService().getAll();
						}
						//排程每執行一次就找一次
						goodsList = new GoodsService().getAll();
						
						// currentDate取得當前的時間
						Calendar cal = Calendar.getInstance();
						Timestamp stamp = new Timestamp(cal.getTimeInMillis());
						//商品廣告下架時間
						cal.add(Calendar.HOUR, 1);
						Timestamp quitStamp = new Timestamp(cal.getTimeInMillis());
						
						for(VipadVO vipadVO : vipadList){
							vipadVO.setStatus(1);
							if(vipadVO.getJoindate() == null){
								vipadVO.setJoindate(stamp);
								vipadVO.setQuitdate(quitStamp);
							}
							for(GoodsVO goodsVO : goodsList){
								//VIP商品時間到期或是該商品交易完成(狀態3)或該商品被刪除(狀態2)，VIP廣告都要被移除
								if(vipadVO.getQuitdate() != null && Calendar.getInstance().getTimeInMillis() >= vipadVO.getQuitdate().getTime()
										|| (goodsVO.getGid().equals(vipadVO.getGid()) && goodsVO.getGoods_status() == 2) 
										|| (goodsVO.getGid().equals(vipadVO.getGid()) && goodsVO.getGoods_status() == 3)){
									new VipadService().deleteVipad(vipadVO);
									continue;
								}
							}
							new VipadService().updateVipadByObject(vipadVO);
							start++;
							if(start == vipadList.size()){
								isEnd = true;
								context.setAttribute("count", 0);
							}
						}
						
					}else{
						//如果一開始啟動的時候list是0,每執行一次排程就重新找一次
						vipadList = new VipadService().getAll();
					}
				}
				context.setAttribute("isAddVipad", 0);
    		}
		};
    	//------------------------------------------------------------
		
		
		timer = new Timer(); 
		timer.scheduleAtFixedRate(task, cal.getTime(), 2*1000);
    }
    
    @Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
    
    @Override
    public void destroy() {
    	// TODO Auto-generated method stub
    	super.destroy();
    	timer.cancel();
    }
	
    public VipadSchedul() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	

}
