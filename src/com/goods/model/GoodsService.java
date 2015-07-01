package com.goods.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class GoodsService {

	private Goods_interface dao;
	private Goods_interface daoHibernate;

	public GoodsService() {
		dao = new GoodsDAO();
		daoHibernate = new GoodsHibernateDAO();
	}

	public Integer addGoods(Integer groupid, String member_id, String g_name,
			String g_describe, Integer g_price, Integer g_level, Integer g_hot,
			Timestamp joindate, Timestamp quitdate, byte[] pic, Integer goods_status) {

		GoodsVO goodsVO = new GoodsVO();

		goodsVO.setGroupid(groupid);
		goodsVO.setMember_id(member_id);
		goodsVO.setG_name(g_name);
		goodsVO.setG_describe(g_describe);
		goodsVO.setG_price(g_price);
		goodsVO.setG_level(g_level);
		goodsVO.setG_hot(g_hot);
		goodsVO.setJoindate(joindate);
		goodsVO.setQuitdate(quitdate);
		goodsVO.setPic(pic);
		goodsVO.setGoods_status(goods_status);

		int insert = daoHibernate.insert(goodsVO);

		return insert;
	}
	
	public int addGoodsGetPrimaryKey(Integer groupid, String member_id, String g_name,
			String g_describe, Integer g_price, Integer g_level, Integer g_hot,
			Timestamp joindate, Timestamp quitdate, byte[] pic, Integer goods_status) {

		GoodsVO goodsVO = new GoodsVO();

		goodsVO.setGroupid(groupid);
		goodsVO.setMember_id(member_id);
		goodsVO.setG_name(g_name);
		goodsVO.setG_describe(g_describe);
		goodsVO.setG_price(g_price);
		goodsVO.setG_level(g_level);
		goodsVO.setG_hot(g_hot);
		goodsVO.setJoindate(joindate);
		goodsVO.setQuitdate(quitdate);
		goodsVO.setPic(pic);
		goodsVO.setGoods_status(goods_status);

		int pk  = daoHibernate.insertGetPrimaryKey(goodsVO);

		return pk;
	}
	
	public int updateGoodsByObject(GoodsVO goodsvo){
		int update = daoHibernate.update(goodsvo);
		return update;
	}

	public void deleteGoodsByGid(Integer gid) {
		dao.deleteByGid(gid);
	}

	public GoodsVO findGoodsByGid(Integer gid) {
		return daoHibernate.findByGid(gid);
	}

	public List<GoodsVO> findGoodsByG_name(String g_name) {
		return daoHibernate.findByG_name(g_name);
	}

	public List<GoodsVO> findByG_nameAlive(String g_name){
		return daoHibernate.findByG_nameAlive(g_name);
	}
	
	public List<GoodsVO> findGoodsByGroup_id(Integer group_id) {
		return daoHibernate.findByGroup_id(group_id);
	}
	
	public List<GoodsVO> findByGroup_idAlive(Integer group_id){
		return daoHibernate.findByGroup_idAlive(group_id);
	}
	
	public List<GoodsVO> findGoodsByMember_id(String member_id) {
		return daoHibernate.findByMember_id(member_id);
	}

	public List<GoodsVO> findGoodsByMember_idAlive(String member_id){
		return daoHibernate.findByMember_idAlive(member_id);
	}
	
	public List<GoodsVO> findGoodsByMember_idG_status(String member_id, Integer goods_status){
		return daoHibernate.findByMember_idG_status(member_id, goods_status);
	}
	
	public List<GoodsVO> findByG_nameGroupidAlive(String g_name, Integer group_id){
		return daoHibernate.findByG_name_Group_idAlive(g_name, group_id);
	}
	
	public List<GoodsVO> getAllOrderByG_hot() {
		return daoHibernate.getAllOrderByG_hot();
	}

	public List<GoodsVO> getAllAlive() {
		return daoHibernate.getAllOrderJoindate();
	}
	
	public List<GoodsVO> getAll() {
		return daoHibernate.getAll();
	}
	
	public List<GoodsVO> getAllMap(Map<String,String[]> map){
		return daoHibernate.getAllComposite(map);
	}
	
	public List<GoodsVO> getAllMapByMember(Map<String,String[]> map){
		return daoHibernate.getAllCompositeByMemberSearch(map);
		}
	
	public List<GoodsVOHibernate> getAllAssociations() {
		return daoHibernate.getAllAssociations();
	}
	
	public List<GoodsVO> findByMember_idAllAlive(String member_id) {
		return daoHibernate.findByMember_idAllAlive(member_id);
	}

}
