package com.damonbian.algorithm;

/**
 * 高斯分布模型
 * @author damonbian
 * 2015-12-28
 */
public class Model {
	/*二分类的正常点或者离群点*/
	public static final int NORMAL = 100;
	public static final int UNNORMAL = 101;
	/*置信度,缺省值0.9*/
	private double confidence = 0.9;
	/*输入样本值数组*/
	private double[] samples;
	/*模型预测值u*/
	private double u;
	/*模型预测值cgm*/
	private double cgm;
	
	public Model() {
		// TODO Auto-generated constructor stub
	}
	
	public Model(double[] samples) {
		this.samples = samples;
	}

	public Model(double confidence, double[] samples) {
		this.confidence = confidence;
		this.samples = samples;
		System.out.println("=============================开始输出残差===================================");
		for(double sample:samples)
			System.out.println(sample);
	}

	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}

	public double[] getSamples() {
		return samples;
	}

	public void setSamples(double[] samples) {
		this.samples = samples;
	}

	public double getU() {
		return u;
	}

	public void setU(double u) {
		this.u = u;
	}

	public double getCgm() {
		return cgm;
	}

	public void setCgm(double cgm) {
		this.cgm = cgm;
	}
	
	/**
	 * f(x)
	 */
	public double f(double value) {
		return 1.0/(Math.sqrt(2*Math.PI)*cgm)*Math.pow(Math.E, (-1)*Math.pow((value - u), 2)/(2*Math.pow(cgm, 2)));
	}
	
	/**
	 * 开始训练模型
	 * 得到cgm和u
	 */
	public void train() {
		double tempSum = 0.0;
		for(double value : samples)
			tempSum += value;
		this.u = tempSum/samples.length;
		tempSum = 0.0;
		for(double value : samples)
			tempSum += Math.pow(value - this.u, 2);
		this.cgm = Math.sqrt(tempSum/samples.length);
		System.out.println("u is "+this.u+" cgm is "+this.cgm);
	}
	
	/**
	 * 开始预测模型
	 */
	public int predict(double value) {
		return f(value) < confidence ? UNNORMAL : NORMAL;
	}
}
