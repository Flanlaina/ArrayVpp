package io.github.arrayv.sortdata;

import java.util.Comparator;

public final class VisualComparator implements Comparator<VisualInfo> {
    public VisualComparator() {}

    @Override
    public int compare(VisualInfo left, VisualInfo right) {
        return left.getListName().compareTo(right.getListName());
    }
}
