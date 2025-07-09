package com.example;

import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParser;

public class CalciteExample {
    public static void main(String[] args) throws Exception {
        String sql = "SELECT name, age FROM employees WHERE age > 30";

        SqlParser parser = SqlParser.create(sql);
        SqlNode sqlNode = parser.parseStmt();

        System.out.println("Parsed SQL AST:\n" + sqlNode.toString());
    }
}