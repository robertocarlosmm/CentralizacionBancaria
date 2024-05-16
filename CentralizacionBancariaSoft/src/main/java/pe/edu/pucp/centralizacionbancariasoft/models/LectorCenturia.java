/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.pucp.centralizacionbancariasoft.models;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Roberto
 */
public class LectorCenturia {
    //private List<DataCenturia> datos;
    private List<DataCenturia> soles;
    private List<DataCenturia> dolares;
    private List<ErrorCenturia> errores;
    
    public LectorCenturia() {
        //datos = new ArrayList<>();
        soles = new ArrayList<>();
        dolares = new ArrayList<>();
        errores = new ArrayList<>();
    }

    public List<DataCenturia> getSoles() {
        return soles;
    }

    public List<DataCenturia> getDolares() {
        return dolares;
    }
    
    public List<ErrorCenturia> getErrores() {
        return errores;
    }
    
    public Date convertirAFecha(String fechaString) throws ParseException {
        System.out.println("leee "+fechaString);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        //System.out.println()
        return formatter.parse(fechaString);
    }
    
    public void resumenArchivo(){
        /*System.out.println(datos.size() + " Datos leídos:");
        for (DataSicop dato : datos) {
            System.out.println(dato);
        }*/
        System.out.println(soles.size() + " Datos soles leídos:");
        for (DataCenturia dato : soles) {
            System.out.println(dato);
        }
        System.out.println(dolares.size() + " Datos dolares leídos:");
        for (DataCenturia dato : dolares) {
            System.out.println(dato);
        }
        System.out.println(errores.size() + " Errores:");
        for(ErrorCenturia error : errores){
            System.out.println(error);
        }
    }
    
    public String resumenArchivoTexto(){
        StringBuilder resumen = new StringBuilder();
        resumen.append("Datos leídos:\n");
        for (DataCenturia dato : soles) {
            resumen.append(dato.toString()).append("\n");
        }
        for (DataCenturia dato : dolares) {
            resumen.append(dato.toString()).append("\n");
        }
        resumen.append("Errores:\n");
        for (ErrorCenturia error : errores) {
            resumen.append(error.toString()).append("\n");
        }
        return resumen.toString();
    }
    
    public void resumenTxt(String resumen){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
            // Escribir la cadena en el archivo
            writer.write(resumen);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        return nombre;
    }
    
    private File getFileFromChooser() {
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
    
    public static void mostrarAdvertenciaERROR(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }
    
    public int readExcelFile(String filePath) {
        //List<String> dataList = new ArrayList<>();
        List<String[]> rowDataList = new ArrayList<>();
        try {
            InputStream myFile = new FileInputStream(new File(filePath));
            XSSFWorkbook wb = new XSSFWorkbook(myFile);
            XSSFSheet sheet = wb.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            int rowNum = 1;

            // Verificar los encabezados
            Row headerRow = rowIterator.next();
            headerRow = rowIterator.next();
            rowNum++;
            if (!verifyHeaders(headerRow)) {
                errores.add(new ErrorCenturia(2,"Los encabezados del archivo Excel no coinciden con los encabezados esperados."));
                System.err.println("Los encabezados del archivo Excel no coinciden con los encabezados esperados.");
                mostrarAdvertenciaERROR("Error CENTURIA: Los encabezados del archivo Excel no coinciden con los encabezados esperados.");
                return -1;
            }
            int columnIndex;
            String cellValue;
            Cell cell;
            int numLinea;
            Date fechaVencimiento;
            String serie;
            int numero;
            String numDoi;
            String razonSocial;
            int moneda;
            double monto;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                rowNum++;

                // Ignorar la primera y segunda fila (encabezados y línea en blanco)
                if (rowNum <= 2) {
                    continue;
                }

                try {
                    // Crear una instancia de DataCenturia para almacenar los datos de esta fila

                    // Asignar los valores de las columnas a los campos correspondientes de DataCenturia
                    numLinea = rowNum; // Número de línea
                    //fechaVencimiento = convertirAFecha(getCellValueAsString(row.getCell(6))); // Columna 7 (Fecha de vencimiento)
                    fechaVencimiento = (row.getCell(6)).getDateCellValue();
                    //System.out.println("fefffff "+fechaVencimiento);
                    serie = getCellValueAsString(row.getCell(9)); // Columna 10 (Serie)
                    
                    // Columna 11 (Número)
                    try {
                        numero = Integer.parseInt(row.getCell(10).getStringCellValue());
                    } catch (NumberFormatException e) {
                        // Manejar la excepción si el valor no se puede convertir a número
                        errores.add(new ErrorCenturia(rowNum,"Numero con formato inválido"));
                        continue;
                    }
                    
                    numDoi = getCellValueAsString(row.getCell(12)); // Columna 13 (Número de DOI)
                    if (!numDoi.matches("[0-9]+")) {
                        errores.add(new ErrorCenturia(numLinea, "NUM DOI Incorrecto"));
                        continue; // Saltar a la siguiente línea
                    }
                    razonSocial = formatoNombre(getCellValueAsString(row.getCell(13))); // Columna 14 (Razón Social)
                    
                    // Columna 16 (Moneda)
                    try {
                        moneda = Integer.parseInt(row.getCell(15).getStringCellValue());
                    } catch (Exception e) {
                        // Manejar la excepción si el valor no se puede convertir a número
                        errores.add(new ErrorCenturia(rowNum,"Moneda con formato inválido"));
                        continue;
                    }

                    // Agregar la instancia de DataCenturia a la lista de datos
                    if(moneda == 1){
                        //leer historico soles:
                        try {
                        // Obtener el valor de la celda como un número de punto flotante (double)
                        double montoRaw = row.getCell(16).getNumericCellValue(); // Columna 17 (Monto soles)

                        // Redondear el valor a dos decimales
                        DecimalFormat df = new DecimalFormat("#.##");
                        df.setRoundingMode(RoundingMode.DOWN); // Truncar el valor, no redondear
                        monto = Double.parseDouble(df.format(montoRaw));
                        soles.add(new DataCenturia(numLinea, fechaVencimiento, serie, 
                            numero, numDoi, razonSocial, moneda, monto));
                        } catch (Exception e) {
                            // Manejar la excepción si el valor no se puede obtener como número
                            errores.add(new ErrorCenturia(rowNum, "Monto con formato inválido"));
                            continue;
                        }
                    }else{
                        if(moneda ==  2){
                            //leer historico dolares:
                            try {
                                // Obtener el valor de la celda como un número de punto flotante (double)
                                double montoRaw = row.getCell(17).getNumericCellValue(); // Columna 18 (Monto dolares)
                                //System.out.println("leendo..");
                                // Redondear el valor a dos decimales
                                DecimalFormat df = new DecimalFormat("#.##");
                                df.setRoundingMode(RoundingMode.DOWN); // Truncar el valor, no redondear
                                monto = Double.parseDouble(df.format(montoRaw));
                                dolares.add(new DataCenturia(numLinea, fechaVencimiento, serie, 
                                    numero, numDoi, razonSocial, moneda, monto));
                            } catch (Exception e) {
                                // Manejar la excepción si el valor no se puede obtener como número
                                errores.add(new ErrorCenturia(rowNum, "Monto con formato inválido"));
                                continue;
                            }
                        }else{
                            errores.add(new ErrorCenturia(numLinea, "Moneda con valor incorrecto"));
                        }
                    }
                    
                    try {
                    // Obtener el valor de la celda como un número de punto flotante (double)
                    double montoRaw = row.getCell(19).getNumericCellValue(); // Columna 20 (Monto)

                    // Redondear el valor a dos decimales
                    DecimalFormat df = new DecimalFormat("#.##");
                    df.setRoundingMode(RoundingMode.DOWN); // Truncar el valor, no redondear
                    monto = Double.parseDouble(df.format(montoRaw));
                    } catch (Exception e) {
                        // Manejar la excepción si el valor no se puede obtener como número
                        errores.add(new ErrorCenturia(rowNum, "Monto con formato inválido"));
                        continue;
                    }
                    /*datos.add(new DataCenturia(numLinea, fechaVencimiento, serie, 
                            numero, numDoi, razonSocial, moneda, monto));*/
                } catch (Exception e) {
                    // Manejar la excepción, por ejemplo, imprimir un mensaje de error
                    System.err.println("Error al procesar la línea " + rowNum + ": " + e.getMessage());
                    // También puedes agregar la línea a una lista de errores si lo deseas
                    errores.add(new ErrorCenturia(rowNum, e.getMessage()));
                }
                
            }
        } catch (Exception e) {
            mostrarAdvertenciaERROR("Error CENTURIA: Error al leer el archivo");
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    private String getCellValueAsString(Cell cell) {
        DataFormatter formatter = new DataFormatter(); // Formateador para manejar todos los formatos de celda
        return formatter.formatCellValue(cell);
    }
    
    private boolean verifyHeaders(Row headerRow) {
        String[] expectedHeaders = {
            "Fecha De Analisi Al",
            "Cuenta contable",
            "Descripcion cuenta contable",
            "Estado",
            "Voucher",
            "Fecha de emsion",
            "Fecha de vencimiento",
            "Año",
            "Tipo",
            "Serie",
            "Numero",
            "Tipo DOI",
            "N° DOI",
            "Nombre O Razon Social",
            "Concatenado",
            "Moneda",
            "Importe . Históricos S/",
            "Importe\n Históricos $",
            "T.C.",
            "Importe S/. Ajustado",
            "Codigo Unidad",
            "Nombre De Unidad",
            "Nombre Subunidad"
        };
        Iterator<Cell> cellIterator = headerRow.cellIterator();
        int cellNum = 0;

        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            if (cellNum >= expectedHeaders.length) {
                System.out.println("Hay más celdas de las esperadas");
                return false;
            }
            if (!cell.getStringCellValue().trim().equals(expectedHeaders[cellNum])) {
                System.out.println("El encabezado '" + cell.getStringCellValue().trim() + "' no coincide");
                return false;
            }
            cellNum++;
        }

        if (cellNum != expectedHeaders.length) {
            System.out.println("Faltan encabezados en la fila");
            return false;
        }


        return cellNum == expectedHeaders.length; // Verificar si hay suficientes celdas
    }
    
    public void leerArchivo() {
        File selectedFile = getFileFromChooser();
        if (selectedFile != null) {
            readExcelFile(selectedFile.getAbsolutePath());
            
            //resumenTxt(resumenArchivoTexto());
        } else {
            System.out.println("No se seleccionó ningún archivo.");
        }
    }
    
    public void resumenErroresTxt() {
        StringBuilder resumen = new StringBuilder();
        resumen.append("Errores:\n");
        if(errores.size()==0){
            resumen.append("No hay errores.\n");
        }
        for (ErrorCenturia error : errores) {
            resumen.append(error.toString()).append("\n");
        }

        // Obtener la fecha y hora actual para el nombre del archivo
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fechaHora = dateFormat.format(new Date());

        // Construir el nombre del archivo
        String nombreArchivo = fechaHora + "_ERRORES_CENTURIA.txt";

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
    
}
