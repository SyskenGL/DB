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
public class SpecialInterf extends DefaultFrame {
    
    private Object bean;

    /**
     * Creates new customizer SpecialInterf
     */
    public SpecialInterf() {
        initComponents();
        this.setLocationRelativeTo(null);
        setFrameTable(jTableSpecialInterf);
        setNomeTabella("SPECIAL_INTERF");
        jTextFieldGenerale.setEnabled(false);
        jTextFieldSpecializzata.setEnabled(false);  
        jTextFieldHier.setEnabled(false);
        jRadioButtonHier.setEnabled(false);
        jRadioButtonHier.setFocusable(false);
        jTableSpecialInterf.setFocusable(false);
        Aggiorna();     
        jTableSpecialInterf.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if(jTableSpecialInterf.getSelectedRow() != -1){
                    getInfo();
                }
            }
        }); 
    }
    
    public void setObject(Object bean) {
        this.bean = bean;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelGenerale = new javax.swing.JLabel();
        jLabelSpecializzata = new javax.swing.JLabel();
        jTextFieldGenerale = new javax.swing.JTextField();
        jTextFieldSpecializzata = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableSpecialInterf = new javax.swing.JTable();
        jLabelHier = new javax.swing.JLabel();
        jTextFieldHier = new javax.swing.JTextField();
        jRadioButtonHier = new javax.swing.JRadioButton();

        setTitle("UML STORAGE - SPECIAL. INTERFACCE");
        setMinimumSize(new java.awt.Dimension(0, 530));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabelGenerale.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelGenerale.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/unina/db2019/operations/fk.png"))); // NOI18N
        jLabelGenerale.setText("| Generale:");

        jLabelSpecializzata.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelSpecializzata.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/unina/db2019/operations/fk.png"))); // NOI18N
        jLabelSpecializzata.setText("| Specializzata:");

        jTextFieldGenerale.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldGenerale.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTextFieldSpecializzata.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldSpecializzata.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTableSpecialInterf.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTableSpecialInterf.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTableSpecialInterf);

        jLabelHier.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelHier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/unina/db2019/operations/hier.png"))); // NOI18N
        jLabelHier.setText("| Gerarchica:");

        jTextFieldHier.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldHier.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jRadioButtonHier.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jRadioButtonHier.setText("Root");
        jRadioButtonHier.setToolTipText("");
        jRadioButtonHier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonHierActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelSpecializzata)
                            .addComponent(jLabelGenerale)
                            .addComponent(jLabelHier))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTextFieldGenerale, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                .addComponent(jTextFieldSpecializzata))
                            .addComponent(jTextFieldHier, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jRadioButtonHier))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 531, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(206, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(154, 154, 154)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelGenerale)
                    .addComponent(jTextFieldGenerale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelSpecializzata)
                    .addComponent(jTextFieldSpecializzata, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelHier)
                        .addComponent(jTextFieldHier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jRadioButtonHier))
                .addGap(29, 29, 29)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButtonHierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonHierActionPerformed
        setEnabledjTextFieldPK(!isJTextFieldPKEnabled());
        jTextFieldGenerale.setEnabled(!jTextFieldGenerale.isEnabled());
        jTextFieldSpecializzata.setEnabled(!jTextFieldSpecializzata.isEnabled());
        jTextFieldHier.setEnabled(!jTextFieldHier.isEnabled());
    }//GEN-LAST:event_jRadioButtonHierActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        DBOperation.currentlyOpen[14] = null;
    }//GEN-LAST:event_formWindowClosing

    @Override
    public void setModalita(int modo){
        super.setModalita(modo);
        switch (modo) {
           case INSERISCI:
              setEnabledjTextFieldPK(true);
              jTextFieldGenerale.setEnabled(true);
              jTextFieldSpecializzata.setEnabled(true);
              jRadioButtonHier.setEnabled(false);
              jTextFieldHier.setEnabled(false);
              break;
           case MODIFICA:
              setEnabledjTextFieldPK(false);
              jTextFieldGenerale.setEnabled(true);
              jTextFieldSpecializzata.setEnabled(true);
              jRadioButtonHier.setEnabled(false);
              jTextFieldHier.setEnabled(false);
              break;
           case ELIMINA:
              setEnabledjTextFieldPK(false);
              jTextFieldGenerale.setEnabled(false);
              jTextFieldSpecializzata.setEnabled(false);
              jRadioButtonHier.setEnabled(false);
              jTextFieldHier.setEnabled(false);
              break;
           case RICERCA:
              if(jRadioButtonHier.isSelected()){
                  setEnabledjTextFieldPK(false);
                  jTextFieldGenerale.setEnabled(false);
                  jTextFieldSpecializzata.setEnabled(false);
                  jTextFieldHier.setEnabled(true);
              } else{
                  setEnabledjTextFieldPK(true);
                  jTextFieldGenerale.setEnabled(true);
                  jTextFieldSpecializzata.setEnabled(true);
                  jTextFieldHier.setEnabled(false);
              }
              jRadioButtonHier.setEnabled(true);
              break;
        }
    }

    @Override
    protected PreparedStatement selectAll(){
        Connection con;
        PreparedStatement st = null;
        super.selectAll();
        query = query + "ORDER BY ID_SPECIALINTERF ";
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
        jTextFieldGenerale.setText("");
        jTextFieldSpecializzata.setText("");
        if(jRadioButtonHier.isSelected() && getModalita() == super.RICERCA){
           setEnabledjTextFieldPK(true);
           jTextFieldGenerale.setEnabled(true);
           jTextFieldSpecializzata.setEnabled(true);
           jRadioButtonHier.setSelected(false);
           jTextFieldHier.setEnabled(false);
        }
        jTextFieldHier.setText("");
        super.setFocusPK();
    }

    @Override
    protected void getInfo(){
        selectedRow = jTableSpecialInterf.getSelectedRow();
        setJTextFieldPK((jTableSpecialInterf.getValueAt(selectedRow, 0)).toString());
        jTextFieldGenerale.setText((jTableSpecialInterf.getValueAt(selectedRow, 1)).toString());
        jTextFieldSpecializzata.setText((jTableSpecialInterf.getValueAt(selectedRow, 2)).toString());
        jTextFieldHier.setText("");
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
        cmdIns = "insert into " + DBConnection.schema + ".VIEW_SPECIAL_INTERF (ID_SpecialInterf,"
                + "FK_Generale, FK_Specializzata)  values(?,?,?)";
        st = c.prepareStatement(cmdIns);
        if(getCodice().getText().equals("")){
            st.setNull(1, Types.INTEGER);
        } else{
            st.setInt(1, Integer.valueOf(getCodice().getText()));
        }
        st.setInt(2, Integer.valueOf(jTextFieldGenerale.getText()));
        st.setInt(3, Integer.valueOf(jTextFieldSpecializzata.getText()));
        return st;
    }

    @Override
    protected PreparedStatement opModifica(Connection c)
           throws SQLException{
        PreparedStatement st = null;
        if(!getCodice().getText().equals("")){
            String cmdUp;
            cmdUp = "update " + DBConnection.schema + ".VIEW_SPECIAL_INTERF "
                            + " set FK_Generale = ?, " 
                            + "     FK_Specializzata = ? "
                            + " where ID_SpecialInterf = ? ";
            st = c.prepareStatement(cmdUp);
            st.setInt(1, Integer.valueOf(jTextFieldGenerale.getText()));
            st.setInt(2, Integer.valueOf(jTextFieldSpecializzata.getText()));
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
        String descrizione;
        if(!jRadioButtonHier.isSelected() || jTextFieldHier.getText().length() == 0){
            cmdSrc = "select * from " + DBConnection.schema + ".SPECIAL_INTERF where";
            if (getCodice().getText().length() > 0) {
               cmdSrc += " ID_SpecialInterf = ? and";
            }
            if (jTextFieldGenerale.getText().length() > 0) {
               cmdSrc += " FK_Generale = ? and";
            }
            if (jTextFieldSpecializzata.getText().length() > 0) {
               cmdSrc += " FK_Specializzata = ? ";
            }
            pat = Pattern.compile("(where|and)$"); 
            match = pat.matcher(cmdSrc);
            cmdSrc = match.replaceAll("");
            cmdSrc += " ORDER BY ID_SPECIALINTERF";
            try {
               st = c.prepareStatement(cmdSrc, ResultSet.TYPE_SCROLL_INSENSITIVE,
                       ResultSet.CONCUR_READ_ONLY); 
            } catch (SQLException e) {
            }
            if(st != null){
                if (getCodice().getText().length() > 0) {
                   st.setInt(index++, Integer.valueOf(getCodice().getText()));
                }
                if (jTextFieldGenerale.getText().length() > 0) {
                    st.setInt(index++, Integer.valueOf(jTextFieldGenerale.getText()));
                }
                if (jTextFieldSpecializzata.getText().length() > 0) {
                    st.setInt(index++, Integer.valueOf(jTextFieldSpecializzata.getText()));
                }
            }
        } else{
            cmdSrc = "select * from " + DBConnection.schema + ".SPECIAL_INTERF "
                   + "connect by FK_Generale = PRIOR FK_Specializzata "
                   + "start with FK_Generale = ?";
            try {
               st = c.prepareStatement(cmdSrc, ResultSet.TYPE_SCROLL_INSENSITIVE,
                       ResultSet.CONCUR_READ_ONLY); 
            } catch (SQLException e) {
            }
            if(st != null){
                st.setInt(index++, Integer.valueOf(jTextFieldHier.getText()));
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
            cmdDel = "delete from " + DBConnection.schema + ".SPECIAL_INTERF where "
                      + "ID_SpecialInterf = ? ";
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
        if(jTableSpecialInterf.getRowCount() > 0){
            switch(getModalita()){
                case MODIFICA:
                    jTableSpecialInterf.setRowSelectionInterval(selectedRow, selectedRow);
                    break;
                case INSERISCI:
                    jTableSpecialInterf.setRowSelectionInterval(jTableSpecialInterf.getRowCount()-1, jTableSpecialInterf.getRowCount()-1);
                    break;
                case ELIMINA:
                    if(selectedRow < jTableSpecialInterf.getRowCount()){
                            jTableSpecialInterf.setRowSelectionInterval(selectedRow, selectedRow);
                    } else{
                        jTableSpecialInterf.setRowSelectionInterval(selectedRow-1, selectedRow-1);
                    }     
            }
        }    
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabelGenerale;
    private javax.swing.JLabel jLabelHier;
    private javax.swing.JLabel jLabelSpecializzata;
    private javax.swing.JRadioButton jRadioButtonHier;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableSpecialInterf;
    private javax.swing.JTextField jTextFieldGenerale;
    private javax.swing.JTextField jTextFieldHier;
    private javax.swing.JTextField jTextFieldSpecializzata;
    // End of variables declaration//GEN-END:variables
}