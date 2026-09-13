/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.utils;

import com.gustavonascimento.sistema.cadastro.exceptions.ValidacaoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Gustavo
 */
class ValidatorsTest {
    
   @Test
    void shouldAcceptValidName() {
        assertDoesNotThrow(() -> Validators.validName("Ana Silva"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "Al"})
    void shouldRejectTooShortOrBlankName(String invalidName) {
        assertThrows(ValidacaoException.class, () -> Validators.validName(invalidName));
    }

    @Test
    void shouldRejectNullName() {
        assertThrows(ValidacaoException.class, () -> Validators.validName(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ana@email.com", "ana.silva+teste@empresa.com.br"})
    void shouldAcceptValidEmail(String email) {
        assertDoesNotThrow(() -> Validators.validEmail(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ana@", "ana.com", "@email.com", "ana email@x.com"})
    void shouldRejectInvalidEmail(String email) {
        assertThrows(ValidacaoException.class, () -> Validators.validEmail(email));
    }

    @Test
    void shouldAcceptPasswordWithEightOrMoreCharacters() {
        assertDoesNotThrow(() -> Validators.validPassword("12345678"));
    }

    @Test
    void shouldRejectShortPassword() {
        assertThrows(ValidacaoException.class, () -> Validators.validPassword("1234567"));
    }

    @Test
    void shouldAcceptTodayOrPastAdmissionDate() {
        assertDoesNotThrow(() -> Validators.validAdmissionDate(LocalDate.now()));
        assertDoesNotThrow(() -> Validators.validAdmissionDate(LocalDate.now().minusYears(1)));
    }

    @Test
    void shouldRejectFutureAdmissionDate() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        assertThrows(ValidacaoException.class, () -> Validators.validAdmissionDate(tomorrow));
    }

    @Test
    void shouldRejectNullAdmissionDate() {
        assertThrows(ValidacaoException.class, () -> Validators.validAdmissionDate(null));
    }

    @Test
    void shouldAcceptPositiveOrZeroSalary() {
        assertDoesNotThrow(() -> Validators.validSalary(BigDecimal.ZERO));
        assertDoesNotThrow(() -> Validators.validSalary(new BigDecimal("2500.50")));
    }

    @Test
    void shouldRejectNegativeSalary() {
        assertThrows(ValidacaoException.class,
                () -> Validators.validSalary(new BigDecimal("-1")));
    }

    @Test
    void shouldRejectNullSalary() {
        assertThrows(ValidacaoException.class, () -> Validators.validSalary(null));
    }
    
}
