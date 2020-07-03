package com.heavenize.parsing.bloomberg;

public class ChunkInfo {
	private int nbCategories;
	private String info;
	
	ChunkInfo(int nbCategories, String info) {
		this.nbCategories = nbCategories;
		this.info = info;
	}
	
	public int getNbCategories() {
		return this.nbCategories;
	}
	
	public String getInfo() {
		return this.info;
	}

}
