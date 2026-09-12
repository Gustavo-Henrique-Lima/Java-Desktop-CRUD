/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.daos.interfaces;

import com.gustavonascimento.sistema.cadastro.exceptions.PersistenciaException;
import com.gustavonascimento.sistema.cadastro.models.AuditLog;

/**
 *
 * @author Gustavo
 */
public interface AuditLogDAOInterface {
    
    void save(AuditLog auditLog) throws PersistenciaException;
}