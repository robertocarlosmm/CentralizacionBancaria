/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.pucp.centralizacionbancariasoft.models;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Roberto
 */
public class FormatoBCP {
    private String codigoDepositante;
    private String nombreDepositante;
    private String informacionRetorno;//serie + " " + numero
    private Date fechaVencimiento;
    private double montoAPagar;
    private String numeroDocumentoPago;

    public FormatoBCP(String codigoDepositante, String nombreDepositante, String serie, 
            int numero, Date fechaVencimiento, double montoAPagar) {
        this.codigoDepositante = codigoDepositante;
        this.nombreDepositante = nombreDepositante;
        this.informacionRetorno = serie + " " + numero;
        this.fechaVencimiento = fechaVencimiento;
        this.montoAPagar = montoAPagar;
        this.numeroDocumentoPago = serie + numero;

        // Formatear fecha de vencimiento
        /*SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechaFormateada = sdf.format(fechaVencimiento);*/
        //System.out.println("Fecha de vencimiento formateada: " + fechaFormateada);

        // Verificar si el año es menor a 2000
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.fechaVencimiento);
        int year = cal.get(Calendar.YEAR);
        if (year < 2000) {
            System.out.println("El año de vencimiento es menor a 2000: " + year);
        }
    }

    private Date ajustarAnio(Date fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        int year = cal.get(Calendar.YEAR);
        if (year < 2000) {
            cal.set(Calendar.YEAR, year + 2000);
            return cal.getTime();
        }
        return fecha;
    }
    
    public String getCodigoDepositante() {
        return codigoDepositante;
    }

    public void setCodigoDepositante(String codigoDepositante) {
        this.codigoDepositante = codigoDepositante;
    }

    public String getNombreDepositante() {
        return nombreDepositante;
    }

    public void setNombreDepositante(String nombreDepositante) {
        this.nombreDepositante = nombreDepositante;
    }

    public String getInformacionRetorno() {
        return informacionRetorno;
    }

    public void setInformacionRetorno(String informacionRetorno) {
        this.informacionRetorno = informacionRetorno;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public double getMontoAPagar() {
        return montoAPagar;
    }

    public void setMontoAPagar(double montoAPagar) {
        this.montoAPagar = montoAPagar;
    }

    public String getNumeroDocumentoPago() {
        return numeroDocumentoPago;
    }

    public void setNumeroDocumentoPago(String numeroDocumentoPago) {
        this.numeroDocumentoPago = numeroDocumentoPago;
    }

    @Override
    public String toString() {
        return "FormatoBCP{" + "codigoDepositante=" + codigoDepositante + ", nombreDepositante=" + nombreDepositante + ", informacionRetorno=" + informacionRetorno + ", fechaVencimiento=" + fechaVencimiento + ", montoAPagar=" + montoAPagar + ", numeroDocumentoPago=" + numeroDocumentoPago + '}';
    }
    
}
