package student;

import java.util.*;

import graph.*;

public class ProblemSolver implements IProblem {

	/**
	 * Siden dette er et MST, kan vi anta at n > m
	 * @param g - The graph of possible power connections the power company can build
	 * @param <T>
	 * @param <E>
	 * @return en liste med de korteste kantene å gå.
	 */
	@Override
	public <T, E extends Comparable<E>> ArrayList<Edge<T>> mst(WeightedGraph<T, E> g) { //O(n) + O(m log m) + O(n*m log m) = O(m log m)

		ArrayList<Edge<T>> shortestEdge = new ArrayList<>(g.numVertices());
		if (g.numVertices() < 1){
			return shortestEdge;
		}

		HashSet<T> marked = new HashSet<>(g.numVertices());
		PriorityQueue<Edge<T>> priEdges = new PriorityQueue<>(g);

		T start = g.vertices().iterator().next(); //O(1)
		for(Edge<T> e : g.adjacentEdges(start)){  //O(m) * O(log n) = O(m log n)
			priEdges.add(e); //O(log n)
		}
		marked.add(start); //O(1)

		while (!priEdges.isEmpty() && shortestEdge.size()-1 < g.size()){ //O(n) * O(m log m) = O(n*m log m)
			Edge<T> e = priEdges.remove(); //O(log m)
			T v = e.a;
			T w = e.b;
			assert marked.contains(v) || marked.contains(w); //O(1)

			if (!marked.contains(v)){ //O(1)
				marked.add(v); //(1)
				shortestEdge.add(e); //O(1)
				for (Edge<T> edge: g.adjacentEdges(v)){ //O(m) * O(log m) = O(m log m)
					if (!marked.contains(edge.other(v))){ //O(1)
						priEdges.add(edge); //O(log m)
					}
				}
			}

			if (!marked.contains(w)){
				marked.add(w);
				shortestEdge.add(e);
				for (Edge<T> edge: g.adjacentEdges(w)){
					if (!marked.contains(edge.other(w))){
						priEdges.add(edge);
					}
				}
			}
		}
		System.out.println(shortestEdge);
		return shortestEdge;
	}





	/**
	 * @param g - The tree of power lines built by the power company.
	 * @param root - The power station.
	 * @param u - u and v are the two points with no power.
	 * @param v - u and v are the two points with no power.
	 * @param <T>
	 * @return en node som er felles common ancestor for to gitte noder
	 */
	@Override 
	public <T> T lca(Graph<T> g, T root, T u, T v) {
		new BFS();

		HashMap<T,T> childToParent = BFS.parents(g,root); //
		ArrayList<T> pathToU = findPath(u,childToParent); //O(N)
		ArrayList<T> pathToV = findPath(v,childToParent); //O(N)

		HashSet<T> PathUSet = new HashSet<>(pathToU);
		for (T node : pathToV){ //O(N)
			if (PathUSet.contains(node)){ //O(1)
				return node;
			}

		}
		throw new IllegalArgumentException("No lca found");
	}

	private <T> ArrayList<T> findPath(T parent, HashMap<T,T> childToParent){
		ArrayList<T> path = new ArrayList<>();
		while (parent != null){ //O(N)
			path.add(parent);
			parent = childToParent.get(parent);
		}
		return path;
	}

	/**
	 * @param g
	 * @param root
	 * @param <T>
	 * @return en edge mellom to løv som skal connecte de to subtrærne.
	 */
	@Override
	public <T> Edge<T> addRedundant(Graph<T> g, T root) { //O(N) + O(log N) = O(N)
		HashMap<T,Integer> count = new HashMap<>();
		HashMap<T,ArrayList<T>> neighbours = new HashMap<>();

		HashSet<T> marked = new HashSet<>();
		HashSet<T> subTrees = new HashSet<>(); //inneholder to elementer. Største subtreene til roten

		ArrayList<T> leaves = new ArrayList<>();
		ArrayList<T> neighbors = new ArrayList<>();

		countDepth(g, root, count, marked, neighbours); //O(N)

		Comparator<T> compareSize = Comparator.comparingInt(count::get); //O(1)

		for (T neighbour : g.neighbours(root)){ //O(N)
			neighbors.add(neighbour); //O(1)
		}
		T biggest = Collections.max(neighbors,compareSize); //O(1)
		subTrees.add(biggest); //O(1)
		neighbors.remove(biggest); //O(1)

		if (g.degree(root) > 1){ //O(logN)
			subTrees.add(Collections.max(neighbors,compareSize));
		} else {
			leaves.add(root); //O(1)
		}

		//Koden går alltid lenger og lenger ned i treet og aldri i samme node mer enn en gang.
		//Derfor blir kjøretiden på foor-loopen under O(N).
		for (T rootNode : subTrees){
			while (g.degree(rootNode) != 1){
				int i = 0;
				T newNode = null;

				for (T neighbor : neighbours.get(rootNode)){
					if (count.get(neighbor) > i){
						i = count.get(neighbor);
						newNode = neighbor;
					}
				}
				rootNode = newNode;
			}
			leaves.add(rootNode); //O(1)
		}

		T leaf1 = leaves.remove(leaves.size()-1); //O(1)
		T leaf2 = leaves.get(leaves.size()-1); //O(1)

		return new Edge<>(leaf1,leaf2);
	}

	//O(N)
	public <T> int countDepth(Graph<T> g, T node, HashMap <T, Integer> count, HashSet <T> marked, HashMap <T, ArrayList<T>> neighbours ){
		int counter = 1;
		marked.add(node); //O(1)
		ArrayList<T> childrenList = new ArrayList<>();
		for(T children : g.neighbours(node)){ //O(N)
			if(!marked.contains(children)){ //O(1)
				childrenList.add(children); //O(1)
				counter += countDepth(g, children, count, marked, neighbours);
			}
		}
		neighbours.put(node, childrenList); //O(1)
		count.put(node, counter); //O(1)
		return counter;
	}
}
