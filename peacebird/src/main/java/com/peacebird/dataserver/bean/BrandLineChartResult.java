package com.peacebird.dataserver.bean;

import java.util.Date;

public class BrandLineChartResult implements Comparable<BrandLineChartResult>{
	
	private Double amount;
	
	private Double preAmount;
	
	private Date date;

	public BrandLineChartResult(Number amount, Number preAmount,Date date) {
		super();
		if(amount!=null){
			this.amount = amount.doubleValue()/10000;
		}
		if(preAmount!=null){
			this.preAmount = preAmount.doubleValue()/10000;
		}
		this.date = date;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}


	public Double getPreAmount() {
		return preAmount;
	}

	public void setPreAmount(Double preAmount) {
		this.preAmount = preAmount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public Double getLike(){
		return (amount/(preAmount)-1)*100;
	}

	@Override
	public int compareTo(BrandLineChartResult o) {
		return date.compareTo(o.getDate());
	}


}