package student;

import graph.Edge;
import graph.Graph;

import java.util.*;

public class BFS {

    /**
     * Bfs er bredde-første-søk og starter på rooten før den undersøker alle noder gjennom grafen.
     * Bfs har i utgangspunktet kjøretid 2*O(m) = O(m), fordi du bare besøker hver node 2 ganger.
     * Summen av alle degrees i treet er 2n-2, som vil si at det å gå gjennom alle degrees er O(m).
     * Derfor konkluderer jeg med at kjøretiden til BFS er O(m).
     * @param g
     * @param root
     * @param <V>
     * @return parents
     */

    public static <V> HashMap<V,V> parents(Graph<V> g, V root){ //O(m)
        HashSet<V> found = new HashSet<V>();
        LinkedList<Edge<V>> toSearch = new LinkedList<>();
        HashMap<V,V> parents = new HashMap<>();

        parents.put(root, null); //O(1)

        update(g, found, toSearch, root);

        while(!toSearch.isEmpty()) { //O(m)
            Edge<V> e = toSearch.removeFirst();
            V foundNode = getFoundNode(e,found);
            V newNode = e.other(foundNode);
            if(found.contains(newNode)) { //O(1)
                continue;
            }

            //compute distance for newNode
            parents.put(newNode, foundNode); //O(1)

            //update newNode
            update(g, found, toSearch, newNode);  	//happens O(n) times takes O(degree) is O(m)
        }

        return parents;
    }


    private static <V> void update(Graph<V> g, HashSet<V> found, LinkedList<Edge<V>> toSearch, V newNode) {
        found.add(newNode);
        for(Edge<V> edge : g.adjacentEdges(newNode)) {
            if(found.contains(edge.a) && found.contains(edge.b)) {
                continue;
            }
            toSearch.addLast(edge);
        }
    }

    private static <V> V getFoundNode(Edge<V> e, HashSet<V> found) {
        if(found.contains(e.a))
            return e.a;
        if(found.contains(e.b))
            return e.b;

        throw new IllegalArgumentException("e should always have one endpoint in found");
    }


}

