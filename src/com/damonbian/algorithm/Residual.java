package com.damonbian.algorithm;

import java.util.List;

import com.damonbian.util.NodeUtil;

import Jama.Matrix;

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
	/*��Ͻ���p,pȱʡֵΪ4�� p<widthOfWindow*/
	private int p = 4;
	
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
		fillPreWindow(indexOfNode, allNodes);
		/*�����򴰿�*/
		fillBackWindow(indexOfNode, allNodes);
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
	
	/**
	 * ����в�
	 */
	private void calResidual(Node node) {
		node.setResidual(calPreResidual(node) + calBackResidual(node));
	}
	
	/**
	 * ����ǰ������غ�������ֵ
	 * ������ļ�������ֵ����Ϊ0
	 * rx[0] ~ rx[p]
	 * ����erfa[1] ~ erfa[p]
	 * ���residual
	 * */
	private double calPreResidual(Node node) {
		double[] rx = new double[p+1];
		double[] erfa = new double[p+1];
		for(int k = 0; k <= p; k++) {
			double tempSum = 0.0;
			for(int j = 0; j <= widthOfWindow - 1; j++) {
//				tempSum += (parseValue(0, node, node.getIndex() - widthOfWindow + j) * parseValue(0, node, node.getIndex() - widthOfWindow + j + k));
				tempSum += (parseValue(0, node, widthOfWindow - j) * parseValue(0, node, widthOfWindow - j - k));
			}
			rx[k] = (1.0/((double) widthOfWindow))*tempSum;
		}
		
		/*ͨ��rx����������*/
		double[] tempX = calX(rx);
		for(int i = 0; i < tempX.length; i++)
			erfa[i+1] = tempX[i];
		/*����xt����*/
		double approValue = calApproximate(node.getPreNodes(), erfa);
		return node.getValue() - approValue;
	}
	
	/**
	 * �������в�
	 */
	private double calBackResidual(Node node) {
		double[] rx = new double[p+1];
		double[] bta = new double[p+1];
		for(int k = 0; k <= p; k++) {
			double tempSum = 0.0;
			for(int j = 0; j <= widthOfWindow - 1; j++) {
				tempSum += (parseValue(1, node, j) * parseValue(1, node, j + k));
			}
			rx[k] = (1.0/((double) widthOfWindow))*tempSum;
		}
		
		/*ͨ��rx����������*/
		double[] tempX = calX(rx);
		for(int i = 0; i < tempX.length; i++)
			bta[i+1] = tempX[i];
		/*����xt����ֵ*/
		double approValue = calApproximate(node.getBackNodes(), bta);
		return node.getValue() - approValue;
	}
	
	/**
	 * type = 0 ��ʾǰ�򴰿�
	 * type = 1��ʾ���򴰿�
	 */
/*	private double parseValue(int type, Node node, int index) {
		List<Node> nodes = (type == 0)?node.getPreNodes():node.getBackNodes();
		for(Node subNode : nodes) {
			if(subNode.getIndex() == index)
				return subNode.getValue();
			else
				continue;
		}
		return 0.0;
	}*/
	
	private double parseValue(int type, Node node, int index) {
		List<Node> nodes = (type == 0)?node.getPreNodes():node.getBackNodes();
		if(index > widthOfWindow || index < 0)
			return 0.0;
		else if(index == 0)
			return node.getValue();
		else 
			return nodes.get(index - 1).getValue();
	}
	
	/**
	 * ͨ������double���鹹���Խ���
	 */
	private Matrix constructMatrix(double[] rx) {
		double[][] arrayA = new double[p][p];
		for(int i = 0; i < p; i++) {
			for(int j = i; j < p; j++) {
				arrayA[i][j] = rx[j-i];
				arrayA[j][i] = rx[j-i];
			}
		}
		return new Matrix(arrayA);
	}
	
	/**
	 * ͨ��rx����������
	 * ע�⣺���ص������±��Ǵ�0��ʼ�ģ�����ʵ��ʹ�õ���ƽ�Ƶ��µ��������1��ʼ��
	 */
	private double[] calX(double[] rx) {
		double[] result = new double[p];
		Matrix A = constructMatrix(rx);
		double[] arrayB = new double[p];
		for(int i = 1; i <= p; i++ )
			arrayB[i-1] = rx[i];
		Matrix b = new Matrix(arrayB, p);
		Matrix x = A.solve(b);
		double[][] tempX = x.getArray();
		for(int i = 0; i < tempX.length; i++)
			result[i] = tempX[i][0];
		return result;
	}
	
	/**
	 * ����x�Ľ���ֵ
	 */
	private double calApproximate(List<Node> nodes, double[] param) {
		double result = 0.0;
		for(int i = 0; i < p; i++) {
			result += param[i+1]*nodes.get(i).getValue();
		}
		return result;
	}
}
