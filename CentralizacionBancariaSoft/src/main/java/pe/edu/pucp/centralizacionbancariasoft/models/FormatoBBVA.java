/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.pucp.centralizacionbancariasoft.models;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Roberto
 */
public class FormatoBBVA {
    private String nombres;
    private String dniPostulante;
    private String conceptoPago;
    private Date fechaVencimiento;
    private Date fechaBloqueo;
    private double importeMaxCobrarDeuda;
    //private int tipoMoneda; //1: soles | 2: dólares

    public FormatoBBVA(String nombres, String dniPostulante, String serie, int numero, Date fechaVencimiento, Date fechaBloqueo, double importeMaxCobrarDeuda) {
        this.nombres = nombres;
        this.dniPostulante = dniPostulante;
        this.conceptoPago = serie + " " + numero;
        this.fechaVencimiento = fechaVencimiento;
        this.fechaBloqueo = fechaBloqueo;
        this.importeMaxCobrarDeuda = importeMaxCobrarDeuda;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getDniPostulante() {
        return dniPostulante;
    }

    public void setDniPostulante(String dniPostulante) {
        this.dniPostulante = dniPostulante;
    }

    public String getConceptoPago() {
        return conceptoPago;
    }

    public void setConceptoPago(String conceptoPago) {
        this.conceptoPago = conceptoPago;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Date getFechaBloqueo() {
        return fechaBloqueo;
    }

    public void setFechaBloqueo(Date fechaBloqueo) {
        this.fechaBloqueo = fechaBloqueo;
    }

    public double getImporteMaxCobrarDeuda() {
        return importeMaxCobrarDeuda;
    }

    public void setImporteMaxCobrarDeuda(double importeMaxCobrarDeuda) {
        this.importeMaxCobrarDeuda = importeMaxCobrarDeuda;
    }

    public FormatoBBVA() {
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        String fechaVencimientoStr = (fechaVencimiento != null) ? sdf.format(fechaVencimiento) : "null";
        String fechaBloqueoStr = (fechaBloqueo != null) ? sdf.format(fechaBloqueo) : "null";
        
        return "FormatoBBVA{" +
                "nombres='" + nombres + '\'' +
                ", dniPostulante='" + dniPostulante + '\'' +
                ", conceptoPago='" + conceptoPago + '\'' +
                ", fechaVencimiento=" + fechaVencimientoStr +
                ", fechaBloqueo=" + fechaBloqueoStr +
                ", importeMaxCobrarDeuda=" + importeMaxCobrarDeuda +
                '}';
    }
    
}
