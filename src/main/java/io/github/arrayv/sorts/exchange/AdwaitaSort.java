package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

Gets its name from the Adwaita design language used by GNOME and its shell. And that's exactly what
this sort is: Gnome and Shell.

*/
public class AdwaitaSort extends Sort {

    int len = 0;

    public AdwaitaSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Adwaita");
        this.setRunAllSortsName("Adwaita Sort");
        this.setRunSortName("Adwaita Sort");
        this.setCategory("Impractical Sorts");
        this.setAuthors("PCBoy");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void adwaitaPass(int[] array, int start, int end, int gap) {
        for (int i = start; i + gap < end; i++) {
            if (Reads.compareIndices(array, i, i + gap, 0.01, true) > 0) {
                Writes.swap(array, i, i + gap, 0.5, true, false);
                if (i - (gap + 1) >= start) i -= gap + 1;
                else i = start - 1;
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        // You could probably guess what this sequence is since I'm the one writing this code.
        // There are better sequences. And that fact frustrates me. Machoota... I'm looking at you.
        int[] gs = {1, 4, 10, 23, 57, 132, 301, 701, 1636, 3657, 8172, 18235, 40764, 91064, 203519, 454741, 1016156, 2270499, 5073398, 11335582, 25328324, 56518561, 126451290, 282544198, 631315018};
        for (int g = gs.length - 1; g >= 0; g--) if (gs[g] / 1.73 < currentLength) {
            arrayVisualizer.setExtraHeading(" / GAP: " + gs[g]);
            adwaitaPass(array, 0, currentLength, gs[g]);
        }
    }
}