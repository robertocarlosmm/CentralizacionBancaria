/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.pucp.centralizacionbancariasoft;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import pe.edu.pucp.centralizacionbancariasoft.models.ErrorCenturia;
import pe.edu.pucp.centralizacionbancariasoft.models.ErrorSicop;
import pe.edu.pucp.centralizacionbancariasoft.models.LectorCenturia;
import pe.edu.pucp.centralizacionbancariasoft.models.LectorSicop;
import pe.edu.pucp.centralizacionbancariasoft.models.MacroBBVA;
import pe.edu.pucp.centralizacionbancariasoft.models.MacroBCP;

/**
 *
 * @author jyalico
 */
public class ManagerFormatoMacros {
    private LectorSicop lectorSicop;
    private LectorCenturia lectorCenturia;
    private double montoMinimo;
    private String fechaBloqueo;
    private MacroBCP macroBCPsoles;
    private MacroBBVA macroBBVAsoles;
    private MacroBCP macroBCPdolares;
    private MacroBBVA macroBBVAdolares;
    private String rutaSicop;
    private String rutaCenturia;
    //PantallaDeCarga pantallaDeCarga = new PantallaDeCarga();

    public void setRutaSicop(String rutaSicop) {
        this.rutaSicop = rutaSicop;
    }

    public void setRutaCenturia(String rutaCenturia) {
        this.rutaCenturia = rutaCenturia;
    }

    public String getRutaSicop() {
        return rutaSicop;
    }

    public String getRutaCenturia() {
        return rutaCenturia;
    }
    
    public LectorSicop getLectorSicop() {
        return lectorSicop;
    }

    public void setLectorSicop(LectorSicop lectorSicop) {
        this.lectorSicop = lectorSicop;
    }

    public LectorCenturia getLectorCenturia() {
        return lectorCenturia;
    }

    public void setLectorCenturia(LectorCenturia lectorCenturia) {
        this.lectorCenturia = lectorCenturia;
    }

    public double getMontoMinimo() {
        return montoMinimo;
    }

    public void setMontoMinimo(double montoMinimo) {
        this.montoMinimo = montoMinimo;
    }

    public String getFechaBloqueo() {
        return fechaBloqueo;
    }

    public void setFechaBloqueo(String fechaBloqueo) {
        this.fechaBloqueo = fechaBloqueo;
    }
    
    public ManagerFormatoMacros(double montoMinimo, String fechaBloqueo) {
        this.montoMinimo = montoMinimo;
        this.fechaBloqueo = fechaBloqueo;
        this.rutaSicop = "";
        this.rutaCenturia = "";
        //this.pantallaDeCarga.setVisible(false);
    }
    public ManagerFormatoMacros() {
        this.rutaSicop = "";
        this.rutaCenturia = "";
        //this.pantallaDeCarga.setVisible(false);
    }
    
    public void configurarManager(double montoMinimo, String fechaBloqueo){
        this.montoMinimo = montoMinimo;
        this.fechaBloqueo = fechaBloqueo;
    }

    public void seleccionarSicop(){
        String rutaArchivo = "";
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos CSV", "csv");
        fileChooser.setFileFilter(filter);
        fileChooser.setDialogTitle("Seleccione un archivo SICOP - CSV");
        int resultado = fileChooser.showOpenDialog(null);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            rutaArchivo = fileChooser.getSelectedFile().getAbsolutePath();
            this.rutaSicop = rutaArchivo;
            System.out.println(rutaArchivo);
        }
    }
    
    public void seleccionarCenturia(){
        File selectedFile = getFileExcelFromChooser();
        if (selectedFile != null) {
            this.rutaCenturia = selectedFile.getAbsolutePath();
            System.out.println(selectedFile.getAbsolutePath());
        } else {
            System.out.println("No se seleccionó ningún archivo.");
        }
    }
    
    private File getFileExcelFromChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccione un archivo Centuria - Excel");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel files", "xls", "xlsx"));
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }
    
    public int leerSicop(){
        if(rutaSicop != ""){
            return lectorSicop.leerArchivo(rutaSicop);
            
        }
        return -1;
    }
    public int leerCenturia(){
        if(rutaCenturia != ""){
            return lectorCenturia.readExcelFile(rutaCenturia);
        }
        return -1;
    }
    
    public void leerArchivos(){
        //inicializar los lectores
        lectorSicop = new LectorSicop();
        lectorCenturia = new LectorCenturia();
        //Leer
        int lec1, lec2;
        lec1 = leerSicop();
        lec2 = leerCenturia();
        resumenErroresTxt();
    }
    
    public int leerArchivos(boolean sicopL, boolean centuriaL){
        //inicializar los lectores
        lectorSicop = new LectorSicop();
        lectorCenturia = new LectorCenturia();
        //Leer
        int lec1=1, lec2=1;
        if(sicopL){
            System.out.println("SICOP LEIDO");
            lec1 = leerSicop();
        }
        if(centuriaL){
            System.out.println("CENTURIA LEIDO");
            lec2 = leerCenturia();
        }
        resumenErroresTxt();
        return (lec1 * lec2);
    }
    
    public void resumenErroresTxt() {
        StringBuilder resumen = new StringBuilder();
        resumen.append("Errores SICOP:\n");
        if(lectorSicop.getErrores().size()==0){
            resumen.append("No hay errores.\n");
        }
        for (ErrorSicop error : lectorSicop.getErrores()) {
            resumen.append(error.toString()).append("\n");
        }
        resumen.append("\n=========================================================================================\n");
        resumen.append("\nErrores CENTURIA:\n");
        if(lectorCenturia.getErrores().size()==0){
            resumen.append("No hay errores.\n");
        }
        for (ErrorCenturia error : lectorCenturia.getErrores()) {
            resumen.append(error.toString()).append("\n");
        }
        // Obtener la fecha y hora actual para el nombre del archivo
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fechaHora = dateFormat.format(new Date());

        // Construir el nombre del archivo
        String nombreArchivo = fechaHora + "_ERRORES_SICOP_CENTURIA.txt";

        // Ruta del directorio de descargas
        String rutaDescargas = System.getProperty("user.home") + "/Downloads/";

        // Ruta completa del archivo
        String rutaArchivo = rutaDescargas + nombreArchivo;

        try {
            // Crear el archivo TXT
            Files.write(Paths.get(rutaArchivo), resumen.toString().getBytes());

            System.out.println("El archivo se ha creado exitosamente en: " + rutaArchivo);
        } catch (IOException e) {
            System.out.println("Error al crear el archivo: " + e.getMessage());
        }
    }
    
    public class PantallaDeCarga extends JDialog {
        public PantallaDeCarga() {
            // Configura la ventana emergente
            setTitle("Cargando...");
            setSize(300, 100);
            setLocationRelativeTo(null); // Centra la ventana en la pantalla
            setModal(true); // Hace que la ventana sea modal
            //setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

            // Crea un JLabel para mostrar el mensaje de carga
            JLabel label = new JLabel("Generando archivos Excel...", SwingConstants.CENTER);
            add(label);
        }
    }
    
    /*public void generarMacros() {
        // Muestra la pantalla de carga
        // Código para generar los archivos Excel
        // Solo tengo las rutas de los archivos, así que primero los leo y genero su reporte de errores
        // BCP
        macroBCPsoles = new MacroBCP(1, getMontoMinimo());
        macroBCPdolares = new MacroBCP(2, getMontoMinimo());
        // BBVA
        macroBBVAsoles = new MacroBBVA(1, getMontoMinimo());
        macroBBVAdolares = new MacroBBVA(2, getMontoMinimo());

        //AIGNACION
        macroBCPsoles.asignarSicop(lectorSicop.getSoles());
        macroBCPsoles.asignarCenturia(lectorCenturia.getSoles());

        macroBCPdolares.asignarSicop(lectorSicop.getDolares());
        macroBCPdolares.asignarCenturia(lectorCenturia.getDolares());

        macroBBVAsoles.asignarSicop(lectorSicop.getSoles(), getFechaBloqueo());
        macroBBVAsoles.asignarCenturia(lectorCenturia.getSoles(), getFechaBloqueo());

        macroBBVAdolares.asignarSicop(lectorSicop.getDolares(), getFechaBloqueo());
        macroBBVAdolares.asignarCenturia(lectorCenturia.getDolares(), getFechaBloqueo());
        PantallaDeCarga pantallaDeCarga = new PantallaDeCarga();
        pantallaDeCarga.setVisible(true);

        // Inicia un hilo para realizar la generación de macros
        Thread tarea = new Thread(() -> {
            

            int bcpSoles = macroBCPsoles.generarExcel();
            int bcpDolares = macroBCPdolares.generarExcel();
            int bbvaSoles = macroBBVAsoles.generarExcel();
            int bbvaDolares = macroBBVAdolares.generarExcel();
            System.out.println("se ocultara la pantalla de carga...");
            pantallaDeCarga.setVisible(false); // Oculta la pantalla de carga
            System.out.println("se ocultó la pantalla de carga...");
            SwingUtilities.invokeLater(() -> {
                
                StringBuilder texto = new StringBuilder("Se creó con éxito: ");
                if (bcpSoles == 1) {
                    texto.append("BCP Soles, ");
                }

                if (bcpDolares == 1) {
                    texto.append("BCP Dólares, ");
                }

                if (bbvaSoles == 1) {
                    texto.append("BBVA Soles, ");
                }

                if (bbvaDolares == 1) {
                    texto.append("BBVA Dólares, ");
                }

                // Eliminar la coma y el espacio extra al final
                if (texto.charAt(texto.length() - 2) == ',') {
                    texto.delete(texto.length() - 2, texto.length());
                }

                try {
                    JOptionPane.showMessageDialog(null, texto.toString(), "Información", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    // Manejar la excepción aquí
                    e.printStackTrace(); // O imprime el mensaje de la excepción en la consola
                }
            });
        });
        tarea.start(); // Inicia el hilo secundario
    }
    */
    public void generarMacros(){
        //solo tengo las rutas de los archivos, así que primero los leo y genero su reporte de errore:
        //leerArchivos();
        //BCP
        macroBCPsoles = new MacroBCP(1, getMontoMinimo());
        macroBCPdolares = new MacroBCP(2, getMontoMinimo());
        //BBVA
        macroBBVAsoles = new MacroBBVA(1, getMontoMinimo());
        macroBBVAdolares = new MacroBBVA(2, getMontoMinimo());
        
        //ASIGNACION DE DATA SICOP Y CENTURIA
        macroBCPsoles.asignarSicop(lectorSicop.getSoles());
        macroBCPsoles.asignarCenturia(lectorCenturia.getSoles());
        
        macroBCPdolares.asignarSicop(lectorSicop.getDolares());
        macroBCPdolares.asignarCenturia(lectorCenturia.getDolares());
        
        macroBBVAsoles.asignarSicop(lectorSicop.getSoles(), getFechaBloqueo());
        macroBBVAsoles.asignarCenturia(lectorCenturia.getSoles(), getFechaBloqueo());
        
        macroBBVAdolares.asignarSicop(lectorSicop.getDolares(), getFechaBloqueo());
        macroBBVAdolares.asignarCenturia(lectorCenturia.getDolares(), getFechaBloqueo());
        ///////////////////////////////////////////////////////////////////////
        
        int bcpSoles = macroBCPsoles.generarExcel();
        int bcpDolares = macroBCPdolares.generarExcel();
        int bbvaSoles = macroBBVAsoles.generarExcel();
        int bbvaDolares = macroBBVAdolares.generarExcel();
        
        StringBuilder texto = new StringBuilder("Se creó con éxito: ");
        if (bcpSoles == 1) {
            texto.append("BCP Soles, ");
        }

        if (bcpDolares == 1) {
            texto.append("BCP Dólares, ");
        }

        if (bbvaSoles == 1) {
            texto.append("BBVA Soles, ");
        }

        if (bbvaDolares == 1) {
            texto.append("BBVA Dólares, ");
        }
        
        // Eliminar la coma y el espacio extra al final
        if (texto.charAt(texto.length() - 2) == ',') {
            texto.delete(texto.length() - 2, texto.length());
        }
        
        try {
            JOptionPane.showMessageDialog(null, texto.toString(), "Información", JOptionPane.INFORMATION_MESSAGE);
        } catch(Exception e) {
            // Manejar la excepción aquí
            e.printStackTrace(); // O imprime el mensaje de la excepción en la consola
        }
        
    }
    
}
