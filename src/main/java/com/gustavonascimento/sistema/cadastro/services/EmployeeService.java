/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.services;

import com.gustavonascimento.sistema.cadastro.daos.impl.EmployeeDAOIMP;
import com.gustavonascimento.sistema.cadastro.daos.interfaces.EmployeeDAOInterface;
import com.gustavonascimento.sistema.cadastro.exceptions.PersistenciaException;
import com.gustavonascimento.sistema.cadastro.exceptions.ValidacaoException;
import com.gustavonascimento.sistema.cadastro.models.Employee;
import com.gustavonascimento.sistema.cadastro.models.enums.AuditAction;
import com.gustavonascimento.sistema.cadastro.utils.Validators;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Gustavo
 */
public class EmployeeService {
    
    private final EmployeeDAOInterface employeeDAO;
    private final AuditLogService auditLogService;

    public EmployeeService() {
        this(new EmployeeDAOIMP(), new AuditLogService());
    }
    
    public EmployeeService(EmployeeDAOInterface employeeDAO, AuditLogService auditLogService) {
        this.employeeDAO = employeeDAO;
        this.auditLogService = auditLogService;
    }

    public Employee register(Long userId, String name, LocalDate admissionDate, BigDecimal salary, boolean active)
            throws ValidacaoException, PersistenciaException {

        Validators.validName(name);
        Validators.validAdmissionDate(admissionDate);
        Validators.validSalary(salary);

        Employee employee = new Employee(name.trim(), admissionDate, salary, active);
        
        auditLogService.log(userId, AuditAction.EMPLOYEE_REGISTERED_SUCESS,
             "Funcionário cadastrado: " + employee.getName());
        
        return employeeDAO.save(employee);
    }

    public List<Employee> listAll() throws PersistenciaException {
        return employeeDAO.findAll();
    }
    
    public Employee update(Long userId, Long employeeId, String name, LocalDate admissionDate,
        BigDecimal salary, boolean active)
        throws ValidacaoException, PersistenciaException {

        Validators.validName(name);
        Validators.validAdmissionDate(admissionDate);
        Validators.validSalary(salary);

        Employee employee = new Employee(name.trim(), admissionDate, salary, active);
        employee.setId(employeeId);
        Employee updatedEmployee = employeeDAO.update(employee);

        auditLogService.log(userId, AuditAction.EMPLOYEE_UPDATED,
                "Funcionário atualizado: " + updatedEmployee.getName() + " (id=" + updatedEmployee.getId() + ")");

        return updatedEmployee;
    }

    public void delete(Long userId, Long employeeId) throws PersistenciaException {
        employeeDAO.delete(employeeId);

        auditLogService.log(userId, AuditAction.EMPLOYEE_DELETED,
                "Funcionário excluído (id=" + employeeId + ")");
    }
    
}
