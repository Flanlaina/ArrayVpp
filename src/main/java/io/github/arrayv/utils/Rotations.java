package io.github.arrayv.utils;

import io.github.arrayv.main.ArrayVisualizer;

public final class Rotations {
    // @checkstyle:off ConstantNameCheck - Unique case
    private static final Writes Writes = ArrayVisualizer.getInstance().getWrites();
    private static final Highlights Highlights = ArrayVisualizer.getInstance().getHighlights();
    // @checkstyle:on ConstantNameCheck

    private Rotations() {
    }

    // utility functions
    private static void swapBlocksBackwards(int[] array, int a, int b, int len, double pause, boolean mark, boolean auxwrite) {
        for (int i = 0; i < len; i++) {
            Writes.swap(array, a + len - i - 1, b + len - i - 1, pause, mark, auxwrite);
        }
    }

    public static void blockSwap(int[] array, int a, int b, int len, double pause, boolean mark, boolean auxwrite) {
        for (int i = 0; i < len; i++) {
            Writes.swap(array, a + i, b + i, pause, mark, auxwrite);
        }
    }

    private static void shiftForwards(int[] array, int start, int length, double pause, boolean mark, boolean auxwrite) {
        int temp = array[start];
        if (mark) Highlights.clearMark(2);
        for (int i = 0; i < length; i++) {
            Writes.write(array, start + i, array[start + i + 1], pause, mark, auxwrite);
        }
        Writes.write(array, start + length, temp, pause, mark, auxwrite);
    }

    private static void shiftBackwards(int[] array, int start, int length, double pause, boolean mark, boolean auxwrite) {
        int temp = array[start + length];
        if (mark) Highlights.clearMark(2);
        for (int i = length; i > 0; i--) {
            Writes.write(array, start + i, array[start + i - 1], pause, mark, auxwrite);
        }
        Writes.write(array, start, temp, pause, mark, auxwrite);
    }

    private static int mapIndex(int index, int n, int length) {
        return (index - n + length) % length;
    }

    private static int swap(int[] arr, int a, int v, double pause, boolean mark, boolean auxwrite) {
        int old = arr[a];
        Writes.write(arr, a, v, pause, mark, auxwrite);
        return old;
    }


    // rotation algorithms
    public static void griesMills(int[] array, int pos, int lenA, int lenB, double pause, boolean mark, boolean auxwrite) {
        while (lenA != 0 && lenB != 0) {
            if (lenA <= lenB) {
                blockSwap(array, pos, pos + lenA, lenA, pause, mark, auxwrite);
                pos += lenA;
                lenB -= lenA;
            } else {
                blockSwap(array, pos + (lenA - lenB), pos + lenA, lenB, pause, mark, auxwrite);
                lenA -= lenB;
            }
        }
    }

    public static void threeReversal(int[] array, int pos, int lenA, int lenB, double pause, boolean mark, boolean auxwrite) {
        Writes.reversal(array, pos, pos + lenA - 1, pause, mark, auxwrite);
        Writes.reversal(array, pos + lenA, pos + lenA + lenB - 1, pause, mark, auxwrite);
        Writes.reversal(array, pos, pos + lenA + lenB - 1, pause, mark, auxwrite);
    }

    public static void holyGriesMills(int[] array, int pos, int lenA, int lenB, double pause, boolean mark, boolean auxwrite) {
        while (lenA > 1 && lenB > 1) {
            while (lenA <= lenB) {
                blockSwap(array, pos, pos + lenA, lenA, pause, mark, auxwrite);
                pos  += lenA;
                lenB -= lenA;
            }

            if (lenA <= 1 || lenB <= 1) break;

            while (lenA > lenB) {
                swapBlocksBackwards(array, pos + lenA - lenB, pos + lenA, lenB, pause, mark, auxwrite);
                lenA -= lenB;
            }
        }

        if (lenA == 1) {
            shiftForwards(array, pos, lenB, pause, mark, auxwrite);
        } else if (lenB == 1) {
            shiftBackwards(array, pos, lenA, pause, mark, auxwrite);
        }
    }

    // by thatsOven
    public static void helium(int[] array, int pos, int lenA, int lenB, double pause, boolean mark, boolean auxwrite) {
        while (lenB > 1 && lenA > 1) {
            if (lenB < lenA) {
                blockSwap(array, pos, pos + lenA, lenB, pause, mark, auxwrite);
                pos  += lenB;
                lenA -= lenB;
            } else {
                swapBlocksBackwards(array, pos, pos + lenB, lenA, pause, mark, auxwrite);
                lenB -= lenA;
            }
        }

        if      (lenB == 1) shiftBackwards(array, pos, lenA, pause, mark, auxwrite);
        else if (lenA == 1) shiftForwards(array, pos, lenB, pause, mark, auxwrite);
    }
    
    public static void neon(int[] array, int pos, int lenA, int lenB, double pause, boolean mark, boolean auxwrite) {
		int factor;
		while(lenA > 0 && lenB > 0) {
	    	if(lenA > lenB) {
	    		factor = lenA / lenB;
	    		for(int i=0; i<lenB; i++) {
	    			int t = array[pos+i+lenA];
	    			for(int j=1; j<=factor; j++) {
	    				int k = pos+i+lenA-(j*lenB);
	    				Writes.write(array, k+lenB, array[k], pause, mark, auxwrite);
	    			}
					Writes.write(array, pos+i+lenA-(factor*lenB), t, pause, mark, auxwrite);
	    		}
	    		lenA %= lenB;
	    	} else {
	    		factor = lenB / lenA;
	    		for(int i=0; i<lenA; i++) {
	    			int t=array[pos+i];
	    			for(int j=1; j<=factor; j++) {
	    				int k=pos+i+(j*lenA);
	    				Writes.write(array, k-lenA, array[k], pause, mark, auxwrite);
	    			}
	    			Writes.write(array, pos+i+(factor*lenA), t, pause, mark, auxwrite);
	    		}
	    		pos += factor*lenA;
	    		lenB %= lenA;
	    	}
		}
    }
    
    public static void neon21(int[] array, int pos, int lenA, int lenB, double pause, boolean mark, boolean auxwrite) {
    	int remainder, k;
		while(lenA > 0 && lenB > 0) {
	    	if(lenA > lenB) {
	    		remainder=lenA%lenB;
	    		for(int i=0; i<lenB; i++) {
	    			int t = array[pos+i+lenA];
	    			for(int j=lenB; j<=lenA-remainder; j+=lenB) {
	    				k=pos+i+lenA-j;
	    				Writes.write(array, k+lenB, array[k], pause, mark, auxwrite);
	    			}
					Writes.write(array, pos+i+remainder, t, pause, mark, auxwrite);
	    		}
	    		lenA %= lenB;
	    	} else {
	    		remainder=lenB%lenA;
	    		for(int i=0; i<lenA; i++) {
	    			int t=array[pos+i];
	    			for(int j=lenA; j<=lenB-remainder; j+=lenA) {
	    				k=pos+i+j;
	    				Writes.write(array, k-lenA, array[k], pause, mark, auxwrite);
	    			}
	    			Writes.write(array, pos+i+lenB-remainder, t, pause, mark, auxwrite);
	    		}
	    		pos += lenB-remainder;
	    		lenB %= lenA;
	    	}
		}
    }
    
    public static void neon22(int[] array, int pos, int lenA, int lenB, double pause, boolean mark, boolean auxwrite) {
    	int end=pos+lenA+lenB;
    	while(lenA>0 && lenB > 0) {
	    	if(lenA < lenB) {
				for(int i=0; i<lenA; i++) {
					int t=array[pos+i], j=pos+i+lenA;
					for(; j<end; j+=lenA) {
						Writes.write(array, j-lenA, array[j], pause, mark, auxwrite);
					}
					Writes.write(array, j-lenA, t, pause, mark, auxwrite);
				}
				pos += lenB;
				lenB %= lenA;
				lenA -= lenB;
			} else {
				for(int i=0; i<lenB; i++) {
					int t=array[pos+i+lenA], j=pos+i+lenA-lenB;
					for(; j>=pos; j-=lenB) {
						Writes.write(array, j+lenB, array[j], pause, mark, auxwrite);
					}
					Writes.write(array, j+lenB, t, pause, mark, auxwrite);
				}
	    		end = pos+lenB;
				lenA %= lenB;
				lenB -= lenA;
			}
    	}
    }
    
    // improved gries mills
    public static void beaker(int[] array, int pos, int lenA, int lenB, double pause, boolean mark, boolean auxwrite) {
    	while(lenA>0 && lenB > 0) {
	    	if(lenA < lenB) {
	    		for(int i=pos; i<pos+lenB; i++) {
	    			Writes.swap(array, i, i+lenA, pause, mark, auxwrite);
	    		}
				pos += lenB;
				lenB %= lenA;
				lenA -= lenB;
			} else {
	    		for(int i=pos+lenA-1; i>=pos; i--) {
	    			Writes.swap(array, i, i+lenB, pause, mark, auxwrite);
	    		}
				lenA %= lenB;
				lenB -= lenA;
			}
    	}
    }
    
    private static void clamberPush(int[] array, int from, int to, double pause, boolean mark, boolean auxwrite) {
		int t = array[from], t2;
    	if(from < to) {
    		for(int i = to; i > from; i--) {
    			t2 = array[i];
    			Writes.write(array, i, t, pause, mark, auxwrite);
    			t = t2;
    		}
    	} else {
    		for(int i = to; i < from; i++) {
    			t2 = array[i];
    			Writes.write(array, i, t, pause, mark, auxwrite);
    			t = t2;
    		}
    	}
		Writes.write(array, from, t, pause, mark, auxwrite);
    }

    // unidirectional beaker
    public static void uniBeaker(int[] array, int pos, int lenA, int lenB, double pause, boolean mark, boolean auxwrite) {
    	boolean backwards = lenA > lenB;
    	while((lenA + lenB) / 2 != Math.abs(lenA - lenB) / 2) {
    		if(backwards) {
    			swapBlocksBackwards(array, pos, pos + lenB, lenA, pause, mark, auxwrite);
	    		lenA %= lenB;
	    		lenB -= lenA;
    		} else {
				blockSwap(array, pos, pos + lenA, lenB, pause, mark, auxwrite);
    			pos += lenB;
    			lenB %= lenA;
    			lenA -= lenB;
    		}
    	}
    	if(backwards) {
    		if(lenB == 1) shiftBackwards(array, pos, lenA, pause, mark, auxwrite);
    		else if(lenA == 1) clamberPush(array, pos, pos + lenB, pause, mark, auxwrite);
    	} else {
    		if(lenA == 1) shiftForwards(array, pos, lenB, pause, mark, auxwrite);
    		else if(lenB == 1) clamberPush(array, pos + lenA, pos, pause, mark, auxwrite);
    	}
    }

    // simplest blockswap rotation implementable, looks like a mix between helium and beaker
    public static void simpleBeaker(int[] array, int pos, int lenA, int lenB, double pause, boolean mark, boolean auxwrite) {
    	while(lenA > 0 && lenB > 0) {
    		swapBlocksBackwards(array, pos, pos + lenB, lenA, pause, mark, auxwrite);
			lenA %= lenB;
			lenB -= lenA;
    	}
    }

    // by Scandum and Control
    public static void cycleReverse(int[] array, int pos, int lenA, int lenB, double pause, boolean mark, boolean auxwrite) {
        if (lenA < 1 || lenB < 1) return;

        int a = pos,
            b = pos + lenA - 1,
            c = pos + lenA,
            d = pos + lenA + lenB - 1;
        int swap;

        while (a < b && c < d) {
            swap = array[b];
            Writes.write(array, b--, array[a], pause/2d, mark, auxwrite);
            Writes.write(array, a++, array[c], pause/2d, mark, auxwrite);
            Writes.write(array, c++, array[d], pause/2d, mark, auxwrite);
            Writes.write(array, d--, swap,     pause/2d, mark, auxwrite);
        }
        while (a < b) {
            swap = array[b];
            Writes.write(array, b--, array[a], pause/2d, mark, auxwrite);
            Writes.write(array, a++, array[d], pause/2d, mark, auxwrite);
            Writes.write(array, d--, swap,     pause/2d, mark, auxwrite);
        }
        while (c < d) {
            swap = array[c];
            Writes.write(array, c++, array[d], pause/2d, mark, auxwrite);
            Writes.write(array, d--, array[a], pause/2d, mark, auxwrite);
            Writes.write(array, a++, swap,     pause/2d, mark, auxwrite);
        }
        if (a < d) { //dont count reversals that dont do anything
            Writes.reversal(array, a, d, pause, mark, auxwrite);
            Highlights.clearMark(2);
        }
    }

    public static void juggling(int[] array, int pos, int lenA, int lenB, double pause, boolean mark, boolean auxwrite) {
        if (lenA == 0 || lenB == 0) return;
        
        int length = lenA + lenB;
        lenA %= length;

        for (int cnt = 0,
                 index = 0,
                 value = array[pos + index],
                 startIndex = index;
            cnt < length; cnt++
        ) {
            int nextIndex = mapIndex(index, lenA, length);

            value = swap(array, pos + nextIndex, value, pause, mark, auxwrite);

            if (nextIndex == startIndex) {
                startIndex = index = mapIndex(index, 1, length);
                value = array[pos + index];
            } else {
                index = nextIndex;
            }
        }
    }
    
    public static void centered(int[] array, int pos, int lenA, int lenB, double pause, boolean mark, boolean auxwrite) {
    	if(lenA < 1 || lenB < 1)
    		return;
    	if(lenA < lenB) {
    		int h=(lenB - lenA) / 2;
    		blockSwap(array, pos, pos + lenA + h, lenA, pause, mark, auxwrite);
    		centered(array, pos + lenA + h, lenA, lenB - lenA - h, pause, mark, auxwrite);
    		centered(array, pos, lenA, h, pause, mark, auxwrite);
    	} else if(lenA == lenB) {
    		blockSwap(array, pos, pos+lenA, lenA, pause, mark, auxwrite);
    	} else {
    		int h=(lenA - lenB) / 2;
    		blockSwap(array, pos + h, pos + lenA, lenB, pause, mark, auxwrite);
    		centered(array, pos, h, lenB, pause, mark, auxwrite);
    		centered(array, pos + h + lenB, lenA - lenB - h, lenB, pause, mark, auxwrite);
    	}
    }

    //by Scandum
    public static void bridge(int[] array, int pos, int left, int right, double pause, boolean mark, boolean auxwrite) {
        if (left < 1 || right < 1) return;

        int pta = pos, ptb = pos + left, ptc = pos + right, ptd = ptb + right, alloc;

        if (left < right) {
            int bridge = right - left;

            if (bridge < left) {
                int loop = left;

                int[] swap = new int[bridge];
                alloc = bridge;
                Writes.changeAllocAmount(alloc);

                Writes.arraycopy(array, ptb, swap, 0, bridge, pause, mark, true);

                while (loop-- > 0) {
                    Writes.write(array, --ptc, array[--ptd], pause/2d, mark, auxwrite);
                    Writes.write(array,   ptd, array[--ptb], pause/2d, mark, auxwrite);
                }
                Writes.arraycopy(swap, 0, array, pta, bridge, pause, mark, auxwrite);
            } else {
                int[] swap = new int[left];
                alloc = left;
                Writes.changeAllocAmount(alloc);

                Writes.arraycopy(array, pta, swap, 0, left, pause, mark, true);
                Writes.arraycopy(array, ptb, array, pta, right, pause, mark, auxwrite);
                Writes.arraycopy(swap, 0, array, ptc, left, pause, mark, auxwrite);
            }
        } else if (right < left) {
            int bridge = left - right;

            if (bridge < right) {
                int loop = right;

                int[] swap = new int[bridge];
                alloc = bridge;
                Writes.changeAllocAmount(alloc);

                Writes.arraycopy(array, ptc, swap, 0, bridge, pause, mark, true);

                while (loop-- > 0) {
                    Writes.write(array, ptc++, array[pta],   pause/2d, mark, auxwrite);
                    Writes.write(array, pta++, array[ptb++], pause/2d, mark, auxwrite);
                }
                Writes.arraycopy(swap, 0, array, ptd - bridge, bridge, pause, mark, auxwrite);
            } else {
                int[] swap = new int[right];
                alloc = right;
                Writes.changeAllocAmount(alloc);

                Writes.arraycopy(array, ptb, swap, 0, right, pause, mark, true);
                while (left-- > 0)
                    Writes.write(array, --ptd, array[--ptb], pause, mark, auxwrite);
                Writes.arraycopy(swap, 0, array, pta, right, pause, mark, auxwrite);
            }
        } else {
            alloc = 0;

            while (left-- > 0)
                Writes.swap(array, pta++, ptb++, pause, mark, auxwrite);
            Highlights.clearMark(2);
        }
        Writes.changeAllocAmount(-alloc);
    }

    // based on the walkthrough, not the actual code, because dear lord are there a lot of helper functions
    public static void tripleShift(int[] array, int pos, int left, int right, double pause, boolean mark, boolean auxwrite) {
    	int a, m, r, b;
    	// IJKLM NO ABCDE
    	//
    	while(left - right != 0) {
    		a = pos; m = pos + Math.min(left, right); r = pos + Math.max(left, right); b = pos + left + right;
    		if(a == m) return;
    		if(a + 1 == m) {
    	        if      (right == 1) shiftBackwards(array, pos, left, pause, mark, auxwrite);
    	        else if (left == 1)  shiftForwards(array, pos, right, pause, mark, auxwrite);
    	        return;
    		}
			int mt = m, rt = r, temp;
    		if(left < right) {
    			while(a < mt) {
    				temp = array[a];
    				Writes.write(array, a++, array[m], pause, mark, auxwrite);
    				Writes.write(array, m++, array[r], pause, mark, auxwrite);
    				Writes.write(array, r++, temp, pause, mark, auxwrite);
    				if(m == rt) m = mt;
    			}
    			left = m - mt;
    			right = rt - m;
    		} else {
    			while(b > rt) {
    				temp = array[--b];
    				Writes.write(array, b, array[--r], pause, mark, auxwrite);
    				Writes.write(array, r, array[--m], pause, mark, auxwrite);
    				Writes.write(array, m, temp, pause, mark, auxwrite);
    				if(r == mt) r = rt;
    			}
    			left = r - mt;
    			right = rt - r;
    		}
			pos = mt;
    	}
    	blockSwap(array, pos, pos + left, left, pause, mark, auxwrite);
    }
}
