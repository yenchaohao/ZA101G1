package com.init;

import com.member.model.*;
import com.tool.MailService;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.goods.model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Schedul
 */
@WebServlet(urlPatterns = { "/Schedul" }, loadOnStartup = 1)
public class GoodsOutStoreSchedul extends HttpServlet implements Runnable {
	private static final long serialVersionUID = 1L;

	//判斷是否有信件需要寄出(是否有商品下架)
	boolean isSend = false;
	//寄信的執行序
	Thread mail_service;
	// 存地址的List
	private List<String> toList = new ArrayList<>();
	// 存主題的List
	private List<String> subjectList = new ArrayList<>();
	// 存內容的List
	private List<String> contentList = new ArrayList<>();
	//給排程知道現在的時間,和商品的下架日期做計算如果超過就下架
	Calendar cal = Calendar.getInstance();
	//設定排程的timer
	Timer timer;
	//回報給使用者讓使用者知道商品什麼時候被下架
	Timestamp stamp;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();

		mail_service = new Thread(this);
		mail_service.setPriority(Thread.MIN_PRIORITY);

		// timertask 排程器
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<GoodsVO> list = new GoodsService().getAllAlive();
				// currentDate取得當前的時間
				Calendar cal = Calendar.getInstance();
				for (GoodsVO goods : list) {
				
					if (goods.getQuitdate() != null) {
						// 如果目前的時間 小於 下架日期
						if (cal.getTimeInMillis() > goods.getQuitdate()
								.getTime()) {
							//這邊判斷如果要寄的信已經累積到5封就跳出迴圈 (一次寄出的信不要太多怕影響效能)
							if(toList.size() > 5){
								isSend = true;
								break;
							}
							
							//取得現在時間
							Calendar current = Calendar.getInstance();
							stamp = new Timestamp(current.getTimeInMillis());
							String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(stamp.getTime())); 
							//取得該商品擁有者的資訊
							MemberVO membervo = new MemberService()
									.getOneMemberByMemberId(goods
											.getMember_id());
							
							toList.add(membervo.getEmail());
							subjectList.add("你的商品: " + goods.getG_name()
									+ "已經下架");
							contentList.add("你的商品: " + goods.getG_name()
									+ " 編號: " + goods.getGid() + " 已經於:"
									+ time + "下架");
							goods.setGoods_status(0);
							new GoodsService().updateGoodsByObject(goods);
							//System.out.println("已改商品");
							isSend = true;
						}
					}
				}
				// 開始寄信
				if (isSend)
					mail_service.start();
				//after sending resume boolean
				isSend = false;
			}
		};

		timer = new Timer();
		// 老師範例 1*60*60*1000 (時)*(分)*(秒)*(毫秒) 秒*毫秒 = 10 秒
		timer.scheduleAtFixedRate(task, cal.getTime(), 10 * 1000);
		// System.out.println("已建立排成");

	}

	@Override
	public void run() {
		/*把排程器停止並且清空
		  在寄信的時候為了避免排程同時也在進行,會繼續去資料庫找是否有下架商品進而去改變toList的大小而造成要寄的信與下架商品不同步,所以在寄信時先把排程清空結束
		  待寄信完成,排程再繼續執行
		*/
		timer.purge();
		timer.cancel();
	
		MailService mailservice = new MailService();
		// TODO Auto-generated method stub
		for (int i = 0; i < toList.size(); i++) {
			//System.out.println("寄信"+i);
			mailservice.sendMail(toList.get(i), subjectList.get(i),
					contentList.get(i));
			//如果有超過一封要寄的話
			if (i > 0) {
				//寄信連網路需要時間 停個一秒等待避免造成重復寄出
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		/* 以下寄信完成開始回復排程和執行序的動作....
		 
		 mail_service在開啟新的執行序 ,以供下一次使用*/
		mail_service = new Thread(this);
		mail_service.setPriority(Thread.MIN_PRIORITY);
		//將集合清除
		toList.clear();
		subjectList.clear();
		contentList.clear();
		//在建一個一模一樣新的task給排程 (不可以用init() 建好的 會錯誤)
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				// System.out.println("已執行排程" + (count++));
				// TODO Auto-generated method stub
				List<GoodsVO> list = new GoodsService().getAllAlive();
				// currentDate取得當前的時間
				Calendar cal = Calendar.getInstance();
				for (GoodsVO goods : list) {
					// 如果目前的時間 小於 下架日期
					if (goods.getQuitdate() != null) {
						if (cal.getTimeInMillis() > goods.getQuitdate()
								.getTime()) {

							Calendar current = Calendar.getInstance();
							stamp = new Timestamp(current.getTimeInMillis());
							String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(stamp.getTime())); 
							
							MemberVO membervo = new MemberService()
									.getOneMemberByMemberId(goods
											.getMember_id());
							toList.add(membervo.getEmail());
							subjectList.add("你的商品: " + goods.getG_name()
									+ "已經下架");
							contentList.add("你的商品: " + goods.getG_name()
									+ " 編號: " + goods.getGid() + " 已經於:"
									+ time + "下架");
							goods.setGoods_status(0);
							new GoodsService().updateGoodsByObject(goods);
							System.out.println("已寄信"+goods.getMember_id()+"和下架商品"+goods.getG_name());
							isSend = true;
						}
					}
				}
				// 開始寄信
				if (isSend)
					mail_service.start();
				//after sending resume boolean
				isSend = false;
			}
		};
		
		//建新的排程 並註冊新的task
		timer = new Timer();
		timer.schedule(task, cal.getTime(), 10 * 1000);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
		timer.cancel();
	}

	public GoodsOutStoreSchedul() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
