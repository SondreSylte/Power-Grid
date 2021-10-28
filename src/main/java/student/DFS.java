package student;

import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;


public class DFS<E>  {


    public   HashMap<E, E> edgeTo;

    public DFS (Graph<E> G){
        edgeTo = new HashMap<>();
        for(Edge<E> v : G.edges()){ //O(N)
            edgeTo.put(v.b,v.a);
        }
    }

    public ArrayList<E> findWholePath(Graph<E> g,E n){
        E firstNode = g.vertices().iterator().next();
        ArrayList<E> pathList = new ArrayList<>();
        while(n != null){ //O(M)
            pathList.add(n);
            n = getEdgeTo(n);
        }
        pathList.add(firstNode);
        return pathList;
    }

    public E getEdgeTo(E n){ // finner noden over
        return edgeTo.get(n);
    }

}

