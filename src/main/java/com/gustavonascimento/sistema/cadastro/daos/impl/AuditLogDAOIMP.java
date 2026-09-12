/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.daos.impl;

import com.gustavonascimento.sistema.cadastro.daos.interfaces.AuditLogDAOInterface;
import com.gustavonascimento.sistema.cadastro.exceptions.PersistenciaException;
import com.gustavonascimento.sistema.cadastro.infra.ConnectionFactory;
import com.gustavonascimento.sistema.cadastro.models.AuditLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gustavo
 */
public class AuditLogDAOIMP implements AuditLogDAOInterface {
    
     private static final Logger logger = LoggerFactory.getLogger(AuditLogDAOIMP.class);

    private static final String SQL_INSERT =
            "INSERT INTO tb_audit_log (user_id, action, details) VALUES (?, ?, ?)";

    @Override
    public void save(AuditLog auditLog) throws PersistenciaException {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT)) {

            if (auditLog.getUserId() != null) {
                stmt.setLong(1, auditLog.getUserId());
            } else {
                stmt.setNull(1, Types.BIGINT);
            }
            stmt.setString(2, auditLog.getAction().name());
            stmt.setString(3, auditLog.getDetails());
            stmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("Erro ao gravar log de auditoria", e);
            throw new PersistenciaException("Não foi possível gravar o log de auditoria.", e);
        }
    }
    
}
