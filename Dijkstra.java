


public class Dijkstra {

    class Node {

        int vertex;
        int cost;
        Node next;
    }
    int n; // number of vertex in this graph
    Node[] edge; // adjacent list for all vertex

    public Dijkstra(int vertex) {
        n = vertex;
        edge = new Node[n];
    }

    public void add(int x, int y, int cost) {
        // add node to adjacent list
        Node tt = new Node();
        tt.vertex = y;
        tt.cost = cost;
        tt.next = edge[x];
        edge[x] = tt;
    }

    private int[] distance;
    private FHeapNode[] fnode;
    private FHeap heap;

    public int[] getDistance() {
        return distance;
    }
    
    public void initialize(int startNode) {
        distance = new int[n];
        fnode = new FHeapNode[n];
        for (int i = 0; i < n; i++) {
            if(startNode != i)
                distance[i] = Integer.MAX_VALUE;
            else
                distance[i] = 0;
        }
        heap = new FHeap();
        fnode[startNode] = heap.insert(0, startNode);
    }
    
    public int iterate() {
        int w, newLen; 
        if ((w = heap.deleteMin()) >= 0) {
            fnode[w] = null; // release memory
            for (Node tmp = edge[w]; tmp != null; tmp = tmp.next) {
                if ((newLen = distance[w] + tmp.cost) < distance[tmp.vertex]) {
                    distance[tmp.vertex] = newLen;
                    if (fnode[tmp.vertex] == null) {
                        fnode[tmp.vertex] = heap.insert(newLen, tmp.vertex);
                    } else {
                        heap.decrease(fnode[tmp.vertex], newLen);
                    }
                }
            }
            edge[w] = null; // release memory
        }
        return w;
    }
}