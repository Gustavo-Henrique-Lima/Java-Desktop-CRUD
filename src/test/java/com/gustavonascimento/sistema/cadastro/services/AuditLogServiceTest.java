/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.services;

import com.gustavonascimento.sistema.cadastro.daos.interfaces.AuditLogDAOInterface;
import com.gustavonascimento.sistema.cadastro.exceptions.PersistenciaException;
import com.gustavonascimento.sistema.cadastro.models.enums.AuditAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

/**
 *
 * @author Gustavo
 */
@ExtendWith(MockitoExtension.class)
class AuditLogServiceTest {
    
      @Mock
    private AuditLogDAOInterface auditLogDAO;

    private AuditLogService auditLogService;

    @BeforeEach
    void setUp() {
        auditLogService = new AuditLogService(auditLogDAO);
    }

    @Test
    void failureToWriteAuditLogShouldNotThrowToTheCaller() throws Exception {
        doThrow(new PersistenciaException("Simulated failure", null))
                .when(auditLogDAO).save(any());

        assertDoesNotThrow(() ->
                auditLogService.log(1L, AuditAction.LOGIN_SUCCESS, "test"));
    }
    
}
