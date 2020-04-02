package com.google.maps.android.quadtree;

import com.google.maps.android.geometry.Bounds;
import com.google.maps.android.geometry.Point;
import com.google.maps.android.quadtree.PointQuadTree.Item;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PointQuadTree<T extends Item> {
    private static final int MAX_DEPTH = 40;
    private static final int MAX_ELEMENTS = 50;
    private final Bounds mBounds;
    private List<PointQuadTree<T>> mChildren;
    private final int mDepth;
    private List<T> mItems;

    public interface Item {
        Point getPoint();
    }

    public PointQuadTree(double d, double d2, double d3, double d4) {
        Bounds bounds = new Bounds(d, d2, d3, d4);
        this(bounds);
    }

    public PointQuadTree(Bounds bounds) {
        this(bounds, 0);
    }

    private PointQuadTree(double d, double d2, double d3, double d4, int i) {
        Bounds bounds = new Bounds(d, d2, d3, d4);
        this(bounds, i);
    }

    private PointQuadTree(Bounds bounds, int i) {
        this.mChildren = null;
        this.mBounds = bounds;
        this.mDepth = i;
    }

    public void add(T t) {
        Point point = t.getPoint();
        if (this.mBounds.contains(point.f58x, point.f59y)) {
            insert(point.f58x, point.f59y, t);
        }
    }

    private void insert(double d, double d2, T t) {
        if (this.mChildren != null) {
            if (d2 < this.mBounds.midY) {
                if (d < this.mBounds.midX) {
                    ((PointQuadTree) this.mChildren.get(0)).insert(d, d2, t);
                } else {
                    ((PointQuadTree) this.mChildren.get(1)).insert(d, d2, t);
                }
            } else if (d < this.mBounds.midX) {
                ((PointQuadTree) this.mChildren.get(2)).insert(d, d2, t);
            } else {
                ((PointQuadTree) this.mChildren.get(3)).insert(d, d2, t);
            }
            return;
        }
        if (this.mItems == null) {
            this.mItems = new ArrayList();
        }
        this.mItems.add(t);
        if (this.mItems.size() > 50 && this.mDepth < 40) {
            split();
        }
    }

    private void split() {
        this.mChildren = new ArrayList(4);
        List<PointQuadTree<T>> list = this.mChildren;
        PointQuadTree pointQuadTree = new PointQuadTree(this.mBounds.minX, this.mBounds.midX, this.mBounds.minY, this.mBounds.midY, this.mDepth + 1);
        list.add(pointQuadTree);
        List<PointQuadTree<T>> list2 = this.mChildren;
        PointQuadTree pointQuadTree2 = new PointQuadTree(this.mBounds.midX, this.mBounds.maxX, this.mBounds.minY, this.mBounds.midY, this.mDepth + 1);
        list2.add(pointQuadTree2);
        List<PointQuadTree<T>> list3 = this.mChildren;
        PointQuadTree pointQuadTree3 = new PointQuadTree(this.mBounds.minX, this.mBounds.midX, this.mBounds.midY, this.mBounds.maxY, this.mDepth + 1);
        list3.add(pointQuadTree3);
        List<PointQuadTree<T>> list4 = this.mChildren;
        PointQuadTree pointQuadTree4 = new PointQuadTree(this.mBounds.midX, this.mBounds.maxX, this.mBounds.midY, this.mBounds.maxY, this.mDepth + 1);
        list4.add(pointQuadTree4);
        List<T> list5 = this.mItems;
        this.mItems = null;
        for (T t : list5) {
            insert(t.getPoint().f58x, t.getPoint().f59y, t);
        }
    }

    public boolean remove(T t) {
        Point point = t.getPoint();
        if (!this.mBounds.contains(point.f58x, point.f59y)) {
            return false;
        }
        return remove(point.f58x, point.f59y, t);
    }

    private boolean remove(double d, double d2, T t) {
        if (this.mChildren == null) {
            List<T> list = this.mItems;
            if (list == null) {
                return false;
            }
            return list.remove(t);
        } else if (d2 < this.mBounds.midY) {
            if (d < this.mBounds.midX) {
                return ((PointQuadTree) this.mChildren.get(0)).remove(d, d2, t);
            }
            return ((PointQuadTree) this.mChildren.get(1)).remove(d, d2, t);
        } else if (d < this.mBounds.midX) {
            return ((PointQuadTree) this.mChildren.get(2)).remove(d, d2, t);
        } else {
            return ((PointQuadTree) this.mChildren.get(3)).remove(d, d2, t);
        }
    }

    public void clear() {
        this.mChildren = null;
        List<T> list = this.mItems;
        if (list != null) {
            list.clear();
        }
    }

    public Collection<T> search(Bounds bounds) {
        ArrayList arrayList = new ArrayList();
        search(bounds, arrayList);
        return arrayList;
    }

    private void search(Bounds bounds, Collection<T> collection) {
        if (this.mBounds.intersects(bounds)) {
            List<PointQuadTree<T>> list = this.mChildren;
            if (list != null) {
                for (PointQuadTree search : list) {
                    search.search(bounds, collection);
                }
            } else if (this.mItems != null) {
                if (bounds.contains(this.mBounds)) {
                    collection.addAll(this.mItems);
                } else {
                    for (T t : this.mItems) {
                        if (bounds.contains(t.getPoint())) {
                            collection.add(t);
                        }
                    }
                }
            }
        }
    }
}
