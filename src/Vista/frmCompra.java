/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import static Vista.ControlesGenerales.reiniciarJTable;
import configuracion.Gestionar;
import controlador.Compra_controlador;
import entidades.Compra;
import entidades.DetalleCompra;
import entidades.Inventario;
import entidades.Proveedor;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Gerard
 */
public class frmCompra extends javax.swing.JInternalFrame {
    private Proveedor prov_actual;
    private Compra_controlador controlador;
    /**
     * Creates new form frmCompras
     */
    public frmCompra() {
        initComponents();
        prov_actual = new Proveedor();
        jTableDetalleCompra.setDefaultRenderer(Object.class, new RenderizadorBoton());
        jTableDetalleCompra.setRowHeight(25);
        controlador = new Compra_controlador();
        Object[] columnas = {"Producto", "Cantidad", "Precio ($)", "Importe", "¿Quitar?"};
        DefaultTableModel modelo = new ControlesGenerales.DefaultTableModelImpl();
        modelo.setColumnIdentifiers(columnas);
        jTableDetalleCompra.setModel(modelo);
        ActionListener updateClockAction = (ActionEvent e) -> {
            Locale local = new Locale("es", "SV");
            SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy - hh:mm:ss a", local);
            txtFecha.setText(formateador.format(new Date()).trim()); 
        };
        Timer t = new Timer(100, updateClockAction);
        t.start();
    }
    private void AñadirDetalle(Inventario inventarioActual) {
        DefaultTableModel modelo = (DefaultTableModel) jTableDetalleCompra.getModel();
        if (!inventarioActual.equals(new Inventario())) {
            boolean encontrado = false;
            for (int i = 0; i < modelo.getRowCount(); i++) {
                Inventario comparador = (Inventario) (modelo.getValueAt(i, 0));
                if (comparador.getId() == inventarioActual.getId()) {
                    encontrado = true;
                    break;
                }
            }
            if (encontrado) {
                JOptionPane.showMessageDialog(this, "Ya se agrego este producto", "Sistemas de Compras y Ventas - Compras", JOptionPane.WARNING_MESSAGE);
            } else {
                Object[] nuevaFila = {
                    inventarioActual,
                    1,
                    inventarioActual.getPrecio().get(0).getCantidad(),
                    new BigDecimal("1").multiply(inventarioActual.getPrecio().get(0).getCantidad()),
                    new JButton("¿Quitar?")
                };
                modelo.addRow(nuevaFila);
                jTableDetalleCompra.setModel(modelo);
                lblProducto.setText("<Producto>");
                calcularTotal();
            }
        }
    }
    private void calcularTotal() {
        double subtotal = 0;
        //double iva = 0;
        double total = 0;
        for (int i = 0; i < jTableDetalleCompra.getRowCount(); i++) {
            subtotal += Double.parseDouble(jTableDetalleCompra.getValueAt(i, 3).toString());
        }
        //iva = subtotal * 0.13;
        //txtIVA.setText(new BigDecimal(iva).setScale(2, RoundingMode.HALF_UP).toString());
        total = subtotal;
        lblTotal.setText(new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).toString());
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
        btnGuardar = new javax.swing.JButton();
        btnEditarProducto = new javax.swing.JButton();
        btnQuitarProducto = new javax.swing.JButton();
        btnBuscarProducto = new javax.swing.JButton();
        btnSeleccionarProveedor = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jpProducto = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lblProducto = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblCodigo = new javax.swing.JLabel();
        cboUnidadMedida = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cboUnidadConversion = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableDetalleCompra = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        txtFecha = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        txtNFactura = new javax.swing.JTextField();
        txtProveedor = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        lblTotalProd = new javax.swing.JLabel();

        setClosable(true);
        setMaximizable(true);
        setTitle("Compras");

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));

        btnGuardar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/save32.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnEditarProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnEditarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/edit32.png"))); // NOI18N
        btnEditarProducto.setText("Editar");

        btnQuitarProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnQuitarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/del24.png"))); // NOI18N
        btnQuitarProducto.setText("Quitar");
        btnQuitarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarProductoActionPerformed(evt);
            }
        });

        btnBuscarProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnBuscarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/search32.png"))); // NOI18N
        btnBuscarProducto.setText("Buscar");
        btnBuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarProductoActionPerformed(evt);
            }
        });

        btnSeleccionarProveedor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSeleccionarProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/provider24.png"))); // NOI18N
        btnSeleccionarProveedor.setText("Proveedor");
        btnSeleccionarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeleccionarProveedorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBuscarProducto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnEditarProducto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnQuitarProducto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSeleccionarProveedor)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEditarProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBuscarProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnSeleccionarProveedor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnQuitarProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel1.setText("Fecha:");

        jLabel2.setText("Proveedor:");

        jpProducto.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos de Producto"));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Descripcion: ");

        lblProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblProducto.setForeground(new java.awt.Color(0, 102, 255));
        lblProducto.setText("<Producto>");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Código");

        lblCodigo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCodigo.setForeground(new java.awt.Color(0, 102, 255));
        lblCodigo.setText("<codigo>");

        cboUnidadMedida.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel12.setText("Unidad de medida:");

        jLabel10.setText("Unidad de Conversion:");

        cboUnidadConversion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout jpProductoLayout = new javax.swing.GroupLayout(jpProducto);
        jpProducto.setLayout(jpProductoLayout);
        jpProductoLayout.setHorizontalGroup(
            jpProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpProductoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpProductoLayout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(lblCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpProductoLayout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cboUnidadMedida, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 153, Short.MAX_VALUE)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cboUnidadConversion, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jpProductoLayout.setVerticalGroup(
            jpProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpProductoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jpProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(lblCodigo)
                    .addComponent(lblProducto)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jpProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboUnidadConversion)
                    .addGroup(jpProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cboUnidadMedida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTableDetalleCompra.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jTableDetalleCompra.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Descripcion", "Cantidad", "Precio", "Importe"
            }
        ));
        jTableDetalleCompra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableDetalleCompraMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTableDetalleCompra);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel3.setText("Total:");

        lblTotal.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(0, 102, 255));
        lblTotal.setText("<Total>");

        txtFecha.setEditable(false);
        txtFecha.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel5.setText("N° de Comprobante:");

        txtNFactura.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtProveedor.setEditable(false);
        txtProveedor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtProveedor.setText("-");
        txtProveedor.setToolTipText("Seleccionar un Proveedor");
        txtProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProveedorActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel6.setText("Productos ingresados: ");

        lblTotalProd.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblTotalProd.setForeground(new java.awt.Color(0, 102, 255));
        lblTotalProd.setText("<Producto>");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotalProd, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jpProducto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(1, 1, 1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1))
                            .addComponent(txtProveedor))))
                .addGap(12, 12, 12))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(txtNFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jpProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTotalProd, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel6))
                .addGap(12, 12, 12))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private void btnBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarProductoActionPerformed
        // TODO add your handling code here:
        Frame f = JOptionPane.getFrameForComponent(this);
        frmAgregarProducto frm = new frmAgregarProducto((JFrame) f, true);
        frm.setVisible(true);
        if (frm.isVisible() == false) {
            if(frm.getInv_seleccion().getId() != 0){
                AñadirDetalle(frm.getInv_seleccion());
                lblTotalProd.setText(String.valueOf(jTableDetalleCompra.getRowCount()));                
            }
            frm.dispose();
        }
    }//GEN-LAST:event_btnBuscarProductoActionPerformed

    private void btnSeleccionarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeleccionarProveedorActionPerformed
        // TODO add your handling code here:
        Frame f = JOptionPane.getFrameForComponent(this);
        frmAgregarProveedor frm = new frmAgregarProveedor((JFrame) f, true);
        frm.setVisible(true);
        if(!frm.isVisible()){
            if(!frm.getProveedor_sel().equals(new Proveedor())){                
                prov_actual = frm.getProveedor_sel();
                frm.dispose();
                txtProveedor.setText(prov_actual.getNombre() + " - " + prov_actual.getRespresentante());
            }
        }
    }//GEN-LAST:event_btnSeleccionarProveedorActionPerformed

    private void jTableDetalleCompraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDetalleCompraMouseClicked
        // TODO add your handling code here:
        int fila = jTableDetalleCompra.getSelectedRow();
        if(fila > -1){
            DefaultTableModel modelo = (DefaultTableModel) jTableDetalleCompra.getModel();
            int columna = jTableDetalleCompra.getSelectedColumn();
            Inventario inv = (Inventario) jTableDetalleCompra.getValueAt(fila, 0);
            switch (columna) {
                case 1:
                    {
                        BigDecimal cantidad = new BigDecimal(JOptionPane.showInputDialog(this, "Digita la nueva cantidad a comprar"));
                        modelo.setValueAt(cantidad, fila, columna);
                        BigDecimal importe = cantidad.multiply(new BigDecimal(modelo.getValueAt(fila, 2).toString()));
                        modelo.setValueAt(importe, fila, 3);
                        break;
                    }
                case 2:
                    {
                        JComboBox cbo = new JComboBox(inv.getPrecio().toArray());
                        JOptionPane.showMessageDialog(this, cbo, "Selecciona el precio con el cual se comprara", JOptionPane.INFORMATION_MESSAGE);
                        BigDecimal precio = new BigDecimal(cbo.getSelectedItem().toString());
                        modelo.setValueAt(precio, fila, columna);
                        BigDecimal importe = precio.multiply(new BigDecimal(modelo.getValueAt(fila, 1).toString()));
                        modelo.setValueAt(importe, fila, 3);
                        break;
                    }
                case 4:{
                    if (jTableDetalleCompra.getValueAt(fila, columna) instanceof JButton) {
                        int respuesta = JOptionPane.showConfirmDialog(this, "¿Estas seguro de eliminar estos datos?", new Gestionar().Leer("Empresa", "nombre"),
                                JOptionPane.YES_NO_OPTION);
                        if (respuesta == JOptionPane.YES_OPTION) {
                            modelo.removeRow(fila);
                            jTableDetalleCompra.setModel(modelo);
                            calcularTotal();
                        }
                    }
                    break;
                }
                default:
                    lblProducto.setText(inv.toString());
                    lblCodigo.setText(inv.getCodigoInterno());
                    break;
            }
            jTableDetalleCompra.setModel(modelo);
            calcularTotal();
        }
    }//GEN-LAST:event_jTableDetalleCompraMouseClicked

    private void btnQuitarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarProductoActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_btnQuitarProductoActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        Compra compra = new Compra();
        if(jTableDetalleCompra.getRowCount() > 0 && prov_actual != null && !txtNFactura.getText().isEmpty()){
            compra.setNumFactura(txtNFactura.getText());
            compra.setDescripcion("Nada");
            compra.setTotal(new BigDecimal(lblTotal.getText()));
            compra.setEmpleado(frmMenu.usuarioActual.getEmpleado());
            compra.setProveedor(prov_actual);
            List<DetalleCompra> detalles = new ArrayList<>();
            for (int i = 0; i < jTableDetalleCompra.getRowCount(); i++) {
                DetalleCompra detalle = new DetalleCompra();
                detalle.setInventario((Inventario) jTableDetalleCompra.getValueAt(i, 0));
                detalle.setCantidad(new BigDecimal(jTableDetalleCompra.getValueAt(i, 1).toString()));
                detalle.setPrecio(new BigDecimal(jTableDetalleCompra.getValueAt(i, 2).toString()));
                detalle.setImporte(new BigDecimal(jTableDetalleCompra.getValueAt(i, 3).toString()));
                detalles.add(detalle);
            }
            compra.setDetalle(detalles);
            if(controlador.Registrar(compra)){
                JOptionPane.showMessageDialog(this, "Compra registrada", this.title, JOptionPane.INFORMATION_MESSAGE);
                    reiniciarJTable(jTableDetalleCompra);
                    txtProveedor.setText("");
                    txtNFactura.setText("");
                    lblTotal.setText("0.00");
                    lblProducto.setText("");
                    lblCodigo.setText("");
                    lblTotalProd.setText("0");
                    prov_actual = new Proveedor();
            }
        } else if(prov_actual == null){
            JOptionPane.showMessageDialog(this,
                    "Selecciona proveedor",
                    new Gestionar().Leer("Empresa", "nombre"),
                    JOptionPane.WARNING_MESSAGE);
        } else if(prov_actual == null){
            JOptionPane.showMessageDialog(this,
                    "Ingresa el numero de factura entregada por el proveedor",
                    new Gestionar().Leer("Empresa", "nombre"),
                    JOptionPane.WARNING_MESSAGE);
        }else {
            JOptionPane.showMessageDialog(this,
                    "Agrega Productos primero",
                    new Gestionar().Leer("Empresa", "nombre"),
                    JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void txtProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProveedorActionPerformed
        // TODO add your handling code here:
        Frame f = JOptionPane.getFrameForComponent(this);
        frmAgregarProveedor frm = new frmAgregarProveedor((JFrame) f, true);
        frm.setVisible(true);
        if(!frm.isVisible()){
            if(!frm.getProveedor_sel().equals(new Proveedor())){                
                prov_actual = frm.getProveedor_sel();
                frm.dispose();
                txtProveedor.setText(prov_actual.getNombre() + " - " + prov_actual.getRespresentante());
            }
        }
    }//GEN-LAST:event_txtProveedorActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarProducto;
    private javax.swing.JButton btnEditarProducto;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnQuitarProducto;
    private javax.swing.JButton btnSeleccionarProveedor;
    private javax.swing.JComboBox<String> cboUnidadConversion;
    private javax.swing.JComboBox<String> cboUnidadMedida;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable jTableDetalleCompra;
    private javax.swing.JPanel jpProducto;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblProducto;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotalProd;
    private javax.swing.JFormattedTextField txtFecha;
    private javax.swing.JTextField txtNFactura;
    private javax.swing.JTextField txtProveedor;
    // End of variables declaration//GEN-END:variables
}
