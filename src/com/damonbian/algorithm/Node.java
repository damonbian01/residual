package com.damonbian.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author biantao
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
	private double value;
	/*deta*/
	private double deta;
	/*����Ƿ�Ϊ��˼��Ⱥ�� 1-LK 0-uLK*/
	private int flag;
	/*�в�residual*/
	private double residual; 
	/*ǰ�򴰿��ڵ�Node����*/
	private List<Node> preNodes = new ArrayList<Node>();
	/*���򴰿��ڵ�Node����*/
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
