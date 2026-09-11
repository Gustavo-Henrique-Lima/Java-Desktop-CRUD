/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.utils;

import com.gustavonascimento.sistema.cadastro.exceptions.ValidacaoException;

import java.util.regex.Pattern;

/**
 *
 * @author Gustavo
 */
public final class Validators {
    
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    private static final int PASSWORD_MIN_LENGTH = 8;
    private static final int NAME_MIN_LENGTH = 3;

    private Validators() {
    }

    public static void validName(String name) throws ValidacaoException {
        if (name == null || name.trim().length() < NAME_MIN_LENGTH) {
            throw new ValidacaoException("O nome deve ter pelo menos " + NAME_MIN_LENGTH + " caracteres.");
        }
    }

    public static void validEmail(String email) throws ValidacaoException {
        if (email == null || !EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new ValidacaoException("Informe um e-mail válido.");
        }
    }

    public static void validPassword(String password) throws ValidacaoException {
        if (password == null || password.length() < PASSWORD_MIN_LENGTH) {
            throw new ValidacaoException("A senha deve ter pelo menos " + PASSWORD_MIN_LENGTH + " caracteres.");
        }
    }
    
}
