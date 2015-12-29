package com.damonbian.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * ��˹�ֲ�ģ��
 * @author damonbian
 * 2015-12-28
 */
public class Model {
	/*������������������Ⱥ��*/
	public static final int NORMAL = 100;
	public static final int UNNORMAL = 101;
	/*ȱʡֵ0.2*/
	private double confidence = 0.2;
	/*ģ��Ԥ��ֵu*/
	private double u;
	/*ģ��Ԥ��ֵcgm*/
	private double cgm;
	/*ִ��ϵ��*/
	private double mu = 0.95;
	/*�����������½�*/
	private double down;
	private double up;
	/*��¼��Ⱥ��Ĳв��*/
	private List<Double> outLiers = new ArrayList<Double>();
	
	public Model() {
		// TODO Auto-generated constructor stub
	}
	
	public Model(List<Double> samples) {
		outLiers.addAll(samples);
	}

	public Model(double confidence, List<Double> samples) {
		this.confidence = confidence;
		outLiers.addAll(samples);
	}

	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
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
	 * ��ʼѵ��ģ��
	 * �õ�cgm��u
	 */
	public void train() {
		Collections.sort(outLiers, new Comparator<Double>() {

			@Override
			public int compare(Double o1, Double o2) {
				/*����*/
				return (o1.compareTo(o2) < 0)?1:-1;
			}
		});
		double[] ks = new double[outLiers.size()];
		for(int i = 0; i < outLiers.size(); i++) {
			if(i == outLiers.size() - 1)
				ks[i] = 0;
			else 
				ks[i] = (outLiers.get(i)-outLiers.get(i+1))/(outLiers.get(0)-outLiers.get(outLiers.size()-1));
		}
		
		for(int i = outLiers.size() - 1; i >= 0; i--){
			if(ks[i] < this.confidence)
				outLiers.remove(i);
			else
				break;
		}
	}
	
	/**
	 * ��ʼԤ��ģ��
	 */
	public int predict(double value) {
		return outLiers.contains(value) ? UNNORMAL : NORMAL;
	}
}
