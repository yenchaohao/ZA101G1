package com.serial.model;

import java.util.List;

import com.message.model.MessageDAO;
import com.message.model.MessageVO;

public class SerialService {
	private SerialDAO dao;
	private SerialHibernateDAO hidao;
	public SerialService() {
		dao = new SerialDAO();
		hidao=new SerialHibernateDAO();
	}
	public SerialVO addSerial(int money){
		SerialVO serialVO = new SerialVO();
		String serial=null;
		
		List<SerialVO> list=getAll();
		Boolean isRepeat=null;
		do{
			//初始化
			serial=getRandomKey();
			isRepeat=false;
			//判斷有無重複
			for(SerialVO vo:list){
				if(serial.equals(vo.getSerial_number())){
					isRepeat=true;
					break;
				}
			}		
		}while(isRepeat==true);
		serialVO.setMoney(money);
		serialVO.setSerial_number(serial);
		
		hidao.insert(serialVO);
		return serialVO;		
	}
	public SerialVO updateSerial(String serial_number,int money,String member_id){
		SerialVO serialVO=new SerialVO();
		serialVO.setMember_id(member_id);
		serialVO.setMoney(money);
		serialVO.setSerial_number(serial_number);
		hidao.update(serialVO);
		return serialVO;		
	}
	
	public void deleteSerial(String serial_number){
		hidao.delete(serial_number);	
	}
	public SerialVO findBySerial(String serial_number){
		SerialVO serialVO=hidao.findByPrimaryKey(serial_number);
		return serialVO;		
	}
	public List<SerialVO> getAll(){
		List<SerialVO> list =hidao.getAll();
		return list;
	}
	public String getRandomKey() {
		int length=10;
		String serial="";
		String [] pwPool={
				"0","1","2","3","4","5","6","7","8","9",
		        "A","B","C","D","E","F","G","H","I","J",
		        "K","L","M","N","O","P","Q","R","S","T",
		        "U","V","W","X","Y","Z","a","b","c","d",
		        "e","f","g","h","i","j","k","l","m","n","o",
		        "p","q","r","s","t","u","v","w","x","y","z"
		};
		for(int i=0;i<length;i++){
			serial+=pwPool[(int) Math.floor(Math.random()*(pwPool.length))];		
		}
		return serial;
	}
}
