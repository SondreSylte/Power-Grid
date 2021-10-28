package student;

import java.lang.reflect.Array;
import java.util.*;
import java.util.jar.JarOutputStream;
import student.DFS;

import graph.*;
import org.w3c.dom.Node;

public class ProblemSolver implements IProblem {

	@Override
	public <T, E extends Comparable<E>> ArrayList<Edge<T>> mst(WeightedGraph<T, E> g) {

		ArrayList<Edge<T>> cheapestEdge = new ArrayList<>(g.numVertices());
		if (g.numVertices() < 1){
			return cheapestEdge;
		}

		HashSet<T> marked = new HashSet<>(g.numVertices());
		HashMap<T,Edge<T>> bestEdge = new HashMap<>(g.numVertices());
		PriorityQueue<Edge<T>> priEdges = new PriorityQueue<>(g);


/*
		T start = g.vertices().iterator().next();
		for(Edge<T> e : g.adjacentEdges(start)){
			priEdges.add(e);
		}
		marked.add(start);
*/
		while (!priEdges.isEmpty()){
			Edge<T> e = priEdges.remove();
			T v = e.a;
			T w = e.b;
			assert marked.contains(v) || marked.contains(w);
			if (marked.contains(v) && marked.contains(w)){
				continue;
			}

			System.out.println("hello");

			if (!marked.contains(v)){
				visit(g,v,marked,bestEdge,priEdges);
				System.out.println("hello2");
			}

			if (!marked.contains(w)){
				visit(g,v,marked,bestEdge,priEdges);
				System.out.println("hello3");
			}
			cheapestEdge.add(e);


		}
		System.out.println(cheapestEdge);
		return cheapestEdge;
	}

	private <E extends Comparable<E>, T> void visit(WeightedGraph<T,E> g, T v, HashSet<T> marked, HashMap<T,
			Edge<T>> bestEdge, PriorityQueue<Edge<T>> priorityEdges) {

		marked.add(v);
		for (Edge<T> e : g.adjacentEdges(v)) {
			T other = e.other(v);
			if (marked.contains(other)) {
				continue;
			}
			Edge<T> best = bestEdge.get(other);
			if (best == null) {
				bestEdge.put(other, e);
				priorityEdges.add(e);
			} else if (g.compare(best, e) > 0) {
				bestEdge.replace(other, e);
				priorityEdges.remove(best);
				priorityEdges.add(e);
			}
		}
	}



	@Override 
	public <T> T lca(Graph<T> g, T root, T u, T v) {

		DFS<T> dfs = new DFS<T>(g);

		List <T> pathToU = dfs.findWholePath(g,u); //henter liste med sti til u fra bfs O(N)
		List <T> pathToV = dfs.findWholePath(g,v);//henter liste med sti til v fra bfs O(N)

		Collections.reverse(pathToU); //for å starte ved roten  O(N)
		Collections.reverse(pathToV); //for å starte ved roten  O(N)

		int Ulength = pathToU.size();
		int Vlength = pathToV.size();

		int index = 0; //counter som teller antall like noder i begge listene
		if(Ulength == 1 || Vlength == 1){
			return pathToU.get(index);
		}
		try {
			while (pathToU.get(index).equals(pathToV.get(index))) { //O(N), worst case
				index++;
			}
		} catch(IndexOutOfBoundsException e){}


		return pathToU.get(index-1);
	}


	@Override
	public <T> Edge<T> addRedundant(Graph<T> g, T root) {
		// Task 3
		// TODO implement method
		return null;
	}
}
