/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.controllers;

import com.gustavonascimento.sistema.cadastro.exceptions.PersistenciaException;
import com.gustavonascimento.sistema.cadastro.exceptions.ValidacaoException;
import com.gustavonascimento.sistema.cadastro.models.User;
import com.gustavonascimento.sistema.cadastro.services.AuthenticationService;
import com.gustavonascimento.sistema.cadastro.views.LoginView;

import javax.swing.SwingWorker;

/**
 *
 * @author Gustavo
 */
public class LoginController {
    
    private final AuthenticationService authenticationService;
    private final LoginView view;

    public LoginController(LoginView view) {
        this.view = view;
        this.authenticationService = new AuthenticationService();
    }

    public void login(String email, String password) {
        view.setFormEnabled(false);

        SwingWorker<User, Void> worker = new SwingWorker<>() {

            private ValidacaoException validacaoErro;
            private PersistenciaException persistenciaErro;

            @Override
            protected User doInBackground() {
                try {
                    return authenticationService.authenticate(email, password);
                } catch (ValidacaoException e) {
                    validacaoErro = e;
                } catch (PersistenciaException e) {
                    persistenciaErro = e;
                }
                return null;
            }

            @Override
            protected void done() {
                view.setFormEnabled(true);

                if (validacaoErro != null) {
                    view.showError(validacaoErro.getMessage());
                    return;
                }
                if (persistenciaErro != null) {
                    view.showError(persistenciaErro.getMessage());
                    return;
                }

                try {
                    User user = get();
                    view.onLoginSuccess(user);
                } catch (Exception e) {
                    view.showError("Não foi possível concluir o login no momento.");
                }
            }
        };

        worker.execute();
    }
    
}
