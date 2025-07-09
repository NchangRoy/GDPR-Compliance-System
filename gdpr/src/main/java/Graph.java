import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import java.util.stream.Collectors;

import java.util.Queue;

public class Graph {
    List<Edge> edges=new ArrayList<>();
    List<Node> nodes=new ArrayList<>();


    Map<Node,List<Edge>> dirGraphAdjaList=new HashMap<>();

     Map<Node,List<Edge>> undirGraphAdjaList=new HashMap<>();
    Graph(List<Table> tables){
        init(tables);
        creatDirGraphAdjaList();
        creatUndirGraphAdjaList();
      
       
        
    }

    //function to create the edge objects and node objects
    public void init(List<Table> tables){
        
        for(Table table:tables){
            nodes.add(new Node(table.getTableName()));

        }
        
        for(Table table:tables){
           
            for(ForeingKey fk:table.foreingKeys){

                for(String destinationColumn:fk.getDestinationColumn()){
                        String cardinality=fk.isUnique?"1:1":"m:1";
                        Edge edge=new Edge(findNodeForTable(table.getTableName()),findNodeForTable(fk.getDestinationTable()) ,destinationColumn, cardinality);
                        edges.add(edge);
                }
            }
        }
    }
    //function to construct graph adjacency list
    public void creatDirGraphAdjaList(){
        
        //add nodes to adjacency list
        for(Node node:nodes){
           
            List<Edge> edgesForNode=edges.stream().filter(n->n.getSourceTable().equals(node)).collect(Collectors.toList());
             dirGraphAdjaList.put(node, edgesForNode);
        }
        //add edges for corrresponding nodes to adjacency list
        

    }

    public void creatUndirGraphAdjaList(){
        //add all the known directed edges to the list
        List<Edge> newEdges=new ArrayList<>(edges);
        //create and add edges that are the opposite  of the directed
        List<Edge> reversedEdges=edges.stream().map(n->{
            Edge edge=new Edge(n.destinationTable, n.sourceTable, null, new StringBuilder(n.Cardinality).reverse().toString());
            edge.setRelation(n.getRelation());
            return edge;
        }).collect(Collectors.toList());
        newEdges.addAll(reversedEdges);
        for(Node node:nodes){
           
            List<Edge> edgesForNode=newEdges.stream().filter(n->n.getSourceTable().equals(node)).collect(Collectors.toList());
             undirGraphAdjaList.put(node, edgesForNode);
        }

    }

    public Node findNodeForTable(String tableName){


        Optional<Node> node= nodes.stream().filter(n->n.tableName.equals(tableName)).findFirst();
       
        return node.get();
    }

    

    public void annotate(String startTable){
         //first find node corresponding to that table
        Node startNode=findNodeForTable(startTable);
        Map<Node,Boolean> VisitedNodes=new HashMap<>();
        Map<Node,Node> parentNode=new HashMap<>();
        
        Queue<Node> queue=new LinkedList<>();
        
        //initializet the visited nodes and parent nodes map
        for(Node node:undirGraphAdjaList.keySet()){
            VisitedNodes.put(node, false);
            parentNode.put(node, null);
            
        }
        //add start node to the queue to start the algorithm

        queue.offer(startNode);

        while(!queue.isEmpty()){
            //mark current node as visited
            Node queue_head=queue.poll();
            if(queue_head!=null){
                
              
                    //we mark the node as visited
                     VisitedNodes.put(queue_head, true);
                // we find neighboring edges to the current node
                List<Edge> neigborEdges=undirGraphAdjaList.get(queue_head);
                for(Edge edge:neigborEdges){
                


                    //treat the edges

                    Node parent=queue_head;
                    Node child=edge.getDestinationTable();


                 
                   
                //first find the edge linking the two node in the directed graph

                //since in the undirected graph we swapped some orientations (new edges)
                //we will check if there exist a pair(parent,current node ) in the directed graph
                //no matter the order
                Edge linkinEdge=null;
                //assuming the parent node is the actual parent node
                
               if(dirGraphAdjaList.get(parent)!=null){
                for(Edge diredge:dirGraphAdjaList.get(parent)){
                    if(diredge.destinationTable.equals(child)){
                        linkinEdge=diredge;


                        String inverseCardinality=new StringBuilder(linkinEdge.Cardinality).reverse().toString();

                            if(inverseCardinality.equals("m:1")){
                                linkinEdge.setRelation("ACCESSES");;
                            }else{
                                linkinEdge.setRelation("OWNS");;
                            }
                        

                       
                    }
                }
               }
                
                //assuming the inverse
                if(dirGraphAdjaList.get(child)!=null){
                    for(Edge diredge:dirGraphAdjaList.get(child)){
                    if(diredge.destinationTable.equals(parent)){
                        linkinEdge=diredge;
                        //we need to compare with the reverse of the actual cardinality
                             
                        if(linkinEdge.Cardinality.equals("m:1")){
                            linkinEdge.setRelation("ACCESSES");;
                        }else{
                            linkinEdge.setRelation("OWNS");
                        }
                        //we compare with the actual cardinality in this case
                        
                    }
                }
                
              
               

            
             }    
                //if the destination node is not visited, add it to the queue
                //and set its parernt to the current queue head
                    // if next node is not treaded we add it to the queue
                    if(!VisitedNodes.get(edge.getDestinationTable())){
                        //mark as visited
                       
                        VisitedNodes.put(edge.getDestinationTable(),true);
                      
                        queue.offer(edge.getDestinationTable());

                    parentNode.put(edge.getDestinationTable(), queue_head);
                    }
                    

                
              }
                }
               
                
            }
        
     
        //label all edges that form loops between two tables and are in same direction as SHARED(ACCESSEs)
        if(findLoopBtwn2Tables()!=null){
            for(Edge edge:findLoopBtwn2Tables()){
            edge.setRelation("SHARED");
        }

        }
        //finally we propagate the access relation on tables that depend on other tables that have the ACCESS relation
            propagateAccessRelation(startTable);
        
          
       
    }

    public List<Edge> findLoopBtwn2Tables(){
        //first find the links between two edges

        List<Edge> loopinEdges=new ArrayList<>();
        for(Edge edge:new HashSet<>(edges)){
            long count=edges.stream().filter(n->(n.getDestinationTable().equals(edge.getDestinationTable()) && n.getSourceTable().equals(edge.getSourceTable()))).count();
            if(count>=2){
                loopinEdges.add(edge);
            }
        }
        
        return loopinEdges;
    }

    public void propagateAccessRelation(String startTable){
        //first reconstruct the undir graph
        creatUndirGraphAdjaList();
        //next do bfs but record the label at each level;
        Queue<Node> queue=new LinkedList<>();
        Map<Node,String> relations=new HashMap<>();
        Map<Node,Boolean> VisitedNodes=new HashMap<>();
        

        //initialization
        for(Node node:undirGraphAdjaList.keySet()){
            relations.put(node, null);
            VisitedNodes.put(node, false);

        }

        Node startNode=findNodeForTable(startTable);
        queue.offer(startNode);

        //start bfs
        while(!queue.isEmpty()){
            Node queue_head=queue.poll();
            VisitedNodes.put(queue_head, true);
            //visit his neigbors
            for(Edge edge:undirGraphAdjaList.get(queue_head)){



                  //treat the edges

                    Node parent=queue_head;
                    Node child=edge.getDestinationTable();


                //we find the edge linking the two parent and child node in the directed graph
                Edge linkinEdge=null;
                //assuming the parent node is the actual parent node
                
               if(dirGraphAdjaList.get(parent)!=null){
                for(Edge diredge:dirGraphAdjaList.get(parent)){
                    if(diredge.destinationTable.equals(child)){
                        linkinEdge=diredge;
                    }
                }
               }
                
                //assuming the inverse
                if(dirGraphAdjaList.get(child)!=null){
                    for(Edge diredge:dirGraphAdjaList.get(child)){
                    if(diredge.destinationTable.equals(parent)){
                        linkinEdge=diredge;
                        //we need to compare with the reverse of the actual cardinality
                            
                    }
                    }
             }
            
                if(!VisitedNodes.get(edge.getDestinationTable())){
                    //for 
                     if(relations.get(parent)!=null && relations.get(parent).equals("ACCESSES")){
              
             linkinEdge.setRelation("ACCESSES");
            }
                    //System.out.println(edge.getRelation());
                     VisitedNodes.put(edge.getDestinationTable(), true);
                    System.out.println(edge.getDestinationTable());
                    queue.offer(edge.getDestinationTable());

                    //update the relation before the table
                    relations.put(edge.getDestinationTable(),edge.getRelation());
                }
            }
        }
       // System.out.println(relations);

       System.out.println("\n\n*******Labeled Adjacency List *********\n\n");
        System.out.println(dirGraphAdjaList);
        
    }

}
