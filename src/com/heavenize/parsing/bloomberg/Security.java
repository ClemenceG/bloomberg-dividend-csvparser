package com.heavenize.parsing.bloomberg;

import java.util.List;
import java.util.ArrayList;

public class Security {
	private int nbDividends;
	private String bloombergTicker;
	private String frequency;
	private List<Dividend> dividends = new ArrayList<Dividend>();


	Security(String ticker) {
		this.bloombergTicker = ticker;
		this.nbDividends = 0;
		this.frequency = null;
		this.dividends = null;
		}

	Security(String ticker, int nbDividends) {
		this.bloombergTicker = ticker;
		this.nbDividends = nbDividends;
	}

	// get method
	public String getBloombergTicker() {
		return this.bloombergTicker;
	}
	public int getNbDividends() {
		return this.nbDividends;
	}
	public String getFrequency() {
		return this.frequency;
	}
	public List<Dividend> getDividends() {
		return this.dividends;
	}
	public Dividend getDividend(int i) {
		return this.dividends.get(i);
	}

	// set and add methods
	public void setTicker(String ticker) {
		this.bloombergTicker = ticker;
	}
	public void setNbDividends(int nb) {
		this.nbDividends = nb;
	}
	public void addDividend(Dividend div) {
		this.dividends.add(div);
	}
	public void setFrequency(String s) {
		this.frequency = s;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bloombergTicker == null) ? 0 : bloombergTicker.hashCode());
		result = prime * result + ((dividends == null) ? 0 : dividends.hashCode());
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
		Security other = (Security) obj;
		if (bloombergTicker == null) {
			if (other.bloombergTicker != null)
				return false;
		} else if (!bloombergTicker.equals(other.bloombergTicker))
			return false;
		if (dividends == null) {
			if (other.dividends != null)
				return false;
		} else if (!dividends.equals(other.dividends))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Security [bloombergTicker=" + bloombergTicker + ", dividends=" + dividends + "]";
	}

}
