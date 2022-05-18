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
import javax.swing.event.*;

/**
 *
 * @author Sysken
 */
public class Realizzazione extends DefaultFrame {
    
    private Object bean;

    /**
     * Creates new customizer Realizzazione
     */
    public Realizzazione() {
        initComponents();
        setFrameTable(jTableRealizzazione);
        setNomeTabella("REALIZZAZIONE");
        jTextFieldClasse.setEnabled(false);
        jTextFieldInterfaccia.setEnabled(false);  
        this.setLocationRelativeTo(null);
        jTableRealizzazione.setFocusable(false);
        Aggiorna();     
        jTableRealizzazione.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if(jTableRealizzazione.getSelectedRow() != -1){
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
              jTextFieldClasse.setEnabled(true);
              jTextFieldInterfaccia.setEnabled(true);
              break;
           case MODIFICA:
              setEnabledjTextFieldPK(false);
              jTextFieldClasse.setEnabled(true);
              jTextFieldInterfaccia.setEnabled(true);
              break;
           case ELIMINA:
              setEnabledjTextFieldPK(false);
              jTextFieldClasse.setEnabled(false);
              jTextFieldInterfaccia.setEnabled(false);
              break;
           case RICERCA:
              setEnabledjTextFieldPK(true);
              jTextFieldClasse.setEnabled(true);
              jTextFieldInterfaccia.setEnabled(true);
              break;
        }
    }

    @Override
    protected PreparedStatement selectAll(){
        Connection con;
        PreparedStatement st = null;
        super.selectAll();
        query = query + "ORDER BY ID_REALIZZAZIONE ";
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
        jTextFieldClasse.setText("");
        jTextFieldInterfaccia.setText("");
        jTableRealizzazione.clearSelection();
        super.setFocusPK();
    }

    @Override
    protected void getInfo(){
        selectedRow = jTableRealizzazione.getSelectedRow();
        setJTextFieldPK((jTableRealizzazione.getValueAt(selectedRow, 0)).toString());
        jTextFieldInterfaccia.setText((String)jTableRealizzazione.getValueAt(selectedRow, 1).toString());
        jTextFieldClasse.setText((jTableRealizzazione.getValueAt(selectedRow, 2)).toString());
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
        cmdIns = "insert into " + DBConnection.schema + ".REALIZZAZIONE (ID_Realizzazione,FK_Interfaccia,"
                + "FK_Classe)  values(?,?,?)";
        st = c.prepareStatement(cmdIns);
        if(getCodice().getText().equals("")){
            st.setNull(1, Types.INTEGER);
        } else{
            st.setInt(1, Integer.valueOf(getCodice().getText()));
        }
        st.setInt(2, Integer.valueOf(jTextFieldInterfaccia.getText()));
        st.setInt(3, Integer.valueOf(jTextFieldClasse.getText()));
        return st;
    }

    @Override
    protected PreparedStatement opModifica(Connection c)
           throws SQLException{
        PreparedStatement st = null;
        if(!getCodice().getText().equals("")){
          String cmdUp;
          cmdUp = "update " + DBConnection.schema + ".REALIZZAZIONE "
                          + " set FK_Interfaccia = ?, " 
                          + "     FK_Classe = ? "
                          + "where     ID_Realizzazione = ? ";;
          st = c.prepareStatement(cmdUp);
          st.setInt(1, Integer.valueOf(jTextFieldInterfaccia.getText()));
          st.setInt(2, Integer.valueOf(jTextFieldClasse.getText()));
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
        cmdSrc = "select * from " + DBConnection.schema + ".REALIZZAZIONE where";
        if (getCodice().getText().length() > 0) {
           cmdSrc += " ID_Realizzazione = ? and";
        }
        if (jTextFieldInterfaccia.getText().length() > 0) {
           cmdSrc += " FK_Interfaccia = ? and";
        }
        if (jTextFieldClasse.getText().length() > 0) {
           cmdSrc += " FK_Classe = ? and";
        }
        pat = Pattern.compile("(where|and)$"); 
        match = pat.matcher(cmdSrc);
        cmdSrc = match.replaceAll("");
        cmdSrc += " ORDER BY ID_REALIZZAZIONE";
        try {
           st = c.prepareStatement(cmdSrc, ResultSet.TYPE_SCROLL_INSENSITIVE,
                   ResultSet.CONCUR_READ_ONLY); 
        } catch (SQLException e) {
        }
        if(st != null){
          if (getCodice().getText().length() > 0) {
              st.setInt(index++, Integer.valueOf(getCodice().getText()));
          }
          if (jTextFieldInterfaccia.getText().length() > 0) {
              st.setInt(index++, Integer.valueOf(jTextFieldInterfaccia.getText()));
          }
          if (jTextFieldClasse.getText().length() > 0) {
              st.setInt(index++, Integer.valueOf(jTextFieldClasse.getText()));
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
            cmdDel = "delete from " + DBConnection.schema + ".REALIZZAZIONE where "
                      + "ID_Realizzazione = ? ";
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
        if(jTableRealizzazione.getRowCount() > 0){
            switch(getModalita()){
                case MODIFICA:
                    jTableRealizzazione.setRowSelectionInterval(selectedRow, selectedRow);
                    break;
                case INSERISCI:
                    jTableRealizzazione.setRowSelectionInterval(jTableRealizzazione.getRowCount()-1, jTableRealizzazione.getRowCount()-1);
                    break;
                case ELIMINA:
                    if(selectedRow < jTableRealizzazione.getRowCount()){
                            jTableRealizzazione.setRowSelectionInterval(selectedRow, selectedRow);
                    } else{
                        jTableRealizzazione.setRowSelectionInterval(selectedRow-1, selectedRow-1);
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
        jTextFieldInterfaccia = new javax.swing.JTextField();
        jTextFieldClasse = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableRealizzazione = new javax.swing.JTable();

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

        setTitle("UML STORAGE - REALIZZAZIONE");
        setMinimumSize(new java.awt.Dimension(0, 490));
        setPreferredSize(new java.awt.Dimension(772, 490));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/unina/db2019/operations/fk.png"))); // NOI18N
        jLabel1.setText("| Interfaccia:");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/unina/db2019/operations/fk.png"))); // NOI18N
        jLabel2.setText("| Classe:");

        jTextFieldInterfaccia.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldInterfaccia.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTextFieldClasse.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldClasse.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTableRealizzazione.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTableRealizzazione.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jTableRealizzazione);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldInterfaccia, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                            .addComponent(jTextFieldClasse)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 489, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(248, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(145, 145, 145)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldInterfaccia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldClasse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        DBOperation.currentlyOpen[11] = null;
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTableRealizzazione;
    private javax.swing.JTextField jTextFieldClasse;
    private javax.swing.JTextField jTextFieldInterfaccia;
    // End of variables declaration//GEN-END:variables
}
