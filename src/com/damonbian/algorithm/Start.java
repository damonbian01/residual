package com.damonbian.algorithm;

public class Start {

	public static void main(String[] args) {
		Data data = new Data();
		data.initNode("fileName");
		Residual residual = new Residual(data.getNodes());	//可以指定窗口宽度和拟合阶数
		residual.start();		
	}

}
