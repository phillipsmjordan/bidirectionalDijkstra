
public class FHeapNode {

    int key, val, degree;
    FHeapNode parent, right, left, child;
    boolean cut;

    public void print() {
        System.out.print("[" + key + "]");
        if (child == null) {
            return;
        }
        System.out.print("(");
        FHeapNode scan = child;
        do {
            scan.print();
        } while ((scan = scan.right) != child);
        System.out.print(")");
    }
}