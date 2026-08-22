package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.IndexedRotations;

/*
 * 
MIT License

Copyright (c) 2026 Flanlaina, Sorting Algorithm Scarlet

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */

/**
 * @author aphitorite
 * @author Flanlaina
 * 
 */
@SortMeta(
    name = "Online Satori",
    category = "Hybrid Sorts",
    authors = "aphitorite, Flanlaina"
)
public class OnlineSatoriSort extends Sort {
    public OnlineSatoriSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        int d = (a > b) ? -1 : 1;
        for (int i = a; i != b; i += d) Writes.write(array, i, array[i + d], 0.5, true, false);
        if (a != b) Writes.write(array, b, temp, 0.5, true, false);
    }

    protected void rotate(int[] array, int a, int m, int b) {
        Highlights.clearAllMarks();
        IndexedRotations.cycleReverse(array, a, m, b, 1, true, false);
    }

    public void segmentReversal(int[] array, int start, int end, double cSleep, double wSleep, boolean mark, boolean aux) {
        for (int i = start; i < end; i++) {
            int left = i;
            while (i < end && Reads.compareIndices(array, i, i + 1, cSleep, true) == 0) i++;
            int right = i;
            if (left != right) Writes.reversal(array, left, right, wSleep, mark, aux);
        }
    }

    protected int binSearch(int[] array, int a, int b, int val, boolean left) {
        while (a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            int c = Reads.compareValues(val, array[m]);
            if (c < 0 || (left && c == 0)) b = m;
            else                           a = m + 1;
        }
        return a;
    }

    protected int minExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left) while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >  0) i *= 2;
        else      while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0) i *= 2;
        int a1 = a + i / 2, b1 = Math.min(b, a - 1 + i);
        return binSearch(array, a1, b1, val, left);
    }

    protected int maxExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left) while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0) i *= 2;
        else      while (b - i >= a && Reads.compareValues(val, array[b - i]) <  0) i *= 2;
        int a1 = Math.max(a, b - i + 1), b1 = b - i / 2;
        return binSearch(array, a1, b1, val, left);
    }

    public int findRun(int[] array, int start, int end) {
        int i = start + 1;
        if (i == end) return i;
        boolean lessunique = false;
        boolean different = false;
        int cmp = Reads.compareIndices(array, i - 1, i, 0.5, true);
        while (cmp == 0 && i < end) {
            lessunique = true;
            i++;
            if (i < end) cmp = Reads.compareIndices(array, i - 1, i, 0.5, true);
        }
        if (cmp > 0) {
            while (cmp >= 0 && i < end) {
                if (cmp == 0) lessunique = true;
                else different = true;
                i++;
                if (i < end) cmp = Reads.compareIndices(array, i - 1, i, 0.5, true);
            }
            if (i - start > 1 && different) {
                Writes.reversal(array, start, i - 1, 0.75, true, false);
                if (lessunique) segmentReversal(array, start, i - 1, 0.5, 0.75, true, false);
            }
        } else {
            while (cmp <= 0 && i < end) {
                i++;
                if (i < end) cmp = Reads.compareIndices(array, i - 1, i, 0.5, true);
            }
        }
        return i;
    }

    protected void insertSort(int[] array, int a, int b) {
        for (int i = findRun(array, a, b); i < b; i++)
            insertTo(array, i, binSearch(array, a, i, array[i], false));
    }

    protected void inPlaceMergeFW(int[] array, int a, int m, int b) {
        while (a < m && m < b) {
            int i = minExpSearch(array, m, b, array[a], true);
            rotate(array, a, m, i);
            int t = i - m;
            m = i;
            a += t + 1;
            if (m >= b) break;
            a = minExpSearch(array, a, m, array[m], false);
        }
    }

    protected void inPlaceMergeBW(int[] array, int a, int m, int b) {
        while (b > m && m > a) {
            int i = maxExpSearch(array, a, m, array[b - 1], false);
            rotate(array, i, m, b);
            int t = m - i;
            m = i;
            b -= t + 1;
            if (m <= a) break;
            b = maxExpSearch(array, m, b, array[m - 1], true);
        }
    }

    public void inPlaceMerge(int[] array, int a, int m, int b) {
        if (a == m || m == b) return;
        if (Reads.compareValues(array[m - 1], array[m]) <= 0) return;
        a = minExpSearch(array, a, m, array[m], false);
        b = maxExpSearch(array, m, b, array[m - 1], true);
        if (Reads.compareValues(array[a], array[b - 1]) > 0) {
            rotate(array, a, m, b);
            return;
        }
        if (b - m <= m - a) inPlaceMergeBW(array, a, m, b);
        else                inPlaceMergeFW(array, a, m, b);
    }

    protected void fragmentedMergeBW(int[] array, int a, int m, int b, int s) {
        if (a == m || m == b) return;
        if (Reads.compareValues(array[m - 1], array[m]) <= 0) return;
        a = minExpSearch(array, a, m, array[m], false);
        b = maxExpSearch(array, m, b, array[m - 1], true);
        if (Reads.compareValues(array[a], array[b - 1]) > 0) {
            rotate(array, a, m, b);
            return;
        }
        while (b - m > s) {
            int rPos;
            int dist;
            int b1 = b - s;
            rPos = binSearch(array, a, m, array[b1 - 1], false);
            rotate(array, rPos, m, b1);
            dist = m - rPos;
            b1 -= dist;
            m -= dist;
            inPlaceMerge(array, b1, b1 + dist, b);
            b = b1;
        }
        inPlaceMerge(array, a, m, b);
    }

    public void laziestSort(int[] array, int a, int b, int s) {
        for (int i = a; i < b; i += s) {
            int j = Math.min(i + s, b);
            insertSort(array, i, j);
            if (i > a) inPlaceMerge(array, a, i, j);
        }
    }

    public void lazierestSort(int[] array, int a, int b) {
        int s = 4, s1 = 16, j = 128;
        for (int i = a; i < b; i += s1) {
            if (i - a == j) {
                s *= 2;
                s1 *= 4;
                j *= 8;
            }
            int e = Math.min(i + s1, b);
            laziestSort(array, i, e, s);
            fragmentedMergeBW(array, a, i, e, s);
        } 
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        lazierestSort(array, 0, sortLength);
    }
}
