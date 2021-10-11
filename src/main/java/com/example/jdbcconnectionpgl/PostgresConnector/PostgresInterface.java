package com.example.jdbcconnectionpgl.PostgresConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgresInterface {
    public Statement st;
    public Connection con;

    public PostgresInterface(Connection con) throws SQLException {
        this.con = con;
        this.st = this.con.createStatement();
    }

    public int updateQuery(String queryString) throws SQLException{
        int result  = st.executeUpdate(queryString);
        return result;
    }

    public ResultSet readQuery(String queryString) throws SQLException {
        ResultSet result = st.executeQuery(queryString);
        return result;
    }




}
