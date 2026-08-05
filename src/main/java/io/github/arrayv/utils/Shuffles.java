package io.github.arrayv.utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.select.MaxHeapSort;
import io.github.arrayv.sorts.select.PoplarHeapSort;
import io.github.arrayv.sorts.select.SmoothSort;
import io.github.arrayv.sorts.select.TriangularHeapSort;
import io.github.arrayv.sorts.templates.PDQSorting;

/*
 *
MIT License

Copyright (c) 2020 ArrayV 4.0 Team

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

public enum Shuffles {
    RANDOM {
        // If you want to learn why the random shuffle was changed,
        // I highly encourage you read this. It's quite fascinating:
        // http://datagenetics.com/blog/november42014/index.html

        public String getName() {
            return "Randomly";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            makeRandom(arrayVisualizer);
            shuffle(array, 0, currentLen, delay ? 1 : 0, Writes);
        }
    },
    REVERSE {
        public String getName() {
            return "Backwards";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            Writes.reversal(array, 0, currentLen-1, delay ? 1 : 0, true, false);
        }
    },
    ALMOST {
        public String getName() {
            return "Slight Shuffle";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            makeRandom(arrayVisualizer);

            for (int i = 0; i < Math.max(currentLen / 20, 1); i++){
                Writes.swap(array, random.nextInt(currentLen), random.nextInt(currentLen), 0, true, false);

                if (arrayVisualizer.shuffleEnabled()) Delays.sleep(10);
            }
        }
    },
    ALREADY {
        public String getName() {
            return "No Shuffle";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            for (int i = 0; i < currentLen; i++) {
                Highlights.markArray(1, i);
                if (arrayVisualizer.shuffleEnabled()) Delays.sleep(1);
            }
        }
    },
    SORTED {
        public String getName() {
            return "Sorted";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            this.sort(array, array, -1, 0, currentLen, delay ? 1 : 0, Writes);
        }
    },
    NAIVE {
        public String getName() {
            return "Naive Randomly";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            makeRandom(arrayVisualizer);

            for (int i = 0; i < currentLen; i++)
                Writes.swap(array, i, random.nextInt(currentLen), delay ? 1 : 0, true, false);
        }
    },
    NAIVER {
        public int randInt(int a, int b, Random rng) {
            return rng.nextInt(b - a) + a;
        }
        public String getName() {
            return "Naiver Randomly";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            double delay = arrayVisualizer.shuffleEnabled() ? 1 : 0;
            makeRandom(arrayVisualizer);

            for (int i = 0; i < currentLen; i++){
                int i1 = randInt(0, currentLen, random);
                int i2 = randInt(0, currentLen - 1, random);
                if (i2 >= i1) i2++;
                Writes.swap(array, i1, i2, delay, true, false);
            }
        }
    },
    SHUFFLED_TAIL {
        public String getName() {
            return "Scrambled Tail";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            makeRandom(arrayVisualizer);
            int[] aux = new int[currentLen];
            int i = 0, j = 0, k = 0;
            while (i < currentLen) {
                Highlights.markArray(2, i);
                if (random.nextDouble() < 1/7d)
                    Writes.write(aux, k++, array[i++], delay ? 1 : 0, false, true);
                else
                    Writes.write(array, j++, array[i++], delay ? 1 : 0, true, false);
            }
            Writes.arraycopy(aux, 0, array, j, k, delay ? 1 : 0, true, false);
            shuffle(array, j, currentLen, delay ? 2 : 0, Writes);
        }
    },
    SHUFFLED_HEAD {
        public String getName() {
            return "Scrambled Head";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            makeRandom(arrayVisualizer);

            int[] aux = new int[currentLen];
            int i = currentLen - 1, j = currentLen - 1, k = 0;
            while (i >= 0) {
                Highlights.markArray(2, i);
                if (random.nextDouble() < 1/7d)
                    Writes.write(aux, k++, array[i--], delay ? 1 : 0, false, true);
                else
                    Writes.write(array, j--, array[i--], delay ? 1 : 0, true, false);
            }
            Writes.arraycopy(aux, 0, array, 0, k, delay ? 1 : 0, true, false);
            shuffle(array, 0, j, delay ? 2 : 0, Writes);
        }
    },
    SHUFFLED_TAIL_INDEXSORT { // Scrambled Tail, but always shuffles 1/7th of the array, and moves data in-place
        public String getName() {
            return "Scrambled Tail (Indexsort)";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            double delay = arrayVisualizer.shuffleEnabled() ? 1 : 0;

            makeRandom(arrayVisualizer);
            int[] aux = new int[currentLen];
            int m = currentLen - (currentLen + 6) / 7;

            for (int i = 0; i < currentLen; ++i) aux[i] = 0;
            for (int i = m; i < currentLen; ++i) {
                int j = random.nextInt(i + 1);
                aux[aux[j] == 1 ? i : j] = 1;
            }

            int[] ptrs = {0, m};
            for (int i = 0; i < currentLen; ++i) aux[i] = ptrs[aux[i]]++;

            for (int i = 0; i < currentLen; ++i)
                while (i != aux[i]) {
                    Writes.swap(array, i, aux[i], 0, true, false);
                    Writes.swap(aux, i, aux[i], delay, false, true);
                }

            shuffle(array, m, currentLen, delay * 2, Writes);
        }
    },
    SHUFFLED_HEAD_INDEXSORT { // Scrambled Head, but always shuffles 1/7th of the array, and moves data in-place
        public String getName() {
            return "Scrambled Head (Indexsort)";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            double delay = arrayVisualizer.shuffleEnabled() ? 1 : 0;

            makeRandom(arrayVisualizer);
            int[] aux = new int[currentLen];
            int m = (currentLen + 6) / 7;

            for (int i = 0; i < currentLen; ++i) aux[i] = 0;
            for (int i = m; i < currentLen; ++i) {
                int j = random.nextInt(i + 1);
                aux[aux[j] == 1 ? i : j] = 1;
            }

            int[] ptrs = {0, m};
            for (int i = 0; i < currentLen; ++i) aux[i] = ptrs[aux[i]]++;

            for (int i = 0; i < currentLen; ++i)
                while (i != aux[i]) {
                    Writes.swap(array, i, aux[i], 0, true, false);
                    Writes.swap(aux, i, aux[i], delay, false, true);
                }

            shuffle(array, 0, m, delay * 2, Writes);
        }
    },
    SHUFFLED_ENDS {
        public String getName() {
            return "Scrambled Start + End";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            double sleep = arrayVisualizer.shuffleEnabled() ? 1 : 0;
            int len = (currentLen + 6) / 7;
            makeRandom(arrayVisualizer);
            int[] aux = new int[currentLen];
            int[] ptrs = {0, len, currentLen - len, currentLen};

            for (int i = 0; i < 3; i++)
                for (int j = ptrs[i]; j < ptrs[i + 1]; j++) {
                    int k = random.nextInt(j + 1);
                    aux[j] = aux[k];
                    aux[k] = i;
                }

            for (int i = 0; i < currentLen; ++i)
                aux[i] = ptrs[aux[i]]++;

            for (int i = 0; i < currentLen; ++i)
                while (i != aux[i]) {
                    Writes.swap(array, i, aux[i], 0, true, false);
                    Writes.swap(aux, i, aux[i], sleep, false, true);
                }

            shuffle(array, 0, len, sleep * 2, Writes);
            shuffle(array, currentLen - len, currentLen, sleep * 2, Writes);
        }
    },
    MOVED_ELEMENT {
        public String getName() {
            return "Shifted Element";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            makeRandom(arrayVisualizer);

            int start = random.nextInt(currentLen);
            int dest = random.nextInt(currentLen);
            if (dest < start) {
                IndexedRotations.holyGriesMills(array, dest, start, start + 1, delay ? 1 : 0, true, false);
            } else {
                IndexedRotations.holyGriesMills(array, start, start + 1, dest, delay ? 1 : 0, true, false);
            }
        }
    },
    RANDOM_ROTATION {
        @Override
        public String getName() {
            return "Random Rotation";
        }
        public int randInt(int a, int b, Random rng) {
            return rng.nextInt(b - a) + a;
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            double delay = arrayVisualizer.shuffleEnabled() ? 1 : 0;
            makeRandom(arrayVisualizer);

            int mid = randInt(1, currentLen, random);
            int start = randInt(0, mid, random), end = randInt(mid, currentLen, random) + 1;
            IndexedRotations.cycleReverse(array, start, mid, end, delay, true, false);
        }
    },
    NOISY {
        public String getName() {
            return "Noisy";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            makeRandom(arrayVisualizer);

            int i, size = Math.max(4, (int)(Math.sqrt(currentLen)/2));
            for (i = 0; i+size <= currentLen; i += random.nextInt(size-1)+1)
                shuffle(array, i, i+size, delay ? 0.5 : 0, Writes);
            shuffle(array, i, currentLen, delay ? 0.5 : 0, Writes);
        }
    },
    SHUFFLED_ODDS {
        public String getName() {
            return "Scrambled Odds";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            makeRandom(arrayVisualizer);

            for (int i = 1; i < currentLen; i += 2){
                int randomIndex = (((random.nextInt(currentLen - i) / 2)) * 2) + i;
                Writes.swap(array, i, randomIndex, 0, true, false);

                if (arrayVisualizer.shuffleEnabled()) Delays.sleep(2);
            }
        }
    },
    FINAL_MERGE {
        public String getName() {
            return "Final Merge Pass";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            int count = 2;

            int k = 0;
            int[] temp = new int[currentLen];

            for (int j = 0; j < count; j++)
                for (int i = j; i < currentLen; i+=count)
                    Writes.write(temp, k++, array[i], 0, false, true);

            for (int i = 0; i < currentLen; i++)
                Writes.write(array, i, temp[i], delay ? 1 : 0, true, false);
        }
    },
    REAL_FINAL_MERGE {
        public String getName() {
            return "Shuffled Final Merge";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            makeRandom(arrayVisualizer);

            this.shuffle(array, 0, currentLen, delay ? 0.5 : 0, Writes);
            Highlights.clearMark(2);
            this.sort(array, array, -1, 0, currentLen / 2, delay ? 0.5 : 0, Writes);
            this.sort(array, array, -1, currentLen / 2, currentLen, delay ? 0.5 : 0, Writes);
        }
    },
    SORTED_HALVES {
        public String getName() {
            return "Sorted Halves";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            this.sort(array, array, -1, 0, currentLen / 2, delay ? 0.5 : 0, Writes);
            this.sort(array, array, -1, currentLen / 2, currentLen, delay ? 0.5 : 0, Writes);
        }
    },
    SHUFFLED_HALF {
        public String getName() {
            return "Shuffled Half";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            makeRandom(arrayVisualizer);

            this.shuffle(array, 0, currentLen, delay ? 2/3d : 0, Writes);
            Highlights.clearMark(2);
            this.sort(array, array, -1, 0, currentLen / 2, delay ? 2/3d : 0, Writes);
        }
    },
    SHUFFLED_HALF_BACK { // Scrambled Back Half, but stackable
        public String getName() {
            return "Scrambled Back Half (Stackable)";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            double delay = arrayVisualizer.shuffleEnabled() ? 1 : 0;

            makeRandom(arrayVisualizer);
            int[] aux = new int[currentLen];
            int m = currentLen / 2;

            for (int i = 0; i < currentLen; ++i) aux[i] = 0;
            for (int i = m; i < currentLen; ++i) {
                int j = random.nextInt(i + 1);
                aux[aux[j] == 1 ? i : j] = 1;
            }

            int[] ptrs = {0, m};
            for (int i = 0; i < currentLen; ++i) aux[i] = ptrs[aux[i]]++;

            for (int i = 0; i < currentLen; ++i)
                while (i != aux[i]) {
                    Writes.swap(array, i, aux[i], 0, true, false);
                    Writes.swap(aux, i, aux[i], delay, false, true);
                }

            shuffle(array, m, currentLen, delay, Writes);
        }
    },
    SHUFFLED_HALF_FRONT { // Scrambled Front Half, but stackable
        public String getName() {
            return "Scrambled Front Half (Stackable)";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            double delay = arrayVisualizer.shuffleEnabled() ? 1 : 0;

            makeRandom(arrayVisualizer);
            int[] aux = new int[currentLen];
            int m = currentLen / 2;

            for (int i = 0; i < currentLen; ++i) aux[i] = 0;
            for (int i = m; i < currentLen; ++i) {
                int j = random.nextInt(i + 1);
                aux[aux[j] == 1 ? i : j] = 1;
            }

            int[] ptrs = {0, m};
            for (int i = 0; i < currentLen; ++i) aux[i] = ptrs[aux[i]]++;

            for (int i = 0; i < currentLen; ++i)
                while (i != aux[i]) {
                    Writes.swap(array, i, aux[i], 0, true, false);
                    Writes.swap(aux, i, aux[i], delay, false, true);
                }

            shuffle(array, 0, m, delay, Writes);
        }
    },
    PARTITIONED {
        public String getName() {
            return "Partitioned";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            makeRandom(arrayVisualizer);

            this.sort(array, array, -1, 0, currentLen, delay ? 0.5 : 0, Writes);
            Highlights.clearMark(2);
            this.shuffle(array, 0, currentLen/2, delay ? 0.5 : 0, Writes);
            this.shuffle(array, currentLen/2, currentLen, delay ? 0.5 : 0, Writes);
        }
    },
    SAWTOOTH {
        public String getName() {
            return "Sawtooth";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            int count = 4;

            int k = 0;
            int[] temp = new int[currentLen];

            for (int j = 0; j < count; j++)
                for (int i = j; i < currentLen; i+=count)
                    Writes.write(temp, k++, array[i], 0, false, true);

            for (int i = 0; i < currentLen; i++)
                Writes.write(array, i, temp[i], delay ? 1 : 0, true, false);
        }
    },
    ORGAN {
        public String getName() {
            return "Pipe Organ";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            int[] temp = new int[currentLen];

            for (int i = 0, j = 0; i < currentLen; i+=2){
                temp[j++] = array[i];
            }
            for (int i = 1, j = currentLen; i < currentLen; i+=2) {
                temp[--j] = array[i];
            }
            for (int i = 0; i < currentLen; i++){
                Writes.write(array, i, temp[i], delay ? 1 : 0, true, false);
            }
        }
    },
    FINAL_BITONIC {
        public String getName() {
            return "Final Bitonic Pass";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            int[] temp = new int[currentLen];

            Writes.reversal(array, 0, currentLen-1, delay ? 1 : 0, true, false);
            Highlights.clearMark(2);
            for (int i = 0, j = 0; i < currentLen; i+=2){
                temp[j++] = array[i];
            }
            for (int i = 1, j = currentLen; i < currentLen; i+=2) {
                temp[--j] = array[i];
            }
            for (int i = 0; i < currentLen; i++){
                Writes.write(array, i, temp[i], delay ? 1 : 0, true, false);
            }
        }
    },
    INTERLACED {
        public String getName() {
            return "Interlaced";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            int[] referenceArray = new int[currentLen];
            for (int i = 0; i < currentLen; i++) {
                referenceArray[i] = array[i];
            }

            int leftIndex = 1;
            int rightIndex = currentLen - 1;

            for (int i = 1; i < currentLen; i++) {
                if (i % 2 == 0) {
                    Writes.write(array, i, referenceArray[leftIndex++], delay ? 1 : 0, true, false);
                } else {
                    Writes.write(array, i, referenceArray[rightIndex--], delay ? 1 : 0, true, false);
                }
            }
        }
    },
    DOUBLE_LAYERED {
        public String getName() {
            return "Double Layered";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();

            for (int i = 0; i < currentLen / 2; i += 2) {
                Writes.swap(array, i, currentLen - i - 1, 0, true, false);
                if (arrayVisualizer.shuffleEnabled()) Delays.sleep(1);
            }
        }
    },
    FINAL_RADIX {
        public String getName() {
            return "Final Radix";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            currentLen -= currentLen % 2;
            int mid = currentLen/2;
            int[] temp = new int[mid];

            for (int i = 0; i < mid; i++)
                Writes.write(temp, i, array[i], 0, false, true);

            for (int i = mid, j = 0; i < currentLen; i++, j+=2) {
                Writes.write(array, j, array[i], delay ? 1 : 0, true, false);
                Writes.write(array, j+1, temp[i-mid], delay ? 1 : 0, true, false);
            }
        }
    },
    REAL_FINAL_RADIX {
        public String getName() {
            return "Real Final Radix";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();

            int mask = 0;
            for (int i = 0; i < currentLen; i++)
                while (mask < array[i]) mask = (mask << 1) + 1;
            mask >>= 1;

            int[] counts = new int[mask+2];
            int[] tmp    = new int[currentLen];

            System.arraycopy(array, 0, tmp, 0, currentLen);

            for (int i = 0; i < currentLen; i++)
                counts[(array[i]&mask)+1]++;

            for (int i = 1; i < counts.length; i++)
                counts[i] += counts[i-1];

            for (int i = 0; i < currentLen; i++)
                Writes.write(array, counts[tmp[i]&mask]++, tmp[i], 1, true, false);
        }
    },
    REC_RADIX {
        public String getName() {
            return "Recursive Final Radix";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            weaveRec(array, 0, currentLen, 1, delay ? 0.5 : 0, Writes);
        }

        public void weaveRec(int[] array, int pos, int length, int gap, double delay, Writes Writes) {
            if (length < 2) return;

            int mod2 = length % 2;
            length -= mod2;
            int mid = length/2;
            int[] temp = new int[mid];

            for (int i = pos, j = 0; i < pos+gap*mid; i+=gap, j++)
                Writes.write(temp, j, array[i], 0, false, true);

            for (int i = pos+gap*mid, j = pos, k = 0; i < pos+gap*length; i+=gap, j+=2*gap, k++) {
                Writes.write(array, j, array[i], delay, true, false);
                Writes.write(array, j+gap, temp[k], delay, true, false);
            }

            weaveRec(array, pos, mid+mod2, 2*gap, delay/2, Writes);
            weaveRec(array, pos+gap, mid, 2*gap, delay/2, Writes);
        }
    },
    HALF_ROTATION {
        public String getName() {
            return "Half Rotation";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            int a = 0, m = (currentLen + 1) / 2;

            if (currentLen % 2 == 0)
                while (m < currentLen) Writes.swap(array, a++, m++, delay ? 1 : 0, true, false);
            else {
                Highlights.clearMark(2);
                int temp = array[a];
                while (m < currentLen) {
                    Writes.write(array, a++, array[m], delay ? 0.5 : 0, true, false);
                    Writes.write(array, m++, array[a], delay ? 0.5 : 0, true, false);
                }
                Writes.write(array, a, temp, delay ? 0.5 : 0, true, false);
            }
        }
    },
    PARTIAL_REVERSE {
        public String getName() {
            return "Half Reversed";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            Writes.reversal(array, 0, currentLen-1, delay ? 1 : 0, true, false);
            Writes.reversal(array, currentLen/4, (3*currentLen+3)/4-1, delay ? 1 : 0, true, false);
        }
    },
    PARTIAL_REVERSE_ALT {
        public String getName() {
            return "Half Reversed (Alternative)";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            Writes.reversal(array, currentLen/4, (3*currentLen+3)/4-1, delay ? 1 : 0, true, false);
        }
    },
    BST_TRAVERSAL {
        public String getName() {
            return "BST Traversal";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            int[] temp = Arrays.copyOf(array, currentLen);

            // credit to sam walko/anon

            class Subarray {
                private int start;
                private int end;
                Subarray(int start, int end) {
                    this.start = start;
                    this.end = end;
                }
            }

            Queue<Subarray> q = new LinkedList<Subarray>();
            q.add(new Subarray(0, currentLen));
            int i = 0;

            while (!q.isEmpty()) {
                Subarray sub = q.poll();
                if (sub.start != sub.end) {
                    int mid = (sub.start + sub.end)/2;
                    Writes.write(array, i, temp[mid], 0, true, false);
                    if (arrayVisualizer.shuffleEnabled()) Delays.sleep(1);
                    i++;
                    q.add(new Subarray(sub.start, mid));
                    q.add(new Subarray(mid+1, sub.end));
                }
            }
        }
    },
    INV_BST {
        public String getName() {
            return "Inverted BST";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            int[] temp = new int[currentLen];

            // credit to sam walko/anon

            class Subarray {
                private int start;
                private int end;
                Subarray(int start, int end) {
                    this.start = start;
                    this.end = end;
                }
            }

            Queue<Subarray> q = new LinkedList<Subarray>();
            q.add(new Subarray(0, currentLen));
            int i = 0;

            while (!q.isEmpty()) {
                Subarray sub = q.poll();
                if (sub.start != sub.end) {
                    int mid = (sub.start + sub.end)/2;
                    Highlights.markArray(1, mid);
                    Writes.write(temp, i, mid, 0, false, true);
                    if (delay) Delays.sleep(0.5);
                    i++;
                    q.add(new Subarray(sub.start, mid));
                    q.add(new Subarray(mid+1, sub.end));
                }
            }
            int[] temp2 = Arrays.copyOf(array, currentLen);
            for (i = 0; i < currentLen; i++)
                Writes.write(array, temp[i], temp2[i], delay ? 0.5 : 0, true, false);
        }
    },
    LOG_SLOPES {
        public String getName() {
            return "Logarithmic Slopes";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            int[] temp = new int[currentLen];
            for (int i = 0; i < currentLen; i++)
                Writes.write(temp, i, array[i], 0, false, true);

            Writes.write(array, 0, 0, delay ? 1 : 0, true, false);
            for (int i = 1; i < currentLen; i++) {
                int log = (int) (Math.log(i) / Math.log(2));
                int power = (int) Math.pow(2, log);
                int value = temp[2 * (i - power) + 1];
                Writes.write(array, i, value, delay ? 1 : 0, true, false);
            }
        }
    },
    HEAPIFIED {
        public String getName() {
            return "Heapified";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            MaxHeapSort heapSort = new MaxHeapSort(arrayVisualizer);
            heapSort.makeHeap(array, 0, currentLen, delay ? 1 : 0);
        }
    },
    SMOOTH {
        public String getName() {
            return "Smoothified";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            if (!arrayVisualizer.shuffleEnabled()) Delays.changeSkipped(true);
            SmoothSort smoothSort = new SmoothSort(arrayVisualizer);
            smoothSort.smoothHeapify(array, currentLen);
            if (!arrayVisualizer.shuffleEnabled()) Delays.changeSkipped(false);
        }
    },
    POPLAR {
        public String getName() {
            return "Poplarified";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            if (!arrayVisualizer.shuffleEnabled()) Delays.changeSkipped(true);
            PoplarHeapSort poplarHeapSort = new PoplarHeapSort(arrayVisualizer);
            poplarHeapSort.poplarHeapify(array, 0, currentLen);
            if (!arrayVisualizer.shuffleEnabled()) Delays.changeSkipped(false);
        }
    },
    TRI_HEAP {
        public String getName() {
            return "Triangular Heapified";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            if (delay) Delays.setSleepRatio(Delays.getSleepRatio()*10);
            else       Delays.changeSkipped(true);

            TriangularHeapSort triangularHeapSort = new TriangularHeapSort(arrayVisualizer);
            triangularHeapSort.triangularHeapify(array, currentLen);

            if (delay) Delays.setSleepRatio(Delays.getSleepRatio()/10);
            else       Delays.changeSkipped(false);
        }
    },
    CIRCLE {
        public String getName() {
            return "First Circle Pass";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            Reads Reads = arrayVisualizer.getReads();
            makeRandom(arrayVisualizer);

            shuffle(array, 0, currentLen, delay ? 0.5 : 0, Writes);

            int n = 1;
            for (; n < currentLen; n*=2);

            circleSortRoutine(array, 0, n-1, currentLen, delay ? 0.5 : 0, Reads, Writes);
        }

        public void circleSortRoutine(int[] array, int lo, int hi, int end, double sleep, Reads Reads, Writes Writes) {
            if (lo == hi) return;

            int high = hi;
            int low = lo;
            int mid = (hi - lo) / 2;

            while (lo < hi) {
                if (hi < end && Reads.compareIndices(array, lo, hi, sleep / 2, true) > 0)
                    Writes.swap(array, lo, hi, sleep, true, false);

                lo++;
                hi--;
            }

            circleSortRoutine(array, low, low + mid, end, sleep/2, Reads, Writes);
            if (low + mid + 1 < end) circleSortRoutine(array, low + mid + 1, high, end, sleep/2, Reads, Writes);
        }
    },
    CIRCLE_NO_SHUFFLE {
        public String getName() {
            return "Circle Pass (No Shuffle)";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            Reads Reads = arrayVisualizer.getReads();

            int n = 1;
            for (; n < currentLen; n*=2);

            circleSortRoutine(array, 0, n-1, currentLen, delay ? 0.5 : 0, Reads, Writes);
        }

        public void circleSortRoutine(int[] array, int lo, int hi, int end, double sleep, Reads Reads, Writes Writes) {
            if (lo == hi) return;

            int high = hi;
            int low = lo;
            int mid = (hi - lo) / 2;

            while (lo < hi) {
                if (hi < end && Reads.compareIndices(array, lo, hi, sleep / 2, true) > 0)
                    Writes.swap(array, lo, hi, sleep, true, false);

                lo++;
                hi--;
            }

            circleSortRoutine(array, low, low + mid, end, sleep/2, Reads, Writes);
            if (low + mid + 1 < end) circleSortRoutine(array, low + mid + 1, high, end, sleep/2, Reads, Writes);
        }
    },
    PAIRWISE {
        public String getName() {
            return "Final Pairwise Pass";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            Reads Reads = arrayVisualizer.getReads();
            makeRandom(arrayVisualizer);

            shuffle(array, 0, currentLen, delay ? 0.5 : 0, Writes);

            //create pairs
            for (int i = 1; i < currentLen; i+=2)
                if (Reads.compareIndices(array, i - 1, i, delay ? 0.5 : 0, true) > 0)
                    Writes.swap(array, i-1, i, delay ? 0.5 : 0, true, false);

            Highlights.clearMark(2);

            int[] temp = new int[currentLen];

            //sort the smaller and larger of the pairs separately with pigeonhole sort
            for (int m = 0; m < 2; m++) {
                for (int k = m; k < currentLen; k+=2)
                    Writes.write(temp, array[k], temp[array[k]] + 1, 0, false, true);

                int i = 0, j = m;
                while (true) {
                    while (i < currentLen && temp[i] == 0) i++;
                    if (i >= currentLen) break;

                    Writes.write(array, j, i, delay ? 0.5 : 0, true, false);

                    j+=2;
                    Writes.write(temp, i, temp[i] - 1, 0, false, true);
                }
            }
        }
    },
    PAIRWISE_NO_SHUFFLE {
        public String getName() {
            return "Final Pairwise Pass (No Shuffle)";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            Reads Reads = arrayVisualizer.getReads();

            //create pairs
            for (int i = 1; i < currentLen; i+=2)
                if (Reads.compareIndices(array, i - 1, i, delay ? 0.5 : 0, true) > 0)
                    Writes.swap(array, i-1, i, delay ? 0.5 : 0, true, false);

            Highlights.clearMark(2);

            int[] temp = new int[currentLen];

            //sort the smaller and larger of the pairs separately with pigeonhole sort
            for (int m = 0; m < 2; m++) {
                for (int k = m; k < currentLen; k+=2)
                    Writes.write(temp, array[k], temp[array[k]] + 1, 0, false, true);

                int i = 0, j = m;
                while (true) {
                    while (i < currentLen && temp[i] == 0) i++;
                    if (i >= currentLen) break;

                    Writes.write(array, j, i, delay ? 0.5 : 0, true, false);

                    j+=2;
                    Writes.write(temp, i, temp[i] - 1, 0, false, true);
                }
            }
        }
    },
    WEAVE {
        public String getName() {
            return "Final Weave Pass";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            makeRandom(arrayVisualizer);

            shuffle(array, 0, currentLen, delay ? 0.5 : 0, Writes);

            Highlights.clearMark(2);

            int[] temp = new int[currentLen];

            //sort the smaller and larger of the pairs separately with pigeonhole sort
            for (int m = 0; m < 2; m++) {
                for (int k = m; k < currentLen; k+=2)
                    Writes.write(temp, array[k], temp[array[k]] + 1, 0, false, true);

                int i = 0, j = m;
                while (true) {
                    while (i < currentLen && temp[i] == 0) i++;
                    if (i >= currentLen) break;

                    Writes.write(array, j, i, delay ? 0.5 : 0, true, false);

                    j+=2;
                    Writes.write(temp, i, temp[i] - 1, 0, false, true);
                }
            }
        }
    },
    WEAVE_NO_SHUFFLE {
        public String getName() {
            return "Final Weave Pass (No Shuffle)";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            int[] temp = new int[currentLen];

            //sort the smaller and larger of the pairs separately with pigeonhole sort
            for (int m = 0; m < 2; m++) {
                for (int k = m; k < currentLen; k+=2)
                    Writes.write(temp, array[k], temp[array[k]] + 1, 0, false, true);

                int i = 0, j = m;
                while (true) {
                    while (i < currentLen && temp[i] == 0) i++;
                    if (i >= currentLen) break;

                    Writes.write(array, j, i, delay ? 0.5 : 0, true, false);

                    j+=2;
                    Writes.write(temp, i, temp[i] - 1, 0, false, true);
                }
            }
        }
    },
    REC_REV {
        public String getName() {
            return "Recursive Reversal";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            reversalRec(array, 0, currentLen, delay ? 1 : 0, Writes);
        }

        public void reversalRec(int[] array, int a, int b, double sleep, Writes Writes) {
            if (b-a < 2) return;

            Writes.reversal(array, a, b-1, sleep, true, false);

            int m = (a+b)/2;
            this.reversalRec(array, a, m, sleep/2, Writes);
            this.reversalRec(array, m, b, sleep/2, Writes);
        }
    },
    GRAY_CODE {
        public String getName() {
            return "Gray Code Fractal";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            reversalRec(array, 0, currentLen, false, delay ? 1 : 0, Writes);
        }

        public void reversalRec(int[] array, int a, int b, boolean bw, double sleep, Writes Writes) {
            if (b-a < 3) return;

            int m = (a+b)/2;

            if (bw) Writes.reversal(array, a, m-1, sleep, true, false);
            else    Writes.reversal(array, m, b-1, sleep, true, false);

            this.reversalRec(array, a, m, false, sleep/2, Writes);
            this.reversalRec(array, m, b, true, sleep/2, Writes);
        }
    },
    SIERPINSKI {
        public String getName() {
            return "Sierpinski Triangle";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            double delay = arrayVisualizer.shuffleEnabled() ? 1 : 0;
            int[] triangle = new int[currentLen];
            triangleRec(triangle, 0, currentLen);

            int[] temp = Arrays.copyOf(array, currentLen);
            for (int i = 0; i < currentLen; i++)
                Writes.write(array, i, temp[triangle[i]], delay, true, false);
        }

        public void triangleRec(int[] array, int a, int b) {
            if (b-a < 2) return;
            if (b-a == 2) {
                array[a+1]++;
                return;
            }

            int h = (b-a)/3, t1 = (a+a+b)/3, t2 = (a+b+b+2)/3;
            for (int i = a;  i < t1; i++) array[i] += h;
            for (int i = t1; i < t2; i++) array[i] += 2*h;

            triangleRec(array, a, t1);
            triangleRec(array, t1, t2);
            triangleRec(array, t2, b);
        }
    },
    TRIANGULAR {
        public String getName() {
            return "Triangular";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            int[] triangle = new int[currentLen];

            int j = 0, k = 2;
            int max = 0;

            for (int i = 1; i < currentLen; i++, j++) {
                if (i == k) {
                    j = 0;
                    k *= 2;
                }
                triangle[i] = triangle[j]+1;
                if (triangle[i] > max) max = triangle[i];
            }
            int[] cnt = new int[max+1];

            for (int i = 0; i < currentLen; i++)
                cnt[triangle[i]]++;

            for (int i = 1; i < cnt.length; i++)
                cnt[i] += cnt[i-1];

            for (int i = currentLen-1; i >= 0; i--)
                triangle[i] = --cnt[triangle[i]];

            int[] temp = Arrays.copyOf(array, currentLen);
            for (int i = 0; i < currentLen; i++)
                Writes.write(array, i, temp[triangle[i]], delay ? 1 : 0, true, false);
        }
    },
    ANTI_CIRCLE {
        public String getName() {
            return "Backwards Circle Pass";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            Reads Reads = arrayVisualizer.getReads();
            double sleep = arrayVisualizer.shuffleEnabled() ? 0.25 : 0;

            int n = 1;
            for (; n < currentLen; n*=2);

            circleSortRoutine(array, 0, n-1, currentLen, sleep, Reads, Writes, false, 0);
        }

        public void circleSortRoutine(int[] array, int lo, int hi, int end, double sleep, Reads Reads, Writes Writes, boolean dir, int d) {
            Writes.recordDepth(d);
            if (lo == hi) return;

            int high = hi;
            int low = lo;
            int mid = (hi - lo) / 2;

            while (lo < hi) {
                if (dir) {
                    if (hi < end && Reads.compareIndices(array, lo, hi, sleep / 2, true) > 0)
                        Writes.swap(array, lo, hi, sleep, true, false);

                    lo++;
                    hi--;
                } else {
                    if (hi < end && Reads.compareIndices(array, lo, hi, sleep / 2, true) < 0)
                        Writes.swap(array, lo, hi, sleep, true, false);

                    lo++;
                    hi--;
                }
            }

            Writes.recursion();
            circleSortRoutine(array, low, low + mid, end, sleep, Reads, Writes, dir, d + 1);
            if (low + mid + 1 < end) {
                Writes.recursion();
                circleSortRoutine(array, low + mid + 1, high, end, sleep, Reads, Writes, !dir, d + 1);
            }
        }
    },
    MODULO {
        public String getName() {
            return "Modulo";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            double sleep = arrayVisualizer.shuffleEnabled() ? 1 : 0;

            for (int i = 1; i < currentLen; i++) {
                if ((i * 2) % currentLen != i) Writes.swap(array, i, (i * 2)%currentLen, sleep, true, false);
            }
        }
    },
    QSORT_BAD {
        public String getName() {
            return "Quicksort Adversary";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            for (int j = currentLen-currentLen%2-2, i = j-1; i >= 0; i-=2, j--)
                Writes.swap(array, i, j, delay ? 1 : 0, true, false);
        }
    },
    PDQ_BAD {
        Reads Reads;
        Writes Writes;
        Highlights Highlights;
        boolean delay;
        double sleep;

        int[] temp;
        boolean hasCandidate;
        int gas, frozen, candidate;

        final class PDQPair {
            private int pivotPosition;
            private boolean alreadyPartitioned;

            private PDQPair(int pivotPos, boolean presorted) {
                this.pivotPosition = pivotPos;
                this.alreadyPartitioned = presorted;
            }

            public int getPivotPosition() {
                return this.pivotPosition;
            }

            public boolean getPresortBool() {
                return this.alreadyPartitioned;
            }
        }

        public String getName() {
            return "PDQ Adversary";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            delay = arrayVisualizer.shuffleEnabled();
            sleep = delay ? 1 : 0;
            this.Reads = arrayVisualizer.getReads();
            this.Writes = Writes;
            this.Highlights = Highlights;

            int[] copy = new int[currentLen];

            hasCandidate = false;
            frozen = 1;
            temp = new int[currentLen];
            gas = currentLen;
            for (int i = 0; i < currentLen; i++) {
                Writes.write(copy, i, array[i], 0, false, false);
                Writes.write(array, i, i, 0, false, false);
                Writes.write(temp, i, gas, sleep, true, true);
            }

            pdqLoop(array, 0, currentLen, false, PDQSorting.pdqLog(currentLen));

            for (int i = 0; i < currentLen; i++) {
                Writes.write(array, i, copy[temp[i] - 1], sleep, true, false);
            }
        }

        protected int compare(int ap, int bp) {
            Reads.addComparison();
            int a, b;
            if (!hasCandidate) {
                candidate = 0;
                hasCandidate = true;
            }

            a = ap;
            b = bp;

            if (temp[a] == gas && temp[b] == gas)
                if (a == candidate)
                    temp[a] = frozen++;
                else
                    temp[b] = frozen++;

            if (temp[a] == gas) {
                candidate = a;
                return 1;
            }

            if (temp[b] == gas) {
                candidate = b;
                return -1;
            }

            if (temp[a] < temp[b])
                return -1;
            if (temp[a] > temp[b])
                return 1;
            return 0;
        }

        protected void pdqLoop(int[] array, int begin, int end, boolean branchless, int badAllowed) {
            boolean leftmost = true;

            while (true) {
                int size = end - begin;

                if (size < 24) {
                    if (leftmost) this.pdqInsertSort(array, begin, end);
                    else this.pdqUnguardInsertSort(array, begin, end);
                    return;
                }

                int halfSize = size / 2;
                if (size > 128) {
                    this.pdqSortThree(array, begin, begin + halfSize, end - 1);
                    this.pdqSortThree(array, begin + 1, begin + (halfSize - 1), end - 2);
                    this.pdqSortThree(array, begin + 2, begin + (halfSize + 1), end - 3);
                    this.pdqSortThree(array, begin + (halfSize - 1), begin + halfSize, begin + (halfSize + 1));
                    Writes.swap(array, begin, begin + halfSize, 1, true, false);
                    Highlights.clearMark(2);
                } else this.pdqSortThree(array, begin + halfSize, begin, end - 1);

                if (!leftmost && !(compare(array[begin - 1], array[begin]) < 0)) {
                    begin = this.pdqPartLeft(array, begin, end) + 1;
                    continue;
                }

                PDQPair partResult = this.pdqPartRight(array, begin, end);

                int pivotPos = partResult.getPivotPosition();
                boolean alreadyParted = partResult.getPresortBool();

                int leftSize = pivotPos - begin;
                int rightSize = end - (pivotPos + 1);
                boolean highUnbalance = leftSize < size / 8 || rightSize < size / 8;

                if (highUnbalance) {
                    if (--badAllowed == 0) {
                        int length = end - begin;
                        for (int i = length / 2; i >= 1; i--) {
                            siftDown(array, i, length, begin, sleep, true);
                        }
                        return;
                    }

                    if (leftSize >= 24) {
                        Writes.swap(array, begin,           begin + leftSize / 4, sleep, true, false);
                        Writes.swap(array, pivotPos-1,   pivotPos - leftSize / 4, sleep, true, false);

                        if (leftSize > 128) {
                            Writes.swap(array, begin+1,           begin + (leftSize / 4 + 1), sleep, true, false);
                            Writes.swap(array, begin+2,           begin + (leftSize / 4 + 2), sleep, true, false);
                            Writes.swap(array, pivotPos-2,     pivotPos - (leftSize / 4 + 1), sleep, true, false);
                            Writes.swap(array, pivotPos-3,     pivotPos - (leftSize / 4 + 2), sleep, true, false);
                        }
                    }

                    if (rightSize >= 24) {
                        Writes.swap(array, pivotPos+1,   pivotPos + (1 + rightSize / 4), sleep, true, false);
                        Writes.swap(array, end-1,                   end - rightSize / 4, sleep, true, false);

                        if (rightSize > 128) {
                            Writes.swap(array, pivotPos+2,   pivotPos + (2 + rightSize / 4), sleep, true, false);
                            Writes.swap(array, pivotPos+3,   pivotPos + (3 + rightSize / 4), sleep, true, false);
                            Writes.swap(array, end-2,             end - (1 + rightSize / 4), sleep, true, false);
                            Writes.swap(array, end-3,             end - (2 + rightSize / 4), sleep, true, false);
                        }
                    }
                    Highlights.clearMark(2);
                } else {
                    if (alreadyParted && pdqPartialInsertSort(array, begin, pivotPos)
                                        && pdqPartialInsertSort(array, pivotPos + 1, end))
                        return;
                }

                this.pdqLoop(array, begin, pivotPos, branchless, badAllowed);
                begin = pivotPos + 1;
                leftmost = false;
            }
        }

        private void siftDown(int[] array, int root, int dist, int start, double sleep, boolean isMax) {
            int compareVal = 0;

            if (isMax) compareVal = -1;
            else compareVal = 1;

            while (root <= dist / 2) {
                int leaf = 2 * root;
                if (leaf < dist && compare(array[start + leaf - 1], array[start + leaf]) == compareVal) {
                    leaf++;
                }
                Highlights.markArray(1, start + root - 1);
                Highlights.markArray(2, start + leaf - 1);
                if (compare(array[start + root - 1], array[start + leaf - 1]) == compareVal) {
                    Writes.swap(array, start + root - 1, start + leaf - 1, 0, true, false);
                    root = leaf;
                } else break;
            }
        }

        private PDQPair pdqPartRight(int[] array, int begin, int end) {
            int pivot = array[begin];
            int first = begin;
            int last = end;

            while (compare(array[++first], pivot) < 0) {
                Highlights.markArray(1, first);
            }

            if (first - 1 == begin)
                while (first < last && !(compare(array[--last], pivot) < 0)) {
                    Highlights.markArray(2, last);
                }
            else
                while (!(compare(array[--last], pivot) < 0)) {
                    Highlights.markArray(2, last);
                }

            boolean alreadyParted = first >= last;

            while (first < last) {
                Writes.swap(array, first, last, 1, true, false);
                while (compare(array[++first], pivot) < 0) {
                    Highlights.markArray(1, first);
                }
                while (!(compare(array[--last], pivot) < 0)) {
                    Highlights.markArray(2, last);
                }
            }
            Highlights.clearMark(2);

            int pivotPos = first - 1;
            Writes.write(array, begin, array[pivotPos], delay ? 1 : 0, true, false);
            Writes.write(array, pivotPos, pivot, delay ? 1 : 0, true, false);

            return new PDQPair(pivotPos, alreadyParted);
        }

        private boolean pdqPartialInsertSort(int[] array, int begin, int end) {
            if (begin == end) return true;

            double sleep = delay ? 1/3d : 0;

            int limit = 0;
            for (int cur = begin + 1; cur != end; ++cur) {
                if (limit > 8) return false;

                int sift = cur;
                int siftMinusOne = cur - 1;

                if (compare(array[sift], array[siftMinusOne]) < 0) {
                    int tmp = array[sift];

                    do {
                        Writes.write(array, sift--, array[siftMinusOne], sleep, true, false);
                    } while (sift != begin && compare(tmp, array[--siftMinusOne]) < 0);

                    Writes.write(array, sift, tmp, sleep, true, false);
                    limit += cur - sift;
                }
            }
            return true;
        }

        private int pdqPartLeft(int[] array, int begin, int end) {
            int pivot = array[begin];
            int first = begin;
            int last = end;

            while (compare(pivot, array[--last]) < 0) {
                Highlights.markArray(2, last);
            }

            if (last + 1 == end)
                while (first < last && !(compare(pivot, array[++first]) < 0)) {
                    Highlights.markArray(1, first);
                }
            else
                while (!(compare(pivot, array[++first]) < 0)) {
                    Highlights.markArray(1, first);
                }

            while (first < last) {
                Writes.swap(array, first, last, 1, true, false);
                while (compare(pivot, array[--last]) < 0) {
                    Highlights.markArray(2, last);
                }
                while (!(compare(pivot, array[++first]) < 0)) {
                    Highlights.markArray(1, first);
                }
            }
            Highlights.clearMark(2);

            int pivotPos = last;
            Writes.write(array, begin, array[pivotPos], delay ? 1 : 0, true, false);
            Writes.write(array, pivotPos, pivot, delay ? 1 : 0, true, false);

            return pivotPos;
        }

        private void pdqSortThree(int[] array, int a, int b, int c) {
            this.pdqSortTwo(array, a, b);
            this.pdqSortTwo(array, b, c);
            this.pdqSortTwo(array, a, b);
        }

        private void pdqSortTwo(int[] array, int a, int b) {
            if (compare(array[b], array[a]) < 0) {
                Writes.swap(array, a, b, 1, true, false);
            }
            Highlights.clearMark(2);
        }

        private void pdqInsertSort(int[] array, int begin, int end) {
            if (begin == end) return;

            double sleep = delay ? 1/3d : 0;

            for (int cur = begin + 1; cur != end; ++cur) {
                int sift = cur;
                int siftMinusOne = cur - 1;

                if (compare(array[sift], array[siftMinusOne]) < 0) {
                    int tmp = array[sift];
                    do {
                        Writes.write(array, sift--, array[siftMinusOne], sleep, true, false);
                    } while (sift != begin && compare(tmp, array[--siftMinusOne]) < 0);

                    Writes.write(array, sift, tmp, sleep, true, false);
                }
            }
        }

        private void pdqUnguardInsertSort(int[] array, int begin, int end) {
            if (begin == end) return;

            double sleep = 1/3d;

            for (int cur = begin + 1; cur != end; ++cur) {
                int sift = cur;
                int siftMinusOne = cur - 1;

                if (compare(array[sift], array[siftMinusOne]) < 0) {
                    int tmp = array[sift];

                    do {
                        Writes.write(array, sift--, array[siftMinusOne], sleep, true, false);
                    } while (compare(tmp, array[--siftMinusOne]) < 0);

                    Writes.write(array, sift, tmp, sleep, true, false);
                }
            }
        }
    },
    GRAIL_BAD {
        public String getName() {
            return "Grailsort Adversary";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            makeRandom(arrayVisualizer);

            if (currentLen <= 16) Writes.reversal(array, 0, currentLen-1, delay ? 1 : 0, true, false);
            else {
                int blockLen = 1;
                while (blockLen * blockLen < currentLen) blockLen *= 2;

                int numKeys = (currentLen - 1) / blockLen + 1;
                int keys = blockLen + numKeys;

                shuffle(array, 0, currentLen, delay ? 0.25 : 0, Writes);
                sort(array, array, -1, 0, keys, delay ? 0.25 : 0, Writes);
                Writes.reversal(array, 0, keys-1, delay ? 0.25 : 0, true, false);
                Highlights.clearMark(2);
                sort(array, array, -1, keys, currentLen, delay ? 0.25 : 0, Writes);

                push(array, keys, currentLen, blockLen, delay ? 0.25 : 0, Writes);
            }
        }

        public void rotate(int[] array, int a, int m, int b, double sleep, Writes Writes) {
            Writes.reversal(array, a, m-1, sleep, true, false);
            Writes.reversal(array, m, b-1, sleep, true, false);
            Writes.reversal(array, a, b-1, sleep, true, false);
        }

        public void push(int[] array, int a, int b, int bLen, double sleep, Writes Writes) {
            int len = b-a,
                b1 = b - len%bLen, len1 = b1-a;
            if (len1 <= 2*bLen) return;

            int m = bLen;
            while (2*m < len) m *= 2;
            m += a;

            if (b1-m < bLen) push(array, a, m, bLen, sleep, Writes);
            else {
                m = a+b1-m;
                rotate(array, m-(bLen-2), b1-(bLen-1), b1, sleep, Writes);
                Writes.multiSwap(array, a, m, sleep/2, true, false);
                rotate(array, a, m, b1, sleep, Writes);
                m = a+b1-m;

                push(array, a, m, bLen, sleep/2, Writes);
                push(array, m, b, bLen, sleep/2, Writes);
            }
        }
    },
    SHUF_MERGE_BAD {
        public String getName() {
            return "Shuffle Merge Adversary";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int n = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            int[] tmp = new int[n];
            int d = 2, end = 1 << (int)(Math.log(n-1)/Math.log(2) + 1);

            while (d <= end) {
                int i = 0, dec = 0;
                double sleep = 1d/d;

                while (i < n) {
                    int j = i;
                    dec += n;
                    while (dec >= d) {
                        dec -= d;
                        j++;
                    }
                    int k = j;
                    dec += n;
                    while (dec >= d) {
                        dec -= d;
                        k++;
                    }
                    shuffleMergeBad(array, tmp, i, j, k, delay ? sleep : 0, Writes);
                    i = k;
                }
                d *= 2;
            }
        }

        public void shuffleMergeBad(int[] array, int[] tmp, int a, int m, int b, double sleep, Writes Writes) {
            if ((b-a)%2 == 1) {
                if (m-a > b-m) a++;
                else           b--;
            }
            shuffleBad(array, tmp, a, b, sleep, Writes);
        }

        //length is always even
        public void shuffleBad(int[] array, int[] tmp, int a, int b, double sleep, Writes Writes) {
            if (b-a < 2) return;

            int m = (a+b)/2;
            int s = (b-a-1)/4+1;

            a = m-s;
            b = m+s;
            int j = a;

            for (int i = a+1; i < b; i += 2)
                Writes.write(tmp, j++, array[i], sleep, true, true);
            for (int i = a; i < b; i += 2)
                Writes.write(tmp, j++, array[i], sleep, true, true);

            Writes.arraycopy(tmp, a, array, a, b-a, sleep, true, false);
        }
    },
    BIT_REVERSE {
        @Override
        public String getName() {
            return "Bit Reversal";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            int len = 1 << (int)(Math.log(arrayVisualizer.getCurrentLength())/Math.log(2));
            boolean delay = arrayVisualizer.shuffleEnabled();
            boolean pow2 = len == currentLen;

            int[] temp = Arrays.copyOf(array, currentLen);
            for (int i = 0; i < len; i++) array[i] = i;

            int m = 0;
            int d1 = len>>1, d2 = d1+(d1>>1);

            for (int i = 1; i < len-1; i++) {
                int j = d1;

                for (
                    int k = i, n = d2;
                    (k&1) == 0;
                    j -= n, k >>= 1, n >>= 1
                );
                m += j;
                if (m > i) Writes.swap(array, i, m, delay ? 1 : 0, true, false);
            }
            Highlights.clearMark(2);

            if (!pow2) {
                for (int i = len; i < currentLen; i++)
                    Writes.write(array, i, array[i-len], 0.5, true, false);

                int[] cnt = new int[len];

                for (int i = 0; i < currentLen; i++)
                    cnt[array[i]]++;

                for (int i = 1; i < cnt.length; i++)
                    cnt[i] += cnt[i-1];

                for (int i = currentLen-1; i >= 0; i--)
                    Writes.write(array, i, --cnt[array[i]], 0.5, true, false);
            }
            int[] bits = Arrays.copyOf(array, currentLen);

            for (int i = 0; i < currentLen; i++)
                Writes.write(array, i, temp[bits[i]], 0, true, false);
        }
    },
    BLOCK_RANDOMLY {
        @Override
        public String getName() {
            return "Randomly w/ Blocks";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            int blockSize = pow2lte((int)Math.sqrt(currentLen));
            currentLen -= currentLen%blockSize;
            boolean delay = arrayVisualizer.shuffleEnabled();
            double sleep = delay ? 1 : 0;

            makeRandom(arrayVisualizer);
            for (int i = 0; i < currentLen; i += blockSize) {
                int randomIndex = random.nextInt((currentLen - i) / blockSize) * blockSize + i;
                blockSwap(array, i, randomIndex, blockSize, Writes, sleep);
            }
        }

        private void blockSwap(int[] array, int a, int b, int len, Writes Writes, double sleep) {
            for (int i = 0; i < len; i++) {
                Writes.swap(array, a + i, b + i, sleep, true, false);
            }
        }

        private int pow2lte(int value) {
            int val;
            for (val = 1; val <= value; val <<= 1);
            return val >> 1;
        }
    },
    BLOCK_REVERSE {
        @Override
        public String getName() {
            return "Block Reverse";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            int blockSize = pow2lte((int)Math.sqrt(currentLen));
            currentLen -= currentLen % blockSize;
            boolean delay = arrayVisualizer.shuffleEnabled();
            double sleep = delay ? 1 : 0;

            int i = 0, j = currentLen - blockSize;
            while (i < j) {
                blockSwap(array, i, j, blockSize, Writes, sleep);
                i += blockSize;
                j -= blockSize;
            }
        }

        private void blockSwap(int[] array, int a, int b, int len, Writes Writes, double sleep) {
            for (int i = 0; i < len; i++) {
                Writes.swap(array, a + i, b + i, sleep, true, false);
            }
        }

        private int pow2lte(int value) {
            int val;
            for (val = 1; val <= value; val <<= 1);
            return val >> 1;
        }
    },
    PRIMES_REVERSED {
        @Override
        public String getName() {
            return "Primes Reversed";
        }
        
        // sieve of Eratosthenes
        void sieve(boolean[] f, int n) {
            for (int i = 2; i <= n; i++) f[i] = true;
            for (int i = 2; i * i <= n; i++)
                if (f[i])
                    for (int j = i * i; j <= n; j += i) f[j] = false;
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            double sleep = arrayVisualizer.shuffleEnabled() ? 1 : 0;
            boolean[] f = new boolean[currentLen];
            sieve(f, currentLen - 1);
            int[] indices = new int[currentLen];
            int cnt = 0;
            for (int i1 = 0; i1 < currentLen; i1++)
                if (f[i1]) {
                    indices[cnt] = i1;
                    cnt++;
                }
            for (int i = 0; i < cnt / 2; i++) {
                Writes.swap(array, indices[i], indices[cnt - 1 - i], sleep, true, false);
            }
        }
    },
    UNIQUE {
        @Override
        public String getName() {
            return "Unique Pattern";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            double sleep = arrayVisualizer.shuffleEnabled() ? 0.5 : 0;

            sort(array, 0, currentLen, sleep, Writes);

            int[] tmp = new int[currentLen];
            int[] cnt = new int[currentLen];
            int[] pos = new int[currentLen];

            System.arraycopy(array, 0, tmp, 0, currentLen);

            int cIdx = 0, cPtr = 0, max = 0;

            for (int i = 0; i < currentLen; i++) {
                if (array[i] > array[cIdx]) {
                    cIdx = i;
                    cPtr = 0;
                }
                cnt[i] = cPtr;
                pos[cPtr]++;
                cPtr++;
                max = Math.max(max, cPtr);
            }
            for (int i = 1; i < max; i++) pos[i] += pos[i-1];

            for (int i = currentLen-1; i >= 0; i--)
                Writes.write(array, --pos[cnt[i]], tmp[i], sleep, true, false);
        }
    },
    ONLY_RUNS {
        public String getName() {
            return "Random Runs (PCBoy)";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            double sleep = arrayVisualizer.shuffleEnabled() ? 1 : 0;
            makeRandom(arrayVisualizer);
            shuffle(array, 0, currentLen, sleep, Writes);
            Highlights.clearMark(2);
            int size = Math.max(4, (int)Math.sqrt(currentLen));
            int r = random.nextInt(size-1)+1;
            int i = 0;
            for (; i + r < currentLen; i += r) {
                sort(array, i, i + r, sleep, Writes);
                r = random.nextInt(size-1)+1;
            }
            sort(array, i, currentLen, sleep, Writes);
        }
    },
    ONLY_RUNS_NO_SHUFFLE {
        public String getName() {
            return "Random Runs (No Shuffle)";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            double sleep = arrayVisualizer.shuffleEnabled() ? 1 : 0;
            makeRandom(arrayVisualizer);
            int size = Math.max(4, (int)Math.sqrt(currentLen));
            int r = random.nextInt(size-1)+1;
            int i = 0;
            for (; i + r < currentLen; i += r) {
                sort(array, i, i + r, sleep, Writes);
                r = random.nextInt(size-1)+1;
            }
            sort(array, i, currentLen, sleep, Writes);
        }
    },
    RUNS {
        public String getName() {
            return "Random Runs + Scrambles";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            double sleep = arrayVisualizer.shuffleEnabled() ? 1 : 0;
            makeRandom(arrayVisualizer);
            shuffle(array, 0, currentLen, sleep, Writes);
            Highlights.clearMark(2);
            int size = Math.max(4, (int)Math.sqrt(currentLen));
            int r = random.nextInt(size-1)+1;
            for (int i = 0; i + r < currentLen; i += r) {
                if (random.nextInt(3) == 0) sort(array, i, i + r, sleep, Writes);
                r = random.nextInt(size-1)+1;
            }
        }
    },
    XORSWAP {
        @Override
        public String getName() {
            return "XOR Swap";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            double sleep = arrayVisualizer.shuffleEnabled() ? 0.5 : 0;
            int j = 0;
            for (int i = 0; i < currentLen; ++i) {
                j = (i + 1 ^ i) % currentLen;
                Writes.swap(array, i, j, sleep, true, false);
            }
        }
    },
    BST_PREORDER {
        public String getName() {
            return "Pre-order BST Traversal";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLength = arrayVisualizer.getCurrentLength();
            double sleep = arrayVisualizer.shuffleEnabled() ? 0.5 : 0;
            
            int[] temp = new int[currentLength];
            int[] idx  = {0};
            
            traverse(array, temp, idx, 0, currentLength, Highlights, Delays, sleep);
            Writes.arraycopy(temp, 0, array, 0, currentLength, sleep, true, false);
        }
        public void traverse(int[] array, int[] temp, int[] idx, int a, int b, Highlights Highlights, Delays Delays, double sleep) {
            if(b-a < 1) return;
            
            int m = (a+b)/2;
            temp[idx[0]++] = array[m];
            Highlights.markArray(1, m);
            Delays.sleep(sleep);
            
            traverse(array, temp, idx, a, m, Highlights, Delays, sleep);
            traverse(array, temp, idx, m+1, b, Highlights, Delays, sleep);
        }
    },
    RBST_PREORDER {
        public String getName() {
            return "Pre-order RBST Traversal";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLength = arrayVisualizer.getCurrentLength();
            Reads Reads = arrayVisualizer.getReads();
            double sleep = arrayVisualizer.shuffleEnabled() ? 0.1 : 0;
           
            int[] lower = new int[currentLength];
            int[] upper = new int[currentLength];
            int[] temp  = new int[currentLength];
            makeRandom(arrayVisualizer);
            shuffle(array, 0, currentLength, sleep, Writes);
            treesort(array, lower, upper, currentLength, sleep, Reads, Highlights, Delays);
            
            int[] idx = {0};
            traverse(Writes, Highlights, Delays, array, idx, temp, lower, upper, 0, sleep);
            Writes.arraycopy(temp, 0, array, 0, currentLength, sleep, true, false);
        }
        public void traverse(Writes Writes, Highlights Highlights, Delays Delays, int[] array, int[] idx, int[] temp, int[] lower, int[] upper, int r, double sleep) {
            Writes.write(temp, idx[0]++, array[r], 0, false, true);
            Highlights.markArray(1, r);
            Delays.sleep(sleep);
            
            if(lower[r] != 0) traverse(Writes, Highlights, Delays, array, idx, temp, lower, upper, lower[r], sleep);
            if(upper[r] != 0) traverse(Writes, Highlights, Delays, array, idx, temp, lower, upper, upper[r], sleep);
        }
    },
    BST_POSTORDER {
        public String getName() {
            return "Post-order BST Traversal";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLength = arrayVisualizer.getCurrentLength();
            double sleep = arrayVisualizer.shuffleEnabled() ? 0.5 : 0;
            
            int[] temp = new int[currentLength];
            int[] idx  = {currentLength};
            
            traverse(array, temp, idx, 0, currentLength, Highlights, Delays, sleep);
            Writes.arraycopy(temp, 0, array, 0, currentLength, sleep, true, false);
        }
        public void traverse(int[] array, int[] temp, int[] idx, int a, int b, Highlights Highlights, Delays Delays, double sleep) {
            if(b-a < 1) return;
            
            int m = (a+b)/2;
            temp[--idx[0]] = array[m];
            Highlights.markArray(1, m);
            Delays.sleep(sleep);
            
            traverse(array, temp, idx, m+1, b, Highlights, Delays, sleep);
            traverse(array, temp, idx, a, m, Highlights, Delays, sleep);
        }
    },
    RBST_POSTORDER {
        public String getName() {
            return "Post-order RBST Traversal";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLength = arrayVisualizer.getCurrentLength();
            Reads Reads = arrayVisualizer.getReads();
            double sleep = arrayVisualizer.shuffleEnabled() ? 0.1 : 0;
           
            int[] lower = new int[currentLength];
            int[] upper = new int[currentLength];
            int[] temp  = new int[currentLength];
            makeRandom(arrayVisualizer);
            shuffle(array, 0, currentLength, sleep, Writes);
            treesort(array, lower, upper, currentLength, sleep, Reads, Highlights, Delays);
            
            int[] idx = {currentLength};
            traverse(Writes, Highlights, Delays, array, idx, temp, lower, upper, 0, sleep);
            Writes.arraycopy(temp, 0, array, 0, currentLength, sleep, true, false);
        }
        public void traverse(Writes Writes, Highlights Highlights, Delays Delays, int[] array, int[] idx, int[] temp, int[] lower, int[] upper, int r, double sleep) {
            Writes.write(temp, --idx[0], array[r], 0, false, true);
            Highlights.markArray(1, r);
            Delays.sleep(sleep);
            
            if(upper[r] != 0) traverse(Writes, Highlights, Delays, array, idx, temp, lower, upper, upper[r], sleep);
            if(lower[r] != 0) traverse(Writes, Highlights, Delays, array, idx, temp, lower, upper, lower[r], sleep);
        }
    },
    RBST_BREADTH {
        public String getName() {
            return "Breadth RBST Traversal";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLength = arrayVisualizer.getCurrentLength();
            Reads Reads = arrayVisualizer.getReads();
            double sleep = arrayVisualizer.shuffleEnabled() ? 0.1 : 0;
           
            int[] lower = new int[currentLength];
            int[] upper = new int[currentLength];
            int[] temp  = new int[currentLength];
            makeRandom(arrayVisualizer);
            shuffle(array, 0, currentLength, sleep, Writes);
            treesort(array, lower, upper, currentLength, sleep, Reads, Highlights, Delays);
            
            Queue<Integer> q = new LinkedList<Integer>();
            q.add(0);
            int i = 0;

            while(!q.isEmpty()) {
                int r = q.poll();
                
                Writes.write(temp, i++, array[r], 0, true, false);
                
                if(lower[r] != 0) q.add(lower[r]);
                if(upper[r] != 0) q.add(upper[r]);
            }
            Writes.arraycopy(temp, 0, array, 0, currentLength, sleep, true, false);
        }
    },
    SHELL_WORST_BASE {
    	public String getName() {
    		return "Shell Killer";
    	}
    	public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int n = arrayVisualizer.getCurrentLength();
    		int[] gaps = {1247501165, 561937462, 253124983, 114020263, 51360479, 23135351, 10528127, 4697153, 2131981, 973657, 443557, 197803, 89129, 40354,
					   18118, 8129, 3659, 1636, 701, 301, 132, 57, 23, 10, 4, 1};
    		int[] cnts = Writes.createExternalArray(n);
    		int[] tmp = Writes.createExternalArray(n);
    		int x = gaps.length;
    		cnts[0] = tmp[0] = x + 1;
    		for(int j : gaps) {
    			x--;
    			for(int i = j; i < n; i++) {
    				int v = i - j;
    				if(cnts[v] > 0 && cnts[i] == 0) {
    					cnts[i] = x;
    				}
    			}
    		}
    		int min = cnts[0], max = cnts[0];
    		for(int i = 1; i < n; i++) {
    			min = Math.min(cnts[i], min);
    			max = Math.max(cnts[i], max);
    		}
    		for(int i = 0; i < n; i++) {
    			Writes.write(cnts, i, cnts[i] - min, 0, false, true);
    		}
    		int[] cnt2 = Writes.createExternalArray(max-min+2);
    		for(int i = 0; i < n; i++) {
    			Writes.write(cnt2, cnts[i], cnt2[cnts[i]] + 1, 0, false, true);
    		}
    		for(int i = 1; i < max - min + 1; i++) {
    			Writes.write(cnt2, i, cnt2[i] + cnt2[i-1], 0, false, true);
    		}
    		for(int i = 0; i < n; i++) {
    			Writes.write(tmp, i, array[--cnt2[cnts[i]]], 1, true, true);
    		}
    		Writes.deleteExternalArrays(cnts, cnt2);
    		Writes.arraycopy(tmp, 0, array, 0, n, 1, true, false);
    		Writes.deleteExternalArrays(tmp);
    	}
    },
    /*SHELL_WORST_NEUE {
    	public String getName() {
    		return "Shell Killer";
    	}
    	public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int n = arrayVisualizer.getCurrentLength();
    		int[] gaps = {1247501165, 561937462, 253124983, 114020263, 51360479, 23135351, 10528127, 4697153, 2131981, 973657, 443557, 197803, 89129, 40354,
					   18118, 8129, 3659, 1636, 701, 301, 132, 57, 23, 10, 4, 1};
    		int[] cnts = Writes.createExternalArray(n);
    		int[] tmp = Writes.createExternalArray(n);
    		int x = gaps.length;
    		cnts[0] = tmp[0] = x;
    		for(int j : gaps) {
    			x--;
    			for(int i = j; i < n; i++) {
    				int v = i - j;
    				if(tmp[v] > 0) {
    					if(tmp[i] == 0) {
    						tmp[i] = 1;
    						cnts[i] = x;
    					} else {
    						if(cnts[v] > cnts[i]) cnts[v] += x;
    					}
    				}
    			}
    		}
    		int min = cnts[0], max = cnts[0];
    		for(int i = 1; i < n; i++) {
    			min = Math.min(cnts[i], min);
    			max = Math.max(cnts[i], max);
    		}
    		for(int i = 0; i < n; i++) {
    			Writes.write(cnts, i, cnts[i] - min, 0, false, true);
    		}
    		int[] cnt2 = Writes.createExternalArray(max-min+2);
    		for(int i = 0; i < n; i++) {
    			Writes.write(cnt2, cnts[i], cnt2[cnts[i]] + 1, 0, false, true);
    		}
    		for(int i = 1; i < max - min + 1; i++) {
    			Writes.write(cnt2, i, cnt2[i] + cnt2[i-1], 0, false, true);
    		}
    		for(int i = 0; i < n; i++) {
    			Writes.write(tmp, i, array[--cnt2[cnts[i]]], 1, true, true);
    		}
    		Writes.deleteExternalArrays(cnts, cnt2);
    		Writes.arraycopy(tmp, 0, array, 0, n, 1, true, false);
    		Writes.deleteExternalArrays(tmp);
    	}
    },*/
    RANDOM_RUNS {
        public String getName() {
            return "Random Runs";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int n = arrayVisualizer.getCurrentLength();
            boolean d = arrayVisualizer.shuffleEnabled();
            makeRandom(arrayVisualizer);
            shuffle(array, 0, n, d?1:0, Writes);
            for(int i = 0; i < n;) {
            	int r = random.nextInt(n / 24);
            	sort(array, array, -1, i, Math.min(i + r, n), d?1:0, Writes);
            	i += r;
            }
        }
    },
    TRAIL {
        public String getName() {
            return "Trail";
        }
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            Reads reads = ArrayVisualizer.getReads();
            int min = currentLen/2, max=min, now=min;
            int[] trail = new int[currentLen];
            for(int i=0; i<currentLen-1; i++) {
            	trail[i]=now;
            	now-=reads.compareValues(array[i], array[i+1]);
            	if(now<min) min=now; if(now>max) max=now;
            }
            trail[currentLen-1] = now;
            for(int i=0; i<currentLen; i++) {
            	double v = (trail[i]-min)/((max-min)/(double)currentLen);
            	Writes.write(array, i, (int) v, 1, true, false);
            }
        }
    };

    /**
     * Places a sorted copy of the range {@code [start, end)} of {@code array} into
     * the range starting at {@code dest_start} of {@code dest_array}. Uses
     * Optimized Pigeonhole Sort.
     * 
     * @param array      the source array
     * @param dest_array the destination array. Can be the same as {@code array}.
     * @param dest_start the start of the destination range. A value of -1 can be
     *                   used to equate the destination range to the source range.
     * @param start      the start of the source range, inclusive
     * @param end        the end of the source range, exclusive
     * @param sleep      the visualization delay
     * @param Writes     the {@link Writes} instance
     * @see #sort(int[], int, int, double, Writes)
     */
    public void sort(int[] array, int[] dest_array, int dest_start, int start, int end, double sleep, Writes Writes) {
    	if (dest_start < 0) dest_start = start;
        int min = array[start], max = min;
        for (int i = start+1; i < end; i++) {
            if (array[i] < min) min = array[i];
            else if (array[i] > max) max = array[i];
        }

        int size = max - min + 1;
        int[] holes = new int[size];

        for (int i = start; i < end; i++)
            Writes.write(holes, array[i] - min, holes[array[i] - min] + 1, 0, false, true);

        for (int i = 0; i < size; i++) {
            while (holes[i] > 0) {
                Writes.write(holes, i, holes[i] - 1, 0, false, true);
                Writes.write(dest_array, dest_start++, i + min, sleep, true, false);
            }
        }
    }

    /**
     * Sorts the range {@code [start, end)} of {@code array} using Optimized
     * Pigeonhole Sort.
     * 
     * @param array  the array
     * @param start  the start of the range, inclusive
     * @param end    the end of the range, exclusive
     * @param sleep  the visualization delay
     * @param writes the {@link Writes} instance
     * @see #sort(int[], int[], int, int, int, double, Writes)
     */
    void sort(int[] array, int start, int end, double sleep, Writes writes) {
        sort(array, array, -1, start, end, sleep, writes);
    }

    public void shuffle(int[] array, int start, int end, double sleep, Writes Writes) {
        for (int i = start; i < end; i++){
            int randomIndex = random.nextInt(end - i) + i;
            Writes.swap(array, i, randomIndex, sleep, true, false);
        }
    }

    public void treesort(int[] array, int[] lower, int[] upper, int currentLength, double sleep, Reads Reads, Highlights Highlights, Delays Delays) {
        for(int i = 1; i < currentLength; i++) {
            Highlights.markArray(2, i);
            int c = 0;
            
            while(true) {
                Highlights.markArray(1, c);
                Delays.sleep(sleep);
                
                int[] next = Reads.compareValues(array[i], array[c]) < 0 ? lower : upper;
                
                if(next[c] == 0) {
                    next[c] = i;
                    break;
                }
                else c = next[c];
            }
        }
        Highlights.clearMark(2);
    }
    
    private static Random random;
    public void makeRandom(ArrayVisualizer arrayVisualizer) {
    	if(arrayVisualizer.isSeeded()) {
    		random = new Random(1337);
    	} else {
    		random = new Random();
    	}
    }

    public Random getRng() {
        return ArrayVisualizer.getInstance().isSeeded() ? new Random(1337) : new Random();
    }

    public abstract String getName();
    public abstract void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes);
}
