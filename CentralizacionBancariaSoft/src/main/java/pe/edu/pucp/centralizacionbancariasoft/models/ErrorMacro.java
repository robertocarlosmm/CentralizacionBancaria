/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.pucp.centralizacionbancariasoft.models;

/**
 *
 * @author jyalico
 */
public class ErrorMacro {
    private String archivo;
    private String numLinea;
    private String detalle;

    public ErrorMacro(String archivo, String numLinea, String detalle) {
        this.archivo = archivo;
        this.numLinea = numLinea;
        this.detalle = detalle;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getNumLinea() {
        return numLinea;
    }

    public void setNumLinea(String numLinea) {
        this.numLinea = numLinea;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }
    
}
