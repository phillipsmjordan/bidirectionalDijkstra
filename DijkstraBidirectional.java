//Jordan Phillips

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class DijkstraBidirectional {
    
    private Dijkstra dijkstraForward = null;
    private Dijkstra dijkstraBackward = null;
    
    private Set<Integer> setForward = null;
    private Set<Integer> setBackward = null;
    
    public DijkstraBidirectional(int n) {
        dijkstraForward = new Dijkstra(n);
        dijkstraBackward = new Dijkstra(n);
        
        setForward = new TreeSet<Integer>();
        setBackward = new TreeSet<Integer>();
    }

    public Set<Integer> getSetBackward() {
        return setBackward;
    }

    public Set<Integer> getSetForward() {
        return setForward;
    }
    
    public Dijkstra getDijkstraBackward() {
        return dijkstraBackward;
    }

    public Dijkstra getDijkstraForward() {
        return dijkstraForward;
    }
    
    public static void main(String [] args) throws FileNotFoundException {
        if(args.length != 1) {
            System.out.println("Please provide the file path of the *.gr file");
            return;
        }
        
        int n = 0;
        DijkstraBidirectional g = null;
        
        Scanner file = new Scanner(new FileReader(new File(args[0])));
        while(file.hasNextLine()) {
            String line = file.nextLine();
            line = line.trim();
            if(line.isEmpty() || line.startsWith("c")) {
                continue;
            }
            if(line.startsWith("p sp")) {
                String [] parts = line.split(" ");
                n = Integer.parseInt(parts[2]);
                g = new DijkstraBidirectional(n);
            } else if (line.startsWith("a")) {
                String [] parts = line.split(" ");
                g.dijkstraForward.add(
                        Integer.parseInt(parts[1]) - 1, 
                        Integer.parseInt(parts[2]) - 1, 
                        Integer.parseInt(parts[3])
                        );
                g.dijkstraBackward.add(
                        Integer.parseInt(parts[2]) - 1, 
                        Integer.parseInt(parts[1]) - 1, 
                        Integer.parseInt(parts[3])
                        );
            } else {
                System.out.println("Unknown command : " + line);
            }
        }
        
        g.getDijkstraForward().initialize(0);
        g.getDijkstraBackward().initialize(n-1);
        
        int shortestPath = Integer.MAX_VALUE;
        long startTime = System.currentTimeMillis();
        while((System.currentTimeMillis() - startTime) / 1000 < 10) {
            int node1 = g.dijkstraForward.iterate();
            int node2 = g.dijkstraBackward.iterate();
            
            
            
            if(node1 == -1 || node2 == -1) {
                continue;
            }
            g.setForward.add(node1);
            g.setBackward.add(node2);
            
            if(g.setBackward.contains(node1)) {
                //System.out.println("node1 : " + node1 + " node2 : " + node2);
                int newPath = g.dijkstraForward.getDistance()[node1] + g.dijkstraBackward.getDistance()[node1];
                if(newPath > 0)
                shortestPath = Math.min(shortestPath, newPath);
            }
            
            if(g.setForward.contains(node2)) {
                //System.out.println("node1 : " + node1 + " node2 : " + node2);
                int newPath = g.dijkstraForward.getDistance()[node2] + g.dijkstraBackward.getDistance()[node2];
                if(newPath > 0)
                shortestPath = Math.min(shortestPath, newPath);
            }
        }
        
        System.out.println("Shortest path : " + shortestPath);
        //int[] shortest = g.shortestPath();
    }
}
