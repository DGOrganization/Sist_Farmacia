/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;


import com.toedter.calendar.JDateChooser;
import entidades.Departamento;
import entidades.Menu;
import entidades.Municipio;
import entidades.Usuario;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.MenuElement;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author darkpastiursSennin
 */
public class Validaciones {
    
    public void iniciarSesion(Usuario pUsuario, MenuElement[] menus){
        if(pUsuario.getNivel().isEstado()){
            for (MenuElement menu : menus) {
                if (menu.getSubElements().length > 0) {
                    if (menu instanceof JPopupMenu) {
                        if (menu.getSubElements().length > 0) {
                            for (MenuElement submenu : menu.getSubElements()) {
                                for (Menu permiso : pUsuario.getNivel().getMenus()) {
                                    String menuName = ((JMenuItem) submenu).getText();
                                    if (menuName.toUpperCase().equals(permiso.getNombre().toUpperCase())) {
                                        ((JMenuItem) submenu).setEnabled(permiso.isPermiso());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    iniciarSesion(pUsuario, menu.getSubElements());
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Este nivel ha sido eliminado y por tanto"+
                    "\nlos permisos han sido revocados");
            cerrarSesion(menus);
        }
    }
    
    
    public void cerrarSesion(MenuElement[] menus){
        for(MenuElement menu : menus){
            if(menu.getSubElements().length > 0){
                if(menu instanceof JPopupMenu){
                    if(menu.getSubElements().length > 0){
                        for(MenuElement submenu : menu.getSubElements()){
                            String excepciones = ((JMenuItem) submenu).getText();
                            if(excepciones.equals("Manual de Usuario") || excepciones.equals("Acerca de") || excepciones.equals("Salir del Sistema")){
                                ((JMenuItem) submenu).setEnabled(true);  
                            } else {
                                ((JMenuItem) submenu).setEnabled(false); 
                            }
                        }
                    }
                }
                cerrarSesion(menu.getSubElements());
            }            
        }
    }
    
    public void duiFormato(JFormattedTextField txt, Component ventana){
        try{
            txt.setFormatterFactory(new DefaultFormatterFactory(new MaskFormatter("########-#")));            
        } catch(Exception ex){
            JOptionPane.showMessageDialog(ventana, "Error: " + ex.getMessage(), "Sistema de Compra y Ventas - Validaciones", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void nitFormato(JFormattedTextField txt, Component ventana){
        try{
            txt.setFormatterFactory(new DefaultFormatterFactory(new MaskFormatter("####-######-###-#")));            
        } catch(Exception ex){
            JOptionPane.showMessageDialog(ventana, "Error: " + ex.getMessage(), "Sistema de Compra y Ventas - Validaciones", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void nrcFormato(JFormattedTextField txt, Component ventana){
        try{
            txt.setFormatterFactory(new DefaultFormatterFactory(new MaskFormatter("########")));            
        } catch(Exception ex){
            JOptionPane.showMessageDialog(ventana, "Error: " + ex.getMessage(), "Sistema de Compra y Ventas - Validaciones", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void telefonoFormato(JFormattedTextField txt, Component ventana){
        try{
            txt.setFormatterFactory(new DefaultFormatterFactory(new MaskFormatter("####-####")));            
        } catch(Exception ex){
            JOptionPane.showMessageDialog(ventana, "Error: " + ex.getMessage(), "Sistema de Compra y Ventas - Validaciones", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void preciosFormato(JFormattedTextField txt, Component ventana){
        try{
            DecimalFormat df = new DecimalFormat("#.##");
            txt = new JFormattedTextField(df);
        } catch(Exception ex){
            JOptionPane.showMessageDialog(ventana, "Error: " + ex.getMessage(), "Sistema de Compra y Ventas - Validaciones", JOptionPane.ERROR_MESSAGE);
        }
    }
    
       
    public void estadosBotones(int codigo, JButton[] botones){
        if(botones.length > 0){
            switch(codigo){
                case 1: //Estado por defecto o cuando se inicia el formulario
                    botones[0].setEnabled(true); //btnNuevo
                    botones[1].setEnabled(false); //btnGuardar
                    botones[2].setEnabled(false); //btnEditar
                    botones[3].setEnabled(false); //btnEliminar
                    botones[4].setEnabled(true); //btnBuscar
                    botones[5].setEnabled(false); //btnCancelar
//                    botones[6].setEnabled(true); //btnSalir
                    break;
                case 2: //Estado para Nuevo o Editar registro
                    botones[0].setEnabled(false); //btnNuevo
                    botones[1].setEnabled(true); //btnGuardar
                    botones[2].setEnabled(false); //btnEditar
                    botones[3].setEnabled(false); //btnEliminar
                    botones[4].setEnabled(false); //btnBuscar
                    botones[5].setEnabled(true); //btnCancelar
//                    botones[6].setEnabled(false); //btnSalir
                    break;
                case 3: //Estado para cuando se le de al boton de Buscar
                    botones[0].setEnabled(true); //btnNuevo
                    botones[1].setEnabled(false); //btnGuardar
                    botones[2].setEnabled(true); //btnEditar
                    botones[3].setEnabled(true); //btnEliminar
                    botones[4].setEnabled(true); //btnBuscar
                    botones[5].setEnabled(false); //btnCancelar
//                    botones[6].setEnabled(true); //btnSalir
                    break;   
                default:
                    System.out.println("Error opcion no existe");
            }
        }
    }
    
    public void habilitarComponentes(Component[] form){
        for(Component componente : form){
            if(componente instanceof JRootPane){
                habilitarComponentes(((JRootPane) componente).getComponents());
            } else if(componente instanceof JPanel){
                habilitarComponentes(((JPanel) componente).getComponents());
            } else if(componente instanceof JLayeredPane){
                habilitarComponentes(((JLayeredPane) componente).getComponents());
            } else if(componente instanceof JScrollPane){
                habilitarComponentes(((JScrollPane) componente).getComponents());
            } else if(componente instanceof JViewport){
                habilitarComponentes(((JViewport) componente).getComponents());
            } else if((componente instanceof JTextField) || (componente instanceof JFormattedTextField)
                     || (componente instanceof JCheckBox) || (componente instanceof JComboBox) 
                     || (componente instanceof JTextArea) || (componente instanceof JSpinner)){
                componente.setEnabled(true);
            }
        }
    }
    
   /* public void habilitarComponentes(JDateChooser dc){
        dc.setEnabled(true);
    }*/
    
    public void deshabilitarComponentes(Component[] form){
        for(Component componente : form){
            if(componente instanceof JRootPane){
                deshabilitarComponentes(((JRootPane) componente).getComponents());
            } else if(componente instanceof JPanel){
                deshabilitarComponentes(((JPanel) componente).getComponents());
            } else if(componente instanceof JLayeredPane){
                deshabilitarComponentes(((JLayeredPane) componente).getComponents());
            } else if(componente instanceof JScrollPane){
                deshabilitarComponentes(((JScrollPane) componente).getComponents());
            } else if(componente instanceof JViewport){
                deshabilitarComponentes(((JViewport) componente).getComponents());
            } else if((componente instanceof JTextField) || (componente instanceof JFormattedTextField)
                     || (componente instanceof JCheckBox) || (componente instanceof JComboBox) 
                     || (componente instanceof JTextArea) || (componente instanceof JSpinner)){
                componente.setEnabled(false);
            }
        }
    }
    
    /*public void deshabilitarComponentes(JDateChooser dc){
        dc.setEnabled(false);
    }*/
    
    public void limpiarComponentes(Component[] form){
        for(Component componente : form){
            if(componente instanceof JRootPane){
                limpiarComponentes(((JRootPane) componente).getComponents());
            } else if(componente instanceof JPanel){
                limpiarComponentes(((JPanel) componente).getComponents());
            } else if(componente instanceof JLayeredPane){
                limpiarComponentes(((JLayeredPane) componente).getComponents());
            } else if(componente instanceof JScrollPane){
                limpiarComponentes(((JScrollPane) componente).getComponents());
            } else if(componente instanceof JViewport){
                limpiarComponentes(((JViewport) componente).getComponents());
            } /*else if((componente instanceof JDateChooser)){
                ((JDateChooser) componente).setDate(new Date());
            }*/ else if((componente instanceof JTextField) || (componente instanceof JFormattedTextField)){
                ((JTextField)componente).setText("");
            } else if(componente instanceof JTextArea){
                ((JTextArea) componente).setText("");
            } else if(componente instanceof JCheckBox){
                ((JCheckBox) componente).setSelected(false);
            } else if(componente instanceof JComboBox){
                ((JComboBox) componente).setSelectedIndex(0);
            }
        }
    }
    
    /*public void cargarNiveles(JComboBox cbo, List<Nivel> pNiveles){
        pNiveles.stream().forEach((nivel) -> {
            if((!nivel.getNombre().toUpperCase().contains("administrador".toUpperCase())) && nivel.isEstado())
                cbo.addItem(nivel);
        });
    }*/
    public void cboDepto(JComboBox cboPrincipal, List<Departamento> pDepto){
        pDepto.forEach(datos -> {
            cboPrincipal.addItem(datos);
        });        
    }
    
    public void cboMuni(JComboBox cboPrincipal, List<Municipio> pDepto){
        pDepto.forEach(datos -> {
            cboPrincipal.addItem(datos);
        });        
    }
    
    public void cboLoad(JComboBox cbo, List<Object> pLista){
        pLista.stream().forEach(datos -> {
            cbo.addItem(datos);
        });
    }
    public boolean validarCamposTexto(JTextField text){
        if(!text.getText().trim().equals("")){
            return true;
        } else {
            JOptionPane.showMessageDialog(text,
                    "Debes llenar este campo", "Sistema de Compras y Ventas - Validaciones",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean validarCamposTexto(JTextArea text){
        if(!text.getText().trim().equals("")){
            return true;
        } else {
            JOptionPane.showMessageDialog(text,
                    "Debes llenar este campo", "Sistema de Compras y Ventas - Validaciones",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    final void validarSoloLetras(JTextField a){
        a.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if(String.valueOf(c).matches("^.*[A-Za-zñÑáéíóúÁÉÍÓÚ ].*$") == false){
                    e.consume();
                    a.getToolkit().beep();
                }
            }
        });
    }
    
    final void validarSoloNumeros(JTextField a){
        a.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e){
                char c = e.getKeyChar();                
                if(String.valueOf(c).matches("^.*[0-9].*$") == false){
                    e.consume();
                    a.getToolkit().beep();
                }
            }
        });
    }
    
    final void validarSoloDecimales(JTextField a){
        a.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e){
                char c = e.getKeyChar();                
                if(String.valueOf(c).matches("^.*[0-9 .].*$") == false){
                    e.consume();
                    a.getToolkit().beep();
                }
            }
        });
    }
    
    public boolean validarFechas(JDateChooser dc){
        boolean exito = false;
        Date validar = new Date();
        if (dc != null) {
            if (((JTextField) dc.getDateEditor().getUiComponent()).getText().equals("")) {
                JOptionPane.showMessageDialog(dc,
                        "Debes poner la fecha", "Sistema de Compras y Ventas - Validaciones",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                long validarFecha = validar.getYear() - dc.getDate().getYear();
                if (validarFecha < 18) {
                    JOptionPane.showMessageDialog(dc,
                            "No deberias venderle algo a un menor de 18 años",
                            "Sistema de Compras y Ventas - Validaciones",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    exito = true;
                }
            }
        } else {
            JOptionPane.showMessageDialog(dc,
                    "Debes poner la fecha", "Sistema de Compras y Ventas - Validaciones",
                    JOptionPane.ERROR_MESSAGE);
        }
        return exito;
    }
    
    public boolean validarEmail(String correo, Component componente) {
        Pattern pat = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        Matcher mat = pat.matcher(correo); 
        boolean eject = false;
        if (mat.find()) {
            eject = true;
        } else {
            JOptionPane.showMessageDialog(componente,
                    "Este correo no es valido, no se almacenara", "Sistema de Compras y Ventas - Validaciones",
                    JOptionPane.ERROR_MESSAGE);
        }
        return eject;
    }
    
    public boolean validarURL(String url, Component componente) {
        Pattern pat = Pattern.compile("^[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
        Matcher mat = pat.matcher(url); 
        boolean eject = false;
        if (mat.find()) {
            eject = true;
        } else {
            JOptionPane.showMessageDialog(componente,
                    "Esta url no es valida, no se almacenara", "Sistema de Compras y Ventas - Validaciones",
                    JOptionPane.ERROR_MESSAGE);
        }
        return eject;
    }
    
    /*public void SelectedItem(JComboBox cbo, Object value){
        for(int i = 0; i < cbo.getItemCount(); i++){
            if(cbo.getItemAt(i).toString().equals(value.toString())){
                cbo.setSelectedIndex(i);
                break;
            }
        }
    }*/
    
    public String[] editarApellidos(String apellidos){
        int primero = apellidos.indexOf(" ");
        int demas = apellidos.indexOf(" ", primero);
        
        String[] partirApellidos = null;
        if(demas > 0){ 
            partirApellidos = new String[]{
                apellidos.substring(0, primero).trim(),
                apellidos.substring(primero).trim()
            };
        }
        return partirApellidos;
    }
    
    public static String[] procesarNombres(String nombreCompleto){
        int nombre1 = nombreCompleto.indexOf(" ", 0);
        int nombre2 = nombreCompleto.indexOf(" ", nombre1+1);
        int apellido1 = nombreCompleto.indexOf(" ", nombre2+1);
        String[] partir = null;
        if(nombre1 > 0){
            partir = new String[]{
                nombreCompleto.substring(0, nombre2).trim(),
                nombreCompleto.substring(nombre2, apellido1).trim(),
                nombreCompleto.substring(apellido1, nombreCompleto.length()).trim()
            };
        }
        return partir;
    }
}
