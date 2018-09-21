package ru.ifmo.rain.elmanov.arrayset;

import java.util.AbstractList;
import java.util.List;

public class ReverseListView<E> extends AbstractList<E> {
    List<E> view;

    public ReverseListView(List<E> list) {
        view = list;
    }

    @Override
    public int size() {
        return view.size();
    }

    @Override
    public E get(int index) {
        return view.get(view.size() - 1 - index);
    }
}
