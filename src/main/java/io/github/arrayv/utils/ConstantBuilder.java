package io.github.arrayv.utils;

import io.github.arrayv.main.ArrayVisualizer;

public class ConstantBuilder {
	private ArrayVisualizer arrayVisualizer;
	public ConstantBuilder(ArrayVisualizer aV) {
		this.arrayVisualizer = aV;
	}
	private long getTotal() {
		return arrayVisualizer.getReads().getComparisons() +
			   arrayVisualizer.getWrites().getMainWriteCount() +
			   arrayVisualizer.getWrites().getAuxWriteCount();
	}
	public String getConstant() {
		long targetConstant = arrayVisualizer.getConstant().apply((long) arrayVisualizer.getCurrentLength()),
			 current = getTotal();
		
		if(targetConstant == -1L)
			return "Constant = ---";
		
		double m = current / (double) targetConstant;
		
		return "Constant = " + String.format("%.3f", m);
	}
}