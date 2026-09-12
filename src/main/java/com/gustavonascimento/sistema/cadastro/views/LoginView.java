/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.views;

import com.gustavonascimento.sistema.cadastro.controllers.LoginController;
import com.gustavonascimento.sistema.cadastro.models.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


/**
 *
 * @author Gustavo
 */
public class LoginView extends JFrame {
    
    private final JTextField emailField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JButton loginButton = new JButton("Entrar");
    private final JButton goToRegisterButton = new JButton("Criar uma conta");

    private final LoginController controller;

    public LoginView() {
        super("Login");
        this.controller = new LoginController(this);

        buildLayout();
        loginButton.addActionListener(e -> onLoginClicked());
        goToRegisterButton.addActionListener(e -> {
            new RegisterView().setVisible(true);
            dispose();
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 420);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void buildLayout() {
        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(new EmptyBorder(36, 40, 36, 40));

        JLabel title = new JLabel("Bem-vindo de volta");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Entre com suas credenciais");
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setBorder(new EmptyBorder(0, 0, 24, 0));

        root.add(title);
        root.add(subtitle);

        root.add(campoComRotulo("E-mail", emailField));
        root.add(Box.createVerticalStrut(14));
        root.add(campoComRotulo("Senha", passwordField));
        root.add(Box.createVerticalStrut(24));

        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        loginButton.putClientProperty("JButton.buttonType", "roundRect");
        loginButton.setBackground(new Color(0x2563EB));
        loginButton.setForeground(Color.WHITE);
        root.add(loginButton);

        root.add(Box.createVerticalStrut(10));

        goToRegisterButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        goToRegisterButton.setBorderPainted(false);
        goToRegisterButton.setContentAreaFilled(false);
        goToRegisterButton.setForeground(new Color(0x2563EB));
        goToRegisterButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        root.add(goToRegisterButton);

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

    private void onLoginClicked() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        controller.login(email, password);
    }

    public void setFormEnabled(boolean enabled) {
        emailField.setEnabled(enabled);
        passwordField.setEnabled(enabled);
        loginButton.setEnabled(enabled);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public void onLoginSuccess(User user) {
        JOptionPane.showMessageDialog(this,
                "Bem-vindo(a), " + user.getName() + "!",
                "Login realizado",
                JOptionPane.INFORMATION_MESSAGE);
        new EmployeeView().setVisible(true);
        dispose();
    }
    
}
