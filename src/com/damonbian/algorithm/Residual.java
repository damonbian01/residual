package com.damonbian.algorithm;

import java.util.List;

import com.damonbian.util.NodeUtil;

/**
 * 基于残差统计的时间序列离群点检测
 * @author admin
 * 2015-12-24
 */
public class Residual {
	/*门限deta*/
	private double thresholdDeta = 0.0;
	
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
}
