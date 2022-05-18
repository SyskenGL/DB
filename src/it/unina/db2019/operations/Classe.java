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
public class Classe extends DefaultFrame {
    
    private boolean isLike = true;
    private Object bean;

    /**
     * Creates new customizer Classe
     */
    public Classe() {
        initComponents();
        this.setLocationRelativeTo(null);
        setFrameTable(jTableClasse);
        setNomeTabella("CLASSE");
        jTextFieldAssociazione.setEnabled(false);
        jTextFieldCD.setEnabled(false);
        jTextFieldDescrizione.setEnabled(false);
        jTextFieldGenerale.setEnabled(false);
        jTextFieldNome.setEnabled(false);
        jRadioButtonEqual.setVisible(false);
        jTableClasse.setFocusable(false);
        jRadioButtonEqual.setFocusable(false);
        jComboBoxType.setEnabled(false);
        jComboBoxType.setFocusable(false);
        jTextFieldHier.setEnabled(false);
        jRadioButtonHier.setEnabled(false);
        jRadioButtonHier.setFocusable(false);
        Aggiorna();     
        jTableClasse.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if(jTableClasse.getSelectedRow() != -1){
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
              jTextFieldAssociazione.setEnabled(true);
              jTextFieldCD.setEnabled(true);
              jTextFieldDescrizione.setEnabled(true);
              jTextFieldGenerale.setEnabled(true);
              jTextFieldNome.setEnabled(true);
              jRadioButtonEqual.setVisible(false);
              jComboBoxType.setEnabled(true);
              jRadioButtonHier.setEnabled(false);
              jTextFieldHier.setEnabled(false);              
              break;
           case MODIFICA:
              setEnabledjTextFieldPK(false);
              jTextFieldAssociazione.setEnabled(true);
              jTextFieldCD.setEnabled(false);      // Nel database il muting del CD non è permesso
              jTextFieldDescrizione.setEnabled(true);
              jTextFieldGenerale.setEnabled(true);
              jTextFieldNome.setEnabled(true);
              jRadioButtonEqual.setVisible(false);
              jComboBoxType.setEnabled(false);     // Nel database il muting del tipo non è permesso
              jRadioButtonHier.setEnabled(false);
              jTextFieldHier.setEnabled(false);              
              break;
           case ELIMINA:
              setEnabledjTextFieldPK(false);
              jTextFieldAssociazione.setEnabled(false);
              jTextFieldCD.setEnabled(false);
              jTextFieldDescrizione.setEnabled(false);
              jTextFieldGenerale.setEnabled(false);
              jTextFieldNome.setEnabled(false);
              jRadioButtonEqual.setVisible(false);
              jComboBoxType.setEnabled(false);
              jRadioButtonHier.setEnabled(false);
              jTextFieldHier.setEnabled(false);              
              break;
           case RICERCA:      
              if(jRadioButtonHier.isSelected()){
                setEnabledjTextFieldPK(false);
                jTextFieldAssociazione.setEnabled(false);
                jTextFieldCD.setEnabled(false);
                jTextFieldDescrizione.setEnabled(false);
                jTextFieldGenerale.setEnabled(false);
                jTextFieldNome.setEnabled(false);
                jRadioButtonEqual.setVisible(true);
                jRadioButtonEqual.setEnabled(false);
                jComboBoxType.setEnabled(false);
                jRadioButtonHier.setEnabled(true);
                jTextFieldHier.setEnabled(true);  
              } else{
                setEnabledjTextFieldPK(true);
                jTextFieldAssociazione.setEnabled(true);
                jTextFieldCD.setEnabled(true);
                jTextFieldDescrizione.setEnabled(true);
                jTextFieldGenerale.setEnabled(true);
                jTextFieldNome.setEnabled(true);
                jRadioButtonEqual.setVisible(true);
                jRadioButtonEqual.setEnabled(true);
                jComboBoxType.setEnabled(true);
                jRadioButtonHier.setEnabled(false);
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
        query = query + "ORDER BY ID_CLASSE ";
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
        jComboBoxType.setSelectedIndex(0);
        jTextFieldCD.setText("");
        jTextFieldAssociazione.setText("");
        jTextFieldGenerale.setText("");
        jTableClasse.clearSelection();
        jRadioButtonEqual.setSelected(true);
        if(jRadioButtonHier.isSelected() && getModalita() == super.RICERCA){
            setEnabledjTextFieldPK(true);
            jTextFieldNome.setEnabled(true);
            jTextFieldDescrizione.setEnabled(true);
            jComboBoxType.setEnabled(true);
            jTextFieldCD.setEnabled(true);
            jTextFieldAssociazione.setEnabled(true);
            jTextFieldGenerale.setEnabled(true);
            jTableClasse.setEnabled(true);
            jRadioButtonEqual.setEnabled(true);
            jRadioButtonHier.setSelected(false);
            jTextFieldHier.setEnabled(false);
        }
        jTextFieldHier.setText("");
        super.setFocusPK();
    }

    @Override
    protected void getInfo(){
        selectedRow = jTableClasse.getSelectedRow();
        setJTextFieldPK((jTableClasse.getValueAt(selectedRow, 0)).toString());
        jTextFieldNome.setText((String)jTableClasse.getValueAt(selectedRow, 1));
        jTextFieldDescrizione.setText((String)jTableClasse.getValueAt(selectedRow, 2));
        String type = (jTableClasse.getValueAt(selectedRow, 3)).toString();
        if(type.equals("semplice")){
            jComboBoxType.setSelectedIndex(1);
        } else if(type.equals("associazione")){
            jComboBoxType.setSelectedIndex(2);
        } else if(type.equals("parametrica")){
            jComboBoxType.setSelectedIndex(3);
        } else {
            jComboBoxType.setSelectedIndex(4);
        }
        jTextFieldCD.setText((jTableClasse.getValueAt(selectedRow, 4)).toString());
        if((jTableClasse.getValueAt(selectedRow, 5) != null)){
            jTextFieldAssociazione.setText((jTableClasse.getValueAt(selectedRow, 5)).toString());
        } else{
            jTextFieldAssociazione.setText("");
        }
        if((jTableClasse.getValueAt(selectedRow, 6)) != null){
            jTextFieldGenerale.setText((jTableClasse.getValueAt(selectedRow, 6)).toString());
        } else{
            jTextFieldGenerale.setText("");
        }
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
        cmdIns = "insert into " + DBConnection.schema + ".CLASSE (ID_Classe,Nome,"
                + "Descrizione, Tipo, FK_ClassDiagram, FK_Associazione, FK_Generale)  values(?,?,?,?,?,?,?)";
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
        st.setString(4, jComboBoxType.getSelectedItem().toString());
        st.setInt(5, Integer.valueOf(jTextFieldCD.getText()));
        if(jTextFieldAssociazione.getText().equals("")){
            st.setNull(6, Types.INTEGER);
        } else{
            st.setInt(6, Integer.valueOf(jTextFieldAssociazione.getText()));
        }
        if(jTextFieldGenerale.getText().equals("")){
            st.setNull(7, Types.INTEGER);
        } else{
            st.setInt(7, Integer.valueOf(jTextFieldGenerale.getText()));
        }
        return st;
    }

    @Override
    protected PreparedStatement opModifica(Connection c)
           throws SQLException{
        PreparedStatement st = null;
        if(!getCodice().getText().equals("")){
            String cmdUp;
            cmdUp = "update " + DBConnection.schema + ".VIEW_CLASSE "
                            + " set Nome = ?, " 
                            + "     Descrizione = ?, "
                            + "     FK_Associazione = ?, "
                            + "     FK_Generale = ? "
                            + " where ID_Classe = ? ";
            st = c.prepareStatement(cmdUp);
            st.setString(1, jTextFieldNome.getText());
            st.setString(2, jTextFieldDescrizione.getText());
            if(jTextFieldAssociazione.getText().equals("")){
                  st.setNull(3, Types.INTEGER);
            } else{
                  st.setInt(3, Integer.valueOf(jTextFieldAssociazione.getText()));
            }
            if(jTextFieldGenerale.getText().equals("")){
                  st.setNull(4, Types.INTEGER);
            } else{
                  st.setInt(4, Integer.valueOf(jTextFieldGenerale.getText()));
            }
            st.setInt(5, Integer.valueOf(getCodice().getText()));
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
            cmdSrc = "select * from " + DBConnection.schema + ".CLASSE where";
            if (getCodice().getText().length() > 0) {
               cmdSrc += " ID_Classe = ? and";
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
            if (!jComboBoxType.getSelectedItem().toString().equals(" ")) {
               cmdSrc += " Tipo = ? and";
            }
            if (jTextFieldCD.getText().length() > 0) {
               cmdSrc += " FK_ClassDiagram = ? and";
            }
            if (jTextFieldAssociazione.getText().length() > 0) {
               cmdSrc += " FK_Associazione = ? and";
            }
            if (jTextFieldGenerale.getText().length() > 0) {
               cmdSrc += " FK_Generale = ? and";
            }
            pat = Pattern.compile("(where|and)$"); 
            match = pat.matcher(cmdSrc);
            cmdSrc = match.replaceAll("");
            cmdSrc += " ORDER BY ID_CLASSE";
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
                if (!jComboBoxType.getSelectedItem().toString().equals(" ")) {
                   st.setString(index++, jComboBoxType.getSelectedItem().toString());
                }
                if (jTextFieldCD.getText().length() > 0) {
                   st.setInt(index++, Integer.valueOf(jTextFieldCD.getText()));
                }
                if (jTextFieldAssociazione.getText().length() > 0) {
                   st.setInt(index++, Integer.valueOf(jTextFieldAssociazione.getText()));
                }
                if (jTextFieldGenerale.getText().length() > 0) {
                   st.setInt(index++, Integer.valueOf(jTextFieldGenerale.getText()));
                }
            }
        } else{
            cmdSrc = "select * from " + DBConnection.schema + ".CLASSE "
                   + "connect by FK_Generale = PRIOR ID_Classe "
                   + "start with ID_Classe = ?";
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
            cmdDel = "delete from " + DBConnection.schema + ".VIEW_CLASSE where "
                      + "ID_Classe = ? ";
            st = c.prepareStatement(cmdDel);
            st.setInt(1, Integer.valueOf(getCodice().getText()));
        } else{
            JOptionPane.showMessageDialog(this, "Nessun record selezionato!",
                                          "Errore", JOptionPane.ERROR_MESSAGE);
        }
        return st;
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
        jLabelTipo = new javax.swing.JLabel();
        jLabelCD = new javax.swing.JLabel();
        jLabelAssociazione = new javax.swing.JLabel();
        jLabelGenerale = new javax.swing.JLabel();
        jTextFieldNome = new javax.swing.JTextField();
        jComboBoxType = new javax.swing.JComboBox<>();
        jTextFieldDescrizione = new javax.swing.JTextField();
        jTextFieldCD = new javax.swing.JTextField();
        jTextFieldAssociazione = new javax.swing.JTextField();
        jTextFieldGenerale = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableClasse = new javax.swing.JTable();
        jRadioButtonEqual = new javax.swing.JRadioButton();
        jLabelHier = new javax.swing.JLabel();
        jTextFieldHier = new javax.swing.JTextField();
        jRadioButtonHier = new javax.swing.JRadioButton();

        setTitle("UML STORAGE - CLASSE");
        setMinimumSize(new java.awt.Dimension(0, 760));
        setPreferredSize(new java.awt.Dimension(856, 750));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabelNome.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelNome.setText("Nome:");

        jLabelDescrizione.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelDescrizione.setText("Descizione:");

        jLabelTipo.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelTipo.setText("Tipo:");

        jLabelCD.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelCD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/unina/db2019/operations/fk.png"))); // NOI18N
        jLabelCD.setText("| Class Diagram:");
        jLabelCD.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabelAssociazione.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelAssociazione.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/unina/db2019/operations/fk.png"))); // NOI18N
        jLabelAssociazione.setText("| Associazione:");

        jLabelGenerale.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelGenerale.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/unina/db2019/operations/fk.png"))); // NOI18N
        jLabelGenerale.setText("| Generale:");

        jTextFieldNome.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldNome.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTextFieldNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNomeActionPerformed(evt);
            }
        });

        jComboBoxType.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jComboBoxType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "semplice", "associazione", "parametrica", "astratta" }));
        jComboBoxType.setBorder(null);

        jTextFieldDescrizione.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldDescrizione.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTextFieldCD.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldCD.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTextFieldAssociazione.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldAssociazione.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTextFieldGenerale.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldGenerale.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTableClasse.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTableClasse.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTableClasse);

        jRadioButtonEqual.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jRadioButtonEqual.setSelected(true);
        jRadioButtonEqual.setText("Like");
        jRadioButtonEqual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonEqualActionPerformed(evt);
            }
        });

        jLabelHier.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelHier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/unina/db2019/operations/hier.png"))); // NOI18N
        jLabelHier.setText("| Gerarchica:");

        jTextFieldHier.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldHier.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jRadioButtonHier.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jRadioButtonHier.setText("Root");
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
                .addGap(121, 121, 121)
                .addComponent(jLabelNome)
                .addGap(4, 4, 4)
                .addComponent(jTextFieldNome, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(91, 91, 91)
                .addComponent(jLabelDescrizione)
                .addGap(4, 4, 4)
                .addComponent(jTextFieldDescrizione, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jRadioButtonEqual))
            .addGroup(layout.createSequentialGroup()
                .addGap(130, 130, 130)
                .addComponent(jLabelTipo)
                .addGap(4, 4, 4)
                .addComponent(jComboBoxType, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabelCD)
                .addGap(4, 4, 4)
                .addComponent(jTextFieldCD, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabelAssociazione)
                .addGap(4, 4, 4)
                .addComponent(jTextFieldAssociazione, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(jLabelGenerale)
                .addGap(4, 4, 4)
                .addComponent(jTextFieldGenerale, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(jLabelHier)
                .addGap(4, 4, 4)
                .addComponent(jTextFieldHier, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jRadioButtonHier))
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 821, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(159, 159, 159)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabelNome))
                    .addComponent(jTextFieldNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabelDescrizione))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jTextFieldDescrizione, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jRadioButtonEqual))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabelTipo))
                    .addComponent(jComboBoxType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabelCD))
                    .addComponent(jTextFieldCD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabelAssociazione))
                    .addComponent(jTextFieldAssociazione, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabelGenerale))
                    .addComponent(jTextFieldGenerale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabelHier))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jTextFieldHier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jRadioButtonHier))
                .addGap(28, 28, 28)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(56, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldNomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldNomeActionPerformed

    private void jRadioButtonEqualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonEqualActionPerformed
        isLike = !isLike;
    }//GEN-LAST:event_jRadioButtonEqualActionPerformed

    private void jRadioButtonHierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonHierActionPerformed
        setEnabledjTextFieldPK(!isJTextFieldPKEnabled());
        jTextFieldAssociazione.setEnabled(!jTextFieldAssociazione.isEnabled());
        jTextFieldCD.setEnabled(!jTextFieldCD.isEnabled());
        jTextFieldDescrizione.setEnabled(!jTextFieldDescrizione.isEnabled());
        jTextFieldGenerale.setEnabled(!jTextFieldGenerale.isEnabled());
        jTextFieldNome.setEnabled(!jTextFieldNome.isEnabled());
        jRadioButtonEqual.setEnabled(!jRadioButtonEqual.isEnabled());
        jComboBoxType.setEnabled(!jComboBoxType.isEnabled());
        jTextFieldHier.setEnabled(!jTextFieldHier.isEnabled());  
    }//GEN-LAST:event_jRadioButtonHierActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        DBOperation.currentlyOpen[1] = null;
    }//GEN-LAST:event_formWindowClosing

    @Override
    protected void reselectPrevRow(){
        if(jTableClasse.getRowCount() > 0){
            switch(getModalita()){
                case MODIFICA:
                    jTableClasse.setRowSelectionInterval(selectedRow, selectedRow);
                    break;
                case INSERISCI:
                    jTableClasse.setRowSelectionInterval(jTableClasse.getRowCount()-1, jTableClasse.getRowCount()-1);
                    break;
                case ELIMINA:
                    if(selectedRow < jTableClasse.getRowCount()){
                            jTableClasse.setRowSelectionInterval(selectedRow, selectedRow);
                    } else{
                        jTableClasse.setRowSelectionInterval(selectedRow-1, selectedRow-1);
                    }     
            }
        }    
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> jComboBoxType;
    private javax.swing.JLabel jLabelAssociazione;
    private javax.swing.JLabel jLabelCD;
    private javax.swing.JLabel jLabelDescrizione;
    private javax.swing.JLabel jLabelGenerale;
    private javax.swing.JLabel jLabelHier;
    private javax.swing.JLabel jLabelNome;
    private javax.swing.JLabel jLabelTipo;
    private javax.swing.JRadioButton jRadioButtonEqual;
    private javax.swing.JRadioButton jRadioButtonHier;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableClasse;
    private javax.swing.JTextField jTextFieldAssociazione;
    private javax.swing.JTextField jTextFieldCD;
    private javax.swing.JTextField jTextFieldDescrizione;
    private javax.swing.JTextField jTextFieldGenerale;
    private javax.swing.JTextField jTextFieldHier;
    private javax.swing.JTextField jTextFieldNome;
    // End of variables declaration//GEN-END:variables
}
