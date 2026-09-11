/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.services;

import com.gustavonascimento.sistema.cadastro.daos.interfaces.UserDAOInterface;
import com.gustavonascimento.sistema.cadastro.daos.impl.UserDAOIMP;
import com.gustavonascimento.sistema.cadastro.exceptions.PersistenciaException;
import com.gustavonascimento.sistema.cadastro.exceptions.ValidacaoException;
import com.gustavonascimento.sistema.cadastro.infra.PasswordHasher;
import com.gustavonascimento.sistema.cadastro.models.User;

import java.util.Optional;

/**
 *
 * @author Gustavo
 */
public class AuthenticationService {
    
     private final UserDAOInterface userDAO;

    public AuthenticationService() {
        this.userDAO = new UserDAOIMP();
    }

    public AuthenticationService(UserDAOInterface userDAO) {
        this.userDAO = userDAO;
    }

    public User authenticate(String email, String password)
            throws ValidacaoException, PersistenciaException {

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new ValidacaoException("Informe e-mail e senha.");
        }

        Optional<User> userOpt = userDAO.findByEmail(email.trim().toLowerCase());

        ValidacaoException credenciaisInvalidas =
                new ValidacaoException("E-mail ou senha inválidos.");

        if (userOpt.isEmpty()) {
            throw credenciaisInvalidas;
        }

        User user = userOpt.get();
        boolean correctPassword = PasswordHasher.verifica(
                password, user.getSalt(), user.getPasswordHash());

        if (!correctPassword) {
            throw credenciaisInvalidas;
        }

        return user;
    }
    
}
