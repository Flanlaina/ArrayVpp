package io.github.arrayv.utils;

import java.util.ArrayList;
import java.util.Arrays;

public final class TasqueManager<E> {
	private ArrayList<Tasque<E>> tasques;
	public TasqueManager() {
		tasques = new ArrayList<>();
	}
	@SuppressWarnings("unchecked")
	public TasqueManager(Tasque<E>... startingTasques) {
		this();
		Arrays.stream(startingTasques).forEachOrdered(tasques::add);
	}
	public boolean hasTasques() {
		return tasques.size() > 0;
	}
	@SuppressWarnings("unchecked")
	public void queueTasques(Tasque<E>... newTasques) {
		for(Tasque<E> k : newTasques) {
			tasques.add(k);
		}
	}
	public String toString() {
		String x = "";
		@SuppressWarnings("unchecked")
		Tasque<E>[] tArr = tasques.toArray(new Tasque[0]);
		int c = 0;
		for(Tasque<E> k : tArr) {
			x += k.toString() + (c==tArr.length-1?"":", ");
			c++;
		}
		return "Manager of <"+x+">";
	}
	public Tasque<E> pullFirst() {
		return tasques.remove(0);
	}
	public Tasque<E> pullLast() {
		return tasques.remove(tasques.size()-1);
	}
}