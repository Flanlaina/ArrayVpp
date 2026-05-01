package io.github.arrayv.sorts.templates;
import io.github.arrayv.main.ArrayVisualizer;

public abstract class ParallelSort extends Sort implements Parallelize {
	protected ParallelSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}
}