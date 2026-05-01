package io.github.arrayv.utils;
import java.util.Arrays;
import java.util.Collection;

/** DynamicArray: Try to make everything fit, regardless of awkwardness
  **/
public class DynamicArray {
	private Object[] Entries;
	public int len;
	public DynamicArray() {
		Entries = new Object[0];
		len = 0;
	}
	public DynamicArray(Collection<? extends Object> c) {
		Entries = c.toArray();
		len = Entries.length;
	}
	public DynamicArray(Object[] initial) {
		Entries = initial;
		len = Entries.length;
	}
	public DynamicArray(int startLength) {
		Entries = new Object[startLength];
		len = startLength;
	}
	public <T> void add(T element) {
		Entries = Arrays.copyOf(Entries, len+1);
		Entries[len++] = element;
	}
	public <T> void add(@SuppressWarnings("unchecked") T... elements) {
		Arrays.stream(elements).forEachOrdered(this::add);
	}
	public <T> void add(int index, T element) {
		Entries = Arrays.copyOf(Entries, len+1);
		for(int i = len-1; i>=index; i--) {
			Entries[i+1]=Entries[i];
		}
		Entries[index]=element;
	}
	public <T> void remove(T element) {
		boolean flag=false;
		for(int i=0; i<len-1; i++) {
			if(element==Entries[i]) {
				flag=true;
			}
			if(flag) {
				Entries[i]=Entries[i+1];
			}
		}
		if(Entries[len-1]==element) {
			flag=true;
		}
		if(flag) {
			Entries = Arrays.copyOf(Entries, len-1);
		}
	}
	public void remove(int index) {
		for(int i=index; i<len-1; i++) {
			Entries[i]=Entries[i+1];
		}
		if(index < len && index >= 0) {
			Entries = Arrays.copyOf(Entries, len-1);
		}
	}
	@SuppressWarnings("unchecked")
	public <T> T get(int index) {
		try {
			if(index < len && index >= 0)
				return (T) Entries[index];
			else
				throw new Exception("Invalid get position");
		} catch(Exception no) {
			no.printStackTrace();
			return (T) null;
		}
	}
	public <T> void set(int index, T element) {
		if(index < len && index >= 0)
			Entries[index] = element;
	}
	public void ensure(int newsize) {
		newsize = Math.max(newsize, len);
		Entries = Arrays.copyOf(Entries, newsize);
	}
	public <T> void fill(T filler) {
		Arrays.fill(Entries, filler);
	}
}