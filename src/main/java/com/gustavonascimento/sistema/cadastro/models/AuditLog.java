/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.models;

import com.gustavonascimento.sistema.cadastro.models.enums.AuditAction;
import java.time.LocalDateTime;

/**
 *
 * @author Gustavo
 */
public class AuditLog {
    
     private Long id;
    private Long userId;
    private AuditAction action;
    private String details;
    private LocalDateTime createdAt;

    public AuditLog(Long userId, AuditAction action, String details) {
        this.userId = userId;
        this.action = action;
        this.details = details;
    }

    public Long getUserId() { return userId; }
    public AuditAction getAction() { return action; }
    public String getDetails() { return details; }
    
}
