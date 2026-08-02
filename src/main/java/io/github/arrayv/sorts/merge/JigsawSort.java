package io.github.arrayv.sorts.merge;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(
    name = "My",
    category = "Category Sorts"
)
public class JigsawSort extends Sort {
    public JigsawSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    public int randInt(int a, int b, Random rng) {
        return rng.nextInt(b - a) + a;
    }

    public void mergeSort(int[] array, int a, int b) {
        if (b - a < 2) return;
        ArrayList<LinkedList<Integer>> aLinkedLists = new ArrayList<>();
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {

    }
}
