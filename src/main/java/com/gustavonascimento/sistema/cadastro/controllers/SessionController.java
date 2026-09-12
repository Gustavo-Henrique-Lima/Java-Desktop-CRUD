/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.controllers;

import com.gustavonascimento.sistema.cadastro.models.User;
import com.gustavonascimento.sistema.cadastro.services.AuthenticationService;
import com.gustavonascimento.sistema.cadastro.session.UserSession;

import javax.swing.SwingWorker;

/**
 *
 * @author Gustavo
 */
public class SessionController {
    
    private final AuthenticationService authenticationService;

    public SessionController() {
        this.authenticationService = new AuthenticationService();
    }

    public void logout(Runnable onComplete) {
        User currentUser = UserSession.getInstance().getCurrentUser();

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                authenticationService.logout(currentUser);
                return null;
            }

            @Override
            protected void done() {
                UserSession.getInstance().logout();
                onComplete.run();
            }
        };

        worker.execute();
    }
    
}
