/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unina.db2019.operations;

import static it.unina.db2019.operations.DefaultFrame.ELIMINA;
import static it.unina.db2019.operations.DefaultFrame.INSERISCI;
import static it.unina.db2019.operations.DefaultFrame.MODIFICA;
import static it.unina.db2019.operations.DefaultFrame.RICERCA;
import it.unina.db2019.project.DBConnection;
import it.unina.db2019.project.DBOperation;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.event.*;

/**
 *
 * @author Sysken
 */
public class Dipendenza extends DefaultFrame {
    
    private Object bean;

    /**
     * Creates new customizer Dipendenza
     */
    public Dipendenza() {
        initComponents();
        setFrameTable(jTableDipendenza);
        setNomeTabella("DIPENDENZA");
        jTextFieldDipendenza.setEnabled(false);
        jTextFieldSuperiore.setEnabled(false);  
        jTextFieldDipendente.setEnabled(false);
        this.setLocationRelativeTo(null);
        jTableDipendenza.setFocusable(false);
        Aggiorna();     
        jTableDipendenza.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if(jTableDipendenza.getSelectedRow() != -1){
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
              jTextFieldDipendenza.setEnabled(true);
              jTextFieldSuperiore.setEnabled(true);  
              jTextFieldDipendente.setEnabled(true);
              break;
           case MODIFICA:
              setEnabledjTextFieldPK(false);
              jTextFieldDipendenza.setEnabled(true);
              jTextFieldSuperiore.setEnabled(true);  
              jTextFieldDipendente.setEnabled(true);
              break;
           case ELIMINA:
              setEnabledjTextFieldPK(false);
              jTextFieldDipendenza.setEnabled(false);
              jTextFieldSuperiore.setEnabled(false);  
              jTextFieldDipendente.setEnabled(false);
              break;
           case RICERCA:
              setEnabledjTextFieldPK(true);
              jTextFieldDipendenza.setEnabled(true);
              jTextFieldSuperiore.setEnabled(true);  
              jTextFieldDipendente.setEnabled(true);
              break;
        }
    }

    @Override
    protected PreparedStatement selectAll(){
        Connection con;
        PreparedStatement st = null;
        super.selectAll();
        query = query + "ORDER BY ID_DIPENDENZA ";
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
        jTextFieldDipendenza.setText("");
        jTextFieldSuperiore.setText("");
        jTextFieldDipendente.setText("");
        jTableDipendenza.clearSelection();
        super.setFocusPK();
    }

    @Override
    protected void getInfo(){
        selectedRow = jTableDipendenza.getSelectedRow();
        setJTextFieldPK((jTableDipendenza.getValueAt(selectedRow, 0)).toString());
        jTextFieldDipendenza.setText((String)jTableDipendenza.getValueAt(selectedRow, 1).toString());
        jTextFieldSuperiore.setText((jTableDipendenza.getValueAt(selectedRow, 2)).toString());
        jTextFieldDipendente.setText((jTableDipendenza.getValueAt(selectedRow, 3)).toString());
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
        cmdIns = "insert into " + DBConnection.schema + ".DIPENDENZA (ID_Dipendenza,Dipendenza,"
                + "FK_Superiore,FK_Dipendente)  values(?,?,?,?)";
        st = c.prepareStatement(cmdIns);
        if(getCodice().getText().equals("")){
            st.setNull(1, Types.INTEGER);
        } else{
            st.setInt(1, Integer.valueOf(getCodice().getText()));
        }
        st.setString(2, jTextFieldDipendenza.getText());
        st.setInt(3, Integer.valueOf(jTextFieldSuperiore.getText()));
        st.setInt(4, Integer.valueOf(jTextFieldDipendente.getText()));
        return st;
    }

    @Override
    protected PreparedStatement opModifica(Connection c)
           throws SQLException{
        PreparedStatement st = null;
        if(!getCodice().getText().equals("")){
          String cmdUp;
          cmdUp = "update " + DBConnection.schema + ".DIPENDENZA "
                          + " set Dipendenza = ?, " 
                          + "     FK_Superiore = ?, "
                          + "     FK_Dipendente = ? "
                          + "where ID_Dipendenza = ? ";
          st = c.prepareStatement(cmdUp);
          st.setString(1, jTextFieldDipendenza.getText());
          st.setInt(2, Integer.valueOf(jTextFieldSuperiore.getText()));
          st.setInt(3, Integer.valueOf(jTextFieldDipendente.getText()));
          st.setInt(4, Integer.valueOf(getCodice().getText()));
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
        cmdSrc = "select * from " + DBConnection.schema + ".DIPENDENZA where";
        if (getCodice().getText().length() > 0) {
           cmdSrc += " ID_Dipendenza = ? and";
        }
        if (jTextFieldDipendenza.getText().length() > 0) {
           cmdSrc += " Dipendenza = ? and";
        }
        if (jTextFieldSuperiore.getText().length() > 0) {
           cmdSrc += " FK_Superiore = ? and";
        }
        if (jTextFieldDipendente.getText().length() > 0) {
           cmdSrc += " FK_Dipendente = ? and";
        }
        pat = Pattern.compile("(where|and)$"); 
        match = pat.matcher(cmdSrc);
        cmdSrc = match.replaceAll("");
        cmdSrc += " ORDER BY ID_DIPENDENZA";
        try {
           st = c.prepareStatement(cmdSrc, ResultSet.TYPE_SCROLL_INSENSITIVE,
                   ResultSet.CONCUR_READ_ONLY); 
        } catch (SQLException e) {
        }
        if(st != null){
          if (getCodice().getText().length() > 0) {
              st.setInt(index++, Integer.valueOf(getCodice().getText()));
          }
          if (jTextFieldDipendenza.getText().length() > 0) {
              st.setString(index++, jTextFieldDipendenza.getText());
          }
          if (jTextFieldSuperiore.getText().length() > 0) {
              st.setInt(index++, Integer.valueOf(jTextFieldSuperiore.getText()));
          }
          if (jTextFieldDipendente.getText().length() > 0) {
              st.setInt(index++, Integer.valueOf(jTextFieldDipendente.getText()));
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
            cmdDel = "delete from " + DBConnection.schema + ".DIPENDENZA where "
                      + "ID_Dipendenza = ? ";
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
        if(jTableDipendenza.getRowCount() > 0){
            switch(getModalita()){
                case MODIFICA:
                    jTableDipendenza.setRowSelectionInterval(selectedRow, selectedRow);
                    break;
                case INSERISCI:
                    jTableDipendenza.setRowSelectionInterval(jTableDipendenza.getRowCount()-1, jTableDipendenza.getRowCount()-1);
                    break;
                case ELIMINA:
                    if(selectedRow < jTableDipendenza.getRowCount()){
                            jTableDipendenza.setRowSelectionInterval(selectedRow, selectedRow);
                    } else{
                        jTableDipendenza.setRowSelectionInterval(selectedRow-1, selectedRow-1);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldDipendenza = new javax.swing.JTextField();
        jTextFieldSuperiore = new javax.swing.JTextField();
        jTextFieldDipendente = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableDipendenza = new javax.swing.JTable();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setTitle("UML STORAGE - DIPENDENZA");
        setMinimumSize(new java.awt.Dimension(0, 520));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel1.setText("Dipendenza:");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/unina/db2019/operations/fk.png"))); // NOI18N
        jLabel2.setText("| Superiore:");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/unina/db2019/operations/fk.png"))); // NOI18N
        jLabel3.setText("| Dipendente:");

        jTextFieldDipendenza.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldDipendenza.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTextFieldSuperiore.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldSuperiore.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTextFieldDipendente.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldDipendente.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTableDipendenza.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTableDipendenza);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextFieldDipendente, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldSuperiore, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldDipendenza, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 613, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(124, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(144, 144, 144)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldDipendenza, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldSuperiore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextFieldDipendente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        DBOperation.currentlyOpen[13] = null;
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTableDipendenza;
    private javax.swing.JTextField jTextFieldDipendente;
    private javax.swing.JTextField jTextFieldDipendenza;
    private javax.swing.JTextField jTextFieldSuperiore;
    // End of variables declaration//GEN-END:variables
}
