/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.infra;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Gustavo
 */
public class DatabaseMigrator {
    
    private static final Logger logger = LoggerFactory.getLogger(DatabaseMigrator.class);

    private DatabaseMigrator() {
    }

    public static void migrate() {
        Properties props = loadProperties();

        Flyway flyway = Flyway.configure()
                .dataSource(
                        props.getProperty("db.url"),
                        props.getProperty("db.user"),
                        props.getProperty("db.password"))
                .locations("classpath:db/migration")
                .load();

        logger.info("Iniciando migração do banco de dados...");
        flyway.migrate();
        logger.info("Migração concluída com sucesso.");
    }

    private static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = DatabaseMigrator.class
                .getClassLoader()
                .getResourceAsStream("db.properties")) {

            if (input == null) {
                throw new IllegalStateException(
                        "Arquivo db.properties não encontrado em src/main/resources.");
            }
            props.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao carregar db.properties", e);
        }
        return props;
    }
    
}
