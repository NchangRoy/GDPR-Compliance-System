public class Node {
    String tableName;
    String primaryKey;
    public Node(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return tableName;
    }
}
