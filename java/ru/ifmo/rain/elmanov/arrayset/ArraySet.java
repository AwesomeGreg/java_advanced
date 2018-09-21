package ru.ifmo.rain.elmanov.arrayset;

import java.util.*;

public class ArraySet<E> extends AbstractSet<E> implements NavigableSet<E> {
    private List<E> elements;
    private Comparator<? super E> comparator;

    public ArraySet() {
        this(Collections.emptyList(), null);
    }

    public ArraySet(Collection<? extends E> collection) {
        this(collection, null);
    }

    public ArraySet(Comparator<? super E> comparator) {
        this(Collections.emptyList(), comparator);
    }

    public ArraySet(Set<E> set) {
        this(set, null);
    }

    private ArraySet(List<E> listView, Comparator<? super E> comparator) {
        elements = listView;
        this.comparator = comparator;
    }

    public ArraySet(Collection<? extends  E> collection, Comparator<? super E> comparator) {
        this.comparator = comparator;
        if (collection.isEmpty()) {
            elements = Collections.emptyList();
        } else {
            ArrayList<? extends  E> newList = new ArrayList(collection);
            newList.sort(this.comparator);
            Iterator<? extends  E> newIt = newList.iterator();
            elements = new ArrayList();
            elements.add(newIt.next());
            while (newIt.hasNext()) {
                E el = newIt.next();
                if (compare(el, elements.get(elements.size()-1)) != 0) {elements.add(el);}
            }
        }
    }

    private int compare(E e1, E e2) {
        if (comparator != null) {
            return comparator.compare(e1, e2);
        }
        if (e1.equals(e2)) {
            return 0;
        } else {
            List<E> pair =  Arrays.asList(e1, e2);
            pair.sort(null);
            if (pair.get(0) == e1) {
                return -1;
            } else return 1;
        }
    }

    @SuppressWarnings("UncheckedCast")
    @Override
    public boolean contains(Object o) {
        return (Collections.binarySearch(elements, (E)o,comparator) >= 0);
    }

    /* ~~~~~~~~~~~~~~~ life simplifiers ~~~~~~~~~~~~~~~ */
    private int binarySearch(E e) {
        return Collections.binarySearch(elements, e, comparator);
    }

    private int ceilingIndex(E e) {
        int index = binarySearch(e);
        return index >= 0 ? index : (-index - 1);
    }

    private int floorIndex(E e) {
        int index = binarySearch(e);
        return index >= 0 ? index : (-index - 1) - 1;
    }

    private int higherIndex(E e) {
        int index = binarySearch(e);
        return index >= 0 ? index + 1 : (-index - 1);
    }

    private int lowerIndex(E e) {
        int index = binarySearch(e);
        return index >= 0 ? index - 1 : (-index - 1) - 1;
    }
    /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Override
    public E lower(E e) {
        int index = lowerIndex(e);
        if (index == -1) {return null;}
        return elements.get(index);
    }

    @Override
    public E floor(E e) {
        int index = floorIndex(e);
        if (index == -1) {return null;}
        return elements.get(index);
    }

    @Override
    public E ceiling(E e) {
        int index = ceilingIndex(e);
        if (index == elements.size()) {return null;}
        return elements.get(index);
    }

    @Override
    public E higher(E e) {
        int index = higherIndex(e);
        if (index == elements.size()) {return null;}
        return elements.get(index);
    }

    @Override
    public E pollFirst() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E pollLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        return Collections.unmodifiableCollection(elements).iterator();
    }

    @Override
    public NavigableSet<E> descendingSet() {
        return new ArraySet<>(new ReverseListView<>(elements), Collections.reverseOrder(comparator));
    }

    @Override
    public Iterator<E> descendingIterator() {
        return descendingSet().iterator();
    }

    @Override
    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
        if (isEmpty() || compare(fromElement, toElement) > 0 || (compare(fromElement, toElement) == 0 && (!fromInclusive || !toInclusive))) {
            return new ArraySet<E>(Collections.emptyList(), comparator);
        }
        int from;
        if (fromInclusive) {from = ceilingIndex(fromElement);}
        else  {from = higherIndex(fromElement);}
        int to;
        if (toInclusive) {to = floorIndex(toElement);}
        else {to = lowerIndex(toElement);}
        return new ArraySet<>(elements.subList(from, to + 1), comparator);
    }

    @Override
    public NavigableSet<E> headSet(E toElement, boolean inclusive) {
        if(isEmpty())
            return new ArraySet<E>(Collections.emptyList(), comparator);
        return subSet(first(), true, toElement, inclusive);
    }

    @Override
    public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
        if (isEmpty())
            return new ArraySet<E>(Collections.emptyList(), comparator);
        return subSet(fromElement, inclusive, last(), true);
    }

    @Override
    public Comparator<? super E> comparator() {
        return comparator;
    }

    @Override
    public NavigableSet<E> subSet(E fromElement, E toElement) {
        return subSet(fromElement, true, toElement, false);
    }

    @Override
    public NavigableSet<E> headSet(E toElement) {
        if(isEmpty()) {return this;}
        return subSet(first(), true, toElement, false);
    }

    @Override
    public NavigableSet<E> tailSet(E fromElement) {
        if (isEmpty())
            return new ArraySet<E>(Collections.emptyList(), comparator);
        return subSet(fromElement, true, last(), true);
    }

    @Override
    public E first() {
        if (elements.isEmpty()) {throw new NoSuchElementException();}
        return elements.get(0);
    }

    @Override
    public E last() {
        if (elements.isEmpty()) {throw new NoSuchElementException();}
        return elements.get(elements.size()-1);
    }

    @Override
    public int size() {
        return elements.size();
    }
}
