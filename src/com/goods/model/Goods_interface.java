package com.goods.model;

import java.util.List;
import java.util.Map;

public interface Goods_interface {
	public int insert(GoodsVO goodsVO);
	public int insertGetPrimaryKey(GoodsVO goodsvo);
	public int update(GoodsVO goodsVO);
	public int deleteByGid(Integer gid);
	public GoodsVO findByGid(Integer gid);
	public List<GoodsVO> findByG_name(String g_name);
	public List<GoodsVO> findByG_nameAlive(String g_name);
	public List<GoodsVO> findByMember_id(String member_id);
	public List<GoodsVO> findByMember_idAlive(String member_id); 
	public List<GoodsVO> findByMember_idG_status(String member_id, Integer goods_status);
	public List<GoodsVO> findByGroup_id(Integer group_id);
	public List<GoodsVO> findByGroup_idAlive(Integer group_id);
	public List<GoodsVO> findByG_name_Group_idAlive(String g_name, Integer group_id);
	public List<GoodsVO> getAllOrderByG_hot();  //��ӫ~�O�W�[���� �M��̷Ӧ��ëױƧ�
	public List<GoodsVO> getAllOrderJoindate(); //��ӫ~�O�W�[���� �M��̷Ӥ���Ƨ�
	public List<GoodsVO> getAllComposite(Map<String,String[]> map);
	public List<GoodsVO> getAll(); 
	public List<GoodsVOHibernate> getAllAssociations();
	public List<GoodsVO> getAllCompositeByMemberSearch(Map<String, String[]> map); //�|�����ڪ��ӫ~�d�ߥ�
	public List<GoodsVO> findByMember_idAllAlive(String member_id); //VIP�s�i�ΡA��X���A���O3��2��
	
}
