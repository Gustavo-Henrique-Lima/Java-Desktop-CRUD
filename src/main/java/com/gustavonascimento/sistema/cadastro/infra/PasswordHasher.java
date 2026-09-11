/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.infra;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 *
 * @author Gustavo
 */
public class PasswordHasher {
    
    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH_BYTES = 16;

    private PasswordHasher() {
    }

    public static String gerarSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH_BYTES];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hash(String senhaPlana, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            digest.update(Base64.getDecoder().decode(salt));
            byte[] hashBytes = digest.digest(senhaPlana.getBytes("UTF-8"));
            return bytesParaHex(hashBytes);
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            throw new IllegalStateException("Falha ao gerar hash de senha", e);
        }
    }

    public static boolean verifica(String senhaPlana, String salt, String hashArmazenado) {
        String hashCalculado = hash(senhaPlana, salt);
        return hashCalculado.equals(hashArmazenado);
    }

    private static String bytesParaHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    
}
