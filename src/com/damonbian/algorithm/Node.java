package com.damonbian.algorithm;

/**
 * 
 * @author admin
 * 2015-12-24
 */
public class Node {
	/*���徲̬����*/
	public static final int LK = 1;
	public static final int uLK = 0;
	
	/*�ڵ���*/
	private int index;
	/*�ڵ�����*/
	private String nodeName;
	/*�ڵ�ֵ*/
	private Double value;
	/*deta*/
	private Double deta;
	/*����Ƿ�Ϊ��˼��Ⱥ�� 1-LK 0-uLK*/
	private int flag;
	
	public Node() {
		// TODO Auto-generated constructor stub
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Double getDeta() {
		return deta;
	}

	public void setDeta(Double deta) {
		this.deta = deta;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
