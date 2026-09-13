/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.services;

import com.gustavonascimento.sistema.cadastro.daos.interfaces.UserDAOInterface;
import com.gustavonascimento.sistema.cadastro.exceptions.ValidacaoException;
import com.gustavonascimento.sistema.cadastro.infra.PasswordHasher;
import com.gustavonascimento.sistema.cadastro.models.enums.AuditAction;
import com.gustavonascimento.sistema.cadastro.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author Gustavo
 */
@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    
    @Mock
    private UserDAOInterface userDAO;

    @Mock
    private AuditLogService auditLogService;

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationService(userDAO, auditLogService);
    }

    private User createUserWithPassword(String plainPassword) {
        String salt = PasswordHasher.gerarSalt();
        String hash = PasswordHasher.hash(plainPassword, salt);
        User user = new User("Ana Silva", "ana@email.com", hash, salt);
        user.setId(1L);
        return user;
    }

    @Test
    void shouldAuthenticateWithCorrectCredentials() throws Exception {
        User user = createUserWithPassword("correctPassword");
        when(userDAO.findByEmail("ana@email.com")).thenReturn(Optional.of(user));

        User authenticated = authenticationService.authenticate("ana@email.com", "correctPassword");

        assertEquals(user.getId(), authenticated.getId());
        verify(auditLogService).log(eq(1L), eq(AuditAction.LOGIN_SUCCESS), isNull());
    }

    @Test
    void shouldRejectIncorrectPasswordWithGenericMessage() throws Exception {
        User user = createUserWithPassword("correctPassword");
        when(userDAO.findByEmail("ana@email.com")).thenReturn(Optional.of(user));

        ValidacaoException error = assertThrows(ValidacaoException.class,
                () -> authenticationService.authenticate("ana@email.com", "wrongPassword"));

        assertEquals("E-mail ou senha inválidos.", error.getMessage());
        verify(auditLogService).log(eq(1L), eq(AuditAction.LOGIN_FAILURE), anyString());
    }

    @Test
    void shouldRejectNonExistentEmailWithTheSameGenericMessage() throws Exception {
        when(userDAO.findByEmail("doesnotexist@email.com")).thenReturn(Optional.empty());

        ValidacaoException error = assertThrows(ValidacaoException.class,
                () -> authenticationService.authenticate("doesnotexist@email.com", "anyPassword"));

        assertEquals("E-mail ou senha inválidos.", error.getMessage());
        verify(auditLogService).log(isNull(), eq(AuditAction.LOGIN_FAILURE), anyString());
    }

    @Test
    void shouldRejectBlankFieldsWithoutQueryingTheDatabase() {
        assertThrows(ValidacaoException.class,
                () -> authenticationService.authenticate("", ""));

        verifyNoInteractions(userDAO);
        verifyNoInteractions(auditLogService);
    }
    
}
