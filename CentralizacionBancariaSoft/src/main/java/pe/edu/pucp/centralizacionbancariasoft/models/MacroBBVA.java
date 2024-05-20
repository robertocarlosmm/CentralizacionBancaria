/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.pucp.centralizacionbancariasoft.models;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Roberto
 */
public class MacroBBVA {
    private List<FormatoBBVA> datos;
    private List<ErrorMacro> errores;
    private int tipoMoneda;//1: soles | 2:dolares
    private double montoMinimo;
    
    public MacroBBVA() {
        datos = new ArrayList<>();
        errores = new ArrayList<>();
    }
    
    public MacroBBVA(int moneda, double montoMinimo) {
        datos = new ArrayList<>();
        errores = new ArrayList<>();
        this.tipoMoneda = moneda;
        this.montoMinimo = montoMinimo;
    }

    public List<ErrorMacro> getErrores() {
        return errores;
    }

    public List<FormatoBBVA> getDatos() {
        return datos;
    }

    public int getTipoMoneda() {
        return tipoMoneda;
    }

    public double getMontoMinimo() {
        return montoMinimo;
    }

    public void asignarSicop(List<DataSicop> dataSicop, String fb){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaBloqueo = null;
        try {
            fechaBloqueo = sdf.parse(fb);
        } catch (ParseException e) {
            // Manejar la excepción si la fecha no se puede parsear correctamente
            e.printStackTrace();
        }
        
        for(DataSicop ds : dataSicop){
            
            try{
                if(ds.getImporte() >= getMontoMinimo()){
                    datos.add(new FormatoBBVA(ds.getRazonSocial(), ds.getRuc(), ds.getSerie(), 
                    ds.getNumero(), ds.getFechaVencimiento(),fechaBloqueo,ds.getImporte()));
                }else{
                    //System.out.println("OJO");
                    errores.add(new ErrorMacro("SICOP", String.valueOf(ds.getNumLinea()), 
                        "ADVERTENCIA: No se copió porque el importe es menor al monto mínimo de "
                        +getMontoMinimo()));
                }
            }catch(Exception e){
                errores.add(new ErrorMacro("SICOP", String.valueOf(ds.getNumLinea()), 
                        "ERROR: NO SE PUDO ASIGNAR ESTE REGISTRO"));
            }
            
        }
    }
    
    public void asignarCenturia(List<DataCenturia> dataCenturia, String fb){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaBloqueo = null;
        try {
            fechaBloqueo = sdf.parse(fb);
        } catch (ParseException e) {
            // Manejar la excepción si la fecha no se puede parsear correctamente
            e.printStackTrace();
        }
        
        for(DataCenturia dc : dataCenturia){
            try{
                if(dc.getMonto() >= getMontoMinimo()){
                    datos.add(new FormatoBBVA(dc.getRazonSocial(), dc.getNumDoi(), dc.getSerie(), 
                    dc.getNumero(), dc.getFechaVencimiento(), fechaBloqueo, dc.getMonto()));
                }else{
                    errores.add(new ErrorMacro("CENTURIA", String.valueOf(dc.getNumLinea()), 
                        "ADVERTENCIA: No se copió porque el importe es menor al monto mínimo de "
                        +getMontoMinimo()));
                }
            }catch(Exception e){
                errores.add(new ErrorMacro("CENTURIA", String.valueOf(dc.getNumLinea()), 
                        "ERROR: NO SE PUDO ASIGNAR ESTE REGISTRO"));
            }
        }
    }
    
    public void reporteDatos(){
        if(this.tipoMoneda == 1){
            System.out.println("Soles:");
        }else{
            if(this.tipoMoneda == 2){
                System.out.println("Dólares:");
            }
        }
        
        for(FormatoBBVA fbbva: datos){
            System.out.println(fbbva);
        }
    }
    public int generarExcel(){
        int result = 0;
        ordenarDatos();
        // Crear un nuevo libro de trabajo de Excel
        Workbook workbook = new XSSFWorkbook();

        // Crear una hoja en el libro de trabajo
        Sheet hoja = workbook.createSheet("Datos_BBVA");
        Sheet hoja2 = workbook.createSheet("ADVERTENCIAS_ERRORES_BBVA");
        //primero asignamos los datos de la hoja 2
        Row encabezados2 = hoja2.createRow(0);
        String[] nombresEncabezados2 = {"ARCHIVO", "NUMERO DE LINEA", "DETALLES"};
        for (int i = 0; i < nombresEncabezados2.length; i++) {
            encabezados2.createCell(i).setCellValue(nombresEncabezados2[i]);
        }
        for(int i = 0; i < errores.size(); i++){
            Row fila2 = hoja2.createRow(i + 1); // Comenzar desde la segunda fila (índice 1)
            ErrorMacro eBcp = errores.get(i);
            fila2.createCell(0).setCellValue(eBcp.getArchivo());
            fila2.createCell(1).setCellValue(eBcp.getNumLinea());
            fila2.createCell(2).setCellValue(eBcp.getDetalle());
        }

        // Crear los encabezados
        Row encabezados = hoja.createRow(0);
        String[] nombresEncabezados = {"NOMBRES Y APELLIDOS", "DNI POSTULANTE", "CONCEPTO DE PAGO", "FECHA VENCIMIENTO", "FECHA BLOQUEO", " - ", "IMPORTE MAX A COBRAR DEUDA", "IMPORTE MIN A COBRAR DEUDA"};
        for (int i = 0; i < nombresEncabezados.length; i++) {
            encabezados.createCell(i).setCellValue(nombresEncabezados[i]);
        }

        // Llenar la hoja con los datos
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String textoTruncado;
        for (int i = 0; i < datos.size(); i++) {
            FormatoBBVA dato = datos.get(i);
            Row fila = hoja.createRow(i + 1); // Comenzar desde la segunda fila (índice 1)
            
            textoTruncado = dato.getNombres().length() > 29 
                    ? dato.getNombres().substring(0, 29) : dato.getNombres();
            
            fila.createCell(0).setCellValue(textoTruncado);
            fila.createCell(1).setCellValue(dato.getDniPostulante());
            fila.createCell(2).setCellValue(dato.getConceptoPago());
            fila.createCell(3).setCellValue(sdf.format(dato.getFechaVencimiento()));
            fila.createCell(4).setCellValue(sdf.format(dato.getFechaBloqueo()));
            fila.createCell(6).setCellValue(dato.getImporteMaxCobrarDeuda());
            fila.createCell(7).setCellValue(getMontoMinimo()); // Valor fijo para "IMPORTE MIN A COBRAR DEUDA"
        }

        // Obtener la carpeta de descargas del usuario
        String carpetaDescargas = System.getProperty("user.home") + "/Downloads/";
        // Crear el archivo en la carpeta de descargas
        // Crear el archivo en la carpeta de descargas
        String moneda = (this.tipoMoneda == 1) ? "soles" : "dolares";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fechaHora = formatter.format(new Date());
        String rutaArchivo = carpetaDescargas + fechaHora + "_BBVA_" + moneda + ".xlsx";

        // Escribir el libro de trabajo en el archivo
        try (FileOutputStream fileOut = new FileOutputStream(rutaArchivo)) {
            workbook.write(fileOut);
            result = 1;
            //JOptionPane.showMessageDialog(null, "Archivo Excel generado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al escribir el archivo Excel BBVA", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        // Cerrar el libro de trabajo
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public void ordenarDatos() {
        Collections.sort(datos, new Comparator<FormatoBBVA>() {
            @Override
            public int compare(FormatoBBVA o1, FormatoBBVA o2) {
                int comparacionDni = o1.getDniPostulante().compareTo(o2.getDniPostulante());
                if (comparacionDni != 0) {
                    return comparacionDni;
                } else {
                    return o2.getFechaVencimiento().compareTo(o1.getFechaVencimiento());
                }
            }
        });
    }
    
}
