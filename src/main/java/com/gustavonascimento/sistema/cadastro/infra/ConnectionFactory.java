/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.infra;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author Gustavo
 */
public class ConnectionFactory {
    
     private static final HikariDataSource DATA_SOURCE;

    static {
        Properties props = loadProperties();

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(props.getProperty("db.url"));
        config.setUsername(props.getProperty("db.user"));
        config.setPassword(props.getProperty("db.password"));
        config.setMaximumPoolSize(
                Integer.parseInt(props.getProperty("db.pool.maxSize", "10")));
        config.setConnectionTimeout(5000);

        DATA_SOURCE = new HikariDataSource(config);
    }

    private ConnectionFactory() {
    }

    private static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = ConnectionFactory.class
                .getClassLoader()
                .getResourceAsStream("db.properties")) {

            if (input == null) {
                throw new IllegalStateException(
                        "Arquivo db.properties não encontrado em src/main/resources. "
                        + "Copie db.properties.example e preencha as credenciais.");
            }
            props.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao carregar db.properties", e);
        }
        return props;
    }

    public static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }
    
}
