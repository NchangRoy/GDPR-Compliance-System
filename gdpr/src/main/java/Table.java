import java.util.ArrayList;
import java.util.List;

public class Table {
    String tableName;
    List<ForeingKey> foreingKeys;

    
    public Table(String tableName) {
        this.tableName = tableName;
        foreingKeys=new ArrayList<>();
    }
    public String getTableName() {
        return tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void addForeingKeyConst(ForeingKey fk){
        this.foreingKeys.add(fk);
    }

    @Override
    public String toString() {
        return "table:" +tableName+"\nfks:"+foreingKeys;
    }

}
