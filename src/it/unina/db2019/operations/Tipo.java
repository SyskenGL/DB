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
public class Tipo extends DefaultFrame {
    
    private Object bean;

    /**
     * Creates new customizer Tipo
     */
    public Tipo() {
        initComponents();
        this.setLocationRelativeTo(null);
        setFrameTable(jTableTipo);
        setNomeTabella("TIPO");
        jTableTipo.setFocusable(false);
        jTextFieldNome.setEnabled(false);
        jComboBoxTipo.setFocusable(false);
        jComboBoxTipo.setEnabled(false);  
        jTextFieldScope.setEnabled(false);
        jTextFieldClasse.setEnabled(false);
        jTextFieldCD.setEnabled(false);
        jTextFieldParametro.setEnabled(false);
        
        Aggiorna();     
        jTableTipo.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if(jTableTipo.getSelectedRow() != -1){
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
              jTextFieldNome.setEnabled(true);
              jComboBoxTipo.setEnabled(true);
              jTextFieldScope.setEnabled(false);
              jTextFieldClasse.setEnabled(false);
              jTextFieldCD.setEnabled(true);
              jTextFieldParametro.setEnabled(false);
              break;
           case MODIFICA:
              setEnabledjTextFieldPK(false);
              jTextFieldNome.setEnabled(true);
              jComboBoxTipo.setEnabled(false);
              jTextFieldScope.setEnabled(false);
              jTextFieldClasse.setEnabled(false);
              jTextFieldCD.setEnabled(true);
              jTextFieldParametro.setEnabled(false);
              break;
           case ELIMINA:
              setEnabledjTextFieldPK(false);
              jTextFieldNome.setEnabled(false);
              jComboBoxTipo.setEnabled(false);
              jTextFieldScope.setEnabled(false);
              jTextFieldClasse.setEnabled(false);
              jTextFieldCD.setEnabled(false);
              jTextFieldParametro.setEnabled(false);
              break;
           case RICERCA:
              setEnabledjTextFieldPK(true);
              jTextFieldNome.setEnabled(true);
              jComboBoxTipo.setEnabled(true);
              jTextFieldScope.setEnabled(true);
              jTextFieldClasse.setEnabled(true);
              jTextFieldCD.setEnabled(true);
              jTextFieldParametro.setEnabled(true);
              break;
        }
    }

    @Override
    protected PreparedStatement selectAll(){
        Connection con;
        PreparedStatement st = null;
        super.selectAll();
        query = query + "ORDER BY ID_TIPO ";
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
        jTextFieldNome.setText("");
        jComboBoxTipo.setSelectedIndex(0);
        jTextFieldScope.setText("");
        jTextFieldClasse.setText("");
        jTextFieldCD.setText("");
        jTextFieldParametro.setText("");
        jTableTipo.clearSelection();
        super.setFocusPK();
    }

    @Override
    protected void getInfo(){
        selectedRow = jTableTipo.getSelectedRow();
        setJTextFieldPK((jTableTipo.getValueAt(selectedRow, 0)).toString());
        jTextFieldNome.setText((String)jTableTipo.getValueAt(selectedRow, 1));
        String type = (jTableTipo.getValueAt(selectedRow, 2)).toString();
        if(type.equals("strutturato")){
            jComboBoxTipo.setSelectedIndex(1);
        } else if(type.equals("classe")){
            jComboBoxTipo.setSelectedIndex(2);
        } else if(type.equals("parametrico")){
            jComboBoxTipo.setSelectedIndex(3);
        } else if(type.equals("primitivo")) {
            jComboBoxTipo.setSelectedIndex(4);
        } else{
            jComboBoxTipo.setSelectedIndex(5);
        }
        if((jTableTipo.getValueAt(selectedRow, 3) != null)){
            jTextFieldScope.setText((jTableTipo.getValueAt(selectedRow, 3)).toString());
        } else{
            jTextFieldScope.setText("");
        }
        if((jTableTipo.getValueAt(selectedRow, 4) != null)){
            jTextFieldCD.setText((jTableTipo.getValueAt(selectedRow, 4)).toString());
        } else{
            jTextFieldCD.setText("");
        }
        if((jTableTipo.getValueAt(selectedRow, 5) != null)){
            jTextFieldParametro.setText((jTableTipo.getValueAt(selectedRow, 5)).toString());
        } else{
            jTextFieldParametro.setText("");
        }
        if((jTableTipo.getValueAt(selectedRow, 6) != null)){
            jTextFieldClasse.setText((jTableTipo.getValueAt(selectedRow, 6)).toString());
        } else{
            jTextFieldClasse.setText("");
        }
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
        PreparedStatement st = null;
        if(!jComboBoxTipo.getSelectedItem().toString().equals("classe") && !jComboBoxTipo.getSelectedItem().toString().equals("parametrico")){
            cmdIns = "insert into " + DBConnection.schema + ".TIPO (ID_Tipo,Nome,"
                   + "Tipo, FK_Scope, FK_ClassDiagram, FK_Parametro, FK_Classe)  values(?,?,?,?,?,?,?)";
            st = c.prepareStatement(cmdIns);
            if(getCodice().getText().equals("")){
                st.setNull(1, Types.INTEGER);
            } else{
                st.setInt(1, Integer.valueOf(getCodice().getText()));
            }
            st.setString(2, jTextFieldNome.getText());
            st.setString(3, jComboBoxTipo.getSelectedItem().toString());
            if(jTextFieldScope.getText().equals("")){
                st.setNull(4, Types.INTEGER);
            } else{
                st.setInt(4, Integer.valueOf(jTextFieldScope.getText()));
            }
            st.setInt(5, Integer.valueOf(jTextFieldCD.getText()));        
            if(jTextFieldParametro.getText().equals("")){
                st.setNull(6, Types.INTEGER);
            } else{
                st.setInt(6, Integer.valueOf(jTextFieldParametro.getText()));
            }
            if(jTextFieldClasse.getText().equals("")){
                st.setNull(7, Types.INTEGER);
            } else{
                st.setInt(7, Integer.valueOf(jTextFieldClasse.getText()));
            }
        } else{
               JOptionPane.showMessageDialog(this, "I tipi parametri/classe sono gestiti direttamente dal DB!",
                                             "Errore", JOptionPane.ERROR_MESSAGE);
        }
        return st;
    }

    @Override
    protected PreparedStatement opModifica(Connection c)
           throws SQLException{
        PreparedStatement st = null;
        if(!getCodice().getText().equals("")){
            String cmdUp;
            cmdUp = "update " + DBConnection.schema + ".TIPO "
                            + " set Nome = ?, " 
                            + "     FK_ClassDiagram = ? "
                            + " where ID_Tipo = ? ";
            st = c.prepareStatement(cmdUp);
            st.setString(1, jTextFieldNome.getText());
            st.setInt(2, Integer.valueOf(jTextFieldCD.getText()));
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
        cmdSrc = "select * from " + DBConnection.schema + ".TIPO where";
        if (getCodice().getText().length() > 0) {
           cmdSrc += " ID_Tipo = ? and";
        }
        if (jTextFieldNome.getText().length() > 0) {
           cmdSrc += " Nome = ? and";
        }
        if (!jComboBoxTipo.getSelectedItem().toString().equals(" ")) {
           cmdSrc += " Tipo = ? and";
        }
        if (jTextFieldScope.getText().length() > 0) {
           cmdSrc += " FK_Scope = ? and";
        }
        if (jTextFieldCD.getText().length() > 0) {
           cmdSrc += " FK_ClassDiagram = ? and";
        }
        if (jTextFieldParametro.getText().length() > 0) {
           cmdSrc += " FK_Parametro = ? and";
        }
        if (jTextFieldClasse.getText().length() > 0) {
           cmdSrc += " FK_Classe = ? and";
        }
        pat = Pattern.compile("(where|and)$"); 
        match = pat.matcher(cmdSrc);
        cmdSrc = match.replaceAll("");
        cmdSrc += " ORDER BY ID_TIPO";
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
          if (!jComboBoxTipo.getSelectedItem().toString().equals(" ")) {
              st.setString(index++, jComboBoxTipo.getSelectedItem().toString());
          }
          if (jTextFieldScope.getText().length() > 0) {
              st.setInt(index++, Integer.valueOf(jTextFieldScope.getText()));
          }
          if (jTextFieldCD.getText().length() > 0) {
              st.setInt(index++, Integer.valueOf(jTextFieldCD.getText()));
          }
          if (jTextFieldParametro.getText().length() > 0) {
              st.setInt(index++, Integer.valueOf(jTextFieldParametro.getText()));
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
            cmdDel = "delete from " + DBConnection.schema + ".VIEW_TIPO where "
                      + "ID_Tipo = ? ";
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
        if(jTableTipo.getRowCount() > 0){
            switch(getModalita()){
                case MODIFICA:
                    jTableTipo.setRowSelectionInterval(selectedRow, selectedRow);
                    break;
                case INSERISCI:
                    jTableTipo.setRowSelectionInterval(jTableTipo.getRowCount()-1, jTableTipo.getRowCount()-1);
                    break;
                case ELIMINA:
                    if(selectedRow < jTableTipo.getRowCount()){
                            jTableTipo.setRowSelectionInterval(selectedRow, selectedRow);
                    } else{
                        jTableTipo.setRowSelectionInterval(selectedRow-1, selectedRow-1);
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

        jLabelNome = new javax.swing.JLabel();
        jLabelTipo = new javax.swing.JLabel();
        jLabelScope = new javax.swing.JLabel();
        jLabelCD = new javax.swing.JLabel();
        jLabelParametro = new javax.swing.JLabel();
        jLabelClasse = new javax.swing.JLabel();
        jTextFieldNome = new javax.swing.JTextField();
        jTextFieldScope = new javax.swing.JTextField();
        jTextFieldParametro = new javax.swing.JTextField();
        jTextFieldCD = new javax.swing.JTextField();
        jTextFieldClasse = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableTipo = new javax.swing.JTable();
        jComboBoxTipo = new javax.swing.JComboBox<>();

        setTitle("UML STORAGE - TIPO");
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(0, 580));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabelNome.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelNome.setText("Nome:");

        jLabelTipo.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelTipo.setText("Tipo:");

        jLabelScope.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelScope.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/unina/db2019/operations/fk.png"))); // NOI18N
        jLabelScope.setText("| Scope:");

        jLabelCD.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelCD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/unina/db2019/operations/fk.png"))); // NOI18N
        jLabelCD.setText("| Class Diagram:");

        jLabelParametro.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelParametro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/unina/db2019/operations/fk.png"))); // NOI18N
        jLabelParametro.setText("| Parametro:");

        jLabelClasse.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelClasse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/unina/db2019/operations/fk.png"))); // NOI18N
        jLabelClasse.setText("| Classe:");

        jTextFieldNome.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldNome.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTextFieldScope.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldScope.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTextFieldParametro.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldParametro.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTextFieldCD.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldCD.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTextFieldClasse.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldClasse.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTableTipo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "null", "null", "null"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableTipo);

        jComboBoxTipo.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jComboBoxTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "strutturato", "primitivo", "enumerazione", "classe", "parametrico" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 716, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelNome)
                            .addComponent(jLabelTipo)
                            .addComponent(jLabelScope)
                            .addComponent(jLabelParametro))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldParametro, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldScope, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(55, 55, 55)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(50, 50, 50)
                                        .addComponent(jLabelClasse))
                                    .addComponent(jLabelCD))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldCD, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldClasse, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jComboBoxTipo, javax.swing.GroupLayout.Alignment.LEADING, 0, 213, Short.MAX_VALUE)
                                .addComponent(jTextFieldNome, javax.swing.GroupLayout.Alignment.LEADING)))))
                .addContainerGap(139, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(144, 144, 144)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNome)
                    .addComponent(jTextFieldNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTipo)
                    .addComponent(jComboBoxTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelScope)
                    .addComponent(jLabelCD)
                    .addComponent(jTextFieldScope, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldCD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelParametro)
                    .addComponent(jLabelClasse)
                    .addComponent(jTextFieldParametro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldClasse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(54, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        DBOperation.currentlyOpen[7] = null;
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> jComboBoxTipo;
    private javax.swing.JLabel jLabelCD;
    private javax.swing.JLabel jLabelClasse;
    private javax.swing.JLabel jLabelNome;
    private javax.swing.JLabel jLabelParametro;
    private javax.swing.JLabel jLabelScope;
    private javax.swing.JLabel jLabelTipo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableTipo;
    private javax.swing.JTextField jTextFieldCD;
    private javax.swing.JTextField jTextFieldClasse;
    private javax.swing.JTextField jTextFieldNome;
    private javax.swing.JTextField jTextFieldParametro;
    private javax.swing.JTextField jTextFieldScope;
    // End of variables declaration//GEN-END:variables
}
