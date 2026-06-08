package com.nexe.service;

import java.sql.*;
import java.util.*;

import org.springframework.stereotype.Component;

@Component
public class DynamicSQLRunner {

    public List<Map<String, Object>> getDetailsFromDB() {

        List<Map<String, Object>> resultList = new ArrayList<>();

        String query = "select * from users";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/userDetails",
                    "root",
                    "root");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();

            while (rs.next()) {

                Map<String, Object> row = new LinkedHashMap<>();

                for (int i = 1; i <= colCount; i++) {
                    row.put(meta.getColumnName(i), rs.getObject(i));
                }

                resultList.add(row);
            }

            rs.close();
            stmt.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }
}