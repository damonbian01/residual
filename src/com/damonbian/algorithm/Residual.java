package com.damonbian.algorithm;

import java.util.List;

import com.damonbian.util.NodeUtil;

import Jama.Matrix;

/**
 * 基于残差统计的时间序列离群点检测
 * @author biantao
 * 2015-12-24
 */
public class Residual {
	/*门限deta*/
	private double thresholdDeta = 0.0;
	/*滑动窗口的宽度,缺省值为12*/
	private int widthOfWindow = 12;
	/*拟合阶数p,p缺省值为4且 p<widthOfWindow*/
	private int p = 4;
	
	/**
	 * 算法入口
	 */
	public void start() {
		System.out.println("开始检测异常点");
		System.out.println("异常点检测结束");
	}

	/**
	 * 计算deta值
	 * */
	private void markLkOrUlk(List<Node> trainNodes) {
		for(Node node : trainNodes) {
			/*第一个点和最后一个点不参与运算，deta设为0*/
			if(node.getIndex() == 0 || node.getIndex() == trainNodes.size() - 1)
				continue;
			int index = node.getIndex();
			node.setDeta(calDeta(trainNodes, index - 1, index, index + 1));
			thresholdDeta += node.getDeta();
		}
		/*计算门限-平均法*/
		thresholdDeta /= (trainNodes.size()-2);
		
		/*对顶点是否疑似做初步标记*/
		for(Node node : trainNodes) {
			if(node.getIndex() == 0 || node.getIndex() == trainNodes.size() - 1) {
				node.setFlag(Node.LK);
				continue;
			}
			node.setFlag(node.getDeta()>thresholdDeta?Node.LK:Node.uLK);
		}
	}
	
	/**
	 * 计算deta
	 * 公式 deta(t) = |[x(t)-x(t-1)]+[x(t)-x[t+1]]|
	 * */
	private Double calDeta(List<Node> allNodes, int indexA, int indexB, int indexC) {
		Node A = NodeUtil.getNodeByIndex(allNodes, indexA);
		Node B = NodeUtil.getNodeByIndex(allNodes, indexB);
		Node C = NodeUtil.getNodeByIndex(allNodes, indexC);
		return Math.abs(B.getValue() - A.getValue() + B.getValue() - C.getValue());
	}
	
	/**
	 * 计算残差residual
	 * 输入参数：indexOfNode
	 * 1、计算erfa和bta
	 * 2、计算e1和e2
	 * */
	private void calResidual(int indexOfNode, List<Node> allNodes) {
		/*填充前向窗口*/
		fillPreWindow(indexOfNode, allNodes);
		/*填充后向窗口*/
		fillBackWindow(indexOfNode, allNodes);
	}
	
	/**
	 * 填充前向窗口,忽略疑似异常点
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
			System.out.println("Node " + indexOfNode + " 前向窗口填充失败！");
			System.exit(1);
		}
	}
	
	/**
	 * 填充后向窗口,忽略异常点(此处可以考虑不忽略异常点，否则后向点数量太少填充不满)
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
			System.out.println("Node " + indexOfNode + " 后向窗口填充失败！");
			System.exit(1);
		}
	} 
	
	/**
	 * 计算残差
	 */
	private void calResidual(Node node) {
		node.setResidual(calPreResidual(node) + calBackResidual(node));
	}
	
	/**
	 * 计算前向自相关函数估计值
	 * 窗口外的计算样本值假设为0
	 * rx[0] ~ rx[p]
	 * 计算erfa[1] ~ erfa[p]
	 * 填充residual
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
		
		/*通过rx构造矩阵并求解*/
		double[] tempX = calX(rx);
		for(int i = 0; i < tempX.length; i++)
			erfa[i+1] = tempX[i];
		/*计算xt近似*/
		double approValue = calApproximate(node.getPreNodes(), erfa);
		return node.getValue() - approValue;
	}
	
	/**
	 * 计算后向残差
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
		
		/*通过rx构造矩阵并求解*/
		double[] tempX = calX(rx);
		for(int i = 0; i < tempX.length; i++)
			bta[i+1] = tempX[i];
		/*计算xt近似值*/
		double approValue = calApproximate(node.getBackNodes(), bta);
		return node.getValue() - approValue;
	}
	
	/**
	 * type = 0 表示前向窗口
	 * type = 1表示后向窗口
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
	 * 通过给定double数组构建对角阵
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
	 * 通过rx构造矩阵并求解
	 * 注意：返回的数组下表是从0开始的，我们实际使用的是平移到新的数组里，从1开始的
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
	 * 计算x的近似值
	 */
	private double calApproximate(List<Node> nodes, double[] param) {
		double result = 0.0;
		for(int i = 0; i < p; i++) {
			result += param[i+1]*nodes.get(i).getValue();
		}
		return result;
	}
}
