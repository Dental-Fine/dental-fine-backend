package com.DentalFine.Dental_Fine_BackEnd.repository;

import com.DentalFine.Dental_Fine_BackEnd.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    UserDetails findByLogin(String login);
}
