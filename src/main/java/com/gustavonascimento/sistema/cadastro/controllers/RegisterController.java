/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.controllers;

import com.gustavonascimento.sistema.cadastro.exceptions.PersistenciaException;
import com.gustavonascimento.sistema.cadastro.exceptions.ValidacaoException;
import com.gustavonascimento.sistema.cadastro.services.UserService;
import com.gustavonascimento.sistema.cadastro.views.RegisterView;

import javax.swing.SwingWorker;

/**
 *
 * @author Gustavo
 */
public class RegisterController {
    
    private final UserService userService;
    private final RegisterView view;

    public RegisterController(RegisterView view) {
        this.view = view;
        this.userService = new UserService();
    }

    public void register(String name, String email, String password) {
        view.setFormEnabled(false);

        SwingWorker<Void, Void> worker = new SwingWorker<>() {

            private ValidacaoException validacaoErro;
            private PersistenciaException persistenciaErro;

            @Override
            protected Void doInBackground() {
                try {
                    userService.save(name, email, password);
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
                    get();
                    view.onRegisterSuccess();
                } catch (Exception e) {
                    view.showError("Ocorreu um erro inesperado ao cadastrar.");
                }
            }
        };

        worker.execute();
    }
    
}
