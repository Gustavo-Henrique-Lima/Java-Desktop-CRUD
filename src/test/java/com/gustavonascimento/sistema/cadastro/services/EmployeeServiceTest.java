/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.services;

import com.gustavonascimento.sistema.cadastro.daos.interfaces.EmployeeDAOInterface;
import com.gustavonascimento.sistema.cadastro.exceptions.ValidacaoException;
import com.gustavonascimento.sistema.cadastro.models.enums.AuditAction;
import com.gustavonascimento.sistema.cadastro.models.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author Gustavo
 */
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    
    @Mock
    private EmployeeDAOInterface employeeDAO;

    @Mock
    private AuditLogService auditLogService;

    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeService(employeeDAO, auditLogService);
    }

    @Test
    void shouldRegisterValidEmployee() throws Exception {
        when(employeeDAO.save(any(Employee.class))).thenAnswer(invocation -> {
            Employee e = invocation.getArgument(0);
            e.setId(10L);
            return e;
        });

        Employee result = employeeService.register(
                1L, "Bruno Costa", LocalDate.now().minusDays(5),
                new BigDecimal("3000.00"), true);

        assertEquals(10L, result.getId());
        verify(auditLogService).log(eq(1L), eq(AuditAction.EMPLOYEE_REGISTERED_SUCESS), anyString());
    }

    @Test
    void shouldRejectNegativeSalaryWithoutCallingTheDAO() {
        assertThrows(ValidacaoException.class, () -> employeeService.register(
                1L, "Bruno Costa", LocalDate.now(), new BigDecimal("-500"), true));

        verifyNoInteractions(employeeDAO);
        verifyNoInteractions(auditLogService);
    }

    @Test
    void shouldRejectFutureAdmissionDate() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        assertThrows(ValidacaoException.class, () -> employeeService.register(
                1L, "Bruno Costa", tomorrow, new BigDecimal("3000"), true));

        verifyNoInteractions(employeeDAO);
    }

    @Test
    void shouldUpdateEmployeeAndRecordAuditLog() throws Exception {
        when(employeeDAO.update(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Employee result = employeeService.update(
                1L, 10L, "Bruno Costa Silva", LocalDate.now(), new BigDecimal("3500"), false);

        assertEquals(10L, result.getId());
        assertFalse(result.isActive());
        verify(auditLogService).log(eq(1L), eq(AuditAction.EMPLOYEE_UPDATED), anyString());
    }

    @Test
    void shouldDeleteEmployeeAndRecordAuditLog() throws Exception {
        employeeService.delete(1L, 10L);

        verify(employeeDAO).delete(10L);
        verify(auditLogService).log(eq(1L), eq(AuditAction.EMPLOYEE_DELETED), anyString());
    }
    
}
