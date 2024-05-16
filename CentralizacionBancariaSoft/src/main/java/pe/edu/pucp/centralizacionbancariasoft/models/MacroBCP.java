/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.pucp.centralizacionbancariasoft.models;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author xdd
 */
public class MacroBCP {
    
    private List<FormatoBCP> datos;
    private List<ErrorMacro> errores;
    private int tipoMoneda;//1: soles | 2:dolares
    private double montoMinimo;
    
    public MacroBCP(int moneda, double montoMinimo) {
        datos = new ArrayList<>();
        errores = new ArrayList<>();
        this.tipoMoneda = moneda;
        this.montoMinimo = montoMinimo;
    }

    public MacroBCP(List<DataSicop> dataSicop, List<DataCenturia> dataCenturia, int moneda, double montoMinimo) {
        datos = new ArrayList<>();
        errores = new ArrayList<>();
        this.tipoMoneda = moneda;
        this.montoMinimo = montoMinimo;
    }

    public List<FormatoBCP> getDatos() {
        return datos;
    }

    public List<ErrorMacro> getErrores() {
        return errores;
    }

    public int getTipoMoneda() {
        return tipoMoneda;
    }

    public double getMontoMinimo() {
        return montoMinimo;
    }
    
    public void asignarSicop(List<DataSicop> dataSicop){
        for(DataSicop ds : dataSicop){
            try{
                if(ds.getImporte() >= getMontoMinimo()){
                    datos.add(new FormatoBCP(ds.getRuc(), ds.getRazonSocial(), ds.getSerie(), 
                    ds.getNumero(), ds.getFechaVencimiento(), ds.getImporte()));
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
    
    public void asignarCenturia(List<DataCenturia> dataCenturia){
        for(DataCenturia dc : dataCenturia){
            try{
                if(dc.getMonto() >= getMontoMinimo()){
                    datos.add(new FormatoBCP(dc.getNumDoi(), dc.getRazonSocial(), dc.getSerie(), 
                    dc.getNumero(), dc.getFechaVencimiento(), dc.getMonto()));
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
        
        for(FormatoBCP fbcp: datos){
            System.out.println(fbcp);
        }
    }
    
    //generar excel
    public int generarExcel(){
        int result = 0;
        // Crear un nuevo libro de trabajo de Excel
        formatearFechasVencimiento();
        System.out.println("FECHAS CON FORMATO");
        Workbook workbook = new XSSFWorkbook();

        // Crear una hoja en el libro de trabajo
        Sheet hoja = workbook.createSheet("Datos_BCP");
        Sheet hoja2 = workbook.createSheet("ADVERTENCIAS_ERRORES_BCP");
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

        // Crear los encabezados de la HOJA 1
        Row encabezados = hoja.createRow(0);
        String[] nombresEncabezados = {"Código del depositante", "Nombre del depositante", 
            "Información de retorno", "Fecha Emisión", "Fecha Vencimiento", 
            "Monto a pagar", "Mora/Cargo fijo", "Monto minimo", "Tipo Registro", "Nro. documento Pago"};
        for (int i = 0; i < nombresEncabezados.length; i++) {
            encabezados.createCell(i).setCellValue(nombresEncabezados[i]);
        }

        // Llenar la hoja con los datos
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String textoTruncado;

        for (int i = 0; i < datos.size(); i++) {
            FormatoBCP dato = datos.get(i);
            Row fila = hoja.createRow(i + 1); // Comenzar desde la segunda fila (índice 1)
            
            textoTruncado = dato.getNombreDepositante().length() > 39 
                    ? dato.getNombreDepositante().substring(0, 39) : dato.getNombreDepositante();
            
           /* boolean esUtf8;
            try {
                byte[] bytes = textoTruncado.getBytes(StandardCharsets.UTF_8);
                String textoDecodificado = new String(bytes, StandardCharsets.UTF_8);
                esUtf8 = textoTruncado.equals(textoDecodificado);
            } catch (Exception e) {
                esUtf8 = false;
            }

            if (esUtf8) {
                System.out.println("El texto está codificado en UTF-8");
            } else {
                System.out.println("El texto no está codificado en UTF-8");
            }*/
            
            fila.createCell(0).setCellValue(dato.getCodigoDepositante());
            fila.createCell(1).setCellValue(textoTruncado);
            fila.createCell(2).setCellValue(dato.getInformacionRetorno());
            fila.createCell(4).setCellValue(sdf.format(dato.getFechaVencimiento()));
            fila.createCell(5).setCellValue(dato.getMontoAPagar());
            fila.createCell(7).setCellValue(getMontoMinimo());// Valor fijo para "IMPORTE MIN A COBRAR DEUDA"
            fila.createCell(8).setCellValue("No aplica");
            fila.createCell(9).setCellValue(dato.getNumeroDocumentoPago());
        }

        // Obtener la carpeta de descargas del usuario
        String carpetaDescargas = System.getProperty("user.home") + "/Downloads/";
        // Crear el archivo en la carpeta de descargas
        // Crear el archivo en la carpeta de descargas
        String moneda = (this.tipoMoneda == 1) ? "soles" : "dolares";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fechaHora = formatter.format(new Date());
        String rutaArchivo = carpetaDescargas + fechaHora + "_BCP_" + moneda + ".xlsx";

        // Escribir el libro de trabajo en el archivo
        try (FileOutputStream fileOut = new FileOutputStream(rutaArchivo)) {
            workbook.write(fileOut);
            result = 1;
            //JOptionPane.showMessageDialog(null, "Archivo Excel generado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al escribir el archivo Excel BCP", "Error", JOptionPane.ERROR_MESSAGE);
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
    
    // Método para ajustar una fecha de vencimiento si ya está en uso por el mismo código de depositante
    public Date ajustarFechaVencimiento(Date fechaVencimiento) {
        // se le agrega 1 día a la fecha de vencimiento
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaVencimiento);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }
    
    //Un hash map con los codigos y luego itero sobre los registros
    public void formatearFechasVencimiento(){
        Map<String, Set<Date>> depositanteFechasVencimiento = new HashMap<>();
        for (FormatoBCP registro : datos) {
            String codigoDepositante = registro.getCodigoDepositante();
            Date fechaVencimiento = registro.getFechaVencimiento();
            
            // Verificar si el código de depositante ya tiene una fecha de vencimiento igual
            if (depositanteFechasVencimiento.containsKey(codigoDepositante)) {
                Set<Date> fechasVencimiento = depositanteFechasVencimiento.get(codigoDepositante);
                //aca hara un bucle para todos los casos posibles
                while (fechasVencimiento.contains(fechaVencimiento)) {
                    // Si la fecha de vencimiento ya existe, ajustarla
                    fechaVencimiento = ajustarFechaVencimiento(fechaVencimiento);
                    registro.setFechaVencimiento(fechaVencimiento);
                }
            } else {
                // Si es la primera vez que se encuentra este código de depositante, agregarlo al mapa
                depositanteFechasVencimiento.put(codigoDepositante, new HashSet<>());
            }
            
            // Agregar la fecha de vencimiento al conjunto correspondiente al código de depositante
            depositanteFechasVencimiento.get(codigoDepositante).add(fechaVencimiento);
        }
    }
    
}
