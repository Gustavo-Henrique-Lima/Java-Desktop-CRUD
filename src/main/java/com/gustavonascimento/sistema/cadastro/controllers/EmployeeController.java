/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.controllers;

import com.gustavonascimento.sistema.cadastro.exceptions.PersistenciaException;
import com.gustavonascimento.sistema.cadastro.exceptions.ValidacaoException;
import com.gustavonascimento.sistema.cadastro.models.Employee;
import com.gustavonascimento.sistema.cadastro.services.EmployeeService;
import com.gustavonascimento.sistema.cadastro.session.UserSession;
import com.gustavonascimento.sistema.cadastro.views.EmployeeView;

import javax.swing.SwingWorker;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Gustavo
 */
public class EmployeeController {
    
    private final EmployeeService employeeService;
    private final EmployeeView view;

    public EmployeeController(EmployeeView view) {
        this.view = view;
        this.employeeService = new EmployeeService();
    }

    public void register(String name, LocalDate admissionDate, BigDecimal salary, boolean active) {
        view.setFormEnabled(false);

        Long userId = UserSession.getInstance().isLoggedIn()
                ? UserSession.getInstance().getCurrentUser().getId()
                : null;

        SwingWorker<Void, Void> worker = new SwingWorker<>() {

            private ValidacaoException validacaoErro;
            private PersistenciaException persistenciaErro;

            @Override
            protected Void doInBackground() {
                try {
                    employeeService.register(userId, name, admissionDate, salary, active);
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

                view.onEmployeeRegistered();
                loadEmployees();
            }
        };

        worker.execute();
    }

    public void loadEmployees() {
        SwingWorker<List<Employee>, Void> worker = new SwingWorker<>() {

            private PersistenciaException persistenciaErro;

            @Override
            protected List<Employee> doInBackground() {
                try {
                    return employeeService.listAll();
                } catch (PersistenciaException e) {
                    persistenciaErro = e;
                    return List.of();
                }
            }

            @Override
            protected void done() {
                if (persistenciaErro != null) {
                    view.showError(persistenciaErro.getMessage());
                    return;
                }
                try {
                    view.updateTable(get());
                } catch (Exception e) {
                    view.showError("Não foi possível atualizar a lista de funcionários.");
                }
            }
        };

        worker.execute();
    }
    
    public void update(Long employeeId, String name, LocalDate admissionDate, BigDecimal salary, boolean active) {
        view.setFormEnabled(false);

        Long userId = UserSession.getInstance().isLoggedIn()
                ? UserSession.getInstance().getCurrentUser().getId()
                : null;

        SwingWorker<Void, Void> worker = new SwingWorker<>() {

            private ValidacaoException validacaoErro;
            private PersistenciaException persistenciaErro;

            @Override
            protected Void doInBackground() {
                try {
                    employeeService.update(userId, employeeId, name, admissionDate, salary, active);
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

                view.onEmployeeUpdated();
                loadEmployees();
            }
        };

        worker.execute();
    }

    public void delete(Long employeeId) {
        Long userId = UserSession.getInstance().isLoggedIn()
                ? UserSession.getInstance().getCurrentUser().getId()
                : null;

        SwingWorker<Void, Void> worker = new SwingWorker<>() {

            private PersistenciaException persistenciaErro;

            @Override
            protected Void doInBackground() {
                try {
                    employeeService.delete(userId, employeeId);
                } catch (PersistenciaException e) {
                    persistenciaErro = e;
                }
                return null;
            }

            @Override
            protected void done() {
                if (persistenciaErro != null) {
                    view.showError(persistenciaErro.getMessage());
                    return;
                }
                view.onEmployeeDeleted();
                loadEmployees();
            }
        };

        worker.execute();
    }
    
}
