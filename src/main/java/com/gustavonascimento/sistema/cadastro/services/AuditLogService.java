/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.services;

import com.gustavonascimento.sistema.cadastro.daos.impl.AuditLogDAOIMP;
import com.gustavonascimento.sistema.cadastro.daos.interfaces.AuditLogDAOInterface;
import com.gustavonascimento.sistema.cadastro.exceptions.PersistenciaException;
import com.gustavonascimento.sistema.cadastro.models.enums.AuditAction;
import com.gustavonascimento.sistema.cadastro.models.AuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gustavo
 */
public class AuditLogService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuditLogService.class);

    private final AuditLogDAOInterface auditLogDAO;

    public AuditLogService() {
        this.auditLogDAO = new AuditLogDAOIMP();
    }

    public AuditLogService(AuditLogDAOInterface auditLogDAO) {
        this.auditLogDAO = auditLogDAO;
    }

    public void log(Long userId, AuditAction action, String details) {
        try {
            auditLogDAO.save(new AuditLog(userId, action, details));
        } catch (PersistenciaException e) {
            logger.error("Falha ao registrar auditoria (ação: {})", action, e);
        }
    }
    
}
