package com.vipad.model;

import java.sql.Date;
import java.util.List;

import com.sun.org.apache.regexp.internal.recompile;


public class VipadService {
	
	private VipadDAO_interface dao;
	
	public VipadService() {
		dao = new VipadHibernateDAO();
	}
	
	public VipadVO addVipad(Integer gid, String member_id) {

		VipadVO vipadVO = new VipadVO();

		vipadVO.setGid(gid);
		vipadVO.setMember_id(member_id);
		dao.insert(vipadVO);

		return vipadVO;
	}

	public VipadVO updateVipad(Integer vid, Integer gid, java.sql.Timestamp joindate, java.sql.Timestamp quitdate, Integer status) {

		VipadVO vipadVO = new VipadVO();
		
		vipadVO.setVid(vid);
		vipadVO.setGid(gid);
		vipadVO.setJoindate(joindate);
		vipadVO.setQuitdate(quitdate);
		vipadVO.setStatus(status);
		dao.update(vipadVO);

		return vipadVO;
	}
	
	public int updateVipadByObject(VipadVO vipadVO){
		return dao.update(vipadVO);
	}

	public void deleteVipad(VipadVO vipadVO) {
		dao.delete(vipadVO);
	}

	public VipadVO getOneVipad(Integer vid) {
		return dao.findByPrimaryKey(vid);
	}

	public List<VipadVO> getAll() {
		return dao.getAll();
	} 
	
	public List<VipadVO> getVipadByGid(Integer gid){
		return dao.getOneVipad(gid);
	}
	
	public List<VipadVO> getVipadByMember(String member_id){
		return dao.getVipadByMember(member_id);
	}
	
	public List<VipadVO> getVipadByMemberAlive(String member_id){
		return dao.getVipadByMemberAlive(member_id);
	}
	
	public List<VipadVO> getAllDelete(){
		return dao.getAllDelete();
	}
	
	public List<VipadVO> getAllAlive(){
		return dao.getAllAlive();
	}
	
	public List<VipadHibernateVO> getAllAliveAssociations(){
		return dao.getAllAliveAssociations();
	}
	
	public List<VipadHibernateVO> getVipadByMemberAllAlive(String member_id){
		return dao.getVipadByMemberAllAlive(member_id);
	}
}
