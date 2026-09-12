/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.views;

import com.gustavonascimento.sistema.cadastro.controllers.EmployeeController;
import com.gustavonascimento.sistema.cadastro.controllers.SessionController;
import com.gustavonascimento.sistema.cadastro.models.Employee;
import com.gustavonascimento.sistema.cadastro.session.UserSession;
import com.gustavonascimento.sistema.cadastro.views.table.EmployeeTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Gustavo
 */
public class EmployeeView extends JFrame {
    
    private final JTextField nameField = new JTextField();
    private final JSpinner admissionDateSpinner;
    private final JTextField salaryField = new JTextField();
    private final JCheckBox activeCheckBox = new JCheckBox("Ativo", true);
    private final JButton registerButton = new JButton("Cadastrar Funcionário");
    private final JButton editButton = new JButton("Editar");
    private final JButton deleteButton = new JButton("Excluir");
    private final JButton cancelButton = new JButton("Cancelar");
    
    private final JLabel userLabel = new JLabel();
    private final JButton logoutButton = new JButton("Sair");
    private final SessionController sessionController = new SessionController();

    private Long editingEmployeeId = null;

    private final EmployeeTableModel tableModel = new EmployeeTableModel();
    private final JTable employeeTable = new JTable(tableModel);

    private final EmployeeController controller;

    public EmployeeView() {
        super("Cadastro de Funcionários");
        
        if (!UserSession.getInstance().isLoggedIn()) {
            throw new IllegalStateException(
                "EmployeeView não pode ser aberta sem um usuário autenticado.");
        }
        
        this.controller = new EmployeeController(this);

        this.admissionDateSpinner = criarSpinnerDeData();

        buildLayout();
        
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        cancelButton.setVisible(false);

        editButton.addActionListener(e -> onEditClicked());
        deleteButton.addActionListener(e -> onDeleteClicked());
        cancelButton.addActionListener(e -> exitEditMode());

        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean hasSelection = employeeTable.getSelectedRow() != -1;
                editButton.setEnabled(hasSelection);
                deleteButton.setEnabled(hasSelection);
            }
        });

        registerButton.addActionListener(e -> onRegisterClicked());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setSize(700, 600);
        setLocationRelativeTo(null);

        controller.loadEmployees();
    }

    private JSpinner criarSpinnerDeData() {
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(dateModel);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "dd/MM/yyyy"));
        return spinner;
    }

    private void buildLayout() {
        JPanel root = new JPanel(new BorderLayout(0, 16));
        root.setBorder(new EmptyBorder(24, 24, 24, 24));

        root.add(buildTopBar(), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.add(buildFormPanel(), BorderLayout.NORTH);
        content.add(buildTablePanel(), BorderLayout.CENTER);
        root.add(content, BorderLayout.CENTER);

        setContentPane(root);
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Novo Funcionário");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        title.setBorder(new EmptyBorder(0, 0, 14, 0));
        panel.add(title);

        JPanel fieldsRow = new JPanel(new GridLayout(1, 4, 12, 0));
        fieldsRow.add(campoComRotulo("Nome", nameField));
        fieldsRow.add(campoComRotulo("Admissão", admissionDateSpinner));
        fieldsRow.add(campoComRotulo("Salário (R$)", salaryField));

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        JLabel statusLabel = new JLabel("Status");
        statusLabel.setFont(statusLabel.getFont().deriveFont(12f));
        statusPanel.add(statusLabel);
        statusPanel.add(Box.createVerticalStrut(8));
        statusPanel.add(activeCheckBox);
        fieldsRow.add(statusPanel);

        fieldsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(fieldsRow);

        panel.add(Box.createVerticalStrut(14));

        JPanel buttonsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttonsRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        registerButton.putClientProperty("JButton.buttonType", "roundRect");
        registerButton.setBackground(new Color(0x2563EB));
        registerButton.setForeground(Color.WHITE);
        buttonsRow.add(registerButton);
        buttonsRow.add(cancelButton);

        panel.add(buttonsRow);

        return panel;
    }

    private JPanel campoComRotulo(String rotulo, JComponent campo) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(rotulo);
        label.setFont(label.getFont().deriveFont(12f));

        panel.add(label);
        panel.add(Box.createVerticalStrut(4));
        panel.add(campo);
        return panel;
    }

    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));

        JPanel actionsRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionsRow.add(editButton);
        actionsRow.add(deleteButton);
        panel.add(actionsRow, BorderLayout.NORTH);

        employeeTable.setRowHeight(28);
        employeeTable.setFillsViewportHeight(true);
        panel.add(new JScrollPane(employeeTable), BorderLayout.CENTER);

        return panel;
    }

    private void onRegisterClicked() {
        String name = nameField.getText();

        Date rawDate = (Date) admissionDateSpinner.getValue();
        LocalDate admissionDate = rawDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        BigDecimal salary = parseSalary(salaryField.getText());
        if (salary == null) {
            showError("Informe um valor numérico válido para o salário (ex.: 2500.00).");
            return;
        }

        boolean active = activeCheckBox.isSelected();

        if (editingEmployeeId != null) {
            controller.update(editingEmployeeId, name, admissionDate, salary, active);
        } else {
            controller.register(name, admissionDate, salary, active);
        }
    }

    private BigDecimal parseSalary(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            return null;
        }
        try {
            String normalized = rawValue.trim().replace(",", ".");
            return new BigDecimal(normalized);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void setFormEnabled(boolean enabled) {
        nameField.setEnabled(enabled);
        admissionDateSpinner.setEnabled(enabled);
        salaryField.setEnabled(enabled);
        activeCheckBox.setEnabled(enabled);
        registerButton.setEnabled(enabled);
        editButton.setEnabled(enabled && employeeTable.getSelectedRow() != -1);
        deleteButton.setEnabled(enabled && employeeTable.getSelectedRow() != -1);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public void onEmployeeRegistered() {
        JOptionPane.showMessageDialog(this,
                "Funcionário cadastrado com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
        exitEditMode();
    }

    public void onEmployeeUpdated() {
        JOptionPane.showMessageDialog(this,
                "Funcionário atualizado com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
        exitEditMode();
    }

    public void onEmployeeDeleted() {
        JOptionPane.showMessageDialog(this,
                "Funcionário excluído com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
        exitEditMode();
    }

    public void updateTable(List<Employee> employees) {
        tableModel.setEmployees(employees);
    }
    
    private void onEditClicked() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        Employee employee = tableModel.getEmployeeAt(selectedRow);
        enterEditMode(employee);
    }

    private void enterEditMode(Employee employee) {
        editingEmployeeId = employee.getId();

        nameField.setText(employee.getName());
        salaryField.setText(employee.getSalary().toPlainString());
        activeCheckBox.setSelected(employee.isActive());
        admissionDateSpinner.setValue(Date.from(
                employee.getAdmissionDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        registerButton.setText("Salvar Alterações");
        cancelButton.setVisible(true);
    }

    private void exitEditMode() {
        editingEmployeeId = null;

        nameField.setText("");
        salaryField.setText("");
        activeCheckBox.setSelected(true);
        admissionDateSpinner.setValue(new Date());

        registerButton.setText("Cadastrar Funcionário");
        cancelButton.setVisible(false);
    }

    private void onDeleteClicked() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        Employee employee = tableModel.getEmployeeAt(selectedRow);

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir \"" + employee.getName() + "\"? "
                + "Esta ação não pode ser desfeita.",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacao == JOptionPane.YES_OPTION) {
            controller.delete(employee.getId());
        }
    }
    
    private JPanel buildTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(new EmptyBorder(0, 0, 12, 0));

        String userName = UserSession.getInstance().isLoggedIn()
                ? UserSession.getInstance().getCurrentUser().getName()
                : "—";
        userLabel.setText("Logado como: " + userName);
        userLabel.setFont(userLabel.getFont().deriveFont(Font.PLAIN, 13f));
        userLabel.setForeground(Color.GRAY);
        topBar.add(userLabel, BorderLayout.WEST);

        logoutButton.setBorderPainted(false);
        logoutButton.setContentAreaFilled(false);
        logoutButton.setForeground(new Color(0xDC2626));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> onLogoutClicked());
        topBar.add(logoutButton, BorderLayout.EAST);

        return topBar;
    }
    
    private void onLogoutClicked() {
        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Deseja realmente sair?",
                "Confirmar saída",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmacao == JOptionPane.YES_OPTION) {
            setFormEnabled(false);
            logoutButton.setEnabled(false);

            sessionController.logout(() -> {
                new LoginView().setVisible(true);
                dispose();
            });
        }
    }
    
}
