import java.util.List;

public class ForeingKey {
    String destinationTable;
    List<String> destinationColumn;
    boolean isUnique;
    
    public ForeingKey(String destinationTable, List<String> destinationColumn, boolean isUnique) {
        this.destinationTable = destinationTable;
        this.destinationColumn = destinationColumn;
        this.isUnique = isUnique;
    }
    
    public String getDestinationTable() {
        return destinationTable;
    }
    public void setDestinationTable(String destinationTable) {
        this.destinationTable = destinationTable;
    }
    public List<String> getDestinationColumn() {
        return destinationColumn;
    }
    public void setDestinationColumn(List<String> destinationColumn) {
        this.destinationColumn = destinationColumn;
    }
    public boolean isUnique() {
        return isUnique;
    }
    public void setUnique(boolean isUnique) {
        this.isUnique = isUnique;
    }
    @Override
    public String toString() {
        // TODO Auto-generated method stub

        String columns="desColumns: "+destinationColumn.stream().reduce(" ", (a,b)->{return a+" , "+b;});
        return "destinationTable: "+destinationTable+
            "\n "+columns+
            "\n isUnique:"+isUnique;
    }

}
