/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import configuracion.Gestionar;
import entidades.Usuario;
import javax.swing.JOptionPane;

/**
 *
 * @author Gerard
 */
public class frmMenuPrincipal extends javax.swing.JFrame {
    public static Usuario usuarioActual;
    /**
     * Creates new form frmMenuPrincipal
     */
    public frmMenuPrincipal() {
        initComponents();
        lblEmpleado.setText(usuarioActual.getEmpleado().toString());
        setExtendedState(MAXIMIZED_BOTH); 
        setTitle(new configuracion.Gestionar().Leer("Empresa","nombre"));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tblSesion = new javax.swing.JToolBar();
        lblEmpleado = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        miProducto = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem28 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenuItem25 = new javax.swing.JMenuItem();
        jMenuItem26 = new javax.swing.JMenuItem();
        jMenuItem27 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMenuItem23 = new javax.swing.JMenuItem();
        jMenuItem24 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tblSesion.setRollover(true);

        lblEmpleado.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblEmpleado.setText("jLabel1");
        tblSesion.add(lblEmpleado);

        jMenu1.setText("Operaciones");
        jMenu1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        miProducto.setText("Articulos");
        miProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miProductoActionPerformed(evt);
            }
        });
        jMenu1.add(miProducto);

        jMenuItem2.setText("Clientes");
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("Proveedores");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem4.setText("Compras");
        jMenu1.add(jMenuItem4);

        jMenuItem6.setText("Ventas");
        jMenu1.add(jMenuItem6);

        jMenuItem7.setText("Inventario Inicial");
        jMenu1.add(jMenuItem7);

        jMenuItem28.setText("Corte de Caja");
        jMenu1.add(jMenuItem28);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Consultas");
        jMenu2.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N

        jMenuItem5.setText("Compras");
        jMenu2.add(jMenuItem5);

        jMenuItem8.setText("Ventas");
        jMenu2.add(jMenuItem8);

        jMenuItem9.setText("Proveedores");
        jMenu2.add(jMenuItem9);

        jMenuItem10.setText("Clientes");
        jMenu2.add(jMenuItem10);

        jMenuItem11.setText("Articulos");
        jMenu2.add(jMenuItem11);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Procesos");
        jMenu3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jMenuItem12.setText("Respaldos");
        jMenu3.add(jMenuItem12);

        jMenuItem13.setText("Importar a Excel");
        jMenu3.add(jMenuItem13);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Reportes");
        jMenu4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jMenuItem14.setText("Ventas");
        jMenu4.add(jMenuItem14);

        jMenuItem15.setText("Compras");
        jMenu4.add(jMenuItem15);

        jMenuItem16.setText("Proveedores");
        jMenu4.add(jMenuItem16);

        jMenuItem17.setText("Articulos");
        jMenu4.add(jMenuItem17);

        jMenuItem18.setText("Clientes");
        jMenu4.add(jMenuItem18);

        jMenuItem19.setText("Farmacias");
        jMenu4.add(jMenuItem19);

        jMenuBar1.add(jMenu4);

        jMenu5.setText("Sistema");
        jMenu5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jMenu6.setText("Administrar");
        jMenu6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jMenuItem21.setText("Empleados");
        jMenu6.add(jMenuItem21);

        jMenuItem25.setText("Usuarios");
        jMenu6.add(jMenuItem25);

        jMenuItem26.setText("Roles");
        jMenu6.add(jMenuItem26);

        jMenuItem27.setText("Operatividad");
        jMenu6.add(jMenuItem27);

        jMenu5.add(jMenu6);

        jMenuItem20.setText("Empresa");
        jMenu5.add(jMenuItem20);

        jMenuItem22.setText("Impresora");
        jMenu5.add(jMenuItem22);

        jMenuItem23.setText("Unidades");
        jMenu5.add(jMenuItem23);

        jMenuItem24.setText("jMenuItem24");
        jMenu5.add(jMenuItem24);

        jMenuBar1.add(jMenu5);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tblSesion, javax.swing.GroupLayout.DEFAULT_SIZE, 607, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(251, Short.MAX_VALUE)
                .addComponent(tblSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void miProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miProductoActionPerformed
        // TODO add your handling code here:
        frmAgregarProductos frm = new frmAgregarProductos();
        frm.setVisible(true);
    }//GEN-LAST:event_miProductoActionPerformed

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
            java.util.logging.Logger.getLogger(frmMenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmMenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmMenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmMenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmMenuPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem23;
    private javax.swing.JMenuItem jMenuItem24;
    private javax.swing.JMenuItem jMenuItem25;
    private javax.swing.JMenuItem jMenuItem26;
    private javax.swing.JMenuItem jMenuItem27;
    private javax.swing.JMenuItem jMenuItem28;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JLabel lblEmpleado;
    private javax.swing.JMenuItem miProducto;
    private javax.swing.JToolBar tblSesion;
    // End of variables declaration//GEN-END:variables
}
