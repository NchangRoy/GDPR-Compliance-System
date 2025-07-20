import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.table.ForeignKeyIndex;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.create.table.Index;

import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;

public class JSqlParser {



    public static  List<Table> Parser(String query){
         List<Table> tables=new ArrayList<>();
        try {
            Statements stmts=CCJSqlParserUtil.parseStatements(query);
           
            for(Statement stmt:stmts){
                if(stmt instanceof CreateTable){

                   
                    //if we are parsing a createtable statement
                    //first print the table name
                    CreateTable createTable=(CreateTable)stmt;
                    //we get the table name
                    String tableName=createTable.getTable().getName();

                     Table table=new Table(tableName);
                    //we get the foreign keys and store them in  set

                    ArrayList<String> colsWithUnique=new ArrayList<>();
                    //we find all the columns with a unique constraint
                    //inline constraints
                    for(ColumnDefinition col:createTable.getColumnDefinitions()){
                        if(col.getColumnSpecs()!=null){
                            for(String constraint:col.getColumnSpecs()){
                            if(constraint.toLowerCase().equals("unique")){
                                colsWithUnique.add(col.getColumnName());
                            }
                        }
                        }
                    }
                    //index level constraints
                  if(createTable.getIndexes()!=null){
                      for(Index index:createTable.getIndexes()){
                        if(index.getType().toLowerCase().equals("unique")){
                            colsWithUnique.addAll(index.getColumnsNames());
                        }
                    }

                  }

                    //next we get the foreign keys
                    System.out.println(colsWithUnique);
                    List<Index> indices=createTable.getIndexes();
                   if(indices!=null){
                     for(Index index:indices){

                        
                        //check for foreing key indices
                        if(index instanceof ForeignKeyIndex){
                            //we get the referenced column and the destination table
                            ForeignKeyIndex fk=(ForeignKeyIndex)index;
                            Boolean isUnique=true;
                            for(String columnName:fk.getColumnsNames()){
                                System.out.println("Column Name:"+columnName);
                                if(colsWithUnique.contains(columnName)){
                                    isUnique=false;                                    
                                    ForeingKey foreingKey=new ForeingKey(fk.getTable().getName(),List.of(columnName ),fk.getReferencedColumnNames(),true);
                                    table.addForeingKeyConst(foreingKey);
                                   
                                }
                                else{
                                     ForeingKey foreingKey=new ForeingKey(fk.getTable().getName(),List.of(columnName ),fk.getReferencedColumnNames() , false);
                                    table.addForeingKeyConst(foreingKey);
                                    
                                }
                            }

                        }
                    }
                   }
                   tables.add(table);
                   System.out.println(table);

                }

               
            }

            
        } catch (JSQLParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

         return tables;
        
    }
   
}
