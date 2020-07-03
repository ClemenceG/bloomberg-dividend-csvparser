package com.heavenize.parsing.bloomberg;

import java.util.Date;

public class Dividend {

	private Date declaredDate;
	private Date exDate;
	private Date recordDate;
	private Date payDate;
	private double amount;
	private String frequency;
	private String type;

	// get methods
	public Date getDeclaredDate() {
		return this.declaredDate;
	}
	public Date getExDate() {
		return this.exDate;
	}
	public Date getRecordDate() {
		return this.recordDate;
	}
	public Date getPayDate() {
		return this.payDate;
	}
	public double getAmount() {
		return this.amount;
	}
	public String getFrequency() {
		return this.frequency;
	}
	public String getType() {
		return this.type;
	}


	// add methods
	public void setDeclaredDate(Date date) {
		this.declaredDate = date;
	}
	public void setExDate(Date date) {
		this.exDate = date;
	}
	public void setRecordDate(Date date) {
		this.recordDate = date;
	}
	public void setPayDate(Date date) {
		this.payDate = date;
	}
	public void setAmount(double d) {
		this.amount = d;
	}
	public void setFrequency(String freq) {
		this.frequency = freq;
	}
	public void setType(String type) {
		this.type = type;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((declaredDate == null) ? 0 : declaredDate.hashCode());
		result = prime * result + ((frequency == null) ? 0 : frequency.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dividend other = (Dividend) obj;
		if (declaredDate == null) {
			if (other.declaredDate != null)
				return false;
		} else if (!declaredDate.equals(other.declaredDate))
			return false;
		if (frequency == null) {
			if (other.frequency != null)
				return false;
		} else if (!frequency.equals(other.frequency))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Dividend [declaredDate=" + declaredDate + ", amount=" + amount + ", type=" + type + "]";
	}

}
