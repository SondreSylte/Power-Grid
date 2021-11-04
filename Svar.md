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

## Task 3 - addRedundant

Skal i denne oppgaven finne et par med noder der det lønner seg mest å legge til en kant. Det jeg tenker er at det er mest gunstig å legge en kant mellom
løvene til de to største subtrærne. Derfor vil jeg i koden finne de to største subtrærne og det nederste løvet i hver av disse. Så vil jeg
lage en edge mellom. For å telle noder bruker jeg en hjelpemetode som heter count. 

I compareSize vil jeg sortere subtrærne til roten. Ved å bruke Collections.max kan jeg forbedre kjøretiden fra O(n) ved bruk av PQ til O(1).


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

      //Koden går alltid lenger og lenger ned i treet og aldri i samme node mer enn en gang.
      //Derfor blir kjøretiden på foor-loopen under O(n).
      for (T rootNode : subTrees){ 
          while (g.degree(rootNode) != 1){ //O(log n)Så lenge en man ikke finner en node som bare har en nabo
              int i = 0;
              T newNode = null;

              for (T neighbor : neighbours.get(rootNode)){ //O(n)
                  if (count.get(neighbor) > i){//leter ett den noden med flest naboer
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
    marked.add(node); //O(1)
    ArrayList<T> childrenList = new ArrayList<>();
    for(T children : g.neighbours(node)){ //O(N)
    if(!marked.contains(children)){ //O(1)
    childrenList.add(children); //O(1)
    counter += count(g, children, count, marked, neighbours);
    }
    }
    neighbours.put(node, childrenList); //O(1)
    count.put(node, counter); //O(1)
    return counter;
    }   



     
   

