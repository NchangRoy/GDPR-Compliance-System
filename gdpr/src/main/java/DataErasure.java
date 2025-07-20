import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.mysql.cj.protocol.Resultset;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
public class DataErasure {
    
    private Map<Node,List<Edge>> graph;
    private Node startNode;
    private Connection connection;
    private Map<String,String> anonymous=new HashMap<>();//field:value
    

    public DataErasure(Map<Node,List<Edge>> graph,Node startNode){
        init();
        this.graph=graph;
        this.startNode=startNode;
        anonymous.put("id","4");
        dfs(startNode);
    }
    
    public void dfs(Node startNode){
       
        Map<Node,Boolean> visitedNodes=new HashMap<>();
        for(Node node:graph.keySet()){
            visitedNodes.put(node, false);
        }
        treatNode(visitedNodes, startNode,Map.of("id","1"),"OWNS");
       
        


    }

    public void treatNode(Map<Node,Boolean> visitedNodes,Node currentNode,Map<String,String> pk_values,String relation){
         //mark current node as visited if it hasn't been visited
        
           try {
         
             System.out.println(currentNode.getTableName());
            //mark node as visited
          
           
            //prepare query to get the user information in the current table
             String condition= pk_values.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("and  "));
           String query="select * from "+currentNode.tableName+" where "+condition;
           System.out.println(query);



            if(!visitedNodes.get(currentNode)){
             PreparedStatement pStatement=connection.prepareStatement(query);
             ResultSet result=pStatement.executeQuery();
            
           

            //print table data

            //loop through data
           
                //get the neigbours
                if(graph.get(currentNode)!=null){

                while(result.next()){
                for(Edge edge : graph.get(currentNode)){
                    //System.out.println(edge);

                    

                       //System.out.println("entered here"); 
                        Map<String,String> map=new HashMap<>();
                        for(int i=0;i<edge.getDestinationColumn().size();i++){
                            map.put(edge.getDestinationColumn().get(i),result.getString(edge.getSourceColumn().get(i)));
                            System.out.println(map);
                        }
                        //treat neigbors first
                        treatNode(visitedNodes, edge.getDestinationTable(),map,edge.getRelation());

                    
                   
                }


            
            }
            //after deleting the neigbors,we need to backrack while deleting the data
            //we can finally delete the instance of the data at this node
            

            }


            //mark node as visited 
              visitedNodes.put(currentNode,true);
            

            //  printResultSet(result);
           
         }
        String dbUpdateQuery=null;
        if(relation.equals("OWNS")){
            //build the deletion query
             condition= pk_values.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("and  "));
            dbUpdateQuery="delete from "+currentNode.tableName+" where "+condition;

              PreparedStatement pStatement=connection.prepareStatement(dbUpdateQuery);
            pStatement.executeUpdate();

           System.out.println(dbUpdateQuery);
        }
        else if(relation.equals("SHARED")){
            //handle anonymization here

            
            //hardcoded for now

            //first fetch anonymous user's data
             condition=anonymous.entrySet().stream().map(n->n.getKey()+" = "+n.getValue()).collect(Collectors.joining(" and  "));
            PreparedStatement pStatement=connection.prepareStatement("select * from "+startNode.getTableName()+" where "+condition);
            ResultSet anonymousCred=pStatement.executeQuery();
           //find edge in graph to get  the mapping of columnnames
           Optional<Edge> edgeOpt = graph.get(startNode).stream()
            .filter(ed -> ed.getDestinationTable().equals(currentNode))
            .filter(ed -> {
                List<String> nodes = new ArrayList<>(pk_values.keySet());
                nodes.retainAll(ed.getDestinationColumn());
                return !nodes.isEmpty();
            })
            .findFirst();

        Edge edge = edgeOpt.orElse(null);
           System.out.println(edge);
            while(anonymousCred.next()){
                condition=pk_values.entrySet().stream().map(n->n.getKey()+" = "+n.getValue()).collect(Collectors.joining(" and "));
                //generate update queries to be done
                List<String> updates = new ArrayList<>();
                for (int i = 0; i < edge.getDestinationColumn().size(); i++) {
                    updates.add(edge.getDestinationColumn().get(i) + " = '" +
                                anonymousCred.getString(edge.getSourceColumn().get(i)) + "'");
                }
                String setString = String.join(", ", updates);
                     query="update "+currentNode.getTableName()+" set "+ setString+ " where "+condition;
                
                //print query
                System.out.println(query);

                //execute query
                pStatement =connection.prepareStatement(query);
                pStatement.executeUpdate();
                
            }
           


        }
        
         




         } catch (Exception e) {
            e.printStackTrace();
           }

    }
    public void init(){


         String jdbcUrl = "jdbc:mysql://localhost:3306/test";
        String username = "admin";
        String password = "password";

        try {
           
            Class.forName("com.mysql.cj.jdbc.Driver");

          
            Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println(" Connection successful!");
            
            connection=conn;
            

            
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Failed to connect to MySQL.");
            e.printStackTrace();
        }
    }

    public static void printResultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();

        // Print column headers
        for (int i = 1; i <= columnCount; i++) {
            System.out.print(meta.getColumnName(i) + "\t");
        }
        System.out.println();

        // Print rows
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(rs.getObject(i) + "\t");
            }
            System.out.println();
        }
}
}
