package com.send.model;

import java.util.List;
import java.util.Map;



public interface Send_interface {
	public int insert(SendVO sendvo);
	public int update(SendVO sendvo);	
	public int deleteBySid(Integer sid);
	public SendVO findBySid(Integer sid);
	public List<SendVO> findByTid(Integer tid);
	public List<SendVO> findByTidUn(Integer tid);
	public List<SendVO> findByTidFinal(Integer tid);
	public List<SendVO> getAll();
	public List<SendVO> getAllAlive();
	public List<SendVO> getAllByStatus(Integer status);
	public List<SendVOAssociations> getAllByStatusAssociations(Integer status);
	public List<SendVOAssociations> getAllByAddressKeyAssociations(String address);
	public List<SendVOAssociations> CompositeQuery(Map<String,String[]> map);
}
