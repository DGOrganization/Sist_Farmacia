/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;


import com.toedter.calendar.JDateChooser;
import entidades.Bodega;
import entidades.Categoria;
import entidades.Departamento;
import entidades.Menu;
import entidades.Municipio;
import entidades.Unidad;
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
    private final String[] UNIDADES = {"", "un ", "dos ", "tres ", "cuatro ", "cinco ", "seis ", "siete ", "ocho ", "nueve "};
    private final String[] DECENAS = {"diez ", "once ", "doce ", "trece ", "catorce ", "quince ", "dieciseis ",
        "diecisiete ", "dieciocho ", "diecinueve", "veinte ", "treinta ", "cuarenta ",
        "cincuenta ", "sesenta ", "setenta ", "ochenta ", "noventa "};
    private final String[] CENTENAS = {"", "ciento ", "doscientos ", "trecientos ", "cuatrocientos ", "quinientos ", "seiscientos ",
        "setecientos ", "ochocientos ", "novecientos "};

    public String Convertir(String numero, boolean mayusculas) {
        String literal = "";
        String parte_decimal;
        //si el numero utiliza (.) en lugar de (,) -> se reemplaza
        numero = numero.replace(".", ",");
        //si el numero no tiene parte decimal, se le agrega ,00
        if (numero.indexOf(",") == -1) {
            numero = numero + ",00";
        }
        if (Pattern.matches("\\d{1,9},\\d{1,2}", numero)) {
            //se divide el numero 0000000,00 -> entero y decimal
            String Num[] = numero.split(",");
            //de da formato al numero decimal
            parte_decimal = Num[1] + "/100 DOLARES";
            //se convierte el numero a literal
            if (Integer.parseInt(Num[0]) == 0) {//si el valor es cero
                literal = "cero ";
            } else if (Integer.parseInt(Num[0]) > 999999) {//si es millon
                literal = getMillones(Num[0]);
            } else if (Integer.parseInt(Num[0]) > 999) {//si es miles
                literal = getMiles(Num[0]);
            } else if (Integer.parseInt(Num[0]) > 99) {//si es centena
                literal = getCentenas(Num[0]);
            } else if (Integer.parseInt(Num[0]) > 9) {//si es decena
                literal = getDecenas(Num[0]);
            } else {//sino unidades -> 9
                literal = getUnidades(Num[0]);
            }
            //devuelve el resultado en mayusculas o minusculas
            if (mayusculas) {
                return (literal + parte_decimal).toUpperCase();
            } else {
                return (literal + parte_decimal);
            }
        } else {//error, no se puede convertir
            return literal = null;
        }
    }

    private String getUnidades(String numero) {// 1 - 9
        //si tuviera algun 0 antes se lo quita -> 09 = 9 o 009=9
        String num = numero.substring(numero.length() - 1);
        return UNIDADES[Integer.parseInt(num)];
    }

    private String getDecenas(String num) {// 99                        
        int n = Integer.parseInt(num);
        if (n < 10) {//para casos como -> 01 - 09
            return getUnidades(num);
        } else if (n > 19) {//para 20...99
            String u = getUnidades(num);
            if (u.equals("")) { //para 20,30,40,50,60,70,80,90
                return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8];
            } else {
                return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8] + "y " + u;
            }
        } else {//numeros entre 11 y 19
            return DECENAS[n - 10];
        }
    }

    private String getCentenas(String num) {// 999 o 099
        if (Integer.parseInt(num) > 99) {//es centena
            if (Integer.parseInt(num) == 100) {//caso especial
                return " cien ";
            } else {
                return CENTENAS[Integer.parseInt(num.substring(0, 1))] + getDecenas(num.substring(1));
            }
        } else {//por Ej. 099 
            //se quita el 0 antes de convertir a decenas
            return getDecenas(Integer.parseInt(num) + "");
        }
    }

    private String getMiles(String numero) {// 999 999
        //obtiene las centenas
        String c = numero.substring(numero.length() - 3);
        //obtiene los miles
        String m = numero.substring(0, numero.length() - 3);
        String n = "";
        //se comprueba que miles tenga valor entero
        if (Integer.parseInt(m) > 0) {
            n = getCentenas(m);
            return n + "mil " + getCentenas(c);
        } else {
            return "" + getCentenas(c);
        }

    }

    private String getMillones(String numero) { //000 000 000        
        //se obtiene los miles
        String miles = numero.substring(numero.length() - 6);
        //se obtiene los millones
        String millon = numero.substring(0, numero.length() - 6);
        String n = "";
        if (millon.length() > 1) {
            n = getCentenas(millon) + "millones ";
        } else {
            n = getUnidades(millon) + "millon ";
        }
        return n + getMiles(miles);
    }
    
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
    
    public void cboCategoria(JComboBox cbo, List<Categoria> pLista){
        pLista.stream().forEach(datos -> {
            cbo.addItem(datos);
        });
    }
    
    public void cboBodega(JComboBox cbo, List<Bodega> pLista){
        pLista.stream().forEach(datos -> {
            cbo.addItem(datos);
        });
    }
     
     public void cboUnidades(JComboBox cbo, List<Unidad> pLista){
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
