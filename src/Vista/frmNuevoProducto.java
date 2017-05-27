/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import configuracion.Gestionar;
import controlador.Inventario_controlador;
import controlador.Categoria_controlador;
import controlador.Bodega_controlador;
import controlador.Unidad_controlador;
import entidades.Inventario;
import entidades.Articulo;
import entidades.Bodega;
import entidades.Categoria;
import entidades.Imagen;
import entidades.Precio;
import entidades.Unidad;
import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Gerard
 */
public class frmNuevoProducto extends javax.swing.JDialog {

    private Inventario inventario;
    private boolean editar = false;
    private final Inventario_controlador controlador;
    private final Validaciones validar;
    private String imagenURL;

    /**
     * Creates new form frmNuevoArticulo
     *
     * @param parent
     * @param modal
     */
    public frmNuevoProducto(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        controlador = new Inventario_controlador();
        validar = new Validaciones();
        Object[] columnas = {"Producto", "Localizacion", "Stock", "Precio ($)"};
        DefaultTableModel modelo = new ControlesGenerales.DefaultTableModelImpl();
        modelo.setColumnIdentifiers(columnas);
        jtCompatibles.setModel(modelo);
        validar.validarSoloNumeros(txtMargen1);
        validar.validarSoloNumeros(txtMargen2);
        validar.validarSoloNumeros(txtMargen3);
        validar.validarSoloDecimales(txtPrecioCompra);
        validar.validarSoloDecimales(txtPrecio1);
        validar.validarSoloDecimales(txtPrecio2);
        validar.validarSoloDecimales(txtPrecio3);
        validar.validarSoloDecimales(txtMayoreo1);
        validar.validarSoloDecimales(txtMayoreo2);
        validar.validarSoloDecimales(txtMayoreo3);
        validar.validarSoloNumeros(txtStockMin);
        validar.validarSoloNumeros(txtStockMax);
        changeText(txtMargen1);
        changeText(txtMargen2);
        changeText(txtMargen3);
        changeText(txtPrecioCompra);
//        TableColumn tc = jtCompatibles.getColumnModel().getColumn(4);
//        tc.setCellEditor(jtCompatibles.getDefaultEditor(Boolean.class));
//        tc.setCellRenderer(jtCompatibles.getDefaultRenderer(Boolean.class)); 
    }

    private void ObtenerImagen() throws FileNotFoundException, IOException {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setCurrentDirectory(new File(System.getProperty("user.home") + System.getProperty("file.separator") + "Pictures"));
        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "jpge"));
        int ventana = fc.showDialog(this, "Seleccionar");
        if (ventana == JFileChooser.APPROVE_OPTION) {
            File archivo = fc.getSelectedFile();
            setImagen(archivo.getPath());
            imagenURL = archivo.getPath();
        }
    }

    private void setImagen(String url) {
        ImageIcon imagen = new ImageIcon(url);
        ImageIcon icono = new ImageIcon(imagen.getImage().getScaledInstance(lblImagen.getWidth(), lblImagen.getHeight(), 0));
        lblImagen.setText("");
        lblImagen.setIcon(icono);
    }

    private String CopiarImagen() throws IOException {
        String urlSalida = System.getProperty("user.dir") + "\\Recursos\\Productos";
        File dir = new File(urlSalida);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                System.out.println("Si funciona");
            }
        }
        File archivo = new File(imagenURL);
        InputStream in = new FileInputStream(archivo);
        OutputStream out = new FileOutputStream(urlSalida + "\\" + archivo.getName());
        try {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.flush();
            urlSalida = urlSalida + "\\" + archivo.getName();
        } catch (IOException ex) {
            throw ex;
        } finally {
            in.close();
            out.close();
        }
        return urlSalida;
    }

    private void CalcularMargenTXT() {
        BigDecimal margen1 = new BigDecimal(0);
        BigDecimal margen2 = new BigDecimal(0);
        BigDecimal margen3 = new BigDecimal(0);
        BigDecimal precioCompra = new BigDecimal(0);
        try {
            if (txtPrecioCompra.getText().isEmpty() == false) {
                if (txtMargen1.getText().isEmpty() == false) {
                    precioCompra = new BigDecimal(txtPrecioCompra.getText());
                    margen1 = new BigDecimal(txtMargen1.getText());
                    BigDecimal precio1 = precioCompra.add(precioCompra.multiply(margen1.divide(new BigDecimal(100))));
                    txtPrecio1.setText(precio1.setScale(2, RoundingMode.HALF_UP).toString());
                }

                if (txtMargen2.getText().isEmpty() == false) {
                    margen2 = new BigDecimal(txtMargen2.getText());
                    BigDecimal precio2 = precioCompra.add(precioCompra.multiply(margen2.divide(new BigDecimal(100))));
                    txtPrecio2.setText(precio2.setScale(2, RoundingMode.HALF_UP).toString());
                }

                if (txtMargen3.getText().isEmpty() == false) {
                    margen3 = new BigDecimal(txtMargen3.getText());
                    BigDecimal precio3 = precioCompra.add(precioCompra.multiply(margen3.divide(new BigDecimal(100))));
                    txtPrecio3.setText(precio3.setScale(2, RoundingMode.HALF_UP).toString());
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "El numero digitado no es valido");
            txtPrecio1.setText("");
            txtPrecio2.setText("");
            txtPrecio3.setText("");
        }
    }

    private void changeText(JTextField txt) {
        txt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent de) {
                CalcularMargenTXT();
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                CalcularMargenTXT();
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                CalcularMargenTXT();
            }

        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpAcciones = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtStockMin = new javax.swing.JTextField();
        txtStockMax = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cboCategoria = new javax.swing.JComboBox<>();
        cboUnidad = new javax.swing.JComboBox<>();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel20 = new javax.swing.JLabel();
        txtProducto = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cboBodega = new javax.swing.JComboBox<>();
        jdcVencimiento = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea();
        jPanel6 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        txtPrecioCompra = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtMargen1 = new javax.swing.JTextField();
        txtMargen2 = new javax.swing.JTextField();
        txtMargen3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        txtPrecio1 = new javax.swing.JTextField();
        txtPrecio2 = new javax.swing.JTextField();
        txtPrecio3 = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        txtMayoreo1 = new javax.swing.JTextField();
        txtMayoreo2 = new javax.swing.JTextField();
        txtMayoreo3 = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        lblImagen = new javax.swing.JLabel();
        lblSeleccionar = new javax.swing.JLabel();
        lblBorrar = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnBuscarCompatible = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        btnQuitar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtCompatibles = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(new Gestionar().Leer("Empresa", "nombre"));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jpAcciones.setBackground(new java.awt.Color(88, 166, 137));

        btnGuardar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/save32.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.setHideActionText(true);
        btnGuardar.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnGuardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGuardar.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        btnGuardar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnCancelar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/cancel24.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.setHideActionText(true);
        btnCancelar.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnCancelar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancelar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpAccionesLayout = new javax.swing.GroupLayout(jpAcciones);
        jpAcciones.setLayout(jpAccionesLayout);
        jpAccionesLayout.setHorizontalGroup(
            jpAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpAccionesLayout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(btnGuardar)
                .addGap(243, 243, 243)
                .addComponent(btnCancelar)
                .addGap(100, 100, 100))
        );
        jpAccionesLayout.setVerticalGroup(
            jpAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpAccionesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGuardar)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Descripcion");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Categoria");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("Unidad de Venta:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Inventario Minimo:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("Inventario Maximo:");

        txtStockMin.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtStockMin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStockMinActionPerformed(evt);
            }
        });

        txtStockMax.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("DATOS DEL MEDICAMENTO");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        jLabel20.setText("Nombre:");

        txtProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductoActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Localizacion:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setText("Fecha de Vencimiento:");

        txtDescripcion.setColumns(20);
        txtDescripcion.setRows(3);
        jScrollPane2.setViewportView(txtDescripcion);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel9))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jdcVencimiento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtStockMin))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtStockMax, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(cboUnidad, javax.swing.GroupLayout.Alignment.TRAILING, 0, 151, Short.MAX_VALUE)))
                            .addComponent(jSeparator2)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cboCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboBodega, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtProducto, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2))))
                .addGap(12, 12, 12))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txtProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cboCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(cboBodega, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cboUnidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jdcVencimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtStockMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtStockMin)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Datos del Medicamento", jPanel1);

        jLabel31.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel31.setText("Precio de Compra:");

        txtPrecioCompra.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel24.setText("Margen de Utilidad %:");

        txtMargen1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtMargen2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtMargen3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(204, 0, 0));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Los precios no incluyen I.V.A");

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("PRECIOS DE VENTA");

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel27.setText("Precio de Venta:");

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/price16.png"))); // NOI18N
        jLabel26.setText("1");

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/price16.png"))); // NOI18N
        jLabel28.setText("2");

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/price16.png"))); // NOI18N
        jLabel29.setText("Precio de Caja");
        jLabel29.setToolTipText("");

        txtPrecio1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtPrecio2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtPrecio3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel30.setText("Mayoreo");

        txtMayoreo1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtMayoreo2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtMayoreo3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtMayoreo3)
                            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(txtPrecio1))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMayoreo2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPrecio2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(txtPrecio3)
                            .addComponent(txtMayoreo1))
                        .addGap(11, 11, 11))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25)
                .addGap(13, 13, 13)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(5, 5, 5)))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtPrecio2)
                    .addComponent(txtPrecio1)
                    .addComponent(txtPrecio3))
                .addGap(15, 15, 15)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtMayoreo1)
                    .addComponent(txtMayoreo2)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtMayoreo3)
                        .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator6))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator4))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel24)
                                .addGap(9, 9, 9)
                                .addComponent(txtMargen1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(25, 25, 25)
                                .addComponent(txtMargen2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22)
                                .addComponent(txtMargen3, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator5))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator3)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel31)
                                .addGap(32, 32, 32)
                                .addComponent(txtPrecioCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(14, 14, 14))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPrecioCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtMargen1)
                            .addComponent(txtMargen2)
                            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(txtMargen3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Precios", jPanel6);

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        lblImagen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblImagen.setText("Presiona Editar para buscar una imagen...");

        lblSeleccionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/edit16.png"))); // NOI18N
        lblSeleccionar.setText("Editar");
        lblSeleccionar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblSeleccionar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSeleccionarMouseClicked(evt);
            }
        });

        lblBorrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/crossdelete16.png"))); // NOI18N
        lblBorrar.setText("Borrar");
        lblBorrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblBorrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblBorrarMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblImagen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(lblSeleccionar)
                        .addGap(18, 18, 18)
                        .addComponent(lblBorrar)
                        .addGap(0, 162, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblImagen, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSeleccionar)
                    .addComponent(lblBorrar))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(139, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(139, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Imagen", jPanel3);

        btnBuscarCompatible.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnBuscarCompatible.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/search24.png"))); // NOI18N
        btnBuscarCompatible.setText("Buscar");
        btnBuscarCompatible.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarCompatibleActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Medicamentos Compatibles");

        btnQuitar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnQuitar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/delete24.png"))); // NOI18N
        btnQuitar.setText("Eliminar Medicamento");
        btnQuitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarActionPerformed(evt);
            }
        });

        jtCompatibles.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jtCompatibles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Producto", "Localizacion", "Stock", "Precio"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jtCompatibles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtCompatiblesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtCompatibles);
        if (jtCompatibles.getColumnModel().getColumnCount() > 0) {
            jtCompatibles.getColumnModel().getColumn(0).setResizable(false);
            jtCompatibles.getColumnModel().getColumn(1).setResizable(false);
            jtCompatibles.getColumnModel().getColumn(2).setResizable(false);
            jtCompatibles.getColumnModel().getColumn(3).setResizable(false);
        }

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(btnBuscarCompatible)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnQuitar)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscarCompatible)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnQuitar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Compatible", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
            .addComponent(jpAcciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpAcciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        if (txtProducto.getText().isEmpty() == false && jdcVencimiento.getDate() != null) {
            inventario.setArticulo(new Articulo(0, txtProducto.getText(), txtDescripcion.getText(), true));
            inventario.setCategoria((Categoria) cboCategoria.getSelectedItem());
            inventario.setBodega((Bodega) cboBodega.getSelectedItem());
            inventario.setUnidad((Unidad) cboUnidad.getSelectedItem());
            inventario.setStockMin(Integer.parseInt(txtStockMin.getText()));
            inventario.setStockMax(Integer.parseInt(txtStockMax.getText()));
            inventario.setVencimiento(jdcVencimiento.getDate());
            if (imagenURL != null || !imagenURL.isEmpty()) {
                try {
                    inventario.setImagen(new Imagen(0, CopiarImagen()));
                } catch (IOException ex) {
                    Logger.getLogger(frmNuevoProducto.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (!isEditar()) {
                List<Precio> precios = new ArrayList<>();
                precios.add(new Precio(0, new BigDecimal(txtPrecio1.getText()).setScale(2, BigDecimal.ROUND_DOWN), "Venta 1", true));
                precios.add(new Precio(0, new BigDecimal(txtPrecio2.getText()).setScale(2, BigDecimal.ROUND_DOWN), "Venta 2", true));
                precios.add(new Precio(0, new BigDecimal(txtPrecio3.getText()).setScale(2, BigDecimal.ROUND_DOWN), "Venta Caja", true));
                precios.add(new Precio(0, new BigDecimal(txtMayoreo1.getText()).setScale(2, BigDecimal.ROUND_DOWN), "Mayoreo 1", true));
                precios.add(new Precio(0, new BigDecimal(txtMayoreo2.getText()).setScale(2, BigDecimal.ROUND_DOWN), "Mayoreo 2", true));
                precios.add(new Precio(0, new BigDecimal(txtMayoreo3.getText()).setScale(2, BigDecimal.ROUND_DOWN), "Mayoreo Caja", true));
                List<Inventario> compatibles = new ArrayList<>();
                for (int i = 0; i < jtCompatibles.getRowCount(); i++) {
                    Inventario compatible = (Inventario) jtCompatibles.getValueAt(i, 0);
                    compatible.setEstado(!((Boolean) jtCompatibles.getValueAt(i, 0)));
                    compatibles.add(compatible);
                }
                inventario.setPrecio(precios);
                inventario.setCompatibles(compatibles);
                if (controlador.Registrar(inventario)) {
                    JOptionPane.showMessageDialog(this,
                            "El registro ha sido ingresado exitosamente",
                            new Gestionar().Leer("Empresa", "nombre"),
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                inventario.getPrecio().get(0).setCantidad(new BigDecimal(txtPrecio1.getText()).setScale(2));
                inventario.getPrecio().get(1).setCantidad(new BigDecimal(txtPrecio2.getText()).setScale(2));
                inventario.getPrecio().get(2).setCantidad(new BigDecimal(txtPrecio3.getText()).setScale(2));
                inventario.getPrecio().get(3).setCantidad(new BigDecimal(txtMayoreo1.getText()).setScale(2));
                inventario.getPrecio().get(4).setCantidad(new BigDecimal(txtMayoreo2.getText()).setScale(2));
                inventario.getPrecio().get(5).setCantidad(new BigDecimal(txtMayoreo3.getText()).setScale(2));
                List<Inventario> compatibles = inventario.getCompatibles();
                for (int i = 0; i < jtCompatibles.getRowCount(); i++) {
                    Inventario compatible = (Inventario) jtCompatibles.getValueAt(i, 0);
                    compatible.setEstado(!(Boolean.parseBoolean(jtCompatibles.getValueAt(i, 0).toString())));
                    compatibles.add(compatible);
                }
                inventario.setCompatibles(compatibles);
                int respuesta = JOptionPane.showConfirmDialog(this, "¿Estas seguro de editar estos datos?", new Gestionar().Leer("Empresa", "nombre"),
                        JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    if (controlador.Editar(inventario)) {
                        JOptionPane.showMessageDialog(this,
                                "El registro ha sido actualizado exitosamente",
                                new Gestionar().Leer("Empresa", "nombre"),
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
            this.setVisible(false);
        } else {
            if (txtProducto.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debes ingresar un nombre de articulo", new Gestionar().Leer("Empresa", "nombre"), JOptionPane.WARNING_MESSAGE);
            } else if (jdcVencimiento.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Debes ingresar la fecha", new Gestionar().Leer("Empresa", "nombre"), JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void lblSeleccionarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSeleccionarMouseClicked
        try {
            // TODO add your handling code here:
            ObtenerImagen();
        } catch (IOException ex) {
            Logger.getLogger(frmNuevoProducto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_lblSeleccionarMouseClicked

    private void lblBorrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBorrarMouseClicked
        // TODO add your handling code here:
        lblImagen.setText("Presiona Editar para buscar una imagen...");
        lblImagen.setIcon(null);
    }//GEN-LAST:event_lblBorrarMouseClicked

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        new Categoria_controlador().Obtener().stream().forEach(cboCategoria::addItem);
        new Bodega_controlador().Obtener().stream().forEach(cboBodega::addItem);
        new Unidad_controlador().Obtener().stream().forEach(cboUnidad::addItem);
        if (isEditar()) {
            txtProducto.setText(inventario.getArticulo().getNombre());
            txtDescripcion.setText(inventario.getArticulo().getDescripcion());
            cboCategoria.getModel().setSelectedItem(inventario.getCategoria());
            cboBodega.getModel().setSelectedItem(inventario.getBodega());
            cboUnidad.getModel().setSelectedItem(inventario.getUnidad());
            jdcVencimiento.setDate(new Date(inventario.getVencimiento().getTime()));
            txtStockMin.setText(String.valueOf(inventario.getStockMin()));
            txtStockMax.setText(String.valueOf(inventario.getStockMax()));
            txtPrecio1.setText(inventario.getPrecio().get(0).getCantidad().toString());
            txtPrecio2.setText(inventario.getPrecio().get(1).getCantidad().toString());
            txtPrecio3.setText(inventario.getPrecio().get(2).getCantidad().toString());
            txtMayoreo1.setText(inventario.getPrecio().get(3).getCantidad().toString());
            txtMayoreo2.setText(inventario.getPrecio().get(4).getCantidad().toString());
            txtMayoreo3.setText(inventario.getPrecio().get(5).getCantidad().toString());
            setImagen(inventario.getImagen().getUrl());
            imagenURL = inventario.getImagen().getUrl();
            DefaultTableModel modelo = (DefaultTableModel) jtCompatibles.getModel();
            inventario.getCompatibles().stream().forEach(datos -> {
                if (datos.isEstado()) {
                    Object[] nuevaFila = {
                        datos,
                        datos.getBodega(),
                        datos.getStock(),
                        datos.getPrecio().get(3),
                        false
                    };
                    modelo.addRow(nuevaFila);
                }
            });
            jtCompatibles.setModel(modelo);
        } else {
            inventario = new Inventario();
        }
    }//GEN-LAST:event_formWindowOpened

    private void txtProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductoActionPerformed

    private void btnBuscarCompatibleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarCompatibleActionPerformed
        // TODO add your handling code here:
        Frame f = JOptionPane.getFrameForComponent(this);
        frmAgregarProducto frm = new frmAgregarProducto((JFrame) f, true);
        if (inventario.getId() != 0) {
            frm.setInv_seleccion(inventario);
        }
        frm.setVisible(true);
        if (!frm.isVisible() && frm.getInv_seleccion().getId() != 0) {
            DefaultTableModel modelo = (DefaultTableModel) jtCompatibles.getModel();
            Object[] nuevaFila = {
                frm.getInv_seleccion(),
                frm.getInv_seleccion().getBodega(),
                frm.getInv_seleccion().getStock(),
                frm.getInv_seleccion().getPrecio().get(3),
                false
            };
            modelo.addRow(nuevaFila);
            jtCompatibles.setModel(modelo);
        }
    }//GEN-LAST:event_btnBuscarCompatibleActionPerformed

    private void jtCompatiblesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtCompatiblesMouseClicked
        // TODO add your handling code here:
//        int fila = jtCompatibles.getSelectedRow();
//        if (fila > -1) {
//            int columna = jtCompatibles.getSelectedColumn();
//            jtCompatibles.set
//        }
    }//GEN-LAST:event_jtCompatiblesMouseClicked

    private void btnQuitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarActionPerformed
        // TODO add your handling code here:
        int fila = jtCompatibles.getSelectedRow();
        if (fila > -1) {
            if (editar) {
                inventario.getCompatibles().get(inventario.getCompatibles().indexOf((Inventario) jtCompatibles.getValueAt(fila, 0))).setEstado(false);
            } else {
                inventario.getCompatibles().remove(inventario.getCompatibles().indexOf((Inventario) jtCompatibles.getValueAt(fila, 0)));
            }
            DefaultTableModel modelo = (DefaultTableModel) jtCompatibles.getModel();
            modelo.removeRow(fila);
            jtCompatibles.setModel(modelo);
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona primero", new Gestionar().Leer("Empresa", "nombre"), JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnQuitarActionPerformed

    private void txtStockMinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStockMinActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStockMinActionPerformed
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
            java.util.logging.Logger.getLogger(frmNuevoProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(() -> {
            frmNuevoProducto dialog = new frmNuevoProducto(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    public Inventario getInventario() {
        return inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }

    public boolean isEditar() {
        return editar;
    }

    public void setEditar(boolean editar) {
        this.editar = editar;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarCompatible;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnQuitar;
    private javax.swing.JComboBox<Bodega> cboBodega;
    private javax.swing.JComboBox<Categoria> cboCategoria;
    private javax.swing.JComboBox<Unidad> cboUnidad;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private com.toedter.calendar.JDateChooser jdcVencimiento;
    private javax.swing.JPanel jpAcciones;
    private javax.swing.JTable jtCompatibles;
    private javax.swing.JLabel lblBorrar;
    private javax.swing.JLabel lblImagen;
    private javax.swing.JLabel lblSeleccionar;
    private javax.swing.JTextArea txtDescripcion;
    private javax.swing.JTextField txtMargen1;
    private javax.swing.JTextField txtMargen2;
    private javax.swing.JTextField txtMargen3;
    private javax.swing.JTextField txtMayoreo1;
    private javax.swing.JTextField txtMayoreo2;
    private javax.swing.JTextField txtMayoreo3;
    private javax.swing.JTextField txtPrecio1;
    private javax.swing.JTextField txtPrecio2;
    private javax.swing.JTextField txtPrecio3;
    private javax.swing.JTextField txtPrecioCompra;
    private javax.swing.JTextField txtProducto;
    private javax.swing.JTextField txtStockMax;
    private javax.swing.JTextField txtStockMin;
    // End of variables declaration//GEN-END:variables
}
