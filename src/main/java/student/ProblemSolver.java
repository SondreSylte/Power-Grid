package student;

import java.util.*;

import graph.*;

public class ProblemSolver<T> implements IProblem {



	/**
	 * mst metoden kommer i alle steg til å velge den korteste kanten for å komme fra en node til en annen.
	 * Har en liste som alltid holder de korteste kantene.
	 * Har en priority queue som prioriterer hvem av kantene til noden som er kortest
	 * Marked listed "markerer" noder som er funnet
	 *
	 * @param g - The graph of possible power connections the power company can build
	 * @param <T>
	 * @param <E>
	 * @return en liste med de korteste kantene å gå.
	 */
	@Override
	public <T, E extends Comparable<E>> ArrayList<Edge<T>> mst(WeightedGraph<T, E> g) { //O(n) + O(m log m) + O(m log m) = O(m log m)

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

		while (!priEdges.isEmpty() && shortestEdge.size()-1 < g.size()){ //O(m) * O(log m) = O(m log m)
			Edge<T> e = priEdges.poll(); //O(log m)

			if(marked.contains(e.a) && marked.contains(e.b) ) continue; //O(1)
			shortestEdge.add(e); //O(1)

			findNodeNotInMarked(g, e.a, marked, priEdges); //O(log m)
			findNodeNotInMarked(g, e.b, marked, priEdges); //O(log m)
		}
		return shortestEdge;
	}

	/**
	 * Marked vil holde styr på hvilken av nodene som er besøkt og ikke. Denne metoden vil sjekke om noden er
	 * besøkt. Om den ikke er det, kan den legge til noden sine kanter i pq.
	 * Markerer videre noden som besøkt.
	 * @param g
	 * @param node
	 * @param marked - listen med de markerte nodene.
	 * @param priEdges - vil addere kantene i denne listen
	 * @param <E>
	 * @param <T>
	 */
	//O(degree) * O(log m) = O(log m)
	private <E extends Comparable<E>,T> void findNodeNotInMarked(WeightedGraph<T, E> g, T node, HashSet<T> marked, PriorityQueue<Edge<T>> priEdges){
		if (!marked.contains(node)){ //O(1)
			marked.add(node); //O(1)
			for (Edge<T> edge: g.adjacentEdges(node)) //O(degree) til edge
					priEdges.add(edge); //O(log m)
			}
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
	public <T> T lca(Graph<T> g, T root, T u, T v) { //O(n)
		new BFS();

		HashMap<T,T> childToParent = BFS.parents(g,root);
		ArrayList<T> pathToU = findPath(u,childToParent); //O(n)
		ArrayList<T> pathToV = findPath(v,childToParent); //O(n)

		HashSet<T> PathUSet = new HashSet<>(pathToU);
		for (T node : pathToV){ //O(n)
			if (PathUSet.contains(node)){ //O(1)
				return node;
			}

		}
		throw new IllegalArgumentException("No lca found");
	}

	/**
	 * Denne metoden lager en path fra en foreldrenode til nodens barn.
	 * En arrayliste vil holde styr på pathen. Her blir det lagt til
	 * @param parent
	 * @param childToParent
	 * @param <T>
	 * @return
	 */
	private <T> ArrayList<T> findPath(T parent, HashMap<T,T> childToParent){
		ArrayList<T> path = new ArrayList<>();
		while (parent != null){
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
	public <T> Edge<T> addRedundant(Graph<T> g, T root) {
		HashMap<T,Integer> count = new HashMap<>();
		HashMap<T,ArrayList<T>> neighbours = new HashMap<>();

		HashSet<T> marked = new HashSet<>();
		HashSet<T> subTrees = new HashSet<>();

		ArrayList<T> leaves = new ArrayList<>();
		ArrayList<T> nodes = new ArrayList<>();

		count(g, root, count, marked, neighbours);

		Comparator<T> compareSize = Comparator.comparingInt(count::get);

		for (T n : g.neighbours(root)){
			nodes.add(n);
		}
		T nodeWithBiggestSubTree = Collections.max(nodes,compareSize);
		subTrees.add(nodeWithBiggestSubTree);
		nodes.remove(nodeWithBiggestSubTree);

		if (g.degree(root) > 1){
			subTrees.add(Collections.max(nodes,compareSize));
		} else {
			leaves.add(root);
		}

		//Koden går alltid lenger og lenger ned i treet og aldri i samme node mer enn en gang.
		//Derfor blir kjøretiden på for-loopen under O(n).
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
			leaves.add(rootNode);
		}

		T leaf1 = leaves.remove(leaves.size()-1);
		T leaf2 = leaves.get(leaves.size()-1);

		return new Edge<>(leaf1,leaf2);
	}

	/**
	 * Denne hjelpemetoden teller nodens barn. Den vil legge til noden og antall barn i et hashmap.
	 * @param g
	 * @param node
	 * @param count
	 * @param marked
	 * @param neighbours
	 * @param <T>
	 * @return
	 */
	public <T> int count(Graph<T> g, T node, HashMap <T, Integer> count, HashSet <T> marked, HashMap <T, ArrayList<T>> neighbours ){
		int counter = 1;
		marked.add(node);
		ArrayList<T> n = new ArrayList<>();
		for(T children : g.neighbours(node)){
			if(!marked.contains(children)){
				n.add(children);
				counter += count(g, children, count, marked, neighbours);
			}
		}
		neighbours.put(node,n);
		count.put(node, counter);
		return counter;
	}
}
