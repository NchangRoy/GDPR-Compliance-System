public class Edge {
    Node sourceTable;
    Node destinationTable;
    String destinationColumn;
    String Cardinality;
    String relation;
public Edge(Node sourceTable, Node destinationTable, String destinationColumn, String cardinality) {
    this.sourceTable = sourceTable;
    this.destinationTable = destinationTable;
    this.destinationColumn = destinationColumn;
    Cardinality = cardinality;
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
public String getDestinationColumn() {
    return destinationColumn;
}
public void setDestinationColumn(String destinationColumn) {
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
    "\ndestinationColumn:"+destinationColumn+"\nCardinality:"+Cardinality+"\nRelation"+relation +"\n\n";
}
}
