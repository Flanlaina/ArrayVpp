package io.github.arrayv.utils;

public final class Tasque<E> {
	private E[] els;
	@SuppressWarnings("unchecked")
	public Tasque() {
		els = (E[]) new Object[0];
	}
	@SuppressWarnings("unchecked")
	public Tasque(E... tasque) {
		els = tasque;
	}
	public E getAttribute(int i) {
		return els[i];
	}
	public String toString() {
		String x = "";
		int c = 0;
		for(E k : els) {
			x+=k.toString()+(c==els.length-1?"":", ");
			c++;
		}
		return "meow{"+x+"}";
	}
}