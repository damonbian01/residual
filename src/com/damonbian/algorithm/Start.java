package com.damonbian.algorithm;

public class Start {

	public static void main(String[] args) {
		Data data = new Data();
//		data.initNode("data/201511241230_314");
		data.initNode("data/201510292355_314");
//		data.initNode("data/total.txt");
		Residual residual = new Residual(data.getNodes(), 7, 3);	//可以指定窗口宽度和拟合阶数
		residual.start();		
	}

}
