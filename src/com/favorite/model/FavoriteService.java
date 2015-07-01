package com.favorite.model;

import java.util.List;


public class FavoriteService {
	private Favorite_interface dao;

	public FavoriteService() {
		dao = new FavoriteHibernateDAO();
	}
	public FavoriteVO addFavorite(Integer gid,String member_id) {

		FavoriteVO favoriteVO = new FavoriteVO();
		favoriteVO.setGid(gid);
		favoriteVO.setMember_id(member_id);		
		dao.insert(favoriteVO);

		return favoriteVO;
	}
	public FavoriteVO updateFavorite(Integer fid,Integer gid,String member_id,java.sql.Date joindate) {

		FavoriteVO favoriteVO = new FavoriteVO();

		favoriteVO.setFid(fid);
		favoriteVO.setGid(gid);
		favoriteVO.setMember_id(member_id);
		favoriteVO.setJoindate(joindate);
		
		dao.update(favoriteVO);

		return favoriteVO;
	}	
	public void deleteFavorite(Integer fid) {
		dao.delete(fid);
	}
	public FavoriteVO getOneEmp(Integer fid) {
		return dao.findByPrimaryKey(fid);
	}
	public List<FavoriteVO> getAll() {
		return dao.getAll();
	}
	
	public List<FavoriteVO> getAllByMember(String member_id){
		return dao.getAllByMember(member_id);
	}
}
