/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author Gustavo
 */
public class ThemeDetector {
    
     private ThemeDetector() {
    }

    public static boolean isWindowsDarkModeEnabled() {
        String os = System.getProperty("os.name", "").toLowerCase();
        if (!os.contains("win")) {
            return false;
        }

        try {
            Process process = Runtime.getRuntime().exec(new String[]{
                "reg", "query",
                "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize",
                "/v", "AppsUseLightTheme"
            });

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("AppsUseLightTheme")) {
                        return line.trim().endsWith("0x0");
                    }
                }
            }
        } catch (Exception e) {
        }
        return false;
    }
}
