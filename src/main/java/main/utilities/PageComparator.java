package main.utilities;

import main.entities.Page;

import java.util.Comparator;

public class PageComparator implements Comparator<Page> {

    @Override
    public int compare(Page o1, Page o2) {
        return Double.compare(o2.getRelativeRelevance(), o1.getRelativeRelevance());
    }
}