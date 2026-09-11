/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.exceptions;

/**
 *
 * @author Gustavo
 * Lançada quando um dado de entrada não passa nas regras de negócio
 * (campo vazio, formato inválido, etc.).
 */
public class ValidacaoException extends Exception {
    public ValidacaoException(String message) {
        super(message);
    }
}