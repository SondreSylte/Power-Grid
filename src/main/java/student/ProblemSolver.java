package student;

import java.util.*;

import graph.*;

public class ProblemSolver implements IProblem {

	@Override
	public <T, E extends Comparable<E>> ArrayList<Edge<T>> mst(WeightedGraph<T, E> g) {

		ArrayList<Edge<T>> cheapestEdge = new ArrayList<>(g.numVertices());
		if (g.numVertices() < 1){
			return cheapestEdge;
		}

		HashSet<T> marked = new HashSet<>(g.numVertices());
		PriorityQueue<Edge<T>> priEdges = new PriorityQueue<>(g);

		T start = g.vertices().iterator().next();
		for(Edge<T> e : g.adjacentEdges(start)){
			priEdges.add(e);
		}
		marked.add(start);

		while (!priEdges.isEmpty() && cheapestEdge.size()-1 < g.size()){
			Edge<T> e = priEdges.remove();
			T v = e.a;
			T w = e.b;
			assert marked.contains(v) || marked.contains(w);

			if (!marked.contains(v)){
				marked.add(v);
				cheapestEdge.add(e);
				for (Edge<T> edge: g.adjacentEdges(v)){
					if (!marked.contains(edge.other(v))){
						priEdges.add(edge);
					}
				}
			}

			if (!marked.contains(w)){
				marked.add(w);
				cheapestEdge.add(e);
				for (Edge<T> edge: g.adjacentEdges(w)){
					if (!marked.contains(edge.other(w))){
						priEdges.add(edge);
					}
				}
			}
		}
		System.out.println(cheapestEdge);
		return cheapestEdge;
	}


	@Override 
	public <T> T lca(Graph<T> g, T root, T u, T v) {

		DFS<T> dfs = new DFS<>(g);

		List <T> pathToU = dfs.findWholePath(g,u); //henter liste med sti til u fra bfs O(N)
		List <T> pathToV = dfs.findWholePath(g,v);//henter liste med sti til v fra bfs O(N)

		Collections.reverse(pathToU); //for å starte ved roten  O(N)
		Collections.reverse(pathToV); //for å starte ved roten  O(N)

		int lengthU = pathToU.size();
		int lengthV = pathToV.size();

		int index = 0; //counter som teller antall like noder i begge listene
		if(lengthU == 1 || lengthV == 1){
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
