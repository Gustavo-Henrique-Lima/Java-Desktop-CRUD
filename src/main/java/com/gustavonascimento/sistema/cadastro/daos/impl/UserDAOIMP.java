/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.daos.impl;

import com.gustavonascimento.sistema.cadastro.daos.interfaces.UserDAOInterface;
import com.gustavonascimento.sistema.cadastro.exceptions.PersistenciaException;
import com.gustavonascimento.sistema.cadastro.infra.ConnectionFactory;
import com.gustavonascimento.sistema.cadastro.models.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.util.Optional;

/**
 *
 * @author Gustavo
 */
public class UserDAOIMP implements UserDAOInterface {
    
    private static final Logger logger = LoggerFactory.getLogger(UserDAOIMP.class);
    
    private static final String SQL_INSERT =
            "INSERT INTO tb_users (name, email, password_hash, salt) VALUES (?, ?, ?, ?)";

    private static final String SQL_BUSCAR_POR_EMAIL =
            "SELECT id, name, email, password_hash, salt, created_at FROM tb_users WHERE email = ?";

    private static final String SQL_EXISTE_EMAIL =
            "SELECT 1 FROM tb_users WHERE email = ?";

    @Override
    public User save(User user) throws PersistenciaException {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPasswordHash());
            stmt.setString(4, user.getSalt());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getLong(1));
                }
            }
            return user;

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                logger.warn("Tentativa de cadastro com e-mail já existente: {}", user.getEmail());
                throw new PersistenciaException("Este e-mail já está cadastrado.", e);
            }
            logger.error("Erro ao salvar usuário", e);
            throw new PersistenciaException("Não foi possível salvar o usuário no momento.", e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) throws PersistenciaException {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_BUSCAR_POR_EMAIL)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(createNewUser(rs));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            logger.error("Erro ao buscar usuário por e-mail", e);
            throw new PersistenciaException("Não foi possível consultar o usuário.", e);
        }
    }

    @Override
    public boolean emailExists(String email) throws PersistenciaException {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_EXISTE_EMAIL)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            logger.error("Erro ao verificar existência de e-mail", e);
            throw new PersistenciaException("Não foi possível validar o e-mail.", e);
        }
    }

    private User createNewUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setSalt(rs.getString("salt"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            user.setCreatedAt(ts.toLocalDateTime());
        }
        logger.info("Usuário convertido com sucesso");
        return user;
    }
}
