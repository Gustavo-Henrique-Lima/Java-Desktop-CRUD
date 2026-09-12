/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.daos.interfaces;

import com.gustavonascimento.sistema.cadastro.exceptions.PersistenciaException;
import com.gustavonascimento.sistema.cadastro.models.Employee;

import java.util.List;

/**
 *
 * @author Gustavo
 */
public interface EmployeeDAOInterface {
    
    Employee save(Employee employee) throws PersistenciaException;
    
    Employee update(Employee employee) throws PersistenciaException;   // novo

    void delete(Long id) throws PersistenciaException;  

    List<Employee> findAll() throws PersistenciaException;
    
}
