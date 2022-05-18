/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unina.db2019.operations;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.AbstractTableModel;

/**
 * Modello di JTable basate su un ResultSet. <br> Si preferisce basare il
 * modello su un ResultSet, piuttosto che su una query, in modo da poterlo
 * condividere con il DBFrame.
 *
 * @author Massimo
 * @author ADeLuca
 * @version 2018
 */
public class DBTableModel extends AbstractTableModel {

   private ResultSet rs; // Resultset su cui si basa il modello
   private final DefaultFrame FRAME; // Finestra di riferim. (per gestione eccezioni)

   /**
    * Crea una nuova istanza di DBTableModel.
    * 
    * @param frame la finestra in cui il modello viene applicato
    */
   public DBTableModel( DefaultFrame frame) {
      //super();
      this.FRAME = frame;
   }
   
   /**
    * Imposta il Resultset su cui si basa il modello.
    * 
    * @param r il ResultSet su cui basare il modello
    */
   public void setRS(ResultSet r) {
      rs = r;
      fireTableStructureChanged();
   }

   /**
    * Restituisce il nome di una colonna secondo i metadati del ResultSet.
    * 
    * @param col intero, indice di colonna
    * @return stringa, il nome della colonna
    */
   @Override
   public String getColumnName(int col) {
      col++;
      if (rs == null) {
         return "";
      }
      try {
         return rs.getMetaData().getColumnName(col);
      } catch (SQLException e) {
         FRAME.mostraErrori(e);
         // System.err.println("errore in getColumnName");
         return "";
      }
   }

   /**
    * Naviga il ResultSet per determinare il numero di righe.
    * 
    * @return intero, numero di righe del modello
    */
   @Override
   public int getRowCount() {
      if (rs == null) {
         return 0;
      }
      try {
         int currentPosition, last;
         currentPosition = rs.getRow();
         rs.last();
         last = rs.getRow();
         if (currentPosition == 0) rs.first();
         else rs.absolute(currentPosition);
         return last;
      } catch (SQLException e) {
         FRAME.mostraErrori(e);
         return 0;
      }
   }

   /**
    * Determina il numero di colonne dai metadati del ResultSet
    * 
    * @return intero, numero di colonne
    */
   @Override
   public int getColumnCount() {
      if (rs == null) {
         return 0;
      }
      try {
         return rs.getMetaData().getColumnCount();
      } catch (SQLException e) {
         FRAME.mostraErrori(e);
         // System.err.println("errore in getColumnCount");
         return 0;
      }
   }

   /**
    * Restituisce il valore da mostrare in una cella, in base al ResultSet
    * @param row intero, indice di riga
    * @param col intero, indice di colonna
    * @return oggetto da mostrare nella cella (row,col)
    */
   @Override
   public Object getValueAt(int row, int col) {
      int currentPosition;
      Object ob;
      row++;
      col++;
      try {
         currentPosition = rs.getRow();
         rs.absolute(row);
         ob = rs.getObject(col);
         rs.absolute(currentPosition);
         return ob;
      } catch (SQLException e) {
         FRAME.mostraErrori(e);
         return null;
      }
   }

   @Override
   public boolean isCellEditable(int row, int col) {
      return false;
   }


   @Override
   public void setValueAt(Object value, int row, int col) {
   }
}