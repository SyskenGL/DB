/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unina.db2019.project;

import java.sql.*;
import oracle.jdbc.pool.OracleDataSource;

/**
 * @author Sysken
 */

public class DBConnection {
    
    /** 
     * Nome utente della connessione 
     */
    static public String user = "";
    
    /** 
     * Password per accedere alla connessione user 
     */
    static public String password = "";
    
    /**
     * Tipo del driver 
     */
    static public String driverType = "thin";
    
    /** 
     * Indirizzo del server Oracle 
     */
    static public String host = "localhost";
    
    /** 
     * Numero di porta della connessione 
     */
    static public int port = 1521;
    
    /** 
     * SID del Database 
     */
    static public String sid = "xe";
    
    /** 
     * Nome dello schema 
     */
    static public String schema = "";
    
    /** 
     * Oggetto DataSource impiegato nella connessione 
     */
    static private OracleDataSource ods;
    
    /** 
     * Oggetto che racchiude la connessione 
     */
    static private Connection conn;
    
    /**
     * Creates a new instance of DBConnection
     */
    public DBConnection(){       
        /* empty */
    }
    
    /** 
     * Oggetto che racchiude la connessione 
     */
    public void setUser(String user){
        
    }
    
    /**
     * Crea una nuova connessione al DB.
     *
     * @return Connessione al DB secondo i parametri attualmente impostati
     * @throws SQLException Errori durante la creazione della connessione.
     */
    static public Connection newConnection() throws SQLException {
        ods = new OracleDataSource();
        ods.setDriverType(driverType);
        ods.setServerName(host);
        ods.setPortNumber(port);
        ods.setUser(user);
        ods.setPassword(password);
        ods.setDatabaseName(sid);
        return ods.getConnection();
     }
    
    /**
     * Restituisce una connessione al DB. Le proprietà risultano static dal momento
     * che la connessione, nel nostro caso, è indirizzata ad un unico database.
     * 
     * @return Connection: Nuova connessione nel caso in cui non ne fosse già
     * presente un'altra, altrimenti la medesima connessione attiva.
     * @throws SQLException Errori durante l'acquisizione della connessione.
    */
    static public Connection getConnection() throws SQLException {
          if (conn == null || conn.isClosed()) {
          conn = newConnection(); 
        }
       return conn;
    }  

   /**
    * Imposta conn ad una connessione specificata in input.
    *
    * @param connection =  Connessione al DB
    */
   static public void setConnection(Connection connection) {
      conn = connection;
   }
   
   
   
   
   static public Object leggiValore(String query) {
      Object ret;
      Connection con;
      Statement st;
      ResultSet rs;
      ret = null;
      try {
         con = getConnection();
         st = con.createStatement();
         rs = st.executeQuery(query);
         rs.next();
         ret = rs.getObject(1);
      } catch (SQLException e) {  //nessuna azione
      }
      return ret;
   }
   
}

  
