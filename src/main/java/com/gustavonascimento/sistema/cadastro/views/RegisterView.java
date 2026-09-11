/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.views;

import com.gustavonascimento.sistema.cadastro.controllers.RegisterController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 *
 * @author Gustavo
 */
public class RegisterView extends JFrame {
    
    private final JTextField nameField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JButton registerButton = new JButton("Cadastrar");
    private final JButton backToLoginButton = new JButton("Já tenho uma conta");

    private final RegisterController controller;

    public RegisterView() {
        super("Cadastro");
        this.controller = new RegisterController(this);

        buildLayout();
        registerButton.addActionListener(e -> onRegisterClicked());
        backToLoginButton.addActionListener(e -> {
            new LoginView().setVisible(true);
            dispose();
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 480);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void buildLayout() {
        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(new EmptyBorder(36, 40, 36, 40));

        JLabel title = new JLabel("Criar conta");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Preencha os dados abaixo para começar");
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setBorder(new EmptyBorder(0, 0, 24, 0));

        root.add(title);
        root.add(subtitle);

        root.add(campoComRotulo("Nome", nameField));
        root.add(Box.createVerticalStrut(14));
        root.add(campoComRotulo("E-mail", emailField));
        root.add(Box.createVerticalStrut(14));
        root.add(campoComRotulo("Senha", passwordField));
        root.add(Box.createVerticalStrut(24));

        registerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        registerButton.putClientProperty("JButton.buttonType", "roundRect");
        registerButton.putClientProperty("JComponent.focusWidth", 1);
        registerButton.setBackground(new Color(0x2563EB));
        registerButton.setForeground(Color.WHITE);
        root.add(registerButton);

        root.add(Box.createVerticalStrut(10));

        backToLoginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        backToLoginButton.setBorderPainted(false);
        backToLoginButton.setContentAreaFilled(false);
        backToLoginButton.setForeground(new Color(0x2563EB));
        backToLoginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        root.add(backToLoginButton);

        setContentPane(root);
    }

    private JPanel campoComRotulo(String rotulo, JTextField campo) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(rotulo);
        label.setFont(label.getFont().deriveFont(Font.PLAIN, 12f));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));

        panel.add(label);
        panel.add(Box.createVerticalStrut(4));
        panel.add(campo);
        return panel;
    }

    private void onRegisterClicked() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        controller.register(name, email, password);
    }

    public void setFormEnabled(boolean enabled) {
        nameField.setEnabled(enabled);
        emailField.setEnabled(enabled);
        passwordField.setEnabled(enabled);
        registerButton.setEnabled(enabled);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public void onRegisterSuccess() {
        JOptionPane.showMessageDialog(this,
                "Cadastro realizado com sucesso! Faça login para continuar.",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
        new LoginView().setVisible(true);
        dispose();
    }
    
}
