/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import com.sun.glass.events.KeyEvent;
import configuracion.Gestionar;
import controlador.Proveedor_controlador;
import entidades.Proveedor;
import java.awt.Frame;
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
public class frmProveedor extends javax.swing.JInternalFrame {

    private Proveedor_controlador controlador;
    private List<Proveedor> proveedorList;

    /**
     * Creates new form frmProveedor
     */
    public frmProveedor() {
        initComponents();
        controlador = new Proveedor_controlador();
        proveedorList = new ArrayList<>();
        proveedorList = controlador.Obtener();
        cargarDatos(proveedorList);
        changeText();
    }

    private void cargarDatos(List<Proveedor> lista) {
        String[] columnas = {"Nombre", "Telefono", "Direccion"};
        ControlesGenerales.reiniciarJTable(jtProveedores);
        DefaultTableModel modelo = new ControlesGenerales.DefaultTableModelImpl();
        modelo.setColumnIdentifiers(columnas);
        lista.forEach(datos -> {
            Object[] nuevaFila = {
                datos,
                datos.getTelefono(),
                datos.getDomicilio()
            };
            if (datos.isEstado()) {
                modelo.addRow(nuevaFila);
            }
        });
        jtProveedores.setModel(modelo);
    }

    private void buscarTXT() {
        List<Proveedor> encontrado = new ArrayList<>();
        encontrado = proveedorList.stream().filter(
                datos -> datos.toString().toUpperCase().contains(txtBusqueda.getText().toUpperCase())
        ).collect(Collectors.toList());
        cargarDatos(encontrado);
        if (jtProveedores.getRowCount() == 1) {
            jtProveedores.setRowSelectionInterval(0, 0);
        }
    }

    private void changeText() {
        txtBusqueda.getDocument().addDocumentListener(new DocumentListener() {
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

    private void Editar() {
        Frame f = JOptionPane.getFrameForComponent(this);
        int fila = jtProveedores.getSelectedRow();
        if (fila > -1) {
            frmNuevoProveedor frm = new frmNuevoProveedor((JFrame) f, true);
            frm.setProveedor(proveedorList.get(proveedorList.indexOf((Proveedor) jtProveedores.getValueAt(fila, 0))));
            frm.setEditar(true);
            frm.setVisible(true);
            if (!frm.isVisible()) {
                proveedorList = controlador.Obtener();
                cargarDatos(proveedorList);
                frm.dispose();
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Selecciona primero",
                    new Gestionar().Leer("Empresa", "nombre"),
                    JOptionPane.WARNING_MESSAGE);
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

        jPanel2 = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JButton();
        btnEditarProveedor = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtBusqueda = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtProveedores = new javax.swing.JTable();

        setClosable(true);
        setMaximizable(true);
        setTitle("Proveedor");

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));

        btnNuevo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/add32.png"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnEditarProveedor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnEditarProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/edit32.png"))); // NOI18N
        btnEditarProveedor.setText("Editar");
        btnEditarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarProveedorActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/delete32.png"))); // NOI18N
        jButton3.setText("Borrar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(59, Short.MAX_VALUE)
                .addComponent(btnNuevo)
                .addGap(50, 50, 50)
                .addComponent(btnEditarProveedor)
                .addGap(50, 50, 50)
                .addComponent(jButton3)
                .addContainerGap(181, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo)
                    .addComponent(btnEditarProveedor)
                    .addComponent(jButton3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/search16.png"))); // NOI18N
        jLabel1.setText("Buscar:");

        txtBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBusquedaKeyPressed(evt);
            }
        });

        jtProveedores.setFont(new java.awt.Font("Trebuchet MS", 0, 15)); // NOI18N
        jtProveedores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Código", "Nombre", "Tel/Cel", "Direccion"
            }
        ));
        jScrollPane1.setViewportView(jtProveedores);

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBusqueda)))
                .addGap(12, 12, 12))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // TODO add your handling code here:
        Frame f = JOptionPane.getFrameForComponent(this);
        frmNuevoProveedor frm = new frmNuevoProveedor((JFrame) f, true);
        frm.setVisible(true);
        if (!frm.isVisible()) {
            proveedorList = controlador.Obtener();
            cargarDatos(proveedorList);
            frm.dispose();
        }
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnEditarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarProveedorActionPerformed
        // TODO add your handling code here:
        Editar();
    }//GEN-LAST:event_btnEditarProveedorActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        int fila = jtProveedores.getSelectedRow();
        if (fila > -1) {
            if (jtProveedores.getValueAt(fila, 0) instanceof Proveedor) {
                int respuesta = JOptionPane.showConfirmDialog(this, "¿Estas seguro de eliminar estos datos?", new Gestionar().Leer("Empresa", "nombre"),
                        JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    if (controlador.Eliminar(proveedorList.get(proveedorList.indexOf((Proveedor) jtProveedores.getValueAt(fila, 0))))) {
                        JOptionPane.showMessageDialog(this,
                                "El registro ha sido eliminado exitosamente",
                                "Sistema de Compras y Ventas - Categoria",
                                JOptionPane.INFORMATION_MESSAGE);
                        proveedorList = controlador.Obtener();
                        cargarDatos(proveedorList);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Selecciona primero",
                    new Gestionar().Leer("Empresa", "nombre"),
                    JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void txtBusquedaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            Editar();
        }
    }//GEN-LAST:event_txtBusquedaKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEditarProveedor;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtProveedores;
    private javax.swing.JTextField txtBusqueda;
    // End of variables declaration//GEN-END:variables
}
