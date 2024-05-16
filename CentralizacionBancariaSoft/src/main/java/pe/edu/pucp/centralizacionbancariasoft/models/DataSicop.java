/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.pucp.centralizacionbancariasoft.models;

import java.util.Date;

/**
 *
 * @author Roberto
 */
public class DataSicop {
    private int numLinea;
    private String serie;
    private int numero;
    private String ruc;
    private String razonSocial;
    private Date fechaVencimiento;
    private String numDocumentoId;
    private double importe;
    private int moneda;

    public DataSicop(int numLinea, String serie, int numero, String ruc, String razonSocial, Date fechaVencimiento, String numDocumentoId, double importe, int moneda) {
        this.numLinea = numLinea;
        this.serie = serie;
        this.numero = numero;
        this.ruc = ruc;
        this.razonSocial = razonSocial;
        this.fechaVencimiento = fechaVencimiento;
        this.numDocumentoId = numDocumentoId;
        this.importe = importe;
        this.moneda = moneda;
    }

    public int getNumLinea() {
        return numLinea;
    }

    public void setNumLinea(int numLinea) {
        this.numLinea = numLinea;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getNumDocumentoId() {
        return numDocumentoId;
    }

    public void setNumDocumentoId(String numDocumentoId) {
        this.numDocumentoId = numDocumentoId;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public int getMoneda() {
        return moneda;
    }

    public void setMoneda(int moneda) {
        this.moneda = moneda;
    }

    @Override
    public String toString() {
        return "DataSicop{" + "numLinea=" + numLinea + ", serie=" + serie + ", numero=" + numero + ", ruc=" + ruc + ", razonSocial=" + razonSocial + ", fechaVencimiento=" + fechaVencimiento + ", numDocumentoId=" + numDocumentoId + ", importe=" + importe + ", moneda=" + moneda + '}';
    }
    
}
