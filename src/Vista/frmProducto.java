/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;
import configuracion.Gestionar;
import controlador.Categoria_controlador;
import controlador.Inventario_controlador;
import entidades.Categoria;
import entidades.Inventario;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
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
public class frmProducto extends javax.swing.JInternalFrame {
   private List<Inventario> inventarioList;
    private final Inventario_controlador controlador;
    /**
     * Creates new form frmProductos
     */
    public frmProducto() {
        initComponents();
        inventarioList = new ArrayList<>();
        controlador = new Inventario_controlador();
        inventarioList = controlador.Obtener();
        cargarDatos(inventarioList);
        changeText();
        chkExistencias.add(jrbExistencia1);
        chkExistencias.add(jrbExistencia2);
        chkExistencias.add(jrbTodos);
        cboCategorias.addItem(new Categoria(0, "Todas", true));
        new Categoria_controlador().Obtener().stream().forEach(cboCategorias::addItem);
        //new Validaciones().cboCategoria2(cboCategorias, new Categoria_controlador().Obtener());
        jrbTodos.setSelected(true);
        jtInventario.setDefaultRenderer(Object.class, new RenderizadoColor());
//        lblTotalProd.setText(String.valueOf(jtInventario.getRowCount()));
    }

    private void cargarDatos(List<Inventario> lista){
        String[] columnas = {"Producto", "Stock Actual", "Ubicacion"};
        Validaciones.reiniciarJTable(jtInventario);
        DefaultTableModel modelo = new DefaultTableModelImpl();
        modelo.setColumnIdentifiers(columnas);
        lista.stream().forEach(datos -> {
            Object[] nuevaFila = {
                datos,
                datos.getStock(),
                datos.getBodega()
            };
            if(datos.isEstado()){
                modelo.addRow(nuevaFila);
            }
        });
        jtInventario.setModel(modelo);
        lblTotalProd.setText(String.valueOf(jtInventario.getRowCount()));
    }

    private void buscarTXT() {
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

    public void Editar(){
        Frame f = JOptionPane.getFrameForComponent(this);
        frmNuevoProducto frm = new frmNuevoProducto((JFrame) f, true);
        frm.setLocationRelativeTo(null);
        int fila = jtInventario.getSelectedRow();
        if (fila > -1) {
            frm.setInventario(inventarioList.get(inventarioList.indexOf(jtInventario.getValueAt(fila, 0))));
            frm.setEditar(true);
            frm.setVisible(true);
            if (!frm.isVisible()) {
                inventarioList = controlador.Obtener();
                cargarDatos(inventarioList);
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

        chkExistencias = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnStock = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtBusqueda = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtInventario = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jrbExistencia1 = new javax.swing.JRadioButton();
        jrbExistencia2 = new javax.swing.JRadioButton();
        cboCategorias = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jrbTodos = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        lblTotalProd = new javax.swing.JLabel();

        setClosable(true);
        setMaximizable(true);
        setTitle("Catalogo de Productos");

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));

        btnNuevo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/add32.png"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.setToolTipText("Nuevo Producto");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnEditar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/edit32.png"))); // NOI18N
        btnEditar.setText("Editar");
        btnEditar.setToolTipText("Editar Producto");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnEliminar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/delete32.png"))); // NOI18N
        btnEliminar.setText("Borrar");
        btnEliminar.setToolTipText("Eliminar Producto");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnStock.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnStock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/inv_ajuste24.png"))); // NOI18N
        btnStock.setText("Ajustar");
        btnStock.setToolTipText("Ajuste de Inventario");
        btnStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStockActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addComponent(btnNuevo)
                .addGap(50, 50, 50)
                .addComponent(btnEditar)
                .addGap(50, 50, 50)
                .addComponent(btnStock)
                .addGap(50, 50, 50)
                .addComponent(btnEliminar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnStock, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEditar))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(12, 12, 12))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/search16.png"))); // NOI18N
        jLabel1.setText("Buscar:");

        txtBusqueda.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jtInventario.setFont(new java.awt.Font("Trebuchet MS", 0, 15)); // NOI18N
        jtInventario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Producto", "stock", "Precio", "Vencimiento"
            }
        ));
        jtInventario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtInventarioKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jtInventario);

        jrbExistencia1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jrbExistencia1.setText("Articulo en existencia");
        jrbExistencia1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jrbExistencia1ItemStateChanged(evt);
            }
        });

        jrbExistencia2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jrbExistencia2.setText("Articulo sin existencia");
        jrbExistencia2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jrbExistencia2ItemStateChanged(evt);
            }
        });

        cboCategorias.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cboCategorias.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboCategoriasItemStateChanged(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Categorias:");

        jrbTodos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jrbTodos.setText("Todos");
        jrbTodos.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jrbTodosItemStateChanged(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Productos:");

        lblTotalProd.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblTotalProd.setForeground(new java.awt.Color(255, 0, 0));
        lblTotalProd.setText("<Producto>");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(cboCategorias, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jrbTodos)
                        .addGap(24, 24, 24)
                        .addComponent(jrbExistencia1)
                        .addGap(18, 18, 18)
                        .addComponent(jrbExistencia2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblTotalProd, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 853, Short.MAX_VALUE)
                    .addComponent(jSeparator2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtBusqueda)))
                .addGap(14, 14, 14))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtBusqueda)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(11, 11, 11)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboCategorias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jrbTodos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jrbExistencia1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jrbExistencia2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3)
                                .addComponent(lblTotalProd, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
        Frame frmP = JOptionPane.getFrameForComponent(this);
        frmNuevoProducto dialog = new frmNuevoProducto(frmP, true);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        if(!frmP.isVisible()){
            inventarioList = controlador.Obtener();
            cargarDatos(inventarioList);
        }
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
        int fila = jtInventario.getSelectedRow();
        if (fila > -1) {
            if (jtInventario.getValueAt(fila, 0) instanceof Inventario) {
                int respuesta = JOptionPane.showConfirmDialog(this, "¿Estas seguro de eliminar estos datos?", new Gestionar().Leer("Empresa", "nombre"),
                        JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    if (controlador.Eliminar(inventarioList.get(inventarioList.indexOf(jtInventario.getValueAt(fila, 0))))) {
                        JOptionPane.showMessageDialog(this,
                                "El registro ha sido eliminado exitosamente",
                                "Sistema de Ventas - Categoria",
                                JOptionPane.INFORMATION_MESSAGE);
                        inventarioList = controlador.Obtener();
                        cargarDatos(inventarioList);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Selecciona primero",
                    new Gestionar().Leer("Empresa", "nombre"),
                    JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        // TODO add your handling code here:
        Editar();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStockActionPerformed
        // TODO add your handling code here:
        Frame frmP = JOptionPane.getFrameForComponent(this);
        frmAjustarInvent dialog = new frmAjustarInvent((JFrame)frmP, true);
        dialog.setLocationRelativeTo(null);
        int fila = jtInventario.getSelectedRow();
        if (fila > -1) {
            dialog.setInv(inventarioList.get(inventarioList.indexOf(jtInventario.getValueAt(fila, 0))));
            dialog.setVisible(true);
            if (!dialog.isVisible()) {
                inventarioList = controlador.Obtener();
                cargarDatos(inventarioList);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Selecciona primero",
                    new Gestionar().Leer("Empresa", "nombre"),
                    JOptionPane.WARNING_MESSAGE);
        }
                
    }//GEN-LAST:event_btnStockActionPerformed

    private void jrbExistencia1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jrbExistencia1ItemStateChanged
        // TODO add your handling code here:
        buscarTXT();
    }//GEN-LAST:event_jrbExistencia1ItemStateChanged

    private void cboCategoriasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboCategoriasItemStateChanged
        // TODO add your handling code here:
        buscarTXT();
    }//GEN-LAST:event_cboCategoriasItemStateChanged

    private void jrbExistencia2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jrbExistencia2ItemStateChanged
        // TODO add your handling code here:
        buscarTXT();
    }//GEN-LAST:event_jrbExistencia2ItemStateChanged

    private void jrbTodosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jrbTodosItemStateChanged
        // TODO add your handling code here:
        buscarTXT();
    }//GEN-LAST:event_jrbTodosItemStateChanged

    private void jtInventarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtInventarioKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            Editar();
        }
    }//GEN-LAST:event_jtInventarioKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnStock;
    private javax.swing.JComboBox<Categoria> cboCategorias;
    private javax.swing.ButtonGroup chkExistencias;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JRadioButton jrbExistencia1;
    private javax.swing.JRadioButton jrbExistencia2;
    private javax.swing.JRadioButton jrbTodos;
    private javax.swing.JTable jtInventario;
    private javax.swing.JLabel lblTotalProd;
    private javax.swing.JTextField txtBusqueda;
    // End of variables declaration//GEN-END:variables
}
