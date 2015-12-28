package com.damonbian.algorithm;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author damonbian
 * 20151228
 */
public class Data {
	public static int index = 0;
	/*Nodes*/
	private List<Node> Nodes = new ArrayList<Node>();
	
	public List<Node> getNodes() {
		return Nodes;
	}

	public void setNodes(List<Node> nodes) {
		Nodes = nodes;
	}

	public void initNode(String fileName) {
		try {
			FileReader reader = new FileReader(fileName);
			BufferedReader br = new BufferedReader(reader);
			StringBuffer sb = new StringBuffer("");
			
			String line = null;
			while((line = br.readLine()) != null) {
				String[] params = line.split("\t");
				String nodeName = params[0];
				double value = Double.parseDouble(params[1]);
				Node node = new Node(index++, nodeName, value);
				Nodes.add(node);
			}
			
			br.close();
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println(fileName+" 文件不存在");
			System.exit(1);
		} catch (IOException e) {
			System.out.println("读取行失败");
			System.exit(1);
		}
	}
}
