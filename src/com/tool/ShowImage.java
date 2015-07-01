package com.tool;

import com.goodsimage.model.*;
import com.businessrefinery.barcode.QRCode;
import com.emp.model.*;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.post.model.*;
import com.goods.model.GoodsDAO;
import com.goods.model.GoodsService;
import com.goods.model.GoodsVO;
import com.member.model.MemberDAO;
import com.member.model.MemberService;
import com.member.model.MemberVO;

/**
 * Servlet implementation class ShowImage
 */
@WebServlet("/ShowImage")
public class ShowImage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ShowImage() {
		super();
		// TODO Auto-generated constructor stub
	}

	private void process(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		res.setContentType("image/gif");
		String action = req.getParameter("action");
		ServletOutputStream out = res.getOutputStream();
		if ("goods".equals(action)) {
			String gid = (String) req.getParameter("gid");
			GoodsVO goodsvo = new GoodsService().findGoodsByGid(Integer.parseInt(gid));
			byte[] pic = goodsvo.getPic();
			String size = req.getParameter("size");
			if(req.getParameter("size") != null){
				try{
				pic = shrink(pic, Integer.parseInt(req.getParameter("size"))); 
				}catch(Exception ex){
					System.out.println(ex.getMessage());
				}
			}
			out.write(pic);
			out.flush();
			out.close();
			return;
		
		}
		if("member".equals(action)){
			String member_id = (String) req.getParameter("member_id");
			MemberVO membervo = new MemberService().getOneMemberByMemberId(member_id);
			byte[] pic = membervo.getPic();
			if(req.getParameter("size") != null){
				try{
				pic = shrink(pic, Integer.parseInt(req.getParameter("size"))); 
				}catch(Exception ex){
					System.out.println(ex.getMessage());
				}
			}
			out.write(pic);
			out.flush();
			out.close();
			return;
		}
		if("emp".equals(action)){
			String empid = req.getParameter("empid");
	    	EmpService empSvc = new EmpService();
	    	EmpVO empVO = empSvc.getOneEmp(empid);
	    	byte[] pic = empVO.getPic();
	    	out.write(pic);
	    	out.flush();
	    	out.close();
			return;
		}
		if("goodsImage".equals(action)){
			String pic_number= (String) req.getParameter("pic_number");
			GoodsImageVO goods_image_vo = new GoodsImageService().findGoodsImageByPicNumber(Integer.parseInt(pic_number));
			byte[] pic = goods_image_vo.getPic();
			String size = req.getParameter("size");
			if(size != null && size.trim().length() != 0){
				int sizeInt = Integer.parseInt(req.getParameter("size"));
				out.write(shrink(pic,sizeInt));
				out.flush();
				out.close();
				return;
			}
			out.write(pic);
			out.flush();
			out.close();
			return;
		}
		if("showVipadPic".equals(action)){
	    	String gid = req.getParameter("gid");
	    	GoodsService goodsSvc = new GoodsService();
	    	GoodsVO goodsVO = goodsSvc.findGoodsByGid(Integer.parseInt(gid));
	    	byte[] pic = goodsVO.getPic();
	    	out.write(pic);
	    	out.flush();
	    	out.close();
    	}
		if("post".equals(action)){
	    	String pid = req.getParameter("pid");
	    	PostVO postVO = new PostService().getOnePost(Integer.parseInt(pid));
	    	if(postVO.getPic() != null){
	    		byte[] pic = postVO.getPic();
	    		out.write(pic);
		    	out.flush();
		    	out.close();
	    	}
	    	
	    	
    	}
		if("qrcode".equals(action)){
			
			QRCode qrcode = new QRCode();
			qrcode.setModuleSize(15);
			qrcode.setCode(req.getParameter("sid"));
			res.setHeader("Pragma", "no-cache");
            res.setHeader("Cache-Control", "no-cache");
            res.setDateHeader("Expires", 0);
            try {
				qrcode.drawOnStream(out);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public static byte[] shrink(byte[] srcImageData, int scaleSize) {
		ByteArrayInputStream bais = new ByteArrayInputStream(srcImageData);
		// 縮小比例，4代表除以4
		int sampleSize = 1;
		int imageWidth = 0;
		int imageHeight = 0;

		// 如果imageSize為0、負數(代表錯誤)或1(1代表與原圖一樣大小)，就直接回傳原圖資料
		if (scaleSize <= 1) {
			return srcImageData;
		}

		try {
			BufferedImage srcBufferedImage = ImageIO.read(bais);
			// 如果無法識別圖檔格式(TYPE_CUSTOM)就回傳TYPE_INT_ARGB；否則回傳既有格式
			int type = srcBufferedImage.getType() == BufferedImage.TYPE_CUSTOM ? BufferedImage.TYPE_INT_RGB
					: srcBufferedImage.getType();
			imageWidth = srcBufferedImage.getWidth();
			imageHeight = srcBufferedImage.getHeight();
			if (imageWidth == 0 || imageHeight == 0) {
				return srcImageData;
			}
			// 只要圖檔較長的一邊超過指定長度(desireSize)，就計算欲縮小的比例
			// 並將圖檔寬高都除以欲縮小比例
			int longer = Math.max(imageWidth, imageHeight);
			if (longer > scaleSize) {
				sampleSize = longer / scaleSize;
				imageWidth = srcBufferedImage.getWidth() / sampleSize;
				imageHeight = srcBufferedImage.getHeight() / sampleSize;
			}
			BufferedImage scaledBufferedImage = new BufferedImage(imageWidth,
					imageHeight, type);
			Graphics graphics = scaledBufferedImage.createGraphics();
			graphics.drawImage(srcBufferedImage, 0, 0, imageWidth, imageHeight,
					null);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(scaledBufferedImage, "jpg", baos);
			return baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return srcImageData; 
		}
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		process(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		process(request, response);
	}

}
