package com.damonbian.util;

import java.util.List;

import com.damonbian.algorithm.Node;

/**
 * 
 * @author biantao
 * 2015-12-24
 */
public class NodeUtil {

	/**
	 * ���ݽڵ�index����node
	 */
	public static Node getNodeByIndex(List<Node> allNodes, int targetIndex) {
		for(Node node : allNodes) {
			if(node.getIndex() == targetIndex)
				return node;
		}
		System.out.println("index " + targetIndex + "doesn't exist");
		System.exit(1);
		return null;
	}
}
