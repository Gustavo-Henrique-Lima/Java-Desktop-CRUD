/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.gustavonascimento.sistema.cadastro;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.gustavonascimento.sistema.cadastro.utils.ThemeDetector;
import com.gustavonascimento.sistema.cadastro.views.LoginView;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author Gustavo
 */
public class Main {

    public static void main(String[] args) {
          try {
            if (ThemeDetector.isWindowsDarkModeEnabled()) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
        } catch (Exception e) {
            System.err.println("Não foi possível aplicar o tema: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }
}
