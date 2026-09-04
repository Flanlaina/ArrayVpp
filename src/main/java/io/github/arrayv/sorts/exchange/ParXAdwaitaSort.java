package io.github.arrayv.sorts.exchange;

import java.util.BitSet;

import io.github.arrayv.main.ArrayVisualizer;
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
 * @author Flanlaina
 * @author Control
 * @author mg-2018
 * @author PCBoy
 *
 */
public class ParXAdwaitaSort extends Sort {
    public ParXAdwaitaSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Par(X) Adwaita");
        this.setRunAllSortsName("Par(X) Adwaita Sort");
        this.setRunSortName("Par(X) Adwaita Sort");
        this.setCategory("Impractical Sorts");
        this.setAuthors("Control, Flanlaina, mg-2018, PCBoy");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the initial division constant for this sort (input / 100):", 300);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 100) return 100;
        return answer;
    }

    // Mess with these and see what you can come up with.
    // Both of these are used in line 108.
    double mult = 1.5;

    protected double threshold(int x) {
        return Math.sqrt(x);
    }

    protected int findDisparity(int[] array, int a, int b) {
        int n = b - a;
        BitSet max = new BitSet(n);
        int maxIdx = 0;
        for (int i = 1; i < n; i++) {
            if (Reads.compareIndices(array, a + i, a + maxIdx, 0, false) > 0) {
                maxIdx = i;
                max.set(i);
            }
        }
        int i = n - 1;
        int p = 1;
        int j = n - 1;
        while (j >= 0 && i >= p) {
            while (!max.get(j) && j > 0) j--;
            maxIdx = j;
            while (Reads.compareIndices(array, a + i, a + maxIdx, 0, false) > 0 && i >= p) i--;
            if (Reads.compareIndices(array, a + i, a + j, 0, false) <= 0 && p < i - j) p = i - j;
            j--;
        }
        return p;
    }

    protected int shellPass(int[] array, int a, int b, int gap, int par, int lastgap) {
        if (gap >= lastgap) return lastgap;
        if (gap == lastgap - 1 && gap != 1) return lastgap;
        for (int k = 0; k < gap; k++) {
            for (int i = a + k + gap; i < b;) {
                if (Reads.compareIndices(array, i - gap, i, 0.5, true) > 0) {
                    Writes.swap(array, i - gap, i, 0.5, true, false);
                    if (i > a + k + gap) i -= gap;
                } else i += gap;
            }
        }
        Highlights.clearAllMarks();
        return gap;
    }

    public void shellSort(int[] array, int a, int b, double constDiv) {
        double truediv = constDiv;
        int lastpar = b - a;
        int lastgap = b - a;
        while (true) {
            int par = findDisparity(array, a, b);
            int passpar = par;
            if (par >= lastpar)  par = lastpar - (int) truediv;
            if (par / (int) truediv <= 1) {
                shellPass(array, a, b, 1, par, lastgap);
                break;
            }
            lastgap = shellPass(array, a, b, (int) ((par / (int) truediv) + par % (int) truediv), passpar, lastgap);
            if (lastpar - par <= threshold(lastpar)) truediv *= mult;
            lastpar = par;
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int constDiv) throws Exception {
        shellSort(array, 0, sortLength, constDiv / 100d);
    }
}
