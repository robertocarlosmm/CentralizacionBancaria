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
public class DataCenturia {
    private int numLinea;
    private Date fechaVencimiento;
    private String serie;
    private int numero;
    private String numDoi;
    private String razonSocial;
    private int moneda;
    private double monto;
    //private String codigoUnidad;
    //private String nombreUnidad;
    //private String nombreSubUnidad;

    public DataCenturia(int numLinea, Date fechaVencimiento, String serie, int numero, String numDoi, String razonSocial, int moneda, double monto) {
        this.numLinea = numLinea;
        this.fechaVencimiento = fechaVencimiento;
        this.serie = serie;
        this.numero = numero;
        this.numDoi = numDoi;
        this.razonSocial = razonSocial;
        this.moneda = moneda;
        this.monto = monto;
    }

    public int getNumLinea() {
        return numLinea;
    }

    public void setNumLinea(int numLinea) {
        this.numLinea = numLinea;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
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

    public String getNumDoi() {
        return numDoi;
    }

    public void setNumDoi(String numDoi) {
        this.numDoi = numDoi;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public int getMoneda() {
        return moneda;
    }

    public void setMoneda(int moneda) {
        this.moneda = moneda;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    @Override
    public String toString() {
        return "DataCenturia{" + "numLinea=" + numLinea + ", fechaVencimiento=" + fechaVencimiento + ", serie=" + serie + ", numero=" + numero + ", numDoi=" + numDoi + ", razonSocial=" + razonSocial +  ", moneda=" + moneda + ", monto=" + monto + '}';
    }

    public DataCenturia() {
    }
    
    
    
}
