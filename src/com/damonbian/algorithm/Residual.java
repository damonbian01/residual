package com.damonbian.algorithm;

import java.util.ArrayList;
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
	/*ģ��Model*/
	private Model model;
	/*����nodes*/
	private List<Node> nodes;
	
	public Residual(List<Node> nodes) {
		this.nodes = nodes;
	}
	
	public Residual(List<Node> nodes, int widthOfWindow, int p) {
		this.nodes = nodes;
		this.widthOfWindow = widthOfWindow;
		this.p = p;
	}

	/**
	 * �㷨���
	 */
	public void start() {
		System.out.println("=====================��ʼ����쳣��=====================");
		System.out.println("=====================��ʼ������Ƶ�=====================");
		markLkOrUlk(nodes);
		System.out.println("=====================����ָ�����䴰��=====================");
		fillWindow(13, 300, nodes);
		System.out.println("=====================����ָ������в�=====================");
		calResidual(13, 300, nodes);
		System.out.println("=====================ѵ��,Ԥ��ģ��=====================");
		train(getTrainNodes(13, 300, nodes));
		outFlag(getTrainNodes(13, 300, nodes));
		System.out.println("=====================�쳣�������=====================");
	}
	
	private List<Node> getTrainNodes(int indexStart, int indexEnd, List<Node> nodes) {
		List<Node> tempNodes = new ArrayList<Node>();
		for(Node node : nodes) {
			if(node.getIndex() < indexStart || node.getIndex() > indexEnd)
				continue;
			else
				tempNodes.add(node);
		}
		return tempNodes;
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
	 * ���ָ����������ݵĴ���
	 */
	private void fillWindow(int indexStart, int indexEnd, List<Node> allNodes) {
		for(Node node : allNodes) {
			int index = node.getIndex();
			if(index < indexStart || index > indexEnd)
				continue;
			else {
				/*���ǰ�򴰿�*/
				fillPreWindow(index, allNodes);
				/*�����򴰿�*/
				fillBackWindow(index, allNodes);
			}
		}
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
	 * ����ָ������Node�Ĳв�
	 */
	private void calResidual(int indexStart, int indexEnd, List<Node> allNodes) {
		for(Node node : allNodes) {
			int index = node.getIndex();
			if(index < indexStart || index > indexEnd)
				continue;
			else
				node.setResidual(calPreResidual(node) + calBackResidual(node));
		}
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
	
	/**
	 * ͨ����˹�ֲ�Ԥ��ģ��
	 * trainNodes������allNodes
	 */
	private void train(List<Node> trainNodes) {
		double[] temp = new double[trainNodes.size()];
		for(int i = 0; i < trainNodes.size(); i++)
			temp[i] = trainNodes.get(i).getValue();
		model = new Model(0.90, temp);
		model.train();
	}
	
	/**
	 * ���Ԥ����
	 */
	private void outFlag(List<Node> trainNodes) {
		for(Node node : trainNodes) {
			if(model == null) {
				System.out.println("Model��δѵ��");
				System.exit(1);
			} else {
				node.setRealFlag(model.predict(node.getValue()));
				switch(node.getRealFlag()) {
				case Model.NORMAL:
					System.out.println(node.toString()+" predict lable is normal");
					break;
				case Model.UNNORMAL:
					System.out.println(node.toString()+" predict lable is unnormal");
					break;
				}
			}
		}
		System.out.println("ѵ������");
	}
	
}
