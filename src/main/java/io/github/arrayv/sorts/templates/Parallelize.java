package io.github.arrayv.sorts.templates;

import java.lang.reflect.Method;
import java.util.function.Function;

import io.github.arrayv.utils.StopSort;

public interface Parallelize {
	public class Func extends Thread {
		protected Function<Object[],?>   z;
		protected volatile Object[]      n;
		public volatile Object   returnVal;
		public Func(Object... r) {
			n = r;
		}
		public Func setConsumer(Function<Object[],?> c) {
			z = c;
			return this;
		}
		public void run() {
			returnVal = z.apply(n);
		}
	}
	// get the method in scope
	default public Object getMethod(String name, int param) {
		Method[] methodsInCls = this.getClass().getDeclaredMethods();
		for(Method i : methodsInCls) {
			if(i.getName() == name) {
				Class<?>[] params = i.getParameterTypes();
				if(params.length == param) {
					return i;
				}
			}
		}
		return null;
	}
	// run a method based off of name and args
	default public Object run(String name, Object[] data) {
		Method m = (Method) getMethod(name, data.length);
		if(m != null) {
			try {
				return m.invoke(this, data);
			} catch(Exception e) {
				if(e.getCause().getClass().equals(StopSort.class))
					return null;
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
}