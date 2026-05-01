package io.github.arrayv.utils;

public class Range {
    public int start;
    public int end;

    public Range(int start1, int end1) {
        this.start = start1;
        this.end = end1;
    }

    public Range() {
        this.start = 0;
        this.end = 0;
    }

    public void set(int start1, int end1) {
        this.start = start1;
        this.end = end1;
    }

    public int length() {
        return this.end - this.start;
    }
}