package com.serial.model;

import java.util.List;

public interface Serial_interface {
	public void insert(SerialVO serialVO);
    public void update(SerialVO serialVO);
    public void delete(String serial_number);
    public SerialVO findByPrimaryKey(String serial_number);
    public List<SerialVO> getAll();
}
