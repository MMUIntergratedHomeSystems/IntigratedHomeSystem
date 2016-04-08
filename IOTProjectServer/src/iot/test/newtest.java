package iot.test;

import iot.dao.DAO;

public class newtest {

	public static void main(String[] args){
		DAO dao = new DAO();
		for (int i = 0; i<=1000; i++){
			//System.out.println(dao.getPubDeviceInfo());
			System.out.println(dao.getDeviceInfo("Test2"));
		}
			
	}
}
