/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import configuracion.Gestionar;
import controlador.Venta_controlador;
import entidades.Cliente;
import entidades.DetalleVenta;
import entidades.Inventario;
import entidades.Precio;
import entidades.Venta;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Gerard
 */
public class frmVentas extends javax.swing.JInternalFrame {
    private Cliente clienteActual;
    private final Venta_controlador controlador;

    /**
     * Creates new form frmVentas
     */
    public frmVentas() {
        initComponents();
        jTableDetalleVenta.setDefaultRenderer(Object.class, new RenderizadorBoton());
        jTableDetalleVenta.setRowHeight(25);
        Object[] columnas = {"Producto", "Cantidad", "Descuento (%)", "Precio ($)", "Importe", "¿Quitar?"};
        DefaultTableModel modelo = new DefaultTableModelImpl();
        modelo.setColumnIdentifiers(columnas);
        jTableDetalleVenta.setModel(modelo);
        txtEmpleado.setText(frmMenu.usuarioActual.getEmpleado().toString());
        controlador = new Venta_controlador();
        clienteActual = new Cliente();    
        ActionListener updateClockAction = (ActionEvent e) -> {
            Locale local = new Locale("es", "SV");
            SimpleDateFormat formateador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy - hh:mm:ss a", local);
            lblReloj.setText(formateador.format(new Date()).trim()); 
        };
        Timer t = new Timer(100, updateClockAction);
        t.start();
    }
    

    private void addDetalle(Inventario inventarioActual) {
        DefaultTableModel modelo = (DefaultTableModel) jTableDetalleVenta.getModel();
        if (inventarioActual.getId() != 0) {
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
                    0,
                    inventarioActual.getPrecio().get(0).getCantidad(),
                    new BigDecimal("1").multiply(inventarioActual.getPrecio().get(0).getCantidad()),
                    new JButton("¿Quitar?")
                };
                modelo.addRow(nuevaFila);
                jTableDetalleVenta.setModel(modelo);
                /*for (int i = 0; i < modelo.getRowCount(); i++) {
                    BigDecimal cantidad = new BigDecimal(JOptionPane.showInputDialog(this, "Digita la nueva cantidad a vender"));
                    modelo.setValueAt(cantidad, fila, 1);
                    BigDecimal importe = new BigDecimal(0);
                    BigDecimal descuento = new BigDecimal(modelo.getValueAt(fila, 2).toString());
                    BigDecimal precio = new BigDecimal(modelo.getValueAt(fila, 3).toString());
                    BigDecimal subtotal = precio.multiply(cantidad);
                    descuento = descuento.divide(new BigDecimal(100));
                    importe = importe.add(subtotal);
                    descuento = descuento.multiply(importe);
                    importe = importe.subtract(descuento).setScale(2, BigDecimal.ROUND_HALF_UP);
                    modelo.setValueAt(importe, fila, 4);
                }*/
                
                jTableDetalleVenta.setModel(modelo);
                lblProducto.setText("<Producto>");
                calcularTotal();
            }
        }
    }

    private void calcularTotal() {
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal iva = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal descuentoTotal = BigDecimal.ZERO;
        for (int i = 0; i < jTableDetalleVenta.getRowCount(); i++) {
            double cantidad = Double.parseDouble(jTableDetalleVenta.getValueAt(i, 1).toString());
            double precio = Double.parseDouble(jTableDetalleVenta.getValueAt(i, 3).toString());
            double descuento = Double.parseDouble(jTableDetalleVenta.getValueAt(i, 2).toString());
            subtotal = subtotal.add(new BigDecimal(jTableDetalleVenta.getValueAt(i, 4).toString()));
            descuentoTotal = descuentoTotal.add(new BigDecimal(((cantidad * precio) * (descuento/100))));
        }
        lblSubTotal.setText(subtotal.setScale(2, RoundingMode.HALF_UP).toString());
        if(cboTipoVenta.getSelectedItem().toString().equals("Crédito Fiscal")){
            iva = subtotal.multiply(new BigDecimal(0.13));
            txtIVA.setText(iva.setScale(2, RoundingMode.HALF_UP).toString());
            total = subtotal.add(iva);
            lblTotal.setText(total.setScale(2, RoundingMode.HALF_UP).toString());
            txtDescuento.setText(descuentoTotal.setScale(2, RoundingMode.HALF_UP).toString());
        } else {
            total = subtotal;
            txtIVA.setText("0.00");
            lblTotal.setText(total.setScale(2, RoundingMode.HALF_UP).toString());
            txtDescuento.setText(descuentoTotal.setScale(2, RoundingMode.HALF_UP).toString());
        }    
    }

    private void setImagen(String url) {
        File f = new File(url);
        if(f.exists() && !f.isDirectory()){
            ImageIcon imagen = new ImageIcon(url);
            ImageIcon icono = new ImageIcon(imagen.getImage().getScaledInstance(lblImagen.getWidth(), lblImagen.getHeight(), 0));
            lblImagen.setText("");
            lblImagen.setIcon(icono);
        } else {
            lblImagen.setIcon(null);
            lblImagen.setText("El producto no tiene imagen");
        }
    }
    
    private void cambiarCantidad(){
        int fila = jTableDetalleVenta.getSelectedRow();
        if (fila > -1) {
            DefaultTableModel modelo = (DefaultTableModel) jTableDetalleVenta.getModel();
            BigDecimal cantidad = new BigDecimal(JOptionPane.showInputDialog(this, "Digita la nueva cantidad a vender"));
            modelo.setValueAt(cantidad, fila, 1);
            BigDecimal importe = new BigDecimal(0);
            BigDecimal descuento = new BigDecimal(modelo.getValueAt(fila, 2).toString());
            BigDecimal precio = new BigDecimal(modelo.getValueAt(fila, 3).toString());
            BigDecimal subtotal = precio.multiply(cantidad);
            descuento = descuento.divide(new BigDecimal(100));
            importe = importe.add(subtotal);
            descuento = descuento.multiply(importe);
            importe = importe.subtract(descuento).setScale(2, BigDecimal.ROUND_HALF_UP);
            modelo.setValueAt(importe, fila, 4);
            jTableDetalleVenta.setModel(modelo);
            calcularTotal();
        }
    }
    
    private void cambiarPrecio(){
        int fila = jTableDetalleVenta.getSelectedRow();
        if (fila > -1) {
            DefaultTableModel modelo = (DefaultTableModel) jTableDetalleVenta.getModel();
            BigDecimal cantidad = new BigDecimal(modelo.getValueAt(fila, 1).toString());
            BigDecimal importe = new BigDecimal(0);
            BigDecimal descuento = new BigDecimal(modelo.getValueAt(fila, 2).toString());
            Inventario inv = (Inventario) modelo.getValueAt(fila, 0);
            JComboBox jcboDialog = new JComboBox(inv.getPrecio().toArray());
            JOptionPane.showMessageDialog(this, jcboDialog, "Selecciona el precio con el cual se vendera", JOptionPane.INFORMATION_MESSAGE);
            BigDecimal precio = ((Precio) jcboDialog.getSelectedItem()).getCantidad();
            modelo.setValueAt(precio, fila, 3);
            BigDecimal subtotal = precio.multiply(cantidad);
            descuento = descuento.divide(new BigDecimal(100));
            importe = importe.add(subtotal);
            descuento = descuento.multiply(importe);
            importe = importe.subtract(descuento).setScale(2, BigDecimal.ROUND_HALF_UP);
            modelo.setValueAt(importe, fila, 4);
            jTableDetalleVenta.setModel(modelo);
            calcularTotal();
        }
    }
    
    private void cambiarDescuento(){
        int fila = jTableDetalleVenta.getSelectedRow();
        if (fila > -1) {
            DefaultTableModel modelo = (DefaultTableModel) jTableDetalleVenta.getModel();
            BigDecimal cantidad = new BigDecimal(modelo.getValueAt(fila, 1).toString());
            BigDecimal importe = new BigDecimal(0);  
            BigDecimal descuento = new BigDecimal(JOptionPane.showInputDialog(this, "Digita el descuento a otorgar en el producto \n Usa cantidades entre 0 y 100"));
            modelo.setValueAt(descuento, fila, 2);
            BigDecimal precio = new BigDecimal(modelo.getValueAt(fila, 3).toString());
            BigDecimal subtotal = precio.multiply(cantidad);
            descuento = descuento.divide(new BigDecimal(100));
            importe = importe.add(subtotal);
            descuento = descuento.multiply(importe);
            importe = importe.subtract(descuento).setScale(2, BigDecimal.ROUND_HALF_UP);
            modelo.setValueAt(importe, fila, 4);
            jTableDetalleVenta.setModel(modelo);
            calcularTotal();
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
        jLabel20 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblReloj = new javax.swing.JLabel();
        txtNFactura = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        btnBuscarProducto = new javax.swing.JButton();
        btnGuardarVenta = new javax.swing.JButton();
        btnSeleccionarPrecio = new javax.swing.JButton();
        btnCambiarCant = new javax.swing.JButton();
        btnHacerDescuento = new javax.swing.JButton();
        btnQuitarProducto = new javax.swing.JButton();
        btnBuscarCliente = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        lblImagen = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        cboTipoVenta = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        txtCliente = new javax.swing.JTextField();
        lblProducto = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lblCodigo = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableDetalleVenta = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        txtEmpleado = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtDescuento = new javax.swing.JTextField();
        txtComentarios = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        lblTotal = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtIVA = new javax.swing.JTextField();
        jSeparator4 = new javax.swing.JSeparator();
        lblSubTotal = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();

        setClosable(true);
        setMaximizable(true);
        setTitle("Punto de Venta");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/inventario16.png"))); // NOI18N
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(0, 153, 204));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel20.setText("N° de Factura:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/compras16.png"))); // NOI18N
        jLabel4.setText("REGISTRRO DE VENTAS");

        lblReloj.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblReloj.setText("<Hora y Fecha>");

        txtNFactura.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtNFactura.setForeground(new java.awt.Color(0, 102, 153));
        txtNFactura.setAlignmentX(1.0F);
        txtNFactura.setAlignmentY(1.0F);
        txtNFactura.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        txtNFactura.setMargin(new java.awt.Insets(2, 3, 2, 3));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(82, 82, 82)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75)
                .addComponent(lblReloj, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNFactura, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblReloj, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE))))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(153, 153, 153));

        btnBuscarProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnBuscarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/search24.png"))); // NOI18N
        btnBuscarProducto.setText("Buscar");
        btnBuscarProducto.setToolTipText("Buscar Producto");
        btnBuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarProductoActionPerformed(evt);
            }
        });

        btnGuardarVenta.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnGuardarVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/cerrarventa24.png"))); // NOI18N
        btnGuardarVenta.setText("Guardar");
        btnGuardarVenta.setToolTipText("Cerrar Venta");
        btnGuardarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarVentaActionPerformed(evt);
            }
        });

        btnSeleccionarPrecio.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSeleccionarPrecio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/numberp24.png"))); // NOI18N
        btnSeleccionarPrecio.setText("Precio");
        btnSeleccionarPrecio.setToolTipText("Cambiar el Numero de Precio");
        btnSeleccionarPrecio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeleccionarPrecioActionPerformed(evt);
            }
        });

        btnCambiarCant.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnCambiarCant.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/cantidad24.png"))); // NOI18N
        btnCambiarCant.setText("Cantidad");
        btnCambiarCant.setToolTipText("Modificar Cantidad");
        btnCambiarCant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCambiarCantActionPerformed(evt);
            }
        });

        btnHacerDescuento.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnHacerDescuento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/percent24.png"))); // NOI18N
        btnHacerDescuento.setText("Descuento");
        btnHacerDescuento.setToolTipText("Aplicar Descuento");
        btnHacerDescuento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHacerDescuentoActionPerformed(evt);
            }
        });

        btnQuitarProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnQuitarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/del24.png"))); // NOI18N
        btnQuitarProducto.setText("Quitar");
        btnQuitarProducto.setToolTipText("Remover Producto Seleccionado");
        btnQuitarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarProductoActionPerformed(evt);
            }
        });

        btnBuscarCliente.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnBuscarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/client24.png"))); // NOI18N
        btnBuscarCliente.setText("Cliente");
        btnBuscarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarClienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnHacerDescuento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBuscarProducto, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                    .addComponent(btnGuardarVenta, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                    .addComponent(btnSeleccionarPrecio, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                    .addComponent(btnCambiarCant, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                    .addComponent(btnQuitarProducto, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                    .addComponent(btnBuscarCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(14, 14, 14))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnBuscarProducto, btnCambiarCant, btnGuardarVenta, btnQuitarProducto, btnSeleccionarPrecio});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addComponent(btnBuscarProducto, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addComponent(btnGuardarVenta, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addComponent(btnSeleccionarPrecio, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addComponent(btnCambiarCant, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addComponent(btnQuitarProducto, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addComponent(btnHacerDescuento, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addComponent(btnBuscarCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                .addGap(43, 43, 43))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        lblImagen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblImagen.setText("Selecciona un producto");
        lblImagen.setToolTipText("Imagen del Producto");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblImagen)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel2.setText("Documento:");

        cboTipoVenta.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cboTipoVenta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ticket", "Factura", "Crédito Fiscal" }));
        cboTipoVenta.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboTipoVentaItemStateChanged(evt);
            }
        });

        jLabel7.setText("Cliente:");

        txtCliente.setEditable(false);
        txtCliente.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCliente.setText("-");
        txtCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtClienteMouseClicked(evt);
            }
        });
        txtCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtClienteActionPerformed(evt);
            }
        });

        lblProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblProducto.setForeground(new java.awt.Color(0, 102, 255));
        lblProducto.setText("<Producto>");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("Descripcion: ");

        lblCodigo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCodigo.setForeground(new java.awt.Color(0, 102, 255));
        lblCodigo.setText("<codigo>");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setText("Código");

        jTableDetalleVenta.setFont(new java.awt.Font("Lucida Sans", 1, 20)); // NOI18N
        jTableDetalleVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null}
            },
            new String [] {
                "Descripcion", "Cant", "Desc", "PrecioU", "Importe"
            }
        ));
        jTableDetalleVenta.setGridColor(new java.awt.Color(102, 153, 255));
        jTableDetalleVenta.setSelectionBackground(new java.awt.Color(102, 102, 102));
        jTableDetalleVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableDetalleVentaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTableDetalleVentaMouseEntered(evt);
            }
        });
        jScrollPane1.setViewportView(jTableDetalleVenta);

        jLabel10.setText("Empleado:");

        txtEmpleado.setEditable(false);
        txtEmpleado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtEmpleado.setText("-");
        txtEmpleado.setEnabled(false);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("I.V.A:");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 102, 153));
        jLabel12.setText("Total:");

        jLabel13.setText("Comentarios:");

        txtDescuento.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtDescuento.setText("0.00");
        txtDescuento.setToolTipText("Total Descontado");
        txtDescuento.setEnabled(false);

        txtComentarios.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        lblTotal.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblTotal.setText("0.00");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setText("Descuento:");

        txtIVA.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtIVA.setText("0.00");
        txtIVA.setToolTipText("Total Descontado");
        txtIVA.setEnabled(false);

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);

        lblSubTotal.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblSubTotal.setText("0.00");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 102, 153));
        jLabel15.setText("Subtotal:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(147, Short.MAX_VALUE)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(cboTipoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(txtCliente)
                                        .addGap(12, 12, 12))))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lblCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(34, 34, 34)
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jSeparator2))
                                .addGap(12, 12, 12))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jSeparator1)
                                .addContainerGap())))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator3)
                            .addComponent(jScrollPane1)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtComentarios)
                                    .addComponent(txtEmpleado))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel11))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtIVA, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                                    .addComponent(txtDescuento))))
                        .addGap(12, 12, 12))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(cboTipoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(lblCodigo)
                            .addComponent(lblProducto)
                            .addComponent(jLabel8))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(7, 7, 7)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtComentarios)
                                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtIVA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblTotal))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblSubTotal)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarProductoActionPerformed
        // TODO add your handling code here:
        Frame f = JOptionPane.getFrameForComponent(this);
        frmAgregarProducto frm = new frmAgregarProducto(f, true);
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
        if (frm.isVisible() == false) {
            if (frm.getInv_seleccion().getStock().compareTo(BigDecimal.ZERO) > 0) {
                addDetalle(frm.getInv_seleccion());
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No hay existencias del producto " + frm.getInv_seleccion() + " para vender",
                        this.getTitle(),
                        JOptionPane.WARNING_MESSAGE
                );
            }
            frm.dispose();
        }
    }//GEN-LAST:event_btnBuscarProductoActionPerformed

    private void btnSeleccionarPrecioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeleccionarPrecioActionPerformed
        // TODO add your handling code here:
        cambiarPrecio();
    }//GEN-LAST:event_btnSeleccionarPrecioActionPerformed

    private void btnGuardarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarVentaActionPerformed
        // TODO add your handling code here:
        Frame f = JOptionPane.getFrameForComponent(this);
        frmPagoVenta frm = new frmPagoVenta(f, true);
        Venta venta = new Venta();
        venta.setCliente(clienteActual);
        venta.setEmpleado(frmMenu.usuarioActual.getEmpleado());
        venta.setObservacion(txtComentarios.getText());
        venta.setFecha(new Date());
        venta.setTotal(new BigDecimal(lblTotal.getText()));
        venta.setLetras(new Validaciones().Convertir(lblTotal.getText(), true));
        venta.setSubtotal(new BigDecimal(lblSubTotal.getText()));
        venta.setIva(new BigDecimal(txtIVA.getText()));
        List<DetalleVenta> detalles = new ArrayList<>();
        for (int i = 0; i < jTableDetalleVenta.getRowCount(); i++) {
            DetalleVenta detalle = new DetalleVenta();
            Inventario extraer = (Inventario) jTableDetalleVenta.getValueAt(i, 0);
            detalle.setInventario(extraer);
            detalle.setCantidad(new BigDecimal(jTableDetalleVenta.getValueAt(i, 1).toString()));
            detalle.setDescuento(Integer.parseInt(jTableDetalleVenta.getValueAt(i, 2).toString()));
            detalle.setPrecio(new BigDecimal(jTableDetalleVenta.getValueAt(i, 3).toString()));
            detalle.setImporte(new BigDecimal(jTableDetalleVenta.getValueAt(i, 4).toString()));
            detalles.add(detalle);
        }
        venta.setDetalle(detalles);
        if (txtNFactura.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "La venta es por Credito Fiscal o Factura de consumidor final y es obligatorio poner numero de factura",
                    new Gestionar().Leer("Empresa", "nombre"),
                    JOptionPane.WARNING_MESSAGE);
        } else {
            venta.setNFactura(txtNFactura.getText());
            if (jTableDetalleVenta.getRowCount() > 0 || clienteActual.getId() > 0) {
                frm.setVentaActual(venta);
                frm.setLocationRelativeTo(null);
                frm.setVisible(true);
                if (frm.isVisible() == false) {
                    if(frm.getVentaActual().getCambio().compareTo(BigDecimal.ZERO) > -1){
                        venta.setCambio(frm.getVentaActual().getCambio());
                        if (controlador.Registrar(venta)) {
                            JOptionPane.showMessageDialog(this, "Venta registrada", this.title, JOptionPane.INFORMATION_MESSAGE);
                            Validaciones.reiniciarJTable(jTableDetalleVenta);
                            txtCliente.setText("");
                            txtComentarios.setText("");
                            lblTotal.setText("0.00");
                            lblImagen.setIcon(null);
                            lblImagen.setText("Selecciona un producto");
                            txtNFactura.setText("");
                            lblCodigo.setText("");
                        }
                    }
                }
            } else if (clienteActual.getId() == 0) {
                JOptionPane.showMessageDialog(this,
                        "Agrega al cliente",
                        new Gestionar().Leer("Empresa", "nombre"),
                        JOptionPane.WARNING_MESSAGE);
                System.out.println("El Cliente");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Agrega Productos primero",
                        new Gestionar().Leer("Empresa", "nombre"),
                        JOptionPane.WARNING_MESSAGE);
            }System.out.println("El Productos");
        }
        
        
    }//GEN-LAST:event_btnGuardarVentaActionPerformed

    private void jTableDetalleVentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDetalleVentaMouseClicked
        // TODO add your handling code here:
        int fila = jTableDetalleVenta.getSelectedRow();
        if (fila > -1) {
            Inventario inv = (Inventario) jTableDetalleVenta.getValueAt(fila, 0);
            if(inv.getImagen().getId() == 0){
                setImagen("");
            } else {
                setImagen(inv.getImagen().getUrl());
            }
            lblProducto.setText(inv.getArticulo().toString());
            lblCodigo.setText(inv.getCodigoInterno());
            int columna = jTableDetalleVenta.getSelectedColumn();
            if (jTableDetalleVenta.getValueAt(fila, columna) instanceof JButton) {
                int respuesta = JOptionPane.showConfirmDialog(this, "¿Estas seguro de eliminar estos datos?", new Gestionar().Leer("Empresa", "nombre"),
                        JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    DefaultTableModel modelo = (DefaultTableModel) jTableDetalleVenta.getModel();
                    modelo.removeRow(fila);
                    jTableDetalleVenta.setModel(modelo);
                    calcularTotal();
                }
            } else if(columna == 1){
                cambiarCantidad();
            } else if(columna == 2){
                cambiarDescuento();
            } else if(columna == 3){
                cambiarPrecio();
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Selecciona primero",
                    new Gestionar().Leer("Empresa", "nombre"),
                    JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jTableDetalleVentaMouseClicked

    private void btnCambiarCantActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCambiarCantActionPerformed
        // TODO add your handling code here:
        cambiarCantidad();
    }//GEN-LAST:event_btnCambiarCantActionPerformed

    private void btnHacerDescuentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHacerDescuentoActionPerformed
        // TODO add your handling code here:
        cambiarDescuento();
    }//GEN-LAST:event_btnHacerDescuentoActionPerformed

    private void btnBuscarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarClienteActionPerformed
        // TODO add your handling code here:
        Frame f = JOptionPane.getFrameForComponent(this);
        frmAgregarCliente frm = new frmAgregarCliente(f, true);
        frm.setVisible(true);
        if (frm.isVisible() == false) {
            if(frm.getCliente().getId() != 0){
                clienteActual = frm.getCliente();
                txtCliente.setText(clienteActual.toString());
            }
            frm.dispose();
        }
    }//GEN-LAST:event_btnBuscarClienteActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // TODO add your handling code here:
    }//GEN-LAST:event_formInternalFrameOpened

    private void txtClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtClienteMouseClicked
        // TODO add your handling code here:
        Frame f = JOptionPane.getFrameForComponent(this);
        frmAgregarCliente frm = new frmAgregarCliente(f, true);
        frm.setVisible(true);
        if (frm.isVisible() == false) {
            clienteActual = frm.getCliente();
            txtCliente.setText(clienteActual.toString());
            frm.dispose();
        }
    }//GEN-LAST:event_txtClienteMouseClicked

    private void cboTipoVentaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboTipoVentaItemStateChanged
        // TODO add your handling code here:
        if(cboTipoVenta.getSelectedItem().toString().equals("Ticket")){
            jLabel20.setText("N° Correlativo");
        } else {
            jLabel20.setText("N° Factura");
        }
        calcularTotal();
    }//GEN-LAST:event_cboTipoVentaItemStateChanged

    private void txtClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClienteActionPerformed
        // TODO add your handling code here:
        Frame f = JOptionPane.getFrameForComponent(this);
        frmAgregarCliente frm = new frmAgregarCliente(f, true);
        frm.setVisible(true);
        if (frm.isVisible() == false) {
            clienteActual = frm.getCliente();
            txtCliente.setText(clienteActual.toString());
            frm.dispose();
        }
    }//GEN-LAST:event_txtClienteActionPerformed

    private void btnQuitarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnQuitarProductoActionPerformed

    private void jTableDetalleVentaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDetalleVentaMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableDetalleVentaMouseEntered


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarCliente;
    private javax.swing.JButton btnBuscarProducto;
    private javax.swing.JButton btnCambiarCant;
    private javax.swing.JButton btnGuardarVenta;
    private javax.swing.JButton btnHacerDescuento;
    private javax.swing.JButton btnQuitarProducto;
    private javax.swing.JButton btnSeleccionarPrecio;
    private javax.swing.JComboBox<String> cboTipoVenta;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTable jTableDetalleVenta;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblImagen;
    private javax.swing.JLabel lblProducto;
    private javax.swing.JLabel lblReloj;
    private javax.swing.JLabel lblSubTotal;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JTextField txtCliente;
    private javax.swing.JTextField txtComentarios;
    private javax.swing.JTextField txtDescuento;
    private javax.swing.JTextField txtEmpleado;
    private javax.swing.JTextField txtIVA;
    private javax.swing.JTextField txtNFactura;
    // End of variables declaration//GEN-END:variables
}
