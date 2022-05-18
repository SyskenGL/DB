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
public class Partecipazione extends DefaultFrame {
    
    private Object bean;

    /**
     * Creates new customizer Partecipazione
     */
    public Partecipazione() {
        initComponents();
        this.setLocationRelativeTo(null);
        setFrameTable(jTablePartecipazione);
        setNomeTabella("PARTECIPAZIONE");
        this.setLocationRelativeTo(null);
        jComboBoxTipo.setFocusable(false);
        jComboBoxTipo.setEnabled(false);
        jTextFieldRole.setEnabled(false);
        jTextFieldCardInf.setEnabled(false);
        jTextFieldCardSup.setEnabled(false);
        jTablePartecipazione.setFocusable(false);
        jTextFieldAssociazione.setEnabled(false);
        jTextFieldClasse.setEnabled(false);
        jRadioButtonNav.setEnabled(false);
        jRadioButtonNav.setFocusable(false);
        jRadioButtonQualify.setEnabled(false);
        jRadioButtonQualify.setFocusable(false);
        Aggiorna();     
        jTablePartecipazione.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if(jTablePartecipazione.getSelectedRow() != -1){
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
              jComboBoxTipo.setEnabled(true);
              jTextFieldRole.setEnabled(true);
              jTextFieldCardInf.setEnabled(true);
              jTextFieldCardSup.setEnabled(true);
              jTextFieldClasse.setEnabled(true);
              jTextFieldAssociazione.setEnabled(true);
              jRadioButtonNav.setEnabled(true);
              jRadioButtonQualify.setEnabled(true);
              break;
           case MODIFICA:
              setEnabledjTextFieldPK(false);
              jComboBoxTipo.setEnabled(false);
              jTextFieldRole.setEnabled(true);
              jTextFieldCardInf.setEnabled(true);
              jTextFieldCardSup.setEnabled(true);
              jTextFieldClasse.setEnabled(false);
              jTextFieldAssociazione.setEnabled(false);
              jRadioButtonNav.setEnabled(true);
              jRadioButtonQualify.setEnabled(true);
              break;
           case ELIMINA:
              setEnabledjTextFieldPK(false);
              jComboBoxTipo.setEnabled(false);
              jTextFieldRole.setEnabled(false);
              jTextFieldCardInf.setEnabled(false);
              jTextFieldCardSup.setEnabled(false);
              jTextFieldClasse.setEnabled(false);
              jTextFieldAssociazione.setEnabled(false);
              jRadioButtonNav.setEnabled(false);
              jRadioButtonQualify.setEnabled(false);
              break;
           case RICERCA:
              setEnabledjTextFieldPK(true);
              jComboBoxTipo.setEnabled(true);
              jTextFieldRole.setEnabled(true);
              jTextFieldCardInf.setEnabled(true);
              jTextFieldCardSup.setEnabled(true);
              jTextFieldClasse.setEnabled(true);
              jTextFieldAssociazione.setEnabled(true);
              jRadioButtonNav.setEnabled(true);
              jRadioButtonQualify.setEnabled(true);
              break;
        }
    }

    @Override
    protected PreparedStatement selectAll(){
        Connection con;
        PreparedStatement st = null;
        super.selectAll();
        query = query + "ORDER BY ID_PARTECIPAZIONE ";
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
        jComboBoxTipo.setSelectedIndex(0);
        jTextFieldRole.setText("");
        jTextFieldCardInf.setText("");
        jTextFieldCardSup.setText("");
        jTextFieldClasse.setText("");
        jRadioButtonNav.setSelected(true);
        jRadioButtonQualify.setSelected(false);
        jTextFieldAssociazione.setText("");
        jTablePartecipazione.clearSelection();
    }

    @Override
    protected void getInfo(){
        selectedRow = jTablePartecipazione.getSelectedRow();
        setJTextFieldPK((jTablePartecipazione.getValueAt(selectedRow, 0)).toString());
        jTextFieldRole.setText((String)jTablePartecipazione.getValueAt(selectedRow, 1));
        jTextFieldCardInf.setText((String)jTablePartecipazione.getValueAt(selectedRow, 2));
        jTextFieldCardSup.setText((String)jTablePartecipazione.getValueAt(selectedRow, 3));
        String tipo = (jTablePartecipazione.getValueAt(selectedRow, 4)).toString();
        if(tipo.equals("semplice")){
            jComboBoxTipo.setSelectedIndex(1);
        } else if(tipo.equals("aggregata")){
            jComboBoxTipo.setSelectedIndex(2);
        } else if(tipo.equals("aggregante")){
            jComboBoxTipo.setSelectedIndex(3);
        } else if(tipo.equals("composta")){
            jComboBoxTipo.setSelectedIndex(4);
        } else{
            jComboBoxTipo.setSelectedIndex(5);
        }
        if((jTablePartecipazione.getValueAt(selectedRow, 5)).toString().equals("1")){
            jRadioButtonNav.setSelected(true);
        } else{
            jRadioButtonNav.setSelected(false);
        }
        if((jTablePartecipazione.getValueAt(selectedRow, 6)).toString().equals("1")){
            jRadioButtonQualify.setSelected(true);
        } else{
            jRadioButtonQualify.setSelected(false);
        }
        jTextFieldAssociazione.setText((jTablePartecipazione.getValueAt(selectedRow, 7)).toString());
        jTextFieldClasse.setText((jTablePartecipazione.getValueAt(selectedRow, 8)).toString());
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
        cmdIns = "insert into " + DBConnection.schema + ".VIEW_PARTECIPAZIONE (ID_Partecipazione, Ruolo, cardinalita_Inf, cardinalita_Sup, Tipo,"
                + "Navigabilita, Qualificatore, FK_Associazione, FK_Classe) " 
                + "values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        st = c.prepareStatement(cmdIns);
        if(getCodice().getText().equals("")){
            st.setNull(1, Types.INTEGER);
        } else{
            st.setInt(1, Integer.valueOf(getCodice().getText()));
        }
        if(jTextFieldRole.getText().equals("")){
          st.setNull(2, Types.VARCHAR);
        } else{
          st.setString(2, jTextFieldRole.getText());
        }
        st.setString(3, jTextFieldCardInf.getText());
        st.setString(4, jTextFieldCardSup.getText());
        st.setString(5, jComboBoxTipo.getSelectedItem().toString());
        if(jRadioButtonNav.isSelected()){
            st.setInt(6, 1);
        } else{
            st.setInt(6, 0);
        }
        if(jRadioButtonQualify.isSelected()){
            st.setInt(7, 1);
        } else{
            st.setInt(7, 0);
        }
        st.setInt(8, Integer.valueOf(jTextFieldAssociazione.getText()));
        st.setInt(9, Integer.valueOf(jTextFieldClasse.getText()));
        return st;
    }

    @Override
    protected PreparedStatement opModifica(Connection c)
           throws SQLException{
        PreparedStatement st = null;
        if(!getCodice().getText().equals("")){
            String cmdUp;
            cmdUp = "update " + DBConnection.schema + ".VIEW_PARTECIPAZIONE"
                            + " set Ruolo = ?, " 
                            + "     cardinalita_Inf = ?, "
                            + "     cardinalita_Sup = ?, "
                            + "     Navigabilita = ?, "
                            + "     Qualificatore = ? "
                            + " where ID_Partecipazione = ? ";
            st = c.prepareStatement(cmdUp);
            st.setString(1, jTextFieldRole.getText());
            st.setString(2, jTextFieldCardInf.getText());
            st.setString(3, jTextFieldCardSup.getText());
            if(jRadioButtonNav.isSelected()){
                st.setString(4, "1");
            } else{
                st.setString(4, "0");
            }
            if(jRadioButtonQualify.isSelected()){
                st.setString(5, "1");
            } else{
                st.setString(5, "0");
            }
            st.setInt(6, Integer.valueOf(getCodice().getText()));
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
        String descrizione;
        cmdSrc = "select * from " + DBConnection.schema + ".PARTECIPAZIONE where";
        if (getCodice().getText().length() > 0) {
           cmdSrc += " ID_Partecipazione = ? and";
        }
        if (jTextFieldRole.getText().length() > 0) {
           cmdSrc += " Ruolo = ? and";
        }
        if (jTextFieldCardInf.getText().length() > 0) {
           cmdSrc += " cardinalita_Inf = ? and";
        }
        if (jTextFieldCardSup.getText().length() > 0) {
           cmdSrc += " cardinalita_Sup = ? and";
        }
        if (!jComboBoxTipo.getSelectedItem().toString().equals(" ")) {
           cmdSrc += " Tipo = ? and";
        }
        cmdSrc += " Navigabilita = ? and";
        cmdSrc += " Qualificatore = ? and";
        if (jTextFieldAssociazione.getText().length() > 0) {
           cmdSrc += " FK_Associazione = ? and";
        }
        if (jTextFieldClasse.getText().length() > 0) {
           cmdSrc += " FK_Classe = ? and";
        }
        pat = Pattern.compile("(where|and)$"); 
        match = pat.matcher(cmdSrc);
        cmdSrc = match.replaceAll("");
        cmdSrc += " ORDER BY ID_Partecipazione";
        try {
           st = c.prepareStatement(cmdSrc, ResultSet.TYPE_SCROLL_INSENSITIVE,
                   ResultSet.CONCUR_READ_ONLY); 
        } catch (SQLException e) {
        }
        if(st != null){
            if (getCodice().getText().length() > 0) {
               st.setInt(index++, Integer.valueOf(getCodice().getText()));
            }
            if (jTextFieldRole.getText().length() > 0) {
               st.setString(index++, jTextFieldRole.getText());
            }
            if (jTextFieldCardInf.getText().length() > 0) {
               st.setString(index++, jTextFieldCardInf.getText());
            }
            if (jTextFieldCardSup.getText().length() > 0) {
               st.setString(index++, jTextFieldCardSup.getText());
            }
            if (!jComboBoxTipo.getSelectedItem().toString().equals(" ")) {
               st.setString(index++, jComboBoxTipo.getSelectedItem().toString());
            }
            if (jRadioButtonNav.isSelected()) {
               st.setInt(index++, 1);
            } else{
               st.setInt(index++, 0);
            }
            if (jRadioButtonQualify.isSelected()) {
               st.setInt(index++, 1);
            } else{
               st.setInt(index++, 0);
            }
            if (jTextFieldAssociazione.getText().length() > 0) {
               st.setInt(index++, Integer.valueOf(jTextFieldAssociazione.getText()));
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
            cmdDel = "delete from " + DBConnection.schema + ".VIEW_PARTECIPAZIONE where "
                      + "ID_Partecipazione = ? ";
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
        if(jTablePartecipazione.getRowCount() > 0){
            switch(getModalita()){
                case MODIFICA:
                    jTablePartecipazione.setRowSelectionInterval(selectedRow, selectedRow);
                    break;
                case INSERISCI:
                    jTablePartecipazione.setRowSelectionInterval(jTablePartecipazione.getRowCount()-1, jTablePartecipazione.getRowCount()-1);
                    break;
                case ELIMINA:
                    if(selectedRow < jTablePartecipazione.getRowCount()){
                            jTablePartecipazione.setRowSelectionInterval(selectedRow, selectedRow);
                    } else{
                        jTablePartecipazione.setRowSelectionInterval(selectedRow-1, selectedRow-1);
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

        jLabelRole = new javax.swing.JLabel();
        jLabelCardINF = new javax.swing.JLabel();
        jLabelCardSUP = new javax.swing.JLabel();
        jLabelTipo = new javax.swing.JLabel();
        jLabelNav = new javax.swing.JLabel();
        jLabelQualify = new javax.swing.JLabel();
        jLabelAssociazione = new javax.swing.JLabel();
        jLabelClasse = new javax.swing.JLabel();
        jTextFieldCardInf = new javax.swing.JTextField();
        jTextFieldCardSup = new javax.swing.JTextField();
        jComboBoxTipo = new javax.swing.JComboBox<>();
        jRadioButtonNav = new javax.swing.JRadioButton();
        jRadioButtonQualify = new javax.swing.JRadioButton();
        jTextFieldAssociazione = new javax.swing.JTextField();
        jTextFieldClasse = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTablePartecipazione = new javax.swing.JTable();
        jTextFieldRole = new javax.swing.JTextField();

        setTitle("UML STORAGE - PARTECIPAZIONE");
        setMinimumSize(new java.awt.Dimension(0, 640));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabelRole.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelRole.setText("Ruolo:");

        jLabelCardINF.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelCardINF.setText("Cardinalità INF:");

        jLabelCardSUP.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelCardSUP.setText("Cardinalità SUP:");

        jLabelTipo.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelTipo.setText("Tipo:");

        jLabelNav.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelNav.setText("Navigabilità:");

        jLabelQualify.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelQualify.setText("Qualificatore:");

        jLabelAssociazione.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelAssociazione.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/unina/db2019/operations/fk.png"))); // NOI18N
        jLabelAssociazione.setText("| Associazione:");

        jLabelClasse.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelClasse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/unina/db2019/operations/fk.png"))); // NOI18N
        jLabelClasse.setText("| Classe:");

        jTextFieldCardInf.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldCardInf.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTextFieldCardSup.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldCardSup.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jComboBoxTipo.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jComboBoxTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "semplice", "aggregata", "aggregante", "composta", "componente" }));

        jRadioButtonNav.setSelected(true);

        jTextFieldAssociazione.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldAssociazione.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTextFieldClasse.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldClasse.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTablePartecipazione.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTablePartecipazione.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "null", "null", "null", "null", "null"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTablePartecipazione);

        jTextFieldRole.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldRole.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(108, 108, 108)
                        .addComponent(jLabelRole)
                        .addGap(4, 4, 4)
                        .addComponent(jTextFieldRole, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jLabelCardINF)
                        .addGap(4, 4, 4)
                        .addComponent(jTextFieldCardInf, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(118, 118, 118)
                        .addComponent(jLabelCardSUP)
                        .addGap(4, 4, 4)
                        .addComponent(jTextFieldCardSup, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(118, 118, 118)
                        .addComponent(jLabelTipo)
                        .addGap(4, 4, 4)
                        .addComponent(jComboBoxTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(jLabelNav)
                        .addGap(4, 4, 4)
                        .addComponent(jRadioButtonNav)
                        .addGap(314, 314, 314)
                        .addComponent(jLabelQualify)
                        .addGap(4, 4, 4)
                        .addComponent(jRadioButtonQualify))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabelAssociazione)
                        .addGap(4, 4, 4)
                        .addComponent(jTextFieldAssociazione, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(145, 145, 145)
                        .addComponent(jLabelClasse)
                        .addGap(4, 4, 4)
                        .addComponent(jTextFieldClasse, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 743, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(151, 151, 151)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabelRole))
                    .addComponent(jTextFieldRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldCardInf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldCardSup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelCardINF)
                            .addComponent(jLabelCardSUP))))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabelTipo))
                    .addComponent(jComboBoxTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelNav)
                    .addComponent(jRadioButtonNav)
                    .addComponent(jLabelQualify)
                    .addComponent(jRadioButtonQualify))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldAssociazione, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldClasse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelAssociazione)
                            .addComponent(jLabelClasse))))
                .addGap(29, 29, 29)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        DBOperation.currentlyOpen[8] = null;
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> jComboBoxTipo;
    private javax.swing.JLabel jLabelAssociazione;
    private javax.swing.JLabel jLabelCardINF;
    private javax.swing.JLabel jLabelCardSUP;
    private javax.swing.JLabel jLabelClasse;
    private javax.swing.JLabel jLabelNav;
    private javax.swing.JLabel jLabelQualify;
    private javax.swing.JLabel jLabelRole;
    private javax.swing.JLabel jLabelTipo;
    private javax.swing.JRadioButton jRadioButtonNav;
    private javax.swing.JRadioButton jRadioButtonQualify;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTablePartecipazione;
    private javax.swing.JTextField jTextFieldAssociazione;
    private javax.swing.JTextField jTextFieldCardInf;
    private javax.swing.JTextField jTextFieldCardSup;
    private javax.swing.JTextField jTextFieldClasse;
    private javax.swing.JTextField jTextFieldRole;
    // End of variables declaration//GEN-END:variables
}
