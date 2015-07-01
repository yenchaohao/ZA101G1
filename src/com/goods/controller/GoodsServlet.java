package com.goods.controller;

import com.goodsimage.model.*;
import com.goods.model.*;
import com.member.model.MemberService;
import com.member.model.MemberVO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 * Servlet implementation class GoodsServlet
 */

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024, maxRequestSize = 5 * 5 * 1024 * 1024)
public class GoodsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public GoodsServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	private void process(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		res.setContentType("text/html ; charset=UTF-8 ");
		req.setCharacterEncoding("UTF-8");
		PrintWriter out = res.getWriter();
		String action = req.getParameter("action");

		if ("addGoods".equals(action)) {
			List<String> errorMessage = new LinkedList<String>();
			InputStream in = null;
			List<InputStream> listPic = new ArrayList<>();
			Collection<Part> parts = req.getParts();
			// 計算在parts找到圖檔的次數
			int picCount = 0;
			for (Part part : parts) {
				// System.out.println(part.getName());
				// 找主圖
				if ("pic".equals(part.getName())) {
					picCount++;
					// 判斷主圖是否空直或不是圖檔
					if (getFileNameFromPart(part) != null
							&& ("jpg"
									.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0
									|| "png".compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0 || "jpeg"
									.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0)) {
						in = part.getInputStream();
					} else
						errorMessage.add("主圖片上傳格式不正確. 格式必須為 JPG , JPEG , PNG");
					// 找附圖
				} else if ("pic1".equals(part.getName())
						|| "pic2".equals(part.getName())
						|| "pic3".equals(part.getName())) {

					picCount++;
					// 判斷附圖是否空直或不是圖檔
					if (getFileNameFromPart(part) != null) {

						if (getFileNameFromPart(part) != null
								&& ("jpg"
										.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0
										|| "png".compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0 || "jpeg"
										.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0)) {
							listPic.add(part.getInputStream());
						} else
							errorMessage.add("商品附圖" + (picCount - 1)
									+ "上傳格式不正確. 格式必須為 JPG , JPEG , PNG");
					}
				}
				// 如果已經找了四張圖檔了,迴圈結束
				if (picCount > 3)
					break;

			}// forEach_parts

			String member_id = (String) req.getSession().getAttribute(
					"member_id");
			String groupid = req.getParameter("groupid");
			String g_name = req.getParameter("g_name");
			String g_describe = req.getParameter("g_describe");
			String g_price = req.getParameter("g_price");
			String g_level = req.getParameter("g_level");

			int price = 0;
			if (g_name.length() <= 0 || g_describe.length() <= 0
					|| g_price.length() <= 0) {
				errorMessage.add("所有欄位為必填欄位,請確實填寫");
			}
			try {
				price = Integer.parseInt(g_price);
			} catch (Exception ex) {
				errorMessage.add("商品價格請填入數字");
			}
			byte[] image = null;
			if (in != null)
				image = InputStreamConvertByteArray(in);
			else
				errorMessage.add("請上傳主照片");
			// 用來傳回前一頁的
			GoodsVO goodsvo = new GoodsVO();
			goodsvo.setGroupid(Integer.parseInt(groupid));
			goodsvo.setG_name(g_name);
			goodsvo.setG_describe(g_describe);
			goodsvo.setG_price(price);
			goodsvo.setG_level(Integer.parseInt(g_level));

			if (errorMessage.size() > 0) {

				req.setAttribute("GoodsVO", goodsvo);
				req.setAttribute("errorMessage", errorMessage);
				RequestDispatcher view = req
						.getRequestDispatcher("/front/goods/addGoods.jsp");
				view.forward(req, res);
				return;

			}

			int pk = new GoodsService().addGoodsGetPrimaryKey(
					Integer.parseInt(groupid), member_id, g_name, g_describe,
					price, Integer.parseInt(g_level), 0, null, null, image, 0);
			if (pk == 0) {
				errorMessage.add("新增商品失敗");
				req.setAttribute("GoodsVo", goodsvo);
				req.setAttribute("errorMessage", errorMessage);
				RequestDispatcher error = req
						.getRequestDispatcher("/front/member/addGoods.jsp");
				error.forward(req, res);
				return;
			}

			if (listPic.size() > 0) {
				for (InputStream pic : listPic) {
					new GoodsImageService().addGoodsImage(
							InputStreamConvertByteArray(pic), pk);
				}
			}

			RequestDispatcher view = req
					.getRequestDispatcher("/front/goods/show_goods_by_member.jsp");
			view.forward(req, res);
			return;

		}// if("addGoods".equals(action))

		if ("update_goods".equals(action)) {

			List<String> errorMessage = new LinkedList<String>();
			req.setAttribute("errorMessage", errorMessage);
			String requestURL = req.getParameter("requestURL").trim();
			try {

				int gid = Integer.parseInt(req.getParameter("gid"));
				GoodsVO goodsvo = new GoodsService().findGoodsByGid(gid);
				req.setAttribute("GoodsVO", goodsvo);
				RequestDispatcher view = req
						.getRequestDispatcher("/front/goods/update_goods_input.jsp");
				view.forward(req, res);

			} catch (Exception ex) {
				errorMessage.add("資料錯誤,修改失敗");
				RequestDispatcher view = req.getRequestDispatcher(requestURL);
				view.forward(req, res);
			}

			return;
		}
		if ("update_goods_input".equals(action)) {
			List<String> errorMessage = new LinkedList<String>();
			req.setAttribute("errorMessage", errorMessage);
			// 主圖的
			InputStream in = null;
			// 儲存三張附圖的集合
			List<InputStream> listPic = new ArrayList<>();
			Collection<Part> parts = req.getParts();
			// 計算在parts找到圖檔的次數
			int picCount = 0;
			for (Part part : parts) {
				// 找主圖
				if ("pic".equals(part.getName())) {
					picCount++;
					// 如果使用者有上傳檔案 和 檔案圖檔的話
					if (getFileNameFromPart(part) != null
							&& ("jpg"
									.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0
									|| "png".compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0 || "jpeg"
									.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0)) {
						in = part.getInputStream();
						// 如果使用者有上傳檔案 和 檔案不是圖檔的話
					} else if (getFileNameFromPart(part) != null
							&& ("jpg"
									.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) != 0
									|| "png".compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) != 0 || "jpeg"
									.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) != 0))
						errorMessage.add("商品附圖" + (picCount - 1)
								+ "上傳格式不正確. 格式必須為 JPG , JPEG , PNG");
					// 如果使用者沒有上傳任何檔案
					else
						continue;

					// 找附圖
				} else if ("pic1".equals(part.getName())
						|| "pic2".equals(part.getName())
						|| "pic3".equals(part.getName())) {
					picCount++;
					// 如果使用者有上傳檔案 和 檔案圖檔的話
					if (getFileNameFromPart(part) != null) {
						if (getFileNameFromPart(part) != null
								&& ("jpg"
										.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0
										|| "png".compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0 || "jpeg"
										.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0)) {

							listPic.add(part.getInputStream());
							// 如果使用者有上傳檔案 和 檔案不是圖檔的話
						} else if (getFileNameFromPart(part) != null
								&& ("jpg"
										.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) != 0
										|| "png".compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) != 0 || "jpeg"
										.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) != 0))
							errorMessage.add("商品附圖" + (picCount - 1)
									+ "上傳格式不正確. 格式必須為 JPG , JPEG , PNG");
						// 如果使用者沒有上傳任何檔案
						else
							continue;
					}
				}
				// 如果已經找了四張圖檔了,迴圈結束
				if (picCount > 3)
					break;

			}// forEach_parts

			String requestURL = req.getParameter("requestURL");

			String gid = req.getParameter("gid").trim();
			String groupid = req.getParameter("groupid").trim();
			String g_name = req.getParameter("g_name").trim();
			String g_describe = req.getParameter("g_describe").trim();
			String g_price = req.getParameter("g_price").trim();
			String g_level = req.getParameter("g_level").trim();

			if (g_name.length() <= 0 || g_describe.length() <= 0
					|| g_price.length() <= 0)
				errorMessage.add("所有欄位請正確輸入");
			int price = 0;
			try {
				price = Integer.parseInt(g_price);
			} catch (Exception ex) {
				errorMessage.add("商品價格輸入不正確,請輸入數字");
			}

			GoodsVO goodsvo = new GoodsService().findGoodsByGid(Integer
					.parseInt(req.getParameter("gid")));
			goodsvo.setGroupid(Integer.parseInt(groupid));
			goodsvo.setG_name(g_name);
			goodsvo.setG_describe(g_describe);
			goodsvo.setG_price(price);
			goodsvo.setG_level(Integer.parseInt(g_level));

			if (in != null)
				goodsvo.setPic(InputStreamConvertByteArray(in));

			// 先判斷使用者是否有上傳圖片

			int listPicCount = 0;
			// 附圖只有3張,所以跑三次迴圈
			for (int i = 0; i < 3; i++) {
				//取得每一張附圖的修改狀態, 0 == 不變 , 1 == 更新 , 2==刪除
				String isChange = req.getParameter("pic" + (i + 1) + "change");
				if (isChange != null) {
					int change = Integer.parseInt(req.getParameter("pic"
							+ (i + 1) + "change"));
					// 如果要更新的話
					if (change == 1) {
						if (listPic.size() > 0) {
							//picId 會存著那張圖在table的PK,如果沒有PK代表此圖從來不存在也就是要新增而不是修改
							if (req.getParameter("picId" + (i + 1)) == null
									|| req.getParameter("picId" + (i + 1))
											.trim().length() == 0) {
								//System.out.println(listPicCount);
								new GoodsImageService().addGoodsImage(
										InputStreamConvertByteArray(listPic
												.get(listPicCount)), goodsvo
												.getGid());
							//新增圖片
							} else {
								int picId = Integer.parseInt(req
										.getParameter("picId" + (i + 1)));
								GoodsImageVO goodsImagevo = new GoodsImageService()
										.findGoodsImageByPicNumber(picId);
								goodsImagevo
										.setPic(InputStreamConvertByteArray(listPic
												.get(listPicCount)));
								new GoodsImageService()
										.updateGoodsImageByObject(goodsImagevo);
							}
						}
						listPicCount++;
					}
					if (change == 2) {
						if (req.getParameter("picId" + (i + 1)) != null
								&& req.getParameter("picId" + (i + 1)).trim()
										.length() != 0) {
							int picId = Integer.parseInt(req
									.getParameter("picId" + (i + 1)));
							new GoodsImageService()
									.deleteGoodsImageByPic_number(picId);
						}
					}

				}// if (isChange != null)

			}// for (int i = 0; i < 3; i++)

			req.setAttribute("GoodsVO", goodsvo);
			if (errorMessage.size() > 0) {
				RequestDispatcher view = req
						.getRequestDispatcher("/front/goods/update_goods_input.jsp");
				view.forward(req, res);
				return;
			}

			new GoodsService().updateGoodsByObject(goodsvo);
			errorMessage.add(goodsvo.getG_name() + " 修改成功");

			RequestDispatcher view = req.getRequestDispatcher(requestURL);
			view.forward(req, res);
			return;

		}// if(update_goods_input)
		if ("ChangeStatusRequest".equals(action)) {
			List<String> errorMessage = new LinkedList<String>();
			req.setAttribute("errorMessage", errorMessage);
			String requestURL = req.getParameter("requestURL");
			HttpSession session = req.getSession();

			// 如果請求是從後端來的 , (只有show_all_goods_after_search 查詢結果頁面的商品資訊用session裝
			// , 其他2頁的商品資訊都是在jsp直接查詢資料庫)
			if (requestURL.equals("/back/goods/show_all_goods.jsp")
					|| requestURL
							.equals("/back/goods/show_all_goods_after_search.jsp")
					|| requestURL.equals("/back/goods/show_report_goods.jsp")) {

				List<GoodsVO> goodslist = (List<GoodsVO>) session
						.getAttribute("goodsList");

				String[] gid = req.getParameterValues("deleteItem");
				if (gid == null || gid.length <= 0) {
					errorMessage.add("請選擇要下架的欄位");
					RequestDispatcher view = req
							.getRequestDispatcher(requestURL);
					view.forward(req, res);
					return;
				}
				for (String id : gid) {
					GoodsVO goodsvo = new GoodsService().findGoodsByGid(Integer
							.parseInt(id));
					goodsvo.setG_hot(0);
					goodsvo.setGoods_status(2);
					new GoodsService().updateGoodsByObject(goodsvo);

					// 要回傳給後端員工商品查詢刪除的goodslist, 查詢結果用集合裝到session 裡面
					// 所以員工操作刪除後,裝在session的集合也要刪除該筆
					if (goodslist != null && goodslist.size() > 0) {
						for (int i = 0; i < goodslist.size(); i++) {
							if (goodslist.get(i).getGid() == Integer
									.parseInt(id)) {
								goodslist.remove(i);
							}
						}
					}

				}
				if (goodslist != null && goodslist.size() > 0)
					session.setAttribute("goodsList", goodslist);

				RequestDispatcher view = req.getRequestDispatcher(requestURL);
				view.forward(req, res);
				return;

			}
			int userRequest = 0;
			int count = Integer.parseInt(req.getParameter("count"));

			//
			// 此處判斷該會員所上架的商品數量 是否超過上限
			String member_id = (String) req.getSession().getAttribute(
					"member_id");
			MemberVO membervo = new MemberService()
					.getOneMemberByMemberId(member_id);
			List<GoodsVO> goodlist = new GoodsService()
					.findGoodsByMember_idAlive(member_id);

			for (int i = 0; i < count; i++) {
				if (req.getParameter("status" + i) != null) {
					int status = Integer.parseInt(req
							.getParameter("status" + i));
					if (status == 1)
						++userRequest;
					if (status == 0 && userRequest >= 0)
						--userRequest;
				}
			}

			if (membervo.getMem_status() == 20
					&& goodlist.size() + userRequest > 3) {
				errorMessage.add("欲上架的商品總數已超過普通會員的上架上限(3件),請重新選擇或加入VIP會員");
				RequestDispatcher view = req.getRequestDispatcher(requestURL);
				view.forward(req, res);
				return;
			}
			if (membervo.getMem_status() == 30
					&& goodlist.size() + userRequest > 10) {
				errorMessage.add("欲上架的商品總數已超過VIP會員的上架上限(10件),請重新選擇");
				RequestDispatcher view = req.getRequestDispatcher(requestURL);
				view.forward(req, res);
				return;
			}

			// 頁面上的商品總數

			try {
				for (int i = 0; i < count; i++) {
					if (req.getParameter("status" + i) != null) {
						int gid = Integer.parseInt(req.getParameter("gid" + i));
						int status = Integer.parseInt(req.getParameter("status"
								+ i));
						// 狀態9代表沒有要改變狀態
						if (status != 9) {

							GoodsVO goodsvo = new GoodsService()
									.findGoodsByGid(gid);
							goodsvo.setGoods_status(status);
							java.util.Date du = new java.util.Date();
							Timestamp joindate = new Timestamp(du.getTime());

							if (status == 1) {
								// System.out.println("up");
								goodsvo.setJoindate(joindate);
								Calendar cal = Calendar.getInstance();
								cal.setTime(joindate);
								cal.add(Calendar.DAY_OF_WEEK, 7);
								Timestamp quitdate = new Timestamp(
										cal.getTimeInMillis());
								goodsvo.setQuitdate(quitdate);
							} else if (status == 0) {
								goodsvo.setG_hot(0);
								goodsvo.setJoindate(null);
								goodsvo.setQuitdate(null);
							}

							new GoodsService().updateGoodsByObject(goodsvo);
						}
					}
				}

			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				errorMessage.add("修改失敗");
				RequestDispatcher view = req.getRequestDispatcher(requestURL);
				view.forward(req, res);
				return;
			}
			errorMessage.add("修改狀態成功");
			RequestDispatcher view = req.getRequestDispatcher(requestURL);
			view.forward(req, res);
			return;

		}
		if ("searchGoodsByName".equals(action)
				|| "CompositeQuery".equals(action)) {
			List<String> errorMessage = new LinkedList<String>();
			HttpSession session = req.getSession();
			req.setAttribute("errorMessage", errorMessage);
			String requestURL = req.getParameter("requestURL");
			// url 查詢成功轉交頁面 前端
			String url = "/front/goods/show_all_goods_after_search.jsp";
			// url 查詢成功轉交頁面 後端
			if (requestURL.equals("/back/goods/select_page.jsp"))
				url = "/back/goods/show_all_goods_after_search.jsp";

			// 單一關鍵字查詢
			if ("searchGoodsByName".equals(action)) {

				String g_name = req.getParameter("g_name").trim();
				if (g_name.length() <= 0) {
					errorMessage.add("請輸入商品名稱");
					// 如果請求從後端來的,錯誤訊息回後端查詢頁面
					if (requestURL.equals("/back/goods/select_page.jsp")) {
						RequestDispatcher view = req
								.getRequestDispatcher(requestURL);
						view.forward(req, res);
						// 如果請求從前端來的,錯誤訊息回前端查詢頁面
					} else {
						RequestDispatcher view = req.getRequestDispatcher(url);
						view.forward(req, res);
					}

					return;
				}

				List<GoodsVO> goodsvo = new GoodsService().getAllMap(req
						.getParameterMap());

				if (goodsvo == null || goodsvo.size() <= 0) {
					errorMessage.add("查無此商品");
					session.setAttribute("goodsList", goodsvo);
					if (requestURL.equals("/back/goods/select_page.jsp")) {
						RequestDispatcher view = req
								.getRequestDispatcher(requestURL);
						view.forward(req, res);
					} else {
						RequestDispatcher view = req.getRequestDispatcher(url);
						view.forward(req, res);
					}
					return;
				}

				session.setAttribute("goodsList", goodsvo);
				RequestDispatcher view = req.getRequestDispatcher(url);
				view.forward(req, res);
				return;

			}
			// 進階查詢
			if ("CompositeQuery".equals(action)) {

				String[] member_id = null;
				// 判斷使用者是否有輸入查詢會員姓名的請求
				if (req.getParameter("mem_name") != null
						&& req.getParameter("mem_name").trim().length() != 0) {
					// 從使用者輸入的關鍵字,去尋找符合關鍵字的會員帳號
					List<MemberVO> list = new MemberService()
							.getMemberByMemberName(req.getParameter("mem_name"));
					// 把找出來的帳號存到member_id陣列
					if (list != null && list.size() > 0) {
						member_id = new String[list.size()];
						for (int i = 0; i < list.size(); i++) {
							member_id[i] = list.get(i).getMember_id();
						}
					}
				}
				// 判斷使用者是否有輸入價錢範圍
				String[] priceRange = null;
				if (req.getParameter("start_price") != null
						&& req.getParameter("start_price").trim().length() != 0
						&& req.getParameter("end_price") != null
						&& req.getParameter("end_price").trim().length() != 0) {
					priceRange = new String[2];
					priceRange[0] = req.getParameter("start_price");
					priceRange[1] = req.getParameter("end_price");
				}

				HashMap<String, String[]> map = new HashMap<String, String[]>();
				map.putAll(req.getParameterMap());

				// 將找到的帳號塞到map裡面
				if (member_id != null)
					map.put("member_id", member_id);
				// 將價錢範圍塞到map
				if (priceRange != null) {
					map.put("g_price", priceRange);
				}

				List<GoodsVO> goodsList = new GoodsService().getAllMap(map);

				if (goodsList == null || goodsList.size() == 0) {
					errorMessage.add("查無此商品,請重新輸入查詢條件");
					 goodsList = new GoodsService().getAllAlive();
					if (requestURL.equals("/back/goods/select_page.jsp")) {
						session.setAttribute("goodsList", goodsList);
						RequestDispatcher view = req
								.getRequestDispatcher(requestURL);
						view.forward(req, res);
						return;
					}

				}

				session.setAttribute("goodsList", goodsList);
				RequestDispatcher view = req.getRequestDispatcher(url);
				view.forward(req, res);
			}

		}

	}// void process

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		process(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		process(request, response);
	}

	public String getFileNameFromPart(Part part) {
		String header = part.getHeader("content-disposition"); // 從前面第一個範例(版本1-基本測試)可得知此head的值
		// System.out.println("part.getHeader('content-disposition'): " +
		// header);
		String filename = header.substring(header.lastIndexOf("=") + 2,
				header.length() - 1);
		// System.out.println(filename);
		if (filename.length() == 0) {
			return null;
		}
		return filename;
	}

	private String getFileFormat(String fileName) {
		int dotpos = fileName.indexOf('.');
		String format = fileName.substring(dotpos + 1);
		return format;
	}

	private byte[] InputStreamConvertByteArray(InputStream in) {
		byte[] buffer = new byte[4 * 1024];
		try {
			ByteArrayOutputStream byteout = new ByteArrayOutputStream();
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				byteout.write(buffer, 0, len);
			}
			byteout.flush();

			return byteout.toByteArray();

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;

	}

}
