# Answer File - Semester 2
# Description of each Implementation
Briefly describe your implementation of the different methods. What was your idea and how did you execute it? If there were any problems and/or failed implementations please add a description.

## Task 1 - mst

Denne metoden vil bruke Prims algoritme som lager et minimum spanning tree i en vektet graf.
I et spanning tree er alle noder koblet sammen. Poenget er å finne den korteste veien gjennom treet.
Lager et set (marked) som holder styr på nodene som allerede er inkludert i mst. Lager en PQ som prioriterer kantene etter størrelse.
Legger først til en startnode, og alle kantene til denne noden. Legger kantene inn i PQ, da vil PQ sortere de etter størrelse og alltid
velge den korteste. Legger så til startnoden i marked listen, som da vil si at den er besøkt. 

Vil så lage en loop som fortsetter til det er én mindre kant enn antall noder.
I loopen vil jeg alltid ta vekk den korteste kanten i pq, og så sjekke om marked listen inneholder de to nodene til denne kanten.
Om den gjør det, vil jeg adde den i shortest edge.

## Task 2 - lca

I denne oppgaven er målet å finne ut hvor det er mest sannsynlig at strømbruddet har skjedd. Det vil si at vi vil finne hvilken kant som er borte.
Bruker BFS - bredde første søk, er en metode for å undersøke en graf. Først undersøker en alle nodene ett steg unna, og så videre nodene som
er to steg unna. BFS søker fra root og gjennom hele grafen. Dermed finner BFS alle foreldrenodene, og lagrer de i form av et HashMap. 


I lca funksjonen vil jeg da finne barna til foreldrenodene ved bruk av BFS. Ved hjelp av en funksjon som jeg kaller findPath som tar inn en foreldrenode
og barnet til denne node, kan jeg lage en path. Videre kan jeg lage et HashSet med lengden av den ene pathen og finne ut når listene inneholder samme node.
Da har jeg funnet lowest common  ancestor. Her skjedde det da et strømbrudd.

Jeg brukte Martin sin BFS fra forelesning 12.

## Task 3 - addRedundant

Skal i denne oppgaven finne et par med noder der det lønner seg mest å legge til en kant. Det jeg tenker er at det er mest gunstig å legge en kant mellom
løvene til de to største subtrærne. Derfor vil jeg i koden finne de to største subtrærne og det nederste løvet i hver av disse. Så vil jeg
lage en edge mellom. For å telle noder bruker jeg en hjelpemetode som heter count. 

I metoden ønsker jeg å følge de to største subtrærne nedover treet til jeg finner den noden som har størst totalt antall barn. Jeg kommer til løvene når jeg finner 
en node som ikke har noen barn. Denne noden blir det ene løvet jeg skal returnere. 

Metoden er rekursiv. Den bruker hjelpefunksjonen count inni metoden. Rekursjonen returnerer en int som representerer antall barn noden har.
Når denne kalles, blir nodens barn og antall barn lagt inn i et hashmap.

I compareSize vil jeg sortere subtrærne til roten. Ved å bruke Collections.max kan jeg forbedre kjøretiden fra O(n) ved bruk av PQ til O(1).
I denne comparatoren finner vi hvilken to subtrær jeg ønsker å traversere gjennom.

I linje 150: Koden går alltid lenger og lenger ned i treet og aldri i samme node mer enn en gang. Derfor blir kjøretiden på for-loopen under O(n).



# Runtime Analysis
For each method of the different strategies give a runtime analysis in Big-O notation and a description of why it has this runtime.

**If you have implemented any helper methods you must add these as well.**

* ``mst(WeightedGraph<T, E> g)``: //O(n) + O(m log m) + O(m log m) = O(m log m)


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
	public <T, E extends Comparable<E>> ArrayList<Edge<T>> mst(WeightedGraph<T, E> g) { 

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


``findNodeNotinMarked()`` O(degree) * O(log m) = O(log m)
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
	
	private <E extends Comparable<E>,T> void findNodeNotInMarked(WeightedGraph<T, E> g, T node, HashSet<T> marked, PriorityQueue<Edge<T>> priEdges){
		if (!marked.contains(node)){ //O(1)
			marked.add(node); //O(1)
			for (Edge<T> edge: g.adjacentEdges(node)) //O(degree) til edge
					priEdges.add(edge); //O(log m)
			}
		}




* ``lca(Graph<T> g, T root, T u, T v)``: O(m) + O(n) + O(n) + O(n) = O(n)

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

    HashMap<T,T> childToParent = BFS.parents(g,root); //O(m)
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

``findPath(T parent,HashMap<T,T> childToParent )`` O(n)

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
		while (parent != null){ //O(n) * O(1) = O(n)
			path.add(parent); //O(1)
			parent = childToParent.get(parent); //O(1)
		}
		return path;
	}



``public class BFS {`` O(m)


    /**
     * Bfs er bredde-første-søk og starter på rooten før den undersøker alle noder gjennom grafen.
     * Bfs har i utgangspunktet kjøretid 2*O(m) = O(m), fordi du bare besøker hver node 2 ganger.
     * Summen av alle degrees i treet er 2m-2, som vil si at det å gå gjennom alle degrees er O(m).
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


    
* ``addRedundant(Graph<T> g, T root)``: O(n) + O(log n) = O(n)

  @Override
  public <T> Edge<T> addRedundant(Graph<T> g, T root) { //O(N) + O(log N) = O(N)
  HashMap<T,Integer> count = new HashMap<>();
  HashMap<T,ArrayList<T>> neighbours = new HashMap<>();

      HashSet<T> marked = new HashSet<>();
      HashSet<T> subTrees = new HashSet<>();

      ArrayList<T> leaves = new ArrayList<>();
      ArrayList<T> neighboursList = new ArrayList<>();

      count(g, root, count, marked, neighbours); //O(N)

      Comparator<T> compareSize = Comparator.comparingInt(count::get); //O(1)

      for (T n : g.neighbours(root)){ //O(N)
          neighboursList.add(n); //O(1)
      }
      T biggestSubTree = Collections.max(neighboursList,compareSize); //O(1)
      subTrees.add(biggestSubTree); //O(1)
      neighboursList.remove(biggestSubTree); //O(1)

      if (g.degree(root) > 1){ //O(logN) //Sjekker om naboen roten bare har en nabo
          subTrees.add(Collections.max(neighboursList,compareSize)); //Legger til i subtre lista
      } else {
          leaves.add(root); //O(1) //legger til i lista som skal returneres
      }
      

      //O(2 * log n * n), men:
      //Vil aldri få O(n log n) hvis O(n) er stor.
      //Koden går alltid lenger og lenger ned i treet og aldri i samme node mer enn en gang.

      //Derfor blir kjøretiden på foor-loopen under O(n). 

      for (T rootNode : subTrees){ //O(2) siden den ikke kjører gjennom mer enn to ganger = O(1)
          while (g.degree(rootNode) != 1){ //O(log n)Så lenge en man ikke finner en node som bare har en nabo
              int i = 0;
              T newNode = null;

              for (T neighbor : neighbours.get(rootNode)){ //O(1/2n) ,mens vi ikke er ved løvet
                  if (count.get(neighbor) > i){// O(1/2n), leter ett den noden med flest naboer 
                      i = count.get(neighbor);
                      newNode = neighbor;
                  }
              }
              rootNode = newNode; //rootNode er da noden med flest naboer. 
                                  // hile løkken leter videre etter neste med flest naboer
          }
          leaves.add(rootNode); //O(1)
      }

      T leaf1 = leaves.remove(leaves.size()-1); //O(1) fjerner siste
      T leaf2 = leaves.get(leaves.size()-1); //O(1) fjerner siste

      return new Edge<>(leaf1,leaf2);
  }


``count(Graph<T> g, T node, HashMap <T, Integer> count, HashSet <T> marked, HashMap <T, ArrayList<T>> neighbours )`` O(n)


	/**
	 * Denne hjelpemetoden teller noder.
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
		ArrayList<T> childrenList = new ArrayList<>();
		for(T children : g.neighbours(node)){ O(n)
			if(!marked.contains(children)){ O(1)
				childrenList.add(children); O(1)
				counter += count(g, children, count, marked, neighbours);
			}
		}
		neighbours.put(node, childrenList); O(1)
		count.put(node, counter); O(1)
		return counter;
	}



     
   

