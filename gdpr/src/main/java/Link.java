public class Link {
    Node sourceTable;
    Node destinationTable;
    
    public Link(Node sourceTable, Node destinationTable) {
        this.sourceTable = sourceTable;
        this.destinationTable = destinationTable;
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

    @Override
    public String toString() {
        return "sourceTable: "+sourceTable+"\ndestinationTable: "+destinationTable;
    }

}
