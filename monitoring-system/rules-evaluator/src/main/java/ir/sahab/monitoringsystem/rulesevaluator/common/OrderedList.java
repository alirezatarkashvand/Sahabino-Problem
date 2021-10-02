package ir.sahab.monitoringsystem.rulesevaluator.common;

import java.util.LinkedList;
import java.util.ListIterator;

public class OrderedList<T extends Comparable<T>> extends LinkedList<T> {

    private static final long serialVersionUID = 1L;


    public void orderedAdd(T element) {
        ListIterator<T> itr = listIterator();
        while(true) {
            if (!itr.hasNext()) {
                itr.add(element);
                break;
            }

            T elementInList = itr.next();
            if (elementInList.compareTo(element) > 0) {
                itr.previous();
                itr.add(element);
                break;
            }
        }
    }
}