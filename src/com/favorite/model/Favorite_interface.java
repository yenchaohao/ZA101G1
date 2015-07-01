package com.favorite.model;

import java.util.List;

public interface Favorite_interface {
	public int insert(FavoriteVO favoriteVO);
    public int update(FavoriteVO favoriteVO);
    public int delete(Integer fid);
    public FavoriteVO findByPrimaryKey(Integer fid);
    public List<FavoriteVO> getAll();
    public List<FavoriteVO> getAllByMember(String member_id);
}
