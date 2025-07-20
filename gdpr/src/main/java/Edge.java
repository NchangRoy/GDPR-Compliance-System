import java.util.List;

public class Edge {
    Node sourceTable;
    Node destinationTable;
    List<String> destinationColumn;
    List<String> sourceColumn;
    String Cardinality;
    String relation;

public Edge(Node sourceTable, Node destinationTable, List<String> destinationColumn,List<String> sourceColumn, String cardinality) {
    this.sourceTable = sourceTable;
    this.destinationTable = destinationTable;
    this.destinationColumn = destinationColumn;
    Cardinality = cardinality;
    this.sourceColumn=sourceColumn;
}

public Edge(Node sourceTable, Node destinationTable, List<String> destinationColumn, List<String> sourceColumn,
        String cardinality, String relation) {
    this.sourceTable = sourceTable;
    this.destinationTable = destinationTable;
    this.destinationColumn = destinationColumn;
    this.sourceColumn = sourceColumn;
    Cardinality = cardinality;
    this.relation = relation;
}

public List<String> getSourceColumn() {
    return sourceColumn;
}

public void setSourceColumn(List<String> sourceColumn) {
    this.sourceColumn = sourceColumn;
}

public Node getSourceTable() {

    return sourceTable;
}
public void setSourceTable(Node sourceTable) {
    this.sourceTable = sourceTable;
}
public Node getDestinationTable() {
    return destinationTable;
}
public void setDestinationTable(Node destinationTable) {
    this.destinationTable = destinationTable;
}
public List<String> getDestinationColumn() {
    return destinationColumn;
}
public void setDestinationColumn(List<String> destinationColumn) {
    this.destinationColumn = destinationColumn;
}
public String getCardinality() {
    return Cardinality;
}
public void setCardinality(String cardinality) {
    Cardinality = cardinality;
}



public String getRelation() {
    return relation;
}
public void setRelation(String relation) {
    this.relation = relation;
}
    @Override
public String toString() {
    return "sourceTable:"+sourceTable+"\ndestinationTable:"+destinationTable+
    "\ndestinationColumn:"+destinationColumn+"\nsourceColumn"+sourceColumn+"\nCardinality:"+Cardinality+"\nRelation"+relation +"\n\n";
}
}
