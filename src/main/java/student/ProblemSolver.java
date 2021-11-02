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
		PathFinder<T> pathFinder = new PathFinder<>(g);

		List <T> pathToU = pathFinder.findPath(g,u); //henter liste med sti til u fra bfs O(N)
		List <T> pathToV = pathFinder.findPath(g,v);//henter liste med sti til v fra bfs O(N)
		Collections.reverse(pathToU); //for å starte ved roten  O(N)
		Collections.reverse(pathToV); //for å starte ved roten  O(N)

		int lengthU = pathToU.size();
		int lengthV = pathToV.size();

		int counter = 0; //counter som teller antall like noder i begge listene
		if(lengthU == 1){
			return pathToU.get(counter);
		}
		if (lengthV == 1){
			return pathToU.get(counter);
		}

		try {
			while (pathToU.get(counter).equals(pathToV.get(counter))) { //O(N), worst case
				counter += 1;
			}
		} catch(IndexOutOfBoundsException ignored){}

		return pathToU.get(counter-1);
	}


	@Override
	public <T> Edge<T> addRedundant(Graph<T> g, T root) {
		HashMap<T,Integer> count = new HashMap<>();
		HashMap<T,ArrayList<T>> neighbours = new HashMap<>();

		HashSet<T> marked = new HashSet<>();
		HashSet<T> subTrees = new HashSet<>(); //inneholder to elementer. Største subtreene til roten

		ArrayList<T> leaves = new ArrayList<>();

		count(g, root, count, marked, neighbours); //O(N)

		Comparator<T> compareSize = (o1, o2) -> count.get(o2) - count.get(o1);
		PriorityQueue<T> pq = new PriorityQueue<>(compareSize);

		for (T neighbor : g.neighbours(root)){ //O(N)
			pq.add(neighbor);
		}
		subTrees.add(pq.poll()); //O(logN)

		if (g.degree(root) > 1){ //O(logN)
			subTrees.add(pq.poll());
		} else {
			leaves.add(root);
		}

		for (T rootNode : subTrees){ //O(1)
			while (g.degree(rootNode) != 1){ //O(logN)
				int i = 0;
				T newNode = null;

				for (T neighbor : neighbours.get(rootNode)){ //O(N)
					if (count.get(neighbor) > i){
						i = count.get(neighbor);
						newNode = neighbor;
					}
				}
				rootNode = newNode;
			}
			leaves.add(rootNode);
		}

		T leaf1 = leaves.remove(leaves.size()-1); //O(1)
		T leaf2 = leaves.get(leaves.size()-1); //O(1)

		return new Edge<>(leaf1,leaf2);
	}

	//O(N)
	public <T> int count(Graph<T> tree, T node, HashMap <T, Integer> count, HashSet <T> marked, HashMap <T, ArrayList<T>> neighbours ){
		int counter = 1;
		marked.add(node); //O(1)
		ArrayList<T> childrenList = new ArrayList<>();
		for(T children : tree.neighbours(node)){ //O(N)
			if(!marked.contains(children)){ //O(1)
				childrenList.add(children); //O(1)
				counter += count(tree, children, count, marked, neighbours);
			}
		}
		neighbours.put(node, childrenList); //O(1)
		count.put(node, counter); //O(1)
		return counter;
	}
}
