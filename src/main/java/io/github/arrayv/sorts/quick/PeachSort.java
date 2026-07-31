package io.github.arrayv.sorts.quick;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.IndexedRotations;

/*
 *
MIT License

Copyright (c) 2025-2026 Flanlaina, Sorting Algorithm Scarlet

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
 * A Fluxsort variant with Logsort and Half Logota Sort.
 * <p>
 * To use this algorithm in another, use {@code quickSort()} from a reference
 * instance.
 *
 * @author Flanlaina
 * @author aphitorite
 * @author Scandum
 *
 */
public class PeachSort extends Sort {
    public PeachSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Peach");
        this.setRunAllSortsName("Peach Sort");
        this.setRunSortName("Peachsort");
        this.setCategory("Quick Sorts");
        this.setAuthors("aphitorite, Flanlaina, Scandum");
        this.setConstant("n log n");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Set block size (default: calculates minimum block length for current length)", 1);
    }

    static final int INSERT_THRESHOLD = 32;

    public static int productLog(int n) {
        int r = 1;
        while((r<<r) < n) r++;
        return r;
    }

    public static int log2(int n) {
        return 31 - Integer.numberOfLeadingZeros(n);
    }

    private static int equ(int a, int b) {
        return ((a - b) >> 31) + ((b - a) >> 31) + 1;
    }

    protected int medOf3(int[] array, int i0, int i1, int i2) {
        int tmp;
        if(Reads.compareIndices(array, i0, i1, 1, true) > 0) {
            tmp = i1;
            i1 = i0;
        } else tmp = i0;
        if(Reads.compareIndices(array, i1, i2, 1, true) > 0) {
            if(Reads.compareIndices(array, tmp, i2, 1, true) > 0) return tmp;
            return i2;
        }
        return i1;
    }

    public int ninther(int[] array, int a, int b) {
        if (b - a <= 9) return a + (b - a) / 2;
        int len = b - a, half = len / 2, quart = len / 4, eight = len / 8;
        int c = medOf3(array, a, a + eight, a + quart);
        int d = medOf3(array, a + quart + eight, a + half, a + half + eight);
        int e = medOf3(array, b - quart, b - eight, b - 1);
        return medOf3(array, c, d, e);
    }

    // Median of 3 ninthers
    public int pseudomo27(int[] array, int a, int b) {
        if (b - a < 64) return this.ninther(array, a, b);
        int d = (b - a) / 3;
        int m0 = this.ninther(array, a, a + d);
        int m1 = this.ninther(array, a + d, a + 2 * d);
        int m2 = this.ninther(array, a + 2 * d, b);
        return this.medOf3(array, m0, m1, m2);
    }

    // Ninther of 9 ninthers
    public int pseudomo81(int[] array, int a, int b) {
        if (b - a < 256) return this.pseudomo27(array, a, b);
        int d = (b - a) / 9;
        int m0 = this.ninther(array, a, a + d);
        int m1 = this.ninther(array, a + d, a + 2 * d);
        int m2 = this.ninther(array, a + 2 * d, a + 3 * d);
        int m3 = this.ninther(array, a + 3 * d, a + 4 * d);
        int m4 = this.ninther(array, a + 4 * d, a + 5 * d);
        int m5 = this.ninther(array, a + 5 * d, a + 6 * d);
        int m6 = this.ninther(array, a + 6 * d, a + 7 * d);
        int m7 = this.ninther(array, a + 7 * d, a + 8 * d);
        int m8 = this.ninther(array, a + 8 * d, b);
        return this.medOf3(array, this.medOf3(array, m0, m1, m2), this.medOf3(array, m3, m4, m5),
                this.medOf3(array, m6, m7, m8));
    }

    // Ninther of 9 medians of 3 ninthers
    public int pseudomo243(int[] array, int a, int b) {
        if (b - a < 16384) return this.pseudomo81(array, a, b);
        int d = (b - a) / 9;
        int m0 = this.pseudomo27(array, a, a + d);
        int m1 = this.pseudomo27(array, a + d, a + 2 * d);
        int m2 = this.pseudomo27(array, a + 2 * d, a + 3 * d);
        int m3 = this.pseudomo27(array, a + 3 * d, a + 4 * d);
        int m4 = this.pseudomo27(array, a + 4 * d, a + 5 * d);
        int m5 = this.pseudomo27(array, a + 5 * d, a + 6 * d);
        int m6 = this.pseudomo27(array, a + 6 * d, a + 7 * d);
        int m7 = this.pseudomo27(array, a + 7 * d, a + 8 * d);
        int m8 = this.pseudomo27(array, a + 8 * d, b);
        return this.medOf3(array, this.medOf3(array, m0, m1, m2), this.medOf3(array, m3, m4, m5),
                this.medOf3(array, m6, m7, m8));
    }

    // get rank of r between [a,a+g...b)
    private int gaprank(int[] array, int a, int b, int g, int r) {
        int re = 0;
        while (a < b) {
            if (a != r) {
                if (Reads.compareIndices(array, a, r, 0.25, true) < 0) re++;
            }
            a += g;
        }
        return re;
    }

    // hopefully better "rank of 243s" median selector
    private int rankof243s(int[] array, int a, int b) {
        // 2^(log(b-a)/2)
        int s = 1;
        while (s * s < b - a) s *= 2;

        // low n: return ninther
        if ((s /= 2) < 2) return ninther(array, a, b);
        int mid = (b - a - 1) / (2 * s) + 1, e = (b - a) / 8, cm = a + (b - a) / 2, cr = 0;

        // select pmo243 with gapped rank closest to middle
        for (int i = 0; i < e; i += s) {
            int p = pseudomo243(array, a + i, b - e + i), r = gaprank(array, a, b, s, p);
            if (i == 0 || Math.abs(cr - mid) > Math.abs(r - mid)) {
                cm = p;
                cr = r;
            }
        }
        return cm;
    }

    public void segmentReversal(int[] array, int start, int end, double cSleep, double wSleep, boolean mark, boolean aux) {
        for (int i = start; i < end; i++) {
            int left = i;
            while (i < end && Reads.compareIndices(array, i, i + 1, cSleep, true) == 0) i++;
            int right = i;
            if (left != right) Writes.reversal(array, left, right, wSleep, mark, aux);
        }
    }

    protected void blockSwap(int[] array, int a, int b, int len) {
        if (a == b) return;
        for (int i = 0; i < len; i++) Writes.swap(array, a + i, b + i, 1, true, false);
    }

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
        IndexedRotations.cycleReverse(array, a, m, b, 1, true, false);
    }

    boolean pivCmp(int v, int piv, int eqLower) {
        int c = Reads.compareValues(v, piv);
        return c < 0 || ((eqLower == 1) && c == 0);
    }

    private void pivBufXor(int[] array, int pa, int pb, int v, int wLen) {
        int i = 0;
        while(wLen-- > 0) {
            if((v&1) == 1) Writes.swap(array, pa+i, pb+i, 1, true, false);
            v >>= 1; i++;
        }
    }
    //@param bit - < pivot means this bit
    private int pivBufGet(int[] array, int pa, int piv, int pCmp, int wLen, int bit) {
        int r = 0;
        while (wLen-- > 0) {
            r <<= 1;
            r |= (this.pivCmp(array[pa + wLen], piv, pCmp) ? 0 : 1) ^ bit;
        }
        return r;
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
                Writes.reversal(array, j, i - 1, 1.0, true, false);
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

    public void insertSort(int[] array, int a, int b) {
        buildRuns(array, a, b, b - a);
    }

    private void mergeFWExt(int[] array, int[] tmp, int a, int m, int b) {
        int s = m-a;
        Writes.arraycopy(array, a, tmp, 0, s, 1, true, true);
        int i = 0, j = m;
        while(i < s && j < b) {
            Highlights.markArray(2, j);

            if(Reads.compareValues(tmp[i], array[j]) <= 0)
                Writes.write(array, a++, tmp[i++], 1, true, false);
            else
                Writes.write(array, a++, array[j++], 1, true, false);
        }
        Highlights.clearAllMarks();
        while(i < s) Writes.write(array, a++, tmp[i++], 1, true, false);
    }

    private void mergeBWExt(int[] array, int[] tmp, int a, int m, int b) {
        int s = b-m;
        Writes.arraycopy(array, m, tmp, 0, s, 1, true, true);
        int i = s-1, j = m-1;
        while(i >= 0 && j >= a) {
            Highlights.markArray(2, j);

            if(Reads.compareValues(tmp[i], array[j]) >= 0)
                Writes.write(array, --b, tmp[i--], 1, true, false);
            else
                Writes.write(array, --b, array[j--], 1, true, false);
        }
        Highlights.clearAllMarks();
        while(i >= 0) Writes.write(array, --b, tmp[i--], 1, true, false);
    }

    protected void blockCycle(int[] array, int a, int n, int tagStart, int bLen, int wLen, int piv, int eqLower,
            int bit) {
        for (int i = 0, aPtr = a, tPtr = tagStart; i < n; i++, aPtr += bLen, tPtr += bLen) {
            int dest = this.pivBufGet(array, aPtr, piv, eqLower, wLen, bit);
            while (dest != i) {
                this.blockSwap(array, aPtr, a + dest * bLen, bLen);
                dest = this.pivBufGet(array, aPtr, piv, eqLower, wLen, bit);
            }
            this.pivBufXor(array, aPtr, tPtr, i, wLen);
        }
    }

    // Adaptive Half Logota Sort merging adapted from Half Ectasort
    // precondition: (m-a) is divisible by bLen && (b-m) is divisible by bLen
    private void blockMergeHelper(int[] array, int[] swap, int a, int m, int b, int p, int bLen, int piv, int pCmp, int bit) {
        if(m-a <= bLen) {
            this.mergeFWExt(array, swap, a, m, b);
            return;
        }
        if(b-m <= bLen) {
            this.mergeBWExt(array, swap, a, m, b);
            return;
        }

        int wLen = log2((b-a)/bLen-1)+1, t = 1;
        int i = a, j = m, l = a, r = m;
        int pc = p + bLen;

        for(int c = 0; c < bLen; c++) {
            if(Reads.compareIndices(array, i, j, 0.5, true) <= 0) {
                Writes.write(swap, c, array[i++], 1, true, true);
            } else {
                Writes.write(swap, c, array[j++], 1, true, true);
            }
        }

        while (l < m && r < b) {
            boolean left = i-l > 0 && (i-l == bLen || Reads.compareIndices(array, l+bLen-1, r+bLen-1, 1, true) <= 0);
            int k = left ? l : r;
            for(int c = 0; c < bLen; c++) {
                int tmp;
                if(i < m && (j == b || Reads.compareIndices(array, i, j, 0.5, true) <= 0)) tmp = array[i++];
                else tmp = array[j++];
                Highlights.markArray(3, k);
                Writes.write(array, k++, tmp, 1, false, false);
            }
            if(left) l = k;
            else     r = k;

            pivBufXor(array, k - bLen, pc, t++, wLen);
            pc += bLen;
        }
        Highlights.clearMark(2);
        Highlights.clearMark(3);

        int pr = l < m ? l : r;
        Writes.arraycopy(swap, 0, array, pr, bLen, 0.5, true, false);
        pivBufXor(array, pr, p, 0, wLen);

        if (l < m) {
            l += bLen;
            while (l < m) {
                pivBufXor(array, l, pc, t++, wLen);
                pc += bLen; l += bLen;
            }
        }
        if (r < b) {
            r += bLen;
            while (r < b) {
                pivBufXor(array, r, pc, t++, wLen);
                pc += bLen; r += bLen;
            }
        }

        this.blockCycle(array, a, (b - a) / bLen, p, bLen, wLen, piv, pCmp, bit);
    }
    private void blockMergeEasy(int[] array, int[] swap, int a, int m, int b, int p, int bLen, int piv, int pCmp, int bit) {
        if (Reads.compareIndices(array, m - 1, m, 0.5, true) <= 0) return;
        b = maxExpSearch(array, m, b, array[m - 1], true);
        if(b-m <= bLen) {
            this.mergeBWExt(array, swap, a, m, b);
            return;
        }
        a = minExpSearch(array, a, m, array[m], false);
        if(m-a <= bLen) {
            this.mergeFWExt(array, swap, a, m, b);
            return;
        }

        int a1 = a+(m-a)%bLen, b1 = b-(b-m)%bLen;
        this.blockMergeHelper(array, swap, a1, m, b1, p, bLen, piv, pCmp, bit);
        this.mergeBWExt(array, swap, a1, b1, b);
        this.mergeFWExt(array, swap, a, a1, b);
    }

    public void blockMerge(int[] array, int[] swap, int a, int m, int b, int bLen) {
        if (Reads.compareIndices(array, m - 1, m, 0.5, true) <= 0) return;
        b = maxExpSearch(array, m, b, array[m - 1], true);
        a = minExpSearch(array, a, m, array[m], false);
        int l = m - a, r = b - m;
        int lCnt = (l + r + 1) / 2, med;

        // find lower ceil((A+B)/2) elements and then find max of halves to get median
        // binary search is used for O(log n) performance
        if (r < l) {
            if (r <= bLen) {
                this.mergeBWExt(array, swap, a, m, b);
                return;
            }
            int la = 0, lb = r;
            while (la < lb) {
                int lm = (la + lb) >>> 1;
                if (Reads.compareIndices(array, m + lm, a + (lCnt - lm) - 1, 0.25, true) <= 0) la = lm + 1;
                else lb = lm;
            }
            if (la == 0) med = array[a + lCnt - 1];
            else med = Reads.compareIndices(array, m + la - 1, a + (lCnt - la) - 1, 0.25, true) > 0
                     ? array[m + la - 1] : array[a + (lCnt - la) - 1];
        } else {
            if (l <= bLen) {
                this.mergeFWExt(array, swap, a, m, b);
                return;
            }
            int la = 0, lb = l;
            while (la < lb) {
                int lm = (la + lb) >>> 1;
                if (Reads.compareIndices(array, a + lm, m + (lCnt - lm) - 1, 0.25, true) < 0) la = lm + 1;
                else lb = lm;
            }
            if (l == r && la == l) med = array[m - 1];
            else if (la == 0)      med = array[m + lCnt - 1];
            else  med = Reads.compareIndices(array, a + la - 1, m + (lCnt - la) - 1, 0.25, true) >= 0
                    ? array[a + la - 1] : array[m + (lCnt - la) - 1];
        }

        // stable ternary partition around median: [ < ][ = ][ > ]
        int m1 = this.binSearch(array, a, m, med, true);
        int m2 = this.binSearch(array, m, b, med, false);
        int ms2 = m - this.binSearch(array, m1, m, med, false);
        int ms1 = this.binSearch(array, m, m2, med, true) - m;
        this.rotate(array, m - ms2, m, m2); // ABCABC -> ABABCC
        this.rotate(array, m1, m - ms2, m + ms1 - ms2); // ABABCC -> AABBCC

        if (m1 > a && ms1 > 0) this.blockMergeEasy(array, swap, a, m1, m1 + ms1, a + lCnt, bLen, med, 0, 0);
        if (m2 < b && ms2 > 0) this.blockMergeEasy(array, swap, m2 - ms2, m2, b, a, bLen, med, 1, 1);
    }

    public void blockMergeSort(int[] array, int[] swap, int left, int right, int bLen) {
        int j = 16, length = right - left;
        if (buildRuns(array, left, right, j)) return;
        for(; j < length; j *= 2)
            for(int i = left; i+j < right; i += 2*j)
                this.blockMerge(array, swap, i, i+j, Math.min(right, i+2*j), bLen);
    }

    protected int partition(int[] array, int[] buf, int a, int b, int bLen, int piv, int eqLower) {
        // determines which elements do not need to be moved
        for(; a < b; a++) {
            Highlights.markArray(1, a);
            Delays.sleep(0.25);
            if(!this.pivCmp(array[a], piv, eqLower)) break;
        }
        for(; b > a; b--) {
            Highlights.markArray(1, b-1);
            Delays.sleep(0.25);
            if(this.pivCmp(array[b-1], piv, eqLower)) break;
        }
        if (b - a <= bLen) {
            int j = a, k = 0;
            for (int i = a; i < b; i++) {
                int cmp = Reads.compareIndexValue(array, i, piv, 0.25, true);
                if (cmp < 0 || ((eqLower == 1) && cmp == 0)) {
                    if (j != i) Writes.write(array, j, array[i], 0.5, true, false);
                    j++;
                } else Writes.write(buf, k++, array[i], 0.5, false, true);
            }
            Writes.arraycopy(buf, 0, array, j, k, 0.5, true, false);
            return j;
        }

        // sort blocks and type blocks
        int p = a;
        int l = 0, r = 0;
        int lb = 0, rb = 0;
        for (int i = a; i < b; i++) {
            int cmp = Reads.compareIndexValue(array, i, piv, 0.25, true);
            if (cmp < 0 || ((eqLower == 1) && cmp == 0)) {
                Writes.write(array, p + l++, array[i], 0.25, true, false);
                if(l == bLen) {
                    l = 0;
                    lb++;
                    p += bLen;
                }
            } else {
                Writes.write(buf, r++, array[i], 0.25, false, true);
                if(r == bLen) {
                    Writes.arraycopy(array, p, array, p+bLen, l, 0.5, true, false);
                    Writes.arraycopy(buf, 0, array, p, bLen, 0.5, true, false);
                    r = 0;
                    rb++;
                    p += bLen;
                }
            }
        }

        // sort blocks
        int min = Math.min(lb, rb);
        int m = a + lb * bLen;
        if (min > 0) {
            int wLen = log2(min - 1) + 1; // ceil(log2(min))
            for (int i = 0, j = a, k = a; i < min; i++) { // set bit buffers
                while (!this.pivCmp(array[j + wLen], piv, eqLower)) j += bLen;
                while (this.pivCmp(array[k + wLen], piv, eqLower)) k += bLen;
                this.pivBufXor(array, j, k, i, wLen);
                j += bLen; k += bLen;
            }
            if (lb < rb) {
                for (int i = p - bLen, j = p; i >= a; i -= bLen) { // swap right to left
                    if (!pivCmp(array[i + wLen], piv, eqLower)) {
                        j -= bLen;
                        blockSwap(array, i, j, bLen);
                    }
                }
                this.blockCycle(array, a, lb, m, bLen, wLen, piv, eqLower, 0);
            } else {
                for (int i = a, j = a; i < p; i += bLen) { // swap left to right
                    if (pivCmp(array[i + wLen], piv, eqLower)) {
                        blockSwap(array, i, j, bLen);
                        j += bLen;
                    }
                }
                this.blockCycle(array, m, rb, a, bLen, wLen, piv, eqLower, 1);
            }
        }

        // handle leftover
        Writes.arraycopy(buf, 0, array, b - r, r, 1, true, false);
        if (l > 0) {
            Highlights.clearMark(2);
            Writes.arraycopy(array, b - r - l, buf, 0, l, 1, false, true);
            Writes.arraycopy(array, m, array, m + l, rb * bLen, 1, true, false);
            Writes.arraycopy(buf, 0, array, m, l, 1, true, false);
        }
        return m + l;
    }

    protected void sortHelper(int[] array, int[] buf, int a, int b, int bLen, int depth, boolean bad) {
        while (b - a > INSERT_THRESHOLD) {
            if (depth == 0) {
                blockMergeSort(array, buf, a, b, bLen);
                return;
            }
            depth--;
            int pIdx;
            if(bad) {
                pIdx = rankof243s(array, a, b);
                bad = false;
            } else pIdx = ninther(array, a, b);
            Highlights.clearMark(2);
            int m = partition(array, buf, a, b, bLen, array[pIdx], 1);
            if (m == b) {
                // pivot is highest rank, partition again with inverted bias
                m = partition(array, buf, a, b, bLen, array[pIdx], 0);
                bad = (m - a) / 8 > b - m;
                b = m;
                continue;
            }
            int lLen = m - a, rLen = b - m;
            bad = rLen / 8 > lLen || lLen / 8 > rLen;
            if (lLen > rLen) {
                sortHelper(array, buf, m, b, bLen, depth, bad);
                b = m;
            } else {
                sortHelper(array, buf, a, m, bLen, depth, bad);
                a = m;
            }
        }
        insertSort(array, a, b);
    }

    /**
     * Sorts the range {@code [a, b)} of {@code array} using Peach Sort.
     *
     * @param array the array
     * @param a     the start of the range, inclusive
     * @param b     the end of the range, exclusive
     * @param bLen  the block size, automatically clamped to {@code [u, n]} where u
     *              is the smallest integer such that u&Cross;2<sup>u</sup> &ge; n.
     *              A value of 1 can be used to have the block size calculated
     *              automatically.
     */
    public void quickSort(int[] array, int a, int b, int bLen) {
        int len = b - a;
        if (len <= INSERT_THRESHOLD) {
            insertSort(array, a, b);
            return;
        }
        int balance = 0, eq = 0, streaks = 0, dist, eqdist, loop, cnt = len, pos = a;
        while (cnt > 16) {
            for (eqdist = dist = 0, loop = 0; loop < 16; loop++) {
                int cmp = Reads.compareIndices(array, pos, pos + 1, 0.5, true);
                dist += cmp > 0 ? 1 : 0;
                eqdist += cmp == 0 ? 1 : 0;
                pos++;
            }
            streaks += equ(dist, 0) | equ(dist + eqdist, 16);
            balance += dist;
            eq += eqdist;
            cnt -= 16;
        }
        while (--cnt > 0) {
            int cmp = Reads.compareIndices(array, pos, pos + 1, 0.5, true);
            balance += cmp > 0 ? 1 : 0;
            eq += cmp == 0 ? 1 : 0;
            pos++;
        }
        if (balance == 0) return;
        if (balance + eq == len - 1) {
            Writes.reversal(array, a, b - 1, 0.75, true, false);
            if (eq > 0) segmentReversal(array, a, b - 1, 0.5, 0.75, true, false);
            return;
        }
        bLen = Math.max(productLog(len), Math.min(bLen, len));
        int[] buf = Writes.createExternalArray(bLen);
        int sixth = len / 6;
        if (streaks > len / 20 || balance <= sixth || balance + eq >= len - sixth) {
            blockMergeSort(array, buf, a, b, bLen);
        } else {
            sortHelper(array, buf, a, b, bLen, 2 * log2(len), false);
        }
        Writes.deleteExternalArray(buf);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength, bucketCount);
    }
}
