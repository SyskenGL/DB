/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unina.db2019.operations;

import it.unina.db2019.project.DBConnection;
import it.unina.db2019.project.DBOperation;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import  javax.swing.event.*;

public class ClassDiagram extends DefaultFrame {
    
    private SelettoreDiDate Data;
    private Object bean;
    private boolean isLike = true;
    /**
     * Creates new customizer ClassDiagram
     */
    public ClassDiagram() {
        super();
        initComponents();
        setFrameTable(jTableClassDiagram);
        setNomeTabella("CLASSDIAGRAM");
        jTextFieldNome.setEnabled(false);
        jTextFieldDescrizione.setEnabled(false);
        jTextFieldAutore.setEnabled(false); 
        jDateChooser.setEnabled(false);
        jRadioButtonEqual.setVisible(false);
        jRadioButtonEqual.setFocusable(false);
        jTableClassDiagram.setFocusable(false);
        this.setLocationRelativeTo(null);
        jDateChooser.getDateEditor().getUiComponent().setFocusable(false);
        Aggiorna();     
        jTableClassDiagram.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if(jTableClassDiagram.getSelectedRow() != -1){
                    getInfo();
                }
            }
        });        
    }
    
    public void setObject(Object bean) {
        this.bean = bean;
    }
    
   @Override
   public final void setModalita(int modo) {
      super.setModalita(modo);
      switch (modo) {
         case INSERISCI:
            setEnabledjTextFieldPK(true);
            jTextFieldNome.setEnabled(true);
            jTextFieldDescrizione.setEnabled(true);
            jTextFieldAutore.setEnabled(true);
            jDateChooser.setEnabled(true);
            jRadioButtonEqual.setVisible(false);
            break;
         case MODIFICA:
            setEnabledjTextFieldPK(false);
            jTextFieldNome.setEnabled(true);
            jTextFieldDescrizione.setEnabled(true);
            jTextFieldAutore.setEnabled(true);
            jDateChooser.setEnabled(true);
            jRadioButtonEqual.setVisible(false);
            break;
         case ELIMINA:
            setEnabledjTextFieldPK(false);
            jTextFieldNome.setEnabled(false);
            jTextFieldDescrizione.setEnabled(false);
            jTextFieldAutore.setEnabled(false);
            jDateChooser.setEnabled(false);
            jRadioButtonEqual.setVisible(false);
            break;
         case RICERCA:
            setEnabledjTextFieldPK(true);
            jTextFieldNome.setEnabled(true);
            jTextFieldDescrizione.setEnabled(true);
            jTextFieldAutore.setEnabled(true);
            jDateChooser.setEnabled(true);
            jRadioButtonEqual.setVisible(true);
            break;
      }
   }
  
   @Override
   protected PreparedStatement selectAll() {
      Connection con;
      PreparedStatement st = null;
      super.selectAll();
      query = query + "ORDER BY ID_CLASSDIAGRAM ";
      try {
         con = DBConnection.getConnection();
         st = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                 ResultSet.CONCUR_READ_ONLY); 
      } catch (SQLException e) {
      }
      return st;
   }
   
    @Override
    public void Aggiorna(){
      PreparedStatement st = selectAll();
      eseguiQuery(st);
    }   
    
    @Override
    public PreparedStatement opElimina(Connection c) throws SQLException{
      PreparedStatement st = null;
      if(!getCodice().getText().equals("")){
          String cmdDel;

          cmdDel = "delete from " + DBConnection.schema + ".CLASSDIAGRAM where "
                    + "ID_ClassDiagram = ? ";
          st = c.prepareStatement(cmdDel);
          st.setInt(1, Integer.valueOf(getCodice().getText()));
      } else{
          JOptionPane.showMessageDialog(this, "Nessun record selezionato!",
                                        "Errore", JOptionPane.ERROR_MESSAGE);
      }
      return st;
    }
    
    @Override
    public PreparedStatement opRicerca(Connection c) throws SQLException{
      PreparedStatement st = null;
      String cmdSrc;
      int index = 1;
      Pattern pat;
      Matcher match;
      String descrizione;
      cmdSrc = "select * from " + DBConnection.schema + ".CLASSDIAGRAM where";
      if (getCodice().getText().length() > 0) {
         cmdSrc += " ID_ClassDiagram = ? and";
      }
      if (jTextFieldNome.getText().length() > 0) {
         cmdSrc += " Nome = ? and";
      }
      if (jTextFieldAutore.getText().length() > 0) {
         cmdSrc += " Autore = ? and";
      }
      if (((javax.swing.JTextField)jDateChooser.getDateEditor().getUiComponent()).getText().length() > 0) {
         cmdSrc += " C_Data = ? and";
      }
      descrizione = jTextFieldDescrizione.getText();
      if (descrizione.length() > 0) {
         if(isLike){
             cmdSrc += " Descrizione like ? ";
             descrizione = "%"+descrizione+"%";  
         } else{
             cmdSrc += " Descrizione = ? ";
         }
      }
      pat = Pattern.compile("(where|and)$"); 
      match = pat.matcher(cmdSrc);
      cmdSrc = match.replaceAll("");
      cmdSrc += " ORDER BY ID_CLASSDIAGRAM";
      try {
         st = c.prepareStatement(cmdSrc, ResultSet.TYPE_SCROLL_INSENSITIVE,
                 ResultSet.CONCUR_READ_ONLY); 
      } catch (SQLException e) {
      }
      if(st != null){
        if (getCodice().getText().length() > 0) {
            st.setInt(index++, Integer.valueOf(getCodice().getText()));
        }
        if (jTextFieldNome.getText().length() > 0) {
            st.setString(index++, jTextFieldNome.getText());
        }
        if (jTextFieldAutore.getText().length() > 0) {
            st.setString(index++, jTextFieldAutore.getText());
        }
        if (((javax.swing.JTextField)jDateChooser.getDateEditor().getUiComponent()).getText().length() > 0) {
            st.setString(index++, ((javax.swing.JTextField) jDateChooser.getDateEditor().getUiComponent()).getText());
        }
        if (jTextFieldDescrizione.getText().length() > 0) {
            st.setString(index, descrizione);
        }
      }
      return st;   
    }
    
    @Override
    public PreparedStatement opModifica(Connection c) throws SQLException{
      PreparedStatement st = null;
      if(!getCodice().getText().equals("")){
        String cmdUp;
        cmdUp = "update " + DBConnection.schema + ".CLASSDIAGRAM "
                        + " set Nome = ?, " 
                        + "     Autore = ?, "
                        + "     C_Data = ?, "
                        + "     Descrizione = ? "
                        + " where ID_ClassDiagram = ? ";
        st = c.prepareStatement(cmdUp);
        st.setString(1, jTextFieldNome.getText());
        st.setString(2, jTextFieldAutore.getText());
        st.setString(3, ((javax.swing.JTextField) jDateChooser.getDateEditor().getUiComponent()).getText());
        st.setString(4, jTextFieldDescrizione.getText());
        st.setInt(5, Integer.valueOf(getCodice().getText()));
      } else{
            JOptionPane.showMessageDialog(this, "Nessun record selezionato!",
                                          "Errore", JOptionPane.ERROR_MESSAGE);
      }
      return st;
    }
    
@Override
    public PreparedStatement opInserisci(Connection c) throws SQLException{
      String cmdIns;
      PreparedStatement st;
      cmdIns = "insert into " + DBConnection.schema + ".CLASSDIAGRAM (ID_ClassDiagram,Nome,"
              + "Autore,C_Data,Descrizione)  values(?,?,?,?,?)";
      st = c.prepareStatement(cmdIns);
      if(getCodice().getText().equals("")){
          st.setNull(1, Types.INTEGER);
      } else{
          st.setInt(1, Integer.valueOf(getCodice().getText()));
      }
      st.setString(2, jTextFieldNome.getText());
      if(jTextFieldAutore.getText().equals("")){
          st.setNull(3, Types.VARCHAR);
      } else{
          st.setString(3, jTextFieldAutore.getText());
      }     
      String s = ((javax.swing.JTextField) jDateChooser.getDateEditor().getUiComponent()).getText();
      if(s.equals("")){
          st.setNull(4,Types.VARCHAR);
      }else{
          st.setString(4,s);
      }
      if(jTextFieldDescrizione.getText().equals("")){
          st.setNull(5, Types.VARCHAR);
      } else{
          st.setString(5, jTextFieldDescrizione.getText());
      }
      return st;
    }
    
    @Override
    protected void pulisci() {
        super.pulisci();
        jTextFieldNome.setText("");
        jTextFieldAutore.setText("");
        jTextFieldDescrizione.setText("");
        jDateChooser.setCalendar(null);
        jTableClassDiagram.clearSelection();
        jRadioButtonEqual.setSelected(true);
        super.setFocusPK();
    }

    
    
    @Override
    protected void getInfo(){
        selectedRow = jTableClassDiagram.getSelectedRow();
        setJTextFieldPK((jTableClassDiagram.getValueAt(selectedRow, 0)).toString());
        jTextFieldNome.setText((String)jTableClassDiagram.getValueAt(selectedRow, 1));
        jTextFieldAutore.setText((String)jTableClassDiagram.getValueAt(selectedRow, 2));
        String date = (String) jTableClassDiagram.getValueAt(selectedRow, 3);
        try{
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
            java.util.Date utilDate = sdf1.parse(date);
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            jDateChooser.setDate(sqlDate);
            
          } catch(ParseException e){}
        jTextFieldDescrizione.setText((String)jTableClassDiagram.getValueAt(selectedRow, 4));
    }
    
    @Override
    protected void reselectPrevRow(){
        if(jTableClassDiagram.getRowCount() > 0){
            switch(getModalita()){
                case MODIFICA:
                    jTableClassDiagram.setRowSelectionInterval(selectedRow, selectedRow);
                    break;
                case INSERISCI:
                    jTableClassDiagram.setRowSelectionInterval(jTableClassDiagram.getRowCount()-1, jTableClassDiagram.getRowCount()-1);
                    break;
                case ELIMINA:
                    if(selectedRow < jTableClassDiagram.getRowCount()){
                            jTableClassDiagram.setRowSelectionInterval(selectedRow, selectedRow);
                    } else{
                        jTableClassDiagram.setRowSelectionInterval(selectedRow-1, selectedRow-1);
                    }     
            }
        }    
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableClassDiagram = new javax.swing.JTable();
        jTextFieldNome = new javax.swing.JTextField();
        jTextFieldAutore = new javax.swing.JTextField();
        jLabelNome = new javax.swing.JLabel();
        jLabelAutore = new javax.swing.JLabel();
        jTextFieldDescrizione = new javax.swing.JTextField();
        jLabelDescrizione = new javax.swing.JLabel();
        jLabelDate = new javax.swing.JLabel();
        jDateChooser = new com.toedter.calendar.JDateChooser();
        jRadioButtonEqual = new javax.swing.JRadioButton();

        setTitle("UML STORAGE - CLASS DIAGRAM ");
        setMinimumSize(new java.awt.Dimension(0, 590));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jTableClassDiagram.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTableClassDiagram.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableClassDiagram.setFocusable(false);
        jTableClassDiagram.setMinimumSize(new java.awt.Dimension(876, 481));
        jScrollPane1.setViewportView(jTableClassDiagram);
        jTableClassDiagram.getAccessibleContext().setAccessibleParent(jTableClassDiagram);

        jTextFieldNome.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldNome.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTextFieldAutore.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldAutore.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabelNome.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelNome.setText("Nome:");

        jLabelAutore.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelAutore.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/unina/db2019/operations/avatar.png"))); // NOI18N
        jLabelAutore.setText("| Autore:");

        jTextFieldDescrizione.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldDescrizione.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabelDescrizione.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelDescrizione.setText("Descrizione:");

        jLabelDate.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/unina/db2019/operations/calendar.png"))); // NOI18N
        jLabelDate.setText("Data: ");

        jDateChooser.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jDateChooser.setDateFormatString("dd-MM-yyyy");
        jDateChooser.setMaxSelectableDate(new java.util.Date(1577836867000L));
        jDateChooser.setMinSelectableDate(new java.util.Date(1546300867000L));

        jRadioButtonEqual.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jRadioButtonEqual.setSelected(true);
        jRadioButtonEqual.setText("| Like");
        jRadioButtonEqual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonEqualActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelAutore)
                            .addComponent(jLabelNome, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldAutore, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                            .addComponent(jTextFieldNome)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelDescrizione)
                            .addComponent(jLabelDate))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextFieldDescrizione, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jRadioButtonEqual))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 769, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(145, 145, 145)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNome, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldAutore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelAutore, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelDate))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldDescrizione, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelDescrizione, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButtonEqual))
                .addGap(29, 29, 29)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButtonEqualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonEqualActionPerformed
        isLike = !isLike;
    }//GEN-LAST:event_jRadioButtonEqualActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        DBOperation.currentlyOpen[0] = null;
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser jDateChooser;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabelAutore;
    private javax.swing.JLabel jLabelDate;
    private javax.swing.JLabel jLabelDescrizione;
    private javax.swing.JLabel jLabelNome;
    private javax.swing.JRadioButton jRadioButtonEqual;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableClassDiagram;
    private javax.swing.JTextField jTextFieldAutore;
    private javax.swing.JTextField jTextFieldDescrizione;
    private javax.swing.JTextField jTextFieldNome;
    // End of variables declaration//GEN-END:variables

}
