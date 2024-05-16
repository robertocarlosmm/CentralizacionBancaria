/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.pucp.centralizacionbancariasoft.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Roberto
 */
//PRUEBA
public class LectorSicop {
    //private List<DataSicop> datos;
    private List<DataSicop> soles;
    private List<DataSicop> dolares;
    private List<ErrorSicop> errores;

    public LectorSicop() {
        //datos = new ArrayList<>();
        soles = new ArrayList<>();
        dolares = new ArrayList<>();
        errores = new ArrayList<>();
    }

    public List<DataSicop> getSoles() {
        return soles;
    }

    public List<DataSicop> getDolares() {
        return dolares;
    }
    
    /*public List<DataSicop> getDatos() {
        return datos;
    }*/

    public List<ErrorSicop> getErrores() {
        return errores;
    }
    
    private Date convertirAFecha(String fechaString) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        // Convertir el String formateado de vuelta a un objeto Date
        // Verificar si el año es menor a 2000
        return formatter.parse(fechaString);
    }
    
    public void resumenArchivo(){
        /*System.out.println(datos.size() + " Datos leídos:");
        for (DataSicop dato : datos) {
            System.out.println(dato);
        }*/
        System.out.println(soles.size() + " Datos soles leídos:");
        for (DataSicop dato : soles) {
            System.out.println(dato);
        }
        System.out.println(dolares.size() + " Datos dolares leídos:");
        for (DataSicop dato : dolares) {
            System.out.println(dato);
        }
        System.out.println(errores.size() + " Errores:");
        for(ErrorSicop error : errores){
            System.out.println(error);
        }
    }
    
    public String resumenArchivoTexto(){
        StringBuilder resumen = new StringBuilder();
        resumen.append("Datos soles leídos:\n");
        for (DataSicop s : soles) {
            resumen.append(s.toString()).append("\n");
        }
        resumen.append("Datos dolares leídos:\n");
        for (DataSicop d : dolares) {
            resumen.append(d.toString()).append("\n");
        }
        resumen.append("Errores:\n");
        for (ErrorSicop error : errores) {
            resumen.append(error.toString()).append("\n");
        }
        return resumen.toString();
    }
    
    public void resumenTxt(){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
            // Escribir la cadena en el archivo
            writer.write(resumenArchivoTexto());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void resumenErroresTxt() {
        StringBuilder resumen = new StringBuilder();
        resumen.append("Errores:\n");
        if(errores.size()==0){
            resumen.append("No hay errores.\n");
        }
        for (ErrorSicop error : errores) {
            resumen.append(error.toString()).append("\n");
        }

        // Obtener la fecha y hora actual para el nombre del archivo
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fechaHora = dateFormat.format(new Date());

        // Construir el nombre del archivo
        String nombreArchivo = fechaHora + "_ERRORES_SICOP.txt";

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
    
    private String[] separarCampos(String linea) {
        List<String> camposList = new ArrayList<>();
        boolean dentroDeComillas = false;
        StringBuilder campoActual = new StringBuilder();

         // Verificar y quitar comillas al inicio y al final de la línea si están presentes
        if (linea.startsWith("\"") && linea.endsWith("\"")) {
            //System.out.println("CASO DOBLE COMILLA");
            //System.out.println(linea);
            linea = linea.substring(1, linea.length() - 1);
            for (int i = 0; i < linea.length(); i++) {
                char c = linea.charAt(i);
                if (i + 1 < linea.length() && c == '"' && linea.charAt(i+1) == '"') {
                    dentroDeComillas = !dentroDeComillas;
                } else if (c == ',' && !dentroDeComillas) {
                    camposList.add(campoActual.toString().trim());
                    campoActual.setLength(0); // Limpiar el StringBuilder
                } else {
                    campoActual.append(c);
                }
            }
        }else{
            for (int i = 0; i < linea.length(); i++) {
                char c = linea.charAt(i);
                if (c == '"') {
                    //System.out.println(linea);
                    dentroDeComillas = !dentroDeComillas;
                } else if (c == ',' && !dentroDeComillas) {
                    camposList.add(campoActual.toString());
                    campoActual.setLength(0); // Limpiar el StringBuilder
                } else {
                    campoActual.append(c);
                }
            }
        }
        

        camposList.add(campoActual.toString());

        return camposList.toArray(new String[0]);
    }
    
    public String formatoNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            return ""; // Si el nombre es nulo o vacío, retorna una cadena vacía
        }
        // Reemplazar caracteres especiales por sus equivalentes en el abecedario
        //String cadena = nombre;
        nombre = nombre.replaceAll("[áäâà]", "a");
        nombre = nombre.replaceAll("[éëêè]", "e");
        nombre = nombre.replaceAll("[íïîì]", "i");
        nombre = nombre.replaceAll("[óöôò]", "o");
        nombre = nombre.replaceAll("[úüûù]", "u");
        nombre = nombre.replaceAll("[ÁÄÂÀ]", "A");
        nombre = nombre.replaceAll("[ÉËÊÈ]", "E");
        nombre = nombre.replaceAll("[ÍÏÎÌ]", "I");
        nombre = nombre.replaceAll("[ÓÖÔÒ]", "O");
        nombre = nombre.replaceAll("[ÚÜÛÙ]", "U");
        nombre = nombre.replaceAll("ñ", "n");
        nombre = nombre.replaceAll("Ñ", "N");
        nombre = nombre.replaceAll("\t", ""); //ELIMINAR LAS TABULACIONES
        nombre = nombre.replaceAll("\n", "");  // Eliminar saltos de línea
        nombre = nombre.replaceAll("\r", "");  // Eliminar retornos de carro
        nombre = nombre.replaceAll("\\\\", "");  // Eliminar caracteres de escape literales
        nombre = nombre.replaceAll("\'", "");  // Eliminar comillas simples
        nombre = nombre.replaceAll("\"", "");  // Eliminar comillas dobles
        nombre = nombre.replaceAll("[^a-zA-Z0-9\\s]", " "); // Reemplazar otros caracteres especiales por espacio en blanco
        // Imprimir si el nombre original fue modificado
        /*if(!cadena.equals(nombre)){
            System.out.println(cadena + " -> " + nombre);
        }*/
        // Eliminar el último espacio si el último carácter es un espacio
        while(nombre.endsWith(" ")){
            nombre = nombre.substring(0, nombre.length() - 1);
        }
        
        
        /*boolean esUtf8;
        try {
            byte[] bytes = nombre.getBytes(StandardCharsets.UTF_8);
            String textoDecodificado = new String(bytes, StandardCharsets.UTF_8);
            esUtf8 = nombre.equals(textoDecodificado);
        } catch (Exception e) {
            esUtf8 = false;
        }

        if (esUtf8) {
            System.out.println("El texto está codificado en UTF-8");
        } else {
            System.out.println("El texto no está codificado en UTF-8");
        }*/
        


        /*for (int i = 0; i < nombre.length(); i++) {
            System.out.println("'"+nombre.charAt(i)+"'");
        }*/

        
        return nombre;
    }
    
    public String obtenerRutaArchivo(){
        String rutaArchivo = "";
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos CSV", "csv");
        fileChooser.setFileFilter(filter);
        fileChooser.setDialogTitle("Seleccione un archivo SICOP - CSV");
        int resultado = fileChooser.showOpenDialog(null);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            rutaArchivo = fileChooser.getSelectedFile().getAbsolutePath();
            System.out.println("Archivo seleccionado: " + rutaArchivo);
        }
        return rutaArchivo;
    }
    
    public static void mostrarAdvertenciaERROR(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }
    
    public int leerArchivo(String rutaArchivo) {
        //String rutaArchivo = obtenerRutaArchivo();
        if (rutaArchivo != "") {
            
            // Aquí puedes llamar al método para leer el archivo utilizando la ruta seleccionada
        
            int numLinea = 1;
            //try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            //new InputStreamReader(new FileInputStream("archivo.txt"), "UTF-8")
            try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rutaArchivo), "ISO-8859-1"))){
                String encabezado = br.readLine();
                if (encabezado == null) {
                    mostrarAdvertenciaERROR("Error SICOP: El archivo está vacío");
                    System.out.println("Error: El archivo está vacío");
                    return -1;
                }

                // Validar que el encabezado tenga el formato correcto
                String[] camposEncabezado = separarCampos(encabezado);
                if (camposEncabezado.length != 8) {
                    mostrarAdvertenciaERROR("Error SICOP: El encabezado no tiene el formato correcto");
                    System.out.println("Error: El encabezado no tiene el formato correcto");
                    return -1;
                }
                // Validar que el encabezado sea correcto
                if (!camposEncabezado[0].equals("serie") || !camposEncabezado[1].equals("numero") || !camposEncabezado[2].equals("ruc")
                        || !camposEncabezado[3].equals("razon_social") || !camposEncabezado[4].equals("fec_vencimiento") 
                        || !camposEncabezado[5].equals("num_documento_id") || !camposEncabezado[6].equals("importe") 
                        || !camposEncabezado[7].equals("moneda")) {
                    
                    // Imprimir los campos del encabezado que no coinciden con los valores esperados
                        for (int i = 0; i < camposEncabezado.length; i++) {
                            if (!camposEncabezado[i].equals("serie") && !camposEncabezado[i].equals("numero")
                                    && !camposEncabezado[i].equals("ruc") && !camposEncabezado[i].equals("razon_social")
                                    && !camposEncabezado[i].equals("fec_vencimiento") && !camposEncabezado[i].equals("num_documento_id")
                                    && !camposEncabezado[i].equals("importe") && !camposEncabezado[i].equals("moneda")) {
                                String errorEncabezado = "Campo inesperado en la posición " + i + ": " + camposEncabezado[i];
                                errores.add(new ErrorSicop(numLinea, errorEncabezado));
                                //System.out.println("Campo inesperado en la posición " + i + ": " + camposEncabezado[i]);
                            }
                        }
                    mostrarAdvertenciaERROR("Error SICOP: El encabezado no tiene el formato esperado");
                    System.out.println("Error: El encabezado no tiene el formato esperado");
                    return -1;
                }

                String linea;
                while ((linea = br.readLine()) != null) {
                    numLinea ++;
                    try {
                        // Separar los campos de la línea por comas
                        String[] campos = separarCampos(linea);

                        // Validar que hay suficientes campos en la línea
                        if (campos.length != 8) {
                            errores.add(new ErrorSicop(numLinea, "Cantidad incorrecta de campos"));
                            /*System.out.println(numLinea + "Cantidad incorrecta de campos");
                            for(int i=0;i<campos.length;i++){
                                System.out.println(campos[i]+" | ");
                            }*/
                            
                            continue; // Saltar a la siguiente línea
                        }

                        // Parsear los campos y crear un objeto DataSicop
                        String serie = campos[0];
                        int numero = Integer.parseInt(campos[1]);
                        String ruc = campos[2];
                        if (!ruc.matches("[0-9]+")) {
                            errores.add(new ErrorSicop(numLinea, "RUC Incorrecto"));
                            continue; // Saltar a la siguiente línea
                        }
                        String razonSocial = formatoNombre(campos[3]);
                        Date fechaVencimiento = convertirAFecha(campos[4]);
                        String numDocumentoId = campos[5];
                        /*Aparentemente el número de documento id si tiene letras
                        if (!numDocumentoId.matches("[0-9]+")) {
                            errores.add(new ErrorSicop(numLinea, "numDocumentoId Incorrecto"));
                            continue; // Saltar a la siguiente línea
                        }*/
                        double importe = Double.parseDouble(campos[6]);
                        
                        int moneda = Integer.parseInt(campos[7]);

                        // Agregar el objeto DataSicop a la lista de datos
                        if(moneda == 1){
                            soles.add(new DataSicop(numLinea, serie, numero, ruc, razonSocial, 
                                fechaVencimiento, numDocumentoId, importe, moneda));
                        }else{
                            if(moneda == 2){
                                dolares.add(new DataSicop(numLinea, serie, numero, ruc, razonSocial, 
                                fechaVencimiento, numDocumentoId, importe, moneda));
                            }else{
                                errores.add(new ErrorSicop(numLinea, "Moneda con valor incorrecto"));
                                continue;
                            }
                        }
                        /*datos.add(new DataSicop(numLinea, serie, numero, ruc, razonSocial, 
                                fechaVencimiento, numDocumentoId, importe, moneda));*/
                    } catch (Exception e) {
                        errores.add(new ErrorSicop(numLinea, e.getMessage()));
                    }
                }
            } catch (IOException e) {
                mostrarAdvertenciaERROR("Error SICOP: al leer el archivo: " + e.getMessage());
                System.out.println("Error al leer el archivo: " + e.getMessage());
                return -1;
            }
            
        } else{
            mostrarAdvertenciaERROR("ERROR SICOP: Debe seleccionar un archivo SICOP csv");
            System.out.println("ERROR SICOP: Debe seleccionar un archivo SICOP csv");
            return -1;
        }
        return 1;
    }
            
}
