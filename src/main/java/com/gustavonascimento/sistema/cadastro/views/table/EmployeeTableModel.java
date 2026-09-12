/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.views.table;

import com.gustavonascimento.sistema.cadastro.models.Employee;

import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Gustavo
 */
public class EmployeeTableModel extends AbstractTableModel {
    
    private static final String[] COLUMNS = {"Nome", "Admissão", "Salário", "Status"};
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private List<Employee> employees = new ArrayList<>();

    @Override
    public int getRowCount() {
        return employees.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMNS[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Employee employee = employees.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> employee.getName();
            case 1 -> employee.getAdmissionDate().format(DATE_FORMAT);
            case 2 -> formatCurrency(employee.getSalary());
            case 3 -> employee.isActive() ? "Ativo" : "Inativo";
            default -> null;
        };
    }

    private String formatCurrency(java.math.BigDecimal value) {
        return java.text.NumberFormat
                .getCurrencyInstance(new Locale("pt", "BR"))
                .format(value);
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
        fireTableDataChanged();
    }
    
    public Employee getEmployeeAt(int rowIndex) {
        return employees.get(rowIndex);
    }
}
