package com.damonbian.algorithm;

/**
 * ʵʱ�����쳣����
 * @author biantao
 * 20151229
 */
public class Start {

	public static void main(String[] args) {
		if(args.length < 4) {
			System.err.println("please input correct params:<file_name> <confidence> <offset> <frequent>");
			System.exit(1);
		}
		
		String fileName = args[0];
		double confidence = Double.parseDouble(args[1]);
		int offset = Integer.parseInt(args[2]);
		int frequent = Integer.parseInt(args[3]);
		
		Data data = new Data();
		data.initNode(fileName);
		Residual residual = new Residual(data.getNodes(), confidence, offset, frequent, 7, 3);	//����ָ�����ڿ�Ⱥ���Ͻ���
		residual.start();		
	}

}
