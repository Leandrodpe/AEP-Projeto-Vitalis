package br.com.vitalis;

import java.sql.*;

public class Conexao {
    private static final String URL =
        "jdbc:mysql://localhost:3306/vitalis"
      + "?useSSL=false"
      + "&serverTimezone=America/Sao_Paulo"
      + "&characterEncoding=UTF-8"
      + "&useUnicode=true";
    private static final String USER = "root";
    private static final String PASS = "Vh190607!";

    public Conexao() {}

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}