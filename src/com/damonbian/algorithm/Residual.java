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
			if(node.getIndex() == 0 || node.getIndex() == trainNodes.size() - 1)
				continue;
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
	public void calResidual(int indexOfNode) {
		/*���ǰ�򴰿�*/
	}
	
	/**
	 * ���ǰ�򴰿�
	 * */
	public void fillWindow(int indexOfNode) {
		
	}
}
