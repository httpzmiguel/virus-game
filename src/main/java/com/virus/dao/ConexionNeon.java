package com.virus.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionNeon {

    private static final String URL = "jdbc:postgresql://ep-square-mountain-bd7kd1ru-pooler.c-8.us-east-2.aws.neon.tech/neondb?sslmode=require";
    private static final String USER = "neondb_owner";
    private static final String PASSWORD = "npg_LxX56bnuDrah";

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
