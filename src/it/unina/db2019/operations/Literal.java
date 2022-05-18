/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unina.db2019.operations;

import it.unina.db2019.project.DBConnection;
import it.unina.db2019.project.DBOperation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Sysken
 */
public class Literal extends DefaultFrame {
    
    private Object bean;

    /**
     * Creates new customizer Literal
     */
    public Literal() {
        initComponents();
        setFrameTable(jTableLiteral);
        setNomeTabella("LITERAL");
        jTextFieldEnum.setEnabled(false);
        jTextFieldLiteral.setEnabled(false);  
        this.setLocationRelativeTo(null);
        jTableLiteral.setFocusable(false);
        Aggiorna();     
        jTableLiteral.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if(jTableLiteral.getSelectedRow() != -1){
                    getInfo();
                }
            }
        }); 
    }
    
    public void setObject(Object bean) {
        this.bean = bean;
    }
    
    @Override
    public void setModalita(int modo){
        super.setModalita(modo);
        switch (modo) {
           case INSERISCI:
              setEnabledjTextFieldPK(true);
              jTextFieldLiteral.setEnabled(true);
              jTextFieldEnum.setEnabled(true);
              break;
           case MODIFICA:
              setEnabledjTextFieldPK(false);
              jTextFieldLiteral.setEnabled(true);
              jTextFieldEnum.setEnabled(true);
              break;
           case ELIMINA:
              setEnabledjTextFieldPK(false);
              jTextFieldLiteral.setEnabled(false);
              jTextFieldEnum.setEnabled(false);
              break;
           case RICERCA:
              setEnabledjTextFieldPK(true);
              jTextFieldLiteral.setEnabled(true);
              jTextFieldEnum.setEnabled(true);
              break;
        }
    }
    
    @Override
    protected PreparedStatement selectAll(){
      Connection con;
      PreparedStatement st = null;
      super.selectAll();
      query = query + "ORDER BY ID_LITERAL ";
      try {
         con = DBConnection.getConnection();
         st = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                 ResultSet.CONCUR_READ_ONLY); 
      } catch (SQLException e) {
      }
      return st;
    }
    
    @Override
    protected void pulisci(){
        super.pulisci();
        jTextFieldLiteral.setText("");
        jTextFieldEnum.setText("");
        jTableLiteral.clearSelection();
        super.setFocusPK();
    }
    
    @Override
    protected void getInfo(){
        selectedRow = jTableLiteral.getSelectedRow();
        setJTextFieldPK((jTableLiteral.getValueAt(selectedRow, 0)).toString());
        jTextFieldLiteral.setText((String)jTableLiteral.getValueAt(selectedRow, 1));
        jTextFieldEnum.setText((jTableLiteral.getValueAt(selectedRow, 2)).toString());
    }
    
    @Override
    protected void Aggiorna(){
      PreparedStatement st = selectAll();
      eseguiQuery(st);
    }
    
    @Override
    protected PreparedStatement opInserisci(Connection c)
           throws SQLException{
        String cmdIns;
        PreparedStatement st;
        cmdIns = "insert into " + DBConnection.schema + ".LITERAL (ID_Literal,Literal,"
                + "FK_Enumerazione)  values(?,?,?)";
        st = c.prepareStatement(cmdIns);
        if(getCodice().getText().equals("")){
            st.setNull(1, Types.INTEGER);
        } else{
            st.setInt(1, Integer.valueOf(getCodice().getText()));
        }
        st.setString(2, jTextFieldLiteral.getText());
        st.setInt(3, Integer.valueOf(jTextFieldEnum.getText()));
        return st;
    }
    
    @Override
    protected PreparedStatement opModifica(Connection c)
           throws SQLException{
      PreparedStatement st = null;
      if(!getCodice().getText().equals("")){
        String cmdUp;
        cmdUp = "update " + DBConnection.schema + ".LITERAL "
                        + " set Literal = ?, " 
                        + "     FK_Enumerazione = ? "
                        + "where     ID_Literal = ? ";;
        st = c.prepareStatement(cmdUp);
        st.setString(1, jTextFieldLiteral.getText());
        st.setInt(2, Integer.valueOf(jTextFieldEnum.getText()));
        st.setInt(3, Integer.valueOf(getCodice().getText()));
      } else{
            JOptionPane.showMessageDialog(this, "Nessun record selezionato!",
                                          "Errore", JOptionPane.ERROR_MESSAGE);
      }
      return st;
    }
   
    @Override
    protected PreparedStatement opRicerca(Connection c)
           throws SQLException{
        PreparedStatement st = null;
        String cmdSrc;
        int index = 1;
        Pattern pat;
        Matcher match;
        cmdSrc = "select * from " + DBConnection.schema + ".LITERAL where";
        if (getCodice().getText().length() > 0) {
           cmdSrc += " ID_Literal = ? and";
        }
        if (jTextFieldLiteral.getText().length() > 0) {
           cmdSrc += " Literal = ? and";
        }
        if (jTextFieldEnum.getText().length() > 0) {
           cmdSrc += " FK_Enumerazione = ? and";
        }
        pat = Pattern.compile("(where|and)$"); 
        match = pat.matcher(cmdSrc);
        cmdSrc = match.replaceAll("");
        cmdSrc += " ORDER BY ID_LITERAL";
        try {
           st = c.prepareStatement(cmdSrc, ResultSet.TYPE_SCROLL_INSENSITIVE,
                   ResultSet.CONCUR_READ_ONLY); 
        } catch (SQLException e) {
        }
        if(st != null){
          if (getCodice().getText().length() > 0) {
              st.setInt(index++, Integer.valueOf(getCodice().getText()));
          }
          if (jTextFieldLiteral.getText().length() > 0) {
              st.setString(index++, jTextFieldLiteral.getText());
          }
          if (jTextFieldEnum.getText().length() > 0) {
              st.setInt(index++, Integer.valueOf(jTextFieldEnum.getText()));
          }
        }
        return st;  
    }
    
    @Override
    protected PreparedStatement opElimina(Connection c)
           throws SQLException{
      PreparedStatement st = null;
      if(!getCodice().getText().equals("")){
          String cmdDel;
          cmdDel = "delete from " + DBConnection.schema + ".LITERAL where "
                    + "ID_Literal = ? ";
          st = c.prepareStatement(cmdDel);
          st.setInt(1, Integer.valueOf(getCodice().getText()));
      } else{
          JOptionPane.showMessageDialog(this, "Nessun record selezionato!",
                                        "Errore", JOptionPane.ERROR_MESSAGE);
      }
      return st;
    }
    
    @Override
    protected void reselectPrevRow(){
        if(jTableLiteral.getRowCount() > 0){
            switch(getModalita()){
                case MODIFICA:
                    jTableLiteral.setRowSelectionInterval(selectedRow, selectedRow);
                    break;
                case INSERISCI:
                    jTableLiteral.setRowSelectionInterval(jTableLiteral.getRowCount()-1, jTableLiteral.getRowCount()-1);
                    break;
                case ELIMINA:
                    if(selectedRow < jTableLiteral.getRowCount()){
                            jTableLiteral.setRowSelectionInterval(selectedRow, selectedRow);
                    } else{
                        jTableLiteral.setRowSelectionInterval(selectedRow-1, selectedRow-1);
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

        jLabelLiteral = new javax.swing.JLabel();
        jLabelEnumerazione = new javax.swing.JLabel();
        jTextFieldEnum = new javax.swing.JTextField();
        jTextFieldLiteral = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableLiteral = new javax.swing.JTable();

        setTitle("UML STORAGE - LITERAL");
        setMinimumSize(new java.awt.Dimension(740, 480));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabelLiteral.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelLiteral.setText("Literal: ");

        jLabelEnumerazione.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelEnumerazione.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/unina/db2019/operations/fk.png"))); // NOI18N
        jLabelEnumerazione.setText("| Enum: ");

        jTextFieldEnum.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldEnum.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTextFieldLiteral.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldLiteral.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTableLiteral.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableLiteral);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelEnumerazione)
                            .addComponent(jLabelLiteral))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldEnum, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldLiteral, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 624, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(71, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(145, 145, 145)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLiteral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLiteral))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldEnum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelEnumerazione))
                .addGap(29, 29, 29)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        DBOperation.currentlyOpen[9] = null;
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabelEnumerazione;
    private javax.swing.JLabel jLabelLiteral;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableLiteral;
    private javax.swing.JTextField jTextFieldEnum;
    private javax.swing.JTextField jTextFieldLiteral;
    // End of variables declaration//GEN-END:variables
}
