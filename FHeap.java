
public class FHeap {

    private FHeapNode root;
    private FHeapNode[] tree = new FHeapNode[32];

    private void insert(FHeapNode x) { // add to root ring
        x.parent = null;
        if (root == null) {
            root = x.left = x.right = x;
            return;
        }
        // add x to root ring
        x.left = root.left;
        x.right = root;
        root.left.right = x;
        root.left = x;
        if (root.key > x.key) { // point to minimun node
            root = x;
        }
    }

    public synchronized FHeapNode insert(int key, int val) {
        FHeapNode tmp = new FHeapNode();
        tmp.key = key;
        tmp.val = val;
        insert(tmp);
        return tmp;
    }

    public synchronized void decrease(FHeapNode tmp, int newKey) {
        FHeapNode parent = tmp.parent;
        tmp.key = newKey;
        if (parent == null) { // tmp is a root
            if (root.key > tmp.key) {
                root = tmp;
            }
            return;
        }
        if (tmp.key < parent.key) { // cut off
            remove(tmp);
            insert(tmp);
            for (tmp = parent, parent = tmp.parent; tmp.cut == true && parent != null; tmp = parent, parent = parent.parent) {
                remove(tmp);
                insert(tmp);
            }
            tmp.cut = true;
        }
    }
    // remove from parent and sibling ring

    private void remove(FHeapNode x) {
        FHeapNode parent = x.parent;
        if (x.right == x) {
            parent.child = null;
        } else {
            // remove x from sibling ring
            x.left.right = x.right;
            x.right.left = x.left;
            if (parent.child == x) {
                parent.child = x.right;
            }
        }
        parent.degree--;
    }

    public synchronized void combine(FHeap fp) {
        FHeapNode x = fp.root;
        FHeapNode xLeft = x.left;
        FHeapNode rootLeft = root.left;
        // xleft <--> x           xleft      x
        //                                X
        // rootleft <--> root    rootleft    root
        x.left = rootLeft;
        root.left = xLeft;
        xLeft.right = root;
        rootLeft.right = x;
        if (x.key < root.key) {
            root = x;
        }
        fp.root = null;
    }

    public synchronized void delete(FHeapNode x) {
        if (root == x) {
            deleteMin();
            return;
        }
        FHeapNode scan, pre;
        // move children of x to root
        if ((scan = x.child) != null) {
            do {
                pre = scan;
                scan = scan.right;
                insert(pre);
            } while (scan != x.child);
        }
        // delete x from parent
        FHeapNode parent = x.parent;
        if (parent != null) {
            if (parent.child == x) {
                if (parent.degree == 1) {
                    parent.child = null;
                } else {
                    parent.child = x.right;
                }
            }
            parent.degree--;
            FHeapNode tmp;
            for (tmp = parent, parent = tmp.parent; tmp.cut == true && parent != null; tmp = parent, parent = parent.parent) {
                remove(tmp);
                insert(tmp);
            }
            tmp.cut = true;
        }
        // remove x from sibling ring
        x.left.right = x.right;
        x.right.left = x.left;
    }

    public synchronized int deleteMin() {
        if (root == null) {
            return -1;
        }
        int x = root.val;
        FHeapNode scan, pre;
        // join children of root
        if ((scan = root.child) != null) {
            do {
                pre = scan;
                scan = scan.right;
                join(pre);
            } while (scan != root.child);
        }
        // join all trees except root
        for (scan = root.right; scan != root;) {
            pre = scan;
            scan = scan.right;
            join(pre);
        }
        root = null;
        // link all trees
        for (int i = 0; i < tree.length; i++) {
            if (tree[i] != null) {
                insert(tree[i]);
                tree[i] = null;
            }
        }
        return x;
    }

    private void join(FHeapNode tmp) {
        while (tree[tmp.degree] != null) {
            // join newRoot & newChild
            FHeapNode newRoot, newChild;
            if (tree[tmp.degree].key < tmp.key) {
                newRoot = tree[tmp.degree];
                newChild = tmp;
            } else {
                newRoot = tmp;
                newChild = tree[tmp.degree];
            }
            newChild.parent = newRoot;
            newChild.cut = false;
            if (newRoot.child == null) {
                newRoot.child = newChild.left = newChild.right = newChild;
            } else {
                // add newChild to new ring
                newChild.left = newRoot.child.left;
                newChild.right = newRoot.child;
                newRoot.child.left.right = newChild;
                newRoot.child.left = newChild;
            }
            tmp = newRoot;
            tree[tmp.degree++] = null;
        }
        tree[tmp.degree] = tmp;
    }

    public static void main(String[] argv) {
        // generate a testing case for 10 nodes
        FHeap fp = new FHeap();
        FHeapNode[] list = new FHeapNode[8];
        for (int i = 0; i < list.length; i++) { // add 10 elements
            list[i] = fp.insert(i, i);
        }
        FHeap fp2 = new FHeap();
        fp2.insert(8, 8);
        fp2.insert(9, 9);
        System.out.println("fp1");
        fp.print();
        System.out.println("fp2");
        fp2.print();
        System.out.println("after combine");
        fp.combine(fp2);
        fp.print();
        System.out.println("after delete min");
        fp.deleteMin();
        fp.print();
        System.out.println("after delete 6");
        fp.delete(list[6]);
        fp.print();
        System.out.println("after delete 7");
        fp.delete(list[7]);
        fp.print();
    }

    public void print() {
        if (root == null) {
            return;
        }
        FHeapNode scan = root;
        do {
            scan.print();
            System.out.println();
            scan = scan.right;
        } while (scan != root);
    }
}