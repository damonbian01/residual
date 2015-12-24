package com.damonbian.algorithm;

import java.util.List;

import com.damonbian.util.NodeUtil;

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
}
