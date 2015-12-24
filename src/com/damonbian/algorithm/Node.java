package com.damonbian.algorithm;

import java.util.ArrayList;
import java.util.List;

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
	/*前向窗口内的Node集合*/
	private List<Node> preNodes = new ArrayList<Node>();
	/*后向窗口内的Node集合*/
	private List<Node> backNodes = new ArrayList<Node>();
	
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

	public List<Node> getPreNodes() {
		return preNodes;
	}

	public void setPreNodes(List<Node> preNodes) {
		this.preNodes = preNodes;
	}

	public List<Node> getBackNodes() {
		return backNodes;
	}

	public void setBackNodes(List<Node> backNodes) {
		this.backNodes = backNodes;
	}

}
