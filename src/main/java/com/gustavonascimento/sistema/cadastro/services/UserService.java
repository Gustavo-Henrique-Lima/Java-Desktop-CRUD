/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavonascimento.sistema.cadastro.services;

import com.gustavonascimento.sistema.cadastro.daos.interfaces.UserDAOInterface;
import com.gustavonascimento.sistema.cadastro.daos.impl.UserDAOIMP;
import com.gustavonascimento.sistema.cadastro.exceptions.PersistenciaException;
import com.gustavonascimento.sistema.cadastro.exceptions.ValidacaoException;
import com.gustavonascimento.sistema.cadastro.infra.PasswordHasher;
import com.gustavonascimento.sistema.cadastro.models.User;
import com.gustavonascimento.sistema.cadastro.utils.Validators;


/**
 *
 * @author Gustavo
 */
public class UserService {

    private final UserDAOInterface userDAO;

    public UserService() {
        this.userDAO = new UserDAOIMP();
    }

    public UserService(UserDAOInterface userDao) {
        this.userDAO = userDao;
    }

    public User save(String name, String email, String password)
            throws ValidacaoException, PersistenciaException {

        Validators.validName(name);
        Validators.validEmail(email);
        Validators.validPassword(password);

        if (userDAO.emailExists(email)) {
            throw new ValidacaoException("Este e-mail já está cadastrado.");
        }

        String salt = PasswordHasher.gerarSalt();
        String passwordHash = PasswordHasher.hash(password, salt);

        User user = new User(name.trim(), email.trim().toLowerCase(), passwordHash, salt);
        return userDAO.save(user);
    }
    
}
