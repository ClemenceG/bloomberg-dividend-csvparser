package com.heavenize.parsing.bloomberg;

import java.util.ArrayList;

public class Chunk {
	private int sizeOfChunk;
	private ArrayList<String> chunkPart = new ArrayList<String>();

	Chunk(int size) {
		this.sizeOfChunk = size;
	}


	// get methods
	public int getSize() {
		return this.sizeOfChunk;
	}
	public String getValue(int i) {
		return chunkPart.get(i);
	}

	// set & add methods
	public void addValue(String s) {
		this.chunkPart.add(s);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chunkPart == null) ? 0 : chunkPart.hashCode());
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
		Chunk other = (Chunk) obj;
		if (chunkPart == null) {
			if (other.chunkPart != null)
				return false;
		} else if (!chunkPart.equals(other.chunkPart))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Chunk [chunk=" + chunkPart + "]";
	}

}
