/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import com.sun.glass.events.KeyEvent;
import configuracion.Gestionar;
import controlador.Categoria_controlador;
import controlador.Inventario_controlador;
import entidades.Categoria;
import entidades.Inventario;
import java.awt.Frame;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author dakrpastiursSennin
 */
public class frmAgregarProducto extends javax.swing.JDialog {
    private List<Inventario> inventarioList;
    private final Inventario_controlador controlador;
    private Inventario inv_seleccion;
    
    /**
     * Creates new form frmAgregarProducto
     * @param parent
     * @param modal
     */
    public frmAgregarProducto(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        inventarioList = new ArrayList<>();
        controlador = new Inventario_controlador();
        inventarioList = controlador.Obtener();
        inv_seleccion = new Inventario();
        changeText();
        this.setTitle(new Gestionar().Leer("Empresa", "nombre"));
        jtInventario.setDefaultRenderer(Object.class, new RenderizadoColor());
        jbgrExistencia.add(jrbExistencia1);
        jbgrExistencia.add(jrbExistencia2);
        jbgrExistencia.add(jrbTodos);
        new Validaciones().cboCategoria2(cboCategorias, new Categoria_controlador().Obtener());
    }
    
    private void cargarDatos(List<Inventario> lista){
        String[] columnas = {"Producto", "Descripcion", "Existencia", "Precio", "Bodega"};
        ControlesGenerales.reiniciarJTable(jtInventario);
        DefaultTableModel modelo = new ControlesGenerales.DefaultTableModelImpl();
        modelo.setColumnIdentifiers(columnas);
        lista.forEach((Inventario datos)->{
            Object[] nuevafila = {
                datos,
                datos.getArticulo().getDescripcion(),
                datos.getStock(),
                datos.getPrecio().get(0).getCantidad(),
                datos.getBodega()
            };
            if(datos.isEstado() && datos.getId() != inv_seleccion.getId()){
                //if(datos.getStock().compareTo(BigDecimal.ZERO) > 0){                    
                    modelo.addRow(nuevafila);
                //}
            }
            
            /*if (datos.getStock().compareTo(new BigDecimal(datos.getStockMin())) < 1) {
                System.err.println("El producto " + datos.getArticulo().getNombre() + " casi no tiene existencia");
            }*/
            
        });
        jtInventario.setModel(modelo);
        //jtInventario.setDefaultRenderer(Object.class, new RenderizadoColor());
    }
    
    private void buscarTXT(){
        Categoria cat = ((Categoria) cboCategorias.getSelectedItem());
        List<Inventario> encontrado = inventarioList.stream().filter(
                datos -> {
                    if(jrbExistencia1.isSelected() == true && jrbExistencia2.isSelected() == false){
                        return datos.toString().toUpperCase().contains(txtBusqueda.getText().toUpperCase()) 
                                && datos.getStock().compareTo(new BigDecimal(datos.getStockMin())) > 0 ;
                    } else if(jrbExistencia1.isSelected() == false && jrbExistencia2.isSelected() == true) {
                        return datos.toString().toUpperCase().contains(txtBusqueda.getText().toUpperCase()) 
                                && datos.getStock().compareTo(new BigDecimal(datos.getStockMin())) <= 0;
                    } else {
                        return datos.toString().toUpperCase().contains(txtBusqueda.getText().toUpperCase());
                    }
                }
        ).collect(Collectors.toList());
        encontrado = encontrado.stream().filter(
                datos -> {
                    if(cat.getId() > 0){
                        return datos.toString().toUpperCase().contains(txtBusqueda.getText().toUpperCase()) 
                                && datos.getCategoria().getId() == cat.getId() ;
                    } else {
                        return datos.toString().toUpperCase().contains(txtBusqueda.getText().toUpperCase());
                    }
                }                
        ).collect(Collectors.toList());
        cargarDatos(encontrado);
        if(jtInventario.getRowCount() == 1){
            jtInventario.setRowSelectionInterval(0, 0);
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
    
    private void SeleccionarInventario() {
        int fila = jtInventario.getSelectedRow();
        if (fila > -1) {
            if (jtInventario.getValueAt(fila, 0) instanceof Inventario) {
                inv_seleccion = inventarioList.get(inventarioList.indexOf(jtInventario.getValueAt(fila, 0)));
                if (inv_seleccion.getStock().compareTo(BigDecimal.ZERO) > 0) {
                    this.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(
                            this, 
                            "No hay existencias del producto " + inv_seleccion + " para vender", 
                            this.getTitle(), 
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un producto primero.",
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

        jbgrExistencia = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtBusqueda = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtInventario = new javax.swing.JTable();
        btnNuevo = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        cboCategorias = new javax.swing.JComboBox<>();
        jrbTodos = new javax.swing.JRadioButton();
        jrbExistencia1 = new javax.swing.JRadioButton();
        jrbExistencia2 = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        btnSeleccionar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/search16.png"))); // NOI18N
        jLabel1.setText("Buscar:");

        txtBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBusquedaKeyPressed(evt);
            }
        });

        jtInventario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Codigo", "Descripcion", "Existencia", "Precio"
            }
        ));
        jtInventario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtInventarioKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jtInventario);

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/add16.png"))); // NOI18N
        btnNuevo.setText("Agregar");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/edit16.png"))); // NOI18N
        btnEditar.setText("Editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Categorias:");

        cboCategorias.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboCategoriasItemStateChanged(evt);
            }
        });

        jrbTodos.setText("Todos");
        jrbTodos.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jrbTodosItemStateChanged(evt);
            }
        });

        jrbExistencia1.setText("Articulo en existencia");
        jrbExistencia1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jrbExistencia1ItemStateChanged(evt);
            }
        });

        jrbExistencia2.setText("Articulo sin existencia");
        jrbExistencia2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jrbExistencia2ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnNuevo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                        .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCategorias, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(94, 94, 94)
                        .addComponent(jrbTodos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jrbExistencia1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jrbExistencia2)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtBusqueda)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                        .addComponent(cboCategorias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jrbExistencia1)
                        .addComponent(jrbExistencia2)
                        .addComponent(jrbTodos)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));

        btnSeleccionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/select24.png"))); // NOI18N
        btnSeleccionar.setText("Seleccionar");
        btnSeleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeleccionarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSeleccionar)
                .addGap(25, 25, 25))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSeleccionar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // TODO add your handling code here:
        Frame f = JOptionPane.getFrameForComponent(this);
        frmNuevoProducto frm = new frmNuevoProducto((JFrame) f, true);
        frm.setVisible(true);
        if(!frm.isVisible()){
            if (frm.getInventario().equals(new Inventario())) {
                inventarioList.add(frm.getInventario());
                Collections.sort(inventarioList, (Inventario inv1, Inventario inv2) -> {
                    return inv1.toString().compareTo(inv2.toString());
                });
                cargarDatos(inventarioList);
            }
            frm.dispose();
        }
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        // TODO add your handling code here:
        int fila = jtInventario.getSelectedRow();
        Frame f = JOptionPane.getFrameForComponent(this);
        frmNuevoProducto frm = new frmNuevoProducto((JFrame) f, true);
        if (fila > -1) {
            if (jtInventario.getValueAt(fila, 0) instanceof Inventario) {
                frm.setInventario(inventarioList.get(inventarioList.indexOf(jtInventario.getValueAt(fila, 0))));
                frm.setEditar(true);
                frm.setVisible(true);
                if (!frm.isVisible()) {
                    inventarioList.add(frm.getInventario());
                    cargarDatos(inventarioList);
                    frm.dispose();
                }
            }
        } else {
            JOptionPane.showMessageDialog(
                    this, 
                    "Selecciona un producto primero.", 
                    new Gestionar().Leer("Empresa", "nombre"), 
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeleccionarActionPerformed
        // TODO add your handling code here:  
        SeleccionarInventario();
    }//GEN-LAST:event_btnSeleccionarActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        cargarDatos(inventarioList);
    }//GEN-LAST:event_formWindowOpened

    private void txtBusquedaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            SeleccionarInventario();
        }
    }//GEN-LAST:event_txtBusquedaKeyPressed

    private void jtInventarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtInventarioKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            SeleccionarInventario();
        }
    }//GEN-LAST:event_jtInventarioKeyPressed

    private void cboCategoriasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboCategoriasItemStateChanged
        // TODO add your handling code here:
        buscarTXT();
    }//GEN-LAST:event_cboCategoriasItemStateChanged

    private void jrbTodosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jrbTodosItemStateChanged
        // TODO add your handling code here:
        buscarTXT();
    }//GEN-LAST:event_jrbTodosItemStateChanged

    private void jrbExistencia1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jrbExistencia1ItemStateChanged
        // TODO add your handling code here:
        buscarTXT();
    }//GEN-LAST:event_jrbExistencia1ItemStateChanged

    private void jrbExistencia2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jrbExistencia2ItemStateChanged
        // TODO add your handling code here:
        buscarTXT();
    }//GEN-LAST:event_jrbExistencia2ItemStateChanged

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmAgregarProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmAgregarProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmAgregarProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmAgregarProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frmAgregarProducto dialog = new frmAgregarProducto(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSeleccionar;
    private javax.swing.JComboBox<String> cboCategorias;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.ButtonGroup jbgrExistencia;
    private javax.swing.JRadioButton jrbExistencia1;
    private javax.swing.JRadioButton jrbExistencia2;
    private javax.swing.JRadioButton jrbTodos;
    private javax.swing.JTable jtInventario;
    private javax.swing.JTextField txtBusqueda;
    // End of variables declaration//GEN-END:variables

    public Inventario getInv_seleccion() {
        return inv_seleccion;
    }

    public void setInv_seleccion(Inventario inv_seleccion) {
        System.out.println("Datos 2 :: " + inv_seleccion.getId());
        this.inv_seleccion = inv_seleccion;
    }
    
}
