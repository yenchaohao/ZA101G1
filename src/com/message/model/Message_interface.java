package com.message.model;

import java.util.List;
import java.util.Map;

public interface Message_interface {
	public void insert(MessageVO messageVO);
	public int insertGetPK(MessageVO messageVO);
    public void update(MessageVO messageVO);
    public void delete(Integer mid);
    public MessageVO findByPrimaryKey(Integer mid);
    public List<MessageVO> getAll();
    public List<MessageVO> getAllLatest();
    public List<MessageVO> search(String title);
}
