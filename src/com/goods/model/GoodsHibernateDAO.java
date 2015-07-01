package com.goods.model;

import java.util.List;
import java.util.Map;

import com.tool.HibernateUtil;

import org.hibernate.*;

public class GoodsHibernateDAO implements Goods_interface {
	
	

	@Override
	public int insert(GoodsVO goodsVO) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(goodsVO);
			session.getTransaction().commit();
			return 1;
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		
		return 0;
	}

	@Override
	public int insertGetPrimaryKey(GoodsVO goodsvo) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(goodsvo);
			session.getTransaction().commit();
			return goodsvo.getGid();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return 0;
	}

	@Override
	public int update(GoodsVO goodsVO) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(goodsVO);
			session.getTransaction().commit();
			return 1;
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return 0;
	}

	@Override
	public int deleteByGid(Integer gid) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public GoodsVO findByGid(Integer gid) {
		// TODO Auto-generated method stub
		GoodsVO goodsvo = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from GoodsVO where gid = ?");
			query.setParameter(0, gid);
			goodsvo = (GoodsVO)query.list().get(0);
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return goodsvo;
	}

	@Override
	public List<GoodsVO> findByG_name(String g_name) {
		// TODO Auto-generated method stub
		List<GoodsVO> goodsvo = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from GoodsVO where g_name = ?");
			query.setParameter(0, g_name);
			goodsvo = query.list();
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return goodsvo;
	}

	@Override
	public List<GoodsVO> findByG_nameAlive(String g_name) {
		// SELECT * FROM ex_goods where goods_status = 1 and  g_name LIKE ?
		List<GoodsVO> goodsvo = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from GoodsVO where goods_status = 1 and g_name like ?");
			query.setParameter(0, "%"+g_name+"%");
			goodsvo = query.list();
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return goodsvo;
	}

	@Override
	public List<GoodsVO> findByMember_id(String member_id) {
		// SELECT * FROM ex_goods where goods_status != 2 and goods_status != 3 and  member_id = ?
		List<GoodsVO> goodsvo = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from GoodsVO  where goods_status != 2  and  member_id = ? order by gid desc");
			query.setParameter(0, member_id);
			goodsvo = query.list();
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return goodsvo;
	}

	@Override
	public List<GoodsVO> findByMember_idAlive(String member_id) {
		// SELECT * FROM ex_goods where goods_status = 1 and  member_id = ?
		List<GoodsVO> goodsvo = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from GoodsVO  where goods_status = 1 and  member_id = ?");
			query.setParameter(0, member_id);
			goodsvo = query.list();
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return goodsvo;
	}

	@Override
	public List<GoodsVO> findByMember_idG_status(String member_id,
			Integer goods_status) {
		// SELECT * FROM ex_goods where member_id = ? and goods_status = ? 
		List<GoodsVO> goodsvo = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from GoodsVO  where member_id = ? and goods_status = ? ");
			query.setParameter(0, member_id);
			query.setParameter(1, goods_status);
			goodsvo = query.list();
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return goodsvo;
	}

	@Override
	public List<GoodsVO> findByGroup_id(Integer group_id) {
		// SELECT * FROM ex_goods where goods_status = 1 and  groupid = ?
		List<GoodsVO> goodsvo = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from GoodsVO  where groupid = ?");
			query.setParameter(0, group_id);
			goodsvo = query.list();
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return goodsvo;
	}

	@Override
	public List<GoodsVO> findByGroup_idAlive(Integer group_id) {
		// TODO SELECT * FROM ex_goods where goods_status = 1 and  groupid = ?
		List<GoodsVO> goodsvo = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction(); 
			Query query = session.createQuery("from GoodsVO  where groupid = ?  and goods_status = 1 ");
			query.setParameter(0, group_id);
			goodsvo = query.list();
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return goodsvo;
	}

	@Override
	public List<GoodsVO> findByG_name_Group_idAlive(String g_name,
			Integer group_id) {
		// SELECT * FROM ex_goods where goods_status = 1 and g_name LIKE ? AND groupid = ?
		List<GoodsVO> goodsvo = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction(); 
			Query query = session.createQuery("from GoodsVO  where goods_status = 1 and g_name LIKE ? AND groupid = ? ");
			query.setParameter(0, "%"+g_name+"%");
			query.setParameter(1, group_id);
			goodsvo = query.list();
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return goodsvo;
	}

	@Override
	public List<GoodsVO> getAllOrderByG_hot() {
		// SELECT * FROM ex_goods order by gid
		List<GoodsVO> goodsvo = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction(); 
			Query query = session.createQuery("from GoodsVO  where goods_status = 1 order by g_hot desc");
			goodsvo = query.list();
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return goodsvo;
	}
	
	@Override
	public List<GoodsVO> getAllOrderJoindate() {
		// SELECT *s FROM ex_goods where goods_status = 1 order by joindate
		List<GoodsVO> goodsvo = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction(); 
			Query query = session.createQuery("from GoodsVO  where goods_status = 1 order by joindate ");
			goodsvo = query.list();
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return goodsvo;
	}

	@Override
	public List<GoodsVO> getAllComposite(Map<String, String[]> map) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		List<GoodsVO> list = null;
		try{
			session.beginTransaction();
			//如果使用者沒有輸入任何查詢資料
			if(GoodsUtil_CompositeQuery.getWhereCondition(map) == null || GoodsUtil_CompositeQuery.getWhereCondition(map).length() == 0)
				return list;
			Query query = session.createQuery("from GoodsVO "+GoodsUtil_CompositeQuery.getWhereCondition(map)+" and goods_status = 1 order by joindate");
			//System.out.println(query.getQueryString());
			list = query.list();
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return list;
	}
	
	@Override
	public List<GoodsVO> getAllCompositeByMemberSearch(Map<String, String[]> map) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		List<GoodsVO> list = null;
		try{
			session.beginTransaction();
			//如果使用者沒有輸入任何查詢資料
			if(GoodsUtil_CompositeQuery.getWhereCondition(map) == null || GoodsUtil_CompositeQuery.getWhereCondition(map).length() == 0)
				return list;
			Query query = session.createQuery("from GoodsVO "+GoodsUtil_CompositeQuery.getWhereCondition(map)+"  order by joindate");
			//System.out.println(query.getQueryString()); 
			list = query.list();
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return list;
	}

	@Override
	public List<GoodsVO> getAll() {
		// TODO Auto-generated method stub
		// SELECT * FROM ex_goods order by gid
		List<GoodsVO> goodsvo = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction(); 
			Query query = session.createQuery("from GoodsVO  order by gid");
			goodsvo = query.list();
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return goodsvo;
	}
	
	@Override
	public List<GoodsVOHibernate> getAllAssociations() {
		// TODO Auto-generated method stub
		// SELECT * FROM ex_goods order by gid 
		List<GoodsVOHibernate> goodsvo = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction(); 
			Query query = session.createQuery("from GoodsVOHibernate  order by gid");
			goodsvo = query.list();
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return goodsvo;
	}

	@Override
	public List<GoodsVO> findByMember_idAllAlive(String member_id) {
		List<GoodsVO> goodsvo = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from GoodsVO  where (goods_status != 2 and goods_status != 3) and  member_id = ? order by gid desc");
			query.setParameter(0, member_id);
			goodsvo = query.list();
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return goodsvo;
	}
	
	

}
