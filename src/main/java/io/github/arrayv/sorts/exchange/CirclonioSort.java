package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(
    name = "Circlonio",
    runName = "Circlonio Sort",
    category = "Exchange Sorts",
    authors = "yuji"
)
public class CirclonioSort extends Sort {
    public CirclonioSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

	private boolean circle(int[] array, int left, int right) {
        int a = left;
        int b = right;
        boolean swapped = false;
        while(a < b) {
        	if(Reads.compareIndices(array, a, b, 0.25, true) == 1) {
        		Writes.swap(array, a, b, 1, true, false);
        		swapped = true;
        	}
    		a++;
    		b--;
    		if(a==b) {
    			b++;
    		}
        }
        return swapped;
    }

    public void circlonio(int[] array, int l, int r, int mode) {
        if (l >= r) return;
        int m = l + (r - l) / 2, ml = l + (m - l) / 2, mr = m + (r - m) / 2;
        switch (mode) {
            case 0:
                circlonio(array, l, r, 3);
                circlonio(array, l, m, 0);
                circlonio(array, m + 1, r, 0);
                break;
            case 1:
                circlonio(array, l, m, 2);
                circlonio(array, m + 1, r, 2);
                circle(array, l, r);
                break;
            case 2:
                circle(array, l, r);
                circlonio(array, l, m, 1);
                circlonio(array, m + 1, r, 1);
                break;
            case 3:
            default:
                circlonio(array, l, r, 2);
                circlonio(array, ml, mr, 3);
                break;
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        circlonio(array, 0, sortLength - 1, 0);
    }
}
