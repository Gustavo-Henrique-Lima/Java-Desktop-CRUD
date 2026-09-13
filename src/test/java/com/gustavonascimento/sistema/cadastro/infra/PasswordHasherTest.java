/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.infra;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Gustavo
 */
class PasswordHasherTest {
    
     @Test
    void twoGeneratedSaltsShouldBeDifferent() {
        String salt1 = PasswordHasher.gerarSalt();
        String salt2 = PasswordHasher.gerarSalt();
        assertNotEquals(salt1, salt2, "Each salt must be unique and random");
    }

    @Test
    void samePasswordWithSameSaltShouldProduceSameHash() {
        String salt = PasswordHasher.gerarSalt();
        String hash1 = PasswordHasher.hash("myPassword123", salt);
        String hash2 = PasswordHasher.hash("myPassword123", salt);
        assertEquals(hash1, hash2, "Hashing must be deterministic for the same input");
    }

    @Test
    void samePasswordWithDifferentSaltsShouldProduceDifferentHashes() {
        String hash1 = PasswordHasher.hash("myPassword123", PasswordHasher.gerarSalt());
        String hash2 = PasswordHasher.hash("myPassword123", PasswordHasher.gerarSalt());
        assertNotEquals(hash1, hash2, "The salt must change the resulting hash, even for equal passwords");
    }

    @Test
    void shouldVerifyCorrectPasswordAsValid() {
        String salt = PasswordHasher.gerarSalt();
        String hash = PasswordHasher.hash("correctPassword", salt);
        assertTrue(PasswordHasher.verifica("correctPassword", salt, hash));
    }

    @Test
    void shouldRejectIncorrectPassword() {
        String salt = PasswordHasher.gerarSalt();
        String hash = PasswordHasher.hash("correctPassword", salt);
        assertFalse(PasswordHasher.verifica("wrongPassword", salt, hash));
    }
    
}
