package com.damonbian.algorithm;

/**
 * 
 * @author biantao
 * 2015-12-24
 */
public class Node {
	/*定义静态常量*/
	public static final int LK = 1;
	public static final int uLK = 0;
	
	/*节点编号*/
	private int index;
	/*节点名称*/
	private String nodeName;
	/*节点值*/
	private double value;
	/*deta*/
	private double deta;
	/*标记是否为意思离群点 1-LK 0-uLK*/
	private int flag;
	/*残差residual*/
	private double residual; 
	
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

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getDeta() {
		return deta;
	}

	public void setDeta(double deta) {
		this.deta = deta;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public double getResidual() {
		return residual;
	}

	public void setResidual(double residual) {
		this.residual = residual;
	}
	
}
