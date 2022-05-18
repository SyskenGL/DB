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

/**
 *
 * @author Sysken
 */
public class Attributo extends DefaultFrame {
    
    private boolean isLike = true;
    private Object bean;

    /**
     * Creates new customizer Attributo
     */
    public Attributo() {
        initComponents();
        this.setLocationRelativeTo(null);
        setFrameTable(jTableAttributo);
        setNomeTabella("ATTRIBUTO");
        jTextFieldNome.setEnabled(false);
        jTextFieldDescrizione.setEnabled(false);
        jTextFieldCardInf.setEnabled(false);
        jTextFieldCardSup.setEnabled(false);
        jRadioButtonEqual.setVisible(false);
        jTableAttributo.setFocusable(false);
        jRadioButtonEqual.setFocusable(false);
        jComboBoxScope.setEnabled(false);
        jComboBoxScope.setFocusable(false);
        jTextFieldClasse.setEnabled(false);
        jTextFieldTipo.setEnabled(false);
        Aggiorna();     
        jTableAttributo.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if(jTableAttributo.getSelectedRow() != -1){
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
              jTextFieldDescrizione.setEnabled(true);
              jTextFieldCardInf.setEnabled(true);
              jTextFieldCardSup.setEnabled(true);
              jComboBoxScope.setEnabled(true);
              jRadioButtonEqual.setVisible(false);
              jTextFieldClasse.setEnabled(true);
              jTextFieldTipo.setEnabled(true);         
              break;
           case MODIFICA:
              setEnabledjTextFieldPK(false);
              jTextFieldNome.setEnabled(true);
              jTextFieldDescrizione.setEnabled(true);
              jTextFieldCardInf.setEnabled(true);
              jTextFieldCardSup.setEnabled(true);
              jComboBoxScope.setEnabled(true);
              jRadioButtonEqual.setVisible(false);
              jTextFieldClasse.setEnabled(true);
              jTextFieldTipo.setEnabled(true);                 
              break;
           case ELIMINA:
              setEnabledjTextFieldPK(false);
              jTextFieldNome.setEnabled(false);
              jTextFieldDescrizione.setEnabled(false);
              jTextFieldCardInf.setEnabled(false);
              jTextFieldCardSup.setEnabled(false);
              jComboBoxScope.setEnabled(false);
              jRadioButtonEqual.setVisible(false);
              jTextFieldClasse.setEnabled(false);
              jTextFieldTipo.setEnabled(false);                
              break;
           case RICERCA:      
              setEnabledjTextFieldPK(true);
              jTextFieldNome.setEnabled(true);
              jTextFieldDescrizione.setEnabled(true);
              jTextFieldCardInf.setEnabled(true);
              jTextFieldCardSup.setEnabled(true);
              jComboBoxScope.setEnabled(true);
              jRadioButtonEqual.setVisible(true);
              jTextFieldClasse.setEnabled(true);
              jTextFieldTipo.setEnabled(true);      
              break;
        }
    }

    @Override
    protected PreparedStatement selectAll(){
        Connection con;
        PreparedStatement st = null;
        super.selectAll();
        query = query + "ORDER BY ID_ATTRIBUTO ";
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
        jTextFieldDescrizione.setText("");
        jComboBoxScope.setSelectedIndex(0);
        jTextFieldCardInf.setText("");
        jTextFieldCardSup.setText("");
        jTextFieldClasse.setText("");
        jTextFieldTipo.setText("");
        jTableAttributo.clearSelection();
        jRadioButtonEqual.setSelected(true);
        super.setFocusPK();
    }

    @Override
    protected void getInfo(){
        selectedRow = jTableAttributo.getSelectedRow();
        setJTextFieldPK((jTableAttributo.getValueAt(selectedRow, 0)).toString());
        jTextFieldNome.setText((String)jTableAttributo.getValueAt(selectedRow, 1));
        jTextFieldDescrizione.setText((String)jTableAttributo.getValueAt(selectedRow, 2));
        jTextFieldCardInf.setText((String)jTableAttributo.getValueAt(selectedRow, 3));
        jTextFieldCardSup.setText((String)jTableAttributo.getValueAt(selectedRow, 4));
        String scope = (String)jTableAttributo.getValueAt(selectedRow, 5);
        if(scope.equals("public")){
            jComboBoxScope.setSelectedIndex(1);
        } else if(scope.equals("package")){
            jComboBoxScope.setSelectedIndex(2);
        } else if(scope.equals("protected")){
            jComboBoxScope.setSelectedIndex(3);
        } else {
            jComboBoxScope.setSelectedIndex(4);
        }
        jTextFieldClasse.setText((jTableAttributo.getValueAt(selectedRow, 6)).toString());
        jTextFieldTipo.setText((jTableAttributo.getValueAt(selectedRow, 7)).toString());
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
        cmdIns = "insert into " + DBConnection.schema + ".ATTRIBUTO (ID_Attributo,Nome,"
                + "Descrizione, cardinalita_Inf, cardinalita_Sup, h_scope, FK_Classe, FK_Tipo)  values(?,?,?,?,?,?,?,?)";
        st = c.prepareStatement(cmdIns);
        if(getCodice().getText().equals("")){
            st.setNull(1, Types.INTEGER);
        } else{
            st.setInt(1, Integer.valueOf(getCodice().getText()));
        }
        st.setString(2, jTextFieldNome.getText());
        if(jTextFieldDescrizione.getText().equals("")){
          st.setNull(3, Types.VARCHAR);
        } else{
          st.setString(3, jTextFieldDescrizione.getText());
        }
        st.setString(4, jTextFieldCardInf.getText());
        st.setString(5, jTextFieldCardSup.getText());
        st.setString(6, jComboBoxScope.getSelectedItem().toString());
        st.setInt(7, Integer.valueOf(jTextFieldClasse.getText()));
        st.setInt(8, Integer.valueOf(jTextFieldTipo.getText()));
        return st;
    }

    @Override
    protected PreparedStatement opModifica(Connection c)
           throws SQLException{
        PreparedStatement st = null;
        if(!getCodice().getText().equals("")){
            String cmdUp;
            cmdUp = "update " + DBConnection.schema + ".ATTRIBUTO "
                            + " set Nome = ?, " 
                            + "     Descrizione = ?, "
                            + "     cardinalita_Inf = ?, "
                            + "     cardinalita_Sup = ?, "
                            + "     h_scope = ?, "
                            + "     FK_Classe = ?, "
                            + "     FK_Tipo = ? "
                            + " where ID_Attributo = ? ";
            st = c.prepareStatement(cmdUp);
            st.setString(1, jTextFieldNome.getText());
            st.setString(2, jTextFieldDescrizione.getText());
            st.setString(3, jTextFieldCardInf.getText());
            st.setString(4, jTextFieldCardSup.getText());
            st.setString(5, jComboBoxScope.getSelectedItem().toString());
            st.setInt(6, Integer.valueOf(jTextFieldClasse.getText()));
            st.setInt(7, Integer.valueOf(jTextFieldTipo.getText()));
            st.setInt(8, Integer.valueOf(getCodice().getText()));
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
        cmdSrc = "select * from " + DBConnection.schema + ".ATTRIBUTO where";
        if (getCodice().getText().length() > 0) {
           cmdSrc += " ID_Attributo = ? and";
        }
        if (jTextFieldNome.getText().length() > 0) {
           cmdSrc += " Nome = ? and";
        }
        descrizione = jTextFieldDescrizione.getText();
        if (descrizione.length() > 0) {
            if(isLike){
                cmdSrc += " Descrizione like ? and";
                   descrizione = "%"+descrizione+"%";  
            } else{
                cmdSrc += " Descrizione = ? and";
            }
        }
        if (jTextFieldCardInf.getText().length() > 0) {
           cmdSrc += " cardinalita_Inf = ? and";
        }
        if (jTextFieldCardSup.getText().length() > 0) {
           cmdSrc += " cardinalita_Sup = ? and";
        }
        if(!jComboBoxScope.getSelectedItem().toString().equals(" ")){
            cmdSrc += " h_scope = ? and";
        }
        if (jTextFieldClasse.getText().length() > 0) {
           cmdSrc += " FK_Classe = ? and";
        }
        if (jTextFieldTipo.getText().length() > 0) {
           cmdSrc += " FK_Tipo = ? and";
        }
        pat = Pattern.compile("(where|and)$"); 
        match = pat.matcher(cmdSrc);
        cmdSrc = match.replaceAll("");
        cmdSrc += " ORDER BY ID_ATTRIBUTO";
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
            if (descrizione.length() > 0) {
                st.setString(index++, descrizione);
            }
            if (jTextFieldCardInf.getText().length() > 0) {
                st.setString(index++, jTextFieldCardInf.getText());
            }
            if (jTextFieldCardSup.getText().length() > 0) {
                st.setString(index++, jTextFieldCardSup.getText());
            }
            if(!jComboBoxScope.getSelectedItem().toString().equals(" ")){
                st.setString(index++, jComboBoxScope.getSelectedItem().toString());
            }
            if (jTextFieldClasse.getText().length() > 0) {
                st.setInt(index++, Integer.valueOf(jTextFieldClasse.getText()));
            }
            if (jTextFieldTipo.getText().length() > 0) {
                st.setInt(index++, Integer.valueOf(jTextFieldTipo.getText()));
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
            cmdDel = "delete from " + DBConnection.schema + ".ATTRIBUTO where "
                      + "ID_Attributo = ? ";
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
        if(jTableAttributo.getRowCount() > 0){
            switch(getModalita()){
                case MODIFICA:
                    jTableAttributo.setRowSelectionInterval(selectedRow, selectedRow);
                    break;
                case INSERISCI:
                    jTableAttributo.setRowSelectionInterval(jTableAttributo.getRowCount()-1, jTableAttributo.getRowCount()-1);
                    break;
                case ELIMINA:
                    if(selectedRow < jTableAttributo.getRowCount()){
                            jTableAttributo.setRowSelectionInterval(selectedRow, selectedRow);
                    } else{
                        jTableAttributo.setRowSelectionInterval(selectedRow-1, selectedRow-1);
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
        jLabelDescrizione = new javax.swing.JLabel();
        jLabelCardInf = new javax.swing.JLabel();
        jLabelCardSup = new javax.swing.JLabel();
        jLabelScope = new javax.swing.JLabel();
        jLabelClasse = new javax.swing.JLabel();
        jLabelTipo = new javax.swing.JLabel();
        jTextFieldNome = new javax.swing.JTextField();
        jTextFieldDescrizione = new javax.swing.JTextField();
        jRadioButtonEqual = new javax.swing.JRadioButton();
        jTextFieldCardInf = new javax.swing.JTextField();
        jTextFieldCardSup = new javax.swing.JTextField();
        jComboBoxScope = new javax.swing.JComboBox<>();
        jTextFieldClasse = new javax.swing.JTextField();
        jTextFieldTipo = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableAttributo = new javax.swing.JTable();

        setTitle("UML STORAGE - ATTRIBUTO");
        setMinimumSize(new java.awt.Dimension(0, 630));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabelNome.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelNome.setText("Nome:");

        jLabelDescrizione.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelDescrizione.setText("Descrizione:");

        jLabelCardInf.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelCardInf.setText("Cardinalità INF:");

        jLabelCardSup.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelCardSup.setText("Cardinalità SUP:");

        jLabelScope.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelScope.setText("Scope:");

        jLabelClasse.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelClasse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/unina/db2019/operations/fk.png"))); // NOI18N
        jLabelClasse.setText("| Classe:");

        jLabelTipo.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelTipo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/unina/db2019/operations/fk.png"))); // NOI18N
        jLabelTipo.setText("| Tipo:");

        jTextFieldNome.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldNome.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTextFieldDescrizione.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldDescrizione.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jRadioButtonEqual.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jRadioButtonEqual.setSelected(true);
        jRadioButtonEqual.setText("Like");
        jRadioButtonEqual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonEqualActionPerformed(evt);
            }
        });

        jTextFieldCardInf.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldCardInf.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTextFieldCardSup.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldCardSup.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jComboBoxScope.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jComboBoxScope.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "public", "package", "protected", "private" }));

        jTextFieldClasse.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldClasse.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTextFieldTipo.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldTipo.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTextFieldTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldTipoActionPerformed(evt);
            }
        });

        jTableAttributo.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTableAttributo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "null", "null", "null", "null"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableAttributo);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(97, 97, 97)
                        .addComponent(jLabelNome)
                        .addGap(4, 4, 4)
                        .addComponent(jTextFieldNome, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabelCardInf)
                        .addGap(4, 4, 4)
                        .addComponent(jTextFieldCardInf, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(61, 61, 61)
                        .addComponent(jLabelCardSup)
                        .addGap(4, 4, 4)
                        .addComponent(jTextFieldCardSup, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addComponent(jLabelScope)
                        .addGap(4, 4, 4)
                        .addComponent(jComboBoxScope, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(jLabelClasse)
                        .addGap(4, 4, 4)
                        .addComponent(jTextFieldClasse, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(102, 102, 102)
                        .addComponent(jLabelTipo)
                        .addGap(4, 4, 4)
                        .addComponent(jTextFieldTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 746, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(jLabelDescrizione)
                        .addGap(4, 4, 4)
                        .addComponent(jTextFieldDescrizione, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jRadioButtonEqual)))
                .addContainerGap(111, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(144, 144, 144)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabelNome))
                    .addComponent(jTextFieldNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabelDescrizione))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldDescrizione, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadioButtonEqual))))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldCardInf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldCardSup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelCardInf)
                            .addComponent(jLabelCardSup))))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabelScope))
                    .addComponent(jComboBoxScope, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldClasse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelClasse)
                            .addComponent(jLabelTipo))))
                .addGap(29, 29, 29)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButtonEqualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonEqualActionPerformed
        isLike = !isLike;
    }//GEN-LAST:event_jRadioButtonEqualActionPerformed

    private void jTextFieldTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldTipoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldTipoActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        DBOperation.currentlyOpen[4] = null;
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> jComboBoxScope;
    private javax.swing.JLabel jLabelCardInf;
    private javax.swing.JLabel jLabelCardSup;
    private javax.swing.JLabel jLabelClasse;
    private javax.swing.JLabel jLabelDescrizione;
    private javax.swing.JLabel jLabelNome;
    private javax.swing.JLabel jLabelScope;
    private javax.swing.JLabel jLabelTipo;
    private javax.swing.JRadioButton jRadioButtonEqual;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableAttributo;
    private javax.swing.JTextField jTextFieldCardInf;
    private javax.swing.JTextField jTextFieldCardSup;
    private javax.swing.JTextField jTextFieldClasse;
    private javax.swing.JTextField jTextFieldDescrizione;
    private javax.swing.JTextField jTextFieldNome;
    private javax.swing.JTextField jTextFieldTipo;
    // End of variables declaration//GEN-END:variables
}
