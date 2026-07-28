package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2022-2026 Flanlaina, Sorting Algorithm Scarlet

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
 * An adaptive stable block merge sort with O(sqrt(n)) dynamic external buffer.
 * <p>
 * To use this algorithm in another, use {@code blockMergeSort()} from a
 * reference instance.
 * 
 * @author Flanlaina
 * @author aphitorite
 *
 */
public class AdaptiveEctaSort extends Sort {

    public AdaptiveEctaSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Adaptive Ecta");
        this.setRunAllSortsName("Adaptive Ecta Sort");
        this.setRunSortName("Adaptive Ectasort");
        this.setCategory("Hybrid Sorts");
        this.setAuthors("aphitorite, Flanlaina");
        this.setConstant("n log n");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    // adaptive stable merge sort with O(sqrt(n)) dynamic external buffer

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        if (a != b) {
            int temp = array[a];
            int d = (a > b) ? -1 : 1;
            for (int i = a; i != b; i += d)
                Writes.write(array, i, array[i + d], 0.5, true, false);
            Writes.write(array, b, temp, 0.5, true, false);
        }
    }

    protected void rotate(int[] array, int a, int m, int b) {
        Highlights.clearAllMarks();
        if (a == m || m == b) return;
        int p0 = a, p1 = m - 1, p2 = m, p3 = b - 1;
        int tmp;
        while (p0 < p1 && p2 < p3) {
            tmp = array[p1];
            Writes.write(array, p1--, array[p0], 0.5, true, false);
            Writes.write(array, p0++, array[p2], 0.5, true, false);
            Writes.write(array, p2++, array[p3], 0.5, true, false);
            Writes.write(array, p3--, tmp, 0.5, true, false);
        }
        while (p0 < p1) {
            tmp = array[p1];
            Writes.write(array, p1--, array[p0], 0.5, true, false);
            Writes.write(array, p0++, array[p3], 0.5, true, false);
            Writes.write(array, p3--, tmp, 0.5, true, false);
        }
        while (p2 < p3) {
            tmp = array[p2];
            Writes.write(array, p2++, array[p3], 0.5, true, false);
            Writes.write(array, p3--, array[p0], 0.5, true, false);
            Writes.write(array, p0++, tmp, 0.5, true, false);
        }
        if (p0 < p3) { // don't count reversals that don't do anything
            if (p3 - p0 >= 3) Writes.reversal(array, p0, p3, 1, true, false);
            else Writes.swap(array, p0, p3, 1, true, false);
            Highlights.clearMark(2);
        }
    }
    
    protected int binSearch(int[] array, int a, int b, int val, boolean left) {
        while (a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            int c = Reads.compareValues(val, array[m]);
            if (c < 0 || (left && c == 0)) b = m;
            else a = m + 1;
        }
        return a;
    }

    protected int minExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left) while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) > 0) i *= 2;
        else while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0) i *= 2;
        return binSearch(array, a + i / 2, Math.min(b, a - 1 + i), val, left);
    }

    protected int maxExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left) while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0) i *= 2;
        else while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0) i *= 2;
        return binSearch(array, Math.max(a, b - i + 1), b - i / 2, val, left);
    }

    protected boolean buildRuns(int[] array, int a, int b, int mRun) {
        int i = a + 1, j = a;
        boolean noSort = true;
        while (i < b) {
            if (Reads.compareIndices(array, i - 1, i++, 1, true) > 0) {
                while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) > 0) i++;
                if (i - j < 4) Writes.swap(array, j, i - 1, 1.0, true, false);
                else Writes.reversal(array, j, i - 1, 1.0, true, false);
            } else while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) <= 0) i++;
            if (i < b) {
                noSort = false;
                j = i - (i - j - 1) % mRun - 1;
            }
            while (i - j < mRun && i < b) {
                insertTo(array, i, binSearch(array, j, i, array[i], false));
                i++;
            }
            j = i++;
        }
        return noSort;
    }

    protected void mergeTo(int[] from, int[] to, int a, int m, int b, int p, boolean aux) {
        int i = a, j = m;
        while(i < m && j < b) {
            Highlights.markArray(2, i);
            Highlights.markArray(3, j);
            if(Reads.compareValues(from[i], from[j]) <= 0)
                Writes.write(to, p++, from[i++], 1, true, aux);
            else
                Writes.write(to, p++, from[j++], 1, true, aux);
        }
        Highlights.clearMark(3);
        while(i < m) {
            Highlights.markArray(2, i);
            Writes.write(to, p++, from[i++], 1, true, aux);
        }
        while(j < b) {
            Highlights.markArray(2, j);
            Writes.write(to, p++, from[j++], 1, true, aux);
        }
        Highlights.clearMark(2);
    }

    protected void mergeFWExt(int[] array, int[] tmp, int a, int m, int b) {
        int s = m - a;
        Writes.arraycopy(array, a, tmp, 0, s, 1, true, true);
        int i = 0, j = m;
        while (i < s && j < b) {
            if (Reads.compareValues(tmp[i], array[j]) <= 0)
                Writes.write(array, a++, tmp[i++], 1, true, false);
            else
                Writes.write(array, a++, array[j++], 1, true, false);
        }
        while (i < s) Writes.write(array, a++, tmp[i++], 1, true, false);
    }

    protected void mergeBWExt(int[] array, int[] tmp, int a, int m, int b) {
        int s = b - m;
        Writes.arraycopy(array, m, tmp, 0, s, 1, true, true);
        int i = s - 1, j = m - 1;
        while (i >= 0 && j >= a) {
            if (Reads.compareValues(tmp[i], array[j]) >= 0)
                Writes.write(array, --b, tmp[i--], 1, true, false);
            else
                Writes.write(array, --b, array[j--], 1, true, false);
        }
        while (i >= 0) Writes.write(array, --b, tmp[i--], 1, true, false);
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

    protected void merge(int[] array, int[] buf, int a, int m, int b) {
        Highlights.clearMark(2);
        if (Math.min(m - a, b - m) <= 8) {
            if (m - a > b - m)
                inPlaceMergeBW(array, a, m, b);
            else
                inPlaceMergeFW(array, a, m, b);
        } else if (m - a > b - m)
            mergeBWExt(array, buf, a, m, b);
        else
            mergeFWExt(array, buf, a, m, b);
    }

    protected void blockCycle(int[] array, int[] buf, int[] keys, int a, int bLen, int bCnt) {
        for (int i = 0; i < bCnt; i++) {
            if (Reads.compareOriginalValues(i, keys[i]) != 0) {
                Writes.arraycopy(array, a + i * bLen, buf, 0, bLen, 1, true, true);
                int j = i, next = keys[i];
                do {
                    Writes.arraycopy(array, a + next * bLen, array, a + j * bLen, bLen, 1, true, false);
                    Writes.write(keys, j, j, 1, true, true);
                    j = next;
                    next = keys[next];
                } while (Reads.compareOriginalValues(next, i) != 0);
                Writes.arraycopy(buf, 0, array, a + j * bLen, bLen, 1, true, false);
                Writes.write(keys, j, j, 1, true, true);
            }
        }
    }

    // precondition: m-a is >= bLen and divisible by bLen
    protected void blockMergeHelper(int[] array, int[] buf, int[] tags, int a, int m, int b, int bLen) {
        int c = 0, t = 2;
        int i = a, j = m, k = 0;
        int l = 0, r = 0;
        while (c++ < 2 * bLen) { // merge 2 blocks into buffer to create 2 buffers
            Highlights.markArray(2, i);
            Highlights.markArray(3, j);
            if (Reads.compareValues(array[i], array[j]) <= 0) {
                Writes.write(buf, k++, array[i++], 1, true, true);
                l++;
            } else {
                Writes.write(buf, k++, array[j++], 1, true, true);
                r++;
            }
        }
        boolean left = l >= r;
        k = left ? i - l : j - r;
        c = 0;
        do {
            if (i < m) Highlights.markArray(2, i);
            else Highlights.clearMark(2);
            if (j < b) Highlights.markArray(3, j);
            else Highlights.clearMark(3);
            if (i < m && (j == b || Reads.compareValues(array[i], array[j]) <= 0)) {
                Writes.write(array, k++, array[i++], 1, true, false);
                l++;
            } else {
                Writes.write(array, k++, array[j++], 1, true, false);
                r++;
            }
            if (++c == bLen) { // change buffer after every block
                Writes.write(tags, t++, (k - a) / bLen - 1, 0, false, true);
                if (left) l -= bLen;
                else r -= bLen;
                left = l >= r;
                k = left ? i - l : j - r;
                c = 0;
            }
        } while (i < m || j < b);
        Highlights.clearAllMarks();
        int b1 = b - c;
        Writes.arraycopy(array, k - c, array, b1, c, 1, true, false); // copy remainder to end (r buffer)
        r -= c;
        // l and r buffers are divisible by bLen
        t = 0;
        k = 0;
        while (l > 0) {
            Writes.arraycopy(buf, k, array, m - l, bLen, 1, true, false);
            Writes.write(tags, t++, (m - a - l) / bLen, 0, false, true);
            k += bLen;
            l -= bLen;
        }
        while (r > 0) {
            Writes.arraycopy(buf, k, array, b1 - r, bLen, 1, true, false);
            Writes.write(tags, t++, (b1 - a - r) / bLen, 0, false, true);
            k += bLen;
            r -= bLen;
        }
        blockCycle(array, buf, tags, a, bLen, (b - a) / bLen);
    }

    protected void smartMerge(int[] array, int[] buf, int a, int m, int b) {
        if (Reads.compareValues(array[m - 1], array[m]) <= 0) return;
        a = minExpSearch(array, a, m, array[m], false);
        b = maxExpSearch(array, m, b, array[m - 1], true);
        if (Reads.compareValues(array[a], array[b - 1]) > 0) {
            rotate(array, a, m, b);
            return;
        }
        merge(array, buf, a, m, b);
    }

    protected void smartInPlaceMerge(int[] array, int a, int m, int b) {
        if (Reads.compareValues(array[m - 1], array[m]) <= 0) return;
        a = minExpSearch(array, a, m, array[m], false);
        b = maxExpSearch(array, m, b, array[m - 1], true);
        if (Reads.compareValues(array[a], array[b - 1]) > 0) {
            rotate(array, a, m, b);
            return;
        }
        if (b - m < m - a) inPlaceMergeBW(array, a, m, b);
        else inPlaceMergeFW(array, a, m, b);
    }
    
    protected void pingPongMerge(int[] array, int[] buf, int a, int m1, int m2, int m3, int b) {
        int p = 0, p1 = p + m2-a, pEnd = p + b-a;
        if(Reads.compareIndices(array, m1-1, m1, 1, true) > 0
        || (m3 < b && Reads.compareIndices(array, m3-1, m3, 1, true) > 0)) {
            mergeTo(array, buf, a, m1, m2, p, true);
            mergeTo(array, buf, m2, m3, b, p1, true);
            mergeTo(buf, array, p, p1, pEnd, a, false);
        } else smartMerge(array, buf, a, m2, b);
    }

    protected void blockMerge(int[] array, int[] buf, int[] tags, int a, int m, int b, int bLen) {
        if (Reads.compareValues(array[m - 1], array[m]) <= 0) return;
        int s = minExpSearch(array, a, m, array[m], false);
        b = maxExpSearch(array, m, b, array[m - 1], true);
        if (Reads.compareValues(array[a], array[b - 1]) > 0) {
            rotate(array, a, m, b);
            return;
        }
        if (Math.min(m - s, b - m) <= 2 * bLen) merge(array, buf, s, m, b);
        else blockMergeHelper(array, buf, tags, s - (s - a) % bLen, m, b, bLen);
    }

    /**
     * Sorts the range {@code [a, b)} of {@code array} using Adaptive Ectasort.
     *
     * @param array the array
     * @param a     the start of the range, inclusive
     * @param b     the end of the range, exclusive
     */
    public void blockMergeSort(int[] array, int a, int b) {
        int len = b - a;
        int j = 16;
        if (buildRuns(array, a, b, j)) return;
        if (len <= 64) { // adaptive lazy stable sort
            int i;
            for (; j < len; j *= 2)
                for (i = a; i + j < b; i += 2 * j)
                    smartInPlaceMerge(array, i, i + j, Math.min(i + 2 * j, b));
            return;
        }
        int bLen;
        for (bLen = j; bLen * bLen < len; bLen *= 2);
        int tLen = len / bLen, bufLen = 2 * bLen;
        int[] buf  = Writes.createExternalArray(bufLen);
        int[] tags = Writes.createExternalArray(tLen);
        int i;
        for (; 4 * j <= bufLen; j *= 4) {
            for(i = a; i+2*j < b; i += 4*j)
                pingPongMerge(array, buf, i, i+j, i+2*j, Math.min(i+3*j, b), Math.min(i+4*j, b));
            if (i + j < b)
                smartMerge(array, buf, i, i + j, b);
        }
        for (; j <= bufLen; j *= 2) {
            for (i = a; i + j < b; i += 2 * j)
                smartMerge(array, buf, i, i + j, Math.min(i + 2 * j, b));
        }
        for (; j < len; j *= 2) {
            for (i = a; i + j < b; i += 2 * j)
                blockMerge(array, buf, tags, i, i + j, Math.min(i + 2 * j, b), bLen);
        }
        Writes.deleteExternalArray(tags);
        Writes.deleteExternalArray(buf);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        blockMergeSort(array, 0, sortLength);

    }

}
