/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.services;

import com.gustavonascimento.sistema.cadastro.daos.interfaces.UserDAOInterface;
import com.gustavonascimento.sistema.cadastro.exceptions.PersistenciaException;
import com.gustavonascimento.sistema.cadastro.exceptions.ValidacaoException;
import com.gustavonascimento.sistema.cadastro.models.enums.AuditAction;
import com.gustavonascimento.sistema.cadastro.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author Gustavo
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDAOInterface userDAO;

    @Mock
    private AuditLogService auditLogService;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userDAO, auditLogService);
    }

    @Test
    void shouldRegisterValidUserSuccessfully() throws Exception {
        when(userDAO.emailExists("ana@email.com")).thenReturn(false);
        when(userDAO.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });

        User result = userService.save("Ana Silva", "ana@email.com", "password1234");

        assertEquals(1L, result.getId());
        assertEquals("ana@email.com", result.getEmail());
        assertNotEquals("password1234", result.getPasswordHash());

        verify(auditLogService).log(eq(1L), eq(AuditAction.USER_REGISTERED_SUCESS), anyString());
    }

    @Test
    void shouldRejectRegistrationWithExistingEmail() throws Exception {
        when(userDAO.emailExists("ana@email.com")).thenReturn(true);

        assertThrows(ValidacaoException.class,
                () -> userService.save("Ana Silva", "ana@email.com", "password1234"));

        verify(userDAO, never()).save(any());
        verify(auditLogService).log(isNull(), eq(AuditAction.USER_REGISTERED_FAILURE), anyString());
    }

    @Test
    void shouldRejectInvalidNameWithoutQueryingTheDatabase() {
        assertThrows(ValidacaoException.class,
                () -> userService.save("An", "ana@email.com", "password1234"));

        verifyNoInteractions(userDAO);
        verifyNoInteractions(auditLogService);
    }

    @Test
    void shouldPropagatePersistenceExceptionWithoutMasking() throws Exception {
        when(userDAO.emailExists(anyString())).thenReturn(false);
        when(userDAO.save(any(User.class)))
                .thenThrow(new PersistenciaException("Simulated database failure", null));

        assertThrows(PersistenciaException.class,
                () -> userService.save("Ana Silva", "ana@email.com", "password1234"));

        verify(auditLogService, never()).log(any(), eq(AuditAction.USER_REGISTERED_SUCESS), anyString());
    }

    @Test
    void shouldNormalizeEmailToLowercaseAndTrimName() throws Exception {
        when(userDAO.emailExists(anyString())).thenReturn(false);
        when(userDAO.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.save("  Ana Silva  ", "ANA@EMAIL.COM", "password1234");

        assertEquals("Ana Silva", result.getName());
        assertEquals("ana@email.com", result.getEmail());
    }
}