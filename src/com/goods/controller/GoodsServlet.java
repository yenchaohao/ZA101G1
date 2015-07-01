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
			// �p��bparts�����ɪ�����
			int picCount = 0;
			for (Part part : parts) {
				// System.out.println(part.getName());
				// ��D��
				if ("pic".equals(part.getName())) {
					picCount++;
					// �P�_�D�ϬO�_�Ū��Τ��O����
					if (getFileNameFromPart(part) != null
							&& ("jpg"
									.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0
									|| "png".compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0 || "jpeg"
									.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0)) {
						in = part.getInputStream();
					} else
						errorMessage.add("�D�Ϥ��W�Ǯ榡�����T. �榡������ JPG , JPEG , PNG");
					// �����
				} else if ("pic1".equals(part.getName())
						|| "pic2".equals(part.getName())
						|| "pic3".equals(part.getName())) {

					picCount++;
					// �P�_���ϬO�_�Ū��Τ��O����
					if (getFileNameFromPart(part) != null) {

						if (getFileNameFromPart(part) != null
								&& ("jpg"
										.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0
										|| "png".compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0 || "jpeg"
										.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0)) {
							listPic.add(part.getInputStream());
						} else
							errorMessage.add("�ӫ~����" + (picCount - 1)
									+ "�W�Ǯ榡�����T. �榡������ JPG , JPEG , PNG");
					}
				}
				// �p�G�w�g��F�|�i���ɤF,�j�鵲��
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
				errorMessage.add("�Ҧ���쬰�������,�нT���g");
			}
			try {
				price = Integer.parseInt(g_price);
			} catch (Exception ex) {
				errorMessage.add("�ӫ~����ж�J�Ʀr");
			}
			byte[] image = null;
			if (in != null)
				image = InputStreamConvertByteArray(in);
			else
				errorMessage.add("�ФW�ǥD�Ӥ�");
			// �ΨӶǦ^�e�@����
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
				errorMessage.add("�s�W�ӫ~����");
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
				errorMessage.add("��ƿ��~,�ק異��");
				RequestDispatcher view = req.getRequestDispatcher(requestURL);
				view.forward(req, res);
			}

			return;
		}
		if ("update_goods_input".equals(action)) {
			List<String> errorMessage = new LinkedList<String>();
			req.setAttribute("errorMessage", errorMessage);
			// �D�Ϫ�
			InputStream in = null;
			// �x�s�T�i���Ϫ����X
			List<InputStream> listPic = new ArrayList<>();
			Collection<Part> parts = req.getParts();
			// �p��bparts�����ɪ�����
			int picCount = 0;
			for (Part part : parts) {
				// ��D��
				if ("pic".equals(part.getName())) {
					picCount++;
					// �p�G�ϥΪ̦��W���ɮ� �M �ɮ׹��ɪ���
					if (getFileNameFromPart(part) != null
							&& ("jpg"
									.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0
									|| "png".compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0 || "jpeg"
									.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0)) {
						in = part.getInputStream();
						// �p�G�ϥΪ̦��W���ɮ� �M �ɮפ��O���ɪ���
					} else if (getFileNameFromPart(part) != null
							&& ("jpg"
									.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) != 0
									|| "png".compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) != 0 || "jpeg"
									.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) != 0))
						errorMessage.add("�ӫ~����" + (picCount - 1)
								+ "�W�Ǯ榡�����T. �榡������ JPG , JPEG , PNG");
					// �p�G�ϥΪ̨S���W�ǥ����ɮ�
					else
						continue;

					// �����
				} else if ("pic1".equals(part.getName())
						|| "pic2".equals(part.getName())
						|| "pic3".equals(part.getName())) {
					picCount++;
					// �p�G�ϥΪ̦��W���ɮ� �M �ɮ׹��ɪ���
					if (getFileNameFromPart(part) != null) {
						if (getFileNameFromPart(part) != null
								&& ("jpg"
										.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0
										|| "png".compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0 || "jpeg"
										.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0)) {

							listPic.add(part.getInputStream());
							// �p�G�ϥΪ̦��W���ɮ� �M �ɮפ��O���ɪ���
						} else if (getFileNameFromPart(part) != null
								&& ("jpg"
										.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) != 0
										|| "png".compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) != 0 || "jpeg"
										.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) != 0))
							errorMessage.add("�ӫ~����" + (picCount - 1)
									+ "�W�Ǯ榡�����T. �榡������ JPG , JPEG , PNG");
						// �p�G�ϥΪ̨S���W�ǥ����ɮ�
						else
							continue;
					}
				}
				// �p�G�w�g��F�|�i���ɤF,�j�鵲��
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
				errorMessage.add("�Ҧ����Х��T��J");
			int price = 0;
			try {
				price = Integer.parseInt(g_price);
			} catch (Exception ex) {
				errorMessage.add("�ӫ~�����J�����T,�п�J�Ʀr");
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

			// ���P�_�ϥΪ̬O�_���W�ǹϤ�

			int listPicCount = 0;
			// ���ϥu��3�i,�ҥH�]�T���j��
			for (int i = 0; i < 3; i++) {
				//���o�C�@�i���Ϫ��ק窱�A, 0 == ���� , 1 == ��s , 2==�R��
				String isChange = req.getParameter("pic" + (i + 1) + "change");
				if (isChange != null) {
					int change = Integer.parseInt(req.getParameter("pic"
							+ (i + 1) + "change"));
					// �p�G�n��s����
					if (change == 1) {
						if (listPic.size() > 0) {
							//picId �|�s�ۨ��i�Ϧbtable��PK,�p�G�S��PK�N���ϱq�Ӥ��s�b�]�N�O�n�s�W�Ӥ��O�ק�
							if (req.getParameter("picId" + (i + 1)) == null
									|| req.getParameter("picId" + (i + 1))
											.trim().length() == 0) {
								//System.out.println(listPicCount);
								new GoodsImageService().addGoodsImage(
										InputStreamConvertByteArray(listPic
												.get(listPicCount)), goodsvo
												.getGid());
							//�s�W�Ϥ�
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
			errorMessage.add(goodsvo.getG_name() + " �ק令�\");

			RequestDispatcher view = req.getRequestDispatcher(requestURL);
			view.forward(req, res);
			return;

		}// if(update_goods_input)
		if ("ChangeStatusRequest".equals(action)) {
			List<String> errorMessage = new LinkedList<String>();
			req.setAttribute("errorMessage", errorMessage);
			String requestURL = req.getParameter("requestURL");
			HttpSession session = req.getSession();

			// �p�G�ШD�O�q��ݨӪ� , (�u��show_all_goods_after_search �d�ߵ��G�������ӫ~��T��session��
			// , ��L2�����ӫ~��T���O�bjsp�����d�߸�Ʈw)
			if (requestURL.equals("/back/goods/show_all_goods.jsp")
					|| requestURL
							.equals("/back/goods/show_all_goods_after_search.jsp")
					|| requestURL.equals("/back/goods/show_report_goods.jsp")) {

				List<GoodsVO> goodslist = (List<GoodsVO>) session
						.getAttribute("goodsList");

				String[] gid = req.getParameterValues("deleteItem");
				if (gid == null || gid.length <= 0) {
					errorMessage.add("�п�ܭn�U�[�����");
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

					// �n�^�ǵ���ݭ��u�ӫ~�d�ߧR����goodslist, �d�ߵ��G�ζ��X�˨�session �̭�
					// �ҥH���u�ާ@�R����,�˦bsession�����X�]�n�R���ӵ�
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
			// ���B�P�_�ӷ|���ҤW�[���ӫ~�ƶq �O�_�W�L�W��
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
				errorMessage.add("���W�[���ӫ~�`�Ƥw�W�L���q�|�����W�[�W��(3��),�Э��s��ܩΥ[�JVIP�|��");
				RequestDispatcher view = req.getRequestDispatcher(requestURL);
				view.forward(req, res);
				return;
			}
			if (membervo.getMem_status() == 30
					&& goodlist.size() + userRequest > 10) {
				errorMessage.add("���W�[���ӫ~�`�Ƥw�W�LVIP�|�����W�[�W��(10��),�Э��s���");
				RequestDispatcher view = req.getRequestDispatcher(requestURL);
				view.forward(req, res);
				return;
			}

			// �����W���ӫ~�`��

			try {
				for (int i = 0; i < count; i++) {
					if (req.getParameter("status" + i) != null) {
						int gid = Integer.parseInt(req.getParameter("gid" + i));
						int status = Integer.parseInt(req.getParameter("status"
								+ i));
						// ���A9�N��S���n���ܪ��A
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
				errorMessage.add("�ק異��");
				RequestDispatcher view = req.getRequestDispatcher(requestURL);
				view.forward(req, res);
				return;
			}
			errorMessage.add("�ק窱�A���\");
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
			// url �d�ߦ��\��歶�� �e��
			String url = "/front/goods/show_all_goods_after_search.jsp";
			// url �d�ߦ��\��歶�� ���
			if (requestURL.equals("/back/goods/select_page.jsp"))
				url = "/back/goods/show_all_goods_after_search.jsp";

			// ��@����r�d��
			if ("searchGoodsByName".equals(action)) {

				String g_name = req.getParameter("g_name").trim();
				if (g_name.length() <= 0) {
					errorMessage.add("�п�J�ӫ~�W��");
					// �p�G�ШD�q��ݨӪ�,���~�T���^��ݬd�߭���
					if (requestURL.equals("/back/goods/select_page.jsp")) {
						RequestDispatcher view = req
								.getRequestDispatcher(requestURL);
						view.forward(req, res);
						// �p�G�ШD�q�e�ݨӪ�,���~�T���^�e�ݬd�߭���
					} else {
						RequestDispatcher view = req.getRequestDispatcher(url);
						view.forward(req, res);
					}

					return;
				}

				List<GoodsVO> goodsvo = new GoodsService().getAllMap(req
						.getParameterMap());

				if (goodsvo == null || goodsvo.size() <= 0) {
					errorMessage.add("�d�L���ӫ~");
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
			// �i���d��
			if ("CompositeQuery".equals(action)) {

				String[] member_id = null;
				// �P�_�ϥΪ̬O�_����J�d�߷|���m�W���ШD
				if (req.getParameter("mem_name") != null
						&& req.getParameter("mem_name").trim().length() != 0) {
					// �q�ϥΪ̿�J������r,�h�M��ŦX����r���|���b��
					List<MemberVO> list = new MemberService()
							.getMemberByMemberName(req.getParameter("mem_name"));
					// ���X�Ӫ��b���s��member_id�}�C
					if (list != null && list.size() > 0) {
						member_id = new String[list.size()];
						for (int i = 0; i < list.size(); i++) {
							member_id[i] = list.get(i).getMember_id();
						}
					}
				}
				// �P�_�ϥΪ̬O�_����J�����d��
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

				// �N��쪺�b�����map�̭�
				if (member_id != null)
					map.put("member_id", member_id);
				// �N�����d����map
				if (priceRange != null) {
					map.put("g_price", priceRange);
				}

				List<GoodsVO> goodsList = new GoodsService().getAllMap(map);

				if (goodsList == null || goodsList.size() == 0) {
					errorMessage.add("�d�L���ӫ~,�Э��s��J�d�߱���");
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
		String header = part.getHeader("content-disposition"); // �q�e���Ĥ@�ӽd��(����1-�򥻴���)�i�o����head����
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
