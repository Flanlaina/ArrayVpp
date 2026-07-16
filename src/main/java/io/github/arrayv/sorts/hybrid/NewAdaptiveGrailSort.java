package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*
 * MIT License
 *
 * Copyright (c) 2013 Andrey Astrelin
 * Copyright (c) 2020 aphitorite, The Holy Grail Sort Project
 * Copyright (c) 2026 Flanlaina, Sorting Algorithm Scarlet
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/**
 * Adaptive Grail Sort - O(1) space stable worst case O(n log n) algorithm
 * designed to take advantage of partially ordered data with constant memory.
 * <p>
 * To use this algorithm in another, use {@code blockMergeSort()} from a
 * reference instance.
 *
 * @author Flanlaina
 * @author aphitorite
 *
 */
public class NewAdaptiveGrailSort extends Sort {
    public NewAdaptiveGrailSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Adaptive Grail (New)");
        this.setRunAllSortsName("Adaptive Grail Sort");
        this.setRunSortName("Adaptive Grailsort");
        this.setCategory("Hybrid Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int tLenCalc(int n, int bLen) {
        int n1 = n - bLen;
        int a = 0, b = bLen;

        while (a < b) {
            int m = (a + b) / 2;
            if (n1 - m < (m + 2) * bLen) b = m;
            else                         a = m + 1;
        }
        return a;
    }

    private void blockSwap(int[] array, int a, int b, int len) {
        for (int i = 0; i < len; i++) Writes.swap(array, a+i, b+i, 1, true, false);
    }

    //changes len sized blocks order ABC -> BCA
    private void blockTriSwap(int[] array, int a, int b, int c, int len) {
        Highlights.clearMark(2);
        double delay = 0.333;
        for(int i = 0; i < len; i++) {
            int temp = array[a+i];
            Writes.write(array, a+i, array[b+i], delay, true, false);
            Writes.write(array, b+i, array[c+i], delay, true, false);
            Writes.write(array, c+i, temp, delay, true, false);
        }
    }

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        int d = (a > b) ? -1 : 1;
        for (int i = a; i != b; i += d)
            Writes.write(array, i, array[i + d], 0.5, true, false);
        if (a != b) Writes.write(array, b, temp, 0.5, true, false);
    }

    private void shiftFW(int[] array, int a, int m, int b) {
        while(m < b) Writes.swap(array, a++, m++, 1, true, false);
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

    // Easy patch to avoid the "reversals can be done in a single swap" notes.
    protected void reverse(int[] array, int a, int b) {
        if (b - a >= 3) Writes.reversal(array, a, b, 1, true, false);
        else Writes.swap(array, a, b, 1, true, false);
    }

    protected int buildUniqueRunFW(int[] array, int a, int n) {
        int nKeys = 1, i = a + 1;
        // build run at start
        if (Reads.compareIndices(array, i - 1, i, 1, true) < 0) {
            do {
                i++;
                nKeys++;
            } while (nKeys < n && Reads.compareIndices(array, i - 1, i, 1, true) < 0);
        } else if (Reads.compareIndices(array, i - 1, i, 1, true) > 0) {
            do {
                i++;
                nKeys++;
            } while (nKeys < n && Reads.compareIndices(array, i - 1, i, 1, true) > 0);
            reverse(array, a, i - 1);
        }
        return nKeys;
    }

    protected int buildUniqueRunBW(int[] array, int b, int n) {
        int nKeys = 1, i = b - 1;
        // build run at end
        if (Reads.compareIndices(array, i - 1, i, 1, true) < 0) {
            do {
                i--;
                nKeys++;
            } while (nKeys < n && Reads.compareIndices(array, i - 1, i, 1, true) < 0);
        } else if (Reads.compareIndices(array, i - 1, i, 1, true) > 0) {
            do {
                i--;
                nKeys++;
            } while (nKeys < n && Reads.compareIndices(array, i - 1, i, 1, true) > 0);
            reverse(array, i, b - 1);
        }
        return nKeys;
    }

    protected int findKeysFW(int[] array, int a, int b, int nKeys, int n) {
        int p = a, pEnd = a + nKeys;
        Highlights.clearMark(2);
        for (int i = pEnd; i < b && nKeys < n; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(1);
            int loc = binSearch(array, p, pEnd, array[i], true);
            if (pEnd == loc || Reads.compareValues(array[i], array[loc]) != 0) {
                rotate(array, p, pEnd, i);
                int inc = i - pEnd;
                loc += inc;
                p += inc;
                pEnd += inc;
                insertTo(array, pEnd, loc);
                nKeys++;
                pEnd++;
            }
        }
        rotate(array, a, p, pEnd);
        return nKeys;
    }

    protected int findKeysBW(int[] array, int a, int b, int nKeys, int n) {
        int p = b - nKeys, pEnd = b;
        Highlights.clearMark(2);
        for (int i = p - 1; i >= a && nKeys < n; i--) {
            Highlights.markArray(1, i);
            Delays.sleep(1);
            int loc = binSearch(array, p, pEnd, array[i], true);
            if (pEnd == loc || Reads.compareValues(array[i], array[loc]) != 0) {
                rotate(array, i + 1, p, pEnd);
                int inc = p - (i + 1);
                loc -= inc;
                pEnd -= inc;
                p -= inc + 1;
                nKeys++;
                insertTo(array, i, loc - 1);
            }
        }
        rotate(array, p, pEnd, b);
        return nKeys;
    }

    protected boolean buildRuns(int[] array, int a, int b, int mRun) {
        int i = a + 1, j = a;
        boolean noSort = true;
        while (i < b) {
            if (Reads.compareIndices(array, i - 1, i++, 1, true) > 0) {
                while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) > 0) i++;
                reverse(array, j, i - 1);
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

    protected void insertSort(int[] array, int a, int b) {
        buildRuns(array, a, b, b - a);
    }

    protected void mergeFW(int[] array, int a, int m, int b, int p) {
        int pLen = m - a;
        blockSwap(array, a, p, pLen);
        int i = 0, j = m, k = a;
        while (i < pLen && j < b) {
            if (Reads.compareValues(array[p + i], array[j]) <= 0)
                Writes.swap(array, k++, p + (i++), 1, true, false);
            else
                Writes.swap(array, k++, j++, 1, true, false);
        }
        while (i < pLen) Writes.swap(array, k++, p + (i++), 1, true, false);
    }

    protected void mergeBW(int[] array, int a, int m, int b, int p) {
        int pLen = b - m;
        blockSwap(array, m, p, pLen);
        int i = pLen - 1, j = m - 1, k = b - 1;
        while (i >= 0 && j >= a) {
            if (Reads.compareValues(array[p + i], array[j]) >= 0)
                Writes.swap(array, k--, p + (i--), 1, true, false);
            else
                Writes.swap(array, k--, j--, 1, true, false);
        }
        while (i >= 0) Writes.swap(array, k--, p + (i--), 1, true, false);
    }

    protected void inPlaceMergeFW(int[] array, int a, int m, int b) {
        while (a < m && m < b) {
            int i = binSearch(array, m, b, array[a], true);
            rotate(array, a, m, i);
            int t = i - m;
            m = i;
            a += t + 1;
            if (m == b) break;
            a = binSearch(array, a, m, array[m], false);
        }
    }

    protected void inPlaceMergeBW(int[] array, int a, int m, int b, boolean fwEq) {
        while (b > m && m > a) {
            int i = binSearch(array, a, m, array[b - 1], !fwEq);
            rotate(array, i, m, b);
            int t = m - i;
            m = i;
            b -= t + 1;
            if (m == a) break;
            b = binSearch(array, m, b, array[m - 1], fwEq);
        }
    }

    public void inPlaceMerge(int[] array, int a, int m, int b) {
        if (b - m < m - a) inPlaceMergeBW(array, a, m, b, true);
        else inPlaceMergeFW(array, a, m, b);
    }

    public void merge(int[] array, int a, int m, int b, int p) {
        if (b - m < m - a) mergeBW(array, a, m, b, p);
        else mergeFW(array, a, m, b, p);
    }

    void mergeTo(int[] array, int a, int m, int b, int p) {
        int i = a, j = m;

        while(i < m && j < b) {
            if(Reads.compareValues(array[i], array[j]) <= 0)
                Writes.swap(array, p++, i++, 1, true, false);
            else
                Writes.swap(array, p++, j++, 1, true, false);
        }
        while(i < m) Writes.swap(array, p++, i++, 1, true, false);
        while(j < b) Writes.swap(array, p++, j++, 1, true, false);
    }

    // returns mKey final position
    private int blockSelect(int[] array, int p, int t, int r, int d, int lCount, int bCount, int bLen) {
        int mKey = lCount;

        for (int j = 0, k = lCount + 1; j < k - 1; j++) {
            int min = j;

            for (int i = Math.max(lCount - r, j + 1); i < k; i++) {
                int comp = Reads.compareIndices(array, p + d + i * bLen, p + d + min * bLen, 2, true);

                if (comp < 0 || (comp == 0 && Reads.compareValues(array[t + i], array[t + min]) < 0)) min = i;
            }

            if (min != j) {
                this.blockSwap(array, p + j * bLen, p + min * bLen, bLen);
                Writes.swap(array, t + j, t + min, 1, true, false);

                if (k < bCount && min == k - 1) k++;
            }
            if (min == mKey) mKey = j;
        }

        return t + mKey;
    }

    //special thanks to @Anonymous0726 for this idea
    private void grailSortKeys(int[] array, int b, int p, int mKey) {
        Writes.swap(array, p, mKey, 1, true, false);
        int i = mKey, j = i+1, k = p+1;

        while(j < b) {
            if(Reads.compareValues(array[j], array[p]) < 0)
                Writes.swap(array, i++, j, 1, true, false);

            else Writes.swap(array, k++, j, 1, true, false);

            j++;
        }

        this.blockSwap(array, i, p, b-i);
    }

    private void grailSortKeysWithoutBuf(int[] array, int b, int mKey) {
        int i = mKey, j = i+1;

        while(j < b) {
            if(Reads.compareValues(array[j], array[i]) < 0)
                this.insertTo(array, j, i++);

            j++;
        }
    }

    protected int mergeBlocks(int[] array, int p, int a, int m, boolean fwEq) {
        int i = m;

        while(a < m) {
            int cmp = Reads.compareIndices(array, a, i, 0.0, true);
            if (cmp < 0 || (fwEq && cmp == 0))
                Writes.swap(array, p++, a++, 1, true, false);
            else
                Writes.swap(array, p++, i++, 1, true, false);
        }

        return i;
    }

    protected void smartTailMerge(int[] array, int p, int a, int m, int b, int bufPos, int bLen) {
        int i = m;

        while (a < m && i < b) {
            if (Reads.compareIndices(array, a, i, 0.0, true) <= 0)
                Writes.swap(array, p++, a++, 1, true, false);
            else
                Writes.swap(array, p++, i++, 1, true, false);
        }
        if (a < m) {
            if (a > p) this.shiftFW(array, p, a, m);
            blockSwap(array, bufPos, b - bLen, bLen);
        } else {
            a = 0;

            while (a < bLen && i < b) {
                Highlights.markArray(2, i);

                if (Reads.compareValues(array[bufPos + a], array[i]) <= 0)
                    Writes.swap(array, p++, bufPos + a++, 1, true, false);
                else
                    Writes.swap(array, p++, i++, 1, true, false);
            }
            Highlights.clearMark(2);
            while (a < bLen) Writes.swap(array, p++, bufPos + a++, 1, true, false);
        }
    }

    private void blockMergeHelper(int[] array, int a, int m, int b, int t, int p, int bLen) {
        int b1 = b - (b - m - 1) % bLen - 1, a1 = a + bLen,
            lCnt = (m - a1) / bLen, bCnt = (b1 - a1) / bLen;

        this.blockTriSwap(array, p, m-bLen, a, bLen);
        this.insertTo(array, t, t+lCnt-1);

        int mKey = this.blockSelect(array, a1, t, 1, bLen-1, lCnt, bCnt, bLen);
        int f = a1;
        boolean leftFrag = Reads.compareValues(array[t], array[mKey]) < 0;
        for (int i = 1; i < bCnt; i++) {
            int nxt = a1 + i * bLen;
            if (leftFrag ^ (Reads.compareValues(array[t + i], array[mKey]) < 0)) {
                f = mergeBlocks(array, f - bLen, f, nxt, leftFrag);
                leftFrag = !leftFrag;
            }
        }
        this.smartTailMerge(array, f - bLen, f, leftFrag ? b1 : f, b, p, bLen);
        grailSortKeys(array, t + bCnt, p, mKey);
    }

    private void blockMergeNoBufHelper(int[] array, int a, int m, int b, int t, int bLen) {
        int b1 = b - (b - m) % bLen,
            lCnt = (m - a) / bLen, bCnt = (b1 - a) / bLen;
        int mKey = this.blockSelect(array, a, t, 0, bLen-1, lCnt, bCnt, bLen);
        int f = a;
        boolean left = Reads.compareValues(array[t], array[mKey]) < 0;
        for (int i = 1; i < bCnt; i++) {
            int nxt = a + i * bLen;
            if (left ^ (Reads.compareValues(array[t + i], array[mKey]) < 0)) {
                int nxtE = this.binSearch(array, nxt, nxt + bLen, array[nxt - 1], left);
                inPlaceMergeBW(array, f, nxt, nxtE, left);
                f = nxtE;
                left = !left;
            }
        }
        if (left) inPlaceMergeBW(array, f, b1, b, true);
        grailSortKeysWithoutBuf(array, t + bCnt, mKey);
    }

    public void smartInPlaceMerge(int[] array, int a, int m, int b) {
        if (Reads.compareValues(array[m - 1], array[m]) <= 0) return;
        a = minExpSearch(array, a, m, array[m], false);
        b = maxExpSearch(array, m, b, array[m - 1], true);
        if (Reads.compareValues(array[a], array[b - 1]) > 0) {
            rotate(array, a, m, b);
            return;
        }
        inPlaceMerge(array, a, m, b);
    }

    public void smartMerge(int[] array, int a, int m, int b, int p) {
        if (Reads.compareValues(array[m - 1], array[m]) <= 0) return;
        a = minExpSearch(array, a, m, array[m], false);
        b = maxExpSearch(array, m, b, array[m - 1], true);
        if (Reads.compareValues(array[a], array[b - 1]) > 0) {
            rotate(array, a, m, b);
            return;
        }
        merge(array, a, m, b, p);
    }

    void pingPongMerge(int[] array, int a, int m1, int m, int m2, int b, int p) {
        int p1 = p+m-a, pEnd = p+b-a;
        if (Reads.compareIndices(array, m1-1, m1, 1, true) > 0
                || (m2 < b && Reads.compareIndices(array, m2-1, m2, 1, true) > 0)) {
            this.mergeTo(array, a, m1, m, p);
            this.mergeTo(array, m, m2, b, p1);
            this.mergeTo(array, p, p1, pEnd, a);
        } else smartMerge(array, a, m, b, p);
    }

    private void blockMerge(int[] array, int a, int m, int b, int t, int p, int bLen) {
        if (Reads.compareValues(array[m - 1], array[m]) <= 0) return;
        int s = minExpSearch(array, a, m, array[m], false);
        b = maxExpSearch(array, m, b, array[m - 1], true);
        if (Reads.compareValues(array[s], array[b - 1]) > 0) {
            rotate(array, s, m, b);
            return;
        }
        if (Math.min(m - s, b - m) <= bLen) merge(array, s, m, b, p);
        else blockMergeHelper(array, s - (s - a) % bLen, m, b, t, p, bLen);
    }

    private void blockMergeNoBuf(int[] array, int a, int m, int b, int t, int bLen) {
        if (Reads.compareValues(array[m - 1], array[m]) <= 0) return;
        int s = minExpSearch(array, a, m, array[m], false);
        b = maxExpSearch(array, m, b, array[m - 1], true);
        if (Reads.compareValues(array[s], array[b - 1]) > 0) {
            rotate(array, s, m, b);
            return;
        }
        if (Math.min(m - s, b - m) <= bLen) inPlaceMerge(array, s, m, b);
        else blockMergeNoBufHelper(array, s - (s - a) % bLen, m, b, t, bLen);
    }

    protected void redistBufferFW(int[] array, int a, int m, int b) {
        int rPos = minExpSearch(array, m, b, array[a], true);
        rotate(array, a, m, rPos);

        int dist = rPos - m;
        a += dist;
        m += dist;

        int a1 = a + (m - a) / 2;
        rPos = binSearch(array, m, b, array[a1], true);
        rotate(array, a1, m, rPos);

        dist = rPos - m;
        a1 += dist;
        m += dist;

        inPlaceMerge(array, a, a1 - dist, a1);
        inPlaceMerge(array, a1, m, b);
    }

    protected void redistBufferBW(int[] array, int a, int m, int b) {
        int rPos = maxExpSearch(array, a, m, array[b - 1], false);
        rotate(array, rPos, m, b);

        int dist = m - rPos;
        b -= dist;
        m -= dist;

        int b1 = m + (b - m) / 2;
        rPos = binSearch(array, a, m, array[b1 - 1], false);
        rotate(array, rPos, m, b1);

        dist = m - rPos;
        b1 -= dist;
        m -= dist;

        inPlaceMerge(array, b1, b1 + dist, b);
        inPlaceMerge(array, a, m, b1);
    }

    public void lazyStableSort(int[] array, int a, int b) {
        int j = 16;
        if (buildRuns(array, a, b, j)) return;
        for (int i; j < b - a; j *= 2) {
            for (i = a; i + j < b; i += 2 * j)
                smartInPlaceMerge(array, i, i + j, Math.min(i + 2 * j, b));
        }
    }

    /**
     * Sorts the range {@code [a, b)} of {@code array} using Adaptive Grailsort.
     *
     * @param array the array
     * @param a     the start of the range, inclusive
     * @param b     the end of the range, exclusive
     */
    public void blockMergeSort(int[] array, int a, int b) {
        int length = b - a;
        if (length <= 32) {
            insertSort(array, a, b);
            return;
        }
        if (length < 64) {
            lazyStableSort(array, a, b);
            return;
        }
        int bLen = 1 << ((33-Integer.numberOfLeadingZeros(length - 1))/2), // ceilPow2(sqrt(len))
            tLen = this.tLenCalc(length, bLen);

        int ideal = bLen + tLen;
        //choose direction to find keys
        boolean bwBuf;
        int rRun = this.buildUniqueRunBW(array, b, ideal), lRun = 0;
        if (rRun == ideal) bwBuf = true;
        else {
            lRun = this.buildUniqueRunFW(array, a, ideal);

            if (lRun == ideal) bwBuf = false;
            else bwBuf = (rRun < 16 && lRun < 16) || rRun >= lRun;
        }
        //find bLen + tLen unique buffer keys
        int keys;
        if (bwBuf) keys = findKeysBW(array, a, b, rRun, ideal);
        else keys = findKeysFW(array, a, b, lRun, ideal);
        if (keys == 1) return;
        if (keys <= 4) { // strategy 3: lazy stable
            lazyStableSort(array, a, b);
            return;
        }
        if (keys < ideal) {
            keys = 1 << (31-Integer.numberOfLeadingZeros(keys)); // 2^floor(log2(keys))
            bLen = keys / 2;
            tLen = keys / 2;
        }
        int i, j = 16, t, p, a1, b1;
        length -= keys;
        if(bwBuf) {
            p = b-bLen; a1 = a; b1 = p-tLen; t = b1;
        } else {
            p = a+tLen; a1 = p+bLen; b1 = b; t = a;
        }
        if (!buildRuns(array, a1, b1, j)) {
            for(; 4*j <= bLen; j *= 4) {
                for(i = a1; i+2*j < b1; i += 4*j)
                    this.pingPongMerge(array, i, i+j, i+2*j, Math.min(i+3*j, b1), Math.min(i+4*j, b1), p);
                if(i+j < b1)
                    this.smartMerge(array, i, i+j, b1, p);
            }
            for(; j <= bLen; j *= 2)
                for(i = a1; i+j < b1; i += 2*j)
                    this.smartMerge(array, i, i+j, Math.min(i+2*j, b1), p);
            // block merge
            int limit = bLen*(tLen+2);

            for (; j < length && Math.min(2*j, length) < limit; j *= 2) {
                for (i = a1; i+j < b1; i += 2*j)
                    this.blockMerge(array, i, i+j, Math.min(i+2*j, b1), t, p, bLen);
            }
            insertSort(array, p, p + bLen);
            // strategy 2
            bLen = 2*j/keys;

            for (; j < length; j *= 2, bLen *= 2) {
                for(i = a1; i+j < b1; i += 2*j)
                    this.blockMergeNoBuf(array, i, i+j, Math.min(i+2*j, b1), t, bLen);
            }
        }
        if (bwBuf) {
            a = minExpSearch(array, a, b1, array[b1], false);
            if (keys >= ideal/2) redistBufferBW(array, a, b1, b);
            else inPlaceMerge(array, a, b1, b);
        } else {
            b = maxExpSearch(array, a1, b, array[a1 - 1], true);
            if (keys >= ideal/2) redistBufferFW(array, a, a1, b);
            else inPlaceMerge(array, a, a1, b);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        blockMergeSort(array, 0, sortLength);
    }
}
