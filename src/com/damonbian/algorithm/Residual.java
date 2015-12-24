package com.damonbian.algorithm;

import java.util.List;

import com.damonbian.util.NodeUtil;

/**
 * ���ڲв�ͳ�Ƶ�ʱ��������Ⱥ����
 * @author biantao
 * 2015-12-24
 */
public class Residual {
	/*����deta*/
	private double thresholdDeta = 0.0;
	/*�������ڵĿ��,ȱʡֵΪ12*/
	private int widthOfWindow = 12;
	
	/**
	 * �㷨���
	 */
	public void start() {
		System.out.println("��ʼ����쳣��");
		System.out.println("�쳣�������");
	}

	/**
	 * ����detaֵ
	 * */
	private void markLkOrUlk(List<Node> trainNodes) {
		for(Node node : trainNodes) {
			/*��һ��������һ���㲻�������㣬deta��Ϊ0*/
			if(node.getIndex() == 0 || node.getIndex() == trainNodes.size() - 1)
				continue;
			int index = node.getIndex();
			node.setDeta(calDeta(trainNodes, index - 1, index, index + 1));
			thresholdDeta += node.getDeta();
		}
		/*��������-ƽ����*/
		thresholdDeta /= (trainNodes.size()-2);
		
		/*�Զ����Ƿ��������������*/
		for(Node node : trainNodes) {
			if(node.getIndex() == 0 || node.getIndex() == trainNodes.size() - 1) {
				node.setFlag(Node.LK);
				continue;
			}
			node.setFlag(node.getDeta()>thresholdDeta?Node.LK:Node.uLK);
		}
	}
	
	/**
	 * ����deta
	 * ��ʽ deta(t) = |[x(t)-x(t-1)]+[x(t)-x[t+1]]|
	 * */
	private Double calDeta(List<Node> allNodes, int indexA, int indexB, int indexC) {
		Node A = NodeUtil.getNodeByIndex(allNodes, indexA);
		Node B = NodeUtil.getNodeByIndex(allNodes, indexB);
		Node C = NodeUtil.getNodeByIndex(allNodes, indexC);
		return Math.abs(B.getValue() - A.getValue() + B.getValue() - C.getValue());
	}
	
	/**
	 * ����в�residual
	 * ���������indexOfNode
	 * 1������erfa��bta
	 * 2������e1��e2
	 * */
	private void calResidual(int indexOfNode, List<Node> allNodes) {
		/*���ǰ�򴰿�*/
	}
	
	/**
	 * ���ǰ�򴰿�,���������쳣��
	 * */
	private void fillPreWindow(int indexOfNode, List<Node> allNodes) {
		List<Node> nodes = NodeUtil.getNodeByIndex(allNodes, indexOfNode).getPreNodes();
		int size = this.widthOfWindow;
		for(int i = 1; indexOfNode - i > 0 && size > 0; i++) {
			Node tempNode = NodeUtil.getNodeByIndex(allNodes, indexOfNode - i);
			if(tempNode.getFlag() != Node.uLK) {
				nodes.add(tempNode);
				size--;
			}
		}
		if(size != 0) {
			System.out.println("Node " + indexOfNode + " ǰ�򴰿����ʧ�ܣ�");
			System.exit(1);
		}
	}
	
	/**
	 * �����򴰿�,�����쳣��(�˴����Կ��ǲ������쳣�㣬������������̫����䲻��)
	 * */
	private void fillBackWindow(int indexOfNode, List<Node> allNodes) {
		List<Node> nodes = NodeUtil.getNodeByIndex(allNodes, indexOfNode).getBackNodes();
		int size = this.widthOfWindow;
		for(int i = 1; indexOfNode + i < allNodes.size() - 1; i++) {
			Node tempNode = NodeUtil.getNodeByIndex(allNodes, indexOfNode + i);
			if(tempNode.getFlag() != Node.uLK) {
				nodes.add(tempNode);
				size--;
			}
		}
		if(size != 0) {
			System.out.println("Node " + indexOfNode + " ���򴰿����ʧ�ܣ�");
			System.exit(1);
		}
	} 
}
