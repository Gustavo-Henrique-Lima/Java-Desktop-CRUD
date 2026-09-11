/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.daos.interfaces;

import com.gustavonascimento.sistema.cadastro.exceptions.PersistenciaException;
import com.gustavonascimento.sistema.cadastro.models.User;

import java.util.Optional;

/**
 *
 * @author Gustavo
 */
public interface UserDAOInterface {
    
    User save(User user) throws PersistenciaException;

    Optional<User> findByEmail(String email) throws PersistenciaException;

    boolean emailExists(String email) throws PersistenciaException;
    
}
