package com.vipad.model;

import java.util.List;

public interface VipadDAO_interface {
	public int insert(VipadVO vipadVO);
    public int update(VipadVO vipadVO);
    public int delete(VipadVO vipadVO);
    public VipadVO findByPrimaryKey(Integer vid);
    public List<VipadVO> getAll();
    public List<VipadVO> getOneVipad(Integer gid);
    public List<VipadVO> getVipadByMember(String member_id);
    public List<VipadVO> getVipadByMemberAlive(String member_id);
    public List<VipadVO> getAllDelete();
    public List<VipadVO> getAllAlive();
    public List<VipadHibernateVO> getAllAliveAssociations(); //���e�ݭ���VIP�M�Ϩϥ�
    public List<VipadHibernateVO> getVipadByMemberAllAlive(String member_id); //��showVipad.jsp�ϥ�
}
