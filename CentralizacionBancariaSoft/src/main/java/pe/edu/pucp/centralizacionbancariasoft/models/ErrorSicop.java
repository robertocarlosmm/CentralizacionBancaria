/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.pucp.centralizacionbancariasoft.models;

/**
 *
 * @author Roberto
 */
public class ErrorSicop {
    private int numeroLinea;
    private String tipoError;

    public int getNumeroLinea() {
        return numeroLinea;
    }

    public void setNumeroLinea(int numeroLinea) {
        this.numeroLinea = numeroLinea;
    }

    public String getTipoError() {
        return tipoError;
    }

    public void setTipoError(String tipoError) {
        this.tipoError = tipoError;
    }

    public ErrorSicop(int numeroLinea, String tipoError) {
        this.numeroLinea = numeroLinea;
        this.tipoError = tipoError;
    }

    @Override
    public String toString() {
        return "ErrorSicop => " + "numeroLinea: " + numeroLinea + ", tipoError: " + tipoError;
    }
    
}
