# Answer File - Semester 2
# Description of each Implementation
Briefly describe your implementation of the different methods. What was your idea and how did you execute it? If there were any problems and/or failed implementations please add a description.

## Task 1 - mst
*Enter description*

## Task 2 - lca
*Enter description*

## Task 3 - addRedundant
*Enter description*


# Runtime Analysis
For each method of the different strategies give a runtime analysis in Big-O notation and a description of why it has this runtime.

**If you have implemented any helper methods you must add these as well.**

* ``mst(WeightedGraph<T, E> g)``: O(N) + O(M logM) + O(N*M log M)

  public <T, E extends Comparable<E>> ArrayList<Edge<T>> mst(WeightedGraph<T, E> g) { 

      ArrayList<Edge<T>> cheapestEdge = new ArrayList<>(g.numVertices());
      if (g.numVertices() < 1){
          return cheapestEdge;
      }

      HashSet<T> marked = new HashSet<>(g.numVertices());
      PriorityQueue<Edge<T>> priEdges = new PriorityQueue<>(g);

      T start = g.vertices().iterator().next(); //O(N)
      for(Edge<T> e : g.adjacentEdges(start)){  //O(M) * O(log M) = O(M logM)
          priEdges.add(e); //O(log M)
      }
      marked.add(start); //O(1)

      while (!priEdges.isEmpty() && cheapestEdge.size()-1 < g.size()){ //O(N) * O(M logM) = O(N*M log M)
          Edge<T> e = priEdges.remove(); //O(log N)
          T v = e.a;
          T w = e.b;
          assert marked.contains(v) || marked.contains(w); //O(1)

          if (!marked.contains(v)){ //O(1)
              marked.add(v); //(1)
              cheapestEdge.add(e); //O(1)
              for (Edge<T> edge: g.adjacentEdges(v)){ //O(M) * O(log M) = O(M logM)
                  if (!marked.contains(edge.other(v))){ //O(1)
                      priEdges.add(edge); //O(log M)
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


* ``lca(Graph<T> g, T root, T u, T v)``: O(n) + O(n) + O(n) + O(n) = O(n)

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

    HashMap<T,T> childToParent = BFS.parents(g,root); //O(n)
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



	


``public class BFS {`` O(n)

    /**
     * Bfs er bredde-første-søk og starter på rooten før den undersøker alle noder gjennom grafen.
     * Bfs har i utgangspunktet kjøretid O(n), fordi du bare besøker hver node 1 gang.
     * Summen av alle degrees i treet er 2n-2, som vil si at det å gå gjennom alle degrees er O(n).
     * Derfor konkluderer jeg med at kjøretiden til BFS er O(n).
     * @param g
     * @param root
     * @param <V>
     * @return parents
     */

    public static <V> HashMap<V,V> parents(Graph<V> g, V root){ //O(n)
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


    
* ``addRedundant(Graph<T> g, T root)``: O(n) + O(log n) = O(n)

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
    HashSet<T> subTrees = new HashSet<>(); //inneholder to elementer. Største subtreene til roten

    ArrayList<T> leaves = new ArrayList<>();
    ArrayList<T> neighbors = new ArrayList<>();

    countDepth(g, root, count, marked, neighbours); //O(n)

    Comparator<T> compareSize = Comparator.comparingInt(count::get); //O(1)

    for (T neighbour : g.neighbours(root)){ //O(n)
    neighbors.add(neighbour); //O(1)
    }
    T biggest = Collections.max(neighbors,compareSize); //O(1)
    subTrees.add(biggest); //O(1)
    neighbors.remove(biggest); //O(1)

    if (g.degree(root) > 1){ //O(log n)
    subTrees.add(Collections.max(neighbors,compareSize));
    } else {
    leaves.add(root); //O(1)
    }

    //Koden går alltid lenger og lenger ned i treet og aldri i samme node mer enn en gang.
    //Derfor blir kjøretiden på foor-loopen under O(n).
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

  //O(n)
  public <T> int countDepth(Graph<T> g, T node, HashMap <T, Integer> count, HashSet <T> marked, HashMap <T, ArrayList<T>> neighbours ){
  int counter = 1;
  marked.add(node); //O(1)
  ArrayList<T> childrenList = new ArrayList<>();
  for(T children : g.neighbours(node)){ //O(n)
  if(!marked.contains(children)){ //O(1)
  childrenList.add(children); //O(1)
  counter += countDepth(g, children, count, marked, neighbours);
  }
  }
  neighbours.put(node, childrenList); //O(1)
  count.put(node, counter); //O(1)
  return counter;
  }
   
     
   

