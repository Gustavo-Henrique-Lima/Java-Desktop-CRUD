/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.daos.impl;

import com.gustavonascimento.sistema.cadastro.daos.interfaces.EmployeeDAOInterface;
import com.gustavonascimento.sistema.cadastro.exceptions.PersistenciaException;
import com.gustavonascimento.sistema.cadastro.infra.ConnectionFactory;
import com.gustavonascimento.sistema.cadastro.models.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gustavo
 */
public class EmployeeDAOIMP implements EmployeeDAOInterface {
    
    private static final Logger logger = LoggerFactory.getLogger(EmployeeDAOIMP.class);

    private static final String SQL_INSERT =
            "INSERT INTO tb_employees (name, admission_date, salary, active) VALUES (?, ?, ?, ?)";

    private static final String SQL_FIND_ALL =
            "SELECT id, name, admission_date, salary, active, created_at "
            + "FROM tb_employees ORDER BY name";
    
    private static final String SQL_UPDATE =
        "UPDATE tb_employees SET name = ?, admission_date = ?, salary = ?, active = ?, "
        + "updated_at = NOW() WHERE id = ?";

    private static final String SQL_DELETE = "DELETE FROM tb_employees WHERE id = ?";

    @Override
    public Employee save(Employee employee) throws PersistenciaException {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, employee.getName());
            stmt.setDate(2, Date.valueOf(employee.getAdmissionDate()));
            stmt.setBigDecimal(3, employee.getSalary());
            stmt.setBoolean(4, employee.isActive());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    employee.setId(rs.getLong(1));
                }
            }
            return employee;

        } catch (SQLException e) {
            logger.error("Erro ao salvar funcionário", e);
            throw new PersistenciaException("Não foi possível salvar o funcionário no momento.", e);
        }
    }

    @Override
    public List<Employee> findAll() throws PersistenciaException {
        List<Employee> employees = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                employees.add(mapEmployee(rs));
            }
            return employees;

        } catch (SQLException e) {
            logger.error("Erro ao listar funcionários", e);
            throw new PersistenciaException("Não foi possível carregar a lista de funcionários.", e);
        }
    }

    private Employee mapEmployee(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        employee.setId(rs.getLong("id"));
        employee.setName(rs.getString("name"));
        employee.setAdmissionDate(rs.getDate("admission_date").toLocalDate());
        employee.setSalary(rs.getBigDecimal("salary"));
        employee.setActive(rs.getBoolean("active"));

        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            employee.setCreatedAt(ts.toLocalDateTime());
        }
        return employee;
    }
    
    @Override
    public Employee update(Employee employee) throws PersistenciaException {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {

            stmt.setString(1, employee.getName());
            stmt.setDate(2, Date.valueOf(employee.getAdmissionDate()));
            stmt.setBigDecimal(3, employee.getSalary());
            stmt.setBoolean(4, employee.isActive());
            stmt.setLong(5, employee.getId());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new PersistenciaException("Funcionário não encontrado para atualização.", null);
            }
            return employee;

        } catch (SQLException e) {
            logger.error("Erro ao atualizar funcionário id={}", employee.getId(), e);
            throw new PersistenciaException("Não foi possível atualizar o funcionário.", e);
        }
    }

    @Override
    public void delete(Long id) throws PersistenciaException {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETE)) {

            stmt.setLong(1, id);
            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new PersistenciaException("Funcionário não encontrado para exclusão.", null);
            }

        } catch (SQLException e) {
            logger.error("Erro ao excluir funcionário id={}", id, e);
            throw new PersistenciaException("Não foi possível excluir o funcionário.", e);
        }
    }
    
}
