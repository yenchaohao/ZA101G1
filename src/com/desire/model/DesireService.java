package com.desire.model;

import java.util.List;


public class DesireService {
	private Desire_interface dao;

	public DesireService() {
		dao = new DesireDAO();
	}
	public DesireVO addDesire(Integer groupId,String member_id) {

		DesireVO desireVO = new DesireVO();
		desireVO.setGroupId(groupId);
		desireVO.setMember_id(member_id);		
		dao.insert(desireVO);

		return desireVO;
	}
	public DesireVO updateDesire(Integer did,Integer groupId,String member_id,java.sql.Date joinDate) {

		DesireVO desireVO = new DesireVO();

		desireVO.setDid(did);
		desireVO.setGroupId(groupId);
		desireVO.setMember_id(member_id);
		desireVO.setJoinDate(joinDate);
		
		dao.update(desireVO);

		return desireVO;
	}	
	public void deleteDesire(Integer did) {
		dao.delete(did);
	}
	public DesireVO getOneEmp(Integer did) {
		return dao.findByPrimaryKey(did);
	}
	public List<DesireVO> getAll() {
		return dao.getAll();
	}
}
