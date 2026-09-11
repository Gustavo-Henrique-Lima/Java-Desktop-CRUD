/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.exceptions;

/**
 *
 * @author Gustavo
 * Lançada quando algo falha na camada de persistência (conexão, SQL).
 */
public class PersistenciaException extends Exception {
    public PersistenciaException(String message, Throwable cause) {
        super(message, cause);
    }
}