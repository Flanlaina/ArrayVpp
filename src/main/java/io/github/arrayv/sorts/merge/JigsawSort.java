package io.github.arrayv.sorts.merge;

import java.util.ArrayList;
import java.util.Collections;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*

+---------------------------+
| SORTING ALGORITHM SCARLET |
+---------------------------+
|    A sorting algorithm    |
|    studio by Flanlaina    |
|    (a.k.a Ayako-chan)     |
+---------------------------+

 */

/**
 * A variant of Merge Sort that shuffles the sorted runs after each iterations.
 * <p>
 * To use this algorithm in another, use {@link #mergeSort(int[], int, int)}
 * from a reference instance.
 * 
 * @author 666666t
 * @author Flanlaina
 * 
 */
@SortMeta(
    name = "Jigsaw",
    runName = "Jigsaw Sort",
    category = "Merge Sorts",
    authors = "666666t, Flanlaina"
)
public class JigsawSort extends Sort {
    public JigsawSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    public int[] merge(int[] left, int[] right, int base) {
        int[] out = new int[left.length + right.length];
        int lp = 0, rp = 0;
        while (lp < left.length && rp < right.length) {
            Highlights.markArray(1, base + lp);
            Highlights.markArray(2, base + left.length + rp);
            if (Reads.compareValues(left[lp], right[rp]) <= 0) {
                Writes.write(out, lp + rp, left[lp++], 0.5, true, true);
            } else {
                Writes.write(out, lp + rp, right[rp++], 0.5, true, true);
            }
        }
        while (lp < left.length) {
            Highlights.markArray(1, base + lp);
            Writes.write(out, lp + rp, left[lp++], 0.5, true, true);
        }
        while (rp < right.length) {
            Highlights.markArray(2, base + left.length + rp);
            Writes.write(out, lp + rp, right[rp++], 0.5, true, true);
        }
        Highlights.clearMark(1);
        Highlights.clearMark(2);
        return out;
    }

    /**
     * Defines a sorted run from {@code array} starting at {@code start}.
     * 
     * @param array the array
     * @param a the start of the range, inclusive
     * @param b the end of the range, exclusive
     * @return An {@code int} with the index where the run gets cut off, the last index of the run exclusively.
     */
    public int findRun(int[] array, int a, int b) {
        int i = a + 1;
        while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0) i++;
        return i;
    }

    /**
     * Sorts the range {@code [a, b)} of {@code array} using Jigsaw Sort.
     *
     * @param array the array
     * @param a     the start of the range, inclusive
     * @param b     the end of the range, exclusive
     */
    public void mergeSort(int[] array, int a, int b) {
        int len = b - a;
        if (len < 2) return;
        ArrayList<int[]> puzzle = new ArrayList<>(), newPuzzle = new ArrayList<>();

        int r = a;
        while (r < b) { // search for runs
            int nr = findRun(array, r, b);
            int[] segment = new int[nr - r];
            Writes.arraycopy(array, r, segment, 0, nr - r, 1, true, true);
            Writes.changeAllocAmount(nr - r);
            puzzle.add(segment);
            r = nr;
        }
        while (puzzle.size() > 1) {
            int baselen = a, nSegments = puzzle.size();

            // merge adjacent runs
            for (int i = 0; i < nSegments; i += 2) {
                if (i + 1 >= nSegments) {
                    newPuzzle.add(puzzle.get(i));
                    continue;
                }
                int[] lArr = puzzle.get(i), rArr = puzzle.get(i + 1);
                int[] combined = merge(lArr, rArr, baselen);
                newPuzzle.add(combined);
                Writes.arraycopy(combined, 0, array, baselen, combined.length, 1, true, false);
                baselen += combined.length;
            }
            puzzle.clear();

            // swap the ArrayLists (on entry, `puzzle' has no elements)
            ArrayList<int[]> tmp = puzzle;
            puzzle = newPuzzle;
            newPuzzle = tmp;

            // shuffle the runs
            Collections.shuffle(puzzle);
            baselen = a;
            for (int[] is : puzzle) {
                Writes.arraycopy(is, 0, array, baselen, is.length, 1, true, false);
                baselen += is.length;
            }
        }
        Writes.changeAllocAmount(-len);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        mergeSort(array, 0, sortLength);
    }
}
