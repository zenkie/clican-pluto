package com.huace.mas.entity;

import java.io.Serializable;

public class Thsl extends Kpi implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5109035903696980696L;
	
	private double damElevation;// 坝面高程

	public double getDamElevation() {
		return damElevation;
	}

	public void setDamElevation(double damElevation) {
		this.damElevation = damElevation;
	}
}
