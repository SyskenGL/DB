/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unina.db2019.operations;
import javax.swing.JComboBox;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.sql.Date;
/**
 *
 * @author gennaro
 */
public class SelettoreDiDate {
   

   private final JComboBox GIORNO;
   private final JComboBox MESE;
   private final JComboBox ANNO;
 
   private final boolean feb29;
   
   /**
    * Costruice un'istanza in base ai parametri forniti.
    * 
    * @param giorno una JComboBox per la selezione del GIORNO
    * @param mese una JComboBox per la selezione del MESE
    * @param anno una JComboBox per la selezione dell'ANNO
    */
   public SelettoreDiDate(JComboBox giorno, JComboBox mese,
           JComboBox anno) {
      GIORNO = giorno;
      MESE = mese;
      ANNO = anno;
      feb29 = false;
    
   }
   
   /**
    * Attiva o disattiva i 3 JComboBox
    * 
    * @param mode true per attivare, false per disattivare
    */
   public void setEnabled(boolean mode) {
      GIORNO.setEnabled(mode);
      MESE.setEnabled(mode);
      ANNO.setEnabled(mode);
   }
   

   /**
    * Imposta una data specifica nelle 3 JComboBox.
    * 
    * @param date data da impostare, come {@link LocalDate}
    */
   public void setDate(LocalDate date) {
      if (date == null) {
         // = "1 gennaio (nessuno)"
         GIORNO.setSelectedIndex(0);
         MESE.setSelectedIndex(0);
         ANNO.setSelectedIndex(0);
      } else{
         GIORNO.setSelectedIndex(date.getDayOfMonth()-1);
         MESE.setSelectedItem(date.getMonth().getDisplayName(TextStyle.FULL,
                 Locale.ITALIAN));
         ANNO.setSelectedItem(date.getYear());
      }
   }

   /**
    * Imposta una data specifica nelle 3 JComboBox.
    * 
    * @param date data da impostare, come {@link Date}
    */
   
   
   public void setDate(Date date) {
      if (date == null) {
         LocalDate d = null;
         setDate(d);
      }
      else setDate(date.toLocalDate());
   }
}


