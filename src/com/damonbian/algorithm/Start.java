package com.damonbian.algorithm;

public class Start {

	public static void main(String[] args) {
		Data data = new Data();
		data.initNode("fileName");
		Residual residual = new Residual(data.getNodes());	//����ָ�����ڿ�Ⱥ���Ͻ���
		residual.start();		
	}

}
