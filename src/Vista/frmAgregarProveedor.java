/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import configuracion.Gestionar;
import controlador.Proveedor_controlador;
import entidades.Proveedor;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Gerard
 */
public class frmAgregarProveedor extends javax.swing.JDialog {
    private final Proveedor_controlador controlador;
    private List<Proveedor> proveedorList;
    private Proveedor proveedor_sel;
    /**
     * Creates new form frmAgregarProveedor
     * @param parent
     * @param modal
     */
    public frmAgregarProveedor(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        controlador = new Proveedor_controlador();
        proveedorList = new ArrayList<>();
        proveedorList = controlador.Obtener();
        cargarDatos(proveedorList);
        changeText();
    }
    
    private void cargarDatos(List<Proveedor> lista){
        String[] columnas = {"Nombre", "Telefono", "Direccion"};
        ControlesGenerales.reiniciarJTable(jtProveedores);
        DefaultTableModel modelo = new ControlesGenerales.DefaultTableModelImpl();
        modelo.setColumnIdentifiers(columnas);
        lista.forEach(datos -> {
            Object[] nuevaFila= {
                datos,
                datos.getTelefono(),
                datos.getDomicilio()
            };
            if(datos.isEstado()){
                modelo.addRow(nuevaFila);
            }
        });
        jtProveedores.setModel(modelo);
    }
    
     private void buscarTXT(){
        List<Proveedor> encontrado = new ArrayList<>();
        encontrado = proveedorList.stream().filter(
                datos -> datos.toString().toUpperCase().contains(txtBusqueda.getText().toUpperCase())
        ).collect(Collectors.toList());
        cargarDatos(encontrado);
        if(jtProveedores.getRowCount() == 1){
            jtProveedores.setRowSelectionInterval(0, 0);
        }
    }
    
    private void changeText(){
        txtBusqueda.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent de) {
                buscarTXT();
            }
            @Override
            public void removeUpdate(DocumentEvent de) {
                buscarTXT();
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                buscarTXT();
            }
        
        });
    }
            
    private void SeleccionarProveedor(){
        int fila = jtProveedores.getSelectedRow();
        if(fila > -1){
            proveedor_sel = proveedorList.get(proveedorList.indexOf((Proveedor) jtProveedores.getValueAt(fila, 0)));
            System.out.println("Proveedor seleccionado = " + proveedor_sel);
            this.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un proveedor primero.",
                    new Gestionar().Leer("Empresa", "nombre"),
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtBusqueda = new javax.swing.JTextField();
        btnNuevo = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtProveedores = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        btnSelecciona = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(new Gestionar().Leer("Empresa", "nombre"));
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/search16.png"))); // NOI18N
        jLabel1.setText("Buscar");

        txtBusqueda.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBusquedaKeyPressed(evt);
            }
        });

        btnNuevo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/add16.png"))); // NOI18N
        btnNuevo.setText("Agregar");
        btnNuevo.setToolTipText("Nuevo Proveedor");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        jtProveedores.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        jtProveedores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Código", "Nombre", "Tel/Cel", "Direccion"
            }
        ));
        jtProveedores.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtProveedoresKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jtProveedores);

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));

        btnSelecciona.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSelecciona.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/select24.png"))); // NOI18N
        btnSelecciona.setText("Seleccionar");
        btnSelecciona.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/okay16.png"))); // NOI18N
        btnSelecciona.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        btnSelecciona.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSelecciona.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeleccionaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSelecciona)
                .addGap(20, 20, 20))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(btnSelecciona, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)))
                .addGap(12, 12, 12))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtBusqueda)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 340, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // TODO add your handling code here:
        Frame f = JOptionPane.getFrameForComponent(this);
        frmNuevoProveedor frm = new frmNuevoProveedor((JFrame) f, true);
        frm.setVisible(true);
        if(!frm.isVisible()){
            proveedorList = controlador.Obtener();
            cargarDatos(proveedorList);
            frm.dispose();
        }
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnSeleccionaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeleccionaActionPerformed
        // TODO add your handling code here:
        SeleccionarProveedor();
    }//GEN-LAST:event_btnSeleccionaActionPerformed

    private void txtBusquedaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            SeleccionarProveedor();
        }
    }//GEN-LAST:event_txtBusquedaKeyPressed

    private void jtProveedoresKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtProveedoresKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            SeleccionarProveedor();
        }
    }//GEN-LAST:event_jtProveedoresKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmAgregarProveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(() -> {
            frmAgregarProveedor dialog = new frmAgregarProveedor(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSelecciona;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtProveedores;
    private javax.swing.JTextField txtBusqueda;
    // End of variables declaration//GEN-END:variables

    public Proveedor getProveedor_sel() {
        return proveedor_sel;
    }
}
